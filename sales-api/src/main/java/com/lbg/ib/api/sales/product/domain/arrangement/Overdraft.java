package com.lbg.ib.api.sales.product.domain.arrangement;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;

public class Overdraft {

    private BigDecimal intFreeAmount;
    private String     intEar;
    private String     intMnthEar;
    private BigDecimal usageFee;
    @RequiredFieldValidation
    private BigDecimal amount;
    private BigDecimal amtExcessFee;
    private BigDecimal amtExcessFeeBalIncr;
    private BigDecimal amtTotalCreditCost;
    private String     intrateBase;
    private String     intrateMarginOBR;
    private String     intrateUnauthEAR;
    private String     intrateUnauthMnthly;
    private BigInteger nExcessFeeCap;

    public BigDecimal getIntFreeAmount() {
        return intFreeAmount;
    }

    public void setIntFreeAmount(BigDecimal intFreeAmount) {
        this.intFreeAmount = intFreeAmount;
    }

    public String getIntEar() {
        return intEar;
    }

    public void setIntEar(String intEar) {
        this.intEar = intEar;
    }

    public String getIntMnthEar() {
        return intMnthEar;
    }

    public void setIntMnthEar(String intMnthEar) {
        this.intMnthEar = intMnthEar;
    }

    public BigDecimal getUsageFee() {
        return usageFee;
    }

    public void setUsageFee(BigDecimal usageFee) {
        this.usageFee = usageFee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmtExcessFee() {
        return amtExcessFee;
    }

    public void setAmtExcessFee(BigDecimal amtExcessFee) {
        this.amtExcessFee = amtExcessFee;
    }

    public BigDecimal getAmtExcessFeeBalIncr() {
        return amtExcessFeeBalIncr;
    }

    public void setAmtExcessFeeBalIncr(BigDecimal amtExcessFeeBalIncr) {
        this.amtExcessFeeBalIncr = amtExcessFeeBalIncr;
    }

    public BigDecimal getAmtTotalCreditCost() {
        return amtTotalCreditCost;
    }

    public void setAmtTotalCreditCost(BigDecimal amtTotalCreditCost) {
        this.amtTotalCreditCost = amtTotalCreditCost;
    }

    public String getIntrateBase() {
        return intrateBase;
    }

    public void setIntrateBase(String intrateBase) {
        this.intrateBase = intrateBase;
    }

    public String getIntrateMarginOBR() {
        return intrateMarginOBR;
    }

    public void setIntrateMarginOBR(String intrateMarginOBR) {
        this.intrateMarginOBR = intrateMarginOBR;
    }

    public String getIntrateUnauthEAR() {
        return intrateUnauthEAR;
    }

    public void setIntrateUnauthEAR(String intrateUnauthEAR) {
        this.intrateUnauthEAR = intrateUnauthEAR;
    }

    public String getIntrateUnauthMnthly() {
        return intrateUnauthMnthly;
    }

    public void setIntrateUnauthMnthly(String intrateUnauthMnthly) {
        this.intrateUnauthMnthly = intrateUnauthMnthly;
    }

    public BigInteger getnExcessFeeCap() {
        return nExcessFeeCap;
    }

    public void setnExcessFeeCap(BigInteger nExcessFeeCap) {
        this.nExcessFeeCap = nExcessFeeCap;
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

}
