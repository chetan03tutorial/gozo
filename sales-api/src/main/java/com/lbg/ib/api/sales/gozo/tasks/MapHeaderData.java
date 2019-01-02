package com.lbg.ib.api.sales.gozo.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;

import com.lbg.ib.api.sales.gozo.SoapMessageInterceptor;
import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.model.WebServiceConfiguration;
import com.lbg.ib.api.sales.gozo.tasks.handlers.SoapHeaderManager;
import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;
import com.lbg.ib.api.sales.header.builder.SoapHeaderBuilder;



public class MapHeaderData extends AbstractInstruction{
	
	public MapHeaderData(Phase phase) {
		super(phase);
	}
	

	@Override
	public void processIncomingPhase(InstructionContext _context) {
		/*WebServiceConfiguration configuration = _context.get(InstructionContext.SERVICE_CONFIG, WebServiceConfiguration.class);
		List<HandlerInfo> messageHandlerList = new ArrayList<HandlerInfo>();
		for(SoapMessageInterceptor messageInterceptor : configuration.getMessageInterceptors()) {
			messageHandlerList.add(messageInterceptor.getOrder(), new HandlerInfo(messageInterceptor.getClass(), null, null));
		}
		HandlerRegistry handlerRegistry = configuration.getHandlerRegistry();
		QName portName = new QName(configuration.getServiceName());
		handlerRegistry.setHandlerChain(portName, messageHandlerList);*/
		
		LinkedList<HandlerInfo> messageHandlerList = new LinkedList<HandlerInfo>();
		/*Map<Class<? extends Handler>, Handler> messageHandlers = new LinkedHashMap<Class<? extends Handler>, Handler>();
		//messageHandlers.put(PCA, value)
		if (wsConfiguration.getMessageInterceptors() != null) {
			
			for (SoapMessageInterceptor messageInterceptor : wsConfiguration.getMessageInterceptors()) {
				messageHandlers.put(messageInterceptor.getHandler(),
						beanLoader.getService(messageInterceptor.getHandler()));
			}
		}
		messageHandlerList.add(new HandlerInfo(DefaultSoapMessageInterceptor.class, messageHandlers, null));*/
		
		WebServiceConfiguration wsConfiguration = _context.get(InstructionContext.SERVICE_CONFIG, WebServiceConfiguration.class);
		for (SoapMessageInterceptor messageInterceptor : wsConfiguration.getMessageInterceptors()) {
			messageHandlerList.add(messageInterceptor.getOrder(),new HandlerInfo(messageInterceptor.getHandler(),messageInterceptor.getConfig(),null));
		}
		Map<String, SoapHeaderBuilder> headerBuilders = new HashMap<String,SoapHeaderBuilder>();
		for(AbstractBaseHeader header : wsConfiguration.getHeaders()) {
			headerBuilders.put(header.getClass().getCanonicalName(), header);
		}
		messageHandlerList.add(new HandlerInfo(SoapHeaderManager.class, headerBuilders, null));

		HandlerRegistry handlerRegistry = wsConfiguration.getHandlerRegistry();
		QName portName = new QName(wsConfiguration.getServiceName());
		handlerRegistry.setHandlerChain(portName, messageHandlerList);
	}

	@Override
	public void processOutgoingPhase(InstructionContext _context) {
		return;
	}
	
}
