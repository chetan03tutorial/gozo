package com.lbg.ib.api.sales.header;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;

import com.lbg.ib.api.sales.gozo.tasks.Instruction;

public abstract class GenericSoapHeaderHandler implements Handler {

	protected GenericSoapHeaderHandler() {
	}

	public boolean handleRequest(MessageContext messagecontext) {
		return true;
	}

	public boolean handleResponse(MessageContext messagecontext) {
		return true;
	}

	public boolean handleFault(MessageContext messagecontext) {
		return true;
	}

	public void init(HandlerInfo handlerinfo) {
	}

	public void destroy() {
	}

	public abstract QName[] getHeaders();
}