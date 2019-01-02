package com.lbg.ib.api.sales.gozo.tasks;

import java.lang.reflect.Method;
import java.rmi.Remote;

import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.model.WebServiceConfiguration;

public class SoapInvoker extends AbstractInstruction {

	public SoapInvoker(Phase phase) {
		super(phase);
	}

	@Override
	public void processIncomingPhase(InstructionContext _context) {
		Object serviceResponse = null;
		Object serviceRequest = _context.get(InstructionContext.SERVICE_REQUEST);
		WebServiceConfiguration configuration = _context.get(InstructionContext.SERVICE_CONFIG,
				WebServiceConfiguration.class);
		try {
			Remote serviceInstance = configuration.getServiceEndpointInterface();
			Class<? extends Remote> sei = serviceInstance.getClass();
			Method operation = sei.getDeclaredMethod(configuration.getOperation(), serviceRequest.getClass());
			// Class<?> returnType = operation.getReturnType();
			serviceResponse = operation.invoke(serviceInstance, serviceRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		_context.set(InstructionContext.SERVICE_RESPONSE, serviceResponse);
	}

	@Override
	public void processOutgoingPhase(InstructionContext _context) {

	}

}
