/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dto.party;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ClassifyPartyResponseDTO {
    private String  countryName;
    private boolean tinRequired;
    private String  regex;
    private String  taxResidencyType;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isTinRequired() {
        return tinRequired;
    }

    public void setTinRequired(boolean tinRequired) {
        this.tinRequired = tinRequired;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getTaxResidencyType() {
        return taxResidencyType;
    }

    public void setTaxResidencyType(String taxResidencyType) {
        this.taxResidencyType = taxResidencyType;
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
