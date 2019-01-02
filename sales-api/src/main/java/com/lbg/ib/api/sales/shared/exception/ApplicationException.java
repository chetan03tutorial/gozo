package com.lbg.ib.api.sales.shared.exception;

public class ApplicationException extends RuntimeException {

	private String errorCode;
	private String errorMessage;
	private Exception exception;

	public ApplicationException(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ApplicationException(Exception exception) {
		this.exception = exception;
	}

	public ApplicationException(String errorCode, String errorMessage, Exception exception) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.exception = exception;
	}

}
