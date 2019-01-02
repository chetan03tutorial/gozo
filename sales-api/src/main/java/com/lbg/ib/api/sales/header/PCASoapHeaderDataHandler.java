package com.lbg.ib.api.sales.header;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.spring.SpringContextHolder;
import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.model.WSConfiguration;
import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;
import com.lbg.ib.api.sales.header.builder.SoapHeaderBuilder;
import com.lbg.ib.api.sales.shared.context.RequestContext;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.exception.ApplicationException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;


public class PCASoapHeaderDataHandler extends GenericSoapHeaderHandler {

	private static final String SOAP_HEADER_HANDLERS = "soapHeaderHandlers";
	
	private QName headers[];
	
	private HandlerInfo configuration;

	public QName[] getHeaders() {
		return headers;
	}

	@Override
	public boolean handleRequest(MessageContext msgContext) {
		System.out.println(this);
		LoggerDAO logger = SpringContextHolder.getBean(LoggerDAO.class);
		boolean hasAdded = true;
		SOAPPart soapPart = ((SOAPMessageContext) msgContext).getMessage().getSOAPPart();
		try {
			@SuppressWarnings("unchecked")
			Set<AbstractBaseHeader> headerSet = (Set<AbstractBaseHeader>) RequestContext
					.getInRequestContext(SOAP_HEADER_HANDLERS);
			for (SoapHeaderBuilder header : headerSet) {
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
	public void destroy() {
		headers = null;
	}

	@Override
	public void init(HandlerInfo _context) {
		System.out.println(this);
		Map<String,AbstractBaseHeader> headers = _context.getHandlerConfig();
		System.out.println(_context.getHandlerClass());
		//Set<AbstractBaseHeader> headerSet = new LinkedHashSet<AbstractBaseHeader>();
		//ServiceConfiguration configuration = _context.get(InstructionContext.SERVICE_CONFIG, ServiceConfiguration.class);
		/*for (Class<? extends AbstractBaseHeader> clazz : configuration.getHeaders()) {
			AbstractBaseHeader builder = beanLoader.getService(clazz);
			builder.registerHandler(headerSet);
		}
		RequestContext.setInRequestContext(SOAP_HEADER_HANDLERS, headerSet);*/
	}
}
