package com.lbg.ib.api.sales.shared.validator.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.overdraft.domain.ExternalServiceErrorResponse;
import com.lbg.ib.api.sales.overdraft.domain.ExternalServiceError;
import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;

@Component
public class ServiceErrorValidator {

	@Autowired
	private LoggerDAO logger;
	
	@TraceLog
	public void validateResponseError(ResultCondition resultCondition) {
		Byte severityCode = resultCondition.getSeverityCode().getValue();
		if (severityCode != 2) {
			return;
		}
		ExternalServiceErrorResponse serviceResponse = new ExternalServiceErrorResponse();
		List<ExternalServiceError> errorList = new LinkedList<ExternalServiceError>();
		Integer reasonCode = resultCondition.getReasonCode();
		logger.traceLog(this.getClass(), "Reason Code is " + reasonCode);
		String reasonText = resultCondition.getReasonText();
		logger.traceLog(this.getClass(), "Reason Text is " + reasonText);
		errorList.add(serviceError(reasonCode, reasonText, severityCode));

		Condition[] conditions = resultCondition.getExtraConditions();
		if (conditions != null) {
			for (Condition condition : conditions) {
				errorList.add(serviceError(condition.getReasonCode(), condition.getReasonText(),
						condition.getSeverityCode().getValue()));
			}
		}
		serviceResponse.setErrors(errorList);
		throw new ServiceError(serviceResponse);
	}

	@TraceLog
	public ExternalServiceError serviceError(Integer reasonCode, String reasonText, Byte severityCode) {
		ExternalServiceError error = new ExternalServiceError();
		error.setErrorCode(String.valueOf(reasonCode));
		error.setErrorMessage(reasonText);
		error.setSeverityCode(String.valueOf(severityCode));
		return error;
	}
}
