package com.lbg.ib.api.sales.gozo.tasks.handlers;

import com.lbg.ib.api.sales.gozo.validators.ServiceValidator;

public class ServiceValidatorHandler {

	public void validateRequest(ServiceValidator validator, Object[] args) {
		validator.validateRequest(args);
	}

	public void validateResponse(ServiceValidator validator, Object serviceResponse) {
		validator.validateResponse(serviceResponse);
	}
}
