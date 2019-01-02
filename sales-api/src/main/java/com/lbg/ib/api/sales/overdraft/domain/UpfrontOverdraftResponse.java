/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.overdraft.domain;

import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sso.domain.user.Account;

import java.util.List;

public class UpfrontOverdraftResponse {

    private Account selectedAccount;
    private List<PartyDetails> partyDetails;
    private String environmentName;
    private String colleagueId;
    private String originatingSortCode;
    private String domain;
    private Double totalOverdraftLimit;
    private String maxOverDraftLimit;// this is the traiagecode from q250


    /**
     * @return the totalOverdraftLimit
     */
    public Double getTotalOverdraftLimit() {
        return totalOverdraftLimit;
    }

    /**
     * @param totalOverdraftLimit the totalOverdraftLimit to set
     */
    public void setTotalOverdraftLimit(Double totalOverdraftLimit) {
        this.totalOverdraftLimit = totalOverdraftLimit;
    }

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

    public String getMaxOverDraftLimit() {
        return maxOverDraftLimit;
    }

    public void setMaxOverDraftLimit(String maxOverDraftLimit) {
        this.maxOverDraftLimit = maxOverDraftLimit;
    }
}