
package com.lbg.ib.api.sales.dto.product.offer;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class SIRAScoreDTO {
    private String  decisionScore;

    private String  decisionResult;

    private String  decisionReason;

    private String  decisionIdentifier;

    private Boolean siraFraudRefer;

    private Boolean siraIDVRefer;

    private Boolean siraDecisionErrorFlag;

    private Boolean siraDataSubmissionErrorFlag;

    private Boolean siraConnectionErrorFlag;

    private String  siraWorkFlowName;

    private String  siraWorkFlowExecutionKey;

    private String  totalRuleMatchCount;

    private String  totalEnquiryMatchCount;

    private String  totalRuleScore;

    public SIRAScoreDTO() {
        // Sonar avoidance comments
    }

    public SIRAScoreDTO(String decisionScore, String decisionResult, String decisionReason, String decisionIdentifier,
            Boolean siraFraudRefer, Boolean siraIDVRefer, Boolean siraDecisionErrorFlag,
            Boolean siraDataSubmissionErrorFlag, Boolean siraConnectionErrorFlag, String siraWorkFlowName,
            String siraWorkFlowExecutionKey, String totalRuleMatchCount, String totalEnquiryMatchCount,
            String totalRuleScore) {
        super();
        this.decisionScore = decisionScore;
        this.decisionResult = decisionResult;
        this.decisionReason = decisionReason;
        this.decisionIdentifier = decisionIdentifier;
        this.siraFraudRefer = siraFraudRefer;
        this.siraIDVRefer = siraIDVRefer;
        this.siraDecisionErrorFlag = siraDecisionErrorFlag;
        this.siraDataSubmissionErrorFlag = siraDataSubmissionErrorFlag;
        this.siraConnectionErrorFlag = siraConnectionErrorFlag;
        this.siraWorkFlowName = siraWorkFlowName;
        this.siraWorkFlowExecutionKey = siraWorkFlowExecutionKey;
        this.totalRuleMatchCount = totalRuleMatchCount;
        this.totalEnquiryMatchCount = totalEnquiryMatchCount;
        this.totalRuleScore = totalRuleScore;
    }

    public String getDecisionScore() {
        return decisionScore;
    }

    public String getDecisionResult() {
        return decisionResult;
    }

    public void setDecisionResult(String decisionResult) {
        this.decisionResult = decisionResult;
    }

    public String getDecisionReason() {
        return decisionReason;
    }

    public String getDecisionIdentifier() {
        return decisionIdentifier;
    }

    public Boolean getSiraFraudRefer() {
        return siraFraudRefer;
    }

    public Boolean getSiraIDVRefer() {
        return siraIDVRefer;
    }

    public Boolean getSiraDecisionErrorFlag() {
        return siraDecisionErrorFlag;
    }

    public Boolean getSiraDataSubmissionErrorFlag() {
        return siraDataSubmissionErrorFlag;
    }

    public Boolean getSiraConnectionErrorFlag() {
        return siraConnectionErrorFlag;
    }

    public void setSiraConnectionErrorFlag(Boolean siraConnectionErrorFlag) {
        this.siraConnectionErrorFlag = siraConnectionErrorFlag;
    }

    public String getSiraWorkFlowName() {
        return siraWorkFlowName;
    }

    public void setSiraWorkFlowName(String siraWorkFlowName) {
        this.siraWorkFlowName = siraWorkFlowName;
    }

    public String getSiraWorkFlowExecutionKey() {
        return siraWorkFlowExecutionKey;
    }

    public void setSiraWorkFlowExecutionKey(String siraWorkFlowExecutionKey) {
        this.siraWorkFlowExecutionKey = siraWorkFlowExecutionKey;
    }

    public String getTotalRuleMatchCount() {
        return totalRuleMatchCount;
    }

    public void setTotalRuleMatchCount(String totalRuleMatchCount) {
        this.totalRuleMatchCount = totalRuleMatchCount;
    }

    public String getTotalEnquiryMatchCount() {
        return totalEnquiryMatchCount;
    }

    public void setTotalEnquiryMatchCount(String totalEnquiryMatchCount) {
        this.totalEnquiryMatchCount = totalEnquiryMatchCount;
    }

    public String getTotalRuleScore() {
        return totalRuleScore;
    }

    public void setTotalRuleScore(String totalRuleScore) {
        this.totalRuleScore = totalRuleScore;
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

}
