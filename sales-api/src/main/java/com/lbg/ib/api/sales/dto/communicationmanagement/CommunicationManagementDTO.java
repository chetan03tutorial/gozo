
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dto.communicationmanagement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.HashMap;

public class CommunicationManagementDTO {

    private String                  templateID;

    private String                  communicationType;

    private String                  contactPointID;

    private String                  brand;

    private HashMap<String, String> tokenisedMap;

    public String getTemplateID() {
        return templateID;
    }

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

    public HashMap<String, String> getTokenisedMap() {
        return tokenisedMap;
    }

    public void setTokenisedMap(HashMap<String, String> tokenisedMap) {
        this.tokenisedMap = tokenisedMap;
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

}
