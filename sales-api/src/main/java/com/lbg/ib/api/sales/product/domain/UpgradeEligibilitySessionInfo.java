package com.lbg.ib.api.sales.product.domain;

import java.util.List;

import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sso.domain.user.Account;

/**
 * Created by 8601769 on 06/03/2018. This class is pojo for holding session information.
 */
public class UpgradeEligibilitySessionInfo {

    private Account selectedAccount;
    private List<PartyDetails> partyDetails;
    private String environmentName;
    private String colleagueId;
    private String originatingSortCode;
    private String domain;
    private Double totalOverdraftLimit;
    private Double shadowLimit;
    private String maxOverDraftLimit;


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
     * @return the shadowLimit
     */
    public Double getShadowLimit() {
        return shadowLimit;
    }

    /**
     * @param shadowLimit the shadowLimit to set
     */
    public void setShadowLimit(Double shadowLimit) {
        this.shadowLimit = shadowLimit;
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
