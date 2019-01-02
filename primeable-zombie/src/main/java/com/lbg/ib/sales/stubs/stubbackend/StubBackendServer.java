package com.lbg.ib.sales.stubs.stubbackend;

import com.lbg.ib.sales.stubs.stubbackend.handler.Stub;
import com.lbg.ib.sales.stubs.stubbackend.handler.StubHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class StubBackendServer {

    private StubHandler httpHandler;
    private final int port;

    public StubBackendServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new StubBackendServer(3434).start();
    }

    public void start() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpHandler = new StubHandler();
        httpServer.createContext("/", httpHandler);
        httpServer.start();
    }

    public StubBackendServer addStub(Stub stub){
        httpHandler.addStubHttpResponse(stub);
        return this;
    }

    public void reset() {
        httpHandler.reset();
    }
}
