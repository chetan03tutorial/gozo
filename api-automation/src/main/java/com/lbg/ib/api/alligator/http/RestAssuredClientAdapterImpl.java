package com.lbg.ib.api.alligator.http;

import org.springframework.stereotype.Component;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.lbg.ib.api.alligator.constants.ResponseStatus;
import com.lbg.ib.api.alligator.exception.FrameworkException;
import com.lbg.ib.api.alligator.exception.ResponseError;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

@Component
public class RestAssuredClientAdapterImpl extends AbstractRestClientAdapter {

    public Response doGet(RequestSpecification requestBuilder, ServiceRequest request) {
        Response response = requestBuilder.cookies(request.getCookies()).get(request.getEndPoint()).then().extract().response();
        return inspectResponse(response);
    }

    public Response doPost(RequestSpecification requestBuilder, ServiceRequest request) {
        Response response = requestBuilder.cookies(request.getCookies()).post(request.getEndPoint()).thenReturn();
        return inspectResponse(response);
    }

    public Response doDelete(RequestSpecification requestBuilder, ServiceRequest request) {
        Response response = requestBuilder.cookies(request.getCookies()).delete(request.getEndPoint()).thenReturn();
        return inspectResponse(response);
    }

    public Response doPut(RequestSpecification requestBuilder, ServiceRequest request) {
        Response response = requestBuilder.cookies(request.getCookies()).put(request.getEndPoint()).thenReturn();
        return inspectResponse(response);
    }

    private Response inspectResponse(Response response) {
        if (response.getStatusCode() != ResponseStatus.SUCCESS) {
            logger.info("Response status " + response.getStatusCode());
            logger.info("Response Line Message is " + response.getStatusLine());
            ResponseError error = new ResponseError(String.valueOf(response.getStatusCode()), response.getStatusLine());
            throw new FrameworkException(error);
        }
        return response;
    }
}