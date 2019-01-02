/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: SubmitCaseDelegateImpl
 * Author(s):8768724
 * Date: 22 Jan 2016
 *********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docupload.constants.ConfigSectionNameConstants;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.delegate.UpdateCaseDelegate;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UpdateCaseRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UpdateCaseResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lloydstsb.ea.config.ConfigurationService;

/**
 * @author 8735182
 *
 */
@Component
public class UpdateCaseDelegateImpl implements UpdateCaseDelegate {

    private URL                      updateCaseEndPoint;

    private LoggerDAO                logger;

    private ExternalRestApiClientDAO externalRestApiClientDAO;

    private ResponseErrorCodeMapper  responseErrorCodeMapper;

    private ConfigurationService     configurationService;

    /**
     * @param submitCaseEndPoint
     * @param logger
     * @param externalRestApiClientDAO
     * @param responseErrorCodeMapper
     * @param configurationService
     */
    @Autowired
    public UpdateCaseDelegateImpl(URL updateCaseEndPoint, LoggerDAO logger,
            ExternalRestApiClientDAO externalRestApiClientDAO, ResponseErrorCodeMapper responseErrorCodeMapper,
            ConfigurationService configurationService) {
        this.updateCaseEndPoint = updateCaseEndPoint;
        this.logger = logger;
        this.externalRestApiClientDAO = externalRestApiClientDAO;
        this.responseErrorCodeMapper = responseErrorCodeMapper;
        this.configurationService = configurationService;
    }

    public CaseDTO updateCaseDetails(CaseDTO caseDto, String operatorType) throws DocUploadServiceException {
        UpdateCaseRequestDTO updateCaseRequestDTO = new UpdateCaseRequestDTO();
        List<CaseDTO> caselist = new ArrayList<CaseDTO>();
        caselist.add(caseDto);
        updateCaseRequestDTO.setCaseDetails(caselist);
        updateCaseRequestDTO.setOperationType(configurationService
                .getConfigurationValueAsString(ConfigSectionNameConstants.UPDATE_OPERATION_TYPE, operatorType));

        UpdateCaseResponseDTO caseResponse = null;
        try {
            caseResponse = externalRestApiClientDAO.post(updateCaseEndPoint.toString(), UpdateCaseResponseDTO.class,
                    updateCaseRequestDTO, Boolean.TRUE);
            if (caseResponse == null || caseResponse.getCaseDetails() == null
                    && !DocUploadConstant.SUCCESS.equalsIgnoreCase(caseResponse.getServiceResponse())) {
                throw new DocUploadServiceException(responseErrorCodeMapper
                        .resolve(ResponseErrorConstants.INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE));
            }
        } catch (ClassCastException castException) {
            logger.logException(this.getClass(), castException);
            throw new DocUploadServiceException(
                    responseErrorCodeMapper.resolve(ResponseErrorConstants.INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE));
        } catch (IllegalArgumentException exception) {
            logger.logException(this.getClass(), exception);
            throw new DocUploadServiceException(
                    responseErrorCodeMapper.resolve(ResponseErrorConstants.INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE));
        }

        return caseResponse.getCaseDetails();
    }

}
