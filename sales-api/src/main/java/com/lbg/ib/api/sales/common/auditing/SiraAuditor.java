package com.lbg.ib.api.sales.common.auditing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.auditing.FraudAuditor;
import com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAO;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;

@Component
public class SiraAuditor implements FraudAuditor {
    public static final int        MAX_LENGTH_100                   = 100;

    public static final String     CURRENT_ACCOUNT_APPLICATION_TYPE = "prim_cur_acc";

    public static final String     SIRA_DECISION_AUDIT_EVENT        = "SZ07";

    public static final String     SIRA_CONNECTIVITY_AUDIT_EVENT    = "SZ06";

    public static final String     FAILURE_SIRA_CALL                = "FAILED";

    public static final String     ACCEPT_SIRA_DECISION             = "ACCEPT";

    public static final String     REFER_SIRA_DECISION              = "REFER";

    public static final String     DECLINE_SIRA_DECISION            = "DECLINE";

    public static final String     SUCCESS_SIRA_SUBMISSION          = "SUBMITTED";

    public static final String     SUCCESS_SIRA_CALL                = "SUCCESS";

    public static final String     ERROR_SIRA_CALL                  = "FAIL";

    public static final String     COMMA                            = ",";

    @Autowired
    ArrangementAuditingDAO<String> arrangementAuditingDAO;

    @Autowired
    ChannelBrandingDAO             channelBranding;

    @Autowired
    private LoggerDAO              logger;

    @Autowired
    SessionManagementDAO           session;

    public SiraAuditor() {
        // Default constructor comments for Avoiding Comments
    }

    public void audit(ProductOfferedDTO productOfferedDTO) {
        // SZ06 audit call
        String auditTextForSIRAConnectivity = prepareSIRAConnectivityAuditText(productOfferedDTO.getSiraScoreDTO(),
                productOfferedDTO.arrangementId());
        arrangementAuditingDAO.audit(auditTextForSIRAConnectivity, SIRA_CONNECTIVITY_AUDIT_EVENT);
        // SZ07 audit call
        if (!isSiraConnectionError(productOfferedDTO)) {
            String auditTextForSIRADecision = prepareSIRADecisionAuditText(productOfferedDTO.getSiraScoreDTO(),
                    productOfferedDTO.arrangementId());
            arrangementAuditingDAO.audit(auditTextForSIRADecision, SIRA_DECISION_AUDIT_EVENT);

        }
    }

    private boolean isSiraConnectionError(ProductOfferedDTO productOfferedDTO) {
        boolean siraConnectionError = false;
        if (productOfferedDTO.getSiraScoreDTO() != null
                && productOfferedDTO.getSiraScoreDTO().getSiraConnectionErrorFlag() != null
                && productOfferedDTO.getSiraScoreDTO().getSiraConnectionErrorFlag()) {
            siraConnectionError = true;
        }
        return siraConnectionError;
    }

    public void audit(ActivateProductResponseDTO activateProductResponseDTO) {
        boolean siraConnectionError = false;
        if (activateProductResponseDTO.getSiraScoreDTO() != null
                && activateProductResponseDTO.getSiraScoreDTO().getSiraConnectionErrorFlag() != null
                && activateProductResponseDTO.getSiraScoreDTO().getSiraConnectionErrorFlag()) {
            siraConnectionError = true;
        }
        // SZ06 audit call
        String auditTextForSIRAConnectivity = prepareSIRAConnectivityAuditText(
                activateProductResponseDTO.getSiraScoreDTO(), activateProductResponseDTO.getArrangementId());
        arrangementAuditingDAO.audit(auditTextForSIRAConnectivity, SIRA_CONNECTIVITY_AUDIT_EVENT);
        // SZ07 audit call
        if (!siraConnectionError) {
            String auditTextForSIRADecision = prepareSIRADecisionAuditText(activateProductResponseDTO.getSiraScoreDTO(),
                    activateProductResponseDTO.getArrangementId());
            arrangementAuditingDAO.audit(auditTextForSIRADecision, SIRA_DECISION_AUDIT_EVENT);

        }
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private String brand() {
        ChannelBrandDTO result = channelBranding.getChannelBrand().getResult();
        if (result == null) {
            return "";
        }
        return result.getBrand();
    }

    private String getPartyId() {
        return session.getUserContext().getPartyId();

    }

    /**
     * Method for preparing audit text for audit event SZ06
     *
     * @param siraScoreDTO
     *            and arrangementId
     * @return
     */
    private String prepareSIRAConnectivityAuditText(SIRAScoreDTO siraScoreDTO, String arrangementId) {
        StringBuilder textToAudit = new StringBuilder(MAX_LENGTH_100);
        if (siraScoreDTO == null) {
            return ",,,";
        }

        String siraConnectivityStatus = SUCCESS_SIRA_CALL;
        if (siraScoreDTO.getSiraConnectionErrorFlag() != null && siraScoreDTO.getSiraConnectionErrorFlag()) {
            siraConnectivityStatus = ERROR_SIRA_CALL;
        }
        textToAudit.append(nullSafe(session.getSessionId()));
        textToAudit.append(COMMA);
        textToAudit.append(nullSafe(arrangementId));
        textToAudit.append(COMMA);
        textToAudit.append(getPartyId());
        textToAudit.append(COMMA);
        textToAudit.append(brand());
        textToAudit.append(COMMA);
        textToAudit.append(CURRENT_ACCOUNT_APPLICATION_TYPE);
        textToAudit.append(COMMA);
        textToAudit.append(siraScoreDTO.getSiraWorkFlowName());
        textToAudit.append(COMMA);
        textToAudit.append(siraConnectivityStatus);
        return textToAudit.toString();
    }

    /**
     * Method for preparing audit text for audit event SZ07
     *
     * @param siraScoreDTO
     *            and arrangementId
     * @return
     */
    private String prepareSIRADecisionAuditText(SIRAScoreDTO siraScoreDTO, String arrangementId) {
        StringBuilder textToAudit = new StringBuilder(MAX_LENGTH_100);
        if (siraScoreDTO == null) {
            return ",,,";
        }
        String siraConnectionServiceStatus = null;
        if (siraScoreDTO.getSiraConnectionErrorFlag() != null && siraScoreDTO.getSiraConnectionErrorFlag()) {
            siraConnectionServiceStatus = ERROR_SIRA_CALL;
        } else {
            siraConnectionServiceStatus = siraScoreDTO.getDecisionResult();
        }
        textToAudit.append(nullSafe(session.getSessionId())).append(COMMA);
        textToAudit.append(nullSafe(arrangementId)).append(COMMA);
        textToAudit.append(getPartyId()).append(COMMA);
        textToAudit.append(brand()).append(COMMA);
        textToAudit.append(CURRENT_ACCOUNT_APPLICATION_TYPE);
        textToAudit.append(COMMA);
        textToAudit.append(siraScoreDTO.getSiraWorkFlowName()).append(COMMA);
        textToAudit.append(siraConnectionServiceStatus).append(COMMA);
        textToAudit.append(siraScoreDTO.getSiraWorkFlowExecutionKey()).append(COMMA);
        textToAudit.append(siraScoreDTO.getTotalRuleMatchCount()).append(COMMA);
        textToAudit.append(siraScoreDTO.getTotalEnquiryMatchCount()).append(COMMA);
        textToAudit.append(COMMA);
        return textToAudit.toString();
    }
}