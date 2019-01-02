package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityResponse;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;

import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface PcaDetermineEligibilityService {
    /**
     * This method is used for determine the product eligibility.
     *
     * @param determineEligibilityRequest
     * @return DetermineEligibilityResponse
     * @throws ServiceException
     */
    PcaDetermineEligibilityResponse determineEligiblity(PcaDetermineEligibilityRequest determineEligibilityRequest,
            String brand) throws ServiceException;

    /**
     * This method is used for determine the product eligibility.
     *
     * @param determineEligibilityRequest
     * @return DetermineEligibilityResponse
     * @throws ServiceException
     */
    PcaDetermineEligibilityResponse determineEligiblitySuitability(
            PcaDetermineEligibilityRequest determineEligibilityRequest) throws ServiceException;

    /**
     * This method is used for determine the product eligibility.
     *
     * @param determineEligibilityRequest
     * @return DetermineEligibilityResponse
     * @throws ServiceException
     */
    DetermineEligibilityResponse determineAuthEligiblity(PcaDetermineEligibilityRequest determineEligibilityRequest)
            throws ServiceException;

    /**
     * This method is used for determine the product eligibility for upgrade/conversion.
     *
     * @param determineEligibilityRequest
     * @param arrangement
     * @return PcaDetermineEligibilityResponse
     * @throws ServiceException
     */
    PcaDetermineEligibilityResponse determineUpgradeEligibility(PcaDetermineEligibilityRequest determineEligibilityRequest,
                                                                Arrangement arrangement, List<Account> accounts, List<String> mnemonics) throws ServiceException;

}
