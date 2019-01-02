package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface DetermineEligibilityService {
    /**
     * This method is used for determine the product eligibility.
     *
     * @param determineEligibilityRequest
     * @return DetermineEligibilityResponse
     * @throws ServiceException
     */
    DetermineEligibilityResponse determineEligiblity(DetermineEligibilityRequest determineEligibilityRequest)
            throws ServiceException;
}
