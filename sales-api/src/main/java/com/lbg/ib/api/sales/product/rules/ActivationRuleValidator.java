/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.rules;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.activate.Activation;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;

@Component
public class ActivationRuleValidator {
    private static final int CBS_PRODUCTID_LENGTH = 10;

    public ValidationError validateRules(Activation activation, ArrangeToActivateParameters params) {
        ValidationError error = validateOverDrawnDtlsForAccSwitch(activation);
        if (error != null) {
            return error;
        }
        error = validateDebitCardDtlsForAccSwitch(activation);
        if (error != null) {
            return error;
        }
        error = validateODAmtOpted(activation, params);
        if (error != null) {
            return error;
        }
        error = validateTextAlertDtlsForAccSwitch(activation);
        if (error != null) {
            return error;
        }
        error = validateVantageOpted(activation, params);
        if (error != null) {
            return error;
        }
        return null;
    }

    public ValidationError validateAddPartyActivationRule(Activation activation) {
        ValidationError error = validateAddPartyActivationRule(activation);
        return error;
    }

    public ValidationError validateOverDrawnDtlsForAccSwitch(Activation activation) {
        /*
         * if (activation.getAccountSwitchingDetails() != null &&
         * activation.getAccountSwitchingDetails().isCanBeOverDrawn() != null &&
         * activation.getAccountSwitchingDetails().isCanBeOverDrawn() &&
         * activation.getAccountSwitchingDetails().getPayOdAmount() == null) {
         * return new ValidationError(
         * "The overdraft amount cannot be null if you are like to be overdrawn"
         * ); }
         */
        if (activation.getAccountSwitchingDetails() != null
                && activation.getAccountSwitchingDetails().isCanBeOverDrawn() != null
                && !activation.getAccountSwitchingDetails().isCanBeOverDrawn()
                && activation.getAccountSwitchingDetails().getPayOdAmount() != null) {
            return new ValidationError("The overdraft amount should be null if you dont want to be overdrawn");
        }
        return null;
    }

    public ValidationError validateDebitCardDtlsForAccSwitch(Activation activation) {
        if ((activation.getAccountSwitchingDetails() != null
                && (activation.getAccountSwitchingDetails().getCardNumber() != null)
                && (activation.getAccountSwitchingDetails().getCardExpiryDate() == null
                        || !(activation.getAccountSwitchingDetails().getCardExpiryDate().length() > 0)))) {
            return new ValidationError("If you entered Card number then card expiry date is mandatory");
        }
        if ((activation.getAccountSwitchingDetails() != null
                && activation.getAccountSwitchingDetails().getCardExpiryDate() != null
                && (activation.getAccountSwitchingDetails().getCardNumber() == null
                        || !(activation.getAccountSwitchingDetails().getCardNumber().length() > 0)))) {
            return new ValidationError("If you entered Card expiry date then card number is mandatory");
        }
        return null;
    }

    public ValidationError validateODAmtOpted(Activation activation, ArrangeToActivateParameters params) {
        if (activation.getOverDraftDetails() != null && activation.getOverDraftDetails().getAmount() != null) {
            if ((params == null || params.getOverdraftIntrestRates() == null
                    || params.getOverdraftIntrestRates().getAmtOverdraft() == null)) {
                return new ValidationError(
                        "Cannot request overdraft amount as session or overdraft amount from offer response is not available");
            }
        }
        BigDecimal odAmtFromResp = null;
        BigDecimal odAmtOpted = null;
        if (activation.getOverDraftDetails() != null && activation.getOverDraftDetails().getAmount() != null) {
            odAmtOpted = activation.getOverDraftDetails().getAmount();
            if (params != null && params.getOverdraftIntrestRates() != null
                    && params.getOverdraftIntrestRates().getAmtOverdraft() != null) {
                odAmtFromResp = params.getOverdraftIntrestRates().getAmtOverdraft();
            }
            if (odAmtOpted!=null && odAmtFromResp!=null && odAmtOpted.intValue()-odAmtFromResp.intValue() > 0) {
                return new ValidationError(
                        "OverDraft amount requested cannot be greater than the overdraft amount offered");
            }
        }
        return null;

    }

    public ValidationError validateTextAlertDtlsForAccSwitch(Activation activation) {
        if (activation.getAccountSwitchingDetails() != null
                && activation.getAccountSwitchingDetails().isTextAlert() != null
                && activation.getAccountSwitchingDetails().isTextAlert()
                && activation.getAccountSwitchingDetails().getMobileNumber() == null) {
            return new ValidationError("Mobile number cannot be null if you  wish to have text alerts");
        }
        if (activation.getAccountSwitchingDetails() != null
                && activation.getAccountSwitchingDetails().isTextAlert() != null
                && !activation.getAccountSwitchingDetails().isTextAlert()
                && activation.getAccountSwitchingDetails().getMobileNumber() != null) {
            return new ValidationError("Mobile number should be null if you dont wish to have text alerts");
        }
        return null;
    }

    /*
     * public ValidationError validateVantageOpted(Activation activation,
     * ArrangeToActivateParameters params) { if (activation.getIsVantageOpted()
     * != null && activation.getIsVantageOpted() && params == null) { return new
     * ValidationError("Cannot opt for vantage as no product has been selected"
     * ); } if ((activation.getIsVantageOpted() != null &&
     * activation.getIsVantageOpted())) {
     *
     * if ((params != null && (params.getVantagePrdIdentifier() == null ||
     * !(params.getVantagePrdIdentifier().length() > 0)))) { return new
     * ValidationError(
     * "Cannot opt for Vantage as the product offered does not have vantage eligibility"
     * ); } } return null; }
     */
    public ValidationError validateVantageOpted(Activation activation, ArrangeToActivateParameters params) {
        if (activation.getIsVantageOpted() != null && activation.getIsVantageOpted() && params == null) {
            return new ValidationError("Cannot opt for vantage as no product has been selected");
        }
        if ((activation.getIsVantageOpted() != null && activation.getIsVantageOpted()) && (params != null
                && ((params.getVantagePrdIdentifier() == null || !(params.getVantagePrdIdentifier().length() > 0))
                        && (params.getAlternateVantagePrdIdentifier() == null
                                || !(params.getAlternateVantagePrdIdentifier().length() > 0))))) {
            return new ValidationError(
                    "Cannot opt for Vantage as the product offered does not have vantage eligibility");
        }
        return null;
    }

    /**
     * Validate account details for add party
     *
     * @param activation
     * @param arrangement
     * @return {@link ValidationError}
     */
    public ValidationError validateAccountDetailsForAddParty(Activation activation, Arrangement arrangement) {
        ValidationError error = null;
        // check if account and product details are present in the request
        if (activation.getAccountNumber() == null || activation.getCbsProductId() == null
                || activation.getSortCode() == null || activation.getSellerLegalEntity() == null) {
            error = new ValidationError("Any of AccountNumber, CbsProductId, SortCode, SellerLegalEntity is missing ");
        }

        // validate if the account & product details in the request are matching
        // with
        // one of the accounts in session
        if (error == null) {
        	if (arrangement != null) {
	            List<Account> accounts = arrangement.getAccounts();
	            boolean isMatched = false;
	            if (CollectionUtils.isNotEmpty(accounts)) {
	                for (Account account : accounts) {
	                    isMatched = compareActivationRequestWithAccountDetails(activation, account);
	                    if (isMatched) {
	                        break;
	                    }
	                }
	            }
	            if (!isMatched) {
	                error = new ValidationError("Account details doesn't match with any of the accounts in session.");
	            }
        	} else {
        		error = new ValidationError("Arrangement details are not present in the session.");
        	}
        }
        return error;
    }

    private boolean compareActivationRequestWithAccountDetails(Activation activation, Account account) {
        boolean isAccountMatched = false;
        if (account != null) {
            if (StringUtils.equalsIgnoreCase(account.getAccountNumber(), activation.getAccountNumber())) {
                if (StringUtils.equalsIgnoreCase(account.getSortCode(), activation.getSortCode())) {
                    isAccountMatched = compareCbsProductId(activation.getCbsProductId(), account.getAccountType());
                }
            }
        }
        return isAccountMatched;
    }

    private boolean compareCbsProductId(String cbsProductId, String accountTypeFromAccountInfo) {
        String cbsProductIdFromAccount = "";
        int accountTypeLength = accountTypeFromAccountInfo.length();
        if (accountTypeLength == CBS_PRODUCTID_LENGTH) {
            cbsProductIdFromAccount = accountTypeFromAccountInfo;
        } else if (accountTypeLength > CBS_PRODUCTID_LENGTH) {
            cbsProductIdFromAccount = accountTypeFromAccountInfo.substring(accountTypeLength - CBS_PRODUCTID_LENGTH);
        }

        return StringUtils.equalsIgnoreCase(cbsProductId, cbsProductIdFromAccount);
    }
}
