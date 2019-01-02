package com.lbg.ib.api.sales.product.domain.features;

import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ProductReference {

    @StringFieldValidation(pattern = "[a-zA-Z0-9_]*", maxLength = ValidationConstants.NUMBER_FIFTY, message = "Please specify a correct Product Reference")
    private String value;

    private String searchType;

    private String productFamily;

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
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

    public ProductReference(){
    }

    public ProductReference(String value) {
        this.value = value;
    }

    public ProductReference(String searchKey, String searchType) {
        this.value = searchKey;
        this.searchType = searchType;
    }

    public String getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }
}
