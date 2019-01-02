package com.lbg.ib.api.sales.questionnaire.domain.message;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;
import com.lbg.ib.api.sales.questionnaire.domain.message.Product;

@Validate
public class QuestionnaireRequestBean {

    @RequiredFieldValidation
    private String  arrangementId;

    @RequiredFieldValidation
    private String  communicationKey;

    private String  arrangementType;

    private String  applicationType;

    private String  accountNumber;

    private String  channelCode;

    private String  subChannelCode;

    private String  communicationValue;

    private String  customerIdentifier;

    private String  customerType;

    private String  internalUserIdentifier;

    private String  partyRole;

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getArrangementType() {
        return arrangementType;
    }

    public void setArrangementType(String arrangementType) {
        this.arrangementType = arrangementType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getSubChannelCode() {
        return subChannelCode;
    }

    public void setSubChannelCode(String subChannelCode) {
        this.subChannelCode = subChannelCode;
    }

    public String getCommunicationKey() {
        return communicationKey;
    }

    public void setCommunicationKey(String communicationKey) {
        this.communicationKey = communicationKey;
    }

    public String getCommunicationValue() {
        return communicationValue;
    }

    public void setCommunicationValue(String communicationValue) {
        this.communicationValue = communicationValue;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getInternalUserIdentifier() {
        return internalUserIdentifier;
    }

    public void setInternalUserIdentifier(String internalUserIdentifier) {
        this.internalUserIdentifier = internalUserIdentifier;
    }

    public String getPartyRole() {
        return partyRole;
    }

    public void setPartyRole(String partyRole) {
        this.partyRole = partyRole;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

}