/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: DocUploadServiceException
 *
 * Author(s):8735182
 *
 * Date: 30 Oct 2015
 *
 ***********************************************************************/
package com.lbg.ib.api.sales.common.rest.error;

public class DocUploadServiceException extends RuntimeException {
    private static final long     serialVersionUID = 1L;
    private final ResponseErrorVO error;

    public DocUploadServiceException(ResponseErrorVO error) {
        this.error = error;
    }

    public ResponseErrorVO getResponseError() {
        return error;
    }

    @Override
    public String toString() {
        return "DocUploadServiceException{" + "error=" + error + '}';
    }
}
