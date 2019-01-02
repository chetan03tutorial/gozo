package com.lbg.ib.api.sales.switching.domain;

import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.sales.common.domain.PegaSwitchTypeEnum;
import com.lbg.ib.api.sales.common.validation.BigDecimalFieldValidation;
import com.lbg.ib.api.shared.validation.IntegerFieldValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Validate
public class AccountSwitchingRequest {
    @RequiredFieldValidation
    private Date                 switchDate;

    @RequiredFieldValidation
    private Boolean              canBeOverDrawn;

    @RequiredFieldValidation
    @BigDecimalFieldValidation
    private BigDecimal           payOdAmount;

    @RequiredFieldValidation
    private SwitchingAccount     newAccountDetails;

    @RequiredFieldValidation
    private SwitchingAccount     oldAccountDetails;

    @RequiredFieldValidation
    private PegaSwitchTypeEnum switchingType;

    private List<SwitchingParty> parties;

    public AccountSwitchingRequest() {
    }

    public AccountSwitchingRequest(Date switchDate, Boolean canBeOverDrawn, BigDecimal payOdAmount,
            SwitchingAccount newAccountDetails, SwitchingAccount oldAccountDetails,
            List<SwitchingParty> parties, PegaSwitchTypeEnum switchingType) {
        this.switchDate = switchDate;
        this.canBeOverDrawn = canBeOverDrawn;
        this.payOdAmount = payOdAmount;
        this.newAccountDetails = newAccountDetails;
        this.oldAccountDetails = oldAccountDetails;
        this.parties = parties;
        this.switchingType = switchingType;
    }

    public Date getSwitchDate() {
        return switchDate;
    }

    public void setSwitchDate(Date switchDate) {
        this.switchDate = switchDate;
    }

    public Boolean getCanBeOverDrawn() {
        return canBeOverDrawn;
    }

    public void setCanBeOverDrawn(Boolean canBeOverDrawn) {
        this.canBeOverDrawn = canBeOverDrawn;
    }

    public BigDecimal getPayOdAmount() {
        return payOdAmount;
    }

    public void setPayOdAmount(BigDecimal payOdAmount) {
        this.payOdAmount = payOdAmount;
    }

    public SwitchingAccount getNewAccountDetails() {
        return newAccountDetails;
    }

    public void setNewAccountDetails(SwitchingAccount newAccountDetails) {
        this.newAccountDetails = newAccountDetails;
    }

    public SwitchingAccount getOldAccountDetails() {
        return oldAccountDetails;
    }

    public void setOldAccountDetails(SwitchingAccount oldAccountDetails) {
        this.oldAccountDetails = oldAccountDetails;
    }

    public List<SwitchingParty> getParties() {
        return parties;
    }

    public void setParties(List<SwitchingParty> parties) {
        this.parties = parties;
    }

    public PegaSwitchTypeEnum getSwitchingType() {
        return switchingType;
    }

    public void setSwitchingType(PegaSwitchTypeEnum switchingType) {
        this.switchingType = switchingType;
    }

    @Override public String toString() {
        return "AccountSwitchingRequest{" + "switchDate=" + switchDate + ", canBeOverDrawn=" + canBeOverDrawn
                + ", payOdAmount=" + payOdAmount + ", newAccountDetails=" + newAccountDetails + ", oldAccountDetails="
                + oldAccountDetails + ", switchingType=" + switchingType + ", parties=" + parties + '}';
    }
}
