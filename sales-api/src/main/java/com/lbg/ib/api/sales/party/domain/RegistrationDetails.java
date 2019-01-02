package com.lbg.ib.api.sales.party.domain;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class RegistrationDetails {
    
    private String              registrationType;
    
    private String              registrationIdentifier;
    
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

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getRegistrationIdentifier() {
        return registrationIdentifier;
    }
    
    public void setRegistrationIdentifier(String registrationIdentifier) {
        this.registrationIdentifier = registrationIdentifier;
    }
    
    @Override
    public String toString() {
        return "RegistrationDetails [registrationType=" + registrationType + ", registrationIdentifier="
                + registrationIdentifier + "]";
    }
    
}
