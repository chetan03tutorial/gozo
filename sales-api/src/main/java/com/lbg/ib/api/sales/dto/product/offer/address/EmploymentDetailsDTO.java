package com.lbg.ib.api.sales.dto.product.offer.address;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class EmploymentDetailsDTO {
    private String employerName;
    private String employerAddressLine1;
    private String employerAddressLine2;
    private String employerPostcode;
    private String currentEmploymentYear;
    private String currentEmploymentMonth;

    public EmploymentDetailsDTO(String employerName, String employerAddressLine1, String employerAddressLine2,
            String employerPostcode, String currentEmploymentYear, String currentEmploymentMonth) {
        this.employerName = employerName;
        this.employerAddressLine1 = employerAddressLine1;
        this.employerAddressLine2 = employerAddressLine2;
        this.employerPostcode = employerPostcode;
        this.currentEmploymentYear = currentEmploymentYear;
        this.currentEmploymentMonth = currentEmploymentMonth;
    }

    public String employerName() {
        return employerName;
    }

    public String employerAddressLine1() {
        return employerAddressLine1;
    }

    public String employerAddressLine2() {
        return employerAddressLine2;
    }

    public String employerPostcode() {
        return employerPostcode;
    }

    public String currentEmploymentYear() {
        return currentEmploymentYear;
    }

    public String currentEmploymentMonth() {
        return currentEmploymentMonth;
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
