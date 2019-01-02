package com.lbg.ib.api.sales.product.domain.pending;

import java.io.Serializable;
import java.util.List;

import com.lbg.ib.api.sales.docupload.domain.CaseHistory;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.features.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TelephoneNumber;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.OverdraftDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.RuleCondition;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 8thSeptember2016
 ***********************************************************************/
public class CustomerPendingDetails implements Serializable {

    private Product                associatedProduct;

    private String                 arrangementType;

    private String                 arrangementId;

    private String                 arrangementStatus;

    private String                 arrangementSubStatus;

    private String                 accountNumber;
    private String                 sortCode;
    private String                 accountPurpose;
    private String                 appSubmissionDate;
    private String                 lastModifiedDate;

    private CustomerName           customerName;

    private String                 nationality;

    private String                 dob;

    private String                 gender;

    private String                 town;

    private String                 countryOfBirth;

    private PostalAddressComponent postalAddComp;

    private String                 emailAddress;

    private List<TelephoneNumber>  telephones;

    private List<CustomerDocument> customerDocuments;

    private String                 caseCreationDate;
    private String                 colleagueId;
    private String                 caseStatus;
    private long                   caseId;
    private CaseHistory[]          caseHistory;

    private String                 applicationType;

    private String                 cidPersID;

    private String                 customerIdentifier;

    private List<CustomerScore>    customerScores;

    protected OverdraftDetails     overdraftDetails;

    protected List<RuleCondition>  conditions;

    private String                 originatingSortCode;

    private String                 internalUserIdentifier;

    private String                 channelCode;

    private String                 subChannelCode;

    public CustomerPendingDetails() {
        // To Avoid Sonar Violations
    }

    public CustomerPendingDetails(Product associatedPdt, String arrangementType, String arrangementId,
            String arrangementStatus, CustomerName customerName, String nationality, String dob, String gender,
            String town, String countryOfBirth, PostalAddressComponent postalAddComp, String emailAddress,
            List<TelephoneNumber> telephones) {
        this.associatedProduct = associatedPdt;
        this.arrangementId = arrangementId;
        this.arrangementType = arrangementType;
        this.arrangementStatus = arrangementStatus;
        this.customerName = customerName;
        this.dob = dob;
        this.nationality = nationality;
        this.gender = gender;
        this.town = town;
        this.countryOfBirth = countryOfBirth;
        this.postalAddComp = postalAddComp;
        this.emailAddress = emailAddress;
        this.telephones = telephones;
    }

    /**
     * @return the associatedProduct
     */
    public Product getAssociatedProduct() {
        return associatedProduct;
    }

    /**
     * @param associatedProduct
     *            the associatedProduct to set
     */
    public void setAssociatedProduct(Product associatedProduct) {
        this.associatedProduct = associatedProduct;
    }

    /**
     * @return the customerName
     */
    public CustomerName getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     *            the customerName to set
     */
    public void setCustomerName(CustomerName customerName) {
        this.customerName = customerName;
    }

    /*  *//**
           * @return the nationalities
           */
    /*
     * public List<String> getNationalities() { return nationalities; }
     *//**
       * @param nationalities
       *            the nationalities to set
       */
    /*
     * public void setNationalities(List<String> nationalities) {
     * this.nationalities = nationalities; }
     */

    /**
     * @return the dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob
     *            the dob to set
     */
    public void setDob(String dob) {
        this.dob = dob;
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
     * @return the town
     */
    public String getTown() {
        return town;
    }

    /**
     * @param town
     *            the town to set
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * @return the countryOfBirth
     */
    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    /**
     * @param countryOfBirth
     *            the countryOfBirth to set
     */
    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    /**
     * @return the postalAddComp
     */
    public PostalAddressComponent getPostalAddComp() {
        return postalAddComp;
    }

    /**
     * @param postalAddComp
     *            the postalAddComp to set
     */
    public void setPostalAddComp(PostalAddressComponent postalAddComp) {
        this.postalAddComp = postalAddComp;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress
     *            the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the arrangementType
     */
    public String getArrangementType() {
        return arrangementType;
    }

    /**
     * @param arrangementType
     *            the arrangementType to set
     */
    public void setArrangementType(String arrangementType) {
        this.arrangementType = arrangementType;
    }

    /**
     * @return the arrangementId
     */
    public String getArrangementId() {
        return arrangementId;
    }

    /**
     * @param arrangementId
     *            the arrangementId to set
     */
    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    /**
     * @return the arrangementStatus
     */
    public String getArrangementStatus() {
        return arrangementStatus;
    }

    /**
     * @param arrangementStatus
     *            the arrangementStatus to set
     */
    public void setArrangementStatus(String arrangementStatus) {
        this.arrangementStatus = arrangementStatus;
    }

    /**
     * @return the telephones
     */
    public List<TelephoneNumber> getTelephones() {
        return telephones;
    }

    /**
     * @param telephones
     *            the telephones to set
     */
    public void setTelephones(List<TelephoneNumber> telephones) {
        this.telephones = telephones;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality
     *            the nationality to set
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<CustomerDocument> getCustomerDocuments() {
        return customerDocuments;
    }

    public void setCustomerDocuments(List<CustomerDocument> customerDocuments) {
        this.customerDocuments = customerDocuments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccountPurpose() {
        return accountPurpose;
    }

    public void setAccountPurpose(String accountPurpose) {
        this.accountPurpose = accountPurpose;
    }

    public String getAppSubmissionDate() {
        return appSubmissionDate;
    }

    public void setAppSubmissionDate(String appSubmissionDate) {
        this.appSubmissionDate = appSubmissionDate;
    }

    public CaseHistory[] getCaseHistory() {
        return caseHistory;
    }

    public void setCaseHistory(CaseHistory[] caseHistory) {
        this.caseHistory = caseHistory;
    }

    public String getCaseCreationDate() {
        return caseCreationDate;
    }

    public void setCaseCreationDate(String caseCreationDate) {
        this.caseCreationDate = caseCreationDate;
    }

    public String getColleagueId() {
        return colleagueId;
    }

    public void setColleagueId(String colleagueId) {
        this.colleagueId = colleagueId;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public long getCaseId() {
        return caseId;
    }

    public void setCaseId(long caseId) {
        this.caseId = caseId;
    }

    public String getArrangementSubStatus() {
        return arrangementSubStatus;
    }

    public void setArrangementSubStatus(String arrangementSubStatus) {
        this.arrangementSubStatus = arrangementSubStatus;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getCidPersID() {
        return cidPersID;
    }

    public void setCidPersID(String cidPersID) {
        this.cidPersID = cidPersID;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public List<CustomerScore> getCustomerScores() {
        return customerScores;
    }

    public void setCustomerScores(List<CustomerScore> customerScores) {
        this.customerScores = customerScores;
    }

    public OverdraftDetails getOverdraftDetails() {
        return overdraftDetails;
    }

    public void setOverdraftDetails(OverdraftDetails overdraftDetails) {
        this.overdraftDetails = overdraftDetails;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<RuleCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RuleCondition> conditions) {
        this.conditions = conditions;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getSubChannelCode() {
        return subChannelCode;
    }

    public void setSubChannelCode(String subChannelCode) {
        this.subChannelCode = subChannelCode;
    }


    public String getOriginatingSortCode() {
        return originatingSortCode;
    }

    public void setOriginatingSortCode(String originatingSortCode) {
        this.originatingSortCode = originatingSortCode;
    }

    public String getInternalUserIdentifier() {
        return internalUserIdentifier;
    }

    public void setInternalUserIdentifier(String internalUserIdentifier) {
        this.internalUserIdentifier = internalUserIdentifier;
    }

}
