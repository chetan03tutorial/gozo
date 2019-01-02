package com.lbg.ib.api.sales.user.service;

import com.lbg.ib.api.sales.user.domain.UserDetails;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.Arrangement;

public interface UserService {
    Arrangement fetchUserInfo(boolean isGoJoint) throws ServiceException;

    /**
     * Fetch user details based on encrypted account details as well.
     * @param accountType
     * @param String
     * @return UserDetails
     * @throws ServiceException
     */
    UserDetails fetchUserInfo(String accountType, String infoRequest, boolean isGoJoint) throws ServiceException;
}
