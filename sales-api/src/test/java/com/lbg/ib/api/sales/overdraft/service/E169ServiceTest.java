package com.lbg.ib.api.sales.overdraft.service;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.E169Request;
import com.lbg.ib.api.sales.overdraft.domain.E169Response;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lloydstsb.www.E169Resp;
import com.lloydstsb.www.E169Result;
import com.lloydstsb.www.E169_AddCBSFrmOd_ServiceLocator;

@RunWith(MockitoJUnitRunner.class)
public class E169ServiceTest {

	@InjectMocks
	private E169ServiceImpl service;

	private final String SERVICE_NAME = "E169";

	@Mock
	private LoggerDAO logger;

	@Mock
	private ModuleContext beanLoader;

	@Mock
	private SessionManagementDAO sessionManager;

	@Mock
	private E169_AddCBSFrmOd_ServiceLocator serviceLocator;

	@Mock
	private HandlerRegistry handleRegistry;

	@Mock
	private SOAInvoker soaInvoker;
	
	@Mock
	private ServiceErrorValidator serviceErrorValidator;

	private BranchContext branchContext() {
		BranchContext branchContext = new BranchContext();
		branchContext.setOriginatingSortCode("303030");
		return branchContext;
	}

	private E169Request e169Request() {
		E169Request e169Request = new E169Request();
		e169Request.setAccountNumber("90909090");
		e169Request.setSortCode("90909090");
		return e169Request;
	}

	private E169Resp e169Response() {
		E169Resp e169Response = new E169Resp();
		E169Result e160Result = new E169Result();
		ResultCondition resultCondition = new ResultCondition();
		resultCondition.setReasonCode(0);
		resultCondition.setSeverityCode(SeverityCode.value1);
		e160Result.setResultCondition(resultCondition);
		e169Response.setE169Result(e160Result);
		return e169Response;
	}

	private E169Resp e169FailureResponse() {
		E169Resp e169Response = new E169Resp();
		E169Result e169Result = new E169Result();
		ResultCondition resultCondition = new ResultCondition();
		resultCondition.setReasonCode(10);
		resultCondition.setSeverityCode(SeverityCode.value3);
		e169Result.setResultCondition(resultCondition);
		Condition[] extraCondition = new Condition[1];
		Condition condition = new Condition();
		condition.setSeverityCode(SeverityCode.value3);
		condition.setReasonCode(1040);
		condition.setReasonText("Account Number is Invalid");
		extraCondition[0] = condition;
		resultCondition.setExtraConditions(extraCondition);
		e169Response.setE169Result(e169Result);
		return e169Response;
	}

	@Before
	public void setup() {
		when(serviceLocator.getE169_AddCBSFrmOdWSDDPortName()).thenReturn(SERVICE_NAME);
		when(beanLoader.getService(E169_AddCBSFrmOd_ServiceLocator.class)).thenReturn(serviceLocator);
		when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
		.thenReturn(e169Response());
	}

	@Test
	public void testE169InDigital() {
		when(sessionManager.getBranchContext()).thenReturn(null);
		service.invokeE169(e169Request());
	}

	@Test
	public void testE169InMCA() {
		when(sessionManager.getBranchContext()).thenReturn(branchContext());
		service.invokeE169(e169Request());
	}

	
}
