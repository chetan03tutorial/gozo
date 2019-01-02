
package com.lbg.ib.api.sales.docupload.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.attachment.AttachmentDataSource;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.helpers.LoadingByteArrayOutputStream;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.ClientWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.transport.http.HTTPConduit;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dao.helper.DynamicOutboundTLSClientParametersFactory;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationItemDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveDocumentationResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

@PrepareForTest(org.apache.cxf.jaxrs.client.WebClient.class)
@SuppressStaticInitializationFor({ "org.apache.cxf.jaxrs.client.WebClient" })
@RunWith(PowerMockRunner.class)
public class RetrieveDocumentationDAOImplTest {
    private static final String                       ENDPOINT = "http://myDevServer.com";

    @Mock
    private ResponseErrorCodeMapper                   resolver;

    @Mock
    private LoggerDAO                                 logger;

    @Mock
    private ObjectMapper                              objectMapper;

    @InjectMocks
    private RetrieveDocumentationDAOImpl              retrieveDocumentationDAOImpl;

    @Mock
    private WebClient                                 client;

    @Mock
    private ClientConfiguration                       clientConfig;

    @Mock
    private HTTPConduit                               conduit;

    @Mock
    private DynamicOutboundTLSClientParametersFactory tlsClientParametersFactory;

    @Before
    public void setUp() throws Exception {
        java.net.URL url = new java.net.URL(ENDPOINT);
        retrieveDocumentationDAOImpl = new RetrieveDocumentationDAOImpl(objectMapper, logger, resolver, url);
        PowerMockito.mockStatic(WebClient.class);
        Mockito.when(WebClient.create(Mockito.anyString(), Mockito.anyList())).thenReturn(client);
        Mockito.when(client.type(MediaType.APPLICATION_JSON_VALUE)).thenReturn(client);

        Mockito.when(WebClient.getConfig(Mockito.anyObject())).thenReturn(clientConfig);
        Mockito.when(clientConfig.getHttpConduit()).thenReturn(conduit);

        ReflectionTestUtils.setField(retrieveDocumentationDAOImpl, "client", client);
        ReflectionTestUtils
        .setField(retrieveDocumentationDAOImpl, "tlsClientParametersFactory", tlsClientParametersFactory);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testPreviewRequestWithHttpMessageConversionException() throws IOException {

        RetrieveDocumentationRequestDTO requestBody = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO res = new RetrieveDocumentationResponseDTO();
        res.setDocumentationItem(new DocumentationItemDTO());
        ObjectMapper obj = new ObjectMapper();
        String responseStr = obj.writeValueAsString(res);
        LoadingByteArrayOutputStream bout = new LoadingByteArrayOutputStream();
        IOUtils.copy(new ByteArrayResource(responseStr.getBytes()).getInputStream(), bout);
        InputStream ins = new DelegatingInputStream(bout.createInputStream());

        DataSource ds = new AttachmentDataSource(MediaType.APPLICATION_JSON_VALUE, ins);
        Collection<Attachment> acollection = (Collection<Attachment>) new ArrayList<Attachment>();
        Attachment att = new Attachment("json", new ByteArrayResource(responseStr.getBytes()).getInputStream(),
                new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));

        DataHandler dh = new DataHandler(ds);
        MultivaluedMap<String, String> headers = att.getHeaders();
        headers.put(DocUploadConstant.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        Attachment jsonPart = new Attachment("json", dh, headers);
        Attachment file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
                new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));
        acollection.add(jsonPart);
        acollection.add(file);

        Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class),
                Mockito.eq(RetrieveDocumentationResponseDTO.class))).thenReturn(res);

        Mockito.<Collection<? extends Attachment>> when(client
                .postAndGetCollection(Mockito.any(RetrieveDocumentationRequestDTO.class), Mockito.eq(Attachment.class)))
                .thenThrow(new HttpMessageConversionException("dummy msg"));

        RetrieveResponseDTO response = retrieveDocumentationDAOImpl.previewRequest(requestBody);
        Assert.assertNotNull(response.getFile());
        Assert.assertNotNull(response.getRetrieveDocumentationResponseDTO());

    }

    @Test
    public void testPreviewRequest() throws IOException {

        RetrieveDocumentationRequestDTO requestBody = new RetrieveDocumentationRequestDTO();
        RetrieveDocumentationResponseDTO res = new RetrieveDocumentationResponseDTO();
        res.setDocumentationItem(new DocumentationItemDTO());
        ObjectMapper obj = new ObjectMapper();
        String responseStr = obj.writeValueAsString(res);
        LoadingByteArrayOutputStream bout = new LoadingByteArrayOutputStream();
        IOUtils.copy(new ByteArrayResource(responseStr.getBytes()).getInputStream(), bout);
        InputStream ins = new DelegatingInputStream(bout.createInputStream());

        DataSource ds = new AttachmentDataSource(MediaType.APPLICATION_JSON_VALUE, ins);
        Collection<Attachment> acollection = (Collection<Attachment>) new ArrayList<Attachment>();
        Attachment att = new Attachment("json", new ByteArrayResource(responseStr.getBytes()).getInputStream(),
                new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));

        DataHandler dh = new DataHandler(ds);
        MultivaluedMap<String, String> headers = att.getHeaders();
        headers.put(DocUploadConstant.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        Attachment jsonPart = new Attachment("json", dh, headers);
        Attachment file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
                new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));
        acollection.add(jsonPart);
        acollection.add(file);

        Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class),
                Mockito.eq(RetrieveDocumentationResponseDTO.class))).thenReturn(res);

        Mockito.<Collection<? extends Attachment>> when(client
                .postAndGetCollection(Mockito.any(RetrieveDocumentationRequestDTO.class), Mockito.eq(Attachment.class)))
                .thenReturn(acollection);

        RetrieveResponseDTO response = retrieveDocumentationDAOImpl.previewRequest(requestBody);
        Assert.assertNotNull(response.getFile());
        Assert.assertNotNull(response.getRetrieveDocumentationResponseDTO());

    }

    @Test(expected = DocUploadServiceException.class)
    public void testWebClientExceptionCase() {
        Mockito.when(client.postAndGetCollection(Mockito.any(RetrieveDocumentationRequestDTO.class),
                Mockito.eq(Attachment.class))).thenThrow(new ClientWebApplicationException());
        retrieveDocumentationDAOImpl.previewRequest(new RetrieveDocumentationRequestDTO());
    }

    @Test
    public void testNullResponse() {
        Collection<Attachment> acollection = (Collection<Attachment>) new ArrayList<Attachment>();
        Attachment jsonPart = new Attachment("id", MediaType.APPLICATION_JSON_VALUE, null);
        acollection.add(jsonPart);
        Mockito.<Collection<? extends Attachment>> when(client
                .postAndGetCollection(Mockito.any(RetrieveDocumentationRequestDTO.class), Mockito.eq(Attachment.class)))
                .thenReturn(acollection);
        RetrieveResponseDTO response = retrieveDocumentationDAOImpl
                .previewRequest(new RetrieveDocumentationRequestDTO());
        Assert.assertNull(response.getRetrieveDocumentationResponseDTO());
        Assert.assertNull(response.getFile());
    }

    @Test(expected = DocUploadServiceException.class)
    public void testHttMessageConversionException() {
        Mockito.when(client.postAndGetCollection(Mockito.any(RetrieveDocumentationRequestDTO.class),
                Mockito.eq(Attachment.class))).thenThrow(new ClientWebApplicationException());
        retrieveDocumentationDAOImpl.previewRequest(new RetrieveDocumentationRequestDTO());
    }

    @Test(expected = DocUploadServiceException.class)
    public void testRestClientException() {
        Mockito.when(client.postAndGetCollection(Mockito.any(RetrieveDocumentationRequestDTO.class),
                Mockito.eq(Attachment.class))).thenThrow(new RestClientException("error"));
        retrieveDocumentationDAOImpl.previewRequest(new RetrieveDocumentationRequestDTO());
    }

    @Test(expected = DocUploadServiceException.class)
    public void testJsonMappingException() throws IOException {

        ObjectMapper obj = new ObjectMapper();
        String responseStr = obj.writeValueAsString(new RetrieveDocumentationRequestDTO());
        LoadingByteArrayOutputStream bout = new LoadingByteArrayOutputStream();
        IOUtils.copy(new ByteArrayResource(responseStr.getBytes()).getInputStream(), bout);
        InputStream ins = new DelegatingInputStream(bout.createInputStream());

        DataSource ds = new AttachmentDataSource(MediaType.APPLICATION_JSON_VALUE, ins);
        Collection<Attachment> acollection = (Collection<Attachment>) new ArrayList<Attachment>();
        Attachment att = new Attachment("jsonId", new ByteArrayResource(responseStr.getBytes()).getInputStream(),
                new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));

        DataHandler dh = new DataHandler(ds);
        MultivaluedMap<String, String> headers = att.getHeaders();
        headers.put(DocUploadConstant.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        Attachment jsonPart = new Attachment("jsonId", dh, headers);
        acollection.add(jsonPart);

        Mockito.when(objectMapper.readValue(Mockito.any(InputStream.class),
                Mockito.eq(RetrieveDocumentationResponseDTO.class))).thenThrow(new JsonMappingException("Error msg"));
        Mockito.<Collection<? extends Attachment>> when(client
                .postAndGetCollection(Mockito.any(RetrieveDocumentationRequestDTO.class), Mockito.eq(Attachment.class)))
                .thenReturn(acollection);

        retrieveDocumentationDAOImpl.previewRequest(new RetrieveDocumentationRequestDTO());

    }

}
