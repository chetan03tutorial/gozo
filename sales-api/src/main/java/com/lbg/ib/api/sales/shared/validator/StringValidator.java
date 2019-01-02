/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.shared.validator;

import org.apache.commons.lang.StringUtils;

/**
 * String validator.
 */

public class StringValidator {

    private StringValidator() {

    }

    /**
     * Method to validate the fields.
     * @param pattern String
     * @param value String
     * @return boolean
     */
    public static boolean validateFieldPattern(String pattern, String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        } else if (!value.matches(pattern)) {
            return false;
        }
        return true;
    }
}
