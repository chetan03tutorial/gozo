/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.domain.activate;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_ALPHA_SPACE;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_NUMERIC;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import com.lbg.ib.api.sales.common.ValidationConstants;
import com.lbg.ib.api.shared.validation.IntegerFieldValidation;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class AccountSwitching {
    @RequiredFieldValidation
    @IntegerFieldValidation(max = ValidationConstants.NUMBER_SIX)
    private String     sortCode;

    @RequiredFieldValidation
    @IntegerFieldValidation(max = ValidationConstants.NUMBER_EIGHT)
    private String     accountNumber;

    @RequiredFieldValidation
    @StringFieldValidation(maxLength = ValidationConstants.NUMBER_SEVENTY, pattern = REQUIRED_ALPHA_SPACE)
    private String     accountName;

    private Boolean    hasDebitCard;

    @IntegerFieldValidation(max = ValidationConstants.NUMBER_SIXTEEN)
    private String     cardNumber;

    @DateTimeFormat(pattern = "mm/yy")
    private String     cardExpiryDate;

    @RequiredFieldValidation
    private Boolean    canBeOverDrawn;

    @IntegerFieldValidation(max = ValidationConstants.NUMBER_TWELVE)
    private BigDecimal payOdAmount;

    @RequiredFieldValidation
    private Boolean    textAlert;

    @StringFieldValidation(pattern = REQUIRED_NUMERIC, maxLength = ValidationConstants.NUMBER_TWELVE)
    private String     mobileNumber;

    private Date       switchingDate;

    private Boolean    consent;

    private Boolean    debitCardIndicator;

    private String     bankName;

    public AccountSwitching() {
        /* jackson */
    }

    public AccountSwitching(String sortCode, String accountNumber, String accountName, Boolean hasDebitCard,
            String cardNumber, String cardExpiryDate, Boolean canBeOverDrawn, BigDecimal payOdAmount, Boolean textAlert,
            String mobileNumber, Date aSwitchingDate, Boolean consent) {
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.hasDebitCard = hasDebitCard;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.canBeOverDrawn = canBeOverDrawn;
        this.payOdAmount = payOdAmount;
        this.textAlert = textAlert;
        this.mobileNumber = mobileNumber;
        this.switchingDate = aSwitchingDate;
        this.consent = consent;
    }

    public String getSortCode() {
        return sortCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public Boolean isHasDebitCard() {
        return hasDebitCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public Boolean isCanBeOverDrawn() {
        return canBeOverDrawn;
    }

    public BigDecimal getPayOdAmount() {
        return payOdAmount;
    }

    public Boolean isTextAlert() {
        return textAlert;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Date getSwitchingDate() {
        return switchingDate;
    }

    public Boolean isConsent() {
        return consent;
    }

    public Boolean getDebitCardIndicator() {
        return debitCardIndicator;
    }

    public void setDebitCardIndicator(Boolean debitCardIndicator) {
        this.debitCardIndicator = debitCardIndicator;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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
        return ReflectionToStringBuilder.toStringExclude(this, "cardNumber", "cardExpiryDate");
    }

}
