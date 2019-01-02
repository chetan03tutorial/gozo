package com.lbg.ib.api.sales.party.domain.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostalAddressDetails {
    private String       postalAddressType;
    private String       contactPointStatus;
    private Date         startDate;
    private Date         endDate;
    private String       addressLifeCycleStatus;
    
    private String       addressCareOfName;
    private List<String> addressLines;
    private String       postCode;
    private String       countryCode;
    
    private String       specialMailingCode;
    
    /**
     * @return the postalAddressType
     */
    public String getPostalAddressType() {
        return postalAddressType;
    }
    
    /**
     * @param postalAddressType
     *            the postalAddressType to set
     */
    public void setPostalAddressType(String postalAddressType) {
        this.postalAddressType = postalAddressType;
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
    
    /**
     * @return the addressLines
     */
    public List<String> getAddressLines() {
        return addressLines;
    }
    
    /**
     * @param addressLines
     *            the addressLines to set
     */
    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }
    
    /**
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }
    
    /**
     * @param postCode
     *            the postCode to set
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
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
     * @return the addressLifeCycleStatus
     */
    public String getAddressLifeCycleStatus() {
        return addressLifeCycleStatus;
    }
    
    /**
     * @param addressLifeCycleStatus
     *            the addressLifeCycleStatus to set
     */
    public void setAddressLifeCycleStatus(String addressLifeCycleStatus) {
        this.addressLifeCycleStatus = addressLifeCycleStatus;
    }
    
    /**
     * @return the addressCareOfName
     */
    public String getAddressCareOfName() {
        return addressCareOfName;
    }
    
    /**
     * @param addressCareOfName
     *            the addressCareOfName to set
     */
    public void setAddressCareOfName(String addressCareOfName) {
        this.addressCareOfName = addressCareOfName;
    }
    
    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }
    
    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    /**
     * @return the specialMailingCode
     */
    public String getSpecialMailingCode() {
        return specialMailingCode;
    }
    
    /**
     * @param specialMailingCode
     *            the specialMailingCode to set
     */
    public void setSpecialMailingCode(String specialMailingCode) {
        this.specialMailingCode = specialMailingCode;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
