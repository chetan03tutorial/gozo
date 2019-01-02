package com.lbg.ib.api.sales.overdraft.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import javax.xml.rpc.handler.HandlerRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.E170Request;
import com.lbg.ib.api.sales.overdraft.domain.E170Response;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lloydstsb.www.E170Resp;
import com.lloydstsb.www.E170Result;
import com.lloydstsb.www.E170_ChaCBSFormalOd_ServiceLocator;
import com.lloydstsb.www.FeatNextRvwGrp;

@RunWith(MockitoJUnitRunner.class)
public class E170ServiceTest {

	@InjectMocks
	private E170ServiceImpl service;
	
	@Mock
	private ServiceErrorValidator serviceErrorValidator;

	private final String SERVICE_NAME = "E170";

	@Mock
	private ModuleContext beanLoader;

	@Mock
	private LoggerDAO logger;

	@Mock
	private SessionManagementDAO sessionManager;

	@Mock
	private E170_ChaCBSFormalOd_ServiceLocator serviceLocator;

	@Mock
	private HandlerRegistry handleRegistry;

	@Mock
	private SOAInvoker soaInvoker;

	private BranchContext branchContext() {
		BranchContext branchContext = new BranchContext();
		branchContext.setOriginatingSortCode("303030");
		return branchContext;
	}

	private E170Request e170Request() {
		E170Request e170Request = new E170Request();
		e170Request.setAccountNumber("90909090");
		e170Request.setSortCode("90909090");
		return e170Request;
	}

	private E170Resp e170Response() {
		E170Resp e170Response = new E170Resp();
		FeatNextRvwGrp featureNextReviewGroup = new FeatNextRvwGrp();
		featureNextReviewGroup.setFeatNextRvwFlagDt(1);
		featureNextReviewGroup.setFeatureNextReviewDt("20072019");
		E170Result e170Result = new E170Result();
		e170Response.setFeatNextRvwGrp(featureNextReviewGroup);
		ResultCondition resultCondition = new ResultCondition();
		resultCondition.setReasonCode(0);
		resultCondition.setSeverityCode(SeverityCode.value1);
		e170Result.setResultCondition(resultCondition);
		e170Response.setE170Result(e170Result);
		return e170Response;
	}

	private E170Resp e170FailureResponse() {
		E170Resp e170Response = new E170Resp();
		E170Result e170Result = new E170Result();
		ResultCondition resultCondition = new ResultCondition();
		resultCondition.setReasonCode(10);
		resultCondition.setSeverityCode(SeverityCode.value3);
		e170Result.setResultCondition(resultCondition);
		Condition[] extraCondition = new Condition[1];
		Condition condition = new Condition();
		condition.setSeverityCode(SeverityCode.value3);
		condition.setReasonCode(1040);
		condition.setReasonText("Account Number is Invalid");
		extraCondition[0] = condition;
		resultCondition.setExtraConditions(extraCondition);
		e170Response.setE170Result(e170Result);
		return e170Response;
	}

	@Before
	public void setup() {
		when(serviceLocator.getE170_ChaCBSFormalOdWSDDPortName()).thenReturn(SERVICE_NAME);
		when(beanLoader.getService(E170_ChaCBSFormalOd_ServiceLocator.class)).thenReturn(serviceLocator);
		when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
		doNothing().when(serviceErrorValidator).validateResponseError(any(ResultCondition.class));

	}

	@Test
	public void testSuccessOfE170InDigital() {
		when(sessionManager.getBranchContext()).thenReturn(null);
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(e170Response());
		E170Response response = service.invokeE170(e170Request());
		// assertNull(response.getErrors());
	}

	@Test
	public void testSuccessOfE170InMCA() {
		when(sessionManager.getBranchContext()).thenReturn(branchContext());
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(e170Response());

		E170Response response = service.invokeE170(e170Request());
		// assertNull(response.getErrors());
	}

	@Test(expected = ServiceError.class)
	public void testFailureOfE170() {
		doThrow(ServiceError.class).when(serviceErrorValidator).validateResponseError(any(ResultCondition.class));
		when(sessionManager.getBranchContext()).thenReturn(null);
		when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class)))
				.thenReturn(e170FailureResponse());
		service.invokeE170(e170Request());
	}

}
