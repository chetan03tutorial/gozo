/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name:   CaseServiceValidatorTest 
 *   
 * Author(s): 8735182
 *  
 * Date: 05 Jan 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.validator;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.refdata.DocumentDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.EvidenceTypeDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessPsfDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CasePsfDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.PartyDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocUploadRefDataServiceMapper;

/**
 * @author 8735182
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class CaseServiceValidatorTest {

    public @Rule ExpectedException        exception               = ExpectedException.none();

    private static final String           processCode             = "REMOX-ON-LTB";

    private static final String           wrg_processCode         = "WRONG_PROCESS_CODE";

    private static final String           documentCode            = "BST";

    private static final String           evidenceTypeCode        = "POI";

    private static final String           casePsfName             = "hi";

    private static final String           casePsfValue            = "hello";

    private static final String           errorCode_Upload        = "600051";

    private static final String           upload_ErrorMsg         = "Invalid Upload Details";

    private static final String           errorCode_Party_Details = "600030";

    private static final String           party_Details_Error_Msg = "Invalid Party Details";

    private static final String           errCode_CasePsf         = "600110";

    private static final String           errMsg                  = "Invalid Case referenece Number";

    private static final String           caseRefNo               = "ABC123";

    private static final int              errStatus               = 400;

    @Mock
    private DocUploadRefDataServiceMapper docUploadServiceHelper;

    @Mock
    private ResponseErrorCodeMapper       errorCodeMapper;

    @Mock
    HttpServletRequest                    httpRequest;

    @Mock
    ProcessDTO                            processDTO;

    @Mock
    CaseDTO                               caseDTO;

    @InjectMocks
    private CaseServiceValidator          caseServiceValidator;

    @Test
    public void validateProcessCodeWithJwt() {
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        boolean validCheck = caseServiceValidator.validateProcessCodeWithJwt(processCode);
        Assert.assertEquals(true, validCheck);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validateProcessCodeWithJwtNegativeCase() {
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        caseServiceValidator.validateProcessCodeWithJwt(wrg_processCode);
    }

    @Test
    public void validateCaseDtoWithPartyDtls() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setPartyDetails(new ArrayList<PartyDTO>());
        caseDTO.setProcessCode(processCode);
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        boolean validCheck = caseServiceValidator.validateCaseDtoWithPartyDtls(caseDTO);
        Assert.assertEquals(true, validCheck);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validateCaseDtoWithPartyDtlsNegCaseWrgPrcd() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setPartyDetails(null);
        caseDTO.setProcessCode(wrg_processCode);
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        caseServiceValidator.validateCaseDtoWithPartyDtls(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validateCaseDtoWithPartyDtlsNegCaseNullPartyDtls() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setPartyDetails(null);
        caseServiceValidator.validateCaseDtoWithPartyDtls(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validateCaseDtoWithPartyDtlsNegCaseNullCaseDto() {
        caseServiceValidator.validateCaseDtoWithPartyDtls(null);
    }

    @Test
    public void returnTrueWhileValidateCaseReferenceNumber() {
        String caseRefNo = "123";
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)).thenReturn(caseRefNo);
        Assert.assertTrue(caseServiceValidator.validateCaseReferenceNumber(caseRefNo));

    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldThrowExceptionWithNullCaseRefNo() {

        String caseRefNo = "123";
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)).thenReturn(null);
        Mockito.when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASE_REFERENCE_NUMBER))
                .thenReturn(new ResponseErrorVO(errCode_CasePsf, errMsg, errStatus));
        caseServiceValidator.validateCaseReferenceNumber(caseRefNo);

    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldThrowExceptionWithInvalidCaseRefNo() {

        String caseRefNo = "123";
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)).thenReturn("abc");
        Mockito.when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASE_REFERENCE_NUMBER))
                .thenReturn(new ResponseErrorVO(errCode_CasePsf, errMsg, errStatus));
        caseServiceValidator.validateCaseReferenceNumber(caseRefNo);

    }

    @Test
    public void shouldReturnExpectedResultForIsOnline() {
        Mockito.when(processDTO.getBusinessProcessTypeName()).thenReturn(DocUploadConstant.ONLINE);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        boolean isOnlineTest = caseServiceValidator.isOnline();
        Assert.assertEquals(true, isOnlineTest);
    }

    @Test
    public void shouldReturnFalseResultForIsOnline() {
        Mockito.when(processDTO.getBusinessProcessTypeName()).thenReturn(DocUploadConstant.CUSTOMER);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        boolean isOnlineTest = caseServiceValidator.isOnline();
        Assert.assertEquals(false, isOnlineTest);
    }

    @Test
    public void shouldExpectedResultValidationForCreateCase() {
        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setTitle("Mr.");
        partyDTO.setForeName("GoodMorning");
        partyDTO.setSurName("hello");
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        CasePsfDTO casePsfDTO = new CasePsfDTO();
        casePsfDTO.setCasePSFName(casePsfName);
        casePsfDTO.setCasePSFValue(casePsfValue);
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        Mockito.when(caseDTO.getProcessCode()).thenReturn(processCode);
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        partyDTO.setAttachmentDetails(uploadList);
        List<CasePsfDTO> psfDTO = new ArrayList<CasePsfDTO>();
        psfDTO.add(casePsfDTO);
        Mockito.when(caseDTO.getCasePSF()).thenReturn(psfDTO);
        List<ProcessPsfDTO> processPSFList = new ArrayList<ProcessPsfDTO>();
        Mockito.when(processDTO.getProcessSpecificField()).thenReturn(processPSFList);
        ProcessPsfDTO processPSFDTO = new ProcessPsfDTO();
        processPSFDTO.setFormat(casePsfValue);
        processPSFDTO.setName(casePsfName);
        processPSFDTO.setMandatoryFlag("Y");
        processPSFDTO.setSize(5);
        processPSFList.add(processPSFDTO);
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        EvidenceTypeDTO evidenceDTO = new EvidenceTypeDTO();
        evidenceDTO.setCode(evidenceTypeCode);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setCode(documentCode);
        List<DocumentDTO> documentList = new ArrayList<DocumentDTO>();
        documentList.add(documentDTO);
        evidenceDTO.setDocument(documentList);
        List<EvidenceTypeDTO> evidenceDTOList = new ArrayList<EvidenceTypeDTO>();
        evidenceDTOList.add(evidenceDTO);
        Mockito.when(processDTO.getEvidenceType()).thenReturn(evidenceDTOList);
        Mockito.when(processDTO.getProcessCd()).thenReturn(processCode);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        Mockito.when(caseDTO.getCasePSF()).thenReturn(psfDTO);
        caseServiceValidator.validationForCreateCase(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldUnExpectedResultValidationForCreateCase() {
        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setTitle("Mr.");
        partyDTO.setForeName("GoodMorning");
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        CasePsfDTO casePsfDTO = new CasePsfDTO();
        casePsfDTO.setCasePSFName(casePsfName);
        casePsfDTO.setCasePSFValue(casePsfValue);
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setAttachmentDetails(uploadList);
        List<CasePsfDTO> psfDTO = new ArrayList<CasePsfDTO>();
        psfDTO.add(casePsfDTO);
        Mockito.when(caseDTO.getCasePSF()).thenReturn(psfDTO);
        List<ProcessPsfDTO> processPSFList = new ArrayList<ProcessPsfDTO>();
        Mockito.when(processDTO.getProcessSpecificField()).thenReturn(processPSFList);
        ProcessPsfDTO processPSFDTO = new ProcessPsfDTO();
        processPSFDTO.setFormat(casePsfValue);
        processPSFDTO.setName(casePsfName);
        processPSFList.add(processPSFDTO);
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        EvidenceTypeDTO evidenceDTO = new EvidenceTypeDTO();
        evidenceDTO.setCode(evidenceTypeCode);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setCode(documentCode);
        List<DocumentDTO> documentList = new ArrayList<DocumentDTO>();
        documentList.add(documentDTO);
        evidenceDTO.setDocument(documentList);
        List<EvidenceTypeDTO> evidenceDTOList = new ArrayList<EvidenceTypeDTO>();
        evidenceDTOList.add(evidenceDTO);
        Mockito.when(processDTO.getEvidenceType()).thenReturn(evidenceDTOList);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        caseServiceValidator.validationForCreateCase(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldUnExpectedResultValidationForCreateCaseWhenInvalidProcessSpecificFields() {
        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setTitle("Mr.");
        partyDTO.setForeName("GoodMorning");
        partyDTO.setSurName("surName");
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        CasePsfDTO casePsfDTO = new CasePsfDTO();
        casePsfDTO.setCasePSFName(casePsfName);
        casePsfDTO.setCasePSFValue(casePsfValue);
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setAttachmentDetails(uploadList);
        List<CasePsfDTO> psfDTO = new ArrayList<CasePsfDTO>();
        psfDTO.add(casePsfDTO);
        Mockito.when(caseDTO.getCasePSF()).thenReturn(psfDTO);
        List<ProcessPsfDTO> processPSFList = new ArrayList<ProcessPsfDTO>();
        Mockito.when(processDTO.getProcessSpecificField()).thenReturn(processPSFList);
        ProcessPsfDTO processPSFDTO = new ProcessPsfDTO();
        processPSFDTO.setFormat("fomat");
        processPSFDTO.setName("name");
        processPSFDTO.setMandatoryFlag("Y");
        processPSFList.add(processPSFDTO);
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        EvidenceTypeDTO evidenceDTO = new EvidenceTypeDTO();
        evidenceDTO.setCode(evidenceTypeCode);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setCode(documentCode);
        List<DocumentDTO> documentList = new ArrayList<DocumentDTO>();
        documentList.add(documentDTO);
        evidenceDTO.setDocument(documentList);
        List<EvidenceTypeDTO> evidenceDTOList = new ArrayList<EvidenceTypeDTO>();
        evidenceDTOList.add(evidenceDTO);
        Mockito.when(processDTO.getEvidenceType()).thenReturn(evidenceDTOList);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        caseServiceValidator.validationForCreateCase(caseDTO);
    }

    @Test
    public void shouldReturnExpectedResultCheckForTitleSurandForeNames() {
        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setTitle("Mr.");
        partyDTO.setForeName("GoodMorning");
        partyDTO.setSurName("Hello");
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        caseServiceValidator.mandatoryCheckForTitleSurandForeNames(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnNotExpectedResultCheckForTitleSurandForeNames() {
        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setForeName("GoodMorning");
        partyDTO.setSurName(casePsfValue);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(null);
        Mockito.when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS))
                .thenReturn(new ResponseErrorVO(errorCode_Party_Details, party_Details_Error_Msg, errStatus));
        caseServiceValidator.mandatoryCheckForTitleSurandForeNames(caseDTO);
    }

    @Test
    public void shouldReturnExpectedCheckForUploadValues() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        EvidenceTypeDTO evidenceDTO = new EvidenceTypeDTO();
        evidenceDTO.setCode(evidenceTypeCode);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setCode(documentCode);
        List<DocumentDTO> documentList = new ArrayList<DocumentDTO>();
        documentList.add(documentDTO);
        evidenceDTO.setDocument(documentList);
        List<EvidenceTypeDTO> evidenceDTOList = new ArrayList<EvidenceTypeDTO>();
        evidenceDTOList.add(evidenceDTO);
        Mockito.when(processDTO.getEvidenceType()).thenReturn(evidenceDTOList);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        caseServiceValidator.mandatoryCheckForUploadValues(caseDTO);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnUnExpectedCheckForUploadValues() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenThrow(DocUploadServiceException.class);
        EvidenceTypeDTO evidenceDTO = new EvidenceTypeDTO();
        evidenceDTO.setCode(evidenceTypeCode);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setCode(documentCode);
        List<DocumentDTO> documentList = new ArrayList<DocumentDTO>();
        documentList.add(documentDTO);
        evidenceDTO.setDocument(documentList);
        List<EvidenceTypeDTO> evidenceDTOList = new ArrayList<EvidenceTypeDTO>();
        evidenceDTOList.add(evidenceDTO);
        Mockito.when(processDTO.getEvidenceType()).thenReturn(evidenceDTOList);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        Mockito.when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_DETAILS))
                .thenReturn(new ResponseErrorVO(errorCode_Upload, upload_ErrorMsg, errStatus));
        caseServiceValidator.mandatoryCheckForUploadValues(caseDTO);
    }

    @Test
    public void shouldReturnExpectedPartyDetails() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setEmailId("LloydsBank@tcs.com");
        partyDTO.setMobileNumber("1236780456");
        partyDTO.setPostCode("BB10 4JJ");
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        caseServiceValidator.validationForPartyDetails(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnUnExpectedPartyDetails() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setEmailId("LloydsBank@tcs.com");
        partyDTO.setMobileNumber("123456780");
        partyDTO.setPostCode("145621");
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(null);
        caseServiceValidator.validationForPartyDetails(caseDTO);
    }

    @Test
    public void shouldReturnExpectedPartyDetailsForSixDigitPostCode() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setEmailId("LloydsBank@tcs.com");
        partyDTO.setMobileNumber("1236780456");
        partyDTO.setPostCode("E152FP");
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        boolean isValid = caseServiceValidator.validationForPartyDetails(caseDTO);
        assertTrue(isValid);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnExpectedPartyDetailsForInvalidPostCode() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setEmailId("LloydsBank@tcs.com");
        partyDTO.setMobileNumber("1236780456");
        partyDTO.setPostCode("E152**");
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        caseServiceValidator.validationForPartyDetails(caseDTO);

    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnExpectedPartyDetailsForInvalidEmailId() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setEmailId("abc");
        partyDTO.setMobileNumber("1236780456");
        partyDTO.setPostCode("E152FP");
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        caseServiceValidator.validationForPartyDetails(caseDTO);

    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnExpectedPartyDetailsForInvalidMobileNo() {
        PartyDTO partyDTO = new PartyDTO();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setEvidenceTypeCode(evidenceTypeCode);
        uploadDTO.setDocumentCode(documentCode);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadList.add(uploadDTO);
        partyDTO.setEmailId("LloydsBank@tcs.com");
        partyDTO.setMobileNumber("abcd");
        partyDTO.setPostCode("E152FP");
        partyDTO.setAttachmentDetails(uploadList);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        caseServiceValidator.validationForPartyDetails(caseDTO);

    }

    @Test
    public void testValidateSubmitCaseDto() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseReferenceNo(caseRefNo);
        caseDTO.setProcessCode(processCode);
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)).thenReturn(caseRefNo);
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        boolean validCheck = caseServiceValidator.validateSubmitCaseDto(caseDTO);
        Assert.assertEquals(true, validCheck);
    }

    @Test
    public void testValidateSubmitCaseDtoWithParty() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseReferenceNo(caseRefNo);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        PartyDTO party = new PartyDTO();
        party.setCasePartyID(1);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO upload = new UploadDTO();
        upload.setUploadSequenceNo(1001);
        uploadList.add(upload);
        party.setAttachmentDetails(uploadList);
        partyList.add(party);
        caseDTO.setPartyDetails(partyList);
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)).thenReturn(caseRefNo);
        boolean validCheck = caseServiceValidator.validateSubmitCaseDto(caseDTO);
        Assert.assertEquals(true, validCheck);
    }

    @Test
    public void testExpectionSubmitCaseDTOWithNullCaseRef() {
        exception.expect(DocUploadServiceException.class);
        caseServiceValidator.validateSubmitCaseDto(null);
    }

    @Test
    public void testExpectionSubmitCaseDTOWithNullParty() {
        exception.expect(DocUploadServiceException.class);
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseReferenceNo(caseRefNo);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        PartyDTO party = new PartyDTO();
        partyList.add(party);
        caseDTO.setPartyDetails(partyList);
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)).thenReturn(caseRefNo);
        caseServiceValidator.validateSubmitCaseDto(caseDTO);
    }

    @Test
    public void testExpectionSubmitCaseDTOWithInvaliUploadSeq() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseReferenceNo(caseRefNo);
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        PartyDTO party = new PartyDTO();
        party.setCasePartyID(1);
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO upload = new UploadDTO();
        uploadList.add(upload);
        party.setAttachmentDetails(uploadList);
        partyList.add(party);
        caseDTO.setPartyDetails(partyList);
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)).thenReturn(caseRefNo);
        Mockito.when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_SEQUENCE_NO))
                .thenReturn(new ResponseErrorVO(errorCode_Upload, upload_ErrorMsg, errStatus));
        exception.expect(DocUploadServiceException.class);
        caseServiceValidator.validateSubmitCaseDto(caseDTO);
    }

    @Test
    public void testValidateGetCaseResponseForValidResponse() {
        CaseDTO getCaseResponse = new CaseDTO();
        CaseDTO caseDto = new CaseDTO();
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyDTOList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(759);

        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO upload = new UploadDTO();
        upload.setUploadSequenceNo(1001);
        upload.setTmpSysFileRefNum("3444234-3");
        uploadList.add(upload);
        partyDTO.setAttachmentDetails(uploadList);

        partyDTOList.add(partyDTO);
        getCaseResponse.setPartyDetails(partyDTOList);

        caseDto.setPartyDetails(partyDTOList);

        boolean validCheck = caseServiceValidator.validateGetCaseDetails(getCaseResponse, caseDto);
        Assert.assertEquals(true, validCheck);
    }

    @Test
    public void testValidateGetCaseResponseForInvalidCasePartyId() {
        CaseDTO getCaseResponse = new CaseDTO();
        CaseDTO caseDto = new CaseDTO();
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyDTOList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(759);

        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO upload = new UploadDTO();
        upload.setUploadSequenceNo(1001);
        uploadList.add(upload);
        partyDTO.setAttachmentDetails(uploadList);

        partyDTOList.add(partyDTO);

        getCaseResponse.setPartyDetails(partyDTOList);

        PartyDTO requestPartyDTO = new PartyDTO();
        List<PartyDTO> requestPartyDTOList = new ArrayList<PartyDTO>();
        requestPartyDTO.setCasePartyID(758);

        List<UploadDTO> requestUploadList = new ArrayList<UploadDTO>();
        UploadDTO requestUpload = new UploadDTO();
        requestUpload.setUploadSequenceNo(1001);
        requestUploadList.add(requestUpload);
        requestPartyDTO.setAttachmentDetails(requestUploadList);

        requestPartyDTOList.add(requestPartyDTO);

        caseDto.setPartyDetails(requestPartyDTOList);
        exception.expect(DocUploadServiceException.class);
        caseServiceValidator.validateGetCaseDetails(getCaseResponse, caseDto);

    }

    @Test
    public void testValidateGetCaseResponseForInvalidUploadSequenceNo() {
        CaseDTO getCaseResponse = new CaseDTO();
        CaseDTO caseDto = new CaseDTO();
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyDTOList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(759);

        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO upload = new UploadDTO();
        upload.setUploadSequenceNo(1001);
        uploadList.add(upload);
        partyDTO.setAttachmentDetails(uploadList);

        partyDTOList.add(partyDTO);

        getCaseResponse.setPartyDetails(partyDTOList);

        PartyDTO requestPartyDTO = new PartyDTO();
        List<PartyDTO> requestPartyDTOList = new ArrayList<PartyDTO>();
        requestPartyDTO.setCasePartyID(759);

        List<UploadDTO> requestUploadList = new ArrayList<UploadDTO>();
        UploadDTO requestUpload = new UploadDTO();
        requestUpload.setUploadSequenceNo(1002);
        requestUploadList.add(requestUpload);
        requestPartyDTO.setAttachmentDetails(requestUploadList);

        requestPartyDTOList.add(requestPartyDTO);

        caseDto.setPartyDetails(requestPartyDTOList);
        exception.expect(DocUploadServiceException.class);
        caseServiceValidator.validateGetCaseDetails(getCaseResponse, caseDto);

    }

}
