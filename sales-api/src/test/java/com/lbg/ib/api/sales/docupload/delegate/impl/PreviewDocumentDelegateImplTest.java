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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
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
import org.springframework.core.io.ByteArrayResource;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.dao.RetrieveDocumentationDAO;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationItemDTO;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationProfileDTO;
import com.lbg.ib.api.sales.docupload.dto.document.InformationContentDTO;
import com.lbg.ib.api.sales.docupload.dto.document.ResponseHeaderDTO;
import com.lbg.ib.api.sales.docupload.dto.document.ResultConditionDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveDocumentationResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocumentationServiceMapper;
import com.lbg.ib.api.sales.docupload.mapper.RequestBodyMapper;
import com.lloydstsb.ea.config.ConfigurationService;

/**
 * @author 1174100
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PreviewDocumentDelegateImplTest {

    public @Rule ExpectedException exception = ExpectedException.none();

    @InjectMocks
    PreviewDocumentDelegateImpl    previewDocumentDelegateImpl;

    @Mock
    ResponseErrorCodeMapper        resolver;

    @Mock
    LoggerDAO                      logger;

    @Mock
    ExternalRestApiClientDAO       externalRestApiClientDAO;

    @Mock
    CaseResponseDTO                caseResponseDTO;

    @Mock
    ConfigurationService           configurationService;

    @Mock
    DocumentationServiceMapper     documentationServiceMapper;

    @Mock
    RetrieveDocumentationDAO       retrieveDocumentationDAO;

    @Mock
    RequestBodyMapper              requestBodyMappingHelper;

    @Mock
    RetrieveResponseDTO            retrieveResponseDTO;
    @Mock
    Attachment                     attachment;
    @Mock
    DataHandler                    dataHandler;

    @Mock
    MultivaluedMap<String, String> headers;

    @Before
    public void setUp() throws Exception {

        previewDocumentDelegateImpl = new PreviewDocumentDelegateImpl(retrieveDocumentationDAO, resolver, logger,
                documentationServiceMapper, requestBodyMappingHelper);
    }

    @Test
    public void testExpectedResult() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);

        Attachment file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
                new ContentDisposition("file"));

        documentResponse.setFile(file);

        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);
        informationContent.add(informationContentDTO);
        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(file);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        Attachment response = previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
        Assert.assertEquals("file", response.getContentDisposition().getType());
    }

    @Test(expected = DocUploadServiceException.class)
    public void testRetrieveDocumentationWhenResponseIsNull() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);
        Attachment file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
                new ContentDisposition("file"));

        documentResponse.setFile(file);
        documentResponse.setRetrieveDocumentationResponseDTO(null);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(file);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        Attachment response = previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
        Assert.assertEquals("file", response.getContentDisposition().getType());
    }

    @Test(expected = DocUploadServiceException.class)
    public void testRetrieveDocumentationWhenInvalidResponse() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);

        Attachment file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
                new ContentDisposition("file"));

        documentResponse.setFile(file);

        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);

        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(file);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        Attachment response = previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
        Assert.assertEquals("file", response.getContentDisposition().getType());
    }

    @Test(expected = DocUploadServiceException.class)
    public void testRetrieveDocumentationForInvalidResponseHeader() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        resheader.setCmdStatus("cmdStatus");
        resheader.setReturnCode("returnCode");
        ResultConditionDTO resultCondition = new ResultConditionDTO();
        resultCondition.setReasonCode("reasonCode");
        resultCondition.setReasonText("reasonText");
        resheader.setResultCondition(resultCondition);
        CaseDTO caseDto = new CaseDTO();
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);

        Attachment file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
                new ContentDisposition("file"));

        documentResponse.setFile(file);

        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);
        informationContent.add(informationContentDTO);
        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(file);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        Attachment response = previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
        Assert.assertEquals("file", response.getContentDisposition().getType());
    }

    @Test(expected = DocUploadServiceException.class)
    public void testRetrieveDocumentationForIOException() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);
        documentResponse.setFile(attachment);
        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        DocumentationProfileDTO.setContentType("contentType");
        DocumentationProfileDTO.setName("name");
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);
        informationContent.add(informationContentDTO);
        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(attachment);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(attachment);
        Mockito.when(attachment.getDataHandler()).thenReturn(dataHandler);
        Mockito.when(attachment.getHeaders()).thenReturn(headers);
        InputStream byteArrayResource = new ByteArrayResource(new byte[1]).getInputStream();
        Mockito.when(dataHandler.getContentType()).thenReturn("contentType");
        Mockito.when(dataHandler.getInputStream()).thenReturn(byteArrayResource).thenThrow(IOException.class);
        Attachment response = previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
        Assert.assertEquals("attachment", response.getContentDisposition().getType());
    }

    @Test
    public void testExpectedResultForUpdateDetails() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);

        Attachment file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
                new ContentDisposition("file"));

        documentResponse.setFile(file);

        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        DocumentationProfileDTO.setContentType("contentType");
        DocumentationProfileDTO.setName("name");
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);
        informationContent.add(informationContentDTO);
        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(file);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        Attachment response = previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
        Assert.assertEquals("attachment", response.getContentDisposition().getType());
    }

    @Test
    public void testInvalidResponse() {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();

        CaseDTO caseDto = new CaseDTO();
        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);
        exception.expect(DocUploadServiceException.class);
        previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
    }

    @Test
    public void testInvalidFileAttachment() {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);

        Attachment file = null;
        documentResponse.setFile(file);

        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);
        informationContent.add(informationContentDTO);
        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(file);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        exception.expect(DocUploadServiceException.class);
        previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testInvalidFileAttachmentForNullInputStream() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);
        documentResponse.setFile(attachment);
        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);
        informationContent.add(informationContentDTO);
        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(attachment);
        Mockito.when(attachment.getDataHandler()).thenReturn(dataHandler);
        Mockito.when(dataHandler.getInputStream()).thenReturn(null);
        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testInvalidFileAttachmentForIOException() throws IOException {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);
        documentResponse.setFile(attachment);
        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);
        informationContent.add(informationContentDTO);
        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(attachment);
        Mockito.when(attachment.getDataHandler()).thenReturn(dataHandler);
        Mockito.when(dataHandler.getInputStream()).thenThrow(IOException.class);
        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
    }

    @Test
    public void testInvalidResponseCode() {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);

        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);

        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        exception.expect(DocUploadServiceException.class);
        previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
    }

    @Test
    public void testInvalidInformationContent() {
        RetrieveResponseDTO documentResponse = new RetrieveResponseDTO();
        ResponseHeaderDTO resheader = new ResponseHeaderDTO();
        CaseDTO caseDto = new CaseDTO();

        resheader.setCmdStatus(DocUploadConstant.OK);
        resheader.setReturnCode(DocUploadConstant.SUCCESS);
        RetrieveDocumentationRequestDTO retrieveDocumentationRequestDTO = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = new RetrieveDocumentationResponseDTO();
        retrieveDocumentationResponseDTO.setResponseHeader(resheader);

        Attachment file = null;
        documentResponse.setFile(file);

        DocumentationItemDTO documentationItemDTO = new DocumentationItemDTO();
        List<InformationContentDTO> informationContent = new ArrayList<InformationContentDTO>();

        DocumentationProfileDTO DocumentationProfileDTO = new DocumentationProfileDTO();
        documentationItemDTO.setDocumentationProfile(DocumentationProfileDTO);

        documentationItemDTO.setInformationContent(informationContent);
        retrieveDocumentationResponseDTO.setDocumentationItem(documentationItemDTO);
        documentResponse.setRetrieveDocumentationResponseDTO(retrieveDocumentationResponseDTO);

        Mockito.when(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto))
                .thenReturn(retrieveDocumentationRequestDTO);
        Mockito.when(retrieveDocumentationDAO.previewRequest(retrieveDocumentationRequestDTO))
                .thenReturn(documentResponse);

        Mockito.when(retrieveResponseDTO.getFile()).thenReturn(file);

        Mockito.when(retrieveResponseDTO.getRetrieveDocumentationResponseDTO())
                .thenReturn(retrieveDocumentationResponseDTO);
        exception.expect(DocUploadServiceException.class);
        previewDocumentDelegateImpl.retrieveDocumentation(caseDto);
    }

}
