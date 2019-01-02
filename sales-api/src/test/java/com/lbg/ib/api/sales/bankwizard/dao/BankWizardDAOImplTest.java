package com.lbg.ib.api.sales.bankwizard.dao;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.bankwizard.domain.BankWizardExtractResponse;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.BankWizardValidateAccountRequestMapper;
import com.lbg.ib.api.sales.dto.bankwizard.BankAccountDetailsRequestDTO;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;
import com.lbg.ib.api.sales.soapapis.bw.arrangement.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.bw.arrangement.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.ArrangementSetup;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsRequest;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsResponse;
import com.lbg.ib.api.sales.soapapis.bw.common.AttributeCondition;
import com.lbg.ib.api.sales.soapapis.bw.common.AttributeConditionValue;
import com.lbg.ib.api.sales.soapapis.bw.common.Condition;
import com.lbg.ib.api.sales.soapapis.bw.common.RuleCondition;
import com.lbg.ib.api.sales.soapapis.bw.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.security.SecurityHeaderType;

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
public class BankWizardDAOImplTest {

    @InjectMocks
    BankWizardDAOImpl                      bankWizardDAOImpl;

    @Mock
    LoggerDAO                              logger;

    @Mock
    BankWizardValidateAccountRequestMapper requestMapper;

    @Mock
    ArrangementSetup                       arrangementSetupService;

    @Mock
    BankWizardExtractResponse              bankWizardExtractResponse;

    @Test
    public void shouldReturnSucessResponseIfCorrectRequestIsPopulated() throws Exception {

        BankAccountDetailsRequestDTO bankAccountDetailsDTO = new BankAccountDetailsRequestDTO("accountNo", "sortCode");
        VerifyProductArrangementDetailsRequest verifyProductArrangementDetailsRequest = new VerifyProductArrangementDetailsRequest();

        BapiInformation mockBapiInformationHeader = mock(BapiInformation.class);
        SecurityHeaderType mockSecurityHeader = mock(SecurityHeaderType.class);
        ContactPoint mockContactHeader = mock(ContactPoint.class);
        ServiceRequest mockServiceHeader = mock(ServiceRequest.class);
        VerifyProductArrangementDetailsResponse arrangementDetailsResponse = sampleValidResponseWithWarnings();
        when(requestMapper.mapRequestAttribute(bankAccountDetailsDTO))
                .thenReturn(verifyProductArrangementDetailsRequest);
        when(requestMapper.getBapiInformationHeader()).thenReturn(mockBapiInformationHeader);
        when(requestMapper.getSecurityHeader()).thenReturn(mockSecurityHeader);
        when(requestMapper.getServiceRequestHeader()).thenReturn(mockServiceHeader);
        when(requestMapper.getContactPointHeader()).thenReturn(mockContactHeader);
        when(arrangementSetupService.verifyProductArrangementDetails(verifyProductArrangementDetailsRequest,
                mockServiceHeader, mockContactHeader, mockSecurityHeader, mockBapiInformationHeader))
                        .thenReturn(arrangementDetailsResponse);
        when(bankWizardExtractResponse.extractResponse(arrangementDetailsResponse))
                .thenReturn(new ValidateBankDetailstResponseDTO(true));
        DAOResponse<ValidateBankDetailstResponseDTO> response = bankWizardDAOImpl
                .validateBankDetails(bankAccountDetailsDTO);

        ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO = response.getResult();

        assertThat(validateBankDetailstResponseDTO.getIsValidIndicator(), is(true));
    }

    @Test
    public void shouldReturnFalaseIfThereAreWarnings() throws Exception {

        BankAccountDetailsRequestDTO bankAccountDetailsDTO = new BankAccountDetailsRequestDTO("accountNo", "sortCode");
        VerifyProductArrangementDetailsRequest verifyProductArrangementDetailsRequest = new VerifyProductArrangementDetailsRequest();

        BapiInformation mockBapiInformationHeader = mock(BapiInformation.class);
        SecurityHeaderType mockSecurityHeader = mock(SecurityHeaderType.class);
        ContactPoint mockContactHeader = mock(ContactPoint.class);
        ServiceRequest mockServiceHeader = mock(ServiceRequest.class);
        VerifyProductArrangementDetailsResponse arrangementDetailsResponse = sampleValidResponseWithWarnings();
        when(requestMapper.mapRequestAttribute(bankAccountDetailsDTO))
                .thenReturn(verifyProductArrangementDetailsRequest);
        when(requestMapper.getBapiInformationHeader()).thenReturn(mockBapiInformationHeader);
        when(requestMapper.getSecurityHeader()).thenReturn(mockSecurityHeader);
        when(requestMapper.getServiceRequestHeader()).thenReturn(mockServiceHeader);
        when(requestMapper.getContactPointHeader()).thenReturn(mockContactHeader);
        when(arrangementSetupService.verifyProductArrangementDetails(verifyProductArrangementDetailsRequest,
                mockServiceHeader, mockContactHeader, mockSecurityHeader, mockBapiInformationHeader))
                        .thenReturn(arrangementDetailsResponse);
        when(bankWizardExtractResponse.extractResponse(arrangementDetailsResponse))
                .thenReturn(new ValidateBankDetailstResponseDTO(false));
        DAOResponse<ValidateBankDetailstResponseDTO> response = bankWizardDAOImpl
                .validateBankDetails(bankAccountDetailsDTO);

        ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO = response.getResult();

        assertThat(validateBankDetailstResponseDTO.getIsValidIndicator(), is(false));
    }

    @Test
    public void shouldThrowErrorIfResposneIsNull() throws Exception {
        when(arrangementSetupService.verifyProductArrangementDetails(new VerifyProductArrangementDetailsRequest(), null,
                null, null, null)).thenReturn(null);
        assertEquals(bankWizardDAOImpl.validateBankDetails(new BankAccountDetailsRequestDTO("accountNo", "sortCode"))
                .getError(), new DAOError("813003", "Bank wizard call is failed"));
    }

    @Test
    public void shouldThrowErrorIfBankWizardServiceThrowsException() throws Exception {
        RemoteException remoteException = new RemoteException();
        when(arrangementSetupService.verifyProductArrangementDetails(null, null, null, null, null))
                .thenThrow(remoteException);
        DAOError expectedError = new DAOError("x1", "Remote connection exception in bank wizard service");
        DAOResponse<ValidateBankDetailstResponseDTO> response = bankWizardDAOImpl
                .validateBankDetails(new BankAccountDetailsRequestDTO("accountNo", "sortCode"));
        assertThat(response.getError(), is(expectedError));

    }

    private VerifyProductArrangementDetailsResponse sampleValidResponse() {
        VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse = new VerifyProductArrangementDetailsResponse();
        ProductArrangement[] arrangementToVerify = new ProductArrangement[1];
        RuleCondition ruleCondition = new RuleCondition();
        ruleCondition.setName("CONDITIONS");
        AttributeCondition[] ruleAttributes = new AttributeCondition[1];
        ruleAttributes[0] = new AttributeCondition();
        ruleCondition.setRuleAttributes(ruleAttributes);
        DepositArrangement depositArrangement = new DepositArrangement();
        Condition[] hasObjectConditions = new Condition[1];
        hasObjectConditions[0] = ruleCondition;
        depositArrangement.setHasObjectConditions(hasObjectConditions);
        arrangementToVerify[0] = depositArrangement;
        DepositArrangement arrangement = depositArrangement;
        arrangement.setHasObjectConditions(hasObjectConditions);
        verifyProductArrangementDetailsResponse.setVerificationResult(arrangementToVerify);

        return verifyProductArrangementDetailsResponse;
    }

    private VerifyProductArrangementDetailsResponse sampleValidResponseWithWarnings() {
        VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse = sampleValidResponse();
        RuleCondition ruleCondition = (RuleCondition) verifyProductArrangementDetailsResponse.getVerificationResult()[0]
                .getHasObjectConditions(0);
        AttributeConditionValue[] hasAttributeConditionValues = new AttributeConditionValue[1];
        ruleCondition.getRuleAttributes()[0].setHasAttributeConditionValues(hasAttributeConditionValues);
        return verifyProductArrangementDetailsResponse;
    }

}
