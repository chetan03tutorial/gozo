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
import com.jayway.restassured.response.ResponseBody;

/**
 * @author sthak4
 *
 */
public class ClassifyInvolvedPartyImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
	
	private static final String HTTP = "http://";

	public Map<String, String> getResponseFromAPI(String apiURL) {
		Map<String, String> responseMap = new HashMap<String, String>();
		System.out.println("=================api url:" + apiURL);
		System.out.println("restassured" + RestAssured.baseURI);
		Response response = when().get(HTTP+apiURL).then().contentType(ContentType.JSON).extract().response();
		ResponseBody responseBody = response.getBody();
		String msg = responseBody.asString();
		String resCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", msg );
		responseMap.put("StatusCode", resCode);
		return responseMap;
	}

}
