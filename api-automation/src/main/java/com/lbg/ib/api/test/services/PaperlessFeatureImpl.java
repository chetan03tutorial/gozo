/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.test.services;
 
import static com.jayway.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
 
/**
 * Feature file for User Mandate info.
 * @author tkhann
 */
public class PaperlessFeatureImpl {
 
    /**
     * Method for user Mandate Info.
     * @param sessionId String
     * @param apiURL String
     * @return Map<String, String>
     */
    public Map<String, String> getUserMandateInfoFromAPI(String sessionId, String apiURL) {
        Map<String, String> responseMap = new HashMap<String, String>();
        Response response = given().sessionId(sessionId).get(apiURL).then().contentType(ContentType.JSON).extract().response();
        ;
        String msg = response.getBody().asString();
        String resCode = String.valueOf(response.getStatusCode());
        responseMap.put("StatusMessage", msg);
        responseMap.put("StatusCode", resCode);
        return responseMap;
    }
 
    /**
     * Method for post Request API.
     * @param sessionId String
     * @param apiURL String
     * @return Map<String, String>
     */
    public Map<String, String> postRequestAPI(String requestBody, String apiURL) {
        Map<String, String> responseMap = new HashMap<String, String>();
        Response response = given().contentType(ContentType.JSON).body(requestBody).post(apiURL)
                .thenReturn();
        String msg = response.getBody().asString();
        String resCode = String.valueOf(response.getStatusCode());
        responseMap.put("StatusMessage", msg);
        responseMap.put("StatusCode", resCode);
        return responseMap;
    }
}
