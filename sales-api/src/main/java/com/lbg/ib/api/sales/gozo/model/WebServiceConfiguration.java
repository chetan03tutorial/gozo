package com.lbg.ib.api.sales.gozo.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;
import com.lbg.ib.api.sales.gozo.SoapMessageInterceptor;
import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;

public class WebServiceConfiguration {

	private AgnosticService locator;
	private List<AbstractBaseHeader> headers;
	private URL address;
	private String operation;
	private List<SoapMessageInterceptor> messageInterceptors;
	private Class<? extends AgnosticService> locatorType;
	private final static String SERVICE_NAME_METHOD = "getServiceName";
	private final static String EXTERNAL_SERVICE_UNAVAILABLE = "EXTERNAL_SERVICE_UNAVAILABLE";
	private final static String GETTER_METHOD_PREFIX = "get";

	public WebServiceConfiguration() {
		headers = new ArrayList<AbstractBaseHeader>();
		messageInterceptors = new ArrayList<SoapMessageInterceptor>();
	}

	public AgnosticService getLocator() {
		return locator;
	}

	public void setLocator(AgnosticService locator) {
		this.locator = locator;
	}

	public List<AbstractBaseHeader> getHeaders() {
		return headers;
	}

	public void addHeader(AbstractBaseHeader header) {
		headers.add(header);
	}

	public URL getAddress() {
		return address;
	}

	public void setAddress(URL address) throws MalformedURLException {
		this.address = address;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public List<SoapMessageInterceptor> getMessageInterceptors() {
		return messageInterceptors;
	}

	public void addMessageInterceptor(SoapMessageInterceptor messageInterceptor) {
		messageInterceptors.add(messageInterceptor);
	}

	public Class<? extends AgnosticService> getLocatorType() {
		return locatorType;
	}

	public void setLocatorType(Class<? extends AgnosticService> locatorType) {
		this.locatorType = locatorType;
	}

	public HandlerRegistry getHandlerRegistry() {
		return locator.getHandlerRegistry();
	}

	public Remote getServiceEndpointInterface() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, MalformedURLException {
		Method serviceNameMethod = getLocatorType().getDeclaredMethod(SERVICE_NAME_METHOD);
		QName qName = (QName) serviceNameMethod.invoke(locator);
		String seiMethodName = GETTER_METHOD_PREFIX.concat(qName.getLocalPart());
		Method seiMethod = locatorType.getDeclaredMethod(seiMethodName, URL.class);
		return (Remote) seiMethod.invoke(locator, address);
	}

	public String getServiceName() {
		Method serviceNameMethod = null;
		QName qName = null;
		try {
			serviceNameMethod = getLocatorType().getDeclaredMethod(SERVICE_NAME_METHOD);
			qName = (QName) serviceNameMethod.invoke(locator);
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
	}

}
