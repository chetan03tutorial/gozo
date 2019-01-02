package com.lbg.ib.api.sales.party.domain;

import java.util.Arrays;

public class IBParties {
    
    private String      partyId;
    
    private String      partyType;
    
    private String      firstName;
    
    private String      secondName;
    
    private String      thirdName;
    
    private String      lastName;
    
    private String      title;
    
    private String      generationSuffix;
    
    private String      birthDate;
    
    private String      deathNotifiedDate;
    
    private String      gender;
    
    private String[]    initials;
    
    private String[]    foreNames;
    
    private Address     postalAddress;
    
    private String[]    agreementRole;
    
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
    
    public String getThirdName() {
        return thirdName;
    }
    
    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
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
    
    public String[] getInitials() {
        return initials;
    }
    
    public void setInitials(String[] initials) {
        this.initials = initials;
    }
    
    public String[] getForeNames() {
        return foreNames;
    }
    
    public void setForeNames(String[] foreNames) {
        this.foreNames = foreNames;
    }
    
    public Address getPostalAddress() {
        return postalAddress;
    }
    
    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String[] getAgreementRole() {
        return agreementRole;
    }

    public void setAgreementRole(String[] agreementRole) {
        this.agreementRole = agreementRole;
    }

    @Override
    public String toString() {
        return "IBParties [partyId=" + partyId + ", partyType=" + partyType + ", firstName=" + firstName
                + ", secondName=" + secondName + ", thirdName=" + thirdName + ", lastName=" + lastName + ", title="
                + title + ", generationSuffix=" + generationSuffix + ", birthDate=" + birthDate + ", deathNotifiedDate="
                + deathNotifiedDate + ", gender=" + gender + ", initials=" + Arrays.toString(initials) + ", foreNames="
                + Arrays.toString(foreNames) + ", postalAddress=" + postalAddress + ", agreementRole="
                + Arrays.toString(agreementRole) + "]";
    }
    
}
