/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.conversion.service;

import com.lbg.ib.api.sales.conversion.domain.AccountConversionRequest;
import com.lbg.ib.api.sales.conversion.domain.AccountConversionResponse;

/**
 * Interface for product conversion.
 * @author cshar8
 */
public interface ConversionService {
    /**
     * API to convert product of account.
     * @param userRequest
     * @return
     */
    public AccountConversionResponse convert(AccountConversionRequest userRequest, String idType);
}
