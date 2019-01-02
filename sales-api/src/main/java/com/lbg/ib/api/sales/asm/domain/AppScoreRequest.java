package com.lbg.ib.api.sales.asm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

public class AppScoreRequest {
    private String creditScoreRequestNo;
    private ApplicationType applicationType;

    public String getCreditScoreRequestNo() {
        return creditScoreRequestNo;
    }

    public void setCreditScoreRequestNo(String creditScoreRequestNo) {
        this.creditScoreRequestNo = creditScoreRequestNo;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
