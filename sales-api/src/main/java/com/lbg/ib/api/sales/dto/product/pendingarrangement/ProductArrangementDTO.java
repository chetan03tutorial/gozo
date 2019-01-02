
package com.lbg.ib.api.sales.dto.product.pendingarrangement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.AccountDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.AffiliateDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ArrangementHistory;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Channel;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CommunicationPreference;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CompanyDetail;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Organisation;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductAccountKeyInfo;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangementIndicator;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Referral;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.RuleCondition;

public class ProductArrangementDTO {
    protected String                            arrangementType;
    protected String                            applicationStatus;
    protected String                            arrangementId;
    protected String                            accountNumber;
    protected Product                           associatedProduct;
    protected Channel                           initiatedThrough;
    protected String                            lifecycleStatus;
    protected String                            campaignCode;
    protected String                            insuranceCode;
    protected Customer                          primaryInvolvedParty;
    protected Organisation                      financialInstitution;
    protected Boolean                           marketingPreferenceBySMS;
    protected List<Customer>                    jointParties;
    protected Boolean                           isJointParty;
    protected String                            applicationSubStatus;
    protected List<AffiliateDetails>            affiliatedetails;
    protected List<Referral>                    referral;
    protected List<Product>                     offeredProducts;
    protected List<ArrangementHistory>          arrangementHistory;
    protected String                            applicationType;
    protected Integer                           retryCount;
    protected String                            accountPurpose;
    protected String                            fundingSource;
    protected XMLGregorianCalendar              arrangementStartDate;
    protected List<String>                      relatedEvents;
    protected String                            relatedApplicationId;
    protected String                            applicationRelationShipType;
    protected List<Product>                     existingProducts;
    protected ProductAccountKeyInfo             productAccountKeyInfo;
    protected List<CommunicationPreference>     communicationPreference;
    protected String                            communicationOption;
    protected String                            affiliateId;
    protected List<RuleCondition>               conditions;
    protected ReasonCode                        reasonCode;
    protected XMLGregorianCalendar              lastModifiedDate;
    protected String                            relatedApplicationStatus;
    protected Boolean                           relatedApplicationExists;
    protected Boolean                           marketingPreferenceByEmail;
    protected Boolean                           marketingPreferenceByPhone;
    protected Boolean                           marketingPreferenceByMail;
    protected List<ProductArrangementIndicator> cbsIndicators;
    protected String                            cctmSessionId;
    protected Integer                           fundingDays;
    protected Customer                          guardianDetails;
    protected AccountDetails                    accountDetails;
    protected List<CompanyDetail>               companyDetails;
    protected Boolean                           loanRefinanceIndicator;
    protected Boolean                           siraEnabledSwitch;
    protected CustomerDocument[]                customerDocuments;

    public String getArrangementType() {
        return arrangementType;
    }

    public void setArrangementType(String value) {
        this.arrangementType = value;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String value) {
        this.applicationStatus = value;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String value) {
        this.arrangementId = value;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    public Product getAssociatedProduct() {
        return associatedProduct;
    }

    public void setAssociatedProduct(Product value) {
        this.associatedProduct = value;
    }

    public Channel getInitiatedThrough() {
        return initiatedThrough;
    }

    public void setInitiatedThrough(Channel value) {
        this.initiatedThrough = value;
    }

    public String getLifecycleStatus() {
        return lifecycleStatus;
    }

    public void setLifecycleStatus(String value) {
        this.lifecycleStatus = value;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String value) {
        this.campaignCode = value;
    }

    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String value) {
        this.insuranceCode = value;
    }

    public Customer getPrimaryInvolvedParty() {
        return primaryInvolvedParty;
    }

    public void setPrimaryInvolvedParty(Customer value) {
        this.primaryInvolvedParty = value;
    }

    public Organisation getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(Organisation value) {
        this.financialInstitution = value;
    }

    public Boolean isMarketingPreferenceBySMS() {
        return marketingPreferenceBySMS;
    }

    public void setMarketingPreferenceBySMS(Boolean value) {
        this.marketingPreferenceBySMS = value;
    }

    public List<Customer> getJointParties() {
        if (jointParties == null) {
            jointParties = new ArrayList<Customer>();
        }
        return this.jointParties;
    }

    public Boolean isIsJointParty() {
        return isJointParty;
    }

    public void setIsJointParty(Boolean value) {
        this.isJointParty = value;
    }

    public String getApplicationSubStatus() {
        return applicationSubStatus;
    }

    public void setApplicationSubStatus(String value) {
        this.applicationSubStatus = value;
    }

    public List<AffiliateDetails> getAffiliatedetails() {
        if (affiliatedetails == null) {
            affiliatedetails = new ArrayList<AffiliateDetails>();
        }
        return this.affiliatedetails;
    }

    public List<Referral> getReferral() {
        if (referral == null) {
            referral = new ArrayList<Referral>();
        }
        return this.referral;
    }

    public List<Product> getOfferedProducts() {
        if (offeredProducts == null) {
            offeredProducts = new ArrayList<Product>();
        }
        return this.offeredProducts;
    }

    public List<ArrangementHistory> getArrangementHistory() {
        if (arrangementHistory == null) {
            arrangementHistory = new ArrayList<ArrangementHistory>();
        }
        return this.arrangementHistory;
    }

    public void setArrangementHistory(List<ArrangementHistory> arrangementHistory) {
        this.arrangementHistory = arrangementHistory;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String value) {
        this.applicationType = value;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer value) {
        this.retryCount = value;
    }

    public String getAccountPurpose() {
        return accountPurpose;
    }

    public void setAccountPurpose(String value) {
        this.accountPurpose = value;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String value) {
        this.fundingSource = value;
    }

    public XMLGregorianCalendar getArrangementStartDate() {
        return arrangementStartDate;
    }

    public void setArrangementStartDate(XMLGregorianCalendar value) {
        this.arrangementStartDate = value;
    }

    public List<String> getRelatedEvents() {
        if (relatedEvents == null) {
            relatedEvents = new ArrayList<String>();
        }
        return this.relatedEvents;
    }

    public String getRelatedApplicationId() {
        return relatedApplicationId;
    }

    public void setRelatedApplicationId(String value) {
        this.relatedApplicationId = value;
    }

    public String getApplicationRelationShipType() {
        return applicationRelationShipType;
    }

    public void setApplicationRelationShipType(String value) {
        this.applicationRelationShipType = value;
    }

    public List<Product> getExistingProducts() {
        if (existingProducts == null) {
            existingProducts = new ArrayList<Product>();
        }
        return this.existingProducts;
    }

    public ProductAccountKeyInfo getProductAccountKeyInfo() {
        return productAccountKeyInfo;
    }

    public void setProductAccountKeyInfo(ProductAccountKeyInfo value) {
        this.productAccountKeyInfo = value;
    }

    public List<CommunicationPreference> getCommunicationPreference() {
        if (communicationPreference == null) {
            communicationPreference = new ArrayList<CommunicationPreference>();
        }
        return this.communicationPreference;
    }

    public String getCommunicationOption() {
        return communicationOption;
    }

    public void setCommunicationOption(String value) {
        this.communicationOption = value;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String value) {
        this.affiliateId = value;
    }

    public List<RuleCondition> getConditions() {
        if (conditions == null) {
            conditions = new ArrayList<RuleCondition>();
        }
        return this.conditions;
    }

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(ReasonCode value) {
        this.reasonCode = value;
    }

    public XMLGregorianCalendar getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(XMLGregorianCalendar value) {
        this.lastModifiedDate = value;
    }

    public String getRelatedApplicationStatus() {
        return relatedApplicationStatus;
    }

    public void setRelatedApplicationStatus(String value) {
        this.relatedApplicationStatus = value;
    }

    public Boolean isRelatedApplicationExists() {
        return relatedApplicationExists;
    }

    public void setRelatedApplicationExists(Boolean value) {
        this.relatedApplicationExists = value;
    }

    public Boolean isMarketingPreferenceByEmail() {
        return marketingPreferenceByEmail;
    }

    public void setMarketingPreferenceByEmail(Boolean value) {
        this.marketingPreferenceByEmail = value;
    }

    public Boolean isMarketingPreferenceByPhone() {
        return marketingPreferenceByPhone;
    }

    public void setMarketingPreferenceByPhone(Boolean value) {
        this.marketingPreferenceByPhone = value;
    }

    public Boolean isMarketingPreferenceByMail() {
        return marketingPreferenceByMail;
    }

    public void setMarketingPreferenceByMail(Boolean value) {
        this.marketingPreferenceByMail = value;
    }

    public List<ProductArrangementIndicator> getCbsIndicators() {
        if (cbsIndicators == null) {
            cbsIndicators = new ArrayList<ProductArrangementIndicator>();
        }
        return this.cbsIndicators;
    }

    public String getCctmSessionId() {
        return cctmSessionId;
    }

    public void setCctmSessionId(String value) {
        this.cctmSessionId = value;
    }

    public Integer getFundingDays() {
        return fundingDays;
    }

    public void setFundingDays(Integer value) {
        this.fundingDays = value;
    }

    public Customer getGuardianDetails() {
        return guardianDetails;
    }

    public void setGuardianDetails(Customer value) {
        this.guardianDetails = value;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails value) {
        this.accountDetails = value;
    }

    public List<CompanyDetail> getCompanyDetails() {
        if (companyDetails == null) {
            companyDetails = new ArrayList<CompanyDetail>();
        }
        return this.companyDetails;
    }

    public Boolean isLoanRefinanceIndicator() {
        return loanRefinanceIndicator;
    }

    public void setLoanRefinanceIndicator(Boolean value) {
        this.loanRefinanceIndicator = value;
    }

    public Boolean isSIRAEnabledSwitch() {
        return siraEnabledSwitch;
    }

    public void setSIRAEnabledSwitch(Boolean value) {
        this.siraEnabledSwitch = value;
    }

    public CustomerDocument[] getCustomerDocuments() {
        return customerDocuments;
    }

    public void setCustomerDocuments(CustomerDocument[] customerDocuments) {
        this.customerDocuments = customerDocuments;
    }
}
