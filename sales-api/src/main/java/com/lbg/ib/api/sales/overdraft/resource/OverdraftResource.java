package com.lbg.ib.api.sales.overdraft.resource;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;
import static com.lbg.ib.api.sales.overdraft.mapper.OverdraftMessageMapper.buildE160;
import static com.lbg.ib.api.sales.overdraft.mapper.OverdraftMessageMapper.buildE169;
import static com.lbg.ib.api.sales.overdraft.mapper.OverdraftMessageMapper.buildE170;
import static com.lbg.ib.api.sales.overdraft.mapper.OverdraftMessageMapper.createServiceError;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.asm.dto.C078ResponseDto;
import com.lbg.ib.api.sales.asm.service.C078Service;
import com.lbg.ib.api.sales.cbs.e588.domain.E588Request;
import com.lbg.ib.api.sales.cbs.e588.mapper.E588MessageMapper;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.resource.BaseResource;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.gozo.marker.ServiceConfig;
import com.lbg.ib.api.sales.gozo.marker.MessageTransformer;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.OverdraftAsmDecisionCode;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.OverdraftFulfillmentOperation;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.OverdraftResponseError;
import com.lbg.ib.api.sales.overdraft.domain.AppealedOverdraftDecision;
import com.lbg.ib.api.sales.overdraft.domain.OdFulfillmentRequest;
import com.lbg.ib.api.sales.overdraft.domain.OverdraftManagementResponse;
import com.lbg.ib.api.sales.overdraft.domain.Q122Request;
import com.lbg.ib.api.sales.overdraft.domain.Q122Response;
import com.lbg.ib.api.sales.overdraft.mapper.E160MessageMapper;
import com.lbg.ib.api.sales.overdraft.service.E160Service;
import com.lbg.ib.api.sales.overdraft.service.E169Service;
import com.lbg.ib.api.sales.overdraft.service.E170Service;
import com.lbg.ib.api.sales.overdraft.service.Q122Service;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.markers.ServiceExceptionManager;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.shared.validator.StringValidator;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.PartyInformation;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.Account;

@Path("/overdraft")
public class OverdraftResource extends BaseResource {

	@Autowired
	private ModuleContext beanLoader;

	@Autowired
	private AccountInContextUtility contextUtility;

	@Autowired
	private LoggerDAO logger;

	@Autowired
	private C078Service service;

	private static final ArrayList<Integer> validCreditScoreResultCodes;

	static {
		validCreditScoreResultCodes = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
			}
		};
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Path("/fulfillment")
	@TraceLog
	@ServiceExceptionManager
	public Response fulfill(OdFulfillmentRequest clientRequest) {
		FieldValidator fieldValidator = beanLoader.getService(FieldValidator.class);
		ValidationError validationError = fieldValidator.validateInstanceFields(clientRequest);
		if (validationError != null) {
			throw new InvalidFormatException(validationError.getMessage());
		}
		Account account = contextUtility.getSelectedAccountDetail();
		Double existingOverdraft = account.getOverdraftLimit();
		Double demandedOverdraft = Double.valueOf(clientRequest.getDemandedOd());
		Double existingBalance = account.getAvailableBal();
		Double overdraftOffset = existingOverdraft - demandedOverdraft;

		fulfilmentRequestSplunkLogging(account, existingOverdraft, demandedOverdraft, existingBalance, overdraftOffset);
		ResponseError error = validateOverdraftOperation(existingOverdraft, demandedOverdraft, existingBalance);
		if (error != null) {
			return respond(Status.OK, error);
		}
		OverdraftFulfillmentOperation opToInvoke = determineOperationToInvoke(existingOverdraft, demandedOverdraft);
		logger.traceLog(this.getClass(), "Invoking the operation " + opToInvoke.toString());
		OverdraftManagementResponse clientResponse = invokeOperations(opToInvoke, clientRequest);

		Double updatedAvailableBalance = existingBalance - overdraftOffset;
		contextUtility.updateOdLimitInSelectedAccount(demandedOverdraft);
		contextUtility.updateAvailableBalanceInSelectedAccount(updatedAvailableBalance);

		clientResponse.setOverdraftLimit(String.valueOf(Math.round(demandedOverdraft)));
		clientResponse.setServiceMessage(opToInvoke.successResponse());

		fulfilmentResponseSplunkLogging(clientRequest, clientResponse);

		return respond(Status.OK, clientResponse);
	}

	private OverdraftFulfillmentOperation determineOperationToInvoke(Double existingOverdraft,
			Double demandedOverdraft) {
		OverdraftFulfillmentOperation opToInvoke = null;
		if (OverdraftConstants.ZERO.equals(demandedOverdraft)) {
			opToInvoke = OverdraftFulfillmentOperation.E160;
		} else if (OverdraftConstants.ZERO.equals(existingOverdraft)) {
			opToInvoke = OverdraftFulfillmentOperation.E169;
		} else {
			opToInvoke = OverdraftFulfillmentOperation.E170;
		}
		return opToInvoke;
	}

	private ResponseError validateOverdraftOperation(final Double existingOverdraft, final Double demandedOverdraft,
			final Double availableBalance) {
		ResponseError error = null;
		Double offset = existingOverdraft - demandedOverdraft;
		if (OverdraftConstants.ZERO.equals(offset)) {
			error = createServiceError(OverdraftResponseError.SAME_OVERDRAFT_LIMIT_ERROR);
		} else if (offset < 0) {
			Double otb = getOTB();
			if (demandedOverdraft > otb) {
				error = createServiceError(OverdraftResponseError.EXCEEDING_OVERDRAFT_LIMIT_ERROR);
			}
		} else {
			Double balance = availableBalance - offset;
			if (balance < 0) {
				error = createServiceError(OverdraftResponseError.NEGATIVE_AVAILABLE_BALANCE_ERROR);
			}
		}
		return error;
	}

	private OverdraftManagementResponse invokeOperations(OverdraftFulfillmentOperation operation,
			OdFulfillmentRequest clientRequest) {
		Account account = contextUtility.getSelectedAccountDetail();
		if (OverdraftFulfillmentOperation.E160 == operation) {
			E160Service e160Service = beanLoader.getService(E160Service.class);
			return e160Service.invokeE160(buildE160(account));
		} else if (OverdraftFulfillmentOperation.E169 == operation) {
			E169Service e169Service = beanLoader.getService(E169Service.class);
			return e169Service.invokeE169(buildE169(clientRequest, account));
		} else {
			E170Service e170Service = beanLoader.getService(E170Service.class);
			return e170Service.invokeE170(buildE170(clientRequest, account));
		}
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Path("/decision")
	@TraceLog
	@ServiceExceptionManager
	public Response decision(Q122Request clientRequest) {
		validateRequest(clientRequest);
		Q122Service q122Service = beanLoader.getService(Q122Service.class);
		Q122Response serviceResponse = q122Service.invokeQ122(clientRequest, ApplicationType.A008.getValue());
		contextUtility.setDemandedOverdraft(Double.valueOf(clientRequest.getDemandedOd()));
		contextUtility.setCreditRequestReferenceNumber(serviceResponse.getCreditScoreReference());
		decisionSplunkLogging(clientRequest, serviceResponse);
		return respond(Status.OK, serviceResponse);

	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Path("/decision/v2")
	@TraceLog
	@ServiceExceptionManager
	public Response decisionv2(Q122Request clientRequest) {
		validateRequest(clientRequest);
		Q122Service q122Service = beanLoader.getService(Q122Service.class);
		Q122Response serviceResponse = q122Service.invokeQ122(clientRequest, ApplicationType.A034.getValue());
		contextUtility.setDemandedOverdraft(Double.valueOf(clientRequest.getDemandedOd()));
		contextUtility.setCreditRequestReferenceNumber(serviceResponse.getCreditScoreReference());
		decisionSplunkLogging(clientRequest, serviceResponse);
		return respond(Status.OK, serviceResponse);
	}

	private void validateRequest(Q122Request clientRequest) {
		ValidationError error = validateQ122Request(clientRequest);
		if (error != null) {
			throw new InvalidFormatException(error.getMessage());
		}
		// we have to call RPD because userInfo does not return start and end
		// date for the current Address
		RetrievePartyDetailsService partyDetailService = beanLoader.getService(RetrievePartyDetailsService.class);
		PartyDetails primaryPartyFromRPD = partyDetailService.getPartyDetails(contextUtility.getPrimaryPartyOcisId());
		PartyDetails primaryParty = contextUtility.getPrimaryPartyDetails();
		logPartyDetails(primaryParty);
		primaryParty.setAddressEndDate(primaryPartyFromRPD.getAddressStartDate());
		primaryParty.setAddressStartDate(primaryPartyFromRPD.getAddressStartDate());
	}

	private Double getOTB() {
		Double offeredOd = contextUtility.getMaximumOverdraftLimit();
		logger.traceLog(this.getClass(), "Overdraft Limit Offered by Q122 is " + offeredOd);
		if (offeredOd == null) {
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable"));
		}
		logger.traceLog(this.getClass(), "OTB is == " + offeredOd);
		return (Double) offeredOd;
	}

	private ValidationError validateQ122Request(Q122Request clientRequest) {
		boolean isPrimaryPartyDetailsPresent = false;
		List<PartyInformation> partyList = clientRequest.getParties();
		boolean isOverdraftValid = StringValidator.validateFieldPattern(REQUIRED_NUMERIC,
				clientRequest.getDemandedOd());
		if (!isOverdraftValid) {
			return new ValidationError("Invalid Demanded Overdraft");
		}
		if (!CollectionUtils.isEmpty(partyList)) {
			for (PartyInformation partyInformation : partyList) {
				if (partyInformation.isPrimary()) {
					isPrimaryPartyDetailsPresent = true;
					break;
				}
			}
			if (!isPrimaryPartyDetailsPresent) {
				return new ValidationError("Primary Party Information is missing");
			}
		}
		return null;
	}

	private void logPartyDetails(PartyDetails party) {
		StringBuilder builder = new StringBuilder();
		builder.append("Party Details for Q122 :- ");
		builder.append("Address Start Date is " + party.getAddressStartDate() + "\t");
		builder.append("Address End Date is " + party.getAddressEndDate() + "\t");
		builder.append("Email address is " + party.getEmail() + "\t");
		logger.traceLog(this.getClass(), builder.toString());
	}

	private void decisionSplunkLogging(Q122Request request, Q122Response response) {
		StringBuilder builder = new StringBuilder();
		Account account = contextUtility.getSelectedAccountDetail();
		builder.append("Credit Assessment Result \t");
		builder.append("Selected Account Number=" + account.getAccountNumber() + "\t");
		builder.append("SortCode=" + account.getSortCode() + "\t");
		builder.append("Existing OD Limit=" + account.getOverdraftLimit() + "\t");
		builder.append("Available Balance=" + account.getAvailableBal() + "\t");
		builder.append("Demanded Overdraft=" + request.getDemandedOd() + "\t");
		builder.append("Credit Score reference number=" + response.getCreditScoreReference() + "\t");
		builder.append("Q122 Decision code=" + response.getCreditScoreResultCode() + "\t");
		builder.append("Q122 Decision value=" + response.getOverdraftDecision() + "\t");
		builder.append("Q122 Affordable amount=" + response.getAffordableAmount());
		builder.append("Maximum overdraft Limit=" + contextUtility.getMaximumOverdraftLimit() + "\t");
		builder.append("UFE=" + contextUtility.getMaxOfferedByAsm());
		logger.traceLog(this.getClass(), builder.toString());
	}

	private void fulfilmentResponseSplunkLogging(OdFulfillmentRequest request, OverdraftManagementResponse response) {
		StringBuilder builder = new StringBuilder();
		Account account = contextUtility.getSelectedAccountDetail();
		builder.append("Fulfilment Response \t");
		builder.append("Selected Account Number= " + account.getAccountNumber() + "\t");
		builder.append("SortCode= " + account.getSortCode() + "\t");
		builder.append("Updated Balance= " + account.getAvailableBal() + "\t");
		builder.append("Updated Overdraft Limit= " + account.getOverdraftLimit());
		logger.traceLog(this.getClass(), builder.toString());
	}

	private void fulfilmentRequestSplunkLogging(Account account, Double existingOverdraft, Double demandedOverdraft,
			Double existingBalance, Double overdraftOffset) {
		StringBuilder builder = new StringBuilder();
		builder.append("Fulfilment Request Details \t");
		builder.append("Selected Account Number=" + account.getAccountNumber() + "\t");
		builder.append("SortCode=" + account.getSortCode() + "\t");
		builder.append("Existing Overdraft=" + existingOverdraft + "\t");
		builder.append("Demanded Overdraft=" + demandedOverdraft + "\t");
		builder.append("Existing Balance=" + existingBalance + "\t");
		builder.append("Offset=" + overdraftOffset);
		logger.traceLog(this.getClass(), builder.toString());
	}

	@GET
	@Produces(APPLICATION_JSON)
	@Path("/appeal")
	@ServiceExceptionManager
	@TraceLog
	public Response fetchAppScoreDecisionFromA008() {
		return respond(Response.Status.OK, getPldDecision(service.invokeC078(ApplicationType.A008)));
	}

	@GET
	@Produces(APPLICATION_JSON)
	@Path("/appeal/v2")
	@ServiceExceptionManager
	@TraceLog
	public Response fetchAppScoreDecisionFromA034() {
		return respond(Response.Status.OK, getPldDecision(service.invokeC078(ApplicationType.A034)));
	}

	public AppealedOverdraftDecision getPldDecision(C078ResponseDto response) {
		AppealedOverdraftDecision pldDecision = new AppealedOverdraftDecision();
		pldDecision.setAsmDecisions(response.getAsmDecisions());
		pldDecision.setCreditScoreResultCode(response.getCreditScore());
		pldDecision.setCreditScoreReference(response.getCreditRequestReferenceNumber());
		OverdraftAsmDecisionCode decisionValue = deriveAppealDecision(Integer.valueOf(response.getCreditScore()));
		pldDecision.setOverdraftDecision(decisionValue.stringValue());
		Double affordableAmount = getApprovedAmount(decisionValue);
		contextUtility.setMaximumOverdraftLimit(affordableAmount);
		pldDecision.setAffordableAmount(String.valueOf(Math.round(affordableAmount)));
		return pldDecision;
	}

	private OverdraftAsmDecisionCode deriveAppealDecision(Integer creditResult) {
		if (!validCreditScoreResultCodes.contains(creditResult)) {
			logger.traceLog(this.getClass(),
					"Unknown PLD decision, Accepted values are only accept(1), refer(2) and decline(3) "
							+ creditResult);
			throw new ServiceException(
					new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable"));
		}
		return OverdraftAsmDecisionCode.deriveFromValue(creditResult);
	}

	private Double getApprovedAmount(OverdraftAsmDecisionCode decision) {
		if (OverdraftAsmDecisionCode.ACCEPT == decision) {
			// In Pound
			return contextUtility.getDemandedOverdraft();
		} else if (decision == OverdraftAsmDecisionCode.REFER) {
			// Amount in session is already either the down sold amount or zero for refer
			// Amount is already stored in pound
			return contextUtility.getMaximumOverdraftLimit();
		} else {
			// contextUtility.setMaximumOverdraftLimit(maximumOverdraftLimit);
			return OverdraftConstants.ZERO;
		}
	}

	@POST
	@Produces(APPLICATION_JSON)
	@Path("/RollerTest")
	@ServiceExceptionManager
	@TraceLog
	@ServiceConfig("/service-config/e588-api-conf")
	@MessageTransformer(E588MessageMapper.class)
	public Response testApi(E588Request clientRequest) {
		return respond(Response.Status.OK);
	}
}
