package com.lbg.ib.api.alligator.annotations.handler;

import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public interface AnnotationHandler {

	public void handle(ServiceRequest request, MethodParameter parameter);
}
