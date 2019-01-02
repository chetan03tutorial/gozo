package com.lbg.ib.api.sales.communication.dto;

import java.util.Map;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Created by rabaja on 16/11/2016.
 */
public class CommunicationRequestDTO {

    private String templateID;
    private String communicationType;
    private String contactPointID;
    private String brand;
    private Map<String, String> tokenisedMap;

    public String getTemplateID() { return templateID; }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public String getContactPointID() {
        return contactPointID;
    }

    public void setContactPointID(String contactPointID) {
        this.contactPointID = contactPointID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Map<String, String> getTokenisedMap() {
        return tokenisedMap;
    }

    public void setTokenisedMap(Map<String, String> tokenisedMap) {
        this.tokenisedMap = tokenisedMap;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
