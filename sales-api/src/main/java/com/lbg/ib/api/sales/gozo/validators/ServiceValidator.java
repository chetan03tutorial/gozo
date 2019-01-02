package com.lbg.ib.api.sales.gozo.validators;

public interface ServiceValidator {

	public Object validateRequest(Object... args);
	public Object validateResponse(Object... args);
}
