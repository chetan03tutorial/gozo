package com.lbg.ib.api.sales.party.domain.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartyDetail {
    private String       partyType;
    private String       partyId;
    private List<String> foreNames;

    // below three fields will be removed once we get the actual URL
    private String firstName;
    private String secondName;
    private String thirdName;

    private char[] initials;
    private String lastName;
    private String title;
    private String suffixTitle;

    private String salutation;
    private String generationSuffix;
    private String birthDate;
    private String deathDate;
    private String deathNotifiedDate;
    private String gender;
    private String maritalStatus;
    private String nationalInsuranceNumber;
    private String armedForcesSerialNumber;

    /**
     * @return the foreNames
     */
    public List<String> getForeNames() {
        return foreNames;
    }

    /**
     * @param foreNames
     *            the foreNames to set
     */
    public void setForeNames(List<String> foreNames) {
        this.foreNames = foreNames;
    }

    /**
     * @return the partyType
     */
    public String getPartyType() {
        return partyType;
    }

    /**
     * @param partyType
     *            the partyType to set
     */
    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    /**
     * @return the partyId
     */
    public String getPartyId() {
        return partyId;
    }

    /**
     * @param partyId
     *            the partyId to set
     */
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the secondName
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * @param secondName
     *            the secondName to set
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * @return the thirdName
     */
    public String getThirdName() {
        return thirdName;
    }

    /**
     * @param thirdName
     *            the thirdName to set
     */
    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    /**
     * @return the initials
     */
    public char[] getInitials() {
        return initials;
    }

    /**
     * @param initials
     *            the initials to set
     */
    public void setInitials(char[] initials) {
        this.initials = initials;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the suffixTitle
     */
    public String getSuffixTitle() {
        return suffixTitle;
    }

    /**
     * @param suffixTitle
     *            the suffixTitle to set
     */
    public void setSuffixTitle(String suffixTitle) {
        this.suffixTitle = suffixTitle;
    }

    /**
     * @return the salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * @param salutation
     *            the salutation to set
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    /**
     * @return the generationSuffix
     */
    public String getGenerationSuffix() {
        return generationSuffix;
    }

    /**
     * @param generationSuffix
     *            the generationSuffix to set
     */
    public void setGenerationSuffix(String generationSuffix) {
        this.generationSuffix = generationSuffix;
    }

    /**
     * @return the birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate
     *            the birthDate to set
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return the deathDate
     */
    public String getDeathDate() {
        return deathDate;
    }

    /**
     * @param deathDate
     *            the deathDate to set
     */
    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    /**
     * @return the deathNotifiedDate
     */
    public String getDeathNotifiedDate() {
        return deathNotifiedDate;
    }

    /**
     * @param deathNotifiedDate
     *            the deathNotifiedDate to set
     */
    public void setDeathNotifiedDate(String deathNotifiedDate) {
        this.deathNotifiedDate = deathNotifiedDate;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     *            the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the maritalStatus
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus
     *            the maritalStatus to set
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @return the nationalInsuranceNumber
     */
    public String getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    /**
     * @param nationalInsuranceNumber
     *            the nationalInsuranceNumber to set
     */
    public void setNationalInsuranceNumber(String nationalInsuranceNumber) {
        this.nationalInsuranceNumber = nationalInsuranceNumber;
    }

    /**
     * @return the armedForcesSerialNumber
     */
    public String getArmedForcesSerialNumber() {
        return armedForcesSerialNumber;
    }

    /**
     * @param armedForcesSerialNumber
     *            the armedForcesSerialNumber to set
     */
    public void setArmedForcesSerialNumber(String armedForcesSerialNumber) {
        this.armedForcesSerialNumber = armedForcesSerialNumber;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
