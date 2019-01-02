package com.lbg.ib.api.sales.header.markers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;
import com.lbg.ib.api.sales.header.PCASoapHeaderDataHandler;
import com.lbg.ib.api.sales.header.TraceRequestHandler;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;

@Component
@Aspect
public class HeaderRegistryInterceptor {

	@Autowired
	private ModuleContext beanLoader;

	static final String SOAP_HEADER_HANDLERS = "soapHeaderHandlers";

	@Before("@annotation(com.lbg.ib.api.sales.header.markers.HeaderRegistry)")
	public void handleSoapHeader(JoinPoint joinPoint) {
		HeaderRegistry registry = null;
		final Signature signature = joinPoint.getStaticPart().getSignature();
		if (signature instanceof MethodSignature) {

			MethodSignature ms = (MethodSignature) signature;
			Method method = ms.getMethod();
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations) {
				if (HeaderRegistry.class.isAssignableFrom(annotation.getClass())) {
					registry = (HeaderRegistry) annotation;
				}
			}
		}
		String methodName = registry.portNameMethod();
		Class<? extends AgnosticService> serviceLocatorType = registry.serviceLocator();
		AgnosticService serviceLocator = beanLoader.getService(serviceLocatorType);
		String dpPortName = null;
		Method methods = null;
		
		try {
			methods = serviceLocatorType.getMethod(methodName);
			dpPortName = (String) methods.invoke(serviceLocator);
			HandlerRegistry handlerRegistry = serviceLocator.getHandlerRegistry();
			QName portName = new QName(dpPortName);
			List<HandlerInfo> handlerList = new ArrayList<HandlerInfo>();
			handlerList.add(0, new HandlerInfo(PCASoapHeaderDataHandler.class, null, null));
			handlerList.add(1, new HandlerInfo(TraceRequestHandler.class, null, null));
			handlerRegistry.setHandlerChain(portName, handlerList);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
