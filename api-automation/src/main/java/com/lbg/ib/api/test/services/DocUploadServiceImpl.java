package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.specification.MultiPartSpecification;
import com.lbg.ib.api.services.util.JsonUtils;

public class DocUploadServiceImpl {

	static {
        RestAssured.useRelaxedHTTPSValidation();
	}
	private Map<String, String> cookieMap = null;
	
	public Map<String, String> getResponseFromAPI(String apiURL, String requestBody, String sessionId) {
		Map<String, String> responseMap = new HashMap<String, String>();
		System.out.println("=================api url:" + apiURL);
		Response response = given().sessionId(sessionId).contentType(ContentType.JSON).body(requestBody).post(apiURL)
				.thenReturn();
		ResponseBody responseBody = response.getBody();
		String responseCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", response.getBody().asString());
		responseMap.put("StatusCode", responseCode);
		cookieMap = response.getCookies();
//		return responseMap;
		
		String msg = responseBody.asString();
		System.out.println("message:" + msg);
		String resCode = String.valueOf(response.getStatusCode());
		System.out.println("resCode:" + resCode);
		responseMap.put("StatusMessage", msg);
		responseMap.put("StatusCode", resCode);
		responseMap.put("sessionId", sessionId);//response.getSessionId());
		return responseMap;
	}
	
	public Map<String, String> getResponseFromAPIWithCookieMap(String apiURL, String requestBody, Map<String, String> cookieMap) {
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
		System.out.println("session Id for offer product:"+response.getSessionId());
		//responseMap.put("sessionId", sessionId);//response.getSessionId());
		return responseMap;
	}
	// This will hit the product retrieve service
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
	
	public Map<String, String> getResponseFromAPI(String apiURL,String requestBody) {
		Map<String, String> responseMap = new HashMap<String, String>();
		
		Response response = given().contentType(ContentType.JSON).body(requestBody).post(apiURL).thenReturn();
		System.out.println("=================api url:" + apiURL);
		ResponseBody responseBody = response.getBody();
		String msg = responseBody.asString();
		System.out.println("message:" + msg);
		
		String resCode = String.valueOf(response.getStatusCode());
		String responseCode = String.valueOf(response.getStatusCode());
		responseMap.put("StatusMessage", response.getBody().asString());
		responseMap.put("StatusCode", responseCode);
		cookieMap = response.getCookies();
		
		System.out.println("resCode:" + resCode);
		responseMap.put("StatusMessage", msg);
		responseMap.put("StatusCode", resCode);
		System.out.println("session Id for the  product:"+response.getSessionId());
//		responseMap.put("sessionId", response.getSessionId());
		return responseMap;
	}
	
	public String getArrangementId(Map<String,String> actualResult) {
		String statusMessage = actualResult.get("StatusMessage");
		String arrangementId = JsonUtils.parseJson(statusMessage, "arrangementId").replaceAll("\"", "");
		return arrangementId;
	}	
	
	public static void main(String[] args) {
		DocUploadServiceImpl docUploadServiceImpl = new DocUploadServiceImpl();
//		docUploadServiceImpl.getMultipartResponse("", "","");
	}
	public Map<String, String> getMultipartResponse(String apiURL, String requestBody, Map<String, String> cookieMap){
		Map<String, String> responseMap = new HashMap<String, String>();
		URL url = Thread.currentThread().getContextClassLoader().getResource("jsonRequestFiles/docUpload/Desert.jpg");
		File attachment = new File(url.getPath());
//		File attachment = new File("D:/Lloyds/PCA/Desert.jpg");
//		File attachment = 
		Response response=given().cookies(cookieMap).
        multiPart("123", attachment).
            formParam("caseDetails", requestBody).
            formParam("123", attachment).
                contentType("multipart/form-data").when().post(apiURL).thenReturn();
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
	
	
	
	public Map<String, String> getCookieMap() {
		return cookieMap;
	}

}

