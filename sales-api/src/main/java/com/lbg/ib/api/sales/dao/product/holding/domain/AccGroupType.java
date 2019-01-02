package com.lbg.ib.api.sales.dao.product.holding.domain;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * 
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/
public enum AccGroupType {

    C("Current"), S("Savings"), M("Monthly Saver"), I("ISA");

    private String value;

    private AccGroupType(final String value) {
        this.value = value;
    }

    /**
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static AccGroupType getByValue(final String value) {
        if (StringUtils.isNotEmpty(value)) {
            for (AccGroupType addressTypeEnum : AccGroupType.values()) {
                if (addressTypeEnum.value.equals(value.trim())) {
                    return addressTypeEnum;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException(value);
    }

    public static class AccGroupValue {

        public static final Map<String, String> ACCOUNT_TYPE_MAP = new HashMap<String, String>();

        private AccGroupValue() {
        }

        static {
            ACCOUNT_TYPE_MAP.put("Current", "1");
        }

        public static String getProductTypeValue(String productType) {
            return ACCOUNT_TYPE_MAP.get(productType);
        }
    }
}
