/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.common.exception;

import com.lbg.ib.api.shared.exception.ResponseError;

public abstract class AbstractApiRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResponseError     error;

    public AbstractApiRuntimeException(ResponseError error) {
        this.error = error;
    }

    public ResponseError getResponseError() {
        return error;
    }

    @Override
    public String toString() {
        return "ApiRuntimeException [error=" + error + "]";
    }

}
