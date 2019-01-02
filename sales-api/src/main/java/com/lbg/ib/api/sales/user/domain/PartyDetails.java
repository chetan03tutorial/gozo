package com.lbg.ib.api.sales.user.domain;

import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.Date;

public class PartyDetails {

	private String email;
	private String firstName;
	private String[] lastName;
	private String[] middleName;
	private String title;
	private String[] addressLines;
	private String postalCode;
	private String surname;
	private boolean isJoint;
	private boolean isPrimaryParty;
	private String dob;
	private Date addressStartDate;
	private Date addressEndDate;
	private PostalAddressComponent currentAddress;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String[] middleName) {
		this.middleName = middleName;
	}

	public String[] getLastName() {
		return lastName;
	}

	public void setLastName(String[] lastName) {
		this.lastName = lastName;
	}

	public String[] getAddressLines() {
		return addressLines;
	}

	public void setAddressLines(String[] addressLines) {
		this.addressLines = addressLines;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isJoint() {
		return isJoint;
	}

	public void setJoint(boolean isJoint) {
		this.isJoint = isJoint;
	}

	public boolean isPrimaryParty() {
		return isPrimaryParty;
	}

	public void setIsPrimaryParty(boolean isPrimaryParty) {
		this.isPrimaryParty = isPrimaryParty;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
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

	public PostalAddressComponent getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(PostalAddressComponent currentAddress) {
		this.currentAddress = currentAddress;
	}

	@Override
	public String toString() {
		return reflectionToString(this);
	}

}
