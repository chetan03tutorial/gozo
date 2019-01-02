package com.lbg.ib.api.sales.header;
/*
Created by Rohit.Soni at 04/06/2018 13:32
*/

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

public interface CustomSoapHeader {
    boolean addAdditionalHeaders(SOAPPart soapPart) throws SOAPException;
    String getHeaderName();
}
