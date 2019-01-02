/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.overdraft.domain;

import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sso.domain.user.Account;

import java.util.Date;
import java.util.List;

public class SelectedAccountSessionInfo {

    private Account selectedAccount;
    private List<PartyDetails> partyDetails;
    private String environmentName;
    private String colleagueId;
    private String originatingSortCode;
    private String domain;
    private Double maxOverDraftLimit;
    private Double demandedOD;
    private String userName;
    private String appScoreReferenceNumber;
    private String productMnemonic;
    private Date switchingDate;
    private Condition[]   conditions;


    /**
     * @return the colleagueId
     */
    public String getColleagueId() {
        return colleagueId;
    }

    /**
     * @param colleagueId the colleagueId to set
     */
    public void setColleagueId(String colleagueId) {
        this.colleagueId = colleagueId;
    }

    /**
     * @return the originatingSortCode
     */
    public String getOriginatingSortCode() {
        return originatingSortCode;
    }

    /**
     * @param originatingSortCode the originatingSortCode to set
     */
    public void setOriginatingSortCode(String originatingSortCode) {
        this.originatingSortCode = originatingSortCode;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public List<PartyDetails> getPartyDetails() {
        return partyDetails;
    }

    public void setPartyDetails(List<PartyDetails> partyDetails) {
        this.partyDetails = partyDetails;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public Double getMaxOverDraftLimit() {
        return maxOverDraftLimit;
    }

    public void setMaxOverDraftLimit(Double maxOverDraftLimit) {
        this.maxOverDraftLimit = maxOverDraftLimit;
    }

    public Double getDemandedOD() {
        return demandedOD;
    }

    public void setDemandedOD(Double demandedOD) {
        this.demandedOD = demandedOD;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppScoreReferenceNumber() {
        return appScoreReferenceNumber;
    }

    public void setAppScoreReferenceNumber(String appScoreReferenceNumber) {
        this.appScoreReferenceNumber = appScoreReferenceNumber;
    }

    public String getProductMnemonic() {
        return productMnemonic;
    }

    public void setProductMnemonic(String productMnemonic) {
        this.productMnemonic = productMnemonic;
    }

    public Date getSwitchingDate() {
        return switchingDate;
    }

    public void setSwitchingDate(Date switchingDate) {
        this.switchingDate = switchingDate;
    }

    public Condition[] getConditions() {
        return conditions;
    }

    public void setConditions(Condition[] conditions) {
        this.conditions = conditions;
    }
}