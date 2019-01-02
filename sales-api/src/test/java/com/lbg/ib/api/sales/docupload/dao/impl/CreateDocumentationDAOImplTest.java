/**
 *
 */

package com.lbg.ib.api.sales.docupload.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.ws.rs.WebApplicationException;

import org.apache.cxf.jaxrs.client.ClientWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
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
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationItemDTO;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationProfileDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lloydstsb.ea.exceptions.ResourceAccessException;

import com.lbg.ib.api.sales.docupload.dao.helper.DynamicOutboundTLSClientParametersFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.jaxrs.client.ClientConfiguration;

/**
 * @author 8735182
 *
 */
@PrepareForTest(org.apache.cxf.jaxrs.client.WebClient.class)
@SuppressStaticInitializationFor({ "org.apache.cxf.jaxrs.client.WebClient" })
@RunWith(PowerMockRunner.class)
public class CreateDocumentationDAOImplTest {

    private static String                             ENDPOINT = "http://myDevServer.com";

    @InjectMocks
    CreateDocumentationDAOImpl                        docUploadDAOImpl;

    @Mock
    private LoggerDAO                                 logger;

    @Mock
    private ResponseErrorCodeMapper                   resolver;

    @Mock
    private ObjectMapper                              objectMapper;

    @Mock
    private URL                                       createDocumentEndpoint;

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

        docUploadDAOImpl = new CreateDocumentationDAOImpl(logger, resolver, objectMapper, url);
        PowerMockito.mockStatic(WebClient.class);
        Mockito.when(WebClient.create(Mockito.anyString(), Mockito.anyList())).thenReturn(client);
        Mockito.when(client.type(DocUploadConstant.MULTIPART_RELATED)).thenReturn(client);
        Mockito.when(WebClient.getConfig(Mockito.anyObject())).thenReturn(clientConfig);
        Mockito.when(clientConfig.getHttpConduit()).thenReturn(conduit);

        ReflectionTestUtils.setField(docUploadDAOImpl, "client", client);
        ReflectionTestUtils
        .setField(docUploadDAOImpl, "tlsClientParametersFactory", tlsClientParametersFactory);
    }

    @Test
    public void testExpectedResult() {
        CreateDocumentationRequestDTO request = new CreateDocumentationRequestDTO();
        request.setDocumentationItem(new DocumentationItemDTO());
        request.getDocumentationItem().setDocumentationProfile(new DocumentationProfileDTO());
        request.getDocumentationItem().getDocumentationProfile().setName("ss.jpg");
        InputStream file = new ByteArrayInputStream(new byte[2]);
        Mockito.when(client.postCollection(Mockito.anyList(), Mockito.eq(Attachment.class),
                Mockito.eq(CreateDocumentationResponseDTO.class))).thenReturn(new CreateDocumentationResponseDTO());
        CreateDocumentationResponseDTO response = docUploadDAOImpl.createDocumentation(request, file);
        Assert.assertNotNull(response);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testExpectedResourceAccessException() {
        CreateDocumentationRequestDTO request = new CreateDocumentationRequestDTO();
        request.setDocumentationItem(new DocumentationItemDTO());
        request.getDocumentationItem().setDocumentationProfile(new DocumentationProfileDTO());
        request.getDocumentationItem().getDocumentationProfile().setName("ss.jpg");
        InputStream file = new ByteArrayInputStream(new byte[2]);
        ResourceAccessException accessException = new ResourceAccessException("1234", "error");
        Mockito.when(client.postCollection(Mockito.anyList(), Mockito.eq(Attachment.class),
                Mockito.eq(CreateDocumentationResponseDTO.class))).thenThrow(accessException);
        docUploadDAOImpl.createDocumentation(request, file);
        Mockito.verify(logger, Mockito.atLeastOnce()).logException(CreateDocumentationDAOImpl.class, accessException);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testExpectedWebApplicationException() {
        CreateDocumentationRequestDTO request = new CreateDocumentationRequestDTO();
        request.setDocumentationItem(new DocumentationItemDTO());
        request.getDocumentationItem().setDocumentationProfile(new DocumentationProfileDTO());
        request.getDocumentationItem().getDocumentationProfile().setName("ss.jpg");
        InputStream file = new ByteArrayInputStream(new byte[2]);
        WebApplicationException webApplicationException = new WebApplicationException();
        Mockito.when(client.postCollection(Mockito.anyList(), Mockito.eq(Attachment.class),
                Mockito.eq(CreateDocumentationResponseDTO.class))).thenThrow(webApplicationException);
        docUploadDAOImpl.createDocumentation(request, file);
        Mockito.verify(logger, Mockito.atLeastOnce()).logException(CreateDocumentationDAOImpl.class,
                webApplicationException);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testExpectedHttpMessageConversionException() {
        CreateDocumentationRequestDTO request = new CreateDocumentationRequestDTO();
        request.setDocumentationItem(new DocumentationItemDTO());
        request.getDocumentationItem().setDocumentationProfile(new DocumentationProfileDTO());
        request.getDocumentationItem().getDocumentationProfile().setName("ss.jpg");
        HttpMessageConversionException httpMessageConversionException = new HttpMessageConversionException("error");
        InputStream file = new ByteArrayInputStream(new byte[2]);
        Mockito.when(client.postCollection(Mockito.anyList(), Mockito.eq(Attachment.class),
                Mockito.eq(CreateDocumentationResponseDTO.class))).thenThrow(httpMessageConversionException);
        docUploadDAOImpl.createDocumentation(request, file);
        Mockito.verify(logger, Mockito.atLeastOnce()).logException(CreateDocumentationDAOImpl.class,
                httpMessageConversionException);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testExpectedRestClientException() {
        CreateDocumentationRequestDTO request = new CreateDocumentationRequestDTO();
        request.setDocumentationItem(new DocumentationItemDTO());
        request.getDocumentationItem().setDocumentationProfile(new DocumentationProfileDTO());
        request.getDocumentationItem().getDocumentationProfile().setName("ss.jpg");
        RestClientException restClientException = new RestClientException("error");
        InputStream file = new ByteArrayInputStream(new byte[2]);
        Mockito.when(client.postCollection(Mockito.anyList(), Mockito.eq(Attachment.class),
                Mockito.eq(CreateDocumentationResponseDTO.class))).thenThrow(restClientException);
        docUploadDAOImpl.createDocumentation(request, file);
        Mockito.verify(logger, Mockito.atLeastOnce()).logException(CreateDocumentationDAOImpl.class,
                restClientException);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testExpectedIllegalArgumentException() {
        CreateDocumentationRequestDTO request = new CreateDocumentationRequestDTO();
        request.setDocumentationItem(new DocumentationItemDTO());
        request.getDocumentationItem().setDocumentationProfile(new DocumentationProfileDTO());
        request.getDocumentationItem().getDocumentationProfile().setName("ss.jpg");
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
        InputStream file = new ByteArrayInputStream(new byte[2]);
        Mockito.when(client.postCollection(Mockito.anyList(), Mockito.eq(Attachment.class),
                Mockito.eq(CreateDocumentationResponseDTO.class))).thenThrow(illegalArgumentException);
        docUploadDAOImpl.createDocumentation(request, file);
        Mockito.verify(logger, Mockito.atLeastOnce()).logException(CreateDocumentationDAOImpl.class,
                illegalArgumentException);
    }

    @Test(expected = DocUploadServiceException.class)
    public void testExpectedClientWebApplicationException() {
        CreateDocumentationRequestDTO request = new CreateDocumentationRequestDTO();
        request.setDocumentationItem(new DocumentationItemDTO());
        request.getDocumentationItem().setDocumentationProfile(new DocumentationProfileDTO());
        request.getDocumentationItem().getDocumentationProfile().setName("ss.jpg");
        ClientWebApplicationException webApplicationException = new ClientWebApplicationException();
        InputStream file = new ByteArrayInputStream(new byte[2]);
        Mockito.when(client.postCollection(Mockito.anyList(), Mockito.eq(Attachment.class),
                Mockito.eq(CreateDocumentationResponseDTO.class))).thenThrow(webApplicationException);
        docUploadDAOImpl.createDocumentation(request, file);
        Mockito.verify(logger, Mockito.atLeastOnce()).logException(CreateDocumentationDAOImpl.class,
                webApplicationException);
    }

}
