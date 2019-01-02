/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.rules;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.validation.ValidationService;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;

@Component
public class ArrangementRuleValidator {

    static final String SELF_EMPLOYED = "001";

    static final String PART_TIME     = "002";

    static final String EMPLOYED      = "003";

    @Autowired
    ValidationService   validationService;

    public ValidationError validateRules(Arrangement arrangement) throws ServiceException {
        PrimaryInvolvedParty primaryInvolvedParty = arrangement.getPrimaryInvolvedParty();
        ValidationError error = validateBuildingNameNumberRule(primaryInvolvedParty);
        if (error != null) {
            return error;
        }
        // Removing the Phone Number

        if (!validationService.isOptional()) {
            error = validatePhoneNumbersRule(primaryInvolvedParty);
            if (error != null) {
                return error;
            }
            error = validateOccupationType(arrangement);
            if (error != null) {
                return error;
            }
        }
        /*
         * error = validateDob(primaryInvolvedParty, new Date()); if (error !=
         * null) { return error; }
         */
        error = validateOverDratftAttributes(primaryInvolvedParty);
        if (error != null) {
            return error;
        }

        error = validateEmploymentStatus(primaryInvolvedParty);
        if (error != null) {
            return error;
        }
        return null;
    }

    static ValidationError validateOccupationType(Arrangement arrangement) {
        String status = arrangement.getPrimaryInvolvedParty().getEmploymentStatus();
        String occupation = arrangement.getPrimaryInvolvedParty().getOccupnType();
        if ((SELF_EMPLOYED.equals(status) || PART_TIME.equals(status) || EMPLOYED.equals(status))
                && empty(occupation)) {
            return new ValidationError("You must specify an occupation type");
        }
        return null;
    }

    public ValidationError validateBuildingNameNumberRule(PrimaryInvolvedParty primaryInvolvedParty) {
        if (primaryInvolvedParty.getCurrentAddress() == null) {
            return null;
        }
        UnstructuredPostalAddress address = primaryInvolvedParty.getCurrentAddress().getUnstructuredAddress();
        if (address != null && StringUtils.isBlank(address.getAddressLine1())
                && StringUtils.isBlank(address.getAddressLine2())) {
            return new ValidationError("Either 'buildingName' or 'buildingNumber' must be specified");
        }
        return null;
    }

    public ValidationError validatePhoneNumbersRule(PrimaryInvolvedParty primaryInvolvedParty) {
        ContactNumber homePhone = primaryInvolvedParty.getHomePhone();
        ContactNumber mobileNumber = primaryInvolvedParty.getMobileNumber();
        if (homePhone == null && mobileNumber == null) {
            return new ValidationError("Either 'homePhone' or 'mobileNumber' must be specified");
        }
        return null;
    }

    public ValidationError validateDob(PrimaryInvolvedParty primaryInvolvedParty, Date current) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(primaryInvolvedParty.getDob());
        instance.add(Calendar.YEAR, 18);
        if (instance.getTime().compareTo(current) > 0) {
            return new ValidationError("The age must be between 18-100");
        }
        instance.setTime(primaryInvolvedParty.getDob());
        instance.add(Calendar.YEAR, 100);
        if (instance.getTime().compareTo(current) < 0) {
            return new ValidationError("The age must be between 18-100");
        }
        return null;
    }

    /**
     * This method used to validate the primary involved party
     *
     * @param arrangement
     * @return
     */
    public ValidationError validateOverDratftAttributes(PrimaryInvolvedParty primaryInvolvedParty) {
        if ((primaryInvolvedParty.getIntendOverDraft() && primaryInvolvedParty.getNumberOfDependents() == null)) {
            return new ValidationError(
                    "If overdraft preference is selected then number of dependents must be specified");
        }
        return null;
    }

    /**
     * This method used to validate the Occupation Type of primary involved
     * party 8711247 Pca-reeng-api-service
     *
     * @param arrangement
     * @return
     */
    public ValidationError validateOccupationType(PrimaryInvolvedParty primaryInvolvedParty) {

        String occuType = primaryInvolvedParty.getOccupnType();
        String empStatus = primaryInvolvedParty.getEmploymentStatus();
        if (occuType != null) {
            if ("000".equals(empStatus) || "006".equals(empStatus)) {
                return new ValidationError(

                        "If you are not in a job, then Occupation Type should be null");

            } else {
                return null;
            }

        } else {
            if ("000".equals(empStatus) || "006".equals(empStatus)) {
                return null;
            } else {

                return new ValidationError(

                        "If you are in a job, then Occupation Type should be provided");

            }
        }

    }

    /**
     * This method used to validate the employer of primary involved party
     *
     * @param primary
     *            involved party
     * @return
     */
    public ValidationError validateEmploymentStatus(PrimaryInvolvedParty primaryInvolvedParty) {
        if ((primaryInvolvedParty.getEmploymentStatus().matches("001|002|003")
                && primaryInvolvedParty.getEmployer() == null)) {
            return new ValidationError("If you are in a job, then Employer Field should not be null");
        } else if ((!primaryInvolvedParty.getEmploymentStatus().matches("001|002|003")
                && primaryInvolvedParty.getEmployer() != null)) {
            return new ValidationError("If you are not in job, then Employer Field should be null");
        }
        return null;
    }

    private static boolean empty(String value) {
        return value == null || "".equals(value);
    }

}
