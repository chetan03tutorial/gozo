package com.lbg.ib.api.sales.dto.bankwizard;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lbg.ib.api.sales.bankwizard.domain.BankInCASS;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ValidateBankDetailstResponseDTO {
    
    private boolean isValidIndicator = false;


    private boolean intraBrandSwitching = false;

    private BankInCASS bankInCASS;

    private String bankName;

    public ValidateBankDetailstResponseDTO(boolean isValidIndicator) {
        this.isValidIndicator = isValidIndicator;
    }

    public ValidateBankDetailstResponseDTO(boolean isValidIndicator,BankInCASS bankInCASS,String bankName,boolean intraBrandSwitching) {
        this.isValidIndicator = isValidIndicator;
        this.bankInCASS = bankInCASS;
        this.bankName = bankName;
        this.intraBrandSwitching = intraBrandSwitching;
    }

    public void setIsValidIndicator(boolean validIndicator) {
        isValidIndicator = validIndicator;
    }

    public boolean isIntraBrandSwitching() {
        return intraBrandSwitching;
    }

    public void setIntraBrandSwitching(boolean intraBrandSwitching) {
        this.intraBrandSwitching = intraBrandSwitching;
    }

    public BankInCASS getBankInCASS() {
        return this.bankInCASS;
    }

    public void setBankWarningResponse(BankInCASS bankInCASS) {
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
