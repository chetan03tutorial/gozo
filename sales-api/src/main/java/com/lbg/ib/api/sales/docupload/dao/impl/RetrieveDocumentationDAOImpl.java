/**
 *
 */

package com.lbg.ib.api.sales.docupload.dao.impl;

import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.docupload.dao.RetrieveDocumentationDAO;
import com.lbg.ib.api.sales.docupload.dao.helper.DynamicOutboundTLSClientParametersFactory;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveDocumentationResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveResponseDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.cxf.jaxrs.client.ClientWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import javax.activation.DataHandler;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author 8735182
 *
 */
@Component
public class RetrieveDocumentationDAOImpl implements RetrieveDocumentationDAO {

    private ObjectMapper            objectMapper;

    private LoggerDAO               logger;

    private ResponseErrorCodeMapper resolver;

    private URL                     retrieveDocumentEndpoint;

    private WebClient               client;

    @Autowired
    private DynamicOutboundTLSClientParametersFactory tlsClientParametersFactory;
    /**
     * @param objectMapper
     * @param logger
     * @param resolver
     * @param retrieveDocumentationWS
     */
    @Autowired
    public RetrieveDocumentationDAOImpl(ObjectMapper objectMapper, LoggerDAO logger, ResponseErrorCodeMapper resolver,
            URL retrieveDocumentEndpoint) {
        this.objectMapper = objectMapper;
        this.logger = logger;
        this.resolver = resolver;
        this.retrieveDocumentEndpoint = retrieveDocumentEndpoint;
    }

    public RetrieveResponseDTO previewRequest(RetrieveDocumentationRequestDTO requestBody) {
        RetrieveResponseDTO resposne = new RetrieveResponseDTO();
        try {
            List<Object> providers = new ArrayList<Object>();
            JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
            jsonProvider.setMapper(objectMapper);
            providers.add(jsonProvider);
            client = WebClient.create(retrieveDocumentEndpoint.toString(), providers);

            WebClient.getConfig(client).getHttpConduit().setTlsClientParameters(
                    tlsClientParametersFactory.getInstance(retrieveDocumentEndpoint));

            client.type(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.MULTIPART_FORM_DATA_VALUE);
            // client.getCollection(Attachment.class);
            Collection<? extends Attachment> atts = client.postAndGetCollection(requestBody, Attachment.class);
            Iterator<? extends Attachment> fileEntries = atts.iterator();
            while (fileEntries.hasNext()) {
                Attachment attachment = fileEntries.next();
                if (attachment != null) {
                    populateResponse(resposne, attachment);
                }
            }
        } catch (HttpMessageConversionException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        } catch (ResourceAccessException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        } catch (RestClientException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        } catch (WebApplicationException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        } catch (IllegalArgumentException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        } catch (ClientWebApplicationException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return resposne;
    }

    private void populateResponse(RetrieveResponseDTO resposne, Attachment attachment) {
        if (attachment.getContentType() != null
                && MediaType.APPLICATION_JSON_VALUE.equals(attachment.getContentType().toString())) {
            resposne.setRetrieveDocumentationResponseDTO(
                    populateRetrieveDocumentationResponseDTO(attachment.getDataHandler()));
        } else if (validAttachment(attachment.getDataHandler())) {

            resposne.setFile(attachment);
        }
    }

    private boolean validAttachment(DataHandler dataHandler) {
        boolean validAttachment = false;
        try {
            if (dataHandler != null && dataHandler.getInputStream() != null && dataHandler.getContentType() != null) {
                validAttachment = true;
            }
        } catch (IOException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return validAttachment;
    }

    private RetrieveDocumentationResponseDTO populateRetrieveDocumentationResponseDTO(DataHandler dataHandlerOfJson) {
        RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO = null;
        try {
            if (dataHandlerOfJson != null && dataHandlerOfJson.getInputStream() != null) {
                retrieveDocumentationResponseDTO = objectMapper.readValue(dataHandlerOfJson.getInputStream(),
                        RetrieveDocumentationResponseDTO.class);
            }
        } catch (JsonParseException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        } catch (JsonMappingException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        } catch (IOException e) {
            logAndThrowDocUploadException(e, ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return retrieveDocumentationResponseDTO;
    }

    private void logAndThrowDocUploadException(Exception e, String errorCode) throws DocUploadServiceException {
        logger.logException(this.getClass(), e);
        throw new DocUploadServiceException(resolver.resolve(errorCode));
    }
}
