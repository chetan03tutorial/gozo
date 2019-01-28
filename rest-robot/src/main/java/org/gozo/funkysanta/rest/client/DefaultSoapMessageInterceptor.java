package org.gozo.funkysanta.rest.client;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;

import org.gozo.funkysanta.rest.client.model.InterceptorEnvelope;

public class DefaultSoapMessageInterceptor implements Handler {

    private HandlerInfo handlerInfo;
    private QName[] qNames;
    private Map<Integer, InterceptorEnvelope> config;

    public boolean handleRequest(MessageContext messagecontext) {
        Map<Integer, InterceptorEnvelope> config = handlerInfo.getHandlerConfig();
        for (InterceptorEnvelope handlerEnvelope : config.values()) {
            Handler handler = handlerEnvelope.getHandler();
            handler.init(new HandlerInfo(handler.getClass(), handlerEnvelope.getConfig(), getHeaders()));
            handler.handleRequest(messagecontext);
        }
        return true;
    }

    public boolean handleResponse(MessageContext messagecontext) {
        Map<Integer, InterceptorEnvelope> config = handlerInfo.getHandlerConfig();
        for (InterceptorEnvelope handlerEnvelope : config.values()) {
            Handler handler = handlerEnvelope.getHandler();
            handler.init(new HandlerInfo(handlerEnvelope.getClass(), handlerEnvelope.getConfig(), getHeaders()));
            handler.handleResponse(messagecontext);
        }
        return true;
    }

    public boolean handleFault(MessageContext messagecontext) {
        Map<Integer, InterceptorEnvelope> config = handlerInfo.getHandlerConfig();
        for (InterceptorEnvelope handlerEnvelope : config.values()) {
            Handler handler = handlerEnvelope.getHandler();
            handler.init(new HandlerInfo(handlerEnvelope.getClass(), handlerEnvelope.getConfig(), getHeaders()));
            handler.handleFault(messagecontext);
        }
        return true;
    }

    public void init(HandlerInfo handlerinfo) {
        this.qNames = handlerinfo.getHeaders();
        this.handlerInfo = handlerinfo;
        this.config = handlerInfo.getHandlerConfig();
    }

    public void destroy() {
    }

    public QName[] getHeaders() {
        return qNames;
    }
}
