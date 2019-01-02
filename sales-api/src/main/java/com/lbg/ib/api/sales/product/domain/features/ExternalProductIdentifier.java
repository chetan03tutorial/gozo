package com.lbg.ib.api.sales.product.domain.features;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ExternalProductIdentifier {
    private final String code;
    private final String id;

    public ExternalProductIdentifier(String code, String id) {
        this.code = code;
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
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
