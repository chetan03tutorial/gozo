package com.lbg.ib.sales.stubs.stubbackend.handler.helper;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class RereadableHttpExchange extends HttpExchange {

    private HttpExchange httpExchange;
    private BufferedInputStream bufferedInputStream;

    public RereadableHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public static HttpExchange rereadable(HttpExchange httpExchange){
        return new RereadableHttpExchange(httpExchange);
    }

    @Override
    public Headers getRequestHeaders() {
        return httpExchange.getRequestHeaders();
    }

    @Override
    public Headers getResponseHeaders() {
        return httpExchange.getResponseHeaders();
    }

    @Override
    public URI getRequestURI() {
        return httpExchange.getRequestURI();
    }

    @Override
    public String getRequestMethod() {
        return httpExchange.getRequestMethod();
    }

    @Override
    public HttpContext getHttpContext() {
        return httpExchange.getHttpContext();
    }

    @Override
    public void close() {
        httpExchange.close();
    }

    @Override
    public InputStream getRequestBody() {
        if(bufferedInputStream==null) {
            bufferedInputStream = new BufferedInputStream(httpExchange.getRequestBody());
            bufferedInputStream.mark(Integer.MAX_VALUE);
        } else {
            try {
                bufferedInputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bufferedInputStream;
    }

    @Override
    public OutputStream getResponseBody() {
        return httpExchange.getResponseBody();
    }

    @Override
    public void sendResponseHeaders(int i, long l) throws IOException {
        httpExchange.sendResponseHeaders(i,l);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return httpExchange.getRemoteAddress();
    }

    @Override
    public int getResponseCode() {
        return httpExchange.getResponseCode();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return httpExchange.getLocalAddress();
    }

    @Override
    public String getProtocol() {
        return httpExchange.getProtocol();
    }

    @Override
    public Object getAttribute(String s) {
        return httpExchange.getAttribute(s);
    }

    @Override
    public void setAttribute(String s, Object o) {
        httpExchange.setAttribute(s,o);
    }

    @Override
    public void setStreams(InputStream inputStream, OutputStream outputStream) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }
}
