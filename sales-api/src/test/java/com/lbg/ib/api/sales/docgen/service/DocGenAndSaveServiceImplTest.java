package com.lbg.ib.api.sales.docgen.service;
/*
Created by Rohit.Soni at 07/06/2018 10:53
*/

import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveResponseParty;
import com.lbg.ib.api.sales.docgen.mapper.CreateCaseMapper;
import com.lbg.ib.api.sales.docgen.mapper.RecordMetaDataContentMapper;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.RecordMetaDataContent;
import com.lbg.ib.api.sales.docmanager.service.DocumentMetaContentService;
import com.lbg.ib.api.sales.docupload.delegate.SearchCaseDelegateService;
import com.lbg.ib.api.sales.docupload.domain.Case;
import com.lbg.ib.api.sales.docupload.domain.Party;
import com.lbg.ib.api.sales.docupload.domain.ServiceResponse;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.PartyDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import com.lbg.ib.api.sales.docupload.mapper.CaseServiceMapper;
import com.lbg.ib.api.sales.docupload.service.CaseDetailsService;
import com.lbg.ib.api.sales.docupload.validator.UploadDocumentValidator;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.http.HttpRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocGenAndSaveServiceImplTest {

    @Mock
    private SessionManagementDAO session;
    /**
     * Logger object.
     */
    @Mock
    private LoggerDAO logger;

    @Mock
    private GenerateDocumentService generateDocumentService;

    @Mock
    private CaseDetailsService caseDetailsService;

    @Mock
    SearchCaseDelegateService searchCaseDelegateService;

    @Mock
    private ResponseErrorCodeMapper errorCodeMapper;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private UploadDocumentValidator uploadDocumentValidator;

    @Mock
    private CaseServiceMapper caseServiceHelper;

    @Mock
    private DocumentMetaContentService documentMetaContentService;

    @Mock
    private GalaxyErrorCodeResolver resolver;

    @Mock
    private RecordMetaDataContentMapper recordMetaDataContentMapper;

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private CreateCaseMapper createCaseMapper;




    @InjectMocks
    private DocGenAndSaveService classUnderTest = new DocGenAndSaveServiceImpl();

    private DocGenAndSaveRequest docGenAndSaveRequest = new DocGenAndSaveRequest();

    private Map<String, PartyDetails> parties = new HashMap<String, PartyDetails>();

    private static String DOCUMENT_AS_STRING = "JVBERi0xLjQKJeLjz9MKMiAwIG9iago8PC9MZW5ndGggMzY2L0ZpbHRlci9GbGF0ZURlY29kZT4+c3RyZWFtCnicrZS9bsMgFIV3PwVjOhCDnTjO0KFVf5YOqWSpIyJw49IYcAyulbcvThM1SV1VarMAMuae7/ges4luiyjNUE4yVMiIIEx3i/iBIpqgYhWN0FXxFjbIyf6oBk1JyspWSWDeMvHKTQmO1XJ1HXe1izuhY2GNAeFjD87XDbwr6JgHrp3ywKrKbmU4AI2zhldsyc1aDWqZMubOgXeHSbYNX1agQapWx49nDHgcIAYLAaHMtVrzZsvsiknQ3ASEMDADIP9GP6h0ZOkX+r46Drq4roA7wA3UFReAK+6hwXRK0zydzLPkR1P7TnRQCavhvw34oh0UG7SwCKzKtJrdCGFb49nLnmXBxZqR9A4Ee9qJYTrJyYwkWZr+5mfXVW8vlCtTDmpdKFd7ZB+CJC7XgDANp/gb6XmFINHoz1wHAKm8sqb/9tksmWdpnh+MnPzb90X0HG36+4Ci/hFFyZyM6QxNUaGPLwRMcX9s9/4HxvZo8AplbmRzdHJlYW0KZW5kb2JqCjQgMCBvYmoKPDwvUGFyZW50IDMgMCBSL0NvbnRlbnRzIDIgMCBSL1R5cGUvUGFnZS9SZXNvdXJjZXM8PC9Gb250PDwvRjEgMSAwIFI+Pj4+L01lZGlhQm94WzAgMCA1OTUgODQyXT4+CmVuZG9iagoxIDAgb2JqCjw8L0Jhc2VGb250L0hlbHZldGljYS9UeXBlL0ZvbnQvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL1N1YnR5cGUvVHlwZTE+PgplbmRvYmoKMyAwIG9iago8PC9UeXBlL1BhZ2VzL0NvdW50IDEvS2lkc1s0IDAgUl0+PgplbmRvYmoKNSAwIG9iago8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMyAwIFI+PgplbmRvYmoKNiAwIG9iago8PC9Qcm9kdWNlcihpVGV4dK4gNS41LjggqTIwMDAtMjAxNSBpVGV4dCBHcm91cCBOViBcKEFHUEwtdmVyc2lvblwpKS9Nb2REYXRlKEQ6MjAxODA1MDgxMjUxNTlaKS9DcmVhdGlvbkRhdGUoRDoyMDE4MDUwODEyNTE1OVopPj4KZW5kb2JqCnhyZWYKMCA3CjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwMDU2MCAwMDAwMCBuIAowMDAwMDAwMDE1IDAwMDAwIG4gCjAwMDAwMDA2NDggMDAwMDAgbiAKMDAwMDAwMDQ0OCAwMDAwMCBuIAowMDAwMDAwNjk5IDAwMDAwIG4gCjAwMDAwMDA3NDQgMDAwMDAgbiAKdHJhaWxlcgo8PC9Sb290IDUgMCBSL0lEIFs8ZDdmY2YzOWMwOWNlYzBlZDY0ZjQ3NzExMmYyMmE1ZjI+PGQ3ZmNmMzljMDljZWMwZWQ2NGY0NzcxMTJmMjJhNWYyPl0vSW5mbyA2IDAgUi9TaXplIDc+PgolaVRleHQtNS41LjgKc3RhcnR4cmVmCjg4OQolJUVPRgo=";

    private static String  GEN_DOC_ERROR = "Error while generating document";
    private static String  CREATE_CASE_ERROR = "Error while creating request for create case";
    private static String  CREATE_GEN_CASE_ERROR =  "Error occured while invoking create case service";
    private static String  UPLOAD_DOC_ERROR = "Error occured while invoking upload document";
    private static String  RECORD_DOC_ERROR = "Error occured while invoking document meta content service";

    private byte[] documentAsByteArray  = DOCUMENT_AS_STRING.getBytes();

    private static String FILE_REF_ID = "XXXXXXXXXX";

    private static int CASE_ID = 1111;

    private  Case caseDetails = new Case();

    private CaseDTO caseDTO = new CaseDTO();

    private ServiceResponse<Case> createCaseResponseSuccess;

    @BeforeClass
    public static void setup(){
        MockitoAnnotations.initMocks(DocGenAndSaveServiceImplTest.class);
    }

    @Before
    public void beforeEachTestSetup(){
        caseDTO.setPartyDetails(createPartiesDTOList());
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.getSession().setAttribute("createdCase", caseDTO);
        when(httpRequest.getSession()).thenReturn(httpServletRequest.getSession());

        caseDetails.setCaseId(CASE_ID);
        caseDetails.setPartyDetails(createPartiesList());

        caseDTO.setPartyDetails(createPartiesDTOList());
        createCaseResponseSuccess = ServiceResponse.withResult(caseDetails);
    }

    private List<PartyDTO> createPartiesDTOList(){
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setTmpSysFileRefNum(FILE_REF_ID);
        List<UploadDTO> attachmentDetails = Arrays.asList(uploadDTO);
        partyDTO.setAttachmentDetails(attachmentDetails);
        List<PartyDTO> partiesList = new ArrayList<PartyDTO>();
        partiesList.add(partyDTO);
        return partiesList;
    }

    private Party addCaseIdToParty(Party party, int casePartyId){
       party.setCasePartyID(casePartyId);
       return party;
    }

    private List<Party> createPartiesList(){
        List<Party> parties = new ArrayList<Party>();
        parties.add(addCaseIdToParty(new Party(),3333));
        parties.add(addCaseIdToParty(new Party(),4444));
        return parties;
    }

    @Test
    public void testGenerateAndSaveDocumentSuccess(){

        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(parties);
        PartyDetails partyDetails1 = new PartyDetails();
        partyDetails1.setIsPrimaryParty(true);
        parties.put("XX", partyDetails1);
        parties.put("YY",new PartyDetails());
        when(generateDocumentService.generateDocument(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(documentAsByteArray);
        when(createCaseMapper.createCaseCreateRequest(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(caseDetails);
        when(caseDetailsService.createCase(anyString())).thenReturn(createCaseResponseSuccess);
        when(caseServiceHelper.mapToCaseDTO(any(Case.class))).thenReturn(caseDTO);
        //Success scenario for validating upload
        when(uploadDocumentValidator.validateUploadDetails(any(CaseDTO.class),anyInt(),any(CaseDTO.class))).thenReturn(true);

        when(caseDetailsService.uploadProcess(any(CaseDTO.class),any(CaseDTO.class),any(InputStream.class),anyInt())).thenReturn(caseDTO);

        RecordMetaDataContent recordMetaDataContent = new RecordMetaDataContent();
        List<CustomerDocumentInfo> customerDocumentInfoList = new ArrayList<CustomerDocumentInfo>();
        customerDocumentInfoList.add(new CustomerDocumentInfo());
        recordMetaDataContent.setCustomerDocumentInfo(customerDocumentInfoList);
        when(recordMetaDataContentMapper.mapRecordCustomerDocumentRequest(anyString(), eq(docGenAndSaveRequest), anyString())).thenReturn(recordMetaDataContent);

        CustomerDocumentInfoResponse customerDocumentInfoResponse = new CustomerDocumentInfoResponse("1111111","Recording successful", true);
        when(documentMetaContentService.recordDocumentMetaContent(any(CustomerDocumentInfo.class))).thenReturn(customerDocumentInfoResponse);
        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);

        ////Asserting that the response has 2 parties
        assertThat(docGenAndSaveResponseParties.size(),is(2));

        assertEquals(true, docGenAndSaveResponseParties.get(0).getDocumentRecordingSuccessful());
        assertEquals(FILE_REF_ID, docGenAndSaveResponseParties.get(0).getFileRefId());

        assertEquals(true, docGenAndSaveResponseParties.get(1).getDocumentRecordingSuccessful());
        assertEquals(FILE_REF_ID, docGenAndSaveResponseParties.get(1).getFileRefId());
    }

    @Test
    public void testGenerateAndSaveDocWhenGenerateDocumentReturnsNull(){
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(parties);
        PartyDetails partyDetails1 = new PartyDetails();
        partyDetails1.setIsPrimaryParty(true);
        parties.put("XX", partyDetails1);
        parties.put("YY",new PartyDetails());
        when(generateDocumentService.generateDocument(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(null);

        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);

        assertThat(docGenAndSaveResponseParties.size(),is(2));

        assertEquals(false, docGenAndSaveResponseParties.get(0).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(0).getFileRefId());
        assertEquals(GEN_DOC_ERROR, docGenAndSaveResponseParties.get(0).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(0).isError());

        assertEquals(false, docGenAndSaveResponseParties.get(1).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(1).getFileRefId());
        assertEquals(GEN_DOC_ERROR, docGenAndSaveResponseParties.get(1).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(1).isError());
    }

    @Test
    public void testGenerateAndSaveDocWhenGenerateDocumentReturnsException(){
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(parties);
        PartyDetails partyDetails1 = new PartyDetails();
        partyDetails1.setIsPrimaryParty(true);
        parties.put("XX", partyDetails1);
        parties.put("YY",new PartyDetails());
        when(generateDocumentService.generateDocument(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenThrow(Exception.class);

        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);

        assertThat(docGenAndSaveResponseParties.size(),is(2));

        assertEquals(false, docGenAndSaveResponseParties.get(0).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(0).getFileRefId());
        assertEquals(GEN_DOC_ERROR, docGenAndSaveResponseParties.get(0).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(0).isError());

        assertEquals(false, docGenAndSaveResponseParties.get(1).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(1).getFileRefId());
        assertEquals(GEN_DOC_ERROR, docGenAndSaveResponseParties.get(1).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(1).isError());

    }

    @Test(expected = ServiceException.class)
    public void testGenAndSaveWithNoPartiesInSession(){
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(null);
        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);
    }

    @Test
    public void testGenAndSaveDocWhenCreateCaseThrowsIOException(){
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(parties);
        PartyDetails partyDetails1 = new PartyDetails();
        partyDetails1.setIsPrimaryParty(true);
        parties.put("XX", partyDetails1);
        parties.put("YY",new PartyDetails());
        when(generateDocumentService.generateDocument(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(documentAsByteArray);
        when(createCaseMapper.createCaseCreateRequest(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(caseDetails);
        when(caseDetailsService.createCase(anyString())).thenThrow(IOException.class);

        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);

        assertThat(docGenAndSaveResponseParties.size(),is(2));

        assertEquals(false, docGenAndSaveResponseParties.get(0).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(0).getFileRefId());
        assertEquals(CREATE_CASE_ERROR, docGenAndSaveResponseParties.get(0).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(0).isError());

        assertEquals(false, docGenAndSaveResponseParties.get(1).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(1).getFileRefId());
        assertEquals(CREATE_CASE_ERROR, docGenAndSaveResponseParties.get(1).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(1).isError());
    }

    @Test
    public void testGenAndSaveDocWhenCreateCaseThrowsException(){
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(parties);
        PartyDetails partyDetails1 = new PartyDetails();
        partyDetails1.setIsPrimaryParty(true);
        parties.put("XX", partyDetails1);
        parties.put("YY",new PartyDetails());
        when(generateDocumentService.generateDocument(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(documentAsByteArray);
        when(createCaseMapper.createCaseCreateRequest(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(caseDetails);
        when(caseDetailsService.createCase(anyString())).thenThrow(Exception.class);

        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);

        assertThat(docGenAndSaveResponseParties.size(),is(2));

        assertEquals(false, docGenAndSaveResponseParties.get(0).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(0).getFileRefId());
        assertEquals(CREATE_GEN_CASE_ERROR, docGenAndSaveResponseParties.get(0).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(0).isError());

        assertEquals(false, docGenAndSaveResponseParties.get(1).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(1).getFileRefId());
        assertEquals(CREATE_GEN_CASE_ERROR, docGenAndSaveResponseParties.get(1).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(1).isError());
    }

    @Test
    public void testGenAndSaveDocWhenUploadDocumentThrowsException(){
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(parties);
        PartyDetails partyDetails1 = new PartyDetails();
        partyDetails1.setIsPrimaryParty(true);
        parties.put("XX", partyDetails1);
        parties.put("YY",new PartyDetails());
        when(generateDocumentService.generateDocument(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(documentAsByteArray);
        when(createCaseMapper.createCaseCreateRequest(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(caseDetails);
        when(caseDetailsService.createCase(anyString())).thenReturn(createCaseResponseSuccess);
        when(caseServiceHelper.mapToCaseDTO(any(Case.class))).thenReturn(caseDTO);
        //Success scenario for validating upload
        when(uploadDocumentValidator.validateUploadDetails(any(CaseDTO.class),anyInt(),any(CaseDTO.class))).thenReturn(true);

        when(caseDetailsService.uploadProcess(any(CaseDTO.class),any(CaseDTO.class),any(InputStream.class),anyInt())).thenThrow(Exception.class);

        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);

        assertThat(docGenAndSaveResponseParties.size(),is(2));

        assertEquals(false, docGenAndSaveResponseParties.get(0).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(0).getFileRefId());
        assertEquals(UPLOAD_DOC_ERROR, docGenAndSaveResponseParties.get(0).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(0).isError());

        assertEquals(false, docGenAndSaveResponseParties.get(1).getDocumentRecordingSuccessful());
        assertEquals(null, docGenAndSaveResponseParties.get(1).getFileRefId());
        assertEquals(UPLOAD_DOC_ERROR, docGenAndSaveResponseParties.get(1).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(1).isError());
    }

    @Test
    public void testGenAndSaveDocWhenRecordDocumentThrowsException(){
        when(sessionManager.getAllPartyDetailsSessionInfo()).thenReturn(parties);
        PartyDetails partyDetails1 = new PartyDetails();
        partyDetails1.setIsPrimaryParty(true);
        parties.put("XX", partyDetails1);
        parties.put("YY",new PartyDetails());
        when(generateDocumentService.generateDocument(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(documentAsByteArray);
        when(createCaseMapper.createCaseCreateRequest(eq(docGenAndSaveRequest),any(PartyDetails.class))).thenReturn(caseDetails);
        when(caseDetailsService.createCase(anyString())).thenReturn(createCaseResponseSuccess);
        when(caseServiceHelper.mapToCaseDTO(any(Case.class))).thenReturn(caseDTO);
        //Success scenario for validating upload
        when(uploadDocumentValidator.validateUploadDetails(any(CaseDTO.class),anyInt(),any(CaseDTO.class))).thenReturn(true);

        when(caseDetailsService.uploadProcess(any(CaseDTO.class),any(CaseDTO.class),any(InputStream.class),anyInt())).thenReturn(caseDTO);

        RecordMetaDataContent recordMetaDataContent = new RecordMetaDataContent();
        List<CustomerDocumentInfo> customerDocumentInfoList = new ArrayList<CustomerDocumentInfo>();
        customerDocumentInfoList.add(new CustomerDocumentInfo());
        recordMetaDataContent.setCustomerDocumentInfo(customerDocumentInfoList);
        when(recordMetaDataContentMapper.mapRecordCustomerDocumentRequest(anyString(), eq(docGenAndSaveRequest), anyString())).thenReturn(recordMetaDataContent);
        when(documentMetaContentService.recordDocumentMetaContent(any(CustomerDocumentInfo.class))).thenThrow(Exception.class);

        List<DocGenAndSaveResponseParty> docGenAndSaveResponseParties = classUnderTest.generateAndSaveDocument(docGenAndSaveRequest);

        assertThat(docGenAndSaveResponseParties.size(),is(2));

        assertEquals(false, docGenAndSaveResponseParties.get(0).getDocumentRecordingSuccessful());
        assertEquals(FILE_REF_ID, docGenAndSaveResponseParties.get(0).getFileRefId());
        assertEquals(RECORD_DOC_ERROR, docGenAndSaveResponseParties.get(0).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(0).isError());

        assertEquals(false, docGenAndSaveResponseParties.get(1).getDocumentRecordingSuccessful());
        assertEquals(FILE_REF_ID, docGenAndSaveResponseParties.get(1).getFileRefId());
        assertEquals(RECORD_DOC_ERROR, docGenAndSaveResponseParties.get(1).getErrorMessage());
        assertEquals(true, docGenAndSaveResponseParties.get(1).isError());
    }



}
