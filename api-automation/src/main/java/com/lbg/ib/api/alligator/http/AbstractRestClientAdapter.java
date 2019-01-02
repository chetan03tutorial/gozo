package com.lbg.ib.api.alligator.http;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.lbg.ib.api.alligator.web.request.HttpMethod;
import com.lbg.ib.api.alligator.web.request.ServiceRequest;

public abstract class AbstractRestClientAdapter implements RestClientAdapter {
    static {
        RestAssured.useRelaxedHTTPSValidation();
    }

    protected final Log logger = LogFactory.getLog(getClass());

    public Response call(ServiceRequest request) {
        Response response = null;

        RequestSpecification requestBuilder = given();
        Map<String, String> pathParam = request.pathParams();
        Map<String, String> formParam = request.formParams();
        Map<String, String> headerParam = request.headers();
        Map<String, File> multipartParam = request.multipartParams();
        Map<String, Object> queryParam = request.queryParams();

        if (pathParam.size() != 0) {
            requestBuilder.pathParams(pathParam);
        }
        if (formParam.size() != 0) {
            requestBuilder.formParameters(formParam);
        }
        if (request.sessionId() != null) {
            requestBuilder.sessionId(request.sessionId());
        }
        if (request.getRequestBody() != null) {
            requestBuilder.body(request.getRequestBody());
        }
        if (headerParam.size() != 0) {
            requestBuilder.headers(headerParam);
        }

        if (queryParam.size() != 0) {
            requestBuilder.queryParameters(request.queryParams());
        }
        if (multipartParam.size() != 0) {
            for (Map.Entry<String, File> fileMap : multipartParam.entrySet()) {
                requestBuilder.multiPart(fileMap.getKey(), fileMap.getValue());
            }
        }
        logRequest(request);
        if (request.getRequestMethod() == HttpMethod.POST)
            response = doPost(requestBuilder, request);

        else if (request.getRequestMethod() == HttpMethod.GET)
            response = doGet(requestBuilder, request);

        else if (request.getRequestMethod() == HttpMethod.DELETE)
            response = doDelete(requestBuilder, request);

        else if (request.getRequestMethod() == HttpMethod.PUT)
            response = doPut(requestBuilder, request);

        logResponse(response);
        return response;
    }

    private void logRequest(ServiceRequest request) {
        logger.info("=================== Endpoint ========================");
        logger.info(request.getEndPoint());
        logger.info("=================== HTTP METHOD ========================");
        logger.info(request.getRequestMethod());
        if (request.getRequestMethod() != HttpMethod.GET) {
            logger.info("=================== RequestBody ========================");
            logger.info(request.getRequestBody());
        }
        if (StringUtils.isNotEmpty(request.sessionId())) {
            logger.info("=================== SessionId ========================");
            logger.info(request.sessionId());
        }
        Map<String, Object> queryParams = request.queryParams();
        if (queryParams.size() != 0) {
            logger.info("=================== Query Parameters ========================");
            for (String queryParam : queryParams.keySet()) {
                logger.info("ParamName=" + queryParam + ", Param Value=" + queryParams.get(queryParam));
            }
        }
        printMap(request.getCookies(), "Cookies");
        printMap(request.headers(), "Headers");
        printMap(request.pathParams(), "Path Parameters");
        printMap(request.formParams(), "Form Parameters");
    }

    private void logResponse(Response response) {
        logger.info("================== JSGM, Response from the service is ==================");
        if (response != null) {
            Map<String, String> cookies = response.getCookies();
            printMap(cookies, "Cookies");
            if (StringUtils.isNotEmpty(response.getSessionId())) {
                logger.info("=============== Session ============ ");
                logger.info(response.getSessionId());
            }

            logger.info("============== Response Status ======================");
            logger.info(String.valueOf(response.getStatusCode()));
            logger.info("============== Response Body ========================");
            logger.info(response.getBody().asString());
        }
    }

    private void printMap(Map<String, String> map, String entity) {
        if (map.size() != 0) {
            logger.info("=================== " + entity + " ========================");
            for (Entry<String, String> entry : map.entrySet()) {
                logger.info("key = " + entry.getKey() + ", value=" + entry.getValue());
            }
        }

    }

    abstract Response doGet(RequestSpecification requestBuilder, ServiceRequest requestWrapper);

    abstract Response doPost(RequestSpecification requestBuilder, ServiceRequest requestWrapper);

    abstract Response doDelete(RequestSpecification requestBuilder, ServiceRequest requestWrapper);

    abstract Response doPut(RequestSpecification requestBuilder, ServiceRequest requestWrapper);

}