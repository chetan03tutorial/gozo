/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.eligibility;

import java.util.List;

import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.validation.AccountTypeValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;

@Validate
public class DetermineEligibilityRequest {

    @RequiredFieldValidation
    private PrimaryInvolvedParty primaryInvolvedParty;

    @AccountTypeValidation(value = { AccountType.CA, AccountType.SA })
    private AccountType          arrangementType;

    @RequiredFieldValidation
    private List<String>         mnemonic;

    public DetermineEligibilityRequest() {
        /* jackson */}

    public PrimaryInvolvedParty getPrimaryInvolvedParty() {
        return primaryInvolvedParty;
    }

    public void setPrimaryInvolvedParty(PrimaryInvolvedParty primaryInvolvedParty) {
        this.primaryInvolvedParty = primaryInvolvedParty;
    }

    /**
     * @return the mnemonic
     */
    public List<String> getMnemonic() {
        return mnemonic;
    }

    /**
     * @param mnemonic
     *            the mnemonic to set
     */
    public void setMnemonic(List<String> mnemonic) {
        this.mnemonic = mnemonic;
    }

    public AccountType getArrangementType() {
        return arrangementType;
    }

    public void setArrangementType(AccountType arrangementType) {
        this.arrangementType = arrangementType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DetermineEligibilityRequest that = (DetermineEligibilityRequest) o;

        if (primaryInvolvedParty != null ? !primaryInvolvedParty.equals(that.primaryInvolvedParty)
                : that.primaryInvolvedParty != null) {
            return false;
        }
        return !(arrangementType != null ? !arrangementType.equals(that.arrangementType)
                : that.arrangementType != null);

    }

    @Override
    public int hashCode() {
        int result = primaryInvolvedParty != null ? primaryInvolvedParty.hashCode() : 0;
        result = 31 * result + (arrangementType != null ? arrangementType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DetermineEligibilityRequest{" + ", arrangementType=" + arrangementType + '}';
    }
}
