package com.lbg.ib.api.sales.overdraft.domain;

public class E170Request extends CbsRequest{

    private String accountNumber;
    private String sortCode;
    private String debitLimitBalance;
    
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
   
    public String getDebitLimitBalance() {
        return debitLimitBalance;
    }
    public void setDebitLimitBalance(String debitLimitBalance) {
        this.debitLimitBalance = debitLimitBalance;
    } 
    
}
