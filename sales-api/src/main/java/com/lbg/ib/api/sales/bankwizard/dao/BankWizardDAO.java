package com.lbg.ib.api.sales.bankwizard.dao;

import java.rmi.RemoteException;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.bankwizard.BankAccountDetailsRequestDTO;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;
import com.lbg.ib.api.sales.soapapis.bw.lcsm.ErrorInfo;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 29thJuly2016
 ***********************************************************************/
public interface BankWizardDAO {

    DAOResponse<ValidateBankDetailstResponseDTO> validateBankDetails(BankAccountDetailsRequestDTO bankAccountDetailsDTO);

}
