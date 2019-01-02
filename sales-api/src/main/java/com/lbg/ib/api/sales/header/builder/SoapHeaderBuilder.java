package com.lbg.ib.api.sales.header.builder;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

public interface SoapHeaderBuilder {

    boolean handle(SOAPPart soapPart, Object ... args) throws SOAPException;
    String name();
}
