package com.lbg.ib.api.automation.http;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Scope("cucumber-glue")
@Component
public class ResponseWrapper {

    private String responseCode;

    private Map<String, String> cookies;

    private String responseBody;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, String> getCookies() {
        if (this.cookies == null) {
            this.cookies = new HashMap<String, String>();
        }
        return this.cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        if (this.cookies == null) {
            this.cookies = new HashMap<String, String>();
        }
        this.cookies.putAll(cookies);

    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

}
