package com.lbg.ib.api.sales.dto.party;

public class C542RequestDTO {

    private String sortCode;
    private String accountNumber;
    private String ocisId;
    private String cbsProductId;

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

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }

    public String getCbsProductId() {
        return cbsProductId;
    }

    public void setCbsProductId(String cbsProductId) {
        this.cbsProductId = cbsProductId;
    }

}
