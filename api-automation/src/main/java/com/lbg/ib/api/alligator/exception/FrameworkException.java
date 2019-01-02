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
package com.lbg.ib.api.alligator.exception;

public class FrameworkException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final ResponseError error;

	public FrameworkException(ResponseError error) {
		this.error = error;
	}

	public ResponseError getResponseError() {
		return error;
	}

	@Override
	public String toString() {
		return "FramworkException{" + "error=" + error.getCode() + " and errorMessage=" + error.getMessage()+'}';
	}
}
