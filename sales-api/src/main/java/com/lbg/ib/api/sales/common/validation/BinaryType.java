/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.common.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum BinaryType {

    Y("Y"), N("N"), YES("YES"), NO("NO"), DONTKNOW("DONTKNOW");

    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryType.class);

    private String              binaryString;

    private BinaryType(String binaryString) {
        this.binaryString = binaryString;
    }

    public String binaryString() {
        return this.binaryString;
    }

    public static boolean validateBinaryString(String stringVal) {

        BinaryType[] binaryStringArr = BinaryType.values();
        for (int i = 0; i < binaryStringArr.length; i++) {
            if (binaryStringArr[i].binaryString.equals(stringVal)) {

                return true;
            }
        }

        return false;
    }

}
