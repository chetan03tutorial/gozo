package com.lbg.ib.api.sales.party.domain;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Address {
    
    private String              postCode;
    
    private String              countryCode;
    
    private String              postalAddressType;
    
    private List<String>        addressLines;
    
    private String              type;
    
    private String              contactPointStatus;
    
    private String              addressLifeCycleStatus;
    
    private Map<String, Object> other = new HashMap<String, Object>();
    
    @JsonAnyGetter
    public Map<String, Object> any() {
        return other;
    }
    
    @JsonAnySetter
    public void set(String name, Object value) {
        other.put(name, value);
    }
    
    public boolean hasUnknowProperties() {
        return !other.isEmpty();
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getContactPointStatus() {
        return contactPointStatus;
    }
    
    public void setContactPointStatus(String contactPointStatus) {
        this.contactPointStatus = contactPointStatus;
    }
    
    public String getAddressLifeCycleStatus() {
        return addressLifeCycleStatus;
    }
    
    public void setAddressLifeCycleStatus(String addressLifeCycleStatus) {
        this.addressLifeCycleStatus = addressLifeCycleStatus;
    }
    
    @Override
    public String toString() {
        return "Address [postCode=" + postCode + ", countryCode=" + countryCode + ", postalAddressType="
                + postalAddressType + ", addressLines=" + addressLines + "]";
    }
    
}
