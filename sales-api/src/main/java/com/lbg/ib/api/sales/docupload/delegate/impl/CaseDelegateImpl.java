package com.lbg.ib.api.sales.docupload.delegate.impl;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.delegate.CaseDelegate;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CreateCaseRequestDTO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaseDelegateImpl implements CaseDelegate {

    @Autowired
    private UriResolver              uriResolver;
    @Autowired
    private LoggerDAO                logger;
    @Autowired
    private ExternalRestApiClientDAO externalRestApiClientDAO;
    @Autowired
    private ResponseErrorCodeMapper  responseErrorCodeMapper;

    public CaseDTO createCase(CreateCaseRequestDTO createCaseRequestDTO) throws DocUploadServiceException {
        CaseDTO response = null;
        try {

            CaseResponseDTO caseResponseDTO = externalRestApiClientDAO.post(
                    uriResolver.getEndpoint(SalsaEndpoints.CREATE_CASE_ENDPOINT), CaseResponseDTO.class,
                    createCaseRequestDTO, Boolean.TRUE);
            // to do need to check the success text from salsa
            if (caseResponseDTO != null
                    && DocUploadConstant.SUCCESS.equalsIgnoreCase((caseResponseDTO.getServiceResponse()))) {
                response = caseResponseDTO.getCaseDetails().get(DocUploadConstant.ZERO);
            } else {
                throw new DocUploadServiceException(responseErrorCodeMapper
                        .resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE));
            }
        } catch (IllegalArgumentException exception) {
            logger.logException(this.getClass(), exception);
            throw new DocUploadServiceException(
                    responseErrorCodeMapper.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE));
        }
        return response;
    }

   /* public CaseDTO updateCaseDetails(UpdateCaseRequestDTO updateCaseRequestDTO) throws DocUploadServiceException {
        // DO NOT DELETE THIS COMMENTED CODE

         * UpdateCaseRequestDTO updateCaseRequestDTO = new
         * UpdateCaseRequestDTO(); List<CaseDTO> caselist = new
         * ArrayList<CaseDTO>(); caselist.add(caseDto);
         * updateCaseRequestDTO.setCaseDetails(caselist);
         * updateCaseRequestDTO.setOperationType(configurationService.
         * getConfigurationValueAsString(
         * ConfigSectionNameConstants.UPDATE_OPERATION_TYPE, operatorType));


        UpdateCaseResponseDTO caseResponse = null;
        try {
            caseResponse = externalRestApiClientDAO.post(
                    uriResolver.getEndpoint(SalsaEndpoints.UPDATE_CASE_ENDPOINT).toString(),
                    UpdateCaseResponseDTO.class, updateCaseRequestDTO, Boolean.TRUE);
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
    }*/

}
