package com.lbg.ib.api.sales.gozo.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import org.springframework.stereotype.Component;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;
import com.lbg.ib.api.sales.gozo.model.WebServiceConfiguration;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;

@Component
public class WSconfigurationUtil {

	/*private final static String SERVICE_NAME_METHOD = "getServiceName";

	private final static String EXTERNAL_SERVICE_UNAVAILABLE = "EXTERNAL_SERVICE_UNAVAILABLE";

	private final static String GETTER_METHOD_PREFIX = "get";

	public HandlerRegistry getHandlerRegistry(WebServiceConfiguration configuration) {
		return getServiceLocator(configuration).getHandlerRegistry();
	}

	public Remote getServiceEndpointInterface(WebServiceConfiguration configuration)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, MalformedURLException {

		Class<? extends AgnosticService> locatorType = getServiceLocator(configuration).getClass();
		AgnosticService serviceLocator = getServiceLocator(configuration);
		Method serviceNameMethod = locatorType.getDeclaredMethod(SERVICE_NAME_METHOD);
		QName qName = (QName) serviceNameMethod.invoke(getServiceLocator(configuration));
		String seiMethodName = GETTER_METHOD_PREFIX.concat(qName.getLocalPart());
		Method seiMethod = locatorType.getDeclaredMethod(seiMethodName, URL.class);
		return (Remote) seiMethod.invoke(serviceLocator, configuration.getAddress());
	}

	public AgnosticService getServiceLocator(WebServiceConfiguration configuration) {
		return configuration.getLocator();
	}

	public String getServiceName(WebServiceConfiguration configuration) {
		AgnosticService serviceLocator = getServiceLocator(configuration);
		Class<? extends AgnosticService> locatorType = serviceLocator.getClass();
		Method serviceNameMethod = null;
		QName qName = null;
		try {
			serviceNameMethod = locatorType.getDeclaredMethod(SERVICE_NAME_METHOD);
			qName = (QName) serviceNameMethod.invoke(serviceLocator);
		} catch (NoSuchMethodException ex) {
			throw new ServiceException(new ResponseError(EXTERNAL_SERVICE_UNAVAILABLE, EXTERNAL_SERVICE_UNAVAILABLE));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return qName.getLocalPart();
	}*/
}
