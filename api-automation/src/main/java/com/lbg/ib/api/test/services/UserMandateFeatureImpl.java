/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************
 * 
 */
package com.lbg.ib.api.test.services;

import static com.jayway.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author tkhann
 */
public class UserMandateFeatureImpl {
    /**
     * Use to retrieve response for REST API.
     * @param requestBody String
     * @param apiURL String
     * @return responseMap
     */
    public Map<String, String> getResponseFromAPI(String requestBody, String apiURL) {
        Map<String, String> responseMap = new HashMap<String, String>();
        Response validateTokenResponse = given().contentType(ContentType.JSON).body(requestBody).post(apiURL)
                .thenReturn();
        String sessionId = validateTokenResponse.getSessionId();
        String msg = validateTokenResponse.getBody().asString();
        String resCode = String.valueOf(validateTokenResponse.getStatusCode());
        responseMap.put("StatusMessage", msg);
        responseMap.put("StatusCode", resCode);
        responseMap.put("sessionId", sessionId);
        return responseMap;
    }

    /**
     * Retrieve response from Mandate info API.
     * @param sessionId String
     * @param apiURL String
     * @return responseMap
     */
    public Map<String, String> getUserInfoFromAPI(String sessionId, String apiURL) {
        Map<String, String> responseMap = new HashMap<String, String>();
        Response response = given().sessionId(sessionId).get(apiURL).then().contentType(ContentType.JSON).extract().response();
        ;
        String msg = response.getBody().asString();
        String resCode = String.valueOf(response.getStatusCode());
        responseMap.put("StatusMessage", msg);
        responseMap.put("StatusCode", resCode);
        return responseMap;
    }
}
