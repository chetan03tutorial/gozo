package com.lbg.ib.api.sales.common.session.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.activate.AssessmentEvidence;
import com.lbg.ib.api.sales.product.domain.activate.CustomerDocument;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;

public class CustomerInfo {

    private String                 title;
    private String                 foreName;
    private String                 surName;
    private String                 sortCode;
    private String                 accountNumber;
    private String                 arrangementId;
    private List<CustomerDocument> customerDocuments;
    private AssessmentEvidence     parentAssessmentEvidence;
    private AssessmentEvidence     primaryAssessmentEvidence;
    private SwitchOptions          intendSwitch;
    private Boolean                intendOverDraft;
    private Condition[]            conditions;
    private String                 mnemonic;

    private BigDecimal             overdraftAmount;
    private String                 colleagueId;
    private String                 originatingSortCode;
    private String                 ocisId;
    private Date                   switchingDate;
    private Boolean                switchSuccess;
    private Boolean                switchApplied;
    private PostalAddressComponent currentAddress;
    private ContactNumber          mobileNumber;
    private ContactNumber          workPhone;
    private ContactNumber          homePhone;
    private String                 customerNumber;
    private String                 email;
    private Date                   dob;
    private String                   userName;

    public Boolean getSwitchApplied() {
        return switchApplied;
    }

    public void setSwitchApplied(Boolean switchApplied) {
        this.switchApplied = switchApplied;
    }

    public Boolean getSwitchSuccess() {
        return switchSuccess;
    }

    public void setSwitchSuccess(Boolean switchSuccess) {
        this.switchSuccess = switchSuccess;
    }

    public Date getSwitchingDate() {
        return switchingDate;
    }

    public void setSwitchingDate(Date switchingDate) {
        this.switchingDate = switchingDate;
    }

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }

    public String getColleagueId() {
        return colleagueId;
    }

    public void setColleagueId(String colleagueId) {
        this.colleagueId = colleagueId;
    }

    public String getOriginatingSortCode() {
        return originatingSortCode;
    }

    public void setOriginatingSortCode(String originatingSortCode) {
        this.originatingSortCode = originatingSortCode;
    }

    public BigDecimal getOverdraftAmount() {
        return overdraftAmount;
    }

    public void setOverdraftAmount(BigDecimal overdraftAmount) {
        this.overdraftAmount = overdraftAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForeName() {
        return foreName;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    public List<CustomerDocument> getCustomerDocuments() {
        return customerDocuments;
    }

    public void setCustomerDocuments(List<CustomerDocument> customerDocuments) {
        this.customerDocuments = customerDocuments;
    }

    public AssessmentEvidence getParentAssessmentEvidence() {
        return parentAssessmentEvidence;
    }

    public void setParentAssessmentEvidence(AssessmentEvidence parentAssessmentEvidence) {
        this.parentAssessmentEvidence = parentAssessmentEvidence;
    }

    public AssessmentEvidence getPrimaryAssessmentEvidence() {
        return primaryAssessmentEvidence;
    }

    public void setPrimaryAssessmentEvidence(AssessmentEvidence primaryAssessmentEvidence) {
        this.primaryAssessmentEvidence = primaryAssessmentEvidence;
    }

    public SwitchOptions getIntendSwitch() {
        return intendSwitch;
    }

    public void setIntendSwitch(SwitchOptions intendSwitch) {
        this.intendSwitch = intendSwitch;
    }

    public Boolean getIntendOverDraft() {
        return intendOverDraft;
    }

    public void setIntendOverDraft(Boolean intendOverDraft) {
        this.intendOverDraft = intendOverDraft;
    }

    public Condition[] getConditions() {
        return conditions;
    }

    public void setConditions(Condition[] conditions) {
        this.conditions = conditions;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public PostalAddressComponent getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(PostalAddressComponent currentAddress) {
        this.currentAddress = currentAddress;
    }

    public ContactNumber getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(ContactNumber mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public ContactNumber getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(ContactNumber workPhone) {
        this.workPhone = workPhone;
    }

    public ContactNumber getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(ContactNumber homePhone) {
        this.homePhone = homePhone;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
