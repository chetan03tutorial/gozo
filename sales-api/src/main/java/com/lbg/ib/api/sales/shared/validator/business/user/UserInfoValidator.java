package com.lbg.ib.api.sales.shared.validator.business.user;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.shared.validator.NumberUtils;
import com.lbg.ib.api.sales.shared.validator.StringValidator;

public class UserInfoValidator {

    public static final String PARTY_ID_REGEX = "\\+[0-9]+";
    public static final String DETAILS_NOT_FOUND_IN_SESSION = "99000091";

    private UserInfoValidator() {

    }

    public static BigInteger validateOcisId(String intVarStr) {
        return validateBigInteger(intVarStr, "ocisId");
    }

    public static void validatePartyId(String value) {
        validateFieldPattern(value.trim(), PARTY_ID_REGEX, "partyId");
    }

    public static boolean validateFieldPattern(String value, String pattern, String fieldName) {
        boolean isValid = StringValidator.validateFieldPattern(pattern, value);
        if (!isValid) {
            throw new InvalidFormatException(String.format("Invalid Format for %s", fieldName));
        }
        return isValid;
    }

    public static BigInteger validateBigInteger(String intVarStr, String fieldName) {
        BigInteger parsedValue = NumberUtils.parseBigInteger(intVarStr);
        if (parsedValue == null) {
            throw new InvalidFormatException(String.format("Invalid Format for %s", fieldName));
        }
        return parsedValue;
    }

    public static void validateUserSession(UserContext context) {
        if (context == null) {
            ResponseError responseError = new ResponseError(DETAILS_NOT_FOUND_IN_SESSION,
                    "UserContext details are not found in session");
            throw new ServiceException(responseError);
        }
    }

    public static void validateJointAccountReq(Account account) {
        if (StringUtils.isEmpty(account.getAccountNumber()) || StringUtils.isEmpty(account.getSortCode())) {
            ResponseError responseError = new ResponseError(DETAILS_NOT_FOUND_IN_SESSION,
                    "Invalid Account/Sort Code");
            throw new ServiceException(responseError);
        }
    }

}
