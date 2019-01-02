package com.lbg.ib.api.sales.docupload.domain;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

public class ServiceResponse<T> {

    private final boolean  success;
    private final T        result;
    private final DAOError error;
    private String         message;
    private String         messageType;

    private ServiceResponse(boolean success, T response, DAOError error, String message, String messageType) {
        this.success = success;
        this.result = response;
        this.error = error;
        this.message = message;
        this.messageType = messageType;
    }

    public static <T> ServiceResponse<T> withResult(T response) {
        return new ServiceResponse<T>(Boolean.TRUE, response, null, null, DocUploadConstant.SUCCESS);
    }

    public static <T> ServiceResponse<T> withResult(T response, String message) {
        return new ServiceResponse<T>(Boolean.TRUE, response, null, message, DocUploadConstant.SUCCESS);
    }

    public static <T> ServiceResponse<T> withError(DocUploadServiceException exception) {
        ResponseErrorVO responseError = exception.getResponseError();
        DAOError serviceError = new DAOError(responseError.getCode(), responseError.getMessage(),
                responseError.getErrorStatus());
        return new ServiceResponse<T>(Boolean.FALSE, null, serviceError, null, DocUploadConstant.FAILURE);
    }

    public static <T> ServiceResponse<T> withError(DocUploadServiceException exception, String message) {
        ResponseErrorVO responseError = exception.getResponseError();
        DAOError serviceError = new DAOError(responseError.getCode(), responseError.getMessage(),
                responseError.getErrorStatus());
        return new ServiceResponse<T>(Boolean.FALSE, null, serviceError, message, DocUploadConstant.FAILURE);
    }

    public boolean getSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public DAOError getError() {
        return error;
    }

    public static class DAOError {

        private final int    status;
        private final String code;
        private final String message;

        public DAOError(String errorCode, String errorMessage, int errorStatus) {
            this.code = errorCode;
            this.message = errorMessage;
            this.status = errorStatus;
        }

        public int getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        /*
         * @Override public boolean equals(Object o) { if (this == o) return
         * true; if (o == null || getClass() != o.getClass()) return false;
         * 
         * DAOError daoError = (DAOError) o;
         * 
         * if (errorCode != null ? !errorCode.equals(daoError.errorCode) :
         * daoError.errorCode != null) return false; return !(errorMessage !=
         * null ? !errorMessage.equals(daoError.errorMessage) :
         * daoError.errorMessage != null);
         * 
         * }
         * 
         * @Override public int hashCode() { int result = errorCode != null ?
         * errorCode.hashCode() : 0; result = 31 * result + (errorMessage !=
         * null ? errorMessage.hashCode() : 0); return result; }
         * 
         * @Override public String toString() { return "DAOError{" +
         * "errorCode='" + errorCode + '\'' + ", errorMessage='" + errorMessage
         * + '\'' + '}'; }
         */
    }

}