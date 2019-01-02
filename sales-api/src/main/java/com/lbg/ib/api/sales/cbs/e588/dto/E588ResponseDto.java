package com.lbg.ib.api.sales.cbs.e588.dto;

public class E588ResponseDto {

	private int additionalDataIndicator;
	private String nationalSortCode;
	private String cbsCustomerNumber;
	private String creditInterestMethodCode;
	private String previousWithdrawalTransactionCode;
	private String bonusMarketCode;
	private String bonusStartDate;
	private String bonusExpiryDate;
	private String bonusCreditInterestMarginAmount;
	private String bonusOverallCreditInterestRate;
	private String productPreferentialRate;
	private String productPreferentialRateStartDate;
	private String productPreferentialRateEndDate;
	private String productPreferentialRateMar;

	public int getAdditionalDataIndicator() {
		return additionalDataIndicator;
	}

	public void setAdditionalDataIndicator(int additionalDataIndicator) {
		this.additionalDataIndicator = additionalDataIndicator;
	}

	public String getNationalSortCode() {
		return nationalSortCode;
	}

	public void setNationalSortCode(String nationalSortCode) {
		this.nationalSortCode = nationalSortCode;
	}

	public String getCbsCustomerNumber() {
		return cbsCustomerNumber;
	}

	public void setCbsCustomerNumber(String cbsCustomerNumber) {
		this.cbsCustomerNumber = cbsCustomerNumber;
	}

	public String getCreditInterestMethodCode() {
		return creditInterestMethodCode;
	}

	public void setCreditInterestMethodCode(String creditInterestMethodCode) {
		this.creditInterestMethodCode = creditInterestMethodCode;
	}

	public String getPreviousWithdrawalTransactionCode() {
		return previousWithdrawalTransactionCode;
	}

	public void setPreviousWithdrawalTransactionCode(String previousWithdrawalTransactionCode) {
		this.previousWithdrawalTransactionCode = previousWithdrawalTransactionCode;
	}

	public String getBonusMarketCode() {
		return bonusMarketCode;
	}

	public void setBonusMarketCode(String bonusMarketCode) {
		this.bonusMarketCode = bonusMarketCode;
	}

	public String getBonusStartDate() {
		return bonusStartDate;
	}

	public void setBonusStartDate(String bonusStartDate) {
		this.bonusStartDate = bonusStartDate;
	}

	public String getBonusExpiryDate() {
		return bonusExpiryDate;
	}

	public void setBonusExpiryDate(String bonusExpiryDate) {
		this.bonusExpiryDate = bonusExpiryDate;
	}

	public String getBonusCreditInterestMarginAmount() {
		return bonusCreditInterestMarginAmount;
	}

	public void setBonusCreditInterestMarginAmount(String bonusCreditInterestMarginAmount) {
		this.bonusCreditInterestMarginAmount = bonusCreditInterestMarginAmount;
	}

	public String getBonusOverallCreditInterestRate() {
		return bonusOverallCreditInterestRate;
	}

	public void setBonusOverallCreditInterestRate(String bonusOverallCreditInterestRate) {
		this.bonusOverallCreditInterestRate = bonusOverallCreditInterestRate;
	}

	public String getProductPreferentialRate() {
		return productPreferentialRate;
	}

	public void setProductPreferentialRate(String productPreferentialRate) {
		this.productPreferentialRate = productPreferentialRate;
	}

	public String getProductPreferentialRateStartDate() {
		return productPreferentialRateStartDate;
	}

	public void setProductPreferentialRateStartDate(String productPreferentialRateStartDate) {
		this.productPreferentialRateStartDate = productPreferentialRateStartDate;
	}

	public String getProductPreferentialRateEndDate() {
		return productPreferentialRateEndDate;
	}

	public void setProductPreferentialRateEndDate(String productPreferentialRateEndDate) {
		this.productPreferentialRateEndDate = productPreferentialRateEndDate;
	}

	public String getProductPreferentialRateMar() {
		return productPreferentialRateMar;
	}

	public void setProductPreferentialRateMar(String productPreferentialRateMar) {
		this.productPreferentialRateMar = productPreferentialRateMar;
	}

}
