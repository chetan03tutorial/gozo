package com.lbg.ib.api.alligator.annotations.handler;

import com.lbg.ib.api.alligator.annotations.MultipartParameter;
import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.util.AttachmentReader;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public class MultipartParameterHandler extends AbstractAnnotationHandler{

	@Override
	public void handle(ServiceRequest request, MethodParameter parameter) {
		MultipartParameter mpAnnotation = (MultipartParameter)parameter.getAnnotation(MultipartParameter.class);
		String filePath = (String)parameter.getParamValue();
		request.addMultipartParam(mpAnnotation.param(), AttachmentReader.readFile(filePath));
	}

}
