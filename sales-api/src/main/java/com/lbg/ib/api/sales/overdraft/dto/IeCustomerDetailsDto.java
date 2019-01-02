package com.lbg.ib.api.sales.overdraft.dto;

public class IeCustomerDetailsDto {

	private String monthlyExpenses;
	private String monthlyIncome;
	private String incomeFrequency;
	private String accommodationExpenses;

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

	public String getAccommodationExpenses() {
		return accommodationExpenses;
	}

	public void setAccommodationExpenses(String accommodationExpenses) {
		this.accommodationExpenses = accommodationExpenses;
	}
}
