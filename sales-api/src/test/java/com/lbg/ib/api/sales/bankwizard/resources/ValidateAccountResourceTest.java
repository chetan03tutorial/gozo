package com.lbg.ib.api.sales.bankwizard.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.bankwizard.domain.BankAccountDetails;
import com.lbg.ib.api.sales.bankwizard.service.BankWizardService;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

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
public class ValidateAccountResourceTest {

    @Mock
    RequestBodyResolver     resolver;

    @Mock
    GalaxyErrorCodeResolver errorcode;
    @Mock
    BankWizardService       bankWizardService;

    @Mock
    private LoggerDAO       logger = mock(LoggerDAO.class);

    @InjectMocks
    ValidateAccountResource testClass;

    @Test
    public void testValidate() throws Exception {
        // given
        String accountNo = "34232344";
        BankAccountDetails bankAccountDetails = new BankAccountDetails(accountNo, "sortCode");
        ValidateBankDetailstResponse validateBankDetailstResponse = new ValidateBankDetailstResponse(true);
        when(resolver.resolve(any(String.class), eq(BankAccountDetails.class))).thenReturn(bankAccountDetails);
        when(bankWizardService.validateBankDetails(bankAccountDetails)).thenReturn(validateBankDetailstResponse);
        
        // when
        testClass.validate("");

        // then
        verify(bankWizardService).validateBankDetails(eq(bankAccountDetails));
    }

    @Test()
    public void testValidate2() {
        // given
        String accountNo = null;
        BankAccountDetails bankAccountDetails = new BankAccountDetails(accountNo, "sortCode");
        try {
            when(resolver.resolve(any(String.class), eq(BankAccountDetails.class))).thenReturn(bankAccountDetails);
        } catch (InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        when(bankWizardService.validateBankDetails(bankAccountDetails)).thenThrow(InvalidFormatException.class);
        when(errorcode.createResponseError(ResponseErrorConstants.BAD_REQUEST_FORMAT)).thenReturn(new ResponseError());
        // when
        testClass.validate("");

        // then
        // verify(bankWizardService).validateBankDetails(eq(bankAccountDetails));
    }

}
