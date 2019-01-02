package com.lbg.ib.api.alligator.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.test.context.ContextConfiguration;

import com.lbg.ib.api.alligator.web.request.ServiceRequest;
import com.lbg.ib.api.alligator.web.response.ServiceResponse;

/**
 * @author cshar8
 */
/*@ContextConfiguration(locations = { "classpath*:conf/api-config.xml" })*/
@ContextConfiguration(value="classpath*:conf/api-config.xml")
public class BaseFeatures {
	
	public BaseFeatures(){
		
	}

	protected ServiceRequest request;

	protected ServiceResponse response;

	private static Map<String, String> cookies;

	public Map<String, String> getCookies() {
		if (cookies == null) {
			cookies = new HashMap<String, String>();
		}
		return cookies;
	}

	public void setCookies(Map<String, String> cookiesMap) {
		/*if (cookies == null) {
			cookies = new HashMap<String, String>();
		}*/
		cookies.putAll(cookiesMap);
	}

	public void setRequest(ServiceRequest request) {
		this.request = request;
	}

	public void setResponse(ServiceResponse response) {
		this.response = response;
	}

	public ServiceResponse response() {
		return this.response;
	}
}
