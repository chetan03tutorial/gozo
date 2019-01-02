package com.lbg.ib.api.sales.gozo.tasks;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.validators.ServiceValidator;

public class ValidateMessage extends AbstractInstruction {

	public ValidateMessage(Phase phase) {
		super(phase);
	}

	@Override
	public void processIncomingPhase(InstructionContext _context) {
		ServiceValidator serviceValidator = _context.get(InstructionContext.SERVICE_VALIDATOR, ServiceValidator.class);
		if(serviceValidator == null) {
			return;
		}
		Object[] arguments = _context.get(InstructionContext.METHOD_ARGUMENTS, Object[].class);
		serviceValidator.validateRequest(serviceValidator, arguments);
	}

	@Override
	public void processOutgoingPhase(InstructionContext _context) {
		ServiceValidator serviceValidator = _context.get(InstructionContext.SERVICE_VALIDATOR, ServiceValidator.class);
		if(serviceValidator == null) {
			return;
		}
		Object serviceResponse = _context.get(InstructionContext.SERVICE_RESPONSE, Object.class);
		serviceValidator.validateResponse(serviceResponse);
	}

}
