package com.lbg.ib.api.sales.mandate.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidated;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidation;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface ValidateUserIdService {
    UserIdValidated validate(UserIdValidation validation) throws ServiceException;
}
