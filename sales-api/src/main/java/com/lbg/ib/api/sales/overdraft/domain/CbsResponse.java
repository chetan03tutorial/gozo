package com.lbg.ib.api.sales.overdraft.domain;

import java.util.List;

public class CbsResponse {

	private Double odLimit;
	private List<CbsError> errors;

	public List<CbsError> getErrors() {
		return errors;
	}

	public void setErrors(List<CbsError> errors) {
		this.errors = errors;
	}

	public Double getOdLimit() {
		return odLimit;
	}

	public void setOdLimit(Double odLimit) {
		this.odLimit = odLimit;
	}
	
	
}
