package com.lbg.ib.api.sales.shared.exception;

public class ServiceError extends RuntimeException {

	private Object errorEntity;

	public ServiceError() {

	}

	public ServiceError(Object errorEntity) {
		this.errorEntity = errorEntity;
	}
	
	public <T> void setErrorEntity(T errorEntity) {
		this.errorEntity = errorEntity;
	}

	public Object getErrorEntity() {
		return this.errorEntity;
	}

	@Override
	public String toString() {
		return "ServiceError{error="+this.errorEntity+"}";
	}
}
