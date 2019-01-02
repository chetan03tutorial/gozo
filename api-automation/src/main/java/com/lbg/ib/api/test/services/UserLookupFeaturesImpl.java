/**
 * 
 */
package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.RestAssured.expect;
import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.RequestSpecification;


public class UserLookupFeaturesImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
	
	public Map<String, String> getResponseFromAPI(RequestSpecification requestSpec,String apiURL,RequestSpecification requestSpec1,String productFeaturesHost,String productArrangementHost) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Response productFeaturesResponse = when().get(productFeaturesHost).then().contentType(ContentType.JSON).extract().response();
		String sessionId = productFeaturesResponse.getSessionId();
		Response productArrangementResponse = expect().statusCode(200).given().sessionId(sessionId).spec(requestSpec1).when().post(productArrangementHost).then().contentType(ContentType.JSON).extract().response();
		Response userAvailabilityResponse = expect().statusCode(200).given().sessionId(sessionId).spec(requestSpec).when().post(apiURL).then().contentType(ContentType.JSON).extract().response(); 
	
		String msg = userAvailabilityResponse.getBody().asString();
		String resCode = String.valueOf(userAvailabilityResponse.getStatusCode());
		responseMap.put("StatusMessage", msg );
		responseMap.put("StatusCode", resCode);
		return responseMap;
	}

}
