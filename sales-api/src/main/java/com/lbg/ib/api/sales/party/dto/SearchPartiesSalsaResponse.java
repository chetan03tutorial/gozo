package com.lbg.ib.api.sales.party.dto;

import com.lbg.ib.api.sales.party.domain.Address;
import com.lbg.ib.api.sales.party.domain.Party;
import com.lbg.ib.api.sales.party.domain.RegistrationDetails;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class SearchPartiesSalsaResponse {
    
    private Party                 party;
    
    private Address[]             postalAddress;
    
    private String[]              agreementRole;
    
    private RegistrationDetails[] partyRegistration;
    
    private Map<String, Object>   other = new HashMap<String, Object>();
    
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
    
    public RegistrationDetails[] getPartyRegistration() {
        return partyRegistration;
    }
    
    public void setPartyRegistration(RegistrationDetails[] partyRegistration) {
        this.partyRegistration = partyRegistration;
    }
    
    public Party getParty() {
        return party;
    }
    
    public void setParty(Party party) {
        this.party = party;
    }
    
    public Address[] getPostalAddress() {
        return postalAddress;
    }
    
    public void setPostalAddress(Address[] postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String[] getAgreementRole() {
        return agreementRole;
    }

    public void setAgreementRole(String[] agreementRole) {
        this.agreementRole = agreementRole;
    }
}
