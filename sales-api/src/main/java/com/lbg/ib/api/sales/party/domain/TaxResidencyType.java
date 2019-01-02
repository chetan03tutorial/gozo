/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.domain;

import org.apache.commons.lang.StringUtils;

public enum TaxResidencyType {
    FATCA("1"), CRS("4");

    private final String taxResidencyCode;

    TaxResidencyType(String taxResidencyCode) {
        this.taxResidencyCode = taxResidencyCode;
    }

    public String getTaxResidencyCode() {
        return taxResidencyCode;
    }

    public static TaxResidencyType findTaxResidencyTypeFromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (TaxResidencyType taxResidencyType : values()) {
            if (taxResidencyType.getTaxResidencyCode().equals(code)) {
                return taxResidencyType;
            }
        }
        return null;
    }
}
