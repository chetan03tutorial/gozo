
package com.lbg.ib.api.sales.product.domain.arrangement;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OverdraftIntrestRates {

    private BigDecimal amtIntFreeOverdraft;

    private BigDecimal amtExcessFee;

    private BigDecimal amtExcessFeeBalIncr;

    private BigDecimal amtUsageFeeOverdraft;

    private BigDecimal amtTotalCreditCost;

    private BigInteger nExcessFeeCap;

    private String     intrateAuthEAR;

    private String     intrateAuthMnthly;

    private String     intrateBase;

    private String     intrateMarginOBR;

    private String     intrateUnauthEAR;

    private String     intrateUnauthMnthly;

    private BigDecimal amtOverdraft;

    public BigDecimal getAmtOverdraft() {
        return amtOverdraft;
    }

    public void setAmtOverdraft(BigDecimal amtOverdraft) {
        this.amtOverdraft = amtOverdraft;
    }

    public BigDecimal getAmtIntFreeOverdraft() {
        return this.amtIntFreeOverdraft;
    }

    public void setAmtIntFreeOverdraft(BigDecimal amtIntFreeOverdraft) {
        this.amtIntFreeOverdraft = amtIntFreeOverdraft;
    }

    public BigDecimal getAmtExcessFee() {
        return this.amtExcessFee;
    }

    public void setAmtExcessFee(BigDecimal amtExcessFee) {
        this.amtExcessFee = amtExcessFee;
    }

    public BigDecimal getAmtExcessFeeBalIncr() {
        return this.amtExcessFeeBalIncr;
    }

    public void setAmtExcessFeeBalIncr(BigDecimal amtExcessFeeBalIncr) {
        this.amtExcessFeeBalIncr = amtExcessFeeBalIncr;
    }

    public BigDecimal getAmtUsageFeeOverdraft() {
        return this.amtUsageFeeOverdraft;
    }

    public void setAmtUsageFeeOverdraft(BigDecimal amtUsageFeeOverdraft) {
        this.amtUsageFeeOverdraft = amtUsageFeeOverdraft;
    }

    public BigDecimal getAmtTotalCreditCost() {
        return this.amtTotalCreditCost;
    }

    public void setAmtTotalCreditCost(BigDecimal amtTotalCreditCost) {
        this.amtTotalCreditCost = amtTotalCreditCost;
    }

    public BigInteger getNExcessFeeCap() {
        return this.nExcessFeeCap;
    }

    public void setNExcessFeeCap(BigInteger nExcessFeeCap) {
        this.nExcessFeeCap = nExcessFeeCap;
    }

    public String getIntrateAuthEAR() {
        return this.intrateAuthEAR;
    }

    public void setIntrateAuthEAR(String intrateAuthEAR) {
        this.intrateAuthEAR = intrateAuthEAR;
    }

    public String getIntrateAuthMnthly() {
        return this.intrateAuthMnthly;
    }

    public void setIntrateAuthMnthly(String intrateAuthMnthly) {
        this.intrateAuthMnthly = intrateAuthMnthly;
    }

    public String getIntrateBase() {
        return this.intrateBase;
    }

    public void setIntrateBase(String intrateBase) {
        this.intrateBase = intrateBase;
    }

    public String getIntrateMarginOBR() {
        return this.intrateMarginOBR;
    }

    public void setIntrateMarginOBR(String intrateMarginOBR) {
        this.intrateMarginOBR = intrateMarginOBR;
    }

    public String getIntrateUnauthEAR() {
        return this.intrateUnauthEAR;
    }

    public void setIntrateUnauthEAR(String intrateUnauthEAR) {
        this.intrateUnauthEAR = intrateUnauthEAR;
    }

    public String getIntrateUnauthMnthly() {
        return this.intrateUnauthMnthly;
    }

    public void setIntrateUnauthMnthly(String intrateUnauthMnthly) {
        this.intrateUnauthMnthly = intrateUnauthMnthly;
    }

}
