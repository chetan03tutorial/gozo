package com.lbg.ib.api.sales.user.service;

import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sso.domain.user.Account;

import java.util.List;

/**
 * Service for Account details.
 * @author tkhann
 */
public interface AccountPortTypeService {
    /**
     * Method to retrieve Joint Party details.
     * @param account
     * @return PartyDetails
     */
    List<String> retrieveJointPartyOcisIds(Account account);

}
