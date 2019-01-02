package com.lbg.ib.api.sales.dto.product.suitability;

public enum ProductSuitabilityOptionsEnum {

    NPF1("ApplicantType", "ApplicantType", "equal"),

    NPF2("ApplicantAge", "ApplicantAge", "or"),

    NPF3("HelpToBuyProduct", "HelpToBuyProduct", "equal"),

    NPF4("FundAccessTimePeriod", "FundAccessTimePeriod", "range"),

    NPF5("ISA_AllowanceUsed", "ISA_AllowanceUsed", "equal"),

    NPF6("FundingFreqType", "FundingFreqType", "equal"),

    NPF7("LegalGuardianReq", "LegalGuardianReq", "equal"),

    NPF8("WithdrawalAllowed", "WithdrawalAllowed", "equal");

    private String optionType;

    private String optionName;

    private String predicate;

    ProductSuitabilityOptionsEnum(String optionType, String optionName, String predicate) {
        this.optionType = optionType;
        this.optionName = optionName;
        this.predicate = predicate;
    }

    public String getOptionType() {
        return optionType;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getPredicate() {
        return predicate;
    }

    public static ProductSuitabilityOptionsEnum getProductSuitabilityOption(String type) {

        for (ProductSuitabilityOptionsEnum optionEnum : ProductSuitabilityOptionsEnum.values()) {

            if (optionEnum.getOptionType().equalsIgnoreCase(type)) {
                return optionEnum;
            }

        }

        return null;

    }

}
