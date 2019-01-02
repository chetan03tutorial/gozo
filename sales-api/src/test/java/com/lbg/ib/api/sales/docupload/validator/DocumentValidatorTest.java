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

import static org.junit.Assert.assertTrue;

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
public class DocumentValidatorTest {
    private static final String           processCode = "REMOX-ON-LTB";

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
    private DocumentValidator             documentValidator;

    @Test
    public void shouldReturnExpectedPartyDetails() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setPartyDetails(new ArrayList<PartyDTO>());
        caseDTO.setProcessCode(processCode);
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        boolean validCheck = documentValidator.validateCaseDtoWithPartyDtls(caseDTO);
        Assert.assertEquals(true, validCheck);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnUnExpectedPartyDetails() {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setPartyDetails(null);
        caseDTO.setProcessCode("asdf");
        Mockito.when(docUploadServiceHelper.getProcessCode()).thenReturn(processCode);
        documentValidator.validateCaseDtoWithPartyDtls(caseDTO);
    }

    @Test
    public void shouldReturnExpectedCasePartyId() {
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        documentValidator.validateCasePartyId(caseDTO);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnUnExpectedCasePartyId() {
        CaseDTO casedto = new CaseDTO();
        casedto.setPartyDetails(null);
        documentValidator.validateCasePartyId(casedto);
    }

    @Test
    public void shouldReturnExpectedUploadDetails() {
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        partyList.add(partyDTO);
        UploadDTO uploadDTO = new UploadDTO();
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadDTO.setSrcSessionId("asf567");
        uploadDTO.setUploadSequenceNo(12);
        uploadDTO.setTmpSysFileRefNum("sadghsd");
        uploadList.add(uploadDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        documentValidator.uploadFieldsValidation(caseDTO, true);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnUnExpectedUploadDetails() {
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        UploadDTO uploadDTO = new UploadDTO();
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadDTO.setSrcSessionId("asf567");
        uploadDTO.setUploadSequenceNo(12);
        uploadDTO.setTmpSysFileRefNum(null);
        uploadList.add(uploadDTO);
        partyDTO.setAttachmentDetails(uploadList);
        partyList.add(partyDTO);
        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        documentValidator.uploadFieldsValidation(caseDTO, true);
    }

    @Test
    public void shouldReturnExpectedUploadDetailsForDeletionService() {
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setSrcSessionId("asf567");
        uploadDTO.setUploadSequenceNo(12);
        uploadDTO.setTmpSysFileRefNum("sadghsd");
        uploadList.add(uploadDTO);
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        partyDTO.setAttachmentDetails(uploadList);

        partyList.add(partyDTO);

        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        boolean isValid = documentValidator.uploadFieldsValidation(caseDTO, false);
        assertTrue(isValid);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnExpectedUploadDetailsForDeletionServiceWhenUploadSequenceNoIsNull() {
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setSrcSessionId("asf567");
        uploadDTO.setUploadSequenceNo(null);
        uploadDTO.setTmpSysFileRefNum("sadghsd");
        uploadList.add(uploadDTO);
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        partyDTO.setAttachmentDetails(uploadList);

        partyList.add(partyDTO);

        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        boolean isValid = documentValidator.uploadFieldsValidation(caseDTO, false);
        assertTrue(isValid);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnExpectedUploadDetailsForDeletionServiceWhenFileRefIsNull() {
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setSrcSessionId("asf567");
        uploadDTO.setUploadSequenceNo(12);
        uploadDTO.setTmpSysFileRefNum(null);
        uploadList.add(uploadDTO);
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        partyDTO.setAttachmentDetails(uploadList);

        partyList.add(partyDTO);

        Mockito.when(caseDTO.getPartyDetails()).thenReturn(partyList);
        boolean isValid = documentValidator.uploadFieldsValidation(caseDTO, false);
        assertTrue(isValid);
    }

    @Test
    public void shouldReturnExpectedResultValidatePreviewService() {
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        UploadDTO uploadDTO = new UploadDTO();
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadDTO.setSrcSessionId("asfsd");
        uploadDTO.setUploadSequenceNo(12);
        uploadDTO.setTmpSysFileRefNum("asd");
        uploadList.add(uploadDTO);
        partyDTO.setAttachmentDetails(uploadList);
        partyList.add(partyDTO);
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setPartyDetails(partyList);
        caseDTO.setProcessCode(processCode);
        documentValidator.validateDocumentService(caseDTO, true);
    }

    @Test(expected = DocUploadServiceException.class)
    public void shouldReturnUnExpectedResultValidatePreviewService() {
        PartyDTO partyDTO = new PartyDTO();
        List<PartyDTO> partyList = new ArrayList<PartyDTO>();
        partyDTO.setCasePartyID(123);
        UploadDTO uploadDTO = new UploadDTO();
        List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
        uploadDTO.setSrcSessionId(null);
        uploadDTO.setUploadSequenceNo(12);
        uploadDTO.setTmpSysFileRefNum(":!{88E47569-9AF8-479F-9C4C-15C0B29E0C5F}");
        uploadList.add(uploadDTO);
        partyDTO.setAttachmentDetails(uploadList);
        partyList.add(partyDTO);
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setPartyDetails(partyList);
        caseDTO.setProcessCode(null);
        documentValidator.validateDocumentService(caseDTO, true);
    }

    @Test
    public void shouldReturnValidateRequestParameters() {

        boolean isValidString = documentValidator.validateRequestParameters("dg");

        Assert.assertEquals(true, isValidString);

    }

    @Test
    public void shouldReturnUnExpectedValidateRequestParameters() {

        boolean isValidString = documentValidator.validateRequestParameters("d!@g");

        Assert.assertEquals(false, isValidString);

    }
}
