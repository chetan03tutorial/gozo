package com.lbg.ib.api.sales.overdraft.domain;

public class PcciResponse {

    private String feeFreeOverdraftAmount;
    private String dailyOverdraftAmount;
    private String dailyCharge;
    private String dailyStep;
    private String plannedOverdraftFee;
    private String usageFee;
    private String totalCostOfCreditAmount;
    private String currency;
    private String unauthorisedOverdraftRate;
    private String unauthrorisedGrossProductInterestRate;
    private String unauthorisedAnnualInterestRate;
    private String interestFeeWaiverAmount;
    private String excessFeeAmount;
    private String excessFeeBalance;

    public String getFeeFreeOverdraftAmount() {
        return feeFreeOverdraftAmount;
    }

    public void setFeeFreeOverdraftAmount(String feeFreeOverdraftAmount) {
        this.feeFreeOverdraftAmount = feeFreeOverdraftAmount;
    }

    public String getDailyOverdraftAmount() {
        return dailyOverdraftAmount;
    }

    public void setDailyOverdraftAmount(String dailyOverdraftAmount) {
        this.dailyOverdraftAmount = dailyOverdraftAmount;
    }

    public String getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(String dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    public String getDailyStep() {
        return dailyStep;
    }

    public void setDailyStep(String dailyStep) {
        this.dailyStep = dailyStep;
    }

    public String getPlannedOverdraftFee() {
        return plannedOverdraftFee;
    }

    public void setPlannedOverdraftFee(String plannedOverdraftFee) {
        this.plannedOverdraftFee = plannedOverdraftFee;
    }

    public String getUsageFee() {
        return usageFee;
    }

    public void setUsageFee(String usageFee) {
        this.usageFee = usageFee;
    }

    public String getTotalCostOfCreditAmount() {
        return totalCostOfCreditAmount;
    }

    public void setTotalCostOfCreditAmount(String totalCostOfCreditAmount) {
        this.totalCostOfCreditAmount = totalCostOfCreditAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUnauthorisedOverdraftRate() {
        return unauthorisedOverdraftRate;
    }

    public void setUnauthorisedOverdraftRate(String unauthorisedOverdraftRate) {
        this.unauthorisedOverdraftRate = unauthorisedOverdraftRate;
    }

    public String getUnauthrorisedGrossProductInterestRate() {
        return unauthrorisedGrossProductInterestRate;
    }

    public void setUnauthrorisedGrossProductInterestRate(String unauthrorisedGrossProductInterestRate) {
        this.unauthrorisedGrossProductInterestRate = unauthrorisedGrossProductInterestRate;
    }

    public String getUnauthorisedAnnualInterestRate() {
        return unauthorisedAnnualInterestRate;
    }

    public void setUnauthorisedAnnualInterestRate(String unauthorisedAnnualInterestRate) {
        this.unauthorisedAnnualInterestRate = unauthorisedAnnualInterestRate;
    }

    public String getInterestFeeWaiverAmount() {
        return interestFeeWaiverAmount;
    }

    public void setInterestFeeWaiverAmount(String interestFeeWaiverAmount) {
        this.interestFeeWaiverAmount = interestFeeWaiverAmount;
    }

    public String getExcessFeeAmount() {
        return excessFeeAmount;
    }

    public void setExcessFeeAmount(String excessFeeAmount) {
        this.excessFeeAmount = excessFeeAmount;
    }

    public String getExcessFeeBalance() {
        return excessFeeBalance;
    }

    public void setExcessFeeBalance(String excessFeeBalance) {
        this.excessFeeBalance = excessFeeBalance;
    }
}
