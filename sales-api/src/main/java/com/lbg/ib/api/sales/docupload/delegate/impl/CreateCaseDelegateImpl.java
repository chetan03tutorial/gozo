/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: CreateCaseDelegateImpl
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 04 Jan 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate.impl;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.delegate.CreateCaseDelegate;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CreateCaseRequestDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 1146728
 *
 */
@Component
public class CreateCaseDelegateImpl implements CreateCaseDelegate {

    private ResponseErrorCodeMapper  resolver;

    private LoggerDAO                logger;

    private ExternalRestApiClientDAO externalRestApiClientDAO;

    private URL                      createCaseEndPoint;

    /**
     * @param resolver
     * @param logger
     * @param externalRestApiClientDAO
     * @param createCaseEndPoint
     */
    @Autowired
    public CreateCaseDelegateImpl(ResponseErrorCodeMapper resolver, LoggerDAO logger,
            ExternalRestApiClientDAO externalRestApiClientDAO, URL createCaseEndPoint) {
        this.resolver = resolver;
        this.logger = logger;
        this.externalRestApiClientDAO = externalRestApiClientDAO;
        this.createCaseEndPoint = createCaseEndPoint;
    }

    public CaseDTO createCase(CaseDTO caseDTO) throws DocUploadServiceException {
        CaseDTO responseCaseDTO = null;
        try {
            CreateCaseRequestDTO createCaseRequestDTO = new CreateCaseRequestDTO();
            createCaseRequestDTO.setCaseDetails(caseDTO);
            CaseResponseDTO caseResponseDTO = externalRestApiClientDAO.post(createCaseEndPoint.toString(),
                    CaseResponseDTO.class, createCaseRequestDTO, Boolean.TRUE);
            // to do need to check the success text from salsa
            if (caseResponseDTO != null
                    && DocUploadConstant.SUCCESS.equalsIgnoreCase((caseResponseDTO.getServiceResponse()))) {
                responseCaseDTO = caseResponseDTO.getCaseDetails().get(DocUploadConstant.ZERO);
            } else {
                throw new DocUploadServiceException(
                        resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE));
            }
        } catch (IllegalArgumentException exception) {
            logger.logException(this.getClass(), exception);
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE));
        }
        return responseCaseDTO;
    }
}
