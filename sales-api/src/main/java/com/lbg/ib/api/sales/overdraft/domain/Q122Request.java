package com.lbg.ib.api.sales.overdraft.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;

import java.util.List;

import com.lbg.ib.api.sales.user.domain.PartyInformation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class Q122Request {

	@StringFieldValidation(pattern = REQUIRED_NUMERIC)
	private String demandedOd;
	private String monthlyExpenses;
	private String monthlyIncome;
	private String incomeFrequency;
	private String overdraftLimitType;
	private String overdraftPurpose;
	private String employmentStatusCode;
	private boolean ieIndicator;
	private String accommodationExpenses;
	private String durationOfStay;
	private List<PartyInformation> parties;

	public String getDemandedOd() {
		return demandedOd;
	}

	public void setDemandedOd(String demandedOd) {
		this.demandedOd = demandedOd;
	}

	public String getMonthlyExpenses() {
		return monthlyExpenses;
	}

	public void setMonthlyExpenses(String monthlyExpenses) {
		this.monthlyExpenses = monthlyExpenses;
	}

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getIncomeFrequency() {
		return incomeFrequency;
	}

	public void setIncomeFrequency(String incomeFrequency) {
		this.incomeFrequency = incomeFrequency;
	}

	public String getOverdraftLimitType() {
		return overdraftLimitType;
	}

	public void setOverdraftLimitType(String overdraftLimitType) {
		this.overdraftLimitType = overdraftLimitType;
	}

	public String getOverdraftPurpose() {
		return overdraftPurpose;
	}

	public void setOverdraftPurpose(String overdraftPurpose) {
		this.overdraftPurpose = overdraftPurpose;
	}

	public String getEmploymentStatusCode() {
		return employmentStatusCode;
	}

	public void setEmploymentStatusCode(String employmentStatusCode) {
		this.employmentStatusCode = employmentStatusCode;
	}

	public boolean isIeIndicator() {
		return ieIndicator;
	}

	public void setIeIndicator(boolean ieIndicator) {
		this.ieIndicator = ieIndicator;
	}

	public String getAccommodationExpenses() {
		return accommodationExpenses;
	}

	public void setAccommodationExpenses(String accommodationExpenses) {
		this.accommodationExpenses = accommodationExpenses;
	}

	public String getDurationOfStay() {
		return durationOfStay;
	}

	public void setDurationOfStay(String durationOfStay) {
		this.durationOfStay = durationOfStay;
	}

	public List<PartyInformation> getParties() {
		return parties;
	}

	public void setParties(List<PartyInformation> parties) {
		this.parties = parties;
	}

}
