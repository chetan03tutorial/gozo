package com.lbg.ib.api.sales.gozo.tasks.handlers;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.ws.webservices.xml.waswebservices.header;
import com.lbg.ib.api.sales.gozo.DefaultSoapMessageInterceptor;
import com.lbg.ib.api.sales.gozo.InstructionProcessor;
import com.lbg.ib.api.sales.gozo.SoapMessageInterceptor;
import com.lbg.ib.api.sales.gozo.context.InstructionContext;
import com.lbg.ib.api.sales.gozo.lang.JoinPointHelper;
import com.lbg.ib.api.sales.gozo.lang.MethodDetailHelper;
import com.lbg.ib.api.sales.gozo.lang.MethodDetails;
import com.lbg.ib.api.sales.gozo.mappers.MessageMapper;
import com.lbg.ib.api.sales.gozo.marker.MessageTransformer;
import com.lbg.ib.api.sales.gozo.marker.MessageValidator;
import com.lbg.ib.api.sales.gozo.marker.ServiceConfig;
import com.lbg.ib.api.sales.gozo.model.WebServiceConfiguration;
import com.lbg.ib.api.sales.gozo.template.resolver.SoapApiConfigurationResolver;
import com.lbg.ib.api.sales.gozo.validators.ServiceValidator;
import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;
import com.lbg.ib.api.sales.header.builder.SoapHeaderBuilder;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;

@Component
@Aspect
public class RestSoapBridgeHandler {

	@Autowired
	private ModuleContext beanLoader;

	@Autowired
	private InstructionProcessor processor;

	@Autowired
	private SoapApiConfigurationResolver resolver;

	@Around("@annotation(com.lbg.ib.api.sales.gozo.marker.ServiceConfig)")
	public Object handle(ProceedingJoinPoint joinPoint) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {

		MethodDetails methodDetails = JoinPointHelper.getMethodDetails(joinPoint);
		Object[] args = MethodDetailHelper.getMethodArguments(methodDetails);

		MessageMapper messageMapper = resolveMessageMapper(methodDetails);
		ServiceValidator serviceValidator = resolveServiceValidator(methodDetails);

		ServiceConfig serviceConfig = (ServiceConfig) MethodDetailHelper.resolveAnnotation(methodDetails,
				ServiceConfig.class);

		WebServiceConfiguration wsConfiguration = resolver.resolveSoapApiConfiguration(serviceConfig.value());

		try {
			Object result = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		LinkedList<HandlerInfo> messageHandlerList = new LinkedList<HandlerInfo>();
		Map<Class<? extends Handler>, Handler> messageHandlers = new LinkedHashMap<Class<? extends Handler>, Handler>();
		//messageHandlers.put(PCA, value)
		if (wsConfiguration.getMessageInterceptors() != null) {
			
			for (SoapMessageInterceptor messageInterceptor : wsConfiguration.getMessageInterceptors()) {
				messageHandlers.put(messageInterceptor.getHandler(),
						beanLoader.getService(messageInterceptor.getHandler()));
			}
		}
		Map<String, SoapHeaderBuilder> headerBuilders = new HashMap<String,SoapHeaderBuilder>();
		for(AbstractBaseHeader header : wsConfiguration.getHeaders()) {
			headerBuilders.put(header.getClass().getCanonicalName(), header);
		}
		messageHandlerList.add(new HandlerInfo(SoapHeaderManager.class, headerBuilders, null));
		messageHandlerList.add(new HandlerInfo(DefaultSoapMessageInterceptor.class, messageHandlers, null));
		HandlerRegistry handlerRegistry = wsConfiguration.getHandlerRegistry();
		QName portName = new QName(wsConfiguration.getServiceName());
		handlerRegistry.setHandlerChain(portName, messageHandlerList);
		InstructionContext context = buildInstructionContext(messageMapper, serviceValidator, wsConfiguration, args);
		processor.process(context);

		return new ResponseBuilderImpl().status(Response.Status.OK).entity(context.get(InstructionContext.SERVICE_RESPONSE)).build();
	}

	private InstructionContext buildInstructionContext(MessageMapper messageMapper, ServiceValidator serviceValidator,
			WebServiceConfiguration serviceConfiguration, Object[] args) {
		InstructionContext _context = new InstructionContext();
		_context.set(InstructionContext.MESSAGE_MAPPER, messageMapper);
		_context.set(InstructionContext.SERVICE_VALIDATOR, serviceValidator);
		_context.set(InstructionContext.METHOD_ARGUMENTS, args);
		_context.set(InstructionContext.SERVICE_CONFIG, serviceConfiguration);
		return _context;
	}

	private ServiceValidator resolveServiceValidator(MethodDetails methodDetails) {
		MessageValidator messageValidator = (MessageValidator) MethodDetailHelper.resolveAnnotation(methodDetails,
				MessageValidator.class);
		if (messageValidator == null) {
			return null;
		}
		return beanLoader.getService(messageValidator.value());
	}

	private MessageMapper resolveMessageMapper(MethodDetails methodDetails) {
		MessageTransformer messageMapperMarker = (MessageTransformer) MethodDetailHelper
				.resolveAnnotation(methodDetails, MessageTransformer.class);
		if (messageMapperMarker == null) {
			return null;
		}
		return beanLoader.getService(messageMapperMarker.value());

	}

	/*
	 * private RequestContext buildRestRequestContext(MessageMapper messageMapper,
	 * ServiceValidator serviceValidator, WSConfiguration serviceConfiguration,
	 * Object[] args) { RequestContext _context = new RequestContext();
	 * _context.setAttribute(InstructionContext.MESSAGE_MAPPER, messageMapper);
	 * _context.setAttribute(InstructionContext.SERVICE_VALIDATOR,
	 * serviceValidator); _context.setAttribute(InstructionContext.METHOD_ARGUMENTS,
	 * args); _context.setAttribute(InstructionContext.SERVICE_CONFIG,
	 * serviceConfiguration); return _context; }
	 */

}
