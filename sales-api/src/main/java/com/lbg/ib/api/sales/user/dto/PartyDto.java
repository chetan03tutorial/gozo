package com.lbg.ib.api.sales.user.dto;

import com.lbg.ib.api.sales.overdraft.dto.IeCustomerDetailsDto;

public class PartyDto {

	private String partyId;
	private String ocisId;
	private String gender;
	private String birthDate;
	private String deathDate;
	private String deathNotifiedDate;
	private String maritalStatus;
	private String nationalInsuranceNumber;
	private String armedForcesSerialNumber;
	private String emailAddress;
	private NameDto name;
	private AddressDto address;
	private String employmentStatus;
	private IeCustomerDetailsDto ieDetails;
	private boolean ieIndicator;
	private String cbsCustomerNumber;

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getOcisId() {
		return ocisId;
	}

	public void setOcisId(String ocisId) {
		this.ocisId = ocisId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}

	public String getDeathNotifiedDate() {
		return deathNotifiedDate;
	}

	public void setDeathNotifiedDate(String deathNotifiedDate) {
		this.deathNotifiedDate = deathNotifiedDate;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getNationalInsuranceNumber() {
		return nationalInsuranceNumber;
	}

	public void setNationalInsuranceNumber(String nationalInsuranceNumber) {
		this.nationalInsuranceNumber = nationalInsuranceNumber;
	}

	public String getArmedForcesSerialNumber() {
		return armedForcesSerialNumber;
	}

	public void setArmedForcesSerialNumber(String armedForcesSerialNumber) {
		this.armedForcesSerialNumber = armedForcesSerialNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public NameDto getName() {
		return name;
	}

	public void setName(NameDto name) {
		this.name = name;
	}

	public AddressDto getAddress() {
		return address;
	}

	public void setAddress(AddressDto address) {
		this.address = address;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public IeCustomerDetailsDto getIeDetails() {
		return ieDetails;
	}

	public void setIeDetails(IeCustomerDetailsDto ieDetails) {
		this.ieDetails = ieDetails;
	}

	public boolean isIeIndicator() {
		return ieIndicator;
	}

	public void setIeIndicator(boolean ieIndicator) {
		this.ieIndicator = ieIndicator;
	}

	public String getCbsCustomerNumber() {
		return cbsCustomerNumber;
	}

	public void setCbsCustomerNumber(String cbsCustomerNumber) {
		this.cbsCustomerNumber = cbsCustomerNumber;
	}

	
}
