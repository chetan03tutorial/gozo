package com.lbg.ib.api.sales.header.builder;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPPart;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.gozo.model.WebServiceConfiguration;
import com.lbg.ib.api.sales.header.common.fields.ClientContext;
import com.lbg.ib.api.sales.header.mapper.AbstractLBGHeaderMapper;
import com.lbg.ib.api.sales.shared.context.RequestContext;
import com.lbg.ib.api.sales.shared.exception.ApplicationException;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sso.domain.user.UserContext;

@Component
public class ServiceRequestHeader extends AbstractBaseHeader {
 	
    private void addServiceRequestHeader(SOAPPart soapPart, ServiceRequest serviceRequestHeader) {

        try {
            SOAPHeader soapHeader = soapPart.getEnvelope().getHeader();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            if (soapHeader != null) {

                SOAPElement serviceRequest = soapHeader.addHeaderElement(soapEnvelope.createName("ServiceRequest",
                        "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                String mustReturn = String.valueOf(serviceRequestHeader.isMustReturn());
                serviceRequest.addAttribute(soapEnvelope.createName("mustReturn", "soap",
                        "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"), mustReturn);
                SOAPElement serviceName = serviceRequest.addChildElement(soapEnvelope.createName("ServiceName", "soap",
                        "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                serviceName.setValue(serviceRequestHeader.getServiceName().toString());
                SOAPElement serviceAction = serviceRequest.addChildElement(soapEnvelope.createName("Action", "soap",
                        "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                serviceAction.setValue(serviceRequestHeader.getAction());
                SOAPElement fromElement = serviceRequest.addChildElement(
                        soapEnvelope.createName("From", "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                fromElement.setValue(serviceRequestHeader.getFrom().toString());
                SOAPElement messageId = serviceRequest.addChildElement(soapEnvelope.createName("MessageId", "soap",
                        "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                messageId.setValue(serviceRequestHeader.getMessageId());
            }
        } catch (SOAPException soapex) {
            logger.traceLog(this.getClass(), "Error in adding the Headers");
            logger.logException(this.getClass(), soapex);
            throw new ApplicationException(ResponseErrorConstants.ERROR_ADDING_HEADERS, "ERROR_ADDING_HEADERS");
        } catch (Exception ex) {
            logger.traceLog(this.getClass(), "Error in adding the Headers");
            logger.logException(this.getClass(), ex);
            throw new ApplicationException(ResponseErrorConstants.ERROR_ADDING_HEADERS, "ERROR_ADDING_HEADERS");
        }
    }

    public boolean handle(SOAPPart soapPart, Object ... args) {
        AbstractLBGHeaderMapper lbgHeaderBuilder = getLBGHeaderBuilder();
        WebServiceConfiguration wsConfiguration = (WebServiceConfiguration) args[0];
        String serviceName = wsConfiguration.getServiceName();
        /*String actionName = (String) RequestContext.getInRequestContext("actionName");
        String serviceName = (String) RequestContext.getInRequestContext("serviceName");*/
        ServiceRequest serviceRequest = (ServiceRequest) lbgHeaderBuilder.prepareServiceRequestHeader(serviceName,
                serviceName);
        addServiceRequestHeader(soapPart, serviceRequest);
        return true;
    }

    public String name() {
        return HEADER_NAME;
    }

    /*
     * public void registerWithServiceInformation(String actionName, String
     * serviceName) { RequestContext.setInRequestContext("actionName",
     * actionName); RequestContext.setInRequestContext("serviceName",
     * serviceName); registerHandler(); }
     */

    private static final String HEADER_NAME = "ServiceRequest";

}
