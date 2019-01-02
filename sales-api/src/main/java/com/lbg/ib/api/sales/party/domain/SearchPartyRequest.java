package com.lbg.ib.api.sales.party.domain;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

@Validate
public class SearchPartyRequest {
    
    @RequiredFieldValidation
    @StringFieldValidation(pattern = "[a-zA-Z0-9_]*")
    private String agreementIdentifier;
    private String partyRole;
    private String agreementType;
    
    public SearchPartyRequest(String agreementIdentifier) {
        this.agreementIdentifier = agreementIdentifier;
    }
    
    public String getAgreementIdentifier() {
        return agreementIdentifier;
    }
    
    public void setAgreementIdentifier(String agreementIdentifier) {
        this.agreementIdentifier = agreementIdentifier;
    }
    
    public String getPartyRole() {
        return partyRole;
    }
    
    public void setPartyRole(String partyRole) {
        this.partyRole = partyRole;
    }
    
    public String getAgreementType() {
        return agreementType;
    }
    
    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
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
        return "SearchPartyRequest [agreementIdentifier=" + agreementIdentifier + ", partyRole=" + partyRole
                + ", agreementType=" + agreementType + "]";
    }
}
