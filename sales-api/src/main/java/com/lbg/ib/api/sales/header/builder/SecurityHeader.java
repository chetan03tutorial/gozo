package com.lbg.ib.api.sales.header.builder;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPPart;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.header.mapper.AbstractLBGHeaderMapper;
import com.lbg.ib.api.sales.shared.exception.ApplicationException;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.SecurityHeaderType;

@Component
public class SecurityHeader extends AbstractBaseHeader {

    private void addSecurityElement(SOAPPart soapPart, SecurityHeaderType securityHeader) {

        try {
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            if (soapHeader != null) {
                SOAPElement securityElement = soapHeader.addHeaderElement(soapEnvelope.createName("Security", "oas",
                        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"));
                String securityMustReturn = String.valueOf(securityHeader.isMustReturn());
                securityElement.addAttribute(soapEnvelope.createName("mustReturn"), securityMustReturn);
                addUsernameToken(soapEnvelope, securityElement, securityHeader);
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

    private void addUsernameToken(SOAPEnvelope soapEnvelope, SOAPElement selemSecurity,
            SecurityHeaderType securityHeader) throws SOAPException {

        SOAPElement userNameToken = selemSecurity.addChildElement(soapEnvelope.createName("UsernameToken", "oas",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"));
        String unpMechanism = securityHeader.getUsernameToken().getUNPMechanismType().toString();

        userNameToken.addAttribute(
                soapEnvelope.createName("UNPMechanismType", "oas",
                        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"),
                unpMechanism);
        String userType = securityHeader.getUsernameToken().getUserType();

        userNameToken.addAttribute(soapEnvelope.createName("UserType", "oas",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"), userType);
        String id = securityHeader.getUsernameToken().getId();

        userNameToken.addAttribute(soapEnvelope.createName("Id"), id);
        String userName = securityHeader.getUsernameToken().getUsername();

        SOAPElement username = userNameToken.addChildElement(soapEnvelope.createName("Username", "oas",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"));
        username.setValue(userName);
    }

    public boolean handle(SOAPPart soapPart, Object... args) {
        AbstractLBGHeaderMapper lbgHeaderBuilder = getLBGHeaderBuilder();
        SecurityHeaderType securityHeader = lbgHeaderBuilder.prepareSecurityRequestHeader();
        addSecurityElement(soapPart, securityHeader);
        return true;
    }

    public String name() {
        return HEADER_NAME;
    }

    private static final String HEADER_NAME = "Security";

}
