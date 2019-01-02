package com.lbg.ib.api.sales.product.domain.eligibility;

import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.validation.AccountTypeValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Validate
public class UpgradeEligibilityRequest {

    @RequiredFieldValidation
    private String                       accountNumber;

    @RequiredFieldValidation
    private String                      sortCode;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    private AccountType                  arrangementType;

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public AccountType getArrangementType() {
        return arrangementType;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public void setArrangementType(AccountType arrangementType) {
        this.arrangementType = arrangementType;
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
