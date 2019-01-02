package com.lbg.ib.api.sales.bankwizard.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BankAccountDetails {

    private String accountNo;

    private String sortCode;

    public BankAccountDetails() {
        // Sonar Violations Avoidance
    }

    public BankAccountDetails(String accountNo, String sortCode) {
        this.accountNo = accountNo;
        this.sortCode = sortCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getSortCode() {
        return sortCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BankAccountDetails)) {
            return false;
        }
        BankAccountDetails other = (BankAccountDetails) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(accountNo, other.accountNo).append(sortCode, other.sortCode);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(accountNo).append(sortCode);
        return hashCodeBuilder.toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
        toStringBuilder.append("accountNo", accountNo).append("sortCode", sortCode);
        return toStringBuilder.toString();
    }
}
