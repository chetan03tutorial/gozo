package com.lbg.ib.api.sales.common.validation;

import com.lbg.ib.api.sales.common.error.ValidationError;

public class ValidationException extends Exception {

    private ValidationError validationError;

    public ValidationException(ValidationError validationError) {
        super();
        this.validationError = validationError;
    }

    public ValidationError getValidationError() {
        return validationError;
    }

}
