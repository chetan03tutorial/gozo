package com.lbg.ib.api.sales.switching.domain;

import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.shared.validation.*;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import org.springframework.format.annotation.DateTimeFormat;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_ALPHA;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_ALPHA_SPACE;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;

@Validate
public class SwitchingParty {
    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_SIXTEEN)
    private String                 namePrefix;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_TWENTY_FOUR)
    private String                 firstName;

    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_THIRTY)
    private String                 middleName;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_TWENTY_FOUR)
    private String                 lastName;

    private String                 dob;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_ALPHA, maxLength = ValidationConstants.NUMBER_FORTY, minLength = ValidationConstants.NUMBER_THREE)
    private String                 nationality;

    private Boolean                hasDebitCard;

    @IntegerFieldValidation(max = ValidationConstants.NUMBER_SIXTEEN)
    private String                 cardNumber;

    @DateTimeFormat(pattern = "mm/yy")
    private String                 cardExpiryDate;

    private PostalAddressComponent postalAddress;

    @RequiredFieldValidation
    private Boolean                textAlert;

    private String                 partyId;

    @RequiredFieldValidation
    private Boolean                primary;

    private Boolean oldAccountHolder;

    public SwitchingParty() {
    }

    public SwitchingParty(String namePrefix, String firstName, String middleName, String lastName, String dob,
            String nationality, Boolean hasDebitCard, String cardNumber, String cardExpiryDate,
            PostalAddressComponent postalAddress, Boolean textAlert, String partyId,
            Boolean primary, boolean oldAccountHolder) {
        this.namePrefix = namePrefix;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dob = dob;
        this.nationality = nationality;
        this.hasDebitCard = hasDebitCard;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.postalAddress = postalAddress;
        this.textAlert = textAlert;
        this.partyId = partyId;
        this.primary = primary;
        this.oldAccountHolder = oldAccountHolder;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Boolean getHasDebitCard() {
        return hasDebitCard;
    }

    public void setHasDebitCard(Boolean hasDebitCard) {
        this.hasDebitCard = hasDebitCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public PostalAddressComponent getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddressComponent postalAddress) {
        this.postalAddress = postalAddress;
    }

    public Boolean getTextAlert() {
        return textAlert;
    }

    public void setTextAlert(Boolean textAlert) {
        this.textAlert = textAlert;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    //This is as CWA can be sending this as null
    public Boolean getOldAccountHolder() {
        if(oldAccountHolder == null){
            return false;
        }
        return oldAccountHolder;
    }

    public void setOldAccountHolder(Boolean oldAccountHolder) {
        this.oldAccountHolder = oldAccountHolder;
    }

    @Override public String toString() {
        return "SwitchingParty{" + "namePrefix='" + namePrefix + '\'' + ", firstName='" + firstName + '\''
                + ", middleName='" + middleName + '\'' + ", lastName='" + lastName + '\'' + ", dob=***"
                + ", nationality=***, hasDebitCard=" + hasDebitCard + ", cardNumber=***"
                + ", cardExpiryDate=***, postalAddress=" + postalAddress
                + ", textAlert=" + textAlert + ", partyId='" + partyId + '\'' + ", primary=" + primary
                + ", oldAccountHolder=" + oldAccountHolder + '}';
    }
}
