package com.lbg.ib.api.sales.common.exception;


import com.lbg.ib.api.shared.exception.ResponseError;

public class PaoServiceException extends RuntimeException {

    private ResponseError error;

    public PaoServiceException(ResponseError error) {
        this.error = error;
    }

    public ResponseError getResponseError() {
        return error;
    }

    @Override
    public String toString() {
        return "ServiceException{" + "error=" + error + '}';
    }
}
