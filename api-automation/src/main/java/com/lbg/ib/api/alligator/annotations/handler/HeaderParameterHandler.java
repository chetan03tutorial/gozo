package com.lbg.ib.api.alligator.annotations.handler;

import com.lbg.ib.api.alligator.annotations.HeaderParameter;
import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public class HeaderParameterHandler extends AbstractAnnotationHandler {

	@Override
	public void handle(ServiceRequest request,MethodParameter parameter) {
		HeaderParameter headerAnnotation = (HeaderParameter)parameter.getAnnotation(HeaderParameter.class);
		String paramValue = (String)parameter.getParamValue();		
		request.addHeaderParam(headerAnnotation.param(), paramValue);
	}
}
