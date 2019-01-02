/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.arrangement;

import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.validation.AccountTypeValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.List;

@Validate
public class ProductArrangement {
    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    private String                            name;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    private String                            mnemonic;

    private List<ProductArrangementCondition> conditions;

    public ProductArrangement() {
        /* jackson */
    }

    public ProductArrangement(String name, String mnemonic, List<ProductArrangementCondition> conditions) {
        this.name = name;
        this.mnemonic = mnemonic;
        this.conditions = conditions;
    }

    public String getName() {
        return name;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public List<ProductArrangementCondition> getConditions() {
        return conditions;
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
