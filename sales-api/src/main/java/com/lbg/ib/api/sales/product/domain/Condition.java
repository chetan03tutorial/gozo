/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.OPTIONAL_ALPHA;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_ALPHA_WITH_UNDERSCORE;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import com.lbg.ib.api.shared.validation.IntegerFieldValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class Condition {
    @RequiredFieldValidation
    @StringFieldValidation(maxLength = 30, pattern = REQUIRED_ALPHA_WITH_UNDERSCORE)
    private String  name;
    @IntegerFieldValidation(max = 99999)
    private Integer key;
    @RequiredFieldValidation
    @StringFieldValidation(maxLength = 100)
    private String  value;

    public Condition() {
        /* jackson */}

    public Condition(String name, Integer key, String value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
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
