package com.lbg.ib.api.sales.docupload.delegate.impl;

import com.lbg.ib.api.sales.application.service.ApplicationService;

import com.lbg.ib.api.sales.common.exception.PaoServiceException;

import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;

import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchCaseDelegateServiceImplTest {
    
    @InjectMocks
    SearchCaseDelegateServiceImpl searchCaseDelegateServiceImpl;
    
    @Mock
    private LoggerDAO logger;
    
    @Mock
    private ExternalRestApiClientDAO externalRestClientInvoker;
    
    @Mock
    private UriResolver ocisUriResolver;

    @Mock
    ApplicationService applicationService;
    
    @Mock
    private DAOExceptionHandler exceptionHandler;
    
    @Test
    public void testSearchCaseDeleteServiceWithValidCaseReference(){
        CaseResponseDTO caseResponseDTO = new CaseResponseDTO();
        caseResponseDTO.setServiceResponse("Success");
        when(ocisUriResolver.getEndpoint(SalsaEndpoints.SEARCH_CASE_ENDPOINT)).thenReturn("SALSA_URL");
        when(externalRestClientInvoker.customPost(any(RestContext.class), any(Class.class))).thenReturn(caseResponseDTO);
        DAOResponse<CaseResponseDTO> caseResponse = searchCaseDelegateServiceImpl.searchCaseDeleteService("VALID_REQUEST","DUMMY_REQUEST");
        assertTrue("Success".equals(caseResponse.getResult().getServiceResponse()));
    }



    @Test
    public void testSearchCaseDeleteServiceWithInValidCaseReference(){
        CaseResponseDTO caseResponseDTO = new CaseResponseDTO();
        caseResponseDTO.setServiceResponse("Success");
        ResponseError responseError = new ResponseError();
        when(ocisUriResolver.getEndpoint(SalsaEndpoints.SEARCH_CASE_ENDPOINT)).thenReturn("SALSA_URL");
        when(externalRestClientInvoker.customPost(any(RestContext.class), any(Class.class))).thenThrow(new PaoServiceException(responseError));
        DAOResponse<CaseResponseDTO> caseResponse = searchCaseDelegateServiceImpl.searchCaseDeleteService("INVALID_REQUEST","INVALID_REQUEST");
        assertTrue(caseResponse.getResult()==null);
    }
}
