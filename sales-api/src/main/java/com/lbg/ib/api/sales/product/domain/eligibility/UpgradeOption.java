package com.lbg.ib.api.sales.product.domain.eligibility;

import java.util.List;

public class UpgradeOption {
    private String  mnemonic;
    private Boolean isEligible;
    private String  tariff;
    private Boolean isVantageEligible;
    private String productIdentifier;
    private List<String> cbsProductIds;
    private Boolean isICOBFlag;


    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public Boolean getEligible() {
        return isEligible;
    }

    public void setEligible(Boolean eligible) {
        isEligible = eligible;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public Boolean getVantageEligible() {
        return isVantageEligible;
    }

    public void setVantageEligible(Boolean vantageEligible) {
        isVantageEligible = vantageEligible;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public List<String> getCbsProductIds() {
        return cbsProductIds;
    }

    public void setCbsProductIds(List<String> cbsProductIds) {
        this.cbsProductIds = cbsProductIds;
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
