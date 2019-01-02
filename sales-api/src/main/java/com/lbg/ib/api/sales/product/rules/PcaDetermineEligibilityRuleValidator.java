/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;

@Component
public class PcaDetermineEligibilityRuleValidator {

    public ValidationError validateRules(PcaDetermineEligibilityRequest determineEligibilityRequest, boolean isAuth)
            throws ServiceException {
        if (determineEligibilityRequest.getCandidateInstructions() == null) {
            return new ValidationError("Candidate Insturctions are Mandatory");
        }
        return validateRules(determineEligibilityRequest);
    }

    public ValidationError validateRules(PcaDetermineEligibilityRequest determineEligibilityRequest)
            throws ServiceException {
        ValidationError error = null;
        if (null != determineEligibilityRequest.getExistingCustomer()
                && !determineEligibilityRequest.getExistingCustomer()) {
            error = validateDob(determineEligibilityRequest);
        }
        if (error != null) {
            return error;
        }
        return null;
    }

    public ValidationError validateDob(PcaDetermineEligibilityRequest determineEligibilityRequest) {
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            instance.setTime(formatter.parse(determineEligibilityRequest.getDob()));
        } catch (ParseException e1) {
            return new ValidationError("Invalid date format");
        }
        return null;
    }

}
