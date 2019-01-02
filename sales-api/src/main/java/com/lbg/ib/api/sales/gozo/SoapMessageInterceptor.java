package com.lbg.ib.api.sales.gozo;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.spring.SpringContextHolder;

@Component
public class SoapMessageInterceptor /*implements Handler*/ {

	private Class<Handler> handler;
	private int order;
	private Map<String, Object> config;
	
	public Class<Handler> getHandler() {
		return handler;
	}
	public void setHandler(Class<Handler> handler) {
		this.handler = handler;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Map<String, Object> getConfig() {
		return config;
	}
	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	

	
	

	/*public boolean handleRequest(MessageContext messagecontext) {
		Map<String, Class<? extends Handler>> config = handlerConfiguration.getHandlerConfig();
		for (Class<? extends Handler> handlerClass : config.values()) {
			Handler handler = SpringContextHolder.getBean(handlerClass);
			handler.handleRequest(messagecontext);
		}
		return true;

	}

	public boolean handleResponse(MessageContext messagecontext) {
		Map<String, Class<? extends Handler>> config = handlerConfiguration.getHandlerConfig();
		for (Class<? extends Handler> handlerClass : config.values()) {
			Handler handler = SpringContextHolder.getBean(handlerClass);
			handler.handleResponse(messagecontext);
		}
		return true;
	}

	public boolean handleFault(MessageContext messagecontext) {
		Map<String, Class<? extends Handler>> config = handlerConfiguration.getHandlerConfig();
		for (Class<? extends Handler> handlerClass : config.values()) {
			Handler handler = SpringContextHolder.getBean(handlerClass);
			handler.handleFault(messagecontext);
		}
		return true;
	}

	public void init(HandlerInfo handlerinfo) {
		this.handlerConfiguration = handlerinfo;
	}

	public void destroy() {
	}

	public QName[] getHeaders() {
		return null;
	}

	public HandlerInfo getHandlerConfiguration() {
		return handlerConfiguration;
	}

	public void setHandlerConfiguration(HandlerInfo handlerConfiguration) {
		this.handlerConfiguration = handlerConfiguration;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}*/
	
	
}
