package com.lbg.ib.api.sales.product.domain.activate;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.OPTIONAL_ALPHA_NUMERIC;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.PASSWORD;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Validate
public class InvolvedPartyRole {
    @StringFieldValidation(pattern = OPTIONAL_ALPHA_NUMERIC, maxLength = ValidationConstants.NUMBER_THIRTY, minLength = ValidationConstants.NUMBER_NINE)
    private String userName;
    @StringFieldValidation(pattern = PASSWORD, maxLength = ValidationConstants.NUMBER_FIFTEEN, minLength = ValidationConstants.NUMBER_SIX)
    private String password;

    public InvolvedPartyRole() {
        /* jackson */
    }

    public InvolvedPartyRole(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
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
        return ReflectionToStringBuilder.toStringExclude(this, "password", "userName");
    }
}
