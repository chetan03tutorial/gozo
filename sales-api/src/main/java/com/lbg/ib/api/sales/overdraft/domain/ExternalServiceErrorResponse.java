package com.lbg.ib.api.sales.overdraft.domain;

import java.util.List;

public class ExternalServiceErrorResponse {

	private List<ExternalServiceError> errors;

	public List<ExternalServiceError> getErrors() {
		return errors;
	}

	public void setErrors(List<ExternalServiceError> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return " errorList = " + this.errors;
	}
}
