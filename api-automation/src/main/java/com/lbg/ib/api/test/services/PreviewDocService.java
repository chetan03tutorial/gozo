package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class PreviewDocService {

	static {
		RestAssured.useRelaxedHTTPSValidation();
	}

	public Map<String, String> getResponseFromAPI(String apiURL, String success) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response;
		if ("yes".equals(success)) {
			response = given().get(apiURL).then().contentType("image/jpg").extract().response();
		} else {
			response = given().get(apiURL).then().contentType("application/json").extract().response();
		}

		String responseCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", response.getBody().asString());
		responseMap.put("StatusCode", responseCode);
		return responseMap;
	}

}
