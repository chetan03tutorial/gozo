package com.lbg.ib.api.sales.user.dto;

import java.util.Date;
import java.util.List;

public class AddressDto {

	private String postCode;
	private String countryCode;
	private String postalAddressType;
	private List<String> addressLines;
	private Date addressStartDate;
	private Date addressEndDate;
	private String durationOfStay;
	
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPostalAddressType() {
		return postalAddressType;
	}
	public void setPostalAddressType(String postalAddressType) {
		this.postalAddressType = postalAddressType;
	}
	public List<String> getAddressLines() {
		return addressLines;
	}
	public void setAddressLines(List<String> addressLines) {
		this.addressLines = addressLines;
	}
	public Date getAddressStartDate() {
		return addressStartDate;
	}
	public void setAddressStartDate(Date addressStartDate) {
		this.addressStartDate = addressStartDate;
	}
	public Date getAddressEndDate() {
		return addressEndDate;
	}
	public void setAddressEndDate(Date addressEndDate) {
		this.addressEndDate = addressEndDate;
	}
	public String getDurationOfStay() {
		return durationOfStay;
	}
	public void setDurationOfStay(String durationOfStay) {
		this.durationOfStay = durationOfStay;
	}
	
	
	
}
