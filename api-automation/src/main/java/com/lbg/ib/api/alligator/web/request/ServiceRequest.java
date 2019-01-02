package com.lbg.ib.api.alligator.web.request;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lbg.ib.api.alligator.constants.ContentType;
import com.lbg.ib.api.alligator.exception.FrameworkException;
import com.lbg.ib.api.alligator.exception.ResponseError;

public class ServiceRequest {

	private String endPoint;
	private String requestBody;
	private Map<String, String> cookies;
	private HttpMethod requestMethod;
	private ConcurrentHashMap<String, String> headers;
	private ConcurrentHashMap<String, String> pathParams;
	private ConcurrentHashMap<String, String> formParams;
	private ConcurrentHashMap<String, Object> queryParams;
	private ConcurrentHashMap<String, File> multipartParams;
	/* private ConcurrentHashMap<String, String> mutipartFormParams; */

	String sessionId;
	private ContentType contentType;

	public ContentType contentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public ServiceRequest() {
		this.pathParams = new ConcurrentHashMap<String, String>();
		this.headers = new ConcurrentHashMap<String, String>();
		this.formParams = new ConcurrentHashMap<String, String>();
		this.queryParams = new ConcurrentHashMap<String, Object>();
		this.cookies = new HashMap<String, String>();
		this.multipartParams = new ConcurrentHashMap<String, File>();
		/* this.mutipartFormParams = new ConcurrentHashMap<String,String>(); */
	}

	public ServiceRequest addPathParam(String paramName, String paramValue) {
		try {
			pathParams.putIfAbsent(paramName, paramValue);
		} catch (Exception ex) {
			throw new FrameworkException(new ResponseError("Path Parameter Exception " + paramName + " := " + paramValue, ex.getMessage()));
		}
		return this;
	}

	public ServiceRequest addFormParam(String paramName, String paramValue) {
		try {
			formParams.putIfAbsent(paramName, paramValue);
		} catch (Exception ex) {
			throw new FrameworkException(new ResponseError("Form Parameter", ex.getMessage()));
		}
		return this;
	}

	public ServiceRequest addQueryParam(String paramName, Object paramValue) {
		try {
			queryParams.put(paramName, paramValue);
		} catch (Exception ex) {
			throw new FrameworkException(new ResponseError("Some Exception", ex.getMessage()));
		}
		return this;
	}

	public ServiceRequest addMultipartParam(String paramName, File paramValue) {
		try {
			multipartParams.putIfAbsent(paramName, paramValue);
		} catch (Exception ex) {
			throw new FrameworkException(new ResponseError("MultiPart Param exception", ex.getMessage()));
		}
		return this;
	}

	/*
	 * public ServiceRequest addMultipartFormParam(String paramName, String
	 * paramValue) { mutipartFormParams.putIfAbsent(paramName, paramValue);
	 * return this; }
	 */

	public ServiceRequest addHeaderParam(String paramName, String paramValue) {
		try {
			headers.putIfAbsent(paramName, paramValue);
		} catch (Exception ex) {
			throw new FrameworkException(new ResponseError("Some Exception",
					"Cannot add null parameter in concurrent hashmap"));
		}
		return this;
	}

	public Map<String, String> pathParams() {
		return this.pathParams;
	}

	public Map<String, File> multipartParams() {
		return this.multipartParams;
	}

	/*
	 * public Map<String, String> multipartFormParams() { return
	 * this.mutipartFormParams; }
	 */

	public Map<String, String> formParams() {
		return this.formParams;
	}

	public Map<String, Object> queryParams() {
		return this.queryParams;
	}

	public Map<String, String> headers() {
		return this.headers;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public Map<String, String> getCookies() {
		/*
		 * if (cookies == null) { cookies = new HashMap<String, String>(); }
		 */
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies= cookies;
	}

	public HttpMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(HttpMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String sessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
