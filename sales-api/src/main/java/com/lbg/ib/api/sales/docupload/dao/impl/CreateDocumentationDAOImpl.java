/**
 *
 */

package com.lbg.ib.api.sales.docupload.dao.impl;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.docupload.dao.CreateDocumentationDAO;
import com.lbg.ib.api.sales.docupload.dao.helper.DynamicOutboundTLSClientParametersFactory;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationResponseDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.exceptions.ResourceAccessException;
import org.apache.cxf.jaxrs.client.ClientWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 8735182
 *
 */
@Component
public class CreateDocumentationDAOImpl implements CreateDocumentationDAO {

    private LoggerDAO               logger;

    private ResponseErrorCodeMapper resolver;

    private ObjectMapper            objectMapper;

    private URL                     createDocumentEndpoint;

    private WebClient               client;

    @Autowired
    private DynamicOutboundTLSClientParametersFactory tlsClientParametersFactory;

    /**
     * @param logger
     * @param resolverR
     * @param objectMapper
     */
    @Autowired
    public CreateDocumentationDAOImpl(LoggerDAO logger, ResponseErrorCodeMapper resolver, ObjectMapper objectMapper,
            URL createDocumentEndpoint) {
        this.logger = logger;
        this.resolver = resolver;
        this.objectMapper = objectMapper;
        this.createDocumentEndpoint = createDocumentEndpoint;
    }

    public CreateDocumentationResponseDTO createDocumentation(CreateDocumentationRequestDTO uploadDocumentRequestDTO,
            InputStream fileInByte) throws DocUploadServiceException {
        CreateDocumentationResponseDTO resposne = null;
        try {
            final String fileName = uploadDocumentRequestDTO.getDocumentationItem().getDocumentationProfile().getName();

            List<Attachment> atts = new LinkedList<Attachment>();
            // Shiv code
            Attachment jsonAtt = new Attachment(DocUploadConstant.CREATE_DOC_JSON_PART,
                    MediaType.APPLICATION_JSON_VALUE, uploadDocumentRequestDTO);

            atts.add(jsonAtt);
            // uncomment it later
            /*
             * atts.add(new Attachment( DocUploadConstant.CREATE_DOC_JSON_PART,
             * MediaType.APPLICATION_JSON_VALUE, uploadDocumentRequestDTO));
             */
            atts.add(new Attachment(DocUploadConstant.CREATE_DOC_FILE, fileInByte,
                    new ContentDisposition(getContentDispositionAttachment(fileName))));
            List<Object> providers = new ArrayList<Object>();
            JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
            jsonProvider.setMapper(objectMapper);
            providers.add(jsonProvider);
            printUploadDocument(uploadDocumentRequestDTO);
            client = WebClient.create(createDocumentEndpoint.toString(), providers);

            WebClient.getConfig(client).getHttpConduit().setTlsClientParameters(
                    tlsClientParametersFactory.getInstance(createDocumentEndpoint));

            client.type(DocUploadConstant.MULTIPART_RELATED).accept(MediaType.APPLICATION_JSON_VALUE);
            resposne = client.postCollection(atts, Attachment.class, CreateDocumentationResponseDTO.class);
        } catch (HttpMessageConversionException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        } catch (ResourceAccessException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        } catch (RestClientException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        } catch (WebApplicationException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        } catch (IllegalArgumentException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        } catch (ClientWebApplicationException e) {
            logger.traceLog(this.getClass(),"Server response::" + e.getResponse());
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        }
        return resposne;
    }

    public void printUploadDocument(CreateDocumentationRequestDTO uploadDocumentRequestDTO) {
        try {
            logger.traceLog(this.getClass(),
                    "Upload Request::" + objectMapper.writeValueAsString(uploadDocumentRequestDTO) + "\nURL::"
                            + createDocumentEndpoint.toString());
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.logException(this.getClass(), e);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.logException(this.getClass(), e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.logException(this.getClass(), e);
        }
    }

    private String getContentDispositionAttachment(String filename) {
        StringBuilder builder = new StringBuilder(DocUploadConstant.CONTENT_DISPOSITION_VALUE);
        builder.append(filename).append('\"');
        return builder.toString();
    }

    /**
     * @param errorCode
     * @throws DocUploadServiceException
     */
    private void logAndThrowDocUploadException(Exception e, String errorCode) throws DocUploadServiceException {
        logger.logException(this.getClass(), e);
        throw new DocUploadServiceException(resolver.resolve(errorCode));
    }

}
