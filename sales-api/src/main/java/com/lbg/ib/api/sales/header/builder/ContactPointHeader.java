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
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;

@Component
public class ContactPointHeader extends AbstractBaseHeader {

    public void addContactPoint(SOAPPart soapPart, ContactPoint contactPointHeader) {
        try {
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            if (soapHeader != null) {
                SOAPElement contactPoint = soapHeader.addHeaderElement(soapEnvelope.createName("ContactPoint", "soap",
                        "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                String mustReturn = String.valueOf(contactPointHeader.isMustReturn());
                contactPoint.addAttribute(soapEnvelope.createName("mustReturn", "soap",
                        "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"), mustReturn);
                SOAPElement contactPointType = contactPoint.addChildElement(soapEnvelope.createName("ContactPointType",
                        "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                if (contactPointHeader != null && contactPointHeader.getContactPointType() != null
                        && !"".equals(contactPointHeader.getContactPointType())) {
                    contactPointType.setValue(contactPointHeader.getContactPointType());
                } else {
                    contactPointType.setValue(withDefault(toSoapValueFromArc("ContactPointType"), "003"));
                }
                SOAPElement contactPointId = contactPoint.addChildElement(soapEnvelope.createName("ContactPointId",
                        "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                if (contactPointHeader != null && contactPointHeader.getContactPointId() != null
                        && !"".equals(contactPointHeader.getContactPointId())) {
                    contactPointId.setValue(contactPointHeader.getContactPointId());
                } else {
                    contactPointId.setValue(withDefault(toSoapValueFromArc("ContactPointId"), "0000777505"));
                }
                SOAPElement applicationId = contactPoint.addChildElement(soapEnvelope.createName("ApplicationId",
                        "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                if (contactPointHeader != null && contactPointHeader.getApplicationId() != null
                        && !"".equals(contactPointHeader.getApplicationId())) {
                    applicationId.setValue(contactPointHeader.getApplicationId());
                } else {
                    applicationId.setValue(
                            withDefault(toSoapValueFromArc(ApplicationAttribute.APPLICATION), "Internet Banking"));
                }
                SOAPElement initialOriginatorType = contactPoint.addChildElement(soapEnvelope.createName(
                        "InitialOriginatorType", "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                if (contactPointHeader != null && contactPointHeader.getInitialOriginatorType() != null
                        && !"".equals(contactPointHeader.getInitialOriginatorType())) {
                    initialOriginatorType.setValue(contactPointHeader.getInitialOriginatorType());
                } else {
                    initialOriginatorType.setValue(withDefault(toSoapValueFromArc("InitialOriginatorType"), "Browser"));
                }
                SOAPElement initialOriginatorId = contactPoint.addChildElement(soapEnvelope.createName(
                        "InitialOriginatorId", "soap", "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                if (contactPointHeader != null && contactPointHeader.getInitialOriginatorId() != null
                        && !"".equals(contactPointHeader.getInitialOriginatorId())) {
                    initialOriginatorId.setValue(contactPointHeader.getInitialOriginatorId());
                } else {
                    initialOriginatorId.setValue(toSoapValueFromArc(ApplicationAttribute.IP_ADDRESS));
                }
                SOAPElement operatorType = contactPoint.addChildElement(soapEnvelope.createName("OperatorType", "soap",
                        "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
                if (contactPointHeader != null && contactPointHeader.getOperatorType() != null
                        && !"".equals(contactPointHeader.getOperatorType())) {
                    operatorType.setValue(contactPointHeader.getOperatorType().getValue());
                } else {
                    operatorType.setValue(withDefault(toSoapValueFromArc("OperatorType"), "Customer"));
                }
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

    public boolean handle(SOAPPart soapPart, Object ... args) throws SOAPException {
        AbstractLBGHeaderMapper lbgHeaderBuilder = getLBGHeaderBuilder();
        ContactPoint contactPoint = (ContactPoint) lbgHeaderBuilder.prepareContactPointHeader();
        addContactPoint(soapPart, contactPoint);
        return true;
    }

    public String name() {
        return HEADER_NAME;
    }

    private static final String HEADER_NAME = "ContactPoint";

}
