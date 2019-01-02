package com.lbg.ib.api.sales.shared.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import com.ibm.ejs.resources.security;
import com.ibm.ws.soap.resources.soap;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.OperatorTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.UNPMechanismTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.SecurityHeaderType;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.UsernameToken;
import com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.domain.BapiHeader;
import com.lloydstsb.ea.dao.header.BAPISOAPHeader;
import com.lloydstsb.ea.dao.header.ContactPointHeader;
import com.lloydstsb.ea.dao.header.ContextBAPIHeader;
import com.lloydstsb.ea.dao.header.ContextOperationalVariables;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.dao.header.SecurityHeader;
import com.lloydstsb.ea.dao.header.ServiceRequestHeader;

public class HeaderServiceUtil {

    public static List<SOAPHeader> prepareSoapHeaders() {
        List<SOAPHeader> soapHeaders = new LinkedList<SOAPHeader>();
        soapHeaders.add(contactPointHeader());
        soapHeaders.add(servicePointHeader());
        soapHeaders.add(securityTypeHeader());
        soapHeaders.add(bapiHeader());
        return soapHeaders;
    }

    private static URI getURI(String url) {
        URI uri = null;
        try {
            uri = new URI(url);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return uri;

    }

    public static SOAPHeader contactPointHeader() {
        SOAPHeader contactPoint = new SOAPHeader();
        contactPoint.setName("ContactPoint");
        contactPoint.setNameSpace(getURI("http://domain/namespace"));
        ContactPoint contactPointValue = new ContactPoint();
        contactPointValue.setMustReturn(false);
        contactPointValue.setContactPointId("278");
        contactPointValue.setInitialOriginatorType("originatorId");
        contactPointValue.setInitialOriginatorType("originatorType");
        contactPointValue.setOperatorType(OperatorTypeEnum.Customer);
        contactPoint.setValue(contactPointValue);
        return contactPoint;
    }

    public static SOAPHeader servicePointHeader() {

        SOAPHeader serviceRequest = new SOAPHeader();
        serviceRequest.setName("ServiceRequest");
        serviceRequest.setNameSpace(getURI("http://domain/namespace"));
        serviceRequest.setPrefix("prefix");
        ServiceRequest serviceRequestValue = new ServiceRequest();
        serviceRequestValue.setMessageId("1223");
        serviceRequestValue.setMustReturn(false);
        serviceRequestValue.setFrom(getURI("http://sandbox.lloyds.com"));
        serviceRequestValue.setServiceName(getURI("http://serviceName.sandbox.lloyds.com"));
        serviceRequest.setValue(serviceRequestValue);

        return serviceRequest;
    }

    public static SOAPHeader securityTypeHeader() {
        SOAPHeader securityHeader = new SOAPHeader();
        securityHeader.setName("Security");
        securityHeader.setNameSpace(getURI("http://domain/namespace"));
        SecurityHeaderType securityHeaderType = new SecurityHeaderType();
        securityHeaderType.setMustReturn(Boolean.TRUE);
        securityHeaderType.setUsernameToken(usernameToken());
        securityHeader.setValue(securityHeaderType);
        return securityHeader;
    }

    private static UsernameToken usernameToken() {

        UsernameToken usernameToken = new UsernameToken();
        usernameToken.setId("usernameId");
        usernameToken.setUsername("username");
        usernameToken.setUserType("userType");
        usernameToken.setUNPMechanismType(UNPMechanismTypeEnum.value1);
        return usernameToken;
    }

    public static SOAPHeader bapiHeader() {
        SOAPHeader bapiHeader = new SOAPHeader();
        bapiHeader.setName("bapiInformation");
        bapiHeader.setNameSpace(getURI("http://domain/namespace"));
        BapiInformation bapiInformation = new BapiInformation();
        bapiInformation.setBAPIId("Bapi");
        bapiInformation.setBAPIOperationalVariables(new OperationalVariables());
        BAPIHeader bapi = new BAPIHeader();
        bapi.setStpartyObo(new HostInformation());
        bapiInformation.setBAPIHeader(bapi);
        bapiHeader.setValue(bapiInformation);
        return bapiHeader;
    }

    public static HeaderData genericHeaderData() {
        return new HeaderData();
    }

}
