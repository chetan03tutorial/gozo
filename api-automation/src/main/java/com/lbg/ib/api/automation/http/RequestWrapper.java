package com.lbg.ib.api.automation.http;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Scope("cucumber-glue")
@Component
public class RequestWrapper {

    private String endPoint;

    private String requestBody;

    private Map<String, String> cookies;

    private String requestMethod;

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        if (this.cookies == null) {
            this.cookies = new HashMap<String, String>();
        }
        this.cookies.putAll(cookies);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

}
