package com.lbg.ib.api.sales.gozo.template.resolver;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;
import com.lbg.ib.api.sales.gozo.SoapMessageInterceptor;
import com.lbg.ib.api.sales.gozo.model.WSConfiguration;
import com.lbg.ib.api.sales.gozo.model.WebServiceConfiguration;
import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;

@Component
public class SoapApiConfigurationResolver {

	@Autowired
	private JsonTemplateResolver jsonTemplateResolver;

	@Autowired
	private ModuleContext beanLoader;

	public WebServiceConfiguration resolveSoapApiConfiguration(String location) throws MalformedURLException {
		WSConfiguration wsConfig = jsonTemplateResolver.resolve(location, WSConfiguration.class);
		WebServiceConfiguration configuration = new WebServiceConfiguration();
		configuration.setAddress(beanLoader.getBeanById(wsConfig.getAddress(),URL.class));
		configuration.setOperation(wsConfig.getOperation());
		
		AgnosticService serviceLocator = beanLoader.getService(wsConfig.getLocator());
		configuration.setLocator(serviceLocator);		
				
		configuration.setLocatorType(serviceLocator.getClass());
		
		if(wsConfig.getHeaders() != null) {
			for (Class<AbstractBaseHeader> headerClass : wsConfig.getHeaders()) {
				configuration.addHeader(beanLoader.getService(headerClass));
			}
		}
		if(wsConfig.getMessageInterceptors() != null) {
			for (Class<SoapMessageInterceptor> interceptorClazz : wsConfig.getMessageInterceptors()) {
				configuration.addMessageInterceptor(beanLoader.getService(interceptorClazz));
			}
		}
		
		
		return configuration;
	}
}
