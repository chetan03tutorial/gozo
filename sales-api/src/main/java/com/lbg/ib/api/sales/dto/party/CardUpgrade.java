package com.lbg.ib.api.sales.dto.party;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CardUpgrade {
    private String sortCode;
    private String account;
    private String cbsFromProductCode;
    private String cbsToProductCode;
    private String creditRiskScore;
    private String cancelOldOrder;

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCbsFromProductCode() {
        return cbsFromProductCode;
    }

    public void setCbsFromProductCode(String cbsFromProductCode) {
        this.cbsFromProductCode = cbsFromProductCode;
    }

    public String getCbsToProductCode() {
        return cbsToProductCode;
    }

    public void setCbsToProductCode(String cbsToProductCode) {
        this.cbsToProductCode = cbsToProductCode;
    }

    public String getCreditRiskScore() {
        return creditRiskScore;
    }

    public void setCreditRiskScore(String creditRiskScore) {
        this.creditRiskScore = creditRiskScore;
    }

    public String getCancelOldOrder() {
        return cancelOldOrder;
    }

    public void setCancelOldOrder(String cancelOldOrder) {
        this.cancelOldOrder = cancelOldOrder;
    }
}
