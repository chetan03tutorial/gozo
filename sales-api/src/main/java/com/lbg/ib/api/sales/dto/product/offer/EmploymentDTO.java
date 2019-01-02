package com.lbg.ib.api.sales.dto.product.offer;

import com.lbg.ib.api.sales.dto.product.offer.address.EmploymentDetailsDTO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class EmploymentDTO {
    private final String               occupation;
    private final String               employmentStatus;
    private final EmploymentDetailsDTO details;

    public EmploymentDTO(String occupation, String employmentStatus) {
        this.occupation = occupation;
        this.employmentStatus = employmentStatus;
        this.details = null;
    }

    public EmploymentDTO(String occupation, String employmentStatus, String employerName, String employerAddressLine1,
            String employerAddressLine2, String employerPostcode, String currentEmploymentYear,
            String currentEmploymentMonth) {
        this.occupation = occupation;
        this.employmentStatus = employmentStatus;
        this.details = new EmploymentDetailsDTO(employerName, employerAddressLine1, employerAddressLine2,
                employerPostcode, currentEmploymentYear, currentEmploymentMonth);
    }

    public String occupation() {
        return occupation;
    }

    public String employmentStatus() {
        return employmentStatus;
    }

    public EmploymentDetailsDTO details() {
        return details;
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
