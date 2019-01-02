package com.lbg.ib.api.sales.asm.service;
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.APPLICATION_SOURCE_CD_BRANCH;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.APPLICATION_SOURCE_CD_DIGITAL;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.CS_ORGANISATION_CD;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.MAX_REPEAT_GROUP_QTY;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.formatter;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.asm.dto.C078RequestDto;
import com.lbg.ib.api.sales.asm.dto.C078ResponseDto;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.mapper.C078RequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.markers.HeaderRegistry;
import com.lbg.ib.api.sales.header.markers.PcaSoapHeaders;
import com.lbg.ib.api.sales.overdraft.domain.AsmDecision;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.www.C078Req;
import com.lloydstsb.www.C078Resp;
import com.lloydstsb.www.C078_Enq_Decision_Request_PortType;
import com.lloydstsb.www.C078_Enq_Decision_Request_ServiceLocator;
import com.lloydstsb.www.DecisionDetails;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018. The class is created to
 * work as a Service for C078 resource to fetch App Score information
 */
@Component
public class C078ServiceImpl extends SoaAbstractService implements C078Service {

	private static final String SERVICE_METHOD = "c078";

	private static final String SERVICE_ACTION = "J078";

	private static final String SERVICE_NAME = "http://www.lloydstsb.com/Schema/Personal/LendingServicePlatform/ASM/C078_Enq_Decision_Request";

	private static final Integer NUMBER_OF_RETRIES = 2;

	@Autowired
	private SessionManagementDAO sessionManager;

	@Autowired
	private ModuleContext beanLoader;

	@Autowired
	private LoggerDAO logger;

	@Autowired
	private C078RequestMapper requestMapper;

	@Autowired
	private ServiceErrorValidator serviceErrorMapper;

	@Autowired
	private AccountInContextUtility contextUtility;

	/*
	 * The invokeC078 is used to invoke the CO78 to fetch the App Score response
	 * 
	 * @param AppScoreRequest
	 * 
	 * @return The C078Resp
	 */
	public C078Resp invokeC078(AppScoreRequest appScoreRequest) {
		final C078_Enq_Decision_Request_ServiceLocator serviceLocator = beanLoader
				.getService(C078_Enq_Decision_Request_ServiceLocator.class);
		setDataHandler(new QName(serviceLocator.getC078_Enq_Decision_RequestWSDDPortName()),
				serviceLocator.getHandlerRegistry());
		final ClientContext clientContext = sessionManager.getUserContext().toClientContext();
		setSoapHeader(clientContext, SERVICE_ACTION, SERVICE_NAME, true);

		final C078Req request = requestMapper.create(appScoreRequest);

		C078Resp response = null;
		Integer attempts = 0;
		while (true) {
			try {
				response = (C078Resp) invoke(SERVICE_METHOD, request);
				return response;
			} catch (ServiceException serviceException) {
				if (attempts == NUMBER_OF_RETRIES) {
					logger.traceLog(this.getClass(), "Maximum number of retrying attempts reached");
					throw serviceException;
				}
				attempts++;
				logger.traceLog(this.getClass(), "C078 : Failed due to "
						+ serviceException.getResponseError().getMessage() + " Retrying Attempt: " + attempts);
			}

		}
	}

	@PcaSoapHeaders(headers = { ContactPointHeader.class, ServiceRequestHeader.class,
			SecurityHeader.class }, serviceName = "C078", serviceAction = "C078")
	@HeaderRegistry(portNameMethod = "getC078_Enq_Decision_RequestWSDDPortName", serviceLocator = C078_Enq_Decision_Request_ServiceLocator.class)
	public C078ResponseDto invokeC078(ApplicationType applicationType) {
		C078RequestDto requestDto = buildC078RequestDto(applicationType);
		C078Req request = C078RequestMapper.buildC078Request(requestDto);
		C078Resp response = (C078Resp) invoke(SERVICE_METHOD, request);
		serviceErrorMapper.validateResponseError(response.getC078Result().getResultCondition());
		C078ResponseDto responseDto = buildC078ResponseDto(request, response);
		return responseDto;
	}

	public Class<?> getPort() {
		return C078_Enq_Decision_Request_PortType.class;
	}

	public C078RequestDto buildC078RequestDto(ApplicationType applicationType) {
		C078RequestDto request = new C078RequestDto();
		request.setMaxRepeatGroupQuantity(MAX_REPEAT_GROUP_QTY);
		request.setProcessDate(formatter.format(new Date()));
		request.setOrganisationCode(CS_ORGANISATION_CD);
		request.setCreditScoreSourceSystemCode(getCreditScoreSourceSystemCode(applicationType));
		request.setCreditScoreRequestNo(contextUtility.getCreditRequestReferenceNumber());
		request.setApplicationSourceCode(getApplicationSourceCode());
		return request;
	}

	public static C078ResponseDto buildC078ResponseDto(C078Req soapRequest, C078Resp soapResponse) {
		C078ResponseDto c078ResponseDto = new C078ResponseDto();
		List<AsmDecision> asmDecisions = new LinkedList<AsmDecision>();
		List<DecisionDetails> decisionList = Arrays.asList(soapResponse.getDecisionDetails());
		for (DecisionDetails decisionDetail : decisionList) {
			AsmDecision decision = new AsmDecision();
			decision.setDecision(decisionDetail.getCSDecisnReasonTypeNr());
			decision.setDecisionCode(decisionDetail.getCSDecisionReasonTypeCd());
			asmDecisions.add(decision);
		}
		c078ResponseDto.setAsmDecisions(asmDecisions);
		c078ResponseDto.setCreditScore(soapResponse.getASMCreditScoreResultCd());
		c078ResponseDto.setAdditionalDetailIndicator(soapResponse.getAdditionalDataIn());
		c078ResponseDto.setCreditRequestReferenceNumber(soapRequest.getCreditScoreRequestNo());
		c078ResponseDto.setCreditSystem(soapRequest.getCreditScoreSourceSystemCd());
		return c078ResponseDto;
	}

	private String getCreditScoreSourceSystemCode(ApplicationType applicationType) {
		String creditSourceSytemCode = contextUtility.getCreditScoreSourceSystemCd();
		if (StringUtils.isEmpty(creditSourceSytemCode)) {
			switch (applicationType) {
			case A034:
				creditSourceSytemCode = ApplicationType.A034.getValue();
				break;
			case A008:
				creditSourceSytemCode = ApplicationType.A008.getValue();
				break;
			default:
				throw new ServiceException(
						new ResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable"));
			}
		}
		return creditSourceSytemCode;
	}

	private String getApplicationSourceCode() {
		if (sessionManager.getBranchContext() != null) {
			return APPLICATION_SOURCE_CD_BRANCH;
		} else {
			return APPLICATION_SOURCE_CD_DIGITAL;
		}
	}
}
