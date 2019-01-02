package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class BranchContextImpl {

	private Map<String, String> cookieMap = null;
	
	public Map<String, String> getResponseFromAPI(String requestBody,String apiURL) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response validateTokenResponse = given().contentType(ContentType.JSON).body(requestBody).post(apiURL)
				.thenReturn();
		String sessionId = validateTokenResponse.getSessionId();
		String msg = validateTokenResponse.getBody().asString();
		String resCode = String.valueOf(validateTokenResponse.getStatusCode());
		responseMap.put("StatusMessage", msg );
		responseMap.put("StatusCode", resCode);
		responseMap.put("sessionId", sessionId);
		
		
		return responseMap;
	}
	
	public Map<String, String> getResponseFromAPI(String apiURL)
			throws FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response = given().get(apiURL).then().contentType(ContentType.JSON).extract().response();
		
		String responseCode = String.valueOf(response.getStatusCode());
		String sessionId = response.getSessionId();
		responseMap.put("StatusMessage", response.getBody().asString());
		responseMap.put("StatusCode", responseCode);
		responseMap.put("sessionId", sessionId);
		
		cookieMap = response.getCookies();
		return responseMap;
	}	
	
	public Map<String, String> getResponseFromAPIUsingSessionId(String sessionId,String apiURL) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response = given().sessionId(sessionId).get(apiURL).then().contentType(ContentType.JSON).extract().response();
		String msg = response.getBody().asString();
		String resCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", msg );
		responseMap.put("StatusCode", resCode);
		return responseMap;
	}
	
	
	public Map<String, String> getCookieMap() {
		return cookieMap;
	}

}
