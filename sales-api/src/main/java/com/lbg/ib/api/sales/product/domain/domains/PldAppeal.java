package com.lbg.ib.api.sales.product.domain.domains;

/**
 * Created by 8796528 on 09/07/2018.
 */
public class PldAppeal {

    private boolean isAmtOverdraftUpdated;
    private boolean isProductUpdated;
    private boolean appealSuccess;
    private boolean isReferUpdated;

    public boolean getIsAmtOverdraftUpdated() {
        return isAmtOverdraftUpdated;
    }

    public void setIsAmtOverdraftUpdated(boolean amtOverdraftUpdated) {
        isAmtOverdraftUpdated = amtOverdraftUpdated;
    }

    public boolean getIsProductUpdated() {
        return this.isProductUpdated;
    }

    public void setIsProductUpdated(boolean productUpdated) {
        this.isProductUpdated = productUpdated;
    }

    public boolean isAppealSuccess() {
        return appealSuccess;
    }

    public void setAppealSuccess(boolean appealSuccess) {
        this.appealSuccess = appealSuccess;
    }

    public boolean getIsReferUpdated() {
        return isReferUpdated;
    }

    public void setIsReferUpdated(boolean referUpdated) {
        isReferUpdated = referUpdated;
    }
}
