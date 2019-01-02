package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;

public class ActivateProductImpl {
	
	static {
        RestAssured.useRelaxedHTTPSValidation();
	}

	
	public Map<String, String> getResponseFromAPI(String apiURL, String requestBody, Map<String, String> cookieMap) {
		Map<String, String> responseMap = new HashMap<String, String>();
		System.out.println("=================api url:" + apiURL);
		Response response = given().cookies(cookieMap).contentType(ContentType.JSON).body(requestBody).post(apiURL)
				.thenReturn();
		ResponseBody responseBody = response.getBody();
		String msg = responseBody.asString();
		System.out.println("message:" + msg);
		String resCode = String.valueOf(response.getStatusCode());
		System.out.println("resCode:" + resCode);
		responseMap.put("StatusMessage", msg);
		responseMap.put("StatusCode", resCode);
		return responseMap;
	}

}
