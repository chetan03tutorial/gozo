package com.lbg.ib.api.sales.product.domain.eligibility;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class PcciOverdraftRequest {

    @RequiredFieldValidation
    private String overDraftAmount;

    public PcciOverdraftRequest() {
    }

    public PcciOverdraftRequest(String overDraftAmount) {
        this.overDraftAmount = overDraftAmount;
    }

    public String getOverDraftAmount() {
        return overDraftAmount;
    }

    public void setOverDraftAmount(String overDraftAmount) {
        this.overDraftAmount = overDraftAmount;
    }

}
