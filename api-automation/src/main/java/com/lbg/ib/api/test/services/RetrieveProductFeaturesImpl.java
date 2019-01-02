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

public class RetrieveProductFeaturesImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}

	private Map<String, String> cookieMap = null;

	public Map<String, String> getResponseFromAPI(String apiURL)
			throws FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response = given().get(apiURL).then().contentType(ContentType.JSON).extract().response();
		
		String responseCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", response.getBody().asString());
		responseMap.put("StatusCode", responseCode);
		cookieMap = response.getCookies();
		return responseMap;
	}

	
	public Map<String, String> getResponseFromAPIWithSessionId(String sessionId,String apiURL) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response response = given().sessionId(sessionId).get(apiURL).then().contentType(ContentType.JSON).extract().response();;
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
