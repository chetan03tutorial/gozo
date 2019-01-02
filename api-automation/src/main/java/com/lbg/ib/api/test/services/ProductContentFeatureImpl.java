/**
 * 
 */
package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author PCA-Team
 */

public class ProductContentFeatureImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
	
	public Map<String, String> getResponseFromAPI(String apiURL)
			throws FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response = given().get(apiURL).then().contentType(ContentType.JSON).extract().response();
		
		String responseCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", response.getBody().asString());
		responseMap.put("StatusCode", responseCode);
		responseMap.put("sessionId", response.sessionId());
		return responseMap;
	}

}
