package com.lbg.ib.api.sales.gozo.tasks.handlers;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.spring.SpringContextHolder;
import com.lbg.ib.api.sales.header.GenericSoapHeaderHandler;
import com.lbg.ib.api.sales.header.builder.SoapHeaderBuilder;
import com.lbg.ib.api.sales.shared.exception.ApplicationException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

public class SoapHeaderManager extends GenericSoapHeaderHandler {

	private List<SoapHeaderBuilder> headerBuilders;

	public boolean handleRequest(MessageContext msgContext) {
		boolean hasAdded = true;
		LoggerDAO logger = SpringContextHolder.getBean(LoggerDAO.class);
		SOAPPart soapPart = ((SOAPMessageContext) msgContext).getMessage().getSOAPPart();
		try {
			for (SoapHeaderBuilder header : headerBuilders) {
				hasAdded = header.handle(soapPart);
				if (!hasAdded) {
					logger.traceLog(this.getClass(), "Not able to add soap header " + header.getClass());
					break;
				}
			}
		} catch (SOAPException soapex) {
			logger.traceLog(this.getClass(), "Error in adding the Headers");
			logger.logException(this.getClass(), soapex);
			throw new ApplicationException(ResponseErrorConstants.ERROR_ADDING_HEADERS, "ERROR_ADDING_HEADERS");
		} catch (Exception ex) {
			logger.traceLog(this.getClass(), "Error in adding the Headers");
			logger.logException(this.getClass(), ex);
			throw new ApplicationException(ResponseErrorConstants.ERROR_ADDING_HEADERS, "ERROR_ADDING_HEADERS");
		}
		logger.traceLog(this.getClass(), "Adding custom soap headers ended successfully");
		return hasAdded;
	}

	@Override
	public QName[] getHeaders() {
		return null;
	}
}
