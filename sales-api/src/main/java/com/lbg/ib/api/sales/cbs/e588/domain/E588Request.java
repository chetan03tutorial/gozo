package com.lbg.ib.api.sales.cbs.e588.domain;

import com.lbg.ib.api.sales.overdraft.domain.CbsRequest;

public class E588Request extends CbsRequest {

	private String accountNumber;
	private int tariff;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getTariff() {
		return tariff;
	}
	public void setTariff(int tariff) {
		this.tariff = tariff;
	}
}
