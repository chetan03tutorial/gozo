package org.gozo.funkysanta.rest.client.model;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;
//import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.*;

public class WebServiceConfiguration {

    private AgnosticService locator;
    private URL address;
    private String operation;
    private Map<String, InterceptorEnvelope> messageInterceptors;
    private Class<? extends AgnosticService> locatorType;
    private final static String SERVICE_NAME_METHOD = "getServiceName";
    private final static String GETTER_METHOD_PREFIX = "get";
    private List<CommandEnvelope> commandEnvelopes;
    private String serviceName;
    //private Map<Class<? extends Command>, Class<?>> commands;


    public WebServiceConfiguration() {
        this.messageInterceptors = new TreeMap<>();
    }

    public AgnosticService getLocator() {
        return locator;
    }

    public void setLocator(AgnosticService locator) {
        this.locator = locator;
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

    public Class<? extends AgnosticService> getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(Class<? extends AgnosticService> locatorType) {
        this.locatorType = locatorType;
    }

    public HandlerRegistry getHandlerRegistry() {
        return locator.getHandlerRegistry();
    }

    public List<CommandEnvelope> getCommandEnvelopes() {
        return commandEnvelopes;
    }

    public void setCommandEnvelopes(List<CommandEnvelope> commandEnvelopes) {
        this.commandEnvelopes = commandEnvelopes;
    }

    public Map<String, InterceptorEnvelope> getMessageInterceptors() {
        return messageInterceptors;
    }

    public void setMessageInterceptors(Map<String, InterceptorEnvelope> messageInterceptors) {
        this.messageInterceptors = messageInterceptors;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
            ex.printStackTrace();
            //throw new ServiceException(new ResponseError(EXTERNAL_SERVICE_UNAVAILABLE, EXTERNAL_SERVICE_UNAVAILABLE));
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




	/*public Map<Class<? extends Command>, Class<?>> getCommands() {
		return commands;
	}*/

	/*public void setCommands(Map<Class<? extends Command>, Class<?>> commands) {
		this.commands = commands;
	}*/
}
