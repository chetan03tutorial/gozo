package com.lbg.ib.api.sales.gozo.model;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;
import com.lbg.ib.api.sales.gozo.SoapMessageInterceptor;
import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;

public class WSConfiguration {

	private Class<AgnosticService> locator;
	private Class<AbstractBaseHeader>[] headers;
	private String address;
	private String operation;
	private Class<SoapMessageInterceptor>[] messageInterceptors;

	public Class<AgnosticService> getLocator() {
		return locator;
	}

	public void setLocator(Class<AgnosticService> locator) {
		this.locator = locator;
	}

	public Class<AbstractBaseHeader>[] getHeaders() {
		return headers;
	}

	public void setHeaders(Class<AbstractBaseHeader>[] headers) {
		this.headers = headers;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Class<SoapMessageInterceptor>[] getMessageInterceptors() {
		return messageInterceptors;
	}

	public void setMessageInterceptors(Class<SoapMessageInterceptor>[] messageInterceptors) {
		this.messageInterceptors = messageInterceptors;
	}

}
