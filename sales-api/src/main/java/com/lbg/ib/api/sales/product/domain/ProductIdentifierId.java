package com.lbg.ib.api.sales.product.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;

import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Validate
public class ProductIdentifierId {
    @StringFieldValidation(pattern = REQUIRED_NUMERIC, maxLength = 15, message = "The product identifier is not valid.")
    private String value;

    public ProductIdentifierId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductIdentifierId that = (ProductIdentifierId) o;

        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProductIdentifierId" + "value='" + value + '\'' + '}';
    }
}
