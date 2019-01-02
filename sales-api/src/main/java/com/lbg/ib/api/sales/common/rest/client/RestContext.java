package com.lbg.ib.api.sales.common.rest.client;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestContext {

    private String endPoint;
    private Object requestBody;
    private MultiValueMap<String, String> headers;
    private ConcurrentHashMap<String, String> pathParams;
    private ConcurrentHashMap<String, String> formParams;
    private ConcurrentHashMap<String, String> queryParams;
    private ConcurrentHashMap<String, File> multipartParams;
    private Class<?> responseType;

    private RestContext() {

    }

    private RestContext(SalsaRestContextBuilder builder) {
        this.pathParams = populateMap(builder.pathParams);
        this.formParams = populateMap(builder.formParams);
        this.queryParams = populateMap(builder.queryParams);
        this.headers = builder.headerParams;
        this.requestBody = builder.requestBody;
        this.endPoint = builder.endPoint;
        this.responseType = builder.responseType;

    }

    private ConcurrentHashMap<String, String> populateMap(Map<String, String> parameterMap) {
        ConcurrentHashMap<String, String> emptyMap = new ConcurrentHashMap<String, String>();
        if (parameterMap != null) {
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    emptyMap.putIfAbsent(entry.getKey(), entry.getValue());
                }
            }
        }
        return emptyMap;
    }

    public Map<String, String> pathParams() {
        return this.pathParams;
    }

    public Map<String, File> multipartParams() {
        return this.multipartParams;
    }

    public Map<String, String> formParams() {
        return this.formParams;
    }

    public Map<String, String> queryParams() {
        return this.queryParams;
    }

    public MultiValueMap<String, String> headers() {
        return this.headers;
    }

    public String endpoint() {
        return endPoint;
    }

    public Object requestBody() {
        return requestBody;
    }

    public Class<?> responseType() {
        return responseType;
    }

    public void setHeaders(Map<String, Object> headers) {
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            this.headers.add(entry.getKey(), (String) entry.getValue());
        }
    }

    public static class SalsaRestContextBuilder {

        protected String endPoint;
        protected Object requestBody;
        protected MultiValueMap<String, String> headerParams;
        protected Map<String, String> pathParams;
        protected Map<String, String> formParams;
        protected Map<String, String> queryParams;
        protected Map<String, File> multipartParams;
        protected Class<?> responseType;

        private SalsaRestContextBuilder(String endpoint) {
            this.endPoint = endpoint;
            this.headerParams = new LinkedMultiValueMap<String, String>();
        }

        public static SalsaRestContextBuilder getBuilder(String endpoint) {
            return new SalsaRestContextBuilder(endpoint);
        }

        public SalsaRestContextBuilder requestBody(Object requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public SalsaRestContextBuilder requestHeaders(Map<String, Object> headers) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                this.headerParams.add(entry.getKey(), (String) entry.getValue());
            }
            return this;
        }

        public SalsaRestContextBuilder queryParameters(Map<String, String> queryParameters) {
            this.queryParams = queryParameters;
            return this;
        }

        public SalsaRestContextBuilder pathParameters(Map<String, String> pathParameters) {
            this.pathParams = pathParameters;
            return this;
        }

        public SalsaRestContextBuilder formParameters(Map<String, String> formParameters) {
            this.formParams = formParameters;
            return this;
        }

        public SalsaRestContextBuilder multipartParameters(Map<String, File> multipathParameters) {
            this.multipartParams = multipathParameters;
            return this;
        }

        public RestContext build() {
            return new RestContext(this);
        }
    }

}
