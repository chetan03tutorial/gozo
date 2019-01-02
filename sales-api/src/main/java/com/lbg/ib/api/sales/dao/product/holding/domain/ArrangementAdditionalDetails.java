package com.lbg.ib.api.sales.dao.product.holding.domain;

import java.util.List;

import com.lbg.ib.api.sso.domain.product.Indicator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrangementAdditionalDetails {

    private Double amtLedgerBal;

    private Double amtAvailableBal;

    private Boolean acceptTransfers;

    private Boolean balanceInfoIsValid;

    private Double amtOverdraftLimit;

    private String name;

    private String accGroupType;

    private String currencyCode;

    private List<Indicator> indicators;

    private String lifecycleStatus;

    public String getLifecycleStatus() {
        return lifecycleStatus;
    }

    public void setLifecycleStatus(String lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }

    public Double getAmtLedgerBal() {
        return amtLedgerBal;
    }

    public void setAmtLedgerBal(Double amtLedgerBal) {
        this.amtLedgerBal = amtLedgerBal;
    }

    public Double getAmtAvailableBal() {
        return amtAvailableBal;
    }

    public void setAmtAvailableBal(Double amtAvailableBal) {
        this.amtAvailableBal = amtAvailableBal;
    }

    public Boolean getAcceptTransfers() {
        return acceptTransfers;
    }

    public void setAcceptTransfers(Boolean acceptTransfers) {
        this.acceptTransfers = acceptTransfers;
    }

    public Boolean getBalanceInfoIsValid() {
        return balanceInfoIsValid;
    }

    public void setBalanceInfoIsValid(Boolean balanceInfoIsValid) {
        this.balanceInfoIsValid = balanceInfoIsValid;
    }

    public Double getAmtOverdraftLimit() {
        return amtOverdraftLimit;
    }

    public void setAmtOverdraftLimit(Double amtOverdraftLimit) {
        this.amtOverdraftLimit = amtOverdraftLimit;
    }

    public String getAccGroupType() {
        return accGroupType;
    }

    public void setAccGroupType(String accGroupType) {
        this.accGroupType = accGroupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArrangementAdditionalDetails that = (ArrangementAdditionalDetails) o;

        if (amtLedgerBal != null ? !amtLedgerBal.equals(that.amtLedgerBal) : that.amtLedgerBal != null) {
            return false;
        }
        if (amtAvailableBal != null ? !amtAvailableBal.equals(that.amtAvailableBal) : that.amtAvailableBal != null) {
            return false;
        }
        if (acceptTransfers != null ? !acceptTransfers.equals(that.acceptTransfers) : that.acceptTransfers != null) {
            return false;
        }
        if (balanceInfoIsValid != null ? !balanceInfoIsValid.equals(that.balanceInfoIsValid)
                : that.balanceInfoIsValid != null) {
            return false;
        }
        if (amtOverdraftLimit != null ? !amtOverdraftLimit.equals(that.amtOverdraftLimit)
                : that.amtOverdraftLimit != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (accGroupType != null ? !accGroupType.equals(that.accGroupType) : that.accGroupType != null) {
            return false;
        }
        if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null) {
            return false;
        }
        if (lifecycleStatus != null ? !lifecycleStatus.equals(that.lifecycleStatus) : that.lifecycleStatus != null) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = amtLedgerBal != null ? amtLedgerBal.hashCode() : 0;
        result = 31 * result + (amtAvailableBal != null ? amtAvailableBal.hashCode() : 0);
        result = 31 * result + (acceptTransfers != null ? acceptTransfers.hashCode() : 0);
        result = 31 * result + (balanceInfoIsValid != null ? balanceInfoIsValid.hashCode() : 0);
        result = 31 * result + (amtOverdraftLimit != null ? amtOverdraftLimit.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (accGroupType != null ? accGroupType.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (lifecycleStatus != null ? lifecycleStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "ArrangementAdditionalDetails {" + "amtLedgerBal='" + amtLedgerBal + '\'' + ", amtAvailableBal='"
                + amtAvailableBal + '\'' + ", balanceInfoIsValid='" + balanceInfoIsValid + '\'' + ", acceptTransfers='"
                + acceptTransfers + '\'' + ", amtOverdraftLimit='" + amtOverdraftLimit + '\'' + ", name='" + name + '\''
                + ", accGroupType='" + accGroupType + '\'' + ", currencyCode='" + currencyCode + '\''
                + ", lifecycleStatus='" + lifecycleStatus + '\'' + '}';

    }

}
