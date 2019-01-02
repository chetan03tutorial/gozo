package com.lbg.ib.api.sales.cbs.e588.domain;

public class E588Response {

	private String cbsCustomerNumber;
	private String status;

	public String getCbsCustomerNumber() {
		return cbsCustomerNumber;
	}

	public void setCbsCustomerNumber(String cbsCustomerNumber) {
		this.cbsCustomerNumber = cbsCustomerNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
