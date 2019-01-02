/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.common.exception;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.ResponseError;

public class SwitchLoadFailedException extends AbstractApiRuntimeException {

    private static final long   serialVersionUID   = 1L;

    private static final String DEFAULT_ERROR_CODE = ResponseErrorConstants.ERROR_FETCHING_SWITCHES;
    private static final String DEFAULT_ERROR_MSG  = "Unexpected error occured while fetching switches";

    public SwitchLoadFailedException() {
        this(new ResponseError(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG));
    }

    public SwitchLoadFailedException(ResponseError error) {
        super(error);
    }
}
