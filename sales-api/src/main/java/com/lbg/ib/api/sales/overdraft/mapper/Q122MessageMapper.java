package com.lbg.ib.api.sales.overdraft.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.lbg.ib.api.sales.common.validation.BinaryType;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.EmploymentStatusCode;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.IncomeFrequency;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.OverdraftAsmDecisionCode;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.OverdraftLimit;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.OverdraftPurposeCode;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.PartyBusnessRelationship;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.PostalAddressCodes;
import com.lbg.ib.api.sales.overdraft.constant.OverdraftConstants.ProductFamilyIdentifier;
import com.lbg.ib.api.sales.overdraft.domain.AsmDecision;
import com.lbg.ib.api.sales.overdraft.domain.Q122Response;
import com.lbg.ib.api.sales.overdraft.dto.IeCustomerDetailsDto;
import com.lbg.ib.api.sales.overdraft.dto.Q122DTO;
import com.lbg.ib.api.sales.shared.util.CalendarUtility;
import com.lbg.ib.api.sales.shared.util.domain.TimeDifference;
import com.lbg.ib.api.sales.user.dto.AddressDto;
import com.lbg.ib.api.sales.user.dto.PartyDto;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lloydsbanking.xml.AddressDetailsWithoutResolution;
import com.lloydsbanking.xml.ApplicationDetails;
import com.lloydsbanking.xml.DecisionDetails;
import com.lloydsbanking.xml.IncomeExpenditureDetails;
import com.lloydsbanking.xml.PartyIdentifiers;
import com.lloydsbanking.xml.PersonalDetails;
import com.lloydsbanking.xml.Q122Req;
import com.lloydsbanking.xml.Q122Resp;
import com.lloydsbanking.xml.RequestDetails;
import com.lloydsbanking.xml.UnformattedAddress;

public class Q122MessageMapper {

	private static final String EXTERNAL_SYSTEM_ID_CBS = "4";
	private static final String EXTERNAL_SYSTEM_ID_OCIS = "3";

	public Q122Req buildQ122Request(Q122DTO clientRequest, String creditScoreSourceSystemCd) {
		Q122Req q122Request = new Q122Req();
		q122Request.setMaxRepeatGroupQy(0);
		q122Request.setRequestDetails(setRequestDetails(clientRequest,creditScoreSourceSystemCd));
		q122Request.setApplicationDetails(setApplicationDetails(clientRequest));
		q122Request.setPersonalDetails(setPersonalDetails(clientRequest));
		return q122Request;
	}

	public Q122Response buildQ122Response(Q122Req serviceRequest, Q122Resp serviceResponse) {
		Q122Response q122Response = new Q122Response();
		q122Response.setAdditionalDetailIndicator(serviceResponse.getAdditionalDataIn());
		q122Response.setCreditScoreReference(serviceRequest.getRequestDetails().getCreditScoreRequestNo());
		q122Response.setCreditScoreResultCode(serviceResponse.getASMCreditScoreResultCd());
		q122Response.setCreditScoreId(serviceResponse.getCreditScoreId());
		List<AsmDecision> asmDecisions = new LinkedList<AsmDecision>();
		List<DecisionDetails> decisionList = Arrays.asList(serviceResponse.getDecisionDetails());
		for (DecisionDetails decisionDetail : decisionList) {
			AsmDecision decision = new AsmDecision();
			decision.setDecision(decisionDetail.getCSDecisnReasonTypeNr());
			decision.setDecisionCode(decisionDetail.getCSDecisionReasonTypeCd());
			asmDecisions.add(decision);
		}
		q122Response.setAsmDecisions(asmDecisions);
		q122Response.setAffordableAmount(convertToPound(serviceResponse.getAffordableAm()));
		OverdraftAsmDecisionCode decisionValue = deriveDecision(serviceResponse);
		q122Response.setOverdraftDecision(decisionValue.stringValue());
		return q122Response;
	}

	private RequestDetails setRequestDetails(Q122DTO clientRequest, String creditScoreSourceSystemCd) {
		RequestDetails details = new RequestDetails();
		String primaryPartyOcisId = clientRequest.getPartiesInformation().get(0).getOcisId();
		String currentTimestamp = CalendarUtility.formatDate(CalendarUtility.DD_MM_YY_HH_MM,
				CalendarUtility.getCurrentDate());
		String creditScoreRequestNumber = currentTimestamp.concat(primaryPartyOcisId);
		int maxLimitOfCreditScoreRequestNumber = creditScoreRequestNumber.length() < 20
				? creditScoreRequestNumber.length()
				: 20;
		details.setCreditScoreRequestNo(creditScoreRequestNumber.substring(0, maxLimitOfCreditScoreRequestNumber));
		details.setCreditScoreSourceSystemCd(returnDefaultIfValueIsNull(clientRequest.getCreditScoreSourceSystemCd(),creditScoreSourceSystemCd));
		details.setApplicationSourceCd(OverdraftConstants.APPLICATION_SOURCE_CODE);
		details.setSortCd(clientRequest.getSelectedAccount().getSortCode());
		return details;
	}

	private ApplicationDetails setApplicationDetails(Q122DTO clientRequest) {
		Account accountInContext = clientRequest.getSelectedAccount();
		ApplicationDetails details = new ApplicationDetails();
		details.setCSOrganisationCd(OverdraftConstants.BANK_ORGANIZATION_CODE); // For Lloyds, Halifax and BOS
		details.setAddressTargetingIn(BinaryType.Y.binaryString());
		details.setCurrencyCd(OverdraftConstants.DEFAULT_CURRENCY_CODE);
		String productId = findProductFamilyId(clientRequest.getChannelDto().getBrand());
		details.setProductId(Integer.valueOf(productId));
		details.setSortCd(accountInContext.getSortCode());
		details.setAccountNo(accountInContext.getAccountNumber()); // optional
		details.setReqstdOvrdrtLimitAm(clientRequest.getDemandedOd());
		Date currentDate = CalendarUtility.getCurrentDate();
		details.setOverdraftStartDt(CalendarUtility.formatDate(CalendarUtility.DD_MM_YYYY_FORMAT, currentDate));
		details.setOverdraftEndDt(CalendarUtility.formatDate(CalendarUtility.DD_MM_YYYY_FORMAT,
				CalendarUtility.getRelativeDate(0, 0, 1)));
		details.setOverdraftLimitTypeCd(returnDefaultIfValueIsNull(clientRequest.getOverdraftLimitType(),
				OverdraftLimit.PERMANENT.stringValue()));
		details.setOverdraftPurposeCd(returnDefaultIfValueIsNull(clientRequest.getOverdraftPurpose(),
				OverdraftPurposeCode.NOT_KNOWN.stringValue()));
		details.setExistgOvrdrtLimitAm(String.valueOf(Math.round(accountInContext.getOverdraftLimit())));
		return details;
	}

	private PersonalDetails[] setPersonalDetails(Q122DTO dto) {
		List<PersonalDetails> partyList = new ArrayList<PersonalDetails>();
		for (PartyDto party : dto.getPartiesInformation()) {
			PersonalDetails personalDetail = new PersonalDetails();
			personalDetail.setFirstForeNm(party.getName().getFirstName());
			personalDetail.setSurname(party.getName().getLastName());
			personalDetail.setEmploymentStatusCd(returnDefaultIfValueIsNull(party.getEmploymentStatus(),
					EmploymentStatusCode.EMPLOYED.stringValue()));
			personalDetail.setPartyBusinessRltnspCd(PartyBusnessRelationship.FRANCHISED.stringValue());
			String dob = party.getBirthDate();
			String formattedDob = CalendarUtility.formatDate(CalendarUtility.DD_MM_YYYY_FORMAT,
					CalendarUtility.parseDate(CalendarUtility.YYYY_MM_DD, dob));
			personalDetail.setBirthDt(formattedDob);
			personalDetail.setPartyId(Integer.valueOf(party.getOcisId()));
			boolean ieIndicator = party.isIeIndicator();
			String ieIndicatorString = ieIndicator ? BinaryType.Y.binaryString() : BinaryType.N.toString();
			personalDetail.setIncomeExpenditureIn(ieIndicatorString);
			if (ieIndicator) {
				personalDetail.setIncomeExpenditureDetails(setIncomeExpenditureDetails(party.getIeDetails()));
			}
			personalDetail.setAddressDetailsWithoutResolution(setAddressDetailsWithoutResolution(party.getAddress()));
			personalDetail.setOtstngOvrdrtApplcnQyAm(Short.valueOf("0"));
			List<PartyIdentifiers> identifier = new LinkedList<PartyIdentifiers>();

			if (StringUtils.isNotEmpty(party.getPartyId())) {
				identifier.add(partyIdentifier(Short.valueOf(EXTERNAL_SYSTEM_ID_OCIS), party.getPartyId()));
			}
			if (StringUtils.isNotEmpty(party.getCbsCustomerNumber())) {
				identifier.add(partyIdentifier(Short.valueOf(EXTERNAL_SYSTEM_ID_CBS), party.getCbsCustomerNumber()));
			}
			if (CollectionUtils.isNotEmpty(identifier)) {
				personalDetail.setPartyIdentifiers(identifier.toArray(new PartyIdentifiers[identifier.size()]));
			}
			partyList.add(personalDetail);
		}
		return partyList.toArray(new PersonalDetails[partyList.size()]);
	}

	private IncomeExpenditureDetails setIncomeExpenditureDetails(IeCustomerDetailsDto ieDetails) {
		IncomeExpenditureDetails details = new IncomeExpenditureDetails();
		details.setMnthlyAccmmnPaymntAm(convertToPence(ieDetails.getAccommodationExpenses()));
		details.setMnthlyTtlOthMjrCmtAm(convertToPence(ieDetails.getMonthlyExpenses()));
		details.setPeriodNetIncomeAm(ieDetails.getMonthlyIncome());
		details.setPeriodNetIncomeFrqncyCd(
				returnDefaultIfValueIsNull(ieDetails.getIncomeFrequency(), IncomeFrequency.MONTHLY.stringValue()));
		return details;
	}

	private AddressDetailsWithoutResolution[] setAddressDetailsWithoutResolution(AddressDto address) {
		List<AddressDetailsWithoutResolution> addressDetails = new LinkedList<AddressDetailsWithoutResolution>();
		AddressDetailsWithoutResolution details = new AddressDetailsWithoutResolution();
		details.setPostalAddressTypeCd(PostalAddressCodes.UK_PAF.stringValue());
		details.setPostCd(address.getPostCode());
		details.setAddressResidenceDr(
				Short.valueOf(returnDefaultIfValueIsNull(address.getDurationOfStay(), getDurationOfStay(address))));
		details.setFormattedAddressIn(BinaryType.N.binaryString());
		details.setUnformattedAddressIn(BinaryType.Y.binaryString());
		details.setUnformattedAddress(buildUnstructuredAddress(address));
		addressDetails.add(details);
		return addressDetails.toArray(new AddressDetailsWithoutResolution[addressDetails.size()]);
	}

	private String getDurationOfStay(AddressDto address) {
		Date addressStartDate = address.getAddressStartDate();
		if (addressStartDate == null) {
			return "0300";
		}
		Date addressEndDate = address.getAddressEndDate();
		if (addressEndDate == null) {
			addressEndDate = new Date();
		}
		TimeDifference timeDiff = CalendarUtility.getDifference(addressStartDate, addressEndDate);
        // Default to 1 month if user has just started living at the current address
        if(timeDiff.getMonths()== 0 && timeDiff.getYears()==0) {
               return "0001";
        }
        String months = append(String.valueOf(timeDiff.getMonths()));
        String years = append(String.valueOf(timeDiff.getYears()));
        return years.concat(months);
	}

	private UnformattedAddress buildUnstructuredAddress(AddressDto address) {
		List<String> addressLines = new LinkedList<String>(address.getAddressLines());
		UnformattedAddress unformattedAddress = new UnformattedAddress();
		unformattedAddress.setUnstrdPostalAddrssTypeCd(
				returnDefaultIfValueIsNull(address.getPostalAddressType(), PostalAddressCodes.UK_PAF.stringValue()));
		int startIndex = addressLines.size();
		if (startIndex < 4) {
			for (int index = startIndex; index < 4; index++) {
				addressLines.add(index, "");
			}
		}
		unformattedAddress.setAddressLine1Tx(addressLines.get(0));
		unformattedAddress.setAddressLine2Tx(addressLines.get(1));
		unformattedAddress.setAddressLine3Tx(addressLines.get(2));
		unformattedAddress.setAddressLine4Tx(addressLines.get(3));
		return unformattedAddress;
	}

	private String returnDefaultIfValueIsNull(String value, String defaultValue) {
		return StringUtils.isEmpty(value) ? defaultValue : value;
	}

	private String append(String value) {
		String newValue = value;
		if (value.length() == 1) {
			newValue = "0".concat(value);
		}
		return newValue;
	}

	private OverdraftAsmDecisionCode deriveDecision(Q122Resp q122Response) {
		Double maximumAllowedOverdraft = Double.valueOf(q122Response.getAffordableAm());
		Integer creditResult = Integer.valueOf(q122Response.getASMCreditScoreResultCd());
		if (creditResult == 2 && maximumAllowedOverdraft > 0) {
			creditResult = 4;
		}
		return OverdraftAsmDecisionCode.deriveFromValue(creditResult);
	}

	private String findProductFamilyId(String channelId) {
		ProductFamilyIdentifier productFamilyIdentifier = OverdraftConstants.ProductFamilyIdentifier.valueOf(channelId);
		return productFamilyIdentifier.stringValue();
	}

	private String convertToPence(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		Integer pence = 100 * Integer.valueOf(value);
		return String.valueOf(pence);
	}

	private String convertToPound(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		Double pence = Double.valueOf(value);
		long pound = Math.round(pence / 100);
		return String.valueOf(pound);
	}

	private PartyIdentifiers partyIdentifier(Short externalSystemId, String partyId) {
		PartyIdentifiers identifier = new PartyIdentifiers();
		identifier.setExtPartyIdTx(partyId);
		identifier.setExtSysId(externalSystemId);
		return identifier;
	}
}
