package com.lbg.ib.api.sales.common.validation;

import com.lbg.ib.api.shared.validation.FieldValidation;

import java.util.regex.Pattern;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class PasswordFieldValidation implements FieldValidation {

    public static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9]{5,15}$");

    public boolean doesFail(Object obj) {
        if (obj != null && obj instanceof String) {
            String value = (String) obj;
            if (PATTERN.matcher(value).matches()) {
                int alphaCount = 0;
                for (Character c : value.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        alphaCount++;
                    }
                }

                return !(value.length() > alphaCount && alphaCount >= 3);
            }
        }
        return true;
    }

    public String message() {
        return "Password is not in correct format";
    }
}
