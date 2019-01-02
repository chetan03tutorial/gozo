package com.lbg.ib.api.sales.common.error;

public class ResponseErrorFromOcis {
    
    private String[] errMsg;
    private String status;

    public ResponseErrorFromOcis() {
        /* json binder needs this */}

    public ResponseErrorFromOcis(String[] errMsg, String status) {
        this.status = status;
        this.errMsg = errMsg;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        if (null != errMsg && errMsg.length > 0) {
            return errMsg[0];
        }
        return null;
    }
    
    public String getErrMsg() {
        if (null != errMsg && errMsg.length > 0) {
            return errMsg[0];
        }
        return null;
    }

    @Override
    public String toString() {
        return "ResponseErrorFromOcis{" + "status='" + status + '\'' + ", message='" + errMsg + '\'' + '}';
    }
}
