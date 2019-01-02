package com.lbg.ib.api.sales.product.domain.eligibility;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class EligibilityDetails {

    private String mnemonic;
    private Boolean isEligible;
    private String code;
    private String desc;
    private Boolean isOnlineProduct = true;
    private Boolean isVantageEligible;
    private Boolean isSuitalble = false;
    private Boolean isICOBFlag = Boolean.FALSE;

    public EligibilityDetails() {
        // default comments for Sonar violations avoidance.
    }

    public EligibilityDetails(String mnemonic, Boolean isEligible) {
        this.mnemonic = mnemonic;
        this.isEligible = isEligible;
    }

    public EligibilityDetails(String mnemonic, Boolean isEligible, String code, String desc) {
        this.mnemonic = mnemonic;
        this.isEligible = isEligible;
        this.code = code;
        this.desc = desc;
    }

    /**
     * This method used to return the mnemonic value
     * @return the mnemonic
     */
    public String getMnemonic() {
        return mnemonic;
    }

    /**
     * @param mnemonic the mnemonic to set
     */
    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    /**
     * @return the isEligible
     */
    public Boolean getIsEligible() {
        return isEligible;
    }

    /**
     * @param isEligible the isEligible to set
     */
    public void setIsEligible(Boolean isEligible) {
        this.isEligible = isEligible;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getOnlineProduct() {
        return isOnlineProduct;
    }

    public void setOnlineProduct(Boolean onlineProduct) {
        isOnlineProduct = onlineProduct;
    }

    public Boolean getIsVantageEligible() {
        return isVantageEligible;
    }

    public void setIsVantageEligible(Boolean isVantageEligible) {
        this.isVantageEligible = isVantageEligible;
    }

    public Boolean getIsSuitalble() {
        return isSuitalble;
    }

    public void setIsSuitalble(Boolean isSuitalble) {
        this.isSuitalble = isSuitalble;
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

    /**
     * @return the isICOBFlag
     */
    public Boolean getIsICOBFlag() {
        return isICOBFlag;
    }

    /**
     * @param isICOBFlag the isICOBFlag to set
     */
    public void setIsICOBFlag(Boolean isICOBFlag) {
        this.isICOBFlag = isICOBFlag;
    }
}
