package com.lbg.ib.api.sales.party.domain.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrievePartyDetailsResponse {
    private PartyDetail party;

    private List<PostalAddressDetails> postalAddress;

    private List<ContactNumber> telephoneNumber;

    private List<ElectronicAddress> electronicAddress;

    /**
     * @return the party
     */
    public PartyDetail getParty() {
        return party;
    }

    /**
     * @return the postalAddress
     */
    public List<PostalAddressDetails> getPostalAddress() {
        return postalAddress;
    }

    /**
     * @param postalAddress
     *            the postalAddress to set
     */
    public void setPostalAddress(List<PostalAddressDetails> postalAddress) {
        this.postalAddress = postalAddress;
    }

    /**
     * @return the telephoneNumber
     */
    public List<ContactNumber> getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * @param telephoneNumber
     *            the telephoneNumber to set
     */
    public void setTelephoneNumber(List<ContactNumber> telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * @return the electronicAddress
     */
    public List<ElectronicAddress> getElectronicAddress() {
        return electronicAddress;
    }

    /**
     * @param electronicAddress
     *            the electronicAddress to set
     */
    public void setElectronicAddress(List<ElectronicAddress> electronicAddress) {
        this.electronicAddress = electronicAddress;
    }

    /**
     * @param party
     *            the party to set
     */
    public void setParty(PartyDetail party) {
        this.party = party;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
    
}
