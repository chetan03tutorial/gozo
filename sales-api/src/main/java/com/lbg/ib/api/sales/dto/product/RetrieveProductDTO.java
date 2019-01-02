package com.lbg.ib.api.sales.dto.product;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class RetrieveProductDTO {

    private String systemCode;

    private String productFamilyIdentifier;

    public RetrieveProductDTO (String systemCode, String productFamilyIdentifier) {
        this.systemCode = systemCode;
        this.productFamilyIdentifier = productFamilyIdentifier;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public String getProductFamilyIdentifier() {
        return productFamilyIdentifier;
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
        return "RetrieveProductDTO [systemCode=" + systemCode + ", " +
                "productFamilyIdentifier=" + productFamilyIdentifier + "]";
    }
}