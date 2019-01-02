package com.lbg.ib.api.sales.common.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lbg.ib.api.shared.validation.OptionalFieldValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.validation.enums.ValidationSectionIdentifier;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.user.domain.AddParty;
import com.lbg.ib.api.sso.domain.user.Arrangement;

@Component
public class ValidationServiceImpl implements ValidationService {

    private static final String  UN_AUTH = "unAuth";
    private static final String  BRANCH  = "branch";
    private static final String  AUTH    = "auth";
    private static final String  NEW_PARTYB      = "newPartyB";
    private static final String  EXISTING_PARTYB = "existingPartyB";

    private SessionManagementDAO session;

    @Autowired
    public ValidationServiceImpl(SessionManagementDAO session) {
        this.session = session;
    }

    public boolean isOptional() {
        Arrangement arrangement = session.getUserInfo();
        if (arrangement != null) {
            return true;
        }
        if (session.getAddPartyContext() != null && session.getAddPartyContext().isExistingParty()) {
            return true;
        }
        return false;
    }

    public boolean isOptional(Field field) {

        // check if validation is required for this field
        if (null != field.getAnnotation(OptionalFieldValidation.class)) {
            List<String> currentContexts = new ArrayList<String>();

            OptionalFieldValidation opsValidation = field.getAnnotation(OptionalFieldValidation.class);
            // find the application context.validate if the application is auth
            // or

            // check whats the context for the application
            // check for auth context
            Arrangement arrangement = session.getUserInfo();
            if (arrangement != null || session.getBranchContext() != null) {

                if (arrangement != null) {
                    currentContexts.add(AUTH);
                }

                if (session.getBranchContext() != null) {
                    currentContexts.add(BRANCH);

                }

            } else {
                currentContexts.add(UN_AUTH);

            }

            AddParty addParty = session.getAddPartyContext();
            if (addParty != null) {
                if (addParty.isExistingParty()) {
                    currentContexts.add(EXISTING_PARTYB);
                } else if (addParty.isNewParty()) {
                    currentContexts.add(NEW_PARTYB);
                }
            }
            String[] skipContexts = opsValidation.values();
            for (String context : currentContexts) {
                if (Arrays.asList(skipContexts).contains(context)) {
                    return true;
                }
            }

        }
        return false;

    }

    /**
     * @param sectionType
     * @param declaredField
     * @param validationType
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public ValidationSectionIdentifier lookUpMessageId(String sectionType, String declaredField)
            throws IllegalAccessException, NoSuchFieldException {

        return ValidationSectionIdentifier.getValidationIdentifierBySection(sectionType, declaredField);
    }

}
