package com.lbg.ib.api.sales.overdraft.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;

import com.lbg.ib.api.shared.validation.StringFieldValidation;

public class IncomeExpenditureDetails {

	private String employmentStatusCode;
	private boolean ieIndicator;
	private String accommodationExpenses;
	@StringFieldValidation(pattern = REQUIRED_NUMERIC)
	private String durationOfStay;
	
}
