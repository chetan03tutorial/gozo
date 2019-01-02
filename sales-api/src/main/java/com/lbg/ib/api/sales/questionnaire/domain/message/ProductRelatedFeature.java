package com.lbg.ib.api.sales.questionnaire.domain.message;

import java.util.HashMap;
import java.util.Map;

import com.lbg.ib.api.sales.common.validation.BinaryStringValidation;
import com.lbg.ib.api.sales.common.validation.BinaryType;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class ProductRelatedFeature {

    static Map<String, String> BINARY_MAP = new HashMap<String, String>();

    static {
        BINARY_MAP.put("Y", "YES");
        BINARY_MAP.put("N", "NO");
        BINARY_MAP.put("DONTKNOW", "DONTKNOW");
        BINARY_MAP.put("YES", "YES");
        BINARY_MAP.put("YES", "NO");
    }
    @RequiredFieldValidation
    private String relatedOptionsIdentifier;

    @RequiredFieldValidation
    @BinaryStringValidation(value = { BinaryType.YES, BinaryType.NO, BinaryType.DONTKNOW })
    private String relatedOptionsValue;

    public String getRelatedOptionsIdentifier() {
        return relatedOptionsIdentifier;
    }

    public void setRelatedOptionsIdentifier(String relatedOptionsIdentifier) {
        this.relatedOptionsIdentifier = relatedOptionsIdentifier;
    }

    public String getRelatedOptionsValue() {
        return relatedOptionsValue;
    }

    public void setRelatedOptionsValue(String relatedOptionsValue) {
        this.relatedOptionsValue = relatedOptionsValue;
    }

    public static String getBinaryValue(String key) {
        return BINARY_MAP.get(key);
    }

}
