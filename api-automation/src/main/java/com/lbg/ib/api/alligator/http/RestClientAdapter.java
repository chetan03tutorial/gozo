package com.lbg.ib.api.alligator.http;

import com.jayway.restassured.response.Response;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public interface RestClientAdapter {
	
	public Response call(ServiceRequest requestWrapper) /*throws Exception*/;
	
}
