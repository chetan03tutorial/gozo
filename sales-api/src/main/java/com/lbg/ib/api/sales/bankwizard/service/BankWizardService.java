package com.lbg.ib.api.sales.bankwizard.service;

import com.lbg.ib.api.sales.bankwizard.domain.BankAccountDetails;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 29thJuly2016
 ***********************************************************************/
public interface BankWizardService {

    ValidateBankDetailstResponse validateBankDetails(BankAccountDetails accountDetails) throws ServiceException;
    
}
