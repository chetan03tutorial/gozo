package com.lbg.ib.api.sales.common.constant;

public enum RuleConditionParameters {

    DEBIT_CARD_RISK_CODE("100012","DEBIT_CARD_RISK_CODE"),
    DEBIT_CARD_REQUIRED_INDICATOR("100037","DEBIT_CARD_REQUIRED_FLAG"),
    CREDIT_CARD_LIMIT_AMOUNT_FEAT("100038","CREDIT_CARD_LIMIT_AMOUNT"),
    USER_NOTES("100048","USER_NOTES"),
    LINKING_FAILURE("100056","LINKING"), //TODO : Not Used in Create PAM flow
    ALERT_MSGS("100044","ALERT_MSGES"), //TODO : Not Used in Create PAM flow
    INTEND_TO_SWITCH("100057","INTEND_TO_SWITCH"),
    INTEND_TO_OVERDRAFT("100058","INTEND_TO_OVERDRAFT"),
    OVERDRAFT_AMOUNT("100059","OVERDRAFT_AMOUNT"),
    OVERDRAFT_RISK_CODE("100060","OVERDRAFT_RISK_CODE"),
    EXP_MONTHLY_DEPOSIT_AMOUNT("100061","EXP_MONTHLY_DEPOSIT_AMOUNT"),
    SECONDARY_SORT_CODE("100064","SECONDARY_SORT_CODE"),
    SECONDARY_ACCOUNT_NUMBER("100065","SECONDARY_ACCOUNT_NUMBER"),
    SECONDARY_PRODUCT_DETAILS("100093","SECONDARY_PRODUCT_DETAILS"),
    CREDIT_CARD_PROD_FAMILY_CODE("100098","CREDIT_CARD_PROD_FAMILY_CODE");

    private final String key;
    private final String value;

    RuleConditionParameters(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return this.key;
    }
    public String getValue() {
        return this.value;
    }

    public static RuleConditionParameters getApplicationParameters(String key) {
        for (RuleConditionParameters appParameters : values()) {
            if (appParameters.getKey().equalsIgnoreCase(key)) {
                return appParameters;
            }
        }
        return null;
    }

    public static String getApplicationParams(String value) {
        for (RuleConditionParameters appParameters : values()) {
            if (appParameters.getValue().equalsIgnoreCase(value)) {
                return appParameters.getKey();
            }
        }
        return null;
    }
}
