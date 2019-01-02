package com.lbg.ib.api.sales.overdraft.domain;


public class E169Response extends OverdraftManagementResponse{

	private boolean hasOdIssued;

	public boolean isHasOdIssued() {
		return hasOdIssued;
	}

	public void setHasOdIssued(boolean hasOdIssued) {
		this.hasOdIssued = hasOdIssued;
	}

}
