package com.lbg.ib.api.alligator.annotations.handler;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.alligator.constants.Brand;
import com.lbg.ib.api.alligator.exception.FrameworkException;
import com.lbg.ib.api.alligator.exception.ResponseError;
import com.lbg.ib.api.alligator.lang.MethodParameter;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public class BrandHandler extends AbstractAnnotationHandler {

	public void handle(ServiceRequest request, MethodParameter parameter) {
	    String brandEndpoint = getHostInformation((String) parameter.getParamValue());
	    if(StringUtils.isEmpty(brandEndpoint)){
	        throw new FrameworkException(new ResponseError("90001", "End point not configured for the brand"));
	    }
		String endpoint = new StringBuilder(brandEndpoint)
				.append(request.getEndPoint()).toString();
		request.setEndPoint(endpoint);
	}

	private String getHostInformation(String brandName) {
		Brand brand = Brand.valueOf(brandName.toUpperCase());
		switch (brand) {
		case BOS:
			return System.getProperty("endpoint.bos");//(String)applicationProperties.get());
		case HALIFAX:
			return System.getProperty("endpoint.halifax");//(String)applicationProperties.get("endpoint.halifax"));
		case LLOYDS:
			return System.getProperty("endpoint.lloyds");//(String)applicationProperties.get("endpoint.lloyds"));
		default:
			throw new IllegalArgumentException("Invalid brand " + brandName);
		}
	}


	
}
