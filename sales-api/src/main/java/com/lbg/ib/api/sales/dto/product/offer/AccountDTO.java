package com.lbg.ib.api.sales.dto.product.offer;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class AccountDTO {
    private String sortCode;
    private String number;
    private String host;
    private String accountType;

    public AccountDTO(String sortCode, String number) {
        this.sortCode = sortCode;
        this.number = number;
    }

    public AccountDTO(String sortCode, String number, String host, String accountType) {
        this.sortCode = sortCode;
        this.number = number;
        this.host = host;
        this.accountType = accountType;
    }

    public String number() {
        return number;
    }

    public String sortCode() {
        return sortCode;
    }

    public String host() {
        return host;
    }

    public String accountType() {
        return accountType;
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
