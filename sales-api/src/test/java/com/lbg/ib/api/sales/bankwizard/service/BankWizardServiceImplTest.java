package com.lbg.ib.api.sales.bankwizard.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.bankwizard.dao.BankWizardDAO;
import com.lbg.ib.api.sales.bankwizard.domain.BankAccountDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.bankwizard.BankAccountDetailsRequestDTO;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;
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
@RunWith(MockitoJUnitRunner.class)
public class BankWizardServiceImplTest {

    @Mock
    LoggerDAO               logger;

    @Mock
    BankWizardDAO           bankWizardDAO;

    @Mock
    GalaxyErrorCodeResolver resolver;

    @InjectMocks
    BankWizardServiceImpl   testClass;

    @Test
    public void testValidateBankDetails() throws Exception {
        // given
        ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO = new ValidateBankDetailstResponseDTO(false);
        BankAccountDetails bankAccountDetails = new BankAccountDetails("accountNo", "sortcode");
        BankAccountDetailsRequestDTO bankAccountDetailsRequestDTO = new BankAccountDetailsRequestDTO("accountNo",
                "sortcode");
        when(bankWizardDAO.validateBankDetails(bankAccountDetailsRequestDTO))
                .thenReturn(withResult((ValidateBankDetailstResponseDTO) validateBankDetailstResponseDTO));
        testClass.validateBankDetails(bankAccountDetails);

        // then
        verify(bankWizardDAO).validateBankDetails(eq(bankAccountDetailsRequestDTO));
    }

    @Test(expected = ServiceException.class)
    public void testBankWizardIfThereIsAnError() throws Exception {
        // given
        BankAccountDetails bankAccountDetails = new BankAccountDetails("accountNo", "sortcode");
        BankAccountDetailsRequestDTO bankAccountDetailsRequestDTO = new BankAccountDetailsRequestDTO("accountNo",
                "sortcode");

        when(bankWizardDAO.validateBankDetails(bankAccountDetailsRequestDTO)).thenReturn(
                DAOResponse.<ValidateBankDetailstResponseDTO> withError(new DAOError("hostErrorCode", "message")));
        // when
        testClass.validateBankDetails(bankAccountDetails);
        // then
    }

    @Test
    public void testWhenRemoteExceptionIsThrownFromBankWizard() throws Exception {
        // given
        BankAccountDetails bankAccountDetails = new BankAccountDetails("accountNo", "sortcode");
        BankAccountDetailsRequestDTO bankAccountDetailsRequestDTO = new BankAccountDetailsRequestDTO("accountNo",
                "sortcode");

        when(bankWizardDAO.validateBankDetails(bankAccountDetailsRequestDTO)).thenReturn(
                DAOResponse.<ValidateBankDetailstResponseDTO> withError(new DAOError("hostErrorCode", "Remote")));
        // when
        ValidateBankDetailstResponse response = testClass.validateBankDetails(bankAccountDetails);
        // then
        Assert.assertTrue(response.getIsError());
        Assert.assertTrue(response.getIsValidIndicator());
    }

}
