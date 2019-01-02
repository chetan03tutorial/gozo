/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.auditing;

import static com.lbg.ib.api.sales.common.auditing.ThreatAuditor.SUCCESS_TM_CALL;

import java.math.BigInteger;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.dao.constants.ArrangementAuditingConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB555AWServMIS;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB555BWServMIS;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.SystemPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.TAudit;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.TMisUpdate;
import com.lloydstsb.ea.context.ClientContext;

@Component
public class ArrangementAuditingDAOImpl implements ArrangementAuditingDAO<String> {

    public static final String     NO_DEVICEID                            = "NoDeviceID";

    public static final int        AFDI_SUB_EVENT_TYPE_SUCCESS            = 1;

    public static final int        AFDI_SUB_EVENT_TYPE_SUCCESS_NODEVICEID = 2;

    public static final int        AFDI_SUB_EVENT_TYPE_FAILURE_OR_ERROR   = 3;

    public static final String     AFDI_EVENT                             = "AFDI";

    public static final BigInteger VALUE_TO_ADD                           = new BigInteger("1");

    public static final String     SIRA_CONNECT_EVENT_CODE                = "729";

    public static final String     SIRA_DECISION_EVENT_CODE               = "730";

    public static final int        SUCCESS_SIRA_CONNECTION                = 4;

    public static final int        FAILED_SIRA_CONNECTION                 = 5;

    public static final int        ACCEPT_SIRA_DECISION                   = 6;

    public static final int        NO_SIRA_DECISION                       = -1;

    public static final int        REFER_SIRA_DECISION                    = 7;

    public static final int        DECLINE_SIRA_DECISION                  = 8;

    public static final int        SUBMIT_SIRA_DECISION                   = 9;

    @Autowired
    private SystemPortType         systemPortType;

    @Autowired
    private SessionManagementDAO   session;

    @Autowired
    private LoggerDAO              logger;

    @TraceLog
    public void audit(String auditMessage, String auditEvent) {
        try {
            StB555AWServMIS mis = new StB555AWServMIS();
            mis.setStheader(stHeader());
            // AFDI2 Strategic changes starts
            if ((ArrangementAuditingConstants.EVENT_ID_SUCCESS_TM_CALL.code()).equalsIgnoreCase(auditEvent)
                    || (ArrangementAuditingConstants.EVENT_ID_FAILURE_TM_CALL.code()).equalsIgnoreCase(auditEvent)) {
                mis.setStaudit(stAudit(auditMessage));
                mis.setAstmisupdate(populateMisAudit(auditMessage));
            } else if ((ArrangementAuditingConstants.EVENT_ID_SIRA_CONNECT.code()).equalsIgnoreCase(auditEvent)
                    || (ArrangementAuditingConstants.EVENT_ID_SIRA_DECISION.code()).equalsIgnoreCase(auditEvent)) {
                mis.setStaudit(stSiraAudit(auditMessage, auditEvent));
                mis.setAstmisupdate(populateSiraMisAudit(auditMessage, auditEvent));
            }
            // AFDI2 Strategic changes ends
            StB555BWServMIS response = systemPortType.b555WServMIS(mis);
            StError sterror = response.getSterror();
            if (sterror != null && sterror.getErrorno() != 0) {
                logger.logError(Integer.toString(sterror.getErrorno()), sterror.getErrormsg(), this.getClass());
            }
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
        }
    }

    private TAudit stAudit(String auditMessage) {
        TAudit audit = new TAudit();
        if (auditMessage.contains(SUCCESS_TM_CALL)) {
            audit.setEvttype(ArrangementAuditingConstants.EVENT_ID_SUCCESS_TM_CALL.code());
        } else {
            audit.setEvttype(ArrangementAuditingConstants.EVENT_ID_FAILURE_TM_CALL.code());
        }
        audit.setEvtlogtext(auditMessage);
        audit.setErrorno(0);
        return audit;
    }

    private TMisUpdate[] populateMisAudit(String auditMessage) {
        TMisUpdate astmisupdate = new TMisUpdate();
        astmisupdate.setEvttype(AFDI_EVENT);
        astmisupdate.setUlValueToAdd(VALUE_TO_ADD);
        if (auditMessage != null && auditMessage.contains(SUCCESS_TM_CALL) && (auditMessage.contains(NO_DEVICEID))) {
            astmisupdate.setLSubtype(AFDI_SUB_EVENT_TYPE_SUCCESS_NODEVICEID);

        } else if (auditMessage != null && auditMessage.contains(SUCCESS_TM_CALL)) {
            astmisupdate.setLSubtype(AFDI_SUB_EVENT_TYPE_SUCCESS);
        } else {
            astmisupdate.setLSubtype(AFDI_SUB_EVENT_TYPE_FAILURE_OR_ERROR);
        }
        return new TMisUpdate[] { astmisupdate };
    }

    private StHeader stHeader() {
        ClientContext clientContext = session.getUserContext().toClientContext();
        StHeader header = new StHeader();
        header.setUseridAuthor(clientContext.getUserId());
        header.setStpartyObo(party(clientContext));
        header.setChanid(clientContext.getChannelId());
        header.setChansecmode(clientContext.getChansecmode());
        header.setSessionid(session.getSessionId());
        header.setUserAgent(clientContext.getUserAgent());
        header.setAcceptLanguage(clientContext.getLanguage());
        header.setInboxidClient(clientContext.getInboxIdClient());
        header.setEncVerNo(new BigInteger("0"));
        header.setChanctxt(new BigInteger("0"));
        return header;
    }

    private StParty party(ClientContext clientContext) {
        StParty party = new StParty();
        party.setPartyid(clientContext.getPartyId());
        party.setHost(clientContext.getHost());
        party.setOcisid(new BigInteger(clientContext.getOsisId()));
        return party;
    }

    private TAudit stSiraAudit(String auditMessage, String auditEvent) {
        TAudit audit = new TAudit();
        audit.setEvttype(auditEvent);
        audit.setEvtlogtext(auditMessage);
        audit.setErrorno(0);
        return audit;
    }

    private TMisUpdate[] populateSiraMisAudit(String auditMessage, String auditEvent) {
        TMisUpdate astmisupdate = new TMisUpdate();
        if ((ArrangementAuditingConstants.EVENT_ID_SIRA_CONNECT.code()).equalsIgnoreCase(auditEvent)) {
            astmisupdate.setEvttype(SIRA_CONNECT_EVENT_CODE);
            int lSubType = (auditMessage != null
                    && auditMessage.contains(ArrangementAuditingConstants.SIRA_SUCCESS.code()))
                            ? SUCCESS_SIRA_CONNECTION : FAILED_SIRA_CONNECTION;
            astmisupdate.setLSubtype(lSubType);
        } else if ((ArrangementAuditingConstants.EVENT_ID_SIRA_DECISION.code()).equalsIgnoreCase(auditEvent)) {
            astmisupdate.setEvttype(SIRA_DECISION_EVENT_CODE);
            int lSubType = getDecisionSubType(auditMessage);
            if (lSubType != NO_SIRA_DECISION) {
                astmisupdate.setLSubtype(lSubType);
            }
        }
        astmisupdate.setUlValueToAdd(VALUE_TO_ADD); // need to confirm
        return new TMisUpdate[] { astmisupdate };
    }

    private int getDecisionSubType(String auditMessage) {
        if (auditMessage != null) {
            if (auditMessage.contains(ArrangementAuditingConstants.ACCEPT.code())) {
                return (ACCEPT_SIRA_DECISION);
            } else if (auditMessage.contains(ArrangementAuditingConstants.REFER.code())) {
                return (REFER_SIRA_DECISION);
            } else if (auditMessage.contains(ArrangementAuditingConstants.DECLINE.code())) {
                return (DECLINE_SIRA_DECISION);
            } else if (auditMessage.contains(ArrangementAuditingConstants.SUBMIT.code())) {
                return (SUBMIT_SIRA_DECISION);
            }
        }
        return (NO_SIRA_DECISION);
    }

    interface SystemLocatorFactory {
        SystemPortType locator() throws ServiceException;
    }

}
