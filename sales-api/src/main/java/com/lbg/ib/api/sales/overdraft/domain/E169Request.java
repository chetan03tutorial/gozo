package com.lbg.ib.api.sales.overdraft.domain;

public class E169Request extends CbsRequest{

	private String accountNumber;
	private String sortCode;
	private String debitLimitBalance;
	private String featureCreationDate;
	private String featureNextReviewDate;
	private String finalPaymentDueDate;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getDebitLimitBalance() {
		return debitLimitBalance;
	}

	public void setDebitLimitBalance(String debitLimitBalance) {
		this.debitLimitBalance = debitLimitBalance;
	}

	public String getFeatureCreationDate() {
		return featureCreationDate;
	}

	public void setFeatureCreationDate(String featureCreationDate) {
		this.featureCreationDate = featureCreationDate;
	}

	public String getFeatureNextReviewDate() {
		return featureNextReviewDate;
	}

	public void setFeatureNextReviewDate(String featureNextReviewDate) {
		this.featureNextReviewDate = featureNextReviewDate;
	}

	public String getFinalPaymentDueDate() {
		return finalPaymentDueDate;
	}

	public void setFinalPaymentDueDate(String finalPaymentDueDate) {
		this.finalPaymentDueDate = finalPaymentDueDate;
	}

}
