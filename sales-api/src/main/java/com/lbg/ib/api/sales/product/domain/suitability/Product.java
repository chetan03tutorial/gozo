package com.lbg.ib.api.sales.product.domain.suitability;

public class Product {

    private Boolean eligibility;
    private String  mnemonic;
    private Boolean withdrawalsAllowed;

    public Product(boolean eligibility, String mnemonic, boolean withdrawalsAllowed) {
        this.eligibility = eligibility;
        this.mnemonic = mnemonic;
        this.withdrawalsAllowed = withdrawalsAllowed;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public Boolean getWithdrawalsAllowed() {
        return withdrawalsAllowed;
    }

    @Override
    public String toString() {
        return "Product [eligibility=" + eligibility + ", mnemonic=" + mnemonic + ", withdrawalsAllowed="
                + withdrawalsAllowed + "]";
    }

}
