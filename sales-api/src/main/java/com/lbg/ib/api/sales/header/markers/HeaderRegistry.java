package com.lbg.ib.api.sales.header.markers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;

@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderRegistry {

	Class<? extends AgnosticService> serviceLocator();
	
	String portNameMethod();
}
