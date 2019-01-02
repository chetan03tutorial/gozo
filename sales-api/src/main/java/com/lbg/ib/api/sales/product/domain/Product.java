package com.lbg.ib.api.sales.product.domain;

import com.lbg.ib.api.sso.domain.product.Event;
import com.lbg.ib.api.sso.domain.product.Indicator;

import java.util.Calendar;
import java.util.List;

/**
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 * 
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thMarch2016
 */
public class Product {

    private String          accountName;

    private String          accountNumber;

    private String          sortCode;

    private String          productType;

    private Calendar        accountOpenedDate;

    private String          accountStatus;

    private String          accountCategory;

    private String          currencyCode;

    private Double          ledgerBal;

    private Double          availableBal;

    private Boolean         aceptTransfers;

    private Boolean         balanceInfoIsValid;

    private Double          overdraftLimit;

    private String          accountNickName;

    private Boolean         dormant;

    private String          accGroupType;

    private Integer         displayOrder;

    private String          productIdentifier;

    private String          lifecyclestatus;

    private String          externalSystemId;

    private List<Event>     events;

    private List<Indicator> indicators;

    private String          sellingLegalEntity;

    private String          manufacturingLegalEntity;

    private String          accountType;

    public String getManufacturingLegalEntity() {
        return manufacturingLegalEntity;
    }

    public void setManufacturingLegalEntity(String manufacturingLegalEntity) {
        this.manufacturingLegalEntity = manufacturingLegalEntity;
    }

    public String getSellingLegalEntity() {
        return sellingLegalEntity;
    }

    public void setSellingLegalEntity(String sellingLegalEntity) {
        this.sellingLegalEntity = sellingLegalEntity;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Calendar getAccountOpenedDate() {
        return accountOpenedDate;
    }

    public void setAccountOpenedDate(Calendar accountOpenedDate) {
        this.accountOpenedDate = accountOpenedDate;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getLedgerBal() {
        return ledgerBal;
    }

    public void setLedgerBal(Double ledgerBal) {
        this.ledgerBal = ledgerBal;
    }

    public Double getAvailableBal() {
        return availableBal;
    }

    public void setAvailableBal(Double availableBal) {
        this.availableBal = availableBal;
    }

    public Boolean getAceptTransfers() {
        return aceptTransfers;
    }

    public void setAceptTransfers(Boolean aceptTransfers) {
        this.aceptTransfers = aceptTransfers;
    }

    public Boolean getBalanceInfoIsValid() {
        return balanceInfoIsValid;
    }

    public void setBalanceInfoIsValid(Boolean balanceInfoIsValid) {
        this.balanceInfoIsValid = balanceInfoIsValid;
    }

    public Double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(Double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }

    public Boolean getDormant() {
        return dormant;
    }

    public void setDormant(Boolean dormant) {
        this.dormant = dormant;
    }

    public String getAccGroupType() {
        return accGroupType;
    }

    public void setAccGroupType(String accGroupType) {
        this.accGroupType = accGroupType;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getLifecyclestatus() {
        return lifecyclestatus;
    }

    public void setLifecyclestatus(String lifecyclestatus) {
        this.lifecyclestatus = lifecyclestatus;
    }

    public String getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(String externalSystemId) {
        this.externalSystemId = externalSystemId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((sortCode == null) ? 0 : sortCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        /*
         * if (this == obj) { return true; } if (obj == null) { return false; }
         * if (getClass() != obj.getClass()) { return false; }
         */
        Product other = (Product) obj;
        if (accountNumber == null) {
            if (other.accountNumber != null) {
                return false;
            }
        } else if (!accountNumber.equals(other.accountNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product [accountName=" + accountName + ", accountNumber=" + accountNumber + ", sortCode=" + sortCode
                + ", productType=" + productType + ", accountOpenedDate=" + accountOpenedDate + ", accountStatus="
                + accountStatus + ", accountCategory=" + accountCategory + ", currencyCode=" + currencyCode
                + ", ledgerBal=" + ledgerBal + ", availableBal=" + availableBal + ", aceptTransfers=" + aceptTransfers
                + ", balanceInfoIsValid=" + balanceInfoIsValid + ", OverdraftLimit=" + overdraftLimit
                + ", accountNickName=" + accountNickName + ", dormant=" + dormant + ", accGroupType=" + accGroupType
                + ", displayOrder=" + displayOrder + ", events=" + events + ", indicators=" + indicators + "]";
    }

}
