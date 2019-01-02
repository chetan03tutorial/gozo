/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.user.domain;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.List;

import com.lbg.ib.api.sso.domain.product.Category;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;

public class UserDetails {

    private PrimaryInvolvedParty primaryInvolvedParty;

    private SelectedAccount selectedAccount;

    private List<Account> accounts;

    private Boolean accountsFetchedStatus;

    private List<Category> categories;

    private String lastLoggedInTime;

    private boolean isMultiOverDraft;

    private Boolean kycRefreshRequired;

    private Boolean isErrorInGoJointEligibilityCheck;

    public UserDetails() {
        /*
         * Default comments to avoid Sonar Violations
         */
    }

    public UserDetails(PrimaryInvolvedParty primaryInvolvedParty, List<Account> accounts, List<Category> categories) {
        this.primaryInvolvedParty = primaryInvolvedParty;
        this.accounts = accounts;
        this.categories = categories;
    }

    /**
     * @return the primaryInvolvedParty
     */
    public PrimaryInvolvedParty getPrimaryInvolvedParty() {
        return primaryInvolvedParty;
    }

    /**
     * @param primaryInvolvedParty the primaryInvolvedParty to set
     */
    public void setPrimaryInvolvedParty(PrimaryInvolvedParty primaryInvolvedParty) {
        this.primaryInvolvedParty = primaryInvolvedParty;
    }

    /**
     * @return the accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the accountsFetchedStatus
     */
    public Boolean getAccountsFetchedStatus() {
        return accountsFetchedStatus;
    }

    /**
     * @param accountsFetchedStatus the accountsFetchedStatus to set
     */
    public void setAccountsFetchedStatus(Boolean accountsFetchedStatus) {
        this.accountsFetchedStatus = accountsFetchedStatus;
    }

    /**
     * @return the categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * @return the lastLoggedInTime
     */
    public String getLastLoggedInTime() {
        return lastLoggedInTime;
    }

    /**
     * @param lastLoggedInTime the lastLoggedInTime to set
     */
    public void setLastLoggedInTime(String lastLoggedInTime) {
        this.lastLoggedInTime = lastLoggedInTime;
    }

    /**
     * @return the isMultiOverDraft
     */
    public boolean isMultiOverDraft() {
        return isMultiOverDraft;
    }

    /**
     * @param isMultiOverDraft the isMultiOverDraft to set
     */
    public void setMultiOverDraft(boolean isMultiOverDraft) {
        this.isMultiOverDraft = isMultiOverDraft;
    }

    /**
     * @return the kycRefreshRequired
     */
    public Boolean getKycRefreshRequired() {
        return kycRefreshRequired;
    }

    /**
     * @param kycRefreshRequired the kycRefreshRequired to set
     */
    public void setKycRefreshRequired(Boolean kycRefreshRequired) {
        this.kycRefreshRequired = kycRefreshRequired;
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
        return reflectionToString(this);
    }

    /**
     * @return the selectedAccount
     */
    public SelectedAccount getSelectedAccount() {
        return selectedAccount;
    }

    /**
     * @param selectedAccount the selectedAccount to set
     */
    public void setSelectedAccount(SelectedAccount selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public void setIsErrorInGoJointEligibilityCheck(Boolean isErrorInGoJointEligibilityCheck) {
        this.isErrorInGoJointEligibilityCheck = isErrorInGoJointEligibilityCheck;
    }

    public Boolean getIsErrorInGoJointEligibilityCheck() {
        return isErrorInGoJointEligibilityCheck;
    }
}