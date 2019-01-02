/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.user.domain;

import java.util.List;

import com.lbg.ib.api.sso.domain.user.Account;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.sso.domain.product.Category;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;

//@Validate
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserArrangement {

    @RequiredFieldValidation
    private PrimaryInvolvedParty primaryInvolvedParty;

    private List<Account>        accounts;

    private Boolean              accountsFetchedStatus;

    private List<Category>       categories;

    private String               ibSessionId;

    private String               host;

    private String               partyId;

    private String               ocisId;

    private String               lastLoggedInTime;

    private boolean              isMultiOverDraft;

    private String               internalUserIdentifier;

    private long                 markedLimit;

    public UserArrangement() {
        // to avoid sonar major voilation
    }

    public UserArrangement(PrimaryInvolvedParty primaryInvolvedParty, List<Account> accounts,
            List<Category> categories) {
        this.primaryInvolvedParty = primaryInvolvedParty;
        this.accounts = accounts;
        this.categories = categories;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public PrimaryInvolvedParty getPrimaryInvolvedParty() {
        return primaryInvolvedParty;
    }

    public void setPrimaryInvolvedParty(PrimaryInvolvedParty primaryInvolvedParty) {
        this.primaryInvolvedParty = primaryInvolvedParty;
    }

    public Boolean isAccountsFetchedStatus() {
        return accountsFetchedStatus;
    }

    public void setAccountsFetchedStatus(Boolean accountsFetchedStatus) {
        this.accountsFetchedStatus = accountsFetchedStatus;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getIbSessionId() {
        return ibSessionId;
    }

    public void setIbSessionId(String ibSessionId) {
        this.ibSessionId = ibSessionId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }

    public String getLastLoggedInTime() {
        return lastLoggedInTime;
    }

    public void setLastLoggedInTime(String lastLoggedInTime) {
        this.lastLoggedInTime = lastLoggedInTime;
    }

    public boolean isMultiOverDraft() {
        return isMultiOverDraft;
    }

    public void setMultiOverDraft(boolean isMultiOverDraft) {
        this.isMultiOverDraft = isMultiOverDraft;
    }

    public String getInternalUserIdentifier() {
        return internalUserIdentifier;
    }

    public void setInternalUserIdentifier(String internalUserIdentifier) {
        this.internalUserIdentifier = internalUserIdentifier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
        result = prime * result + ((accountsFetchedStatus == null) ? 0 : accountsFetchedStatus.hashCode());
        result = prime * result + ((categories == null) ? 0 : categories.hashCode());
        result = prime * result + ((primaryInvolvedParty == null) ? 0 : primaryInvolvedParty.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserArrangement other = (UserArrangement) obj;
        if (accounts == null) {
            if (other.accounts != null) {
                return false;
            }
        } else if (!accounts.equals(other.accounts)) {
            return false;
        }
        if (accountsFetchedStatus == null) {
            if (other.accountsFetchedStatus != null) {
                return false;
            }
        } else if (!accountsFetchedStatus.equals(other.accountsFetchedStatus)) {
            return false;
        }
        if (categories == null) {
            if (other.categories != null) {
                return false;
            }
        } else if (!categories.equals(other.categories)) {
            return false;
        }
        if (primaryInvolvedParty == null) {
            if (other.primaryInvolvedParty != null) {
                return false;
            }
        } else if (!primaryInvolvedParty.equals(other.primaryInvolvedParty)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductArrangement [primaryInvolvedParty=" + primaryInvolvedParty + ", accounts=" + accounts
                + ", accountsFetchedStatus=" + accountsFetchedStatus + ", categories=" + categories + "]";
    }

    public long getMarkedLimit() {
        return markedLimit;
    }

    public void setMarkedLimit(long markedLimit) {
        this.markedLimit = markedLimit;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
