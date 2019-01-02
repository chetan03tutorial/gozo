/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.suitability;

import java.util.List;

import com.lbg.ib.api.sales.product.domain.features.ProductAttributes;

/**
 * Class to return suitability of a product and its product features
 */
public class SuitableProductAttribute {

    private boolean                 isSuitable;

    private List<ProductAttributes> productAttributes;

    public SuitableProductAttribute() {
        /* Default constructor */
    }

    public SuitableProductAttribute(boolean isSuitable, List<ProductAttributes> productAttributes) {
        this.isSuitable = isSuitable;
        this.productAttributes = productAttributes;
    }

    public boolean isSuitable() {
        return isSuitable;
    }

    public void setSuitable(boolean isSuitable) {
        this.isSuitable = isSuitable;
    }

    public List<ProductAttributes> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttributes> productAttributes) {
        this.productAttributes = productAttributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isSuitable ? 1231 : 1237);
        result = prime * result + ((productAttributes == null) ? 0 : productAttributes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SuitableProductAttribute other = (SuitableProductAttribute) obj;
        if (isSuitable != other.isSuitable) {
            return false;
        }
        if (productAttributes == null) {
            if (other.productAttributes != null) {
                return false;
            }
        } else if (!productAttributes.equals(other.productAttributes)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SuitableProductAttribute [isSuitable=" + isSuitable + ", productAttributes=" + productAttributes + "]";
    }

}
