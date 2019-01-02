package com.lbg.ib.api.sales.overdraft.domain;

public class E160Request extends CbsRequest{

    private String accountNumber;
    private String sortCode;
    
    
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
