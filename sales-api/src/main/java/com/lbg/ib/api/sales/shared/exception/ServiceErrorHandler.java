package com.lbg.ib.api.sales.shared.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
@Provider
public class ServiceErrorHandler implements ExceptionMapper<ServiceError>{

	@Autowired
	private LoggerDAO logger;

	public Response toResponse(ServiceError serviceError) {
		logger.logException(this.getClass(), serviceError);
		Status status = Status.OK;
		return (new ResponseBuilderImpl()).status(status).entity(serviceError.getErrorEntity()).build();
	}
}
