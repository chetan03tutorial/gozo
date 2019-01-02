/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dto.product.offer;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lbg.ib.api.shared.validation.ApplicantType;
import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.PostalAddressComponentDTO;
import com.lbg.ib.api.sales.product.domain.arrangement.RelatedInvolvedParty;
import com.lbg.ib.api.sso.domain.product.arrangement.SwitchOptions;

public class OfferProductDTO {
    private SwitchOptions             intendSwitch;

    private Boolean                   intendOverDraft;

    private Integer                   intendOdAmount;

    private String                    prefixTitleName;

    private String                    firstName;

    private String                    middleName;

    private String                    lastName;

    private String                    emailAddress;

    private PhoneDTO                  mobilePhone;

    private PhoneDTO                  homePhone;

    private PhoneDTO                  workPhone;

    private PostalAddressComponentDTO currentAddress;

    private PostalAddressComponentDTO previousAddress;

    private String                    gender;

    private Date                      birthDate;

    private String                    maritalStatus;

    private BigInteger                numberOfDependants;

    private EmploymentDTO             employment;

    private BigDecimal                netMonthlyIncome;

    private String                    residentialStatus;

    private Date                      ukResidenceStartDate;

    private Date                      visaExpiryDate;

    private Boolean                   marketPreferencedByEmail;

    private Boolean                   marketPreferencedByMail;

    private Boolean                   marketPreferencedByPhone;

    private Boolean                   marketPreferencedBySMS;

    private BigDecimal                monthlyMortgageAmount;

    private BigDecimal                monthlyLoanRepaymentAmount;

    private BigDecimal                savingsAmount;

    private String                    fundingSource;

    private String                    accountPurpose;

    private String                    productIdentifier;

    private Map<String, String>       productOptions;

    private Map<String, String>       productExternalIdentifiers;

    private String                    productName;

    private String                    mnemonic;

    private RelatedApplicationDTO     relatedApplication;

    private String                    accType;

    private String                    birthCity;

    private TinDetailsDTO             tinDetails;

    private Double                    exptdMntlyDepAmt;

    private BigInteger                currentYearOfStudy;

    private String                    anticipateDateOfGraduation;

    private String                    ucasCode;

    private ThreatMatrixDTO           threatMatrixDTO;

    private boolean                   siraEnabledSwitch;

    private String                    exactDeviceId;

    private String                    smartDeviceIdConfidence;

    private String                    smartDeviceId;

    private String                    trueIp;

    private String                    accountLogin;

    private String                    tmxSummaryRiskScore;

    private String                    tmxSummaryReasonCode;

    private String                    tmxPolicyScore;

    private String                    tmxReasonCode;

    private String                    tmxRiskRating;

    private String                    tmxReviewStatus;

    private String                    deviceFirstSeen;

    private String                    deviceLastEvent;

    private String                    trueIpGeo;

    private String                    trueIpIsp;

    private String                    trueIpOrganization;

    private String                    proxyIpGeo;

    private String                    dnsIPGeo;

    private String                    browserLanguage;

    private String                    siraWorkFlowName;

    private String                    ocisId;

    private String                    partyID;

    private ApplicantType             applicantType;
    
    private RelatedInvolvedParty      relatedInvolvedParty;

    private String                    overDraftLimit;

    /**
    * List of marketingPreferences to store the consent options
    */
    private List<MarketingPreferenceDTO> marketingPreferences;

    public Double getExptdMntlyDepAmt() {
        return exptdMntlyDepAmt;
    }

    public void setExptdMntlyDepAmt(Double exptdMntlyDepAmt) {
        this.exptdMntlyDepAmt = exptdMntlyDepAmt;
    }

    public OfferProductDTO() {
        // Empty Sonar to avoid Sonar violations
    }

    public OfferProductDTO(SwitchOptions intendSwitch, Boolean intendOverDraft, Integer intendOdAmount,
            String prefixTitleName, String firstName, String middleName, String lastName, String emailAddress,
            PhoneDTO mobilePhone, PhoneDTO homePhone, PhoneDTO workPhone, PostalAddressComponentDTO currentAddress,
            PostalAddressComponentDTO previousAddress, String gender, Date birthDate, String maritalStatus,
            BigInteger numberOfDependants, EmploymentDTO employment, BigDecimal netMonthlyIncome,
            String residentialStatus, Date ukResidenceStartDate, Date visaExpiryDate, Boolean marketPreferencedByEmail,
            Boolean marketPreferencedByMail, Boolean marketPreferencedByPhone, Boolean marketPreferencedBySMS,
            BigDecimal monthlyMortgageAmount, BigDecimal monthlyLoanRepaymentAmount, BigDecimal savingsAmount,
            String fundingSource, String accountPurpose, String productIdentifier, Map<String, String> productOptions,
            Map<String, String> productExternalIdentifiers, String productName, String mnemonic,
            RelatedApplicationDTO relatedApplication, ThreatMatrixDTO threatMatrixDTO, List<MarketingPreferenceDTO> marketingPreferences,String overDraftLimit) {
        this.intendSwitch = intendSwitch;
        this.intendOverDraft = intendOverDraft;
        this.intendOdAmount = intendOdAmount;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.workPhone = workPhone;
        this.currentAddress = currentAddress;
        this.previousAddress = previousAddress;
        this.gender = gender;
        this.birthDate = birthDate;
        this.maritalStatus = maritalStatus;
        this.numberOfDependants = numberOfDependants;
        this.employment = employment;
        this.netMonthlyIncome = netMonthlyIncome;
        this.residentialStatus = residentialStatus;
        this.ukResidenceStartDate = ukResidenceStartDate;
        this.visaExpiryDate = visaExpiryDate;
        this.marketPreferencedByEmail = marketPreferencedByEmail;
        this.marketPreferencedByMail = marketPreferencedByMail;
        this.marketPreferencedByPhone = marketPreferencedByPhone;
        this.marketPreferencedBySMS = marketPreferencedBySMS;
        this.monthlyMortgageAmount = monthlyMortgageAmount;
        this.monthlyLoanRepaymentAmount = monthlyLoanRepaymentAmount;
        this.savingsAmount = savingsAmount;
        this.fundingSource = fundingSource;
        this.accountPurpose = accountPurpose;
        this.productIdentifier = productIdentifier;
        this.productOptions = productOptions;
        this.productExternalIdentifiers = productExternalIdentifiers;
        this.productName = productName;
        this.mnemonic = mnemonic;
        this.prefixTitleName = prefixTitleName;
        this.relatedApplication = relatedApplication;
        this.threatMatrixDTO = threatMatrixDTO;
        this.marketingPreferences = marketingPreferences;
        this.overDraftLimit = overDraftLimit;
    }

    public SwitchOptions getIntendSwitch() {
        return intendSwitch;
    }

    public void setIntendSwitch(SwitchOptions intendSwitch) {
        this.intendSwitch = intendSwitch;
    }

    public Boolean isIntendOverDraft() {
        return intendOverDraft;
    }

    public void setIsIntendOverDraft(Boolean intendOverDraft) {
        this.intendOverDraft = intendOverDraft;
    }

    public Integer getIntendOdAmount() {
        return intendOdAmount;
    }

    public void setIntendOdAmount(Integer intendOdAmount) {
        this.intendOdAmount = intendOdAmount;
    }

    public String prefixTitleName() {
        return prefixTitleName;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String middleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String gender() {
        return gender;
    }

    public Date birthDate() {
        return birthDate;
    }

    public String maritalStatus() {
        return maritalStatus;
    }

    public BigDecimal netMonthlyIncome() {
        return netMonthlyIncome;
    }

    public BigInteger numberOfDependants() {
        return numberOfDependants;
    }

    public Date ukResidenceStartDate() {
        return ukResidenceStartDate;
    }

    public Date visaExpiryDate() {
        return visaExpiryDate;
    }

    public String residentialStatus() {
        return residentialStatus;
    }

    public String emailAddress() {
        return emailAddress;
    }

    public PhoneDTO mobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(PhoneDTO mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public PhoneDTO homePhone() {
        return homePhone;
    }

    public PhoneDTO workPhone() {
        return workPhone;
    }

    public Boolean marketPreferencedByEmail() {
        return marketPreferencedByEmail;
    }

    public Boolean marketPreferencedByMail() {
        return marketPreferencedByMail;
    }

    public Boolean marketPreferencedByPhone() {
        return marketPreferencedByPhone;
    }

    public Boolean marketPreferencedBySMS() {
        return marketPreferencedBySMS;
    }

    public BigDecimal monthlyMortgageAmount() {
        return monthlyMortgageAmount;
    }

    public BigDecimal monthlyLoanRepaymentAmount() {
        return monthlyLoanRepaymentAmount;
    }

    public BigDecimal savingsAmount() {
        return savingsAmount;
    }

    public String fundingSource() {
        return fundingSource;
    }

    public String accountPurpose() {
        return accountPurpose;
    }

    public PostalAddressComponentDTO currentAddress() {
        return currentAddress;
    }

    public PostalAddressComponentDTO previousAddress() {
        return previousAddress;
    }

    public String productIdentifier() {
        return productIdentifier;
    }

    public Map<String, String> productOptions() {
        return productOptions;
    }

    public Map<String, String> productExternalIdentifiers() {
        return productExternalIdentifiers;
    }

    public String productName() {
        return productName;
    }

    public String mnemonic() {
        return mnemonic;
    }

    public EmploymentDTO employment() {
        return employment;
    }

    public RelatedApplicationDTO getRelatedApplication() {
        return relatedApplication;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public TinDetailsDTO getTinDetails() {
        return tinDetails;
    }

    public void setTinDetails(TinDetailsDTO tinDetails) {
        this.tinDetails = tinDetails;
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

    public String getUcasCode() {
        return ucasCode;
    }

    public void setUcasCode(String ucasCode) {
        this.ucasCode = ucasCode;
    }

    public ThreatMatrixDTO getThreatMatrixDTO() {
        return threatMatrixDTO;
    }

    public void setThreatMatrixDTO(ThreatMatrixDTO threatMatrixDTO) {
        this.threatMatrixDTO = threatMatrixDTO;
    }

    public void setSiraEnabledSwitch(boolean siraEnabledSwitch) {
        this.siraEnabledSwitch = siraEnabledSwitch;
    }

    public boolean getSiraEnabledSwitch() {
        return siraEnabledSwitch;
    }

    public String getExactDeviceId() {
        return exactDeviceId;
    }

    public void setExactDeviceId(String exactDeviceId) {
        this.exactDeviceId = exactDeviceId;
    }

    public String getSmartDeviceIdConfidence() {
        return smartDeviceIdConfidence;
    }

    public void setSmartDeviceIdConfidence(String smartDeviceIdConfidence) {
        this.smartDeviceIdConfidence = smartDeviceIdConfidence;
    }

    public String getSmartDeviceId() {
        return smartDeviceId;
    }

    public void setSmartDeviceId(String smartDeviceId) {
        this.smartDeviceId = smartDeviceId;
    }

    public String getTrueIp() {
        return trueIp;
    }

    public void setTrueIp(String trueIp) {
        this.trueIp = trueIp;
    }

    public String getTmxSummaryRiskScore() {
        return tmxSummaryRiskScore;
    }

    public void setTmxSummaryRiskScore(String tmxSummaryRiskScore) {
        this.tmxSummaryRiskScore = tmxSummaryRiskScore;
    }

    public String getTmxSummaryReasonCode() {
        return tmxSummaryReasonCode;
    }

    public void setTmxSummaryReasonCode(String tmxSummaryReasonCode) {
        this.tmxSummaryReasonCode = tmxSummaryReasonCode;
    }

    public String getTmxPolicyScore() {
        return tmxPolicyScore;
    }

    public void setTmxPolicyScore(String tmxPolicyScore) {
        this.tmxPolicyScore = tmxPolicyScore;
    }

    public String getTmxReasonCode() {
        return tmxReasonCode;
    }

    public void setTmxReasonCode(String tmxReasonCode) {
        this.tmxReasonCode = tmxReasonCode;
    }

    public String getTmxRiskRating() {
        return tmxRiskRating;
    }

    public void setTmxRiskRating(String tmxRiskRating) {
        this.tmxRiskRating = tmxRiskRating;
    }

    public String getTmxReviewStatus() {
        return tmxReviewStatus;
    }

    public void setTmxReviewStatus(String tmxReviewStatus) {
        this.tmxReviewStatus = tmxReviewStatus;
    }

    public String getTrueIpGeo() {
        return trueIpGeo;
    }

    public void setTrueIpGeo(String trueIpGeo) {
        this.trueIpGeo = trueIpGeo;
    }

    public String getTrueIpIsp() {
        return trueIpIsp;
    }

    public void setTrueIpIsp(String trueIpIsp) {
        this.trueIpIsp = trueIpIsp;
    }

    public String getTrueIpOrganization() {
        return trueIpOrganization;
    }

    public void setTrueIpOrganization(String trueIpOrganization) {
        this.trueIpOrganization = trueIpOrganization;
    }

    public String getProxyIpGeo() {
        return proxyIpGeo;
    }

    public void setProxyIpGeo(String proxyIpGeo) {
        this.proxyIpGeo = proxyIpGeo;
    }

    public String getAccountLogin() {
        return accountLogin;
    }

    public void setAccountLogin(String accountLogin) {
        this.accountLogin = accountLogin;
    }

    public String getDeviceFirstSeen() {
        return deviceFirstSeen;
    }

    public void setDeviceFirstSeen(String deviceFirstSeen) {
        this.deviceFirstSeen = deviceFirstSeen;
    }

    public String getDeviceLastEvent() {
        return deviceLastEvent;
    }

    public void setDeviceLastEvent(String deviceLastEvent) {
        this.deviceLastEvent = deviceLastEvent;
    }

    public String getDnsIPGeo() {
        return dnsIPGeo;
    }

    public void setDnsIPGeo(String dnsIPGeo) {
        this.dnsIPGeo = dnsIPGeo;
    }

    public String getBrowserLanguage() {
        return browserLanguage;
    }

    public void setBrowserLanguage(String browserLanguage) {
        this.browserLanguage = browserLanguage;
    }

    public String getSiraWorkFlowName() {
        return siraWorkFlowName;
    }

    public void setSiraWorkFlowName(String siraWorkFlowName) {
        this.siraWorkFlowName = siraWorkFlowName;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }

    public String getOverDraftLimit() {
        return overDraftLimit;
    }

    public void setOverDraftLimit(String overDraftLimit) {
        this.overDraftLimit = overDraftLimit;
    }

    /**
     * @param relatedApplication
     *            the relatedApplication to set
     */
    public void setRelatedApplication(RelatedApplicationDTO relatedApplication) {
        this.relatedApplication = relatedApplication;
    }

    public RelatedInvolvedParty getRelatedInvolvedParty() {
        return relatedInvolvedParty;
    }

    public void setRelatedInvolvedParty(RelatedInvolvedParty relatedInvolvedParty) {
        this.relatedInvolvedParty = relatedInvolvedParty;
    }

    public String getPartyID() {
        return partyID;
    }

    public void setPartyID(String partyID) {
        this.partyID = partyID;
    }

    public List<MarketingPreferenceDTO> getMarketingPreferences() {
        return marketingPreferences;
    }

    public void setMarketingPreferences(List<MarketingPreferenceDTO> marketingPreferences) {
        this.marketingPreferences = marketingPreferences;
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
        return ReflectionToStringBuilder.toStringExclude(this, "birthDate");
    }

}
