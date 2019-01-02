package com.lbg.ib.api.sales.party.domain.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactNumber {
	private String contactPointStatus;
	private Date startDate;

	private String fullPhoneNumber;
	private Integer phoneSequenceNumber;
	private String phoneType;
	private String deviceType;
	private String extensionNumber;


	/**
	 * @return the fullPhoneNumber
	 */
	public String getFullPhoneNumber() {
		return fullPhoneNumber;
	}

	/**
	 * @param fullPhoneNumber the fullPhoneNumber to set
	 */
	public void setFullPhoneNumber(String fullPhoneNumber) {
		this.fullPhoneNumber = fullPhoneNumber;
	}

	/**
	 * @return the contactPointStatus
	 */
	public String getContactPointStatus() {
		return contactPointStatus;
	}

	/**
	 * @param contactPointStatus
	 *            the contactPointStatus to set
	 */
	public void setContactPointStatus(String contactPointStatus) {
		this.contactPointStatus = contactPointStatus;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the phoneSequenceNumber
	 */
	public Integer getPhoneSequenceNumber() {
		return phoneSequenceNumber;
	}

	/**
	 * @param phoneSequenceNumber
	 *            the phoneSequenceNumber to set
	 */
	public void setPhoneSequenceNumber(Integer phoneSequenceNumber) {
		this.phoneSequenceNumber = phoneSequenceNumber;
	}

	/**
	 * @return the phoneType
	 */
	public String getPhoneType() {
		return phoneType;
	}

	/**
	 * @param phoneType
	 *            the phoneType to set
	 */
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType
	 *            the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the extensionNumber
	 */
	public String getExtensionNumber() {
		return extensionNumber;
	}

	/**
	 * @param extensionNumber
	 *            the extensionNumber to set
	 */
	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
