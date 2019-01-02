package com.lbg.ib.api.sales.paperless.dto;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.PAPERLESS_OR_PAPER;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;

import java.util.Calendar;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class Account {

    private String status;
    private String name;
    private String type;
    private Calendar openingDate;
    private String category;
    private String brand;
    @StringFieldValidation(pattern = PAPERLESS_OR_PAPER)
    private String statementType;
    @StringFieldValidation(pattern = PAPERLESS_OR_PAPER)
    private String correspondanceType;
    private String greenStatus;
    @StringFieldValidation(pattern = REQUIRED_NUMERIC)
    private String accountNumber;
    @StringFieldValidation(pattern = REQUIRED_NUMERIC)
    private String sortCode;
    private String host;
    private String productType;
    @StringFieldValidation(pattern = REQUIRED_NUMERIC)
    private String cardNumber;
    @RequiredFieldValidation
    private String externalSystemProductId;
    @RequiredFieldValidation
    private int externalSystem;
    @RequiredFieldValidation
    private String externalSystemProductHeldId;
    @RequiredFieldValidation
    private String externalPartyIdentifierText;

    public String getCorrespondanceType() {
        return correspondanceType;
    }

    public void setCorrespondanceType(String correspondanceType) {
        this.correspondanceType = correspondanceType;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Calendar getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Calendar openingDate) {
        this.openingDate = openingDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGreenStatus() {
        return greenStatus;
    }

    public void setGreenStatus(String greenStatus) {
        this.greenStatus = greenStatus;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExternalSystemProductId() {
        return externalSystemProductId;
    }

    public void setExternalSystemProductId(String externalSystemProductId) {
        this.externalSystemProductId = externalSystemProductId;
    }

    public String getExternalSystemProductHeldId() {
        return externalSystemProductHeldId;
    }

    public void setExternalSystemProductHeldId(String externalSystemProductHeldId) {
        this.externalSystemProductHeldId = externalSystemProductHeldId;
    }

    public int getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(int externalSystem) {
        this.externalSystem = externalSystem;
    }

    /**
     * @return the externalPartyIdentifierText
     */
    public String getExternalPartyIdentifierText() {
        return externalPartyIdentifierText;
    }

    /**
     * @param externalPartyIdentifierText the externalPartyIdentifierText to set
     */
    public void setExternalPartyIdentifierText(String externalPartyIdentifierText) {
        this.externalPartyIdentifierText = externalPartyIdentifierText;
    }

    @Override
    public String toString() {
        return "UserPreference account number=" + accountNumber + ", statementType=" + statementType
                + ", correspondanceType=" + correspondanceType;
    }
}