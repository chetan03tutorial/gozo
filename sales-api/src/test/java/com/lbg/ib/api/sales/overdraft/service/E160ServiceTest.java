package com.lbg.ib.api.sales.overdraft.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.E160Request;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lloydstsb.www.E160Resp;
import com.lloydstsb.www.E160Result;
import com.lloydstsb.www.E160_DelCBSFormalOdr_ServiceLocator;

@RunWith(MockitoJUnitRunner.class)
public class E160ServiceTest {

	@InjectMocks
	private E160ServiceImpl service;

	@Mock
	private LoggerDAO logger;
	
	@Mock
	private ServiceErrorValidator serviceErrorValidator;

	private final String SERVICE_NAME = "E160";

	@Mock
	private ModuleContext beanLoader;

	@Mock
	private SessionManagementDAO sessionManager;

	@Mock
	private E160_DelCBSFormalOdr_ServiceLocator serviceLocator;

	@Mock
	private HandlerRegistry handleRegistry;

	@Mock
	private SOAInvoker soaInvoker;

	private BranchContext branchContext() {
		BranchContext branchContext = new BranchContext();
		branchContext.setOriginatingSortCode("303030");
		return branchContext;
	}

	private E160Request e160Request() {
		E160Request e160Request = new E160Request();
		e160Request.setAccountNumber("09020239");
		e160Request.setSortCode("2398239");
		e160Request.setOriginatingSortcode("238723");
		return e160Request;
	}

	private E160Resp e160Response() {
		E160Resp e160Response = new E160Resp();
		E160Result e160Result = new E160Result();
		ResultCondition resultCondition = new ResultCondition();
		resultCondition.setReasonCode(0);
		resultCondition.setSeverityCode(SeverityCode.value1);
		e160Result.setResultCondition(resultCondition);
		e160Response.setE160Result(e160Result);
		return e160Response;
	}

	private E160Resp e160FailureResponse() {
		E160Resp e160Response = new E160Resp();
		E160Result e160Result = new E160Result();
		ResultCondition resultCondition = new ResultCondition();
		resultCondition.setReasonCode(10);
		resultCondition.setSeverityCode(SeverityCode.value3);
		e160Result.setResultCondition(resultCondition);
		Condition[] extraCondition = new Condition[1];
		Condition condition = new Condition();
		condition.setSeverityCode(SeverityCode.value3);
		condition.setReasonCode(1040);
		condition.setReasonText("Account Number is Invalid");
		extraCondition[0] = condition;
		resultCondition.setExtraConditions(extraCondition);
		e160Response.setE160Result(e160Result);
		return e160Response;
	}

	@Before
	public void setup() {
		when(serviceLocator.getE160_DelCBSFormalOdrWSDDPortName()).thenReturn(SERVICE_NAME);
		when(beanLoader.getService(E160_DelCBSFormalOdr_ServiceLocator.class)).thenReturn(serviceLocator);
		when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(e160Response());
		doNothing().when(serviceErrorValidator).validateResponseError(any(ResultCondition.class));

	}

	@Test
	public void testE160InDigital() {
		when(sessionManager.getBranchContext()).thenReturn(null);
		service.invokeE160(e160Request());
	}

	@Test
	public void testE160InMCA() {
		when(sessionManager.getBranchContext()).thenReturn(branchContext());
		service.invokeE160(e160Request());
	}

	/*@Test(expected = ServiceError.class)
	public void testFailureOfE160() {
		when(sessionManager.getBranchContext()).thenReturn(null);
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(e160FailureResponse());
		service.invokeE160(e160Request());
	}*/

}
