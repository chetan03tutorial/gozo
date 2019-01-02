package com.lbg.ib.sales.stubs.stubbackend.handler;

import com.lbg.ib.sales.stubs.stubbackend.handler.conditions.Condition;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class Stub {
    private int statusCode;
    private String responsePayload;
    private List<Condition> conditions = new ArrayList<Condition>();

    private Stub(int statusCode) {
        this.statusCode = statusCode;
    }

    public static Stub httpOk(){
        return new Stub(200);
    }

    public static Stub httpNotFound(){
        return new Stub(404);
    }

    public Stub withPayload(String response){
        this.responsePayload = response;
        return this;
    }

    public Stub addCondition(Condition condition){
        conditions.add(condition);
        return this;
    }

    public boolean tryReturnStubbedResponse(HttpExchange httpExchange) {
        for (Condition condition : conditions) {
            if(condition.matches(httpExchange)) {
                try {
                    httpExchange.sendResponseHeaders(statusCode, responsePayload.length());
                    OutputStream responseBody = httpExchange.getResponseBody();
                    responseBody.write(responsePayload.getBytes());
                    responseBody.flush();
                    responseBody.close();
                    httpExchange.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Stub stub = (Stub) o;

        return new EqualsBuilder()
                .append(conditions, stub.conditions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(conditions)
                .toHashCode();
    }
}
