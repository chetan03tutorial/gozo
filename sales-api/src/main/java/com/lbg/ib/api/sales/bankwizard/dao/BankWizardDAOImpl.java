package com.lbg.ib.api.sales.bankwizard.dao;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.bankwizard.domain.BankWizardExtractResponse;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.BankWizardValidateAccountRequestMapper;
import com.lbg.ib.api.sales.dto.bankwizard.BankAccountDetailsRequestDTO;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.ArrangementSetup;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsRequest;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsResponse;
import com.lbg.ib.api.sales.soapapis.bw.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.bw.lcsm.ErrorInfo;
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
@Component
public class BankWizardDAOImpl implements BankWizardDAO {

    @Autowired
    private LoggerDAO                              logger;

    @Autowired
    private BankWizardValidateAccountRequestMapper requestMapper;

    @Autowired
    private BankWizardExtractResponse              bankWizardExtractResponse;

    @Autowired
    private ArrangementSetup                       arrangementSetupService;

    private DAOError validateResponse(VerifyProductArrangementDetailsResponse response) {
        logger.traceLog(this.getClass(), "Validating account deatils response in Dao");
        if (response == null) {
            DAOError error = new DAOError(BUSSINESS_ERROR, "Bank wizard call is failed");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
            return error;
        }
        logger.traceLog(this.getClass(), " End of validating account deatils response in Dao");
        return null;
    }

    @TraceLog
    public DAOResponse<ValidateBankDetailstResponseDTO> validateBankDetails(
            BankAccountDetailsRequestDTO bankAccountDetailsDTO) {
        logger.traceLog(this.getClass(), "Validating Account details in Dao");
        try {
            requestMapper.prepareHeader();
            BapiInformation bapiInformationHeader = requestMapper.getBapiInformationHeader();
            SecurityHeaderType securityHeader = requestMapper.getSecurityHeader();
            ServiceRequest serviceRequestHeader = requestMapper.getServiceRequestHeader();
            ContactPoint contactPointHeader = requestMapper.getContactPointHeader();
            VerifyProductArrangementDetailsRequest verifyProductArrangementDetailsRequest = requestMapper
                    .mapRequestAttribute(bankAccountDetailsDTO);
            VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse = arrangementSetupService
                    .verifyProductArrangementDetails(verifyProductArrangementDetailsRequest, serviceRequestHeader,
                            contactPointHeader, securityHeader, bapiInformationHeader);
            DAOError error = validateResponse(verifyProductArrangementDetailsResponse);
            if (error == null) {
                logger.traceLog(this.getClass(), "End of Validating Account details in Dao");
                return withResult(bankWizardExtractResponse.extractResponse(verifyProductArrangementDetailsResponse));
            } else {
                return withError(error);
            }
        } catch (Exception e) {
            logger.logException(BankWizardDAOImpl.class, e);
            return withError(new DAOError(REMOTE_EXCEPTION, "Remote connection exception in bank wizard service"));
        }
    }
}
