package com.lbg.ib.api.sales.user.domain;

public class AddParty {

    private boolean isNewParty;
    private boolean isExistingParty;
    private String  cbsProductId;
    private String  sellerLegalEntity;
    private String  accountNumber;
    private String  sortCode;

    public boolean isNewParty() {
        return isNewParty;
    }

    public void setNewParty(boolean isNewParty) {
        this.isNewParty = isNewParty;
    }

    public boolean isExistingParty() {
        return isExistingParty;
    }

    public void setExistingParty(boolean isExistingParty) {
        this.isExistingParty = isExistingParty;
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

}
