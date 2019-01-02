package com.lbg.ib.api.sales.dto.b274;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by 8796528 on 27/07/2018.
 */
public class B274RequestDTO {
    private String cbsProductNumberTrimmed;
    private BigDecimal overdraftAmount;
    private String mnemonic;
    private BigInteger currentYearOfStudy;
    private Map<String, String> productOptions;

    public String getCbsProductNumberTrimmed() {
        return cbsProductNumberTrimmed;
    }

    public void setCbsProductNumberTrimmed(String cbsProductNumberTrimmed) {
        this.cbsProductNumberTrimmed = cbsProductNumberTrimmed;
    }

    public BigDecimal getOverdraftAmount() {
        return overdraftAmount;
    }

    public void setOverdraftAmount(BigDecimal overdraftAmount) {
        this.overdraftAmount = overdraftAmount;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public BigInteger getCurrentYearOfStudy() {
        return currentYearOfStudy;
    }

    public void setCurrentYearOfStudy(BigInteger currentYearOfStudy) {
        this.currentYearOfStudy = currentYearOfStudy;
    }

    public Map<String, String> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(Map<String, String> productOptions) {
        this.productOptions = productOptions;
    }
}
