package com.lbg.ib.api.sales.dto.product.offer.address;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class UnstructuredPostalAddressDTO {
    private String[] lines;

    public UnstructuredPostalAddressDTO(String... lines) {
        this.lines = lines;
    }

    public String unstructuredAddressLine(int i) {
        return i >= lines.length ? null : lines[i];
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
