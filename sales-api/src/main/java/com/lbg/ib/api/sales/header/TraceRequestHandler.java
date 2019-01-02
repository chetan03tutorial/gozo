package com.lbg.ib.api.sales.header;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.spring.SpringContextHolder;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
public class TraceRequestHandler extends GenericSoapHeaderHandler {

	private QName headers[];

	public QName[] getHeaders() {
		return headers;
	}

	@Override
	public boolean handleRequest(MessageContext msgContext) {
		SessionManagementDAO sessionManager = SpringContextHolder.getBean(SessionManagementDAO.class);
		LoggerDAO serviceLogger = SpringContextHolder.getBean(LoggerDAO.class);
		String hasEnabledTracing = sessionManager.getTraceRequestFlag();
		if (Boolean.valueOf(hasEnabledTracing)) {
			SOAPPart soapPart = ((SOAPMessageContext) msgContext).getMessage().getSOAPPart();
			try {
				SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
				serviceLogger.traceLog(this.getClass(), "=== Outgoing Request XML "+ soapEnvelope);
			} catch (SOAPException e) {
				serviceLogger.traceLog(this.getClass(), "Error in logging the soap request");
				serviceLogger.logException(this.getClass(), e);
			}
			serviceLogger.traceLog(this.getClass(), "Adding custom soap headers ended successfully");
		}

		return true;
	}

	@Override
	public void destroy() {
		headers = null;
	}
}
