package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;

public class RetrieveProductArrangementQuestionnireImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
	
	public Map<String, String> getResponseFromAPI(String apiURL) {
		Map<String, String> responseMap = new HashMap<String, String>();
		
	    Response response = when().get(apiURL).then().contentType(ContentType.JSON).extract().response();
		System.out.println("=================api url:" + apiURL);
		ResponseBody responseBody = response.getBody();
		String msg = responseBody.asString();
		System.out.println("message:" + msg);
		String resCode = String.valueOf(response.getStatusCode());
		System.out.println("resCode:" + resCode);
		responseMap.put("StatusMessage", msg);
		responseMap.put("StatusCode", resCode);
		System.out.println("session Id for the  product:"+response.getSessionId());
		responseMap.put("sessionId", response.getSessionId());
		return responseMap;
	}
	


}


