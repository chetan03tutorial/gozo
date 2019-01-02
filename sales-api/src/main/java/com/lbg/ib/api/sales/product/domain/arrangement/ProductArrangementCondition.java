package com.lbg.ib.api.sales.product.domain.arrangement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ProductArrangementCondition {
    private String  name;
    private Integer key;
    private String  value;

    public ProductArrangementCondition(String name, Integer key, String value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public ProductArrangementCondition() {
        /* jackson */}

    public String getName() {
        return name;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
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
