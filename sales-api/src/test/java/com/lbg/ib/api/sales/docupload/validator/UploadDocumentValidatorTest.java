/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name:   DocumentValidatorTest 
 *   
 * Author(s): 1146728
 *  
 * Date: 09 Apr 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.validator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.refdata.DocumentDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.EvidenceTypeDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.FileFormatsDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.PartyDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocUploadRefDataServiceMapper;

/**
 * @author 1146728
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class UploadDocumentValidatorTest {

    private static final String           EVIDENCE_CODE = "BST";

    private static final String           caseStatus    = "OP";

    private static final String           DOC_CODE      = "POI";

    private static final String           S_ID          = "abcd";

    private static final String           FILE_NAME     = "ss.jpg";

    private static final String           CNT_TYPE      = "image/jpeg";

    @Mock
    private DocUploadRefDataServiceMapper docUploadServiceHelper;

    @Mock
    private ResponseErrorCodeMapper       errorCodeMapper;

    @Mock
    private HttpServletRequest            httpRequest;

    @InjectMocks
    private UploadDocumentValidator       documentValidator;

    @Test
    public void shouldReturnValidateRequestParameters() {

        boolean isValidString = documentValidator.validateRequestParameters("dg");

        Assert.assertEquals(true, isValidString);

    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnUnExpectedValidateRequestParameters() {
        documentValidator.validateRequestParameters("d!@g");
    }

    @Test
    public void validDetailsToUpdateScanStatus() {
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        boolean isValidString = documentValidator.validateToUpdateScanStatus(caseDto);
        Assert.assertEquals(true, isValidString);
    }

    @Test(expected = DocUploadServiceException.class)
    public void inValidDetailsToUpdateScanStatus() {
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        documentValidator.validateToUpdateScanStatus(caseDto);
    }

    @Test
    public void validUploadDetails() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        caseDto.setCaseStatus(caseStatus);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn(S_ID);
        boolean isValidString = documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
        boolean isValidCount = documentValidator.validateFileUploadCount(caseDto, getCaseDetails());
        Assert.assertEquals(true, isValidString);
        Assert.assertEquals(true, isValidCount);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validUploadDetailsForInvalidFileSize() {
        int fileSize = 1200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        caseDto.setCaseStatus(caseStatus);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn(S_ID);
        boolean isValidString = documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
        boolean isValidCount = documentValidator.validateFileUploadCount(caseDto, getCaseDetails());
        Assert.assertEquals(true, isValidString);
        Assert.assertEquals(true, isValidCount);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validUploadDetailsWhenInValidContentType() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        caseDto.setCaseStatus(caseStatus);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType("abc");
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn(S_ID);
        boolean isValidString = documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
        boolean isValidCount = documentValidator.validateFileUploadCount(caseDto, getCaseDetails());
        Assert.assertEquals(true, isValidString);
        Assert.assertEquals(true, isValidCount);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validUploadDetailsWhenContentTypeIsNull() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        caseDto.setCaseStatus(caseStatus);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(null);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn(S_ID);
        boolean isValidString = documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
        boolean isValidCount = documentValidator.validateFileUploadCount(caseDto, getCaseDetails());
        Assert.assertEquals(true, isValidString);
        Assert.assertEquals(true, isValidCount);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validUploadDetailsWhenAttachmentIsNUll() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        caseDto.setCaseStatus(caseStatus);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn(S_ID);
        boolean isValidString = documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
        boolean isValidCount = documentValidator.validateFileUploadCount(caseDto, getCaseDetails());
        Assert.assertEquals(true, isValidString);
        Assert.assertEquals(true, isValidCount);
    }

    @Test(expected = DocUploadServiceException.class)
    public void inValidUploadDetails() {
        int fileSize = 150;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void inValidCaseDetails() {
        int fileSize = 40;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void inValidPartyId() {
        int fileSize = 100;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void multiplePartyDetailsPassed() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void multipleUploadDetailsPassed() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploaddtos.add(uploadDTO);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void invalidFileSizeUploaded() {
        int fileSize = 12000;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(12000);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void inValidEvidenceCode() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(DOC_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void inValidDocCode() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(EVIDENCE_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void inValidContentType() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(FILE_NAME);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void nullContentType() {
        int fileSize = 200;
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(fileSize);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(getProcessDTO());
        documentValidator.validateUploadDetails(caseDto, fileSize, caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void validFileUploadCount() {
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(200);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);
        ProcessDTO processDTO = getProcessDTO();
        processDTO.setUploadSizeLimit(3);
        processDTO.setDocUploadFileLimit(2);
        Mockito.when(docUploadServiceHelper.getProcessDto()).thenReturn(processDTO);
        documentValidator.validateFileUploadCount(caseDto, getCaseDetails());

    }

    private CaseDTO getCaseDetails() {
        CaseDTO caseDto = new CaseDTO();
        caseDto.setCaseId(12);
        List<PartyDTO> partys = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        partyDetail.setCasePartyID(123);
        List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setContentType(CNT_TYPE);
        uploadDTO.setFileName(FILE_NAME);
        uploadDTO.setSrcSessionId(S_ID);
        uploadDTO.setEvidenceTypeCode(EVIDENCE_CODE);
        uploadDTO.setDocumentCode(DOC_CODE);
        uploadDTO.setFileSize(200);
        uploaddtos.add(uploadDTO);
        uploaddtos.add(uploadDTO);
        uploaddtos.add(uploadDTO);
        uploaddtos.add(uploadDTO);
        partyDetail.setAttachmentDetails(uploaddtos);
        partys.add(partyDetail);
        caseDto.setPartyDetails(partys);

        return caseDto;
    }

    private ProcessDTO getProcessDTO() {
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setUploadSizeLimit(1000);
        processDTO.setDocUploadFileLimit(10);
        List<EvidenceTypeDTO> evidenceType = new ArrayList<EvidenceTypeDTO>();
        EvidenceTypeDTO evidenceTypeDTO = new EvidenceTypeDTO();
        DocumentDTO document = new DocumentDTO();
        document.setCode("POI");
        document.setUploadFileLimit(10);
        List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();
        documentDTOs.add(document);
        evidenceTypeDTO.setCode("BST");
        evidenceTypeDTO.setDocument(documentDTOs);
        evidenceType.add(evidenceTypeDTO);
        processDTO.setEvidenceType(evidenceType);
        List<FileFormatsDTO> fileFormats = new ArrayList<FileFormatsDTO>();
        FileFormatsDTO fileFormat = new FileFormatsDTO();
        fileFormat.setCode("122");
        fileFormat.setContentType("image/jpeg");
        fileFormats.add(fileFormat);
        processDTO.setFileFormat(fileFormats);
        return processDTO;
    }

}
