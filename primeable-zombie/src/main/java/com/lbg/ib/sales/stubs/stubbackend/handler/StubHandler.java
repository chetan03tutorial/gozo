package com.lbg.ib.sales.stubs.stubbackend.handler;

import com.lbg.ib.sales.stubs.stubbackend.handler.helper.RereadableHttpExchange;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class StubHandler implements HttpHandler {
    private Set<Stub> stubs = new HashSet<Stub>();

//    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpExchange rereadable = RereadableHttpExchange.rereadable(httpExchange);
        for (Stub stub : stubs) {
            if(stub.tryReturnStubbedResponse(rereadable)) {
                return;
            }
        }
        noStubbedResponseFound(rereadable);
    }

    private void noStubbedResponseFound(HttpExchange httpExchange){
        String message = String.format("No stubbed response found for:%n%s%n%s%n%s", httpExchange.getRequestMethod(),
                headersString(httpExchange), requestPayload(httpExchange));
        try {
            httpExchange.sendResponseHeaders(404, message.length());
            OutputStream responseBody = httpExchange.getResponseBody();
            responseBody.write(message.getBytes());
            responseBody.flush();
            responseBody.close();
            httpExchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String requestPayload(HttpExchange httpExchange){
        try {
            return IOUtils.toString(httpExchange.getRequestBody());
        } catch (IOException e) {
            return "Could not read request payload";
        }
    }

    private String headersString(HttpExchange httpExchange) {
        StringBuilder builder = new StringBuilder();
        Headers headers = httpExchange.getRequestHeaders();
        for (String key : headers.keySet()) {
            builder.append(key).append(":").append(headers.getFirst(key)).append("\n");
        }
        return builder.toString();
    }

    public void addStubHttpResponse(Stub stub){
        this.stubs.add(stub);
    }

    public void reset() {
        this.stubs.clear();
    }
}
