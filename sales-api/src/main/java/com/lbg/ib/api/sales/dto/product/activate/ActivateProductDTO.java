/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dto.product.activate;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.product.domain.activate.AccountSwitching;
import com.lbg.ib.api.sales.product.domain.activate.AssessmentEvidence;
import com.lbg.ib.api.sales.product.domain.activate.CustomerDocument;
import com.lbg.ib.api.sales.product.domain.activate.Location;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;

public class ActivateProductDTO {

    private String arrangementId;
    private String productIdentifier;
    private String productName;
    private String productFamilyIdentifier;
    private String arrangementType;
    private String applicationType;
    private List<ConditionDTO> conditions;
    private String username;
    private String password;
    private Boolean overDraftOpted;
    private BigDecimal odAmountOpted;
    private OverdraftIntrestRates overdraftIntrestRates;
    private Location location;
    private AccountSwitching switchDetails;
    private String sortCode;
    private String appStatus;
    private String siraWorkFlowName;
    private boolean siraEnabledSwitch;
    private List<CustomerDocument> customerDocuments;
    private AssessmentEvidence primaryAssessmentEvidence;
    private AssessmentEvidence parentAssessmentEvidence;
    private boolean isSnr;
    private String originatingSortCode;
    private String colleagueId;

    private String accountNumber;
    private String cbsProductId;
    private String sellerLegalEntity;
    private String accountingSortCode;
    private String alternateSortCode;

    public ActivateProductDTO() {
        // Sonar violation avoidance comments
    }

    public ActivateProductDTO(String arrangementId, String productIdentifier, String productName,
            String productFamilyIdentifier, String arrangementType, String applicationType,
            List<ConditionDTO> conditions, String username, String password, Boolean overDraftOpted,
            BigDecimal odAmountOpted, OverdraftIntrestRates overdraftIntrestRates, Location location,
            AccountSwitching switchDetails, String sortCode, String appStatus, String accountingSortCode) {

        this.arrangementId = arrangementId;
        this.productIdentifier = productIdentifier;
        this.productName = productName;
        this.productFamilyIdentifier = productFamilyIdentifier;
        this.arrangementType = arrangementType;
        this.applicationType = applicationType;
        this.conditions = conditions;
        this.username = username;
        this.password = password;
        this.overDraftOpted = overDraftOpted;
        this.odAmountOpted = odAmountOpted;
        this.overdraftIntrestRates = overdraftIntrestRates;
        this.location = location;
        this.switchDetails = switchDetails;
        this.sortCode = sortCode;
        this.appStatus = appStatus;
        this.accountingSortCode = accountingSortCode;
    }

    public String getArrangementType() {
        return arrangementType;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductFamilyIdentifier() {
        return productFamilyIdentifier;
    }

    public String getPassword() {
        return password;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public List<ConditionDTO> getConditions() {
        return conditions;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getOverDraftOpted() {
        return overDraftOpted;
    }

    public BigDecimal getOdAmountOpted() {
        return odAmountOpted;
    }

    public OverdraftIntrestRates getOverdraftIntrestRates() {
        return overdraftIntrestRates;
    }

    public Location getLocation() {
        return location;
    }

    public AccountSwitching getSwitchDetails() {
        return switchDetails;
    }

    public String getSortCode() {
        return sortCode;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public String getSiraWorkFlowName() {
        return siraWorkFlowName;
    }

    public void setSiraWorkFlowName(String siraWorkFlowName) {
        this.siraWorkFlowName = siraWorkFlowName;
    }

    public void setSiraEnabledSwitch(boolean siraEnabledSwitch) {
        this.siraEnabledSwitch = siraEnabledSwitch;
    }

    public boolean getSiraEnabledSwitch() {
        return siraEnabledSwitch;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductFamilyIdentifier(String productFamilyIdentifier) {
        this.productFamilyIdentifier = productFamilyIdentifier;
    }

    public void setArrangementType(String arrangementType) {
        this.arrangementType = arrangementType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public void setConditions(List<ConditionDTO> conditions) {
        this.conditions = conditions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOverDraftOpted(Boolean overDraftOpted) {
        this.overDraftOpted = overDraftOpted;
    }

    public void setOdAmountOpted(BigDecimal odAmountOpted) {
        this.odAmountOpted = odAmountOpted;
    }

    public void setOverdraftIntrestRates(OverdraftIntrestRates overdraftIntrestRates) {
        this.overdraftIntrestRates = overdraftIntrestRates;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSwitchDetails(AccountSwitching switchDetails) {
        this.switchDetails = switchDetails;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public List<CustomerDocument> getCustomerDocuments() {
        return customerDocuments;
    }

    public void setCustomerDocuments(List<CustomerDocument> customerDocuments) {
        this.customerDocuments = customerDocuments;
    }

    public AssessmentEvidence getPrimaryAssessmentEvidence() {
        return primaryAssessmentEvidence;
    }

    public void setPrimaryAssessmentEvidence(AssessmentEvidence primaryAssessmentEvidence) {
        this.primaryAssessmentEvidence = primaryAssessmentEvidence;
    }

    public AssessmentEvidence getParentAssessmentEvidence() {
        return this.parentAssessmentEvidence;
    }

    public void setParentAssessmentEvidence(AssessmentEvidence parentAssessmentEvidence) {
        this.parentAssessmentEvidence = parentAssessmentEvidence;
    }

    public boolean isSnr() {
        return isSnr;
    }

    public void setSnr(boolean isSnr) {
        this.isSnr = isSnr;
    }

    public String getOriginatingSortCode() {
        return originatingSortCode;
    }

    public void setOriginatingSortCode(String originatingSortCode) {
        this.originatingSortCode = originatingSortCode;
    }

    public String getColleagueId() {
        return colleagueId;
    }

    public void setColleagueId(String colleagueId) {
        this.colleagueId = colleagueId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCbsProductId() {
        return cbsProductId;
    }

    public void setCbsProductId(String cbsProductId) {
        this.cbsProductId = cbsProductId;
    }

    public String getSellerLegalEntity() {
        return sellerLegalEntity;
    }

    public void setSellerLegalEntity(String sellerLegalEntity) {
        this.sellerLegalEntity = sellerLegalEntity;
    }

    public String getAccountingSortCode() {
        return accountingSortCode;
    }

    public void setAccountingSortCode(String accountingSortCode) {
        this.accountingSortCode = accountingSortCode;
    }

    public String getAlternateSortCode() {
        return alternateSortCode;
    }

    public void setAlternateSortCode(String alternateSortCode) {
        this.alternateSortCode = alternateSortCode;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this).append("ActivateProductDTO [arrangementId=" + arrangementId
                + ", productIdentifier=" + productIdentifier + ", productName=" + productName
                + ", productFamilyIdentifier=" + productFamilyIdentifier + ", arrangementType=" + arrangementType
                + ", applicationType=" + applicationType + ", conditions=" + conditions + ", overDraftOpted="
                + overDraftOpted + ", odAmountOpted=" + odAmountOpted + ", overdraftIntrestRates="
                + overdraftIntrestRates + ", location=" + location + ", switchDetails=" + switchDetails + ", sortCode="
                + sortCode + ", appStatus=" + appStatus + "]").toString();
    }

}
