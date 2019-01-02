
package com.lbg.ib.api.sales.docupload.delegate.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docupload.constants.ConfigSectionNameConstants;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UpdateCaseRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UpdateCaseResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lloydstsb.ea.config.ConfigurationService;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCaseDelegateImplTest {

    private static final String ENDPOINT = "http://myDevServer.com";

    @InjectMocks
    UpdateCaseDelegateImpl      updateCaseDelegateImpl;

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
        updateCaseDelegateImpl = new UpdateCaseDelegateImpl(url, logger, externalRestApiClientDAO, resolver,
                configurationService);
    }

    @Test
    public void returnExactResponse() {

        UpdateCaseResponseDTO caseResponseDTO = new UpdateCaseResponseDTO();
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseStatus(DocUploadConstant.SUCCESS);
        caseResponseDTO.setServiceResponse(DocUploadConstant.SUCCESS);
        caseResponseDTO.setCaseDetails(caseDto);
        caseResponseDTO.setServiceResponse(DocUploadConstant.SUCCESS);

        Mockito.when(externalRestApiClientDAO.post(Mockito.anyString(), Mockito.eq(UpdateCaseResponseDTO.class),
                Mockito.any(UpdateCaseRequestDTO.class), Mockito.eq(Boolean.TRUE))).thenReturn(caseResponseDTO);
        CaseDTO response = updateCaseDelegateImpl.updateCaseDetails(caseDTO,
                ConfigSectionNameConstants.OPERATION_TYPE_PREVIEW);
        Assert.assertEquals(DocUploadConstant.SUCCESS, response.getCaseStatus());
    }

    @SuppressWarnings({ "unchecked" })
    @Test(expected = DocUploadServiceException.class)
    public void testClassCastException() {

        Mockito.when(externalRestApiClientDAO.post(Mockito.anyString(), Mockito.eq(UpdateCaseResponseDTO.class),
                Mockito.any(UpdateCaseRequestDTO.class), Mockito.eq(Boolean.TRUE))).thenThrow(ClassCastException.class);
        Mockito.when(resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE))
                .thenReturn(new ResponseErrorVO(ResponseErrorConstants.INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE,
                        "INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE", HttpStatus.SERVICE_UNAVAILABLE.value()));
        updateCaseDelegateImpl.updateCaseDetails(caseDTO, ConfigSectionNameConstants.OPERATION_TYPE_PREVIEW);
    }

    @SuppressWarnings({ "unchecked" })
    @Test(expected = DocUploadServiceException.class)
    public void testIllegalArgumentException() {
        Mockito.when(externalRestApiClientDAO.post(Mockito.anyString(), Mockito.eq(UpdateCaseResponseDTO.class),
                Mockito.any(UpdateCaseRequestDTO.class), Mockito.eq(Boolean.TRUE)))
                .thenThrow(IllegalArgumentException.class);
        Mockito.when(resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE))
                .thenReturn(new ResponseErrorVO(ResponseErrorConstants.INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE,
                        "INVALID_RESPONSE_FROM_UPDATE_CASE_SERVICE", HttpStatus.SERVICE_UNAVAILABLE.value()));
        updateCaseDelegateImpl.updateCaseDetails(caseDTO, ConfigSectionNameConstants.OPERATION_TYPE_PREVIEW);
    }

    @Test(expected = DocUploadServiceException.class)
    public void nullResponseCaseData() {
        UpdateCaseResponseDTO caseResponse = new UpdateCaseResponseDTO();
        caseResponse.setCaseDetails(null);

        Mockito.when(externalRestApiClientDAO.post(Mockito.eq(ENDPOINT), Mockito.eq(UpdateCaseResponseDTO.class),
                Mockito.any(UpdateCaseRequestDTO.class), Mockito.eq(true))).thenReturn(caseResponse);
        updateCaseDelegateImpl.updateCaseDetails(caseDTO, ConfigSectionNameConstants.OPERATION_TYPE_PREVIEW);
    }

}
