/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ResponseError
 *
 * Author(s):8735182
 *
 * Date: 30 Oct 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.common.rest.error;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseErrorVO {
    private String code;

    private String message;

    private int    errorStatus;

    public ResponseErrorVO() {
        // Default code
        this.code = ResponseErrorConstants.SERVICE_UNAVAILABLE;
    }

    public ResponseErrorVO(String code, String message, int errorStatus) {
        this.code = code;
        this.message = message;
        this.errorStatus = errorStatus;
    }

    /**
     * @return the errorStatus
     */
    public int getErrorStatus() {
        return errorStatus;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
