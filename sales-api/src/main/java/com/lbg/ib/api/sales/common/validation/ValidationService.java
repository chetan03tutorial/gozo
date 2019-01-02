package com.lbg.ib.api.sales.common.validation;

import java.lang.reflect.Field;

import com.lbg.ib.api.sales.common.validation.enums.ValidationSectionIdentifier;

public interface ValidationService {

    /**
     * @param field
     * @return
     */
    boolean isOptional(Field field);

    /**
     * @param sectionType
     * @param declaredField
     * @param validationType
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public ValidationSectionIdentifier lookUpMessageId(String sectionType, String declaredField)
            throws IllegalAccessException, NoSuchFieldException;

    public boolean isOptional();
}
