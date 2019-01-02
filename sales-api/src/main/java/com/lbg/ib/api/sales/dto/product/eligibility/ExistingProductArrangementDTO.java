/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 * 
 * The class has used the business object directly instead of individual attributes 
 * since this is not being consumed anywhere else in the REST layer.
 ***********************************************************************/

package com.lbg.ib.api.sales.dto.product.eligibility;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ExistingProductArrangementDTO {

    private final ProductArrangement[] anExistingProductArrangments;

    public ExistingProductArrangementDTO(ProductArrangement[] existingProductArrangments) {
        this.anExistingProductArrangments = existingProductArrangments;

    }

    /**
     * This method should not be used anywhere else in the REST layer & hence
     * though ProductArrangement[] is returned in this method, it is explicitly
     * changed to Object[]
     */
    public Object[] getExistingProductArrangments() {
        return anExistingProductArrangments;
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
