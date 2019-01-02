package com.lbg.ib.api.sales.party.domain.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElectronicAddress {
    private String      electronicAddressType;
    private String                         address;
    private String electronicPartyAddressType;

    private String contactPointStatus;
    private Date                   endDate;

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the electronicAddressType
     */
    public String getElectronicAddressType() {
        return electronicAddressType;
    }

    /**
     * @param electronicAddressType
     *            the electronicAddressType to set
     */
    public void setElectronicAddressType(String electronicAddressType) {
        this.electronicAddressType = electronicAddressType;
    }

    /**
     * @return the electronicPartyAddressType
     */
    public String getElectronicPartyAddressType() {
        return electronicPartyAddressType;
    }

    /**
     * @param electronicPartyAddressType
     *            the electronicPartyAddressType to set
     */
    public void setElectronicPartyAddressType(String electronicPartyAddressType) {
        this.electronicPartyAddressType = electronicPartyAddressType;
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
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
