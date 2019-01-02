package com.lbg.ib.api.sales.user.domain;

import com.lbg.ib.api.sales.overdraft.domain.IeCustomerDetails;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class PartyInformation {

	private String employmentStatus;
	private IeCustomerDetails ieDetails;
	private boolean ieIndicator;
	private String durationOfStay;
	private boolean isPrimary;
	
	public PartyInformation() {
		
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public boolean isIeIndicator() {
		return ieIndicator;
	}

	public void setIeIndicator(boolean ieIndicator) {
		this.ieIndicator = ieIndicator;
	}

	public String getDurationOfStay() {
		return durationOfStay;
	}

	public void setDurationOfStay(String durationOfStay) {
		this.durationOfStay = durationOfStay;
	}

	public IeCustomerDetails getIeDetails() {
		return ieDetails;
	}

	public void setIeDetails(IeCustomerDetails ieDetails) {
		this.ieDetails = ieDetails;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	

}
