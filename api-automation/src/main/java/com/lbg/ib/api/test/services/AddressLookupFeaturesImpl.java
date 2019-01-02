/**
 * 
 */
package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.when;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class AddressLookupFeaturesImpl {
	
	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
	
	public Map<String, String> getResponseFromAPI(String apiURL) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response = when().get(apiURL).then().contentType(ContentType.JSON).extract().response();
		String msg = response.getBody().asString();
		String resCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", msg );
		responseMap.put("StatusCode", resCode);
		return responseMap;
	}

}
