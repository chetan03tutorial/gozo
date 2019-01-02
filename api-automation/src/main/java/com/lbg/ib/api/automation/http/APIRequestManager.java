package com.lbg.ib.api.automation.http;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.jayway.restassured.RestAssured.given;

@Scope("cucumber-glue")
@Component
public class APIRequestManager {

    @Autowired
    private RequestWrapper requestWrapper;

    @Autowired
    private ResponseWrapper responseWrapper;

    public void callAPI() {
        Response response = null;
        String endPoint = requestWrapper.getEndPoint();
        if (StringUtils.isEmpty(requestWrapper.getRequestBody()) || endPoint.contains("userInfo")) {
            requestWrapper.setCookies(responseWrapper.getCookies());
            if (null != requestWrapper.getCookies()) {
                response = given().cookies(requestWrapper.getCookies()).get(endPoint).then().contentType(ContentType.JSON).extract().response();
            } else {
                response = given().get(endPoint).then().contentType(ContentType.JSON).extract().response();
            }
        } else if (!StringUtils.isEmpty(requestWrapper.getRequestBody())) {
            requestWrapper.setCookies(responseWrapper.getCookies());
            System.out.println("cookies used:" + requestWrapper.getCookies().toString());
            System.out.println("end point to invoke:" + endPoint);
            response = given().cookies(requestWrapper.getCookies()).contentType(ContentType.JSON).body(requestWrapper.getRequestBody()).post(requestWrapper.getEndPoint()).thenReturn();

        } else {
            throw new RuntimeException("Undefined Protocol.Supoorts GET And POST only");

        }

        if (null == response) {
            throw new RuntimeException("Unable to get any response");
        }
        responseWrapper.setResponseCode(String.valueOf(response.getStatusCode()));
        responseWrapper.setCookies(response.getCookies());
        responseWrapper.setResponseBody(response.getBody().asString());
    }

}
