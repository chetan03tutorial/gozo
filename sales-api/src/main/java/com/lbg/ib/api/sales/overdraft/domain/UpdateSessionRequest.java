package com.lbg.ib.api.sales.overdraft.domain;

import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;

@Validate
public class UpdateSessionRequest {

    @StringFieldValidation(pattern = REQUIRED_NUMERIC)
    private Double demandedOd;

    public Double getDemandedOd() {
        return demandedOd;
    }

    public void setDemandedOd(Double demandedOd) {
        this.demandedOd = demandedOd;
    }
}
