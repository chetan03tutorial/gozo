package com.lbg.ib.api.sales.dto.product.offer;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class PhoneDTO {
    private final String countryCode;
    private final String telephoneAreaCode;
    private final String phoneNumber;
    private String       extNumber;
    private final String type;

    public PhoneDTO(String countryCode, String telephoneAreaCode, String phoneNumber, String extNumber, String type) {
        this.countryCode = countryCode;
        this.telephoneAreaCode = telephoneAreaCode;
        this.phoneNumber = phoneNumber;
        this.extNumber = extNumber;
        this.type = type;
    }

    public String countryCode() {
        return countryCode;
    }

    public String telephoneAreaCode() {
        return telephoneAreaCode;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public String type() {
        return type;
    }

    public String getExtNumber() {
        return extNumber;
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
