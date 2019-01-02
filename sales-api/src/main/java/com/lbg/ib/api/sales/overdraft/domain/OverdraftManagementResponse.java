package com.lbg.ib.api.sales.overdraft.domain;

public class OverdraftManagementResponse {

	private String overdraftLimit;
	private String serviceMessage;

	public String getOverdraftLimit() {
		return overdraftLimit;
	}

	public void setOverdraftLimit(String overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
	}

	public String getServiceMessage() {
		return serviceMessage;
	}

	public void setServiceMessage(String serviceMessage) {
		this.serviceMessage = serviceMessage;
	}
	
	
	
}
