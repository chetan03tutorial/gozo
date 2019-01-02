/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: PreviewDocumentDelegateImplTest
 *
 * Author(s):1174100
 *
 * Date: 8 May 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dao.CreateDocumentationDAO;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationItemIdDTO;
import com.lbg.ib.api.sales.docupload.dto.document.InformationContentIdDTO;
import com.lbg.ib.api.sales.docupload.dto.document.ResponseHeaderDTO;
import com.lbg.ib.api.sales.docupload.dto.document.ResultConditionDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocumentationServiceMapper;

/**
 * @author 1174100
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class UploadDocumentDelegateImplTest {

    public @Rule ExpectedException     exception = ExpectedException.none();

    @InjectMocks
    UploadDocumentDelegateImpl         uploadDocumentDelegateImpl;

    @Mock
    private ResponseErrorCodeMapper    resolver;
    @Mock
    private LoggerDAO                  logger;
    @Mock
    private CreateDocumentationDAO     createDocumentationDAO;
    @Mock
    private DocumentationServiceMapper mapper;

    @Before
    public void setUp() throws Exception {

        uploadDocumentDelegateImpl = new UploadDocumentDelegateImpl(resolver, logger, createDocumentationDAO, mapper);
    }

    @Test
    public void testExpectedValidResults() {
        InputStream file = new ByteArrayInputStream(new byte[2]);
        CaseDTO caseDto = new CaseDTO();
        String idgen = "{wewer}";
        CreateDocumentationRequestDTO req = new CreateDocumentationRequestDTO();
        Mockito.when(mapper.populateCreateDocRequest(caseDto)).thenReturn(req);
        CreateDocumentationResponseDTO createRespone = new CreateDocumentationResponseDTO();
        createRespone.setDocumentationItem(new DocumentationItemIdDTO());
        createRespone.getDocumentationItem().setInformationContent(new InformationContentIdDTO());
        createRespone.getDocumentationItem().getInformationContent().setId(idgen);
        createRespone.setResponseHeader(new ResponseHeaderDTO());
        createRespone.getResponseHeader().setCmdStatus(DocUploadConstant.OK);
        createRespone.getResponseHeader().setReturnCode(DocUploadConstant.SUCCESS);
        Mockito.when(createDocumentationDAO.createDocumentation(req, file)).thenReturn(createRespone);
        String response = uploadDocumentDelegateImpl.uploadDocument(caseDto, file);
        Assert.assertEquals(idgen, response);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testUploadDocumentForInvalidResponseInfo() {
        InputStream file = new ByteArrayInputStream(new byte[2]);
        CaseDTO caseDto = new CaseDTO();
        String idgen = "{wewer}";
        CreateDocumentationRequestDTO req = new CreateDocumentationRequestDTO();
        Mockito.when(mapper.populateCreateDocRequest(caseDto)).thenReturn(req);
        CreateDocumentationResponseDTO createRespone = new CreateDocumentationResponseDTO();
        createRespone.setDocumentationItem(new DocumentationItemIdDTO());
        createRespone.getDocumentationItem().setInformationContent(new InformationContentIdDTO());
        createRespone.getDocumentationItem().getInformationContent();
        createRespone.setResponseHeader(new ResponseHeaderDTO());
        createRespone.getResponseHeader().setCmdStatus(DocUploadConstant.OK);
        createRespone.getResponseHeader().setReturnCode(DocUploadConstant.SUCCESS);
        Mockito.when(createDocumentationDAO.createDocumentation(req, file)).thenReturn(createRespone);
        String response = uploadDocumentDelegateImpl.uploadDocument(caseDto, file);
        Assert.assertEquals(idgen, response);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testUploadDocumentForInvalidResponseHeader() {
        InputStream file = new ByteArrayInputStream(new byte[2]);
        CaseDTO caseDto = new CaseDTO();
        String idgen = "{wewer}";
        CreateDocumentationRequestDTO req = new CreateDocumentationRequestDTO();
        Mockito.when(mapper.populateCreateDocRequest(caseDto)).thenReturn(req);
        CreateDocumentationResponseDTO createRespone = new CreateDocumentationResponseDTO();
        createRespone.setDocumentationItem(new DocumentationItemIdDTO());
        createRespone.getDocumentationItem().setInformationContent(new InformationContentIdDTO());
        createRespone.getDocumentationItem().getInformationContent().setId(idgen);
        createRespone.setResponseHeader(new ResponseHeaderDTO());
        createRespone.getResponseHeader().setCmdStatus(DocUploadConstant.OK);
        createRespone.getResponseHeader().setReturnCode(DocUploadConstant.FAILURE);
        ResultConditionDTO resultCondition = new ResultConditionDTO();
        resultCondition.setReasonCode("reasonCode");
        resultCondition.setReasonText("reasonText");
        createRespone.getResponseHeader().setResultCondition(resultCondition);
        Mockito.when(createDocumentationDAO.createDocumentation(req, file)).thenReturn(createRespone);
        String response = uploadDocumentDelegateImpl.uploadDocument(caseDto, file);
        Assert.assertEquals(idgen, response);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testUploadDocumentForNullResponse() {
        InputStream file = new ByteArrayInputStream(new byte[2]);
        CaseDTO caseDto = new CaseDTO();
        CreateDocumentationRequestDTO req = new CreateDocumentationRequestDTO();
        Mockito.when(mapper.populateCreateDocRequest(caseDto)).thenReturn(req);
        Mockito.when(createDocumentationDAO.createDocumentation(req, file)).thenReturn(null);
        uploadDocumentDelegateImpl.uploadDocument(caseDto, file);

    }

    @Test
    public void testExpectedDocUploadExceptionForErrorRes() {
        InputStream file = new ByteArrayInputStream(new byte[2]);
        CaseDTO caseDto = new CaseDTO();
        String idgen = "{wewer}";
        CreateDocumentationRequestDTO req = new CreateDocumentationRequestDTO();
        Mockito.when(mapper.populateCreateDocRequest(caseDto)).thenReturn(req);
        CreateDocumentationResponseDTO createRespone = new CreateDocumentationResponseDTO();
        createRespone.setDocumentationItem(new DocumentationItemIdDTO());
        createRespone.getDocumentationItem().setInformationContent(new InformationContentIdDTO());
        createRespone.getDocumentationItem().getInformationContent().setId(idgen);
        createRespone.setResponseHeader(new ResponseHeaderDTO());
        createRespone.getResponseHeader().setCmdStatus(DocUploadConstant.OK);
        Mockito.when(createDocumentationDAO.createDocumentation(req, file)).thenReturn(createRespone);
        exception.expect(DocUploadServiceException.class);
        uploadDocumentDelegateImpl.uploadDocument(caseDto, file);
    }

    @Test
    public void testExpectedDocUploadExceptionFroEmptyId() {
        InputStream file = new ByteArrayInputStream(new byte[2]);
        CaseDTO caseDto = new CaseDTO();
        CreateDocumentationRequestDTO req = new CreateDocumentationRequestDTO();
        Mockito.when(mapper.populateCreateDocRequest(caseDto)).thenReturn(req);
        CreateDocumentationResponseDTO createRespone = new CreateDocumentationResponseDTO();
        createRespone.setDocumentationItem(new DocumentationItemIdDTO());
        createRespone.getDocumentationItem().setInformationContent(new InformationContentIdDTO());
        createRespone.setResponseHeader(new ResponseHeaderDTO());
        createRespone.getResponseHeader().setCmdStatus(DocUploadConstant.OK);
        createRespone.getResponseHeader().setReturnCode(DocUploadConstant.SUCCESS);
        Mockito.when(createDocumentationDAO.createDocumentation(req, file)).thenReturn(createRespone);
        exception.expect(DocUploadServiceException.class);
        uploadDocumentDelegateImpl.uploadDocument(caseDto, file);
    }

}
