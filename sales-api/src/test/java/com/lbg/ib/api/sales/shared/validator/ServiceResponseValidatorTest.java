package com.lbg.ib.api.sales.shared.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.sales.shared.validator.service.ServiceErrorValidator;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class ServiceResponseValidatorTest {

	@InjectMocks
	private ServiceErrorValidator errorValidator;

	@Mock
	private LoggerDAO logger;
	
	@Before
	public void setup() {

	}

	@Test
	public void testErrorWhenSeverityIsLessThan2() {
		errorValidator.validateResponseError(resultConditionWithSeverityLessThan2());
	}

	@Test(expected = ServiceError.class)
	public void testErrorWhenSeverityIs2() {
		errorValidator.validateResponseError(resultConditionWithSeverity2());
	}

	@Test(expected = ServiceError.class)
	public void testErrorWhenSeverityIs2AndExtraConditionsAreAvailable() {
		errorValidator.validateResponseError(resultConditionWithSeverity2AndExtraCondition());
	}

	private ResultCondition resultConditionWithSeverityLessThan2() {
		ResultCondition resultCondition = new ResultCondition();
		SeverityCode severityCode = SeverityCode.value2;
		resultCondition.setSeverityCode(severityCode);
		return resultCondition;
	}

	private ResultCondition resultConditionWithSeverity2() {
		ResultCondition resultCondition = new ResultCondition();
		SeverityCode severityCode = SeverityCode.value3;
		resultCondition.setReasonCode(1001);
		resultCondition.setReasonText("Error in service");
		resultCondition.setSeverityCode(severityCode);
		return resultCondition;
	}

	private ResultCondition resultConditionWithSeverity2AndExtraCondition() {
		ResultCondition resultCondition = new ResultCondition();
		SeverityCode severityCode = SeverityCode.value3;
		resultCondition.setReasonCode(1001);
		resultCondition.setReasonText("Error in service");
		resultCondition.setSeverityCode(severityCode);

		Condition[] conditions = new Condition[1];
		Condition condition = new Condition();
		condition.setReasonCode(10100);
		condition.setReasonText("Service Error");
		condition.setSeverityCode(SeverityCode.value3);
		conditions[0] = condition;
		resultCondition.setExtraConditions(conditions);
		return resultCondition;
	}
}
