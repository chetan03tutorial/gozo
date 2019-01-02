/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.dto;

import java.util.List;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class UserPreferences {
    /**
     * Email Address of user;
     */
    private String emailAddress;
    /**
     * Account list.
     */
    @RequiredFieldValidation
    private List<Account> accounts;

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
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
