package com.lbg.ib.api.sales.overdraft.domain;


public class E160Response extends OverdraftManagementResponse{

    private String currencyCode;
    private String debitLimit;
    private String differentialInterestRate1;
    private String overdraftLowerLimit1;
    private String differentialInterestRate2;
    private String overdraftLowerLimit2;
    private String differentialInterestRate3;
    private String productFeatureBeginDate;
    private String featureNextReviewDate;
    private String finalPaymntDueDate;
    private int securityCode;
    private int loanPurposeCode;
    private int lendingAuthenticationCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDebitLimit() {
        return debitLimit;
    }

    public void setDebitLimit(String debitLimit) {
        this.debitLimit = debitLimit;
    }

    public String getDifferentialInterestRate1() {
        return differentialInterestRate1;
    }

    public void setDifferentialInterestRate1(String differentialInterestRate1) {
        this.differentialInterestRate1 = differentialInterestRate1;
    }

    public String getOverdraftLowerLimit1() {
        return overdraftLowerLimit1;
    }

    public void setOverdraftLowerLimit1(String overdraftLowerLimit1) {
        this.overdraftLowerLimit1 = overdraftLowerLimit1;
    }

    public String getDifferentialInterestRate2() {
        return differentialInterestRate2;
    }

    public void setDifferentialInterestRate2(String differentialInterestRate2) {
        this.differentialInterestRate2 = differentialInterestRate2;
    }

    public String getOverdraftLowerLimit2() {
        return overdraftLowerLimit2;
    }

    public void setOverdraftLowerLimit2(String overdraftLowerLimit2) {
        this.overdraftLowerLimit2 = overdraftLowerLimit2;
    }

    public String getDifferentialInterestRate3() {
        return differentialInterestRate3;
    }

    public void setDifferentialInterestRate3(String differentialInterestRate3) {
        this.differentialInterestRate3 = differentialInterestRate3;
    }

    public String getProductFeatureBeginDate() {
        return productFeatureBeginDate;
    }

    public void setProductFeatureBeginDate(String productFeatureBeginDate) {
        this.productFeatureBeginDate = productFeatureBeginDate;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public int getLoanPurposeCode() {
        return loanPurposeCode;
    }

    public void setLoanPurposeCode(int loanPurposeCode) {
        this.loanPurposeCode = loanPurposeCode;
    }

    public int getLendingAuthenticationCode() {
        return lendingAuthenticationCode;
    }

    public void setLendingAuthenticationCode(int lendingAuthenticationCode) {
        this.lendingAuthenticationCode = lendingAuthenticationCode;
    }

    public String getFeatureNextReviewDate() {
        return featureNextReviewDate;
    }

    public void setFeatureNextReviewDate(String featureNextReviewDate) {
        this.featureNextReviewDate = featureNextReviewDate;
    }

    public String getFinalPaymntDueDate() {
        return finalPaymntDueDate;
    }

    public void setFinalPaymntDueDate(String finalPaymntDueDate) {
        this.finalPaymntDueDate = finalPaymntDueDate;
    }

    

    

}
