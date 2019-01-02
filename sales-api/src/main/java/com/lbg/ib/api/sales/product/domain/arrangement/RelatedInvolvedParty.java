/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.domain.arrangement;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.BIRTHCITY;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.EMAIL;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_ALPHA_SPACE;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.math.BigInteger;
import java.util.Date;

import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.validation.AccountTypeValidation;
import com.lbg.ib.api.shared.validation.IntegerFieldValidation;
import com.lbg.ib.api.shared.validation.OptionalFieldValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.sales.common.validation.SectionType;
import com.lbg.ib.api.sales.common.validation.SectionTypeValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;
import com.lbg.ib.api.shared.validation.ApplicantType;
import com.lbg.ib.api.shared.domain.TinDetails;

@Validate
@SectionTypeValidation(sectionType = SectionType.PERSONAL_DETAILS)
public class RelatedInvolvedParty {

    private static final String    AUTH   = "auth";

    private static final String    BRANCH = "branch";

    @AccountTypeValidation(value = AccountType.CA)
    private SwitchOptions intendSwitch;

    @AccountTypeValidation(value = AccountType.CA)
    private Boolean                intendOverDraft;

    private Integer                intendOdAmount;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_SIXTEEN)
    private String                 title;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_TWENTY_FOUR)
    private String                 firstName;

    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_THIRTY)
    private String                 middleName;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = ValidationConstants.NUMBER_TWENTY_FOUR)
    private String                 lastName;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date                   dob;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = "000|001|002")
    private String                 gender;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @OptionalFieldValidation(values = { AUTH })
    private PostalAddressComponent currentAddress;

    private PostalAddressComponent previousAddress;

    // @RequiredFieldValidation //Commenting out the required Validation as ""
    // is also accepted.
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = "|^00[0-5]$")
    private String                 maritalStatus;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = "^00[0-6]$")
    private String                 residentialStatus;

    @IntegerFieldValidation(max = ValidationConstants.NUMBER_NINETY_NINE)
    private Integer                numberOfDependents;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = "^[1-3]$")
    private String                 fundSource;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = "^(BENPA|BIEXP|CRSWB|INHER|PRETR|SAV|STUD|SPORI|SPMNY)")
    private String                 purpose;

    private ContactNumber          mobileNumber;

    private ContactNumber          workPhone;

    private ContactNumber homePhone;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = EMAIL, maxLength = ValidationConstants.NUMBER_EIGHTY)
    private String                 email;

    private Boolean                marketPrefPhone;

    private Boolean                marketPrefEmail;

    private Boolean                marketPrefText;

    private Boolean                marketPrefPost;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date                   ukArrivalDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date                   visaExpiryDate;

    private Boolean                ilrStatus;

    private Integer                income;

    @RequiredFieldValidation
    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = "^0[012][0-9]$")
    private String                 employmentStatus;

    @StringFieldValidation(pattern = "^(0(?:[0-2][1-9]|3[0-3]|[1-2]0))+$")
    private String                 occupnType;

    private EmployerDetails employer;

    private Integer                rentMortgCost;

    private Integer                maintnCost;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = "^(0|500|12500|37500|55000)")
    private Integer                savingsAmount;

    private Integer                prevBankYear;

    @IntegerFieldValidation(max = ValidationConstants.NUMBER_ELEVEN)
    private Integer                prevBankMonth;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    @StringFieldValidation(pattern = BIRTHCITY, minLength = ValidationConstants.NUMBER_TWO, maxLength = ValidationConstants.NUMBER_THIRTY_ONE)
    private String                 birthCity;

    private Double                 exptdMntlyDepAmt;

    private BigInteger             currentYearOfStudy;

    private String                 anticipateDateOfGraduation;

    private String                 ucasCode;

    private ApplicantType          applicantType;

    private String                 partyId;
    private String                 overDraftLimit;

    private String                 ocisId;
    
    private String 				   cbsCustomerNumber;

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public String getUcasCode() {
        return ucasCode;
    }

    public void setUcasCode(String ucasCode) {
        this.ucasCode = ucasCode;
    }

    public BigInteger getCurrentYearOfStudy() {
        return currentYearOfStudy;
    }

    public void setCurrentYearOfStudy(BigInteger currentYearOfStudy) {
        this.currentYearOfStudy = currentYearOfStudy;
    }

    public String getAnticipateDateOfGraduation() {
        return anticipateDateOfGraduation;
    }

    public void setAnticipateDateOfGraduation(String anticipateDateOfGraduation) {
        this.anticipateDateOfGraduation = anticipateDateOfGraduation;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    private TinDetails tinDetails;

    public TinDetails getTinDetails() {
        return tinDetails;
    }

    public void setTinDetails(TinDetails tinDetails) {
        this.tinDetails = tinDetails;
    }

    public RelatedInvolvedParty() {/* jackson */
    }

    /**
     * @param intendSwitch
     *            the intendSwitch to set
     */
    public void setIntendSwitch(SwitchOptions intendSwitch) {
        this.intendSwitch = intendSwitch;
    }

    /**
     * @param intendOverDraft
     *            the intendOverDraft to set
     */
    public void setIntendOverDraft(Boolean intendOverDraft) {
        this.intendOverDraft = intendOverDraft;
    }

    /**
     * @param intendOdAmount
     *            the intendOdAmount to set
     */
    public void setIntendOdAmount(Integer intendOdAmount) {
        this.intendOdAmount = intendOdAmount;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param middleName
     *            the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param dob
     *            the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @param gender
     *            the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @param currentAddress
     *            the currentAddress to set
     */
    public void setCurrentAddress(PostalAddressComponent currentAddress) {
        this.currentAddress = currentAddress;
    }

    /**
     * @param previousAddress
     *            the previousAddress to set
     */
    public void setPreviousAddress(PostalAddressComponent previousAddress) {
        this.previousAddress = previousAddress;
    }

    /**
     * @param maritalStatus
     *            the maritalStatus to set
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @param residentialStatus
     *            the residentialStatus to set
     */
    public void setResidentialStatus(String residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    /**
     * @param numberOfDependents
     *            the numberOfDependents to set
     */
    public void setNumberOfDependents(Integer numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }

    /**
     * @param fundSource
     *            the fundSource to set
     */
    public void setFundSource(String fundSource) {
        this.fundSource = fundSource;
    }

    /**
     * @param purpose
     *            the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * @param mobileNumber
     *            the mobileNumber to set
     */
    public void setMobileNumber(ContactNumber mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @param workPhone
     *            the workPhone to set
     */
    public void setWorkPhone(ContactNumber workPhone) {
        this.workPhone = workPhone;
    }

    /**
     * @param homePhone
     *            the homePhone to set
     */
    public void setHomePhone(ContactNumber homePhone) {
        this.homePhone = homePhone;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param marketPrefPhone
     *            the marketPrefPhone to set
     */
    public void setMarketPrefPhone(Boolean marketPrefPhone) {
        this.marketPrefPhone = marketPrefPhone;
    }

    /**
     * @param marketPrefEmail
     *            the marketPrefEmail to set
     */
    public void setMarketPrefEmail(Boolean marketPrefEmail) {
        this.marketPrefEmail = marketPrefEmail;
    }

    /**
     * @param marketPrefText
     *            the marketPrefText to set
     */
    public void setMarketPrefText(Boolean marketPrefText) {
        this.marketPrefText = marketPrefText;
    }

    /**
     * @param marketPrefPost
     *            the marketPrefPost to set
     */
    public void setMarketPrefPost(Boolean marketPrefPost) {
        this.marketPrefPost = marketPrefPost;
    }

    /**
     * @param ukArrivalDate
     *            the ukArrivalDate to set
     */
    public void setUkArrivalDate(Date ukArrivalDate) {
        this.ukArrivalDate = ukArrivalDate;
    }

    /**
     * @param visaExpiryDate
     *            the visaExpiryDate to set
     */
    public void setVisaExpiryDate(Date visaExpiryDate) {
        this.visaExpiryDate = visaExpiryDate;
    }

    /**
     * @param ilrStatus
     *            the ilrStatus to set
     */
    public void setIlrStatus(Boolean ilrStatus) {
        this.ilrStatus = ilrStatus;
    }

    /**
     * @param income
     *            the income to set
     */
    public void setIncome(Integer income) {
        this.income = income;
    }

    /**
     * @param employmentStatus
     *            the employmentStatus to set
     */
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    /**
     * @param occupnType
     *            the occupnType to set
     */
    public void setOccupnType(String occupnType) {
        this.occupnType = occupnType;
    }

    /**
     * @param employer
     *            the employer to set
     */
    public void setEmployer(EmployerDetails employer) {
        this.employer = employer;
    }

    /**
     * @param rentMortgCost
     *            the rentMortgCost to set
     */
    public void setRentMortgCost(Integer rentMortgCost) {
        this.rentMortgCost = rentMortgCost;
    }

    /**
     * @param maintnCost
     *            the maintnCost to set
     */
    public void setMaintnCost(Integer maintnCost) {
        this.maintnCost = maintnCost;
    }

    /**
     * @param savingsAmount
     *            the savingsAmount to set
     */
    public void setSavingsAmount(Integer savingsAmount) {
        this.savingsAmount = savingsAmount;
    }

    /**
     * @param prevBankYear
     *            the prevBankYear to set
     */
    public void setPrevBankYear(Integer prevBankYear) {
        this.prevBankYear = prevBankYear;
    }

    /**
     * @param prevBankMonth
     *            the prevBankMonth to set
     */
    public void setPrevBankMonth(Integer prevBankMonth) {
        this.prevBankMonth = prevBankMonth;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getOverDraftLimit() {
        return overDraftLimit;
    }

    public void setOverDraftLimit(String overDraftLimit) {
        this.overDraftLimit = overDraftLimit;
    }

    public RelatedInvolvedParty(SwitchOptions intendSwitch, Boolean intendOverDraft, Integer intendOdAmount,
            String title, String firstName, String middleName, String lastName, String email, Date dob, String gender,
            PostalAddressComponent currentAddress, PostalAddressComponent previousAddress, String maritalStatus,
            String residentialStatus, Integer numberOfDependents, String fundSource, String purpose,
            ContactNumber mobileNumber, ContactNumber workPhone, ContactNumber homePhone, Boolean marketPrefPhone,
            Boolean marketPrefEmail, Boolean marketPrefText, Boolean marketPrefPost, Date ukArrivalDate,
            Date visaExpiryDate, Boolean ilrStatus, Integer income, String employmentStatus, String occupnType,
            EmployerDetails employer, Integer rentMortgCost, Integer maintnCost, Integer savingsAmount,
            Integer prevBankYear, Integer prevBankMonth) {
        this.intendSwitch = intendSwitch;
        this.intendOverDraft = intendOverDraft;
        this.intendOdAmount = intendOdAmount;
        this.title = title;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.currentAddress = currentAddress;
        this.previousAddress = previousAddress;
        this.maritalStatus = maritalStatus;
        this.residentialStatus = residentialStatus;
        this.numberOfDependents = numberOfDependents;
        this.fundSource = fundSource;
        this.purpose = purpose;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.workPhone = workPhone;
        this.homePhone = homePhone;
        this.marketPrefPhone = marketPrefPhone;
        this.marketPrefEmail = marketPrefEmail;
        this.marketPrefText = marketPrefText;
        this.marketPrefPost = marketPrefPost;
        this.ukArrivalDate = ukArrivalDate;
        this.visaExpiryDate = visaExpiryDate;
        this.ilrStatus = ilrStatus;
        this.income = income;
        this.employmentStatus = employmentStatus;
        this.occupnType = occupnType;
        this.employer = employer;
        this.rentMortgCost = rentMortgCost;
        this.maintnCost = maintnCost;
        this.savingsAmount = savingsAmount;
        this.prevBankYear = prevBankYear;
        this.prevBankMonth = prevBankMonth;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public PostalAddressComponent getCurrentAddress() {
        return currentAddress;
    }

    public PostalAddressComponent getPreviousAddress() {
        return previousAddress;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getResidentialStatus() {
        return residentialStatus;
    }

    public Integer getNumberOfDependents() {
        return numberOfDependents;
    }

    public String getFundSource() {
        return fundSource;
    }

    public String getPurpose() {
        return purpose;
    }

    public ContactNumber getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public ContactNumber getWorkPhone() {
        return workPhone;
    }

    public ContactNumber getHomePhone() {
        return homePhone;
    }

    public Boolean getMarketPrefPhone() {
        return marketPrefPhone;
    }

    public Boolean getMarketPrefEmail() {
        return marketPrefEmail;
    }

    public Boolean getMarketPrefText() {
        return marketPrefText;
    }

    public Boolean getMarketPrefPost() {
        return marketPrefPost;
    }

    public Date getUkArrivalDate() {
        return ukArrivalDate;
    }

    public Date getVisaExpiryDate() {
        return visaExpiryDate;
    }

    public Boolean getIlrStatus() {
        return ilrStatus;
    }

    public Integer getIncome() {
        return income;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public String getOccupnType() {
        return occupnType;
    }

    public EmployerDetails getEmployer() {
        return employer;
    }

    public Integer getRentMortgCost() {
        return rentMortgCost;
    }

    public Integer getMaintnCost() {
        return maintnCost;
    }

    public Integer getSavingsAmount() {
        return savingsAmount;
    }

    public Integer getPrevBankYear() {
        return prevBankYear;
    }

    public Integer getPrevBankMonth() {
        return prevBankMonth;
    }

    public SwitchOptions getIntendSwitch() {
        return intendSwitch;
    }

    public Boolean getIntendOverDraft() {
        return intendOverDraft;
    }

    public Integer getIntendOdAmount() {
        return intendOdAmount;
    }

    public Double getExptdMntlyDepAmt() {
        return exptdMntlyDepAmt;
    }

    public void setExptdMntlyDepAmt(Double exptdMntlyDepAmt) {
        this.exptdMntlyDepAmt = exptdMntlyDepAmt;
    }

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }
    
    /**
	 * @return the cbsCustomerNumber
	 */
	public String getCbsCustomerNumber() {
		return cbsCustomerNumber;
	}

	/**
	 * @param cbsCustomerNumber the cbsCustomerNumber to set
	 */
	public void setCbsCustomerNumber(String cbsCustomerNumber) {
		this.cbsCustomerNumber = cbsCustomerNumber;
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
        return ReflectionToStringBuilder.toStringExclude(this, "dob");
    }
}
