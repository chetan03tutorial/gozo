/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.common.auditing;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAO;
import com.lbg.ib.api.sales.dao.threatmatrix.ThreatMatrixDAO;
import com.lbg.ib.api.sales.dto.AFDIStrategiConstants;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
public class ThreatAuditor implements Auditor<ProductOfferedDTO> {
    public static final int                MAX_LENGTH_100                   = 100;
    public static final String             CURRENT_ACCOUNT_APPLICATION_TYPE = "prim_cur_acc";
    public static final String             SUCCESS_TM_CALL                  = "SUCCESS";
    public static final String             ERROR_TM_CALL                    = "ERROR";
    public static final String             FAIL_TM_CALL                     = "FAIL";
    public static final String             COMMA                            = ",";
    private List<String>                   attributeKeys                    = asList("policy_score", "reason_code",
            "enabled_js", "enabled_fl", "enabled_ck", "enabled_im", "css_image_loaded", "image_loaded",
            "true_ip_worst_score", "true_ip_score", "proxy_ip_worst_score", "device_id", "device_first_seen",
            "device_match_result", "device_result", "device_score", "device_worst_score", "fuzzy_device_id",
            "fuzzy_device_first_seen", "fuzzy_device_id_confidence", "fuzzy_device_match_result", "fuzzy_device_result",
            "custom_count_2", "request_result", "request_id", "policy", "device_id_confidence", "transaction_id");

    @Autowired
    private ArrangementAuditingDAO<String> arrangementAuditingDAO;

    @Autowired
    private ThreatMatrixDAO                threatMatrixDAO;

    @Autowired
    private ChannelBrandingDAO             channelBranding;

    @Autowired
    private LoggerDAO                      logger;

    public void audit(ProductOfferedDTO productOffered) {
        try {
            DAOResponse<Map<String, String>> tmResults = threatMatrixDAO.retrieveThreadMatrixResults(
                    productOffered.arrangementId(), CURRENT_ACCOUNT_APPLICATION_TYPE,
                    productOffered.customerIdentifier());
            String deviceIdCallStatus = deviceIdCallStatus(tmResults);
            String text = populateMalwareTextToAudit(tmResults.getResult())
                    + populateRequiredTextToAudit(tmResults.getResult()) + populateAuditTextForAFDI(
                            productOffered.arrangementId(), CURRENT_ACCOUNT_APPLICATION_TYPE, deviceIdCallStatus);
            // AFDI2 Strategic changes starts
            String auditEvent = getAuditEvent(text);
            arrangementAuditingDAO.audit(text, auditEvent);
            // AFDI2 Strategic changes ends
        } catch (Exception e) {
            logger.logException(ThreatAuditor.class, e);
        }
    }

    private String getAuditEvent(String text) {
        String auditEvent = null;
        if (text.contains(SUCCESS_TM_CALL)) {
            auditEvent = AFDIStrategiConstants.EVENT_ID_SUCCESS_TM_CALL;
        } else {
            auditEvent = AFDIStrategiConstants.EVENT_ID_FAILURE_TM_CALL;
        }
        return auditEvent;
    }

    private String populateMalwareTextToAudit(Map<String, String> auditee) {
        StringBuilder textToAudit = new StringBuilder(MAX_LENGTH_100);
        if (auditee == null) {
            return ",,,";
        }
        textToAudit.append(nullSafe(auditee.get("session_id")));
        textToAudit.append(COMMA);
        textToAudit.append(nullSafe(auditee.get("account_login")).replaceFirst("^\\+", "").trim());
        textToAudit.append(COMMA);
        textToAudit.append(channelId());
        textToAudit.append(COMMA);
        return textToAudit.toString();
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private String populateRequiredTextToAudit(Map<String, String> auditee) {
        if (auditee == null) {
            return "";
        }
        StringBuilder textToAudit = new StringBuilder(MAX_LENGTH_100);
        for (Map.Entry<String, String> property : auditee.entrySet()) {
            if (attributeKeys.contains(property.getKey())) {
                String value = property.getValue();
                if (value != null) {
                    textToAudit.append(value);
                }
                textToAudit.append(COMMA);
            }
        }
        return textToAudit.toString();
    }

    private String populateAuditTextForAFDI(String applicationId, String applicationType, String deviceIdCallStatus) {
        StringBuilder textToAudit = new StringBuilder(MAX_LENGTH_100);
        if (!SUCCESS_TM_CALL.equalsIgnoreCase(deviceIdCallStatus)) {
            if (applicationId != null) {
                textToAudit.append(applicationId);
            }
            textToAudit.append(COMMA);
        }
        if (applicationType != null) {
            textToAudit.append(applicationType);
        }
        textToAudit.append(COMMA);
        textToAudit.append(deviceIdCallStatus);

        return textToAudit.toString();
    }

    private String deviceIdCallStatus(DAOResponse<Map<String, String>> tmResults) {
        if (tmResults.getError() != null) {
            return FAIL_TM_CALL;
        } else {
            return tmResults.getResult().keySet().isEmpty() ? ERROR_TM_CALL : SUCCESS_TM_CALL;
        }
    }

    private String channelId() {
        ChannelBrandDTO result = channelBranding.getChannelBrand().getResult();
        if (result == null) {
            return "";
        }
        return result.getChannelId();
    }

}
