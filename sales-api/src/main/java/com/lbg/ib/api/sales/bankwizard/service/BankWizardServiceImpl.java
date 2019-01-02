package com.lbg.ib.api.sales.bankwizard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.bankwizard.dao.BankWizardDAO;
import com.lbg.ib.api.sales.bankwizard.domain.BankAccountDetails;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.bankwizard.BankAccountDetailsRequestDTO;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponse;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;

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
public class BankWizardServiceImpl implements BankWizardService {

    @Autowired
    private LoggerDAO               logger;

    @Autowired
    private BankWizardDAO           bankWizardDAO;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @TraceLog
    public ValidateBankDetailstResponse validateBankDetails(BankAccountDetails accountDetails) throws ServiceException {
        logger.traceLog(this.getClass(), "Validating account details in service");
        BankAccountDetailsRequestDTO bankAccountDetailsRequestDTO = new BankAccountDetailsRequestDTO(
                accountDetails.getAccountNo(), accountDetails.getSortCode());
        DAOResponse<ValidateBankDetailstResponseDTO> validateBankDetailsResponseDto;

        validateBankDetailsResponseDto = bankWizardDAO.validateBankDetails(bankAccountDetailsRequestDTO);

        if (validateBankDetailsResponseDto.getError() != null) {
            String errorCode = validateBankDetailsResponseDto.getError().getErrorCode();
            String errorMsg = validateBankDetailsResponseDto.getError().getErrorMessage();
            if (errorMsg.contains("Remote")) {
                ValidateBankDetailstResponse validateBankDetailsResponse = new ValidateBankDetailstResponse(true);
                validateBankDetailsResponse.setIsError(true);
                return validateBankDetailsResponse;
            }
            logger.logError(errorCode, errorMsg, this.getClass());
            throw new ServiceException(resolver.resolve(validateBankDetailsResponseDto.getError().getErrorCode()));
        }
        logger.traceLog(this.getClass(), "End of validating account details in service");
        ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO = validateBankDetailsResponseDto.getResult();
        return convertToResponse(validateBankDetailstResponseDTO);
    }


    public ValidateBankDetailstResponse convertToResponse(ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO){
        return new ValidateBankDetailstResponse(validateBankDetailstResponseDTO.getIsValidIndicator(),
                validateBankDetailstResponseDTO.getBankInCASS(),
                validateBankDetailstResponseDTO.getBankName(),
                validateBankDetailstResponseDTO.isIntraBrandSwitching());
    }

}
