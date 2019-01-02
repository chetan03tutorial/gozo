package com.lbg.ib.api.sales.asm.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.dao.mapper.C078RequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.AccountInContextUtility;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.www.C078Req;
import com.lloydstsb.www.C078Resp;
import com.lloydstsb.www.C078Result;
import com.lloydstsb.www.C078_Enq_Decision_Request_ServiceLocator;
import com.lloydstsb.www.DecisionDetails;

/**
 * Created by Debashish on 29/05/2018 for Test Case Coverage of C078 Service
 */

@RunWith(MockitoJUnitRunner.class)
public class C078ServiceImplTest {

	@Mock
	private SessionManagementDAO sessionManager;

	@Mock
	private ModuleContext beanLoader;

	@Mock
	private LoggerDAO logger;

	@Mock
	private GalaxyErrorCodeResolver resolver;

	@Mock
	private C078_Enq_Decision_Request_ServiceLocator serviceLocator;

	@Mock
	private HandlerRegistry handleRegistry;

	@Mock
	private SoapHeaderGenerator soapHeaderGenerator;

	@Mock
	private C078RequestMapper requestMapper = new C078RequestMapper();

	@Mock
	private SOAInvoker soaInvoker;

	@Mock
	private ServiceErrorValidator serviceErrorValidator;

	@Mock
	private AccountInContextUtility contextUtility;

	private static final String brand = "LTB";

	@InjectMocks
	private C078ServiceImpl serviceImpl;

	C078Req req = new C078Req();

	@Before
	public void setup() {
		when(serviceLocator.getC078_Enq_Decision_RequestWSDDPortName()).thenReturn("c078");
		when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
		when(serviceLocator.getServiceName()).thenReturn(new QName("c078"));
		when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
		when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString()))
				.thenReturn(HeaderServiceUtil.genericHeaderData());
		when(beanLoader.getService(C078_Enq_Decision_Request_ServiceLocator.class)).thenReturn(serviceLocator);
		when(contextUtility.getCreditRequestReferenceNumber()).thenReturn("20DIGITREFERENCE");
		doNothing().when(serviceErrorValidator).validateResponseError(any(ResultCondition.class));

	}

	/*
	 * @Test public void testInvokeC078WithSuccessfulResponse() { AppScoreRequest
	 * appScoreRequest = new AppScoreRequest();
	 * appScoreRequest.setCreditScoreRequestNo("12345");
	 * appScoreRequest.setApplicationType(ApplicationType.CAAS);
	 * 
	 * C078Resp response = new C078Resp();
	 * 
	 * when(requestMapper.create(any(AppScoreRequest.class))).thenReturn(req);
	 * when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class),
	 * any(Object[].class))) .thenReturn(response);
	 * 
	 * C078Resp actualResponse = serviceImpl.invokeC078(appScoreRequest);
	 * assertNotNull(actualResponse); }
	 */

	@Test
	public void testC078WithIISInIB() {
		when(contextUtility.getCreditScoreSourceSystemCd()).thenReturn("034");
		when(contextUtility.getPrimaryPartyOcisId()).thenReturn("21012023");
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(c078Response());
		serviceImpl.invokeC078(ApplicationType.A008);
	}

	@Test
	public void testC078WithIISInMCA() {
		when(sessionManager.getBranchContext()).thenReturn(SessionServiceUtil.prepareBranchContext());
		when(contextUtility.getCreditScoreSourceSystemCd()).thenReturn("034");
		when(contextUtility.getPrimaryPartyOcisId()).thenReturn("21012023");
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(c078Response());
		serviceImpl.invokeC078(ApplicationType.A008);
	}

	@Test
	public void testC078WithoutIISAnd008DefaultSystemCode() {
		when(contextUtility.getPrimaryPartyOcisId()).thenReturn("21012023124343");
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(c078Response());
		serviceImpl.invokeC078(ApplicationType.A008);
	}

	@Test
	public void testC078WithoutIISAnd034DefaultSystemCode() {
		when(contextUtility.getPrimaryPartyOcisId()).thenReturn("21012023");
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(c078Response());
		serviceImpl.invokeC078(ApplicationType.A034);
	}

	@Test(expected = ServiceException.class)
	public void testC078WithoutIISAndCASSDefaultSystemCode() {
		when(contextUtility.getPrimaryPartyOcisId()).thenReturn("21012023");
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(c078Response());
		serviceImpl.invokeC078(ApplicationType.CAAS);
	}

	@Test(expected = ServiceException.class)
	public void testInvokeC078WithExceptionInServiceResponse() {
		AppScoreRequest appScoreRequest = new AppScoreRequest();
		appScoreRequest.setCreditScoreRequestNo("12345");
		appScoreRequest.setApplicationType(ApplicationType.CAAS);

		C078Resp response = new C078Resp();
		when(requestMapper.create(any(AppScoreRequest.class))).thenReturn(req);

		ResponseError error = new ResponseError();
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenThrow(new ServiceException(error));

		serviceImpl.invokeC078(appScoreRequest);
	}

	private C078Resp c078Response() {
		C078Resp response = new C078Resp();
		C078Result result = new C078Result();
		ResultCondition resultCondition = new ResultCondition();
		resultCondition.setSeverityCode(SeverityCode.value1);
		result.setResultCondition(resultCondition);
		response.setC078Result(result);
		DecisionDetails[] decisionList = new DecisionDetails[1];
		DecisionDetails decisionDetail = new DecisionDetails();
		decisionDetail.setCSDecisnReasonTypeNr("Accept");
		decisionDetail.setCSDecisionReasonTypeCd("601");
		decisionList[0] = decisionDetail;
		response.setDecisionDetails(decisionList);
		response.setASMCreditScoreResultCd("1");
		return response;
	}
}
