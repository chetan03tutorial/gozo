/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: CreateCaseDelegateImplTest
 *
 * Author(s):1146728
 *
 * Date: 15 Feb 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CreateCaseRequestDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lloydstsb.ea.config.ConfigurationService;

/**
 * @author 1146728
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateCaseDelegateImplTest {

    private static final String ENDPOINT = "http://myDevServer.com";

    @InjectMocks
    CreateCaseDelegateImpl      createCaseDelegateImpl;

    @Mock
    ResponseErrorCodeMapper     resolver;

    @Mock
    CaseDTO                     caseDTO;

    @Mock
    ExternalRestApiClientDAO    externalRestApiClientDAO;

    @Mock
    DocUploadServiceException   docUploadServiceException;

    @Mock
    ResponseErrorConstants      responseErrorConstants;

    @Mock
    ConfigurationService        configurationService;

    @Mock
    LoggerDAO                   logger;

    @Before
    public void setUp() throws Exception {
        java.net.URL url = new java.net.URL(ENDPOINT);
        createCaseDelegateImpl = new CreateCaseDelegateImpl(resolver, logger, externalRestApiClientDAO, url);
    }

    @Test
    public void returnExactResponse() {

        CaseResponseDTO caseResponseDTO = new CaseResponseDTO();
        List<CaseDTO> caseList = new ArrayList<CaseDTO>();
        caseList.add(new CaseDTO());
        caseResponseDTO.setCaseDetails(caseList);
        caseResponseDTO.setServiceResponse(DocUploadConstant.SUCCESS);
        Mockito.when(externalRestApiClientDAO.post(Mockito.anyString(), Mockito.eq(CaseResponseDTO.class),
                Mockito.any(CreateCaseRequestDTO.class), Mockito.eq(Boolean.TRUE))).thenReturn(caseResponseDTO);
        createCaseDelegateImpl.createCase(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void returnInvalidResponse() {
        createCaseDelegateImpl.createCase(caseDTO);
        Mockito.when(externalRestApiClientDAO.post(Mockito.anyString(), CaseResponseDTO.class,
                Mockito.any(CreateCaseRequestDTO.class), Boolean.TRUE)).thenReturn(new CaseResponseDTO());
        Mockito.when(resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE))
                .thenReturn(new ResponseErrorVO(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE,
                        "INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE", HttpStatus.SERVICE_UNAVAILABLE.value()));
    }

    @SuppressWarnings({ "unchecked" })
    @Test(expected = DocUploadServiceException.class)
    public void returnDocUploadServiceException() {
        Mockito.when(externalRestApiClientDAO.post(Mockito.anyString(), Mockito.eq(CaseResponseDTO.class),
                Mockito.any(CreateCaseRequestDTO.class), Mockito.eq(Boolean.TRUE)))
                .thenThrow(IllegalArgumentException.class);
        Mockito.when(resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE))
                .thenReturn(new ResponseErrorVO(ResponseErrorConstants.INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE,
                        "INVALID_RESPONSE_FORM_CREATE_CASE_SERVICE", HttpStatus.SERVICE_UNAVAILABLE.value()));
        createCaseDelegateImpl.createCase(caseDTO);
    }

}
