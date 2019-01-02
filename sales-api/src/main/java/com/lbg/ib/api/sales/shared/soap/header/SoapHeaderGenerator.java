package com.lbg.ib.api.sales.shared.soap.header;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.SecurityHeaderType;
import com.lloydstsb.ea.dao.header.BAPISOAPHeader;
import com.lloydstsb.ea.dao.header.ContactPointHeader;
import com.lloydstsb.ea.dao.header.ContextBAPIHeader;
import com.lloydstsb.ea.dao.header.ContextHostInformation;
import com.lloydstsb.ea.dao.header.ContextOperationalVariables;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.dao.header.SecurityHeader;
import com.lloydstsb.ea.dao.header.ServiceRequestHeader;
import com.lloydstsb.ea.dao.header.UsernameToken;

@Component
public class SoapHeaderGenerator {

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private GBOHeaderUtility     headerUtility;

    @Autowired
    private MCAHeaderUtility     mcaheaderUtility;

    public HeaderData prepareHeaderData(final String serviceAction, final String serviceName) {
        return prepareHeaderData(serviceAction, serviceName, false);
    }

    public HeaderData prepareHeaderData(final String serviceAction, final String serviceName, final boolean useServiceName) {

        HeaderData contextualHeader = new HeaderData();
        List<SOAPHeader> headerData = getGenericSoapHeader(serviceAction, serviceName, useServiceName);
        for (SOAPHeader soap : headerData) {
            if ("ContactPoint".equalsIgnoreCase(soap.getName())) {
                contactPointHeader(contextualHeader.getContactPointHeader(), (ContactPoint) soap.getValue());
            }
            if ("ServiceRequest".equalsIgnoreCase(soap.getName())) {
                buildServiceRequestHeader(contextualHeader.getServiceRequestHeader(), (ServiceRequest) soap.getValue());
            }
            if ("Security".equalsIgnoreCase(soap.getName())) {
                buildSecurityHeader(contextualHeader.getSecurityHeader(), (SecurityHeaderType) soap.getValue());
            }
            if ("bapiInformation".equalsIgnoreCase(soap.getName())) {
                buildBapiSoapHeader(contextualHeader.getHeader(), (BapiInformation) soap.getValue());
            }
        }
        return contextualHeader;
    }

    public ContactPointHeader contactPointHeader(ContactPointHeader contactPointHeader, ContactPoint contactPoint) {
        contactPointHeader.setApplicationId(contactPoint.getApplicationId());
        contactPointHeader.setContactPointId(contactPoint.getContactPointId());
        contactPointHeader.setContactPointType(contactPoint.getContactPointType());
        contactPointHeader.setInitialOriginatorType(contactPoint.getInitialOriginatorType());
        contactPointHeader.setOperatorType(contactPoint.getOperatorType().getValue());
        contactPointHeader.setMustReturn(String.valueOf(contactPoint.isMustReturn()));
        return contactPointHeader;
    }

    public ServiceRequestHeader buildServiceRequestHeader(ServiceRequestHeader serviceRequestHeader,
            ServiceRequest serviceRequest) {
        serviceRequestHeader.setMustReturn(String.valueOf(serviceRequest.isMustReturn()));
        serviceRequestHeader.setFrom(serviceRequest.getFrom().toString());
        serviceRequestHeader.setServiceName(serviceRequest.getServiceName().toString());
        serviceRequestHeader.setAction(serviceRequest.getAction());
        serviceRequestHeader.setMessageId(serviceRequest.getMessageId());
        return serviceRequestHeader;
    }

    public SecurityHeader buildSecurityHeader(SecurityHeader securityHeader, SecurityHeaderType securityHeaderType) {
        securityHeader.setUsernameToken(usernameToken(securityHeaderType.getUsernameToken()));
        securityHeader.setMustReturn(String.valueOf(securityHeaderType.isMustReturn()));
        return securityHeader;
    }

    public BAPISOAPHeader buildBapiSoapHeader(BAPISOAPHeader bapiSoapHeader,
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation bapiInformationHeader) {

        ContextBAPIHeader contextBapiHeader = bapiContextBapiHeader(bapiInformationHeader.getBAPIHeader());

        ContextOperationalVariables contextOperationalVariable = buildOperationalVariables(
                bapiInformationHeader.getBAPIOperationalVariables());

        bapiSoapHeader.setBapiId(bapiInformationHeader.getBAPIId());
        bapiSoapHeader.setContextOperationalVariables(contextOperationalVariable);
        bapiSoapHeader.setContextBAPIHeader(contextBapiHeader);

        return bapiSoapHeader;
    }

    private ContextBAPIHeader bapiContextBapiHeader(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader bapiHeaderRequest) {
        ContextBAPIHeader bapiHeader = new ContextBAPIHeader();
        bapiHeader.setChanid(bapiHeaderRequest.getChanid());
        bapiHeader.setChansecMode(bapiHeaderRequest.getChansecmode());
        bapiHeader.setSessionId(bapiHeaderRequest.getSessionid());
        bapiHeader.setIpaddressCaller(bapiHeaderRequest.getIpAddressCaller());
        bapiHeader.setUserAgent(bapiHeaderRequest.getUserAgent());
        bapiHeader.setAcceptLanguage(bapiHeaderRequest.getAcceptLanguage());
        bapiHeader.setInboxidClient(bapiHeaderRequest.getInboxidClient());
        bapiHeader.setUserIdAuthor(bapiHeaderRequest.getUseridAuthor());
        bapiHeader.setContextHostInformation(buildHostInformation(bapiHeaderRequest.getStpartyObo()));

        return bapiHeader;
    }

    private UsernameToken usernameToken(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.UsernameToken that) {
        UsernameToken usernameToken = new UsernameToken();
        usernameToken.setId(that.getId());
        usernameToken.setUsername(that.getUsername());
        usernameToken.setUserType(that.getUserType());
        usernameToken.setUNPMechanismType(that.getUNPMechanismType().toString());
        return usernameToken;
    }

    private ContextHostInformation buildHostInformation(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation hostInformtionHeader) {

        ContextHostInformation hostInformation = new ContextHostInformation();
        hostInformation.setHost(hostInformtionHeader.getHost());
        hostInformation.setOcisId(hostInformtionHeader.getOcisid());
        hostInformation.setPartyId(hostInformtionHeader.getPartyid());
        return hostInformation;
    }

    private ContextOperationalVariables buildOperationalVariables(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables operationalVariablesHeader) {
        ContextOperationalVariables operationalVariables = new ContextOperationalVariables();
        operationalVariables.setBBatchRetry(operationalVariablesHeader.isBBatchRetry());
        operationalVariables.setBForceHostCall(operationalVariablesHeader.isBForceHostCall());
        operationalVariables.setBPopulateCache(operationalVariablesHeader.isBPopulateCache());
        return operationalVariables;
    }

    public List<SOAPHeader> getGenericSoapHeader(final String serviceAction, final String serviceName, final boolean useServiceName) {

        if (null != session.getBranchContext()) {
            return mcaheaderUtility.prepareSoapHeader(serviceAction, serviceName, useServiceName);
        } else {
            return headerUtility.prepareSoapHeader(serviceAction, serviceName, useServiceName);
        }
    }
}
