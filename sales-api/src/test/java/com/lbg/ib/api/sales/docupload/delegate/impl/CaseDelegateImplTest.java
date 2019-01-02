
package com.lbg.ib.api.sales.docupload.delegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CreateCaseRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.PartyDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UpdateCaseResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;

@RunWith(MockitoJUnitRunner.class)
public class CaseDelegateImplTest {

    @InjectMocks
    private CaseDelegateImpl         service;

    @Mock
    private LoggerDAO                logger;

    @Mock
    private ResponseErrorCodeMapper  responseErrorCodeMapper;

    @Mock
    private ExternalRestApiClientDAO externalRestApiClientDAO;

    @Mock
    private HttpServletRequest       httpRequest;

    @Mock
    private UriResolver              uriResolver;

    private static final String      ENDPOINT       = "http://myDevServer.com";

    private static final String      VALID_CASE_REF = "123";

    private static final String      SESSION_ID     = "X3562-HUR45-90";

    /*
     * @Test public void testRetrieveCaseWithSuccessfulResponse() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(CaseRequest.class), eq(true)))
     * .thenReturn(validCaseResponseDto()); CaseDTO caseResponse =
     * service.getCase(caseRequest()); assertEquals(VALID_CASE_REF,
     * caseResponse.getCaseReferenceNo()); }
     */

    /*
     * @Test(expected = DocUploadServiceException.class) public void
     * testRetrieveCaseWithFailedResponse() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(SearchCaseRequestDTO.class),
     * eq(true))).thenThrow(docUploadServiceException());
     * service.getCase(caseRequest()); }
     */

    private DocUploadServiceException docUploadServiceException() {
        ResponseErrorVO responseError = new ResponseErrorVO("code", "message", 302);
        DocUploadServiceException docUploadException = new DocUploadServiceException(responseError);
        return docUploadException;
    }

    /*
     * @Test(expected = DocUploadServiceException.class) public void
     * throwClassCastExceptionWhenRetrievingCaseDetails() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(CaseRequest.class), eq(true)))
     * .thenThrow(new ClassCastException()); service.getCase(caseRequest()); }
     */

    /*
     * @Test(expected = DocUploadServiceException.class) public void
     * throwIllegalArgumentExceptionWhenRetrievingCaseDetails() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(SearchCaseRequestDTO.class),
     * eq(true))).thenThrow(new IllegalArgumentException());
     * service.getCase(caseRequest()); }
     */

    @Test(expected = DocUploadServiceException.class)
    public void testCreateCaseWithSuccessfulResponse() {

        when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).thenReturn(ENDPOINT);
        when(externalRestApiClientDAO.post(eq(ENDPOINT), eq(CaseResponseDTO.class), any(CreateCaseRequestDTO.class),
                eq(true))).thenReturn(validCaseResponseDto());
        CaseDTO caseResponse = service.createCase(createCaseRequest());
        assertEquals(VALID_CASE_REF, caseResponse.getCaseReferenceNo());
    }

    @Test(expected = DocUploadServiceException.class)
    public void throwIllegalArgumentExceptionWhenCreatingCase() {

        when(uriResolver.getEndpoint(SalsaEndpoints.CREATE_CASE_ENDPOINT)).thenReturn(ENDPOINT);
        when(externalRestApiClientDAO.post(eq(ENDPOINT), eq(CaseResponseDTO.class), any(CreateCaseRequestDTO.class),
                eq(true))).thenThrow(new IllegalArgumentException());
        service.createCase(createCaseRequest());
    }

    /*
     * @Test public void testSubmitCaseWithSuccessfulResponse() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.UPDATE_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(UpdateCaseResponseDTO.class), any(UpdateCaseRequestDTO.class),
     * eq(true))).thenReturn(validUpdateCaseResponseDto()); CaseDTO caseResponse
     * = service.submitCaseData(updateCaseRequest());
     * assertNotNull(caseResponse); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * throwIllegalArgumentExceptionWhenSubmittingCase() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.UPDATE_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(UpdateCaseResponseDTO.class), any(UpdateCaseRequestDTO.class),
     * eq(true))).thenThrow(new IllegalArgumentException());
     * service.submitCaseData(updateCaseRequest()); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * throwClassCastExceptionWhenSubmittingCaseDetails() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.UPDATE_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(UpdateCaseResponseDTO.class), any(UpdateCaseRequestDTO.class),
     * eq(true))).thenThrow(new ClassCastException());
     * service.submitCaseData(updateCaseRequest()); }
     */

    /*
     * @Test public void testUpdateCaseWithSuccessfulResponse() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.UPDATE_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(UpdateCaseResponseDTO.class), any(UpdateCaseRequestDTO.class),
     * eq(true))).thenReturn(validUpdateCaseResponseDto()); CaseDTO caseResponse
     * = service.updateCaseDetails(updateCaseRequest());
     * assertEquals(DocUploadConstant.SUCCESS,
     * validUpdateCaseResponseDto().getServiceResponse()); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * throwIllegalArgumentExceptionWhenUpdatingCase() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.UPDATE_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(UpdateCaseResponseDTO.class), any(UpdateCaseRequestDTO.class),
     * eq(true))).thenThrow(new IllegalArgumentException());
     * service.updateCaseDetails(updateCaseRequest()); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * throwClassCastExceptionWhenUpdatingCaseDetails() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.UPDATE_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(UpdateCaseResponseDTO.class), any(UpdateCaseRequestDTO.class),
     * eq(true))).thenThrow(new ClassCastException());
     * service.updateCaseDetails(updateCaseRequest()); }
     * 
     * private UpdateCaseRequestDTO updateCaseRequest() { UpdateCaseRequestDTO
     * updateCaseRequestDTO = new UpdateCaseRequestDTO();
     * updateCaseRequestDTO.setOperationType("SUBMIT"); CaseDTO caseDto = new
     * CaseDTO(); caseDto.setCaseReferenceNo(VALID_CASE_REF);
     * caseDto.setProcessCode("AOOIDV-ON-LTB");
     * caseDto.setCreatorID("Customer"); List<CaseDTO> caseDTOList = new
     * ArrayList<CaseDTO>(); caseDTOList.add(caseDto);
     * updateCaseRequestDTO.setCaseDetails(caseDTOList); return
     * updateCaseRequestDTO; }
     */

    private CaseResponseDTO validCaseResponseDto() {
        CaseDTO caseDto = new CaseDTO();
        PartyDTO partyDto = new PartyDTO();
        List<PartyDTO> partyList = new LinkedList<PartyDTO>();
        partyList.add(partyDto);

        List<UploadDTO> uploadList = new LinkedList<UploadDTO>();
        UploadDTO uploadDto = new UploadDTO();
        uploadDto.setStatus("OP");
        uploadDto.setSrcSessionId(SESSION_ID);
        uploadList.add(uploadDto);

        uploadDto = new UploadDTO();
        uploadDto.setStatus("OP");
        uploadDto.setSrcSessionId("INDIFFERENT" + SESSION_ID);
        uploadList.add(uploadDto);

        uploadDto = new UploadDTO();
        uploadDto.setStatus("CLOSE");
        uploadDto.setSrcSessionId("INDIFFERENT" + SESSION_ID);
        uploadList.add(uploadDto);

        partyDto.setAttachmentDetails(uploadList);
        caseDto.setPartyDetails(partyList);
        CaseResponseDTO caseResponse = new CaseResponseDTO();
        caseDto.setCaseReferenceNo(VALID_CASE_REF);
        List<CaseDTO> caseList = new ArrayList<CaseDTO>();
        caseList.add(caseDto);
        caseResponse.setCaseDetails(caseList);
        caseResponse.setServiceResponse(DocUploadConstant.SUCCESS);
        return caseResponse;
    }

    private UpdateCaseResponseDTO validUpdateCaseResponseDto() {
        CaseDTO caseDto = new CaseDTO();
        PartyDTO partyDto = new PartyDTO();
        List<PartyDTO> partyList = new LinkedList<PartyDTO>();
        partyList.add(partyDto);

        List<UploadDTO> uploadList = new LinkedList<UploadDTO>();
        UploadDTO uploadDto = new UploadDTO();
        uploadDto.setStatus("OP");
        uploadDto.setSrcSessionId(SESSION_ID);
        uploadList.add(uploadDto);

        uploadDto = new UploadDTO();
        uploadDto.setStatus("OP");
        uploadDto.setSrcSessionId("INDIFFERENT" + SESSION_ID);
        uploadList.add(uploadDto);

        uploadDto = new UploadDTO();
        uploadDto.setStatus("CLOSE");
        uploadDto.setSrcSessionId("INDIFFERENT" + SESSION_ID);
        uploadList.add(uploadDto);

        partyDto.setAttachmentDetails(uploadList);
        caseDto.setPartyDetails(partyList);
        UpdateCaseResponseDTO caseResponse = new UpdateCaseResponseDTO();
        caseDto.setCaseReferenceNo(VALID_CASE_REF);

        caseResponse.setCaseDetails(caseDto);
        caseResponse.setServiceResponse(DocUploadConstant.SUCCESS);
        return caseResponse;
    }

    private CaseResponseDTO inValidCaseResponseDto() {
        CaseResponseDTO caseResponse = new CaseResponseDTO();
        caseResponse.setCaseDetails(null);
        caseResponse.setServiceResponse("Unsuccessfull");
        return caseResponse;
    }

    /*
     * private SearchCaseRequestDTO caseRequest() { SearchCaseRequestDTO
     * searchCaseDto = new SearchCaseRequestDTO();
     * searchCaseDto.setCaseId(Integer.parseInt(VALID_CASE_REF));
     * 
     * return searchCaseDto; }
     */

    private CreateCaseRequestDTO createCaseRequest() {
        CreateCaseRequestDTO createCaseDto = new CreateCaseRequestDTO();
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseReferenceNo(VALID_CASE_REF);
        PartyDTO partyDto = new PartyDTO();
        List<PartyDTO> partyList = new LinkedList<PartyDTO>();
        partyList.add(partyDto);

        List<UploadDTO> uploadList = new LinkedList<UploadDTO>();
        UploadDTO uploadDto = new UploadDTO();
        uploadDto.setStatus("OP");
        uploadDto.setSrcSessionId(SESSION_ID);
        uploadList.add(uploadDto);

        uploadDto = new UploadDTO();
        uploadDto.setStatus("OP");
        uploadDto.setSrcSessionId("INDIFFERENT" + SESSION_ID);
        uploadList.add(uploadDto);

        uploadDto = new UploadDTO();
        uploadDto.setStatus("CLOSE");
        uploadDto.setSrcSessionId("INDIFFERENT" + SESSION_ID);
        uploadList.add(uploadDto);

        partyDto.setAttachmentDetails(uploadList);
        caseDto.setPartyDetails(partyList);
        createCaseDto.setCaseDetails(caseDto);

        return createCaseDto;
    }

    /*
     * @Test public void testRetrieveMultipleCaseWithSuccessfulResponse() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(CaseRequest.class), eq(true)))
     * .thenReturn(validCaseResponseDto()); List<CaseDTO> caseResponse =
     * service.searchCase(caseRequest()); Assert.assertNotNull(caseResponse);
     * assertEquals(VALID_CASE_REF, caseResponse.get(0).getCaseReferenceNo()); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * testRetrieveMultipleCaseWithFailedResponse() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(CaseRequest.class), eq(true)))
     * .thenReturn(inValidCaseResponseDto()); service.searchCase(caseRequest());
     * 
     * }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * throwClassCastExceptionWhenRetrievingMultipleCaseDetails() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(CaseRequest.class), eq(true)))
     * .thenThrow(new ClassCastException()); service.searchCase(caseRequest());
     * }
     */
    /*
     * @Test(expected = DocUploadServiceException.class) public void
     * throwIllegalArgumentExceptionWhenRetrievingMultipleCaseDetails() {
     * 
     * when(uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT)).
     * thenReturn(ENDPOINT); when(externalRestApiClientDAO.post(eq(ENDPOINT),
     * eq(CaseResponseDTO.class), any(CaseRequest.class), eq(true)))
     * .thenThrow(new IllegalArgumentException());
     * service.searchCase(caseRequest()); }
     */

    /*
     * @Test public void testRetrieveCaseDataWithUpload() {
     * 
     * CaseDTO caseDto = new CaseDTO(); CaseResponseDTO caseResponse = new
     * CaseResponseDTO(); PartyDTO partyDTO = new PartyDTO(); List<PartyDTO>
     * partyList = new ArrayList<PartyDTO>(); UploadDTO uploadDTO = new
     * UploadDTO(); uploadDTO.setStatus("OP"); uploadDTO.setSrcSessionId("abc");
     * List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
     * uploadList.add(uploadDTO); partyDTO.setAttachmentDetails(uploadList);
     * partyList.add(partyDTO); caseDto.setCaseReferenceNo(CASEREFNO);
     * caseDto.setPartyDetails(partyList); List<CaseDTO> caseList = new
     * ArrayList<CaseDTO>(); caseList.add(caseDto);
     * caseResponse.setCaseDetails(caseList);
     * caseResponse.setServiceResponse(DocUploadConstant.SUCCESS);
     * Mockito.when(externalRestApiClientDAO.post(Mockito.eq(ENDPOINT),
     * Mockito.eq(CaseResponseDTO.class),
     * Mockito.any(SearchCaseRequestDTO.class),
     * Mockito.eq(true))).thenReturn(caseResponse);
     * Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).
     * thenReturn("abc"); CaseDTO response =
     * getCaseDelegateImpl.retrieveDocUploadCaseData(new
     * SearchCaseRequestDTO()); Assert.assertEquals(CASEREFNO,
     * response.getCaseReferenceNo()); }
     * 
     * @Test public void testRetrieveCaseDataWithStatus() {
     * 
     * CaseDTO caseDto = new CaseDTO(); CaseResponseDTO caseResponse = new
     * CaseResponseDTO();
     * 
     * caseDto.setCaseReferenceNo(CASEREFNO); caseDto.setCaseStatus(STATUS);
     * List<CaseDTO> caseList = new ArrayList<CaseDTO>(); caseList.add(caseDto);
     * caseResponse.setCaseDetails(caseList);
     * caseResponse.setServiceResponse(DocUploadConstant.SUCCESS);
     * Mockito.when(externalRestApiClientDAO.post(Mockito.eq(ENDPOINT),
     * Mockito.eq(CaseResponseDTO.class),
     * Mockito.any(SearchCaseRequestDTO.class),
     * Mockito.eq(true))).thenReturn(caseResponse);
     * Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).
     * thenReturn("abc"); CaseDTO response =
     * getCaseDelegateImpl.retrieveDocUploadCaseData(new
     * SearchCaseRequestDTO()); Assert.assertEquals(CASEREFNO,
     * response.getCaseReferenceNo()); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * nullResponseCaseData() { CaseResponseDTO caseResponse = new
     * CaseResponseDTO(); caseResponse.setCaseDetails(null); CaseDTO caseDto =
     * new CaseDTO(); caseDto.setCaseReferenceNo(CASEREFNO);
     * caseDto.setCaseStatus(STATUS);
     * 
     * Mockito.when(externalRestApiClientDAO.post(Mockito.eq(ENDPOINT),
     * Mockito.eq(CaseResponseDTO.class), Mockito.any(CaseDTO.class),
     * Mockito.eq(true))).thenReturn(caseResponse);
     * getCaseDelegateImpl.retrieveDocUploadCaseData(new
     * SearchCaseRequestDTO()); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * withClassCastingFailureCase() { CaseResponseDTO caseResponse = new
     * CaseResponseDTO(); caseResponse.setCaseDetails(null); CaseDTO caseDto =
     * new CaseDTO(); caseDto.setCaseReferenceNo(CASEREFNO);
     * caseDto.setCaseStatus(STATUS);
     * Mockito.when(externalRestApiClientDAO.post(Mockito.eq(ENDPOINT),
     * Mockito.eq(CaseResponseDTO.class), Mockito.any(CaseDTO.class),
     * Mockito.eq(true))).thenThrow(new ClassCastException());
     * getCaseDelegateImpl.retrieveDocUploadCaseData(new
     * SearchCaseRequestDTO()); }
     * 
     * @Test(expected = DocUploadServiceException.class) public void
     * withClassIlleagalArgumentFailureCase() { CaseResponseDTO caseResponse =
     * new CaseResponseDTO(); caseResponse.setCaseDetails(null);
     * Mockito.when(externalRestApiClientDAO.post(Mockito.eq(ENDPOINT),
     * Mockito.eq(CaseResponseDTO.class), Mockito.any(CaseDTO.class),
     * Mockito.eq(true))).thenThrow(new IllegalArgumentException());
     * getCaseDelegateImpl.retrieveDocUploadCaseData(null); }
     */
}
