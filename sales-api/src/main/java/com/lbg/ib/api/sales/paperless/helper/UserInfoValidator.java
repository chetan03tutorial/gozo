/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.helper;

import java.math.BigInteger;

import org.apache.commons.lang.*;

import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.shared.validator.NumberUtils;
import com.lbg.ib.api.sales.shared.validator.StringValidator;

/**
 * Use to validate the request.
 * @author tkhann
 */
public class UserInfoValidator {

    public static final String BASE_ERROR = "Invalid Format for %s";
    public static final String PARTY_ID_REGEX = "\\+[0-9]+";
    public static final String DETAILS_NOT_FOUND_IN_SESSION = "99000091";

    private UserInfoValidator() {
    }

    public static BigInteger validateOcisId(String intVarStr) {
        return validateBigInteger(intVarStr, "ocisId", true);
    }

    public static void validatePartyId(String value) {
        validateFieldPattern(value, PARTY_ID_REGEX, "partyId");
    }

    /**
     * Regex validate field
     * @param value String
     * @param pattern String
     * @param fieldName String
     * @return boolean
     */
    public static boolean validateFieldPattern(String value, String pattern, String fieldName) {
        boolean isValid = false;
        try {
            if (!StringUtils.isEmpty(value)) {
                isValid = StringValidator.validateFieldPattern(pattern, value.trim());
            }
        } catch (Exception ex) {
            throw new InvalidFormatException(String.format("Invalid Format for %s", fieldName), ex);
        }
        if (!isValid) {
            throw new InvalidFormatException(String.format("Invalid Format for %s", fieldName));
        }
        return isValid;
    }

    /**
     * @param intVarStr String
     * @param fieldName String
     * @param enforceNotNull String
     * @return BigInteger
     * @throws InvalidFormatException
     */
    public static BigInteger validateBigInteger(String intVarStr, String fieldName, boolean enforceNotNull) throws InvalidFormatException {
        BigInteger parsedValue = null;
        try {
            parsedValue = NumberUtils.parseBigInteger(intVarStr);
        } catch (Exception ex) {
            throw new InvalidFormatException(String.format("Invalid Format for %s", fieldName), ex);
        }
        return parsedValue;
    }

    /**
     * Validate User Session.
     * @param context UserContext
     */

    public static void validateUserSession(UserContext context) {
        if (context == null) {
            ResponseError responseError = new ResponseError(DETAILS_NOT_FOUND_IN_SESSION, "UserContext details are not found in session");
            throw new ServiceException(responseError);
        }
    }
}
