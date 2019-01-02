package com.lbg.ib.api.sales.header.builder;

import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.header.common.fields.ClientContext;
import com.lbg.ib.api.sales.header.mapper.AbstractLBGHeaderMapper;
import com.lbg.ib.api.sales.shared.context.RequestContext;
import com.lbg.ib.api.sales.shared.exception.ApplicationException;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

@Component
public class BapiHeader extends AbstractBaseHeader {
	

    public boolean handle(SOAPPart soapPart, Object ... args) throws SOAPException {
        AbstractLBGHeaderMapper lbgHeaderBuilder = getLBGHeaderBuilder();
        BapiInformation bapiInformation = lbgHeaderBuilder.prepareBAPIHeader();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        ClientContext context = (ClientContext) RequestContext.getInRequestContext("ClientContext");
        try {
            addBapiInformationHeaderElement(soapEnvelope, context, bapiInformation);
        } catch (SOAPException soapex) {
            logger.traceLog(this.getClass(), "Error in adding the Headers");
            logger.logException(this.getClass(), soapex);
            throw new ApplicationException(ResponseErrorConstants.ERROR_ADDING_HEADERS, "ERROR_ADDING_HEADERS");
        } catch (Exception ex) {
            logger.traceLog(this.getClass(), "Error in adding the Headers");
            logger.logException(this.getClass(), ex);
            throw new ApplicationException(ResponseErrorConstants.ERROR_ADDING_HEADERS, "ERROR_ADDING_HEADERS");
        }
        return true;
    }

    private void addBapiInformationHeaderElement(SOAPEnvelope envelope, ClientContext context,
            BapiInformation bapiInformation) throws SOAPException {
        SOAPHeader header = envelope.getHeader();
        if (header == null)
            return;
        SOAPElement selemBapiInfo = header.addHeaderElement(
                envelope.createName("bapiInformation", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        if (context == null)
            return;
        if (bapiInformation != null) {
            addBapiInformation(envelope, selemBapiInfo, bapiInformation);
        }
        addBapiHeader(envelope, selemBapiInfo, context);
    }

    private void addBapiHeader(SOAPEnvelope envelope, SOAPElement selemBapiInfo, ClientContext context)
            throws SOAPException {
        SOAPElement selemBapiHeader = selemBapiInfo.addChildElement(
                envelope.createName("BAPIHeader", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        setBapiValue(envelope, selemBapiHeader, "useridAuthor",
                mandatory(toSoapValue(context.getUserId()), "UserId Author"));
        addStPartyObo(envelope, selemBapiHeader, context);
        setBapiValue(envelope, selemBapiHeader, "chanid", mandatory(toSoapValue(context.getChannelId()), "Channel Id"));
        setBapiValue(envelope, selemBapiHeader, "chansecmode",
                mandatory(toSoapValue(context.getChansecmode()), "Channel Sec"));
        setBapiValue(envelope, selemBapiHeader, "sessionid", toSoapValue(context.getSessionId()));
        setBapiValue(envelope, selemBapiHeader, "ipAddressCaller", toSoapValue(context.getIpAddress()));
        setBapiValue(envelope, selemBapiHeader, "userAgent", toSoapValue(context.getUserAgent()));
        setBapiValue(envelope, selemBapiHeader, "acceptLanguage", toSoapValue(context.getLanguage()));
        setBapiValue(envelope, selemBapiHeader, "inboxidClient", toSoapValue(context.getInboxIdClient()));
        if (!"...".equals(ApplicationRequestContext.get("FLE_STATUS"))) {
            setBapiValue(envelope, selemBapiHeader, "encVerNo",
                    mandatory(toSoapValue((String) ApplicationRequestContext.get("FLE_STATUS")), "FLE key version"));
        } else {
            setBapiValue(envelope, selemBapiHeader, "encVerNo", mandatory("0", "FLE key version"));
        }

        addStDeviceApp(envelope, selemBapiHeader);
        String pointOfOrigin = ApplicationRequestContext.get(ApplicationAttribute.POINT_OF_ORIGIN).toString();
        boolean mcaRequest = false;
        if (context.getChannelId().startsWith("B")) {
            mcaRequest = true;
        } else if (context.getChannelId().startsWith("T")) {
            mcaRequest = true;
        }
        if (null != pointOfOrigin && !pointOfOrigin.equalsIgnoreCase("...") || mcaRequest) {
            addStColleagueDetails(envelope, selemBapiHeader, context);
            addStTransactionDetails(envelope, selemBapiHeader, pointOfOrigin);
            addCCTMAuthCode(envelope, selemBapiHeader);
            addCallerLineId(envelope, selemBapiHeader);
        }
    }

    private void addCCTMAuthCode(SOAPEnvelope envelope, SOAPElement selemBapiHeader) throws SOAPException {
        Map colleagueContextMap = getColleagueContextMap();
        if (null != colleagueContextMap && !colleagueContextMap.isEmpty()) {
            String cctmAuthCode = (String) colleagueContextMap.get("CctmAuthCode");
            if (null != cctmAuthCode) {
                setBapiValue(envelope, selemBapiHeader, "cctmauthcd", toSoapValue(cctmAuthCode));
            }
        }
    }

    private void addCallerLineId(SOAPEnvelope envelope, SOAPElement selemBapiHeader) throws SOAPException {
        Map colleagueContextMap = getColleagueContextMap();
        if (null != colleagueContextMap && !colleagueContextMap.isEmpty()) {
            String callerLineId = (String) colleagueContextMap.get("CallerLineId");
            if (null != callerLineId) {
                setBapiValue(envelope, selemBapiHeader, "callerlineid", toSoapValue(callerLineId));
            }
        }
    }

    private void addStColleagueDetails(SOAPEnvelope envelope, SOAPElement selemBapiHeader, ClientContext context)
            throws SOAPException {
        SOAPElement stcolleagueDetails = selemBapiHeader.addChildElement(
                envelope.createName("stcolleaguedetails", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        Map colleagueContextMap = context.getColleagueMap();
        if (!colleagueContextMap.isEmpty()) {
            setBapiValue(envelope, stcolleagueDetails, "colleagueid",
                    mandatory(toSoapValue((String) colleagueContextMap.get("ColleagueId")), "ColleagueId"));
            setBapiValue(envelope, stcolleagueDetails, "roleColleague",
                    mandatory(toSoapValue((String) colleagueContextMap.get("ColleagueRole")), "ColleagueRole"));
        } else {
            Map colleagueCtxtMap = getColleagueContextMap();
            if (colleagueCtxtMap != null && !colleagueCtxtMap.isEmpty()) {
                setBapiValue(envelope, stcolleagueDetails, "colleagueid",
                        mandatory(toSoapValue((String) colleagueCtxtMap.get("ColleagueId")), "ColleagueId"));
                if (null != colleagueCtxtMap.get("ColleagueRole")) {
                    setBapiValue(envelope, stcolleagueDetails, "roleColleague",
                            mandatory(toSoapValue((String) colleagueCtxtMap.get("ColleagueRole")), "ColleagueRole"));
                } else {
                    setBapiValue(envelope, stcolleagueDetails, "roleColleague",
                            mandatory(toSoapValue("BTR"), "ColleagueRole"));
                }
            } else {
                logger.traceLog(this.getClass(),
                        "Default value added for scheduler calls ColleagueId: SYSTEM ColleagueRole: BTR");
                setBapiValue(envelope, stcolleagueDetails, "colleagueid",
                        mandatory(toSoapValue("SYSTEM"), "ColleagueId"));
                setBapiValue(envelope, stcolleagueDetails, "roleColleague",
                        mandatory(toSoapValue("BTR"), "ColleagueRole"));
            }
        }
    }

    private Map getColleagueContextMap() {
        Map colleagueContextMap = null;
        Object colleagueMap = ApplicationRequestContext.get("ColleagueContext");
        if (colleagueMap != null && (colleagueMap instanceof Map)) {
            colleagueContextMap = (HashMap) colleagueMap;
        } else {
            logger.traceLog(this.getClass(), "Colleague context map is either null or not an instance of Map");
        }
        return colleagueContextMap;
    }

    private void addStTransactionDetails(SOAPEnvelope envelope, SOAPElement selemBapiHeader, String pointOfOriginParam)
            throws SOAPException {
        SOAPElement sttransactiondetails = selemBapiHeader.addChildElement(
                envelope.createName("sttransactiondetails", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        Map colleagueContextMap = getColleagueContextMap();
        if (null != colleagueContextMap && !colleagueContextMap.isEmpty()) {
            if ("TELEPHONY".equalsIgnoreCase(pointOfOriginParam)) {
                setBapiValue(envelope, sttransactiondetails, "ucid",
                        toSoapValue((String) colleagueContextMap.get("UCId")));
                setBapiValue(envelope, sttransactiondetails, "workstationid",
                        toSoapValue((String) colleagueContextMap.get("WorkstationId")));
            } else {
                setBapiValue(envelope, sttransactiondetails, "ucid", toSoapValue(((String) (null))));
                setBapiValue(envelope, sttransactiondetails, "workstationid", toSoapValue(((String) (null))));
            }
            if (null != colleagueContextMap.get("ManagerID")) {
                setBapiValue(envelope, sttransactiondetails, "managerid",
                        toSoapValue((String) colleagueContextMap.get("ManagerID")));
            } else {
                setBapiValue(envelope, sttransactiondetails, "managerid",
                        toSoapValue(getDefaultValueForOverride("ManagerID")));
            }
            if (null != colleagueContextMap.get("InputOfficerStatusFlag")) {
                setBapiValue(envelope, sttransactiondetails, "inputofcflgstatuscd",
                        toSoapValue((String) colleagueContextMap.get("InputOfficerStatusFlag")));
            } else {
                setBapiValue(envelope, sttransactiondetails, "inputofcflgstatuscd",
                        toSoapValue(getDefaultValueForOverride("InputOfficerStatusFlag")));
            }
            if (null != colleagueContextMap.get("InputOfficerStatusCode")) {
                setBapiValue(envelope, sttransactiondetails, "inputofcstatuscd",
                        toSoapValue((String) colleagueContextMap.get("InputOfficerStatusCode")));
            } else {
                setBapiValue(envelope, sttransactiondetails, "inputofcstatuscd",
                        toSoapValue(getDefaultValueForOverride("InputOfficerStatusCode")));
            }
            if (null != colleagueContextMap.get("OverrideDetailsCode")) {
                setBapiValue(envelope, sttransactiondetails, "overridedtlscd",
                        toSoapValue((String) colleagueContextMap.get("OverrideDetailsCode")));
            } else {
                setBapiValue(envelope, sttransactiondetails, "overridedtlscd",
                        toSoapValue(getDefaultValueForOverride("OverrideDetailsCode")));
            }
            if (null != colleagueContextMap.get("SkillLevelAccuired")) {
                setBapiValue(envelope, sttransactiondetails, "skllvlacqdcd",
                        toSoapValue((String) colleagueContextMap.get("SkillLevelAccuired")));
            } else {
                setBapiValue(envelope, sttransactiondetails, "skllvlacqdcd",
                        toSoapValue(getDefaultValueForOverride("SkillLevelAccuired")));
            }
        } else {
            setBapiValue(envelope, sttransactiondetails, "ucid", toSoapValue(((String) (null))));
            setBapiValue(envelope, sttransactiondetails, "workstationid", toSoapValue(((String) (null))));
            setBapiValue(envelope, sttransactiondetails, "managerid",
                    toSoapValue(getDefaultValueForOverride("ManagerID")));
            setBapiValue(envelope, sttransactiondetails, "inputofcflgstatuscd",
                    toSoapValue(getDefaultValueForOverride("InputOfficerStatusFlag")));
            setBapiValue(envelope, sttransactiondetails, "inputofcstatuscd",
                    toSoapValue(getDefaultValueForOverride("InputOfficerStatusCode")));
            setBapiValue(envelope, sttransactiondetails, "overridedtlscd",
                    toSoapValue(getDefaultValueForOverride("OverrideDetailsCode")));
            setBapiValue(envelope, sttransactiondetails, "skllvlacqdcd",
                    toSoapValue(getDefaultValueForOverride("SkillLevelAccuired")));
        }
        String hostAppid = getValueFromARC("HostApplicationId");
        if (null != hostAppid && !"...".equalsIgnoreCase(hostAppid)) {
            setBapiValue(envelope, sttransactiondetails, "hostapplicationid", toSoapValue(hostAppid));
        } else {
            setBapiValue(envelope, sttransactiondetails, "hostapplicationid", toSoapValue("DigitalMCA"));
        }
        setNullValInARC("HostApplicationId", null);
        String externalSysId = getValueFromARC("ExternalSystemID");
        if (null != externalSysId && !"...".equalsIgnoreCase(externalSysId)) {
            setBapiValue(envelope, sttransactiondetails, "extsysid", toSoapValue(externalSysId));
        } else {
            setBapiValue(envelope, sttransactiondetails, "extsysid", toSoapValue("19"));
        }
        setNullValInARC("ExternalSystemID", null);
    }

    private void addStPartyObo(SOAPEnvelope envelope, SOAPElement selemBapiHeader, ClientContext context)
            throws SOAPException {
        SOAPElement stpartyobo = selemBapiHeader.addChildElement(
                envelope.createName("stpartyObo", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        setBapiValue(envelope, stpartyobo, "host", toSoapValue(context.getHost()));
        setBapiValue(envelope, stpartyobo, "partyid", mandatory(toSoapValue(context.getPartyId()), "PartyId"));
        setBapiValue(envelope, stpartyobo, "ocisid", mandatory(toSoapValue(context.getOsisId()), "OcisId"));
    }

    private void addStDeviceApp(SOAPEnvelope envelope, SOAPElement selemBapiHeader) throws SOAPException {
        SOAPElement parent = selemBapiHeader.addChildElement(
                envelope.createName("stdeviceapp", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        setBapiValue(envelope, parent, "deviceappname", toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_NAME));
        setBapiValue(envelope, parent, "deviceappversion",
                toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_VERSION));
        setBapiValue(envelope, parent, "deviceplatform",
                toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_PLATFORM));
        setBapiValue(envelope, parent, "devicesubchannel",
                toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_SUB_CHANNEL));
    }

    private void addBapiInformation(SOAPEnvelope envelope, SOAPElement selemBapiInfo, BapiInformation bapiInformation)
            throws SOAPException {
        SOAPElement bapiIdentifier = selemBapiInfo.addChildElement(
                envelope.createName("BAPIId", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        bapiIdentifier.setValue(toSoapValue(bapiInformation.getBAPIId()));
        SOAPElement selemBapiOpVars = selemBapiInfo.addChildElement(envelope.createName("BAPIOperationalVariables",
                "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        OperationalVariables opVars = bapiInformation.getBAPIOperationalVariables();
        SOAPElement bforcedCall = selemBapiOpVars.addChildElement(
                envelope.createName("bForceHostCall", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        bforcedCall.setValue(toSoapValue(opVars.isBForceHostCall()));
        SOAPElement populateCache = selemBapiOpVars.addChildElement(
                envelope.createName("bPopulateCache", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        populateCache.setValue(toSoapValue(opVars.isBPopulateCache()));
        SOAPElement batchRetry = selemBapiOpVars.addChildElement(
                envelope.createName("bBatchRetry", "lcsm", "http://www.lloydstsb.com/Schema/Enterprise/LCSM"));
        batchRetry.setValue(toSoapValue(opVars.isBBatchRetry()));
    }

    private void setBapiValue(SOAPEnvelope envelope, SOAPElement parent, String name, String value)
            throws SOAPException {
        javax.xml.soap.Name elementName = envelope.createName(name, "lcsm",
                "http://www.lloydstsb.com/Schema/Enterprise/LCSM");
        SOAPElement element = parent.addChildElement(elementName);
        element.setValue(value);
    }

    public String name() {
        return HEADER_NAME;
    }

    private static final String HEADER_NAME = "bapiInformation";
}
