package com.lbg.ib.api.sales.party.domain;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Party {
    
    private String              partyId;
    
    private String              partyType;
    
    private String              firstName;
    
    private String              thirdName;
    
    private String              secondName;
    
    private String              lastName;
    
    private String              title;
    
    private String              generationSuffix;
    
    private String              birthDate;
    
    private String              deathNotifiedDate;
    
    private String              gender;
    
    private String[]            initials;
    
    private String[]            foreNames;
    
    private String              organisationName;
    
    private String              tradingName;
    
    private String              kycStatus;
    
    private String[]            organisationType;
    
    private String              deathDate;
    
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
    
    public String getOrganisationName() {
        return organisationName;
    }
    
    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }
    
    public String getTradingName() {
        return tradingName;
    }
    
    public String getDeathDate() {
        return deathDate;
    }
    
    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }
    
    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String[] getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(String[] organisationType) {
        this.organisationType = organisationType;
    }

    public String[] getInitials() {
        return initials;
    }
    
    public String getThirdName() {
        return thirdName;
    }
    
    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }
    
    public void setInitials(String[] initials) {
        this.initials = initials;
    }
    
    public String getPartyId() {
        return partyId;
    }
    
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
    
    public String getPartyType() {
        return partyType;
    }
    
    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getSecondName() {
        return secondName;
    }
    
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getGenerationSuffix() {
        return generationSuffix;
    }
    
    public void setGenerationSuffix(String generationSuffix) {
        this.generationSuffix = generationSuffix;
    }
    
    public String getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getDeathNotifiedDate() {
        return deathNotifiedDate;
    }
    
    public void setDeathNotifiedDate(String deathNotifiedDate) {
        this.deathNotifiedDate = deathNotifiedDate;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String[] getForeNames() {
        return foreNames;
    }
    
    public void setForeNames(String[] foreNames) {
        this.foreNames = foreNames;
    }
    
    @Override
    public String toString() {
        return "Party [partyId=" + partyId + ", partyType=" + partyType + ", firstName=" + firstName + ", thirdName="
                + thirdName + ", secondName=" + secondName + ", lastName=" + lastName + ", title=" + title
                + ", generationSuffix=" + generationSuffix + ", birthDate=" + birthDate + ", deathNotifiedDate="
                + deathNotifiedDate + ", gender=" + gender + ", initials=" + Arrays.toString(initials)
                + ", organisationName=" + organisationName + ", tradingName=" + tradingName + ", kycStatus=" + kycStatus
                + ", organisationType=" + Arrays.toString(organisationType) + "]";
    }
    
}
