package com.lbg.ib.api.sales.gozo;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;

import org.springframework.stereotype.Component;

@Component
public class DefaultSoapMessageInterceptor implements Handler {

	private HandlerInfo handlerInfo;

	public boolean handleRequest(MessageContext messagecontext) {
		Map<Class<? extends Handler>, Handler> config = handlerInfo.getHandlerConfig();
		for (Handler handler : config.values()) {
			handler.handleRequest(messagecontext);
		}
		return true;

	}

	public boolean handleResponse(MessageContext messagecontext) {
		Map<Class<? extends Handler>, Handler> config = handlerInfo.getHandlerConfig();
		for (Handler handler : config.values()) {
			handler.handleResponse(messagecontext);
		}
		return true;
	}

	public boolean handleFault(MessageContext messagecontext) {
		Map<Class<? extends Handler>, Handler> config = handlerInfo.getHandlerConfig();
		if(config == null) {
			return true;
		}
		for (Handler handler : config.values()) {
			handler.handleFault(messagecontext);
		}
		return true;
	}

	public void init(HandlerInfo handlerinfo) {
		this.handlerInfo = handlerinfo;
	}

	public void destroy() {
	}

	public QName[] getHeaders() {
		return null;
	}

	/*
	 * public HandlerInfo getHandlerConfiguration() { return handlerConfiguration; }
	 * 
	 * public void setHandlerConfiguration(HandlerInfo handlerConfiguration) {
	 * this.handlerConfiguration = handlerConfiguration; }
	 */

	/*
	 * public int getOrder() { return order; }
	 * 
	 * public void setOrder(int order) { this.order = order; }
	 */

}
