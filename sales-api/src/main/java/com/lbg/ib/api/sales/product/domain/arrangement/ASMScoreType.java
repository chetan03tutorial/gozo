/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.domain.arrangement;

import org.apache.commons.lang.StringUtils;

public enum ASMScoreType {
    ACCEPT("1"), REFER("2"), DECLINE("3");

    private final String asmScoreCode;

    ASMScoreType(String asmScoreCode) {
        this.asmScoreCode = asmScoreCode;
    }

    public String getAsmScoreCode() {
        return asmScoreCode;
    }

    public static ASMScoreType findASMTypeFromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (ASMScoreType asmScoreType : values()) {
            if (asmScoreType.getAsmScoreCode().equals(code)) {
                return asmScoreType;
            }
        }
        return null;
    }
}
