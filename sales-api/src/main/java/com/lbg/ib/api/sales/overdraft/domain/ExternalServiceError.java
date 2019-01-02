package com.lbg.ib.api.sales.overdraft.domain;

public class ExternalServiceError {

	private String errorCode;
	private String errorMessage;
	private String severityCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSeverityCode() {
		return severityCode;
	}

	public void setSeverityCode(String severityCode) {
		this.severityCode = severityCode;
	}

	@Override
	public String toString() {
		return "{ code=" + errorCode + " message = " + errorMessage + " severity = " + severityCode + "}";
	}
}
