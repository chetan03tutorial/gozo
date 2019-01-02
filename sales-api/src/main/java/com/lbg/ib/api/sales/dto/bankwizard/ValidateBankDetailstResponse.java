package com.lbg.ib.api.sales.dto.bankwizard;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lbg.ib.api.sales.bankwizard.domain.BankInCASS;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ValidateBankDetailstResponse {

    private boolean isValidIndicator = false;


    private boolean isIntraBrandSwitching = false;

    private BankInCASS bankInCASS;

    private String bankName;

    private boolean isError;

    public ValidateBankDetailstResponse(boolean isValidIndicator) {
        this.isValidIndicator = isValidIndicator;
    }

    public ValidateBankDetailstResponse(boolean isValidIndicator,BankInCASS bankInCASS,String bankName,boolean intraBrandSwitching) {
        this.isValidIndicator = isValidIndicator;
        this.bankInCASS = bankInCASS;
        this.bankName = bankName;
        this.isIntraBrandSwitching = intraBrandSwitching;
    }

    public boolean getIsIntraBrandSwitching() {
        return this.isIntraBrandSwitching;
    }

    public void setIntraBrandSwitching(boolean isIntraBrandSwitching) {
        this.isIntraBrandSwitching = isIntraBrandSwitching;
    }

    public BankInCASS getBankInCASS() {
        return this.bankInCASS;
    }

    public void setBankInCASS(BankInCASS bankInCASS) {
        this.bankInCASS = bankInCASS;
    }


    public boolean getIsValidIndicator() {
        return isValidIndicator;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean getIsError() {
        return isError;
    }

    public void setIsError(boolean error) {
        isError = error;
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }
}
