package com.lbg.ib.api.sales.overdraft.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;

import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class OdFulfillmentRequest {

	@StringFieldValidation(pattern = REQUIRED_NUMERIC)
	private String demandedOd;

	public String getDemandedOd() {
		return demandedOd;
	}

	public void setDemandedOd(String demandedOd) {
		this.demandedOd = demandedOd;
	}
}
