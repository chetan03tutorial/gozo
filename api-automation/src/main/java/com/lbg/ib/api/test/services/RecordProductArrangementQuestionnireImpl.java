package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.lbg.ib.api.services.util.SSLUtil;

public class RecordProductArrangementQuestionnireImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
	
	public Map<String, String> getResponseFromAPI(String apiURL, String requestBody)
	{
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response;
		if(apiURL.contains("https:")) {
			Map<String, String> cookieMap = SSLUtil.getCookies(apiURL);
			response = given().cookies(cookieMap).contentType(ContentType.JSON).body(requestBody).post(apiURL).thenReturn();
		} else {
			response = given().contentType(ContentType.JSON).body(requestBody).post(apiURL).thenReturn();
		}
		ResponseBody responseBody = response.getBody();
		String statusCode = String.valueOf(response.getStatusCode());
		String msg = responseBody.asString();
		System.out.println("Getting the following message in response body" + msg);
		
		//System.out.println("resCode:" + statusCode);
		responseMap.put("StatusMessage", msg);
		responseMap.put("StatusCode", statusCode);
		//System.out.println("session Id for the  product:"+response.getSessionId());
		responseMap.put("sessionId", response.getSessionId());
		return responseMap;
	}

}


