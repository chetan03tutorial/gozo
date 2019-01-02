package com.lbg.ib.api.alligator.annotations.handler;

import com.lbg.ib.api.alligator.annotations.QueryParameter;
import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public class QueryParameterHandler extends AbstractAnnotationHandler {

	@Override
	public void handle(ServiceRequest request,MethodParameter parameter) {
		QueryParameter queryParam = (QueryParameter)parameter.getAnnotation(QueryParameter.class);		
		request.addQueryParam(queryParam.param(), (String)parameter.getParamValue());
	}
}