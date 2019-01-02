package com.lbg.ib.api.sales.switching.domain;

import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.shared.validation.IntegerFieldValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_ALPHA_SPACE;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

@Validate
public class SwitchingAccount {
    @RequiredFieldValidation
    @StringFieldValidation(pattern = "[0-9]*", maxLength = ValidationConstants.NUMBER_SIX, minLength = ValidationConstants.NUMBER_SIX)
    private String sortCode;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = "[0-9]*", maxLength = ValidationConstants.NUMBER_TEN, minLength = ValidationConstants.NUMBER_SIX)
    private String accountNumber;

    private String accountName;

    @RequiredFieldValidation
    private String bankName;

    public SwitchingAccount() {
    }

    public SwitchingAccount(String sortCode, String accountNumber, String accountName, String bankName) {
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.bankName = bankName;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override public String toString() {
        return "SwitchingAccount{" + "sortCode='" + sortCode + '\'' + ", accountNumber='" + accountNumber + '\''
                + ", accountName='" + accountName + '\'' + ", bankName='" + bankName + '\'' + '}';
    }
}
