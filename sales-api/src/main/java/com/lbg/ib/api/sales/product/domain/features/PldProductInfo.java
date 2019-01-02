package com.lbg.ib.api.sales.product.domain.features;

import java.math.BigDecimal;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Created by 8796528 on 11/06/2018.
 */
public class PldProductInfo {

    private BigDecimal amtOverdraft;
    private boolean isAmtOverdraftUpdated;
    private boolean isProductUpdated;
    private boolean isReferUpdated;
    private boolean isAmtOverdraftUpdatedFromNull;
    private boolean isAsmDecisionDecline;

    Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getAmtOverdraft() {
        return amtOverdraft;
    }

    public void setAmtOverdraft(BigDecimal amtOverdraft) {
        this.amtOverdraft = amtOverdraft;
    }

    public boolean getIsAmtOverdraftUpdated() {
        return isAmtOverdraftUpdated;
    }

    public void setIsAmtOverdraftUpdated(boolean isAmtOverdraftUpdated) {
        this.isAmtOverdraftUpdated = isAmtOverdraftUpdated;
    }

    public boolean getIsProductUpdated() {
        return isProductUpdated;
    }

    public void setIsProductUpdated(boolean isProductUpdated) {
        this.isProductUpdated = isProductUpdated;
    }

    public void setIsReferUpdated(boolean isReferUpdated){
        this.isReferUpdated=isReferUpdated;
    }

    public boolean getIsReferUpdated(){
        return this.isReferUpdated;
    }

    public boolean getIsAmtOverdraftUpdatedFromNull() {
        return isAmtOverdraftUpdatedFromNull;
    }

    public void setIsAmtOverdraftUpdatedFromNull(boolean amtOverdraftUpdatedFromNull) {
        isAmtOverdraftUpdatedFromNull = amtOverdraftUpdatedFromNull;
    }

    public boolean getIsAsmDecisionDecline() {
        return isAsmDecisionDecline;
    }

    public void setIsAsmDecisionDecline(boolean isAsmDecisionDecline) {
        this.isAsmDecisionDecline = isAsmDecisionDecline;
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
