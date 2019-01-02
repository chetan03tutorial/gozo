package com.lbg.ib.api.alligator.web.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.restassured.response.Response;
import com.lbg.ib.api.alligator.util.InvalidFormatException;
import com.lbg.ib.api.alligator.util.JsonUtils;

/**
 * 
 * @author cshar8
 *
 */
public class ServiceResponse {

	private final String serviceStatus;
	private final Object serviceMessage;
	private final String sessionId ;
	private Map<String, String> cookies;
	
	private ServiceResponse(final String serviceStatus, final String sessionId, final Object serviceMessage, Map<String, String> cookies) throws InvalidFormatException
	{
		this.serviceMessage = serviceMessage;
		this.serviceStatus = serviceStatus;
		this.sessionId = sessionId;
		this.cookies = cookies;
	}
	
	public String getServiceStatus() {
		return serviceStatus;
	}
	
	public boolean isServiceResponseAnArray() {
		return (serviceMessage instanceof List);
	}
	
	public Map<String,Object> getServiceMessage() {
		return (serviceMessage instanceof Map)? (Map<String, Object>) serviceMessage: null;
	}
	
	public List<Map<String,Object>> getServiceMessages() {
		return (serviceMessage instanceof List)? (List<Map<String, Object>>) serviceMessage: null;
	}
	
	public static ServiceResponse withResult(Response response) throws InvalidFormatException
	{
		String httpStatus = String.valueOf(response.getStatusCode());
		String jsonMsg = response.getBody().asString();
		String session = response.getSessionId();
		Map<String, String> cookies = response.getCookies();
		return new ServiceResponse(httpStatus,session,JsonUtils.resolve(jsonMsg,new TypeReference<Object>(){}), cookies);
	}
	
	public Object getPropValue(String propName)
	{
		Map map = getServiceMessage();
		return null != map?map.get(propName):null;
	}
	
	
	public void addPropertyInResponseMap(String key, Object value)
	{
		Map map = getServiceMessage();
		if(null != map)
			map.put(key, value);
	}
	
	public String getSessionId()
	{
		return this.sessionId;
	}

	public Map<String, String> getCookies() {
		/*if(cookies == null)
			cookies = new HashMap<String, String>();*/
		return cookies;
	}
	
	
}
