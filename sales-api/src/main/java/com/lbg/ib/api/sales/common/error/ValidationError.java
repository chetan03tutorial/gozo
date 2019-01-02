package com.lbg.ib.api.sales.common.error;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ValidationError {
    private final String message;

    public ValidationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ValidationError{" + "message='" + message + '\'' + '}';
    }
}
