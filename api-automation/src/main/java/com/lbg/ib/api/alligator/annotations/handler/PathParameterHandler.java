package com.lbg.ib.api.alligator.annotations.handler;

import com.lbg.ib.api.alligator.annotations.PathParameter;
import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.util.DataHandlerUtil;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public class PathParameterHandler extends AbstractAnnotationHandler {

	@Override
	public void handle(ServiceRequest request,MethodParameter parameter) {
		
		PathParameter pathAnnotation = (PathParameter)parameter.getAnnotation(PathParameter.class);
		String requestData = DataHandlerUtil.dataHandler(pathAnnotation.dataHandler(),(String)parameter.getParamValue());		
		request.addPathParam(pathAnnotation.param(), requestData);
	}
}
