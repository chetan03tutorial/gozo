/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.activate;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.List;

import com.lbg.ib.api.shared.validation.Validate;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;

@Validate
public class Activation {
    private InvolvedPartyRole involvedPartyRole;
    private Condition[] conditions;
    private Location location;
    private Overdraft overDraftDetails;
    private AccountSwitching accountSwitchingDetails;
    private Boolean isVantageOpted;
    private String productId;
    private List<CustomerDocument> customerDocuments;
    private AssessmentEvidence parentAssessmentEvidence;
    private AssessmentEvidence primaryAssessmentEvidence;
    private CustomerPendingDetialsJsonReponse customerPendingDetails;
    private String originatingSortCode;
    private String colleagueId;

    private String domain;
    private boolean isSnr;
    private String currentSortCode;
    private String currentColleagueId;
    private String currentDomain;

    private String cbsProductId;
    private String sellerLegalEntity;
    private String accountNumber;
    private String sortCode;
    private String alternateSortCode;

    public Activation() {
        // default comments for Sonar violations avoidance.
    }

    public Activation(InvolvedPartyRole involvedPartyRole, Condition[] conditions, Location location,
            Overdraft overDraftDetails, AccountSwitching accountSwitchingDetails, Boolean isVantageOpted,
            String productId, List<CustomerDocument> customerDocuments, AssessmentEvidence parentAssessmentEvidence,
            AssessmentEvidence primaryAssessmentEvidence, CustomerPendingDetialsJsonReponse customerPendingDetails) {
        this.involvedPartyRole = involvedPartyRole;
        this.conditions = conditions;
        this.location = location;
        this.overDraftDetails = overDraftDetails;
        this.accountSwitchingDetails = accountSwitchingDetails;
        this.isVantageOpted = isVantageOpted;
        this.productId = productId;
        this.customerDocuments = customerDocuments;
        this.parentAssessmentEvidence = parentAssessmentEvidence;
        this.primaryAssessmentEvidence = primaryAssessmentEvidence;
        this.customerPendingDetails = customerPendingDetails;
    }

    public AccountSwitching getAccountSwitchingDetails() {
        return accountSwitchingDetails;
    }

    public Boolean getIsVantageOpted() {
        return isVantageOpted;
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
        return parentAssessmentEvidence;
    }

    public void setParentAssessmentEvidence(AssessmentEvidence parentAssessmentEvidence) {
        this.parentAssessmentEvidence = parentAssessmentEvidence;
    }

    public CustomerPendingDetialsJsonReponse getCustomerPendingDetails() {
        return customerPendingDetails;
    }

    public void setCustomerPendingDetails(CustomerPendingDetialsJsonReponse customerPendingDetails) {
        this.customerPendingDetails = customerPendingDetails;
    }

    public String getOriginatingSortCode() {
        return originatingSortCode;
    }

    public void setOriginatingSortCode(String originatingSortCode) {
        this.originatingSortCode = originatingSortCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public InvolvedPartyRole getInvolvedPartyRole() {
        return involvedPartyRole;
    }

    public Condition[] getConditions() {
        return conditions;
    }

    public Location getLocation() {
        return location;
    }

    public Overdraft getOverDraftDetails() {
        return overDraftDetails;
    }

    public void setIsSnr(boolean isSnr) {
        this.isSnr = isSnr;
    }

    public String getCurrentSortCode() {
        return currentSortCode;
    }

    public void setCurrentSortCode(String currentSortCode) {
        this.currentSortCode = currentSortCode;
    }

    public String getCurrentColleagueId() {
        return currentColleagueId;
    }

    public void setCurrentColleagueId(String currentColleagueId) {
        this.currentColleagueId = currentColleagueId;
    }

    public String getCurrentDomain() {
        return currentDomain;
    }

    public void setCurrentDomain(String currentDomain) {
        this.currentDomain = currentDomain;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getColleagueId() {
        return colleagueId;
    }

    public void setColleagueId(String colleagueId) {
        this.colleagueId = colleagueId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isSnr() {
        return isSnr;
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
        return reflectionToString(this);
    }

}
