/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.shared.validator;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;

public class NumberUtils {

    private NumberUtils() {

    }

    public static BigInteger parseBigInteger(String intVarStr) {
        BigInteger intVal = null;
        try {
            intVal = StringUtils.isEmpty(intVarStr) ? null : new BigInteger(intVarStr);
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Unable to parse BigInteger", ex);
        }
        return intVal;
    }

}