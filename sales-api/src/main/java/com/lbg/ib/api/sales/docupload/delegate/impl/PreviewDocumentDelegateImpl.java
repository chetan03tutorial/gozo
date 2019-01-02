/**
 *
 */

package com.lbg.ib.api.sales.docupload.delegate.impl;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dao.RetrieveDocumentationDAO;
import com.lbg.ib.api.sales.docupload.delegate.PreviewDocumentDelegate;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationItemDTO;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationProfileDTO;
import com.lbg.ib.api.sales.docupload.dto.document.ResponseHeaderDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveDocumentationResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.document.RetrieveResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocumentationServiceMapper;
import com.lbg.ib.api.sales.docupload.mapper.RequestBodyMapper;
import com.lloydstsb.ea.logging.event.TraceEvent;

/**
 * @author 8735182
 *
 */
@Component
public class PreviewDocumentDelegateImpl implements PreviewDocumentDelegate {
    private RetrieveDocumentationDAO   retrieveDocumentationDAO;

    private ResponseErrorCodeMapper    resolver;

    private LoggerDAO                  logger;

    private DocumentationServiceMapper documentationServiceMapper;

    /**
     * @param externalRestApiClientDAO
     * @param resolver
     * @param logger
     */
    @Autowired
    public PreviewDocumentDelegateImpl(RetrieveDocumentationDAO retrieveDocumentationDAO,
            ResponseErrorCodeMapper resolver, LoggerDAO logger, DocumentationServiceMapper documentationServiceMapper,
            RequestBodyMapper requestBodyMappingHelper) {
        this.retrieveDocumentationDAO = retrieveDocumentationDAO;
        this.resolver = resolver;
        this.logger = logger;
        this.documentationServiceMapper = documentationServiceMapper;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.lbg.ib.docupload.delegate.PreviewDocumentDelegate#
     * retrieveDocumentation(com.lbg.ib.docupload .dto.transaction.CaseDTO)
     */
    public Attachment retrieveDocumentation(CaseDTO caseDto) throws DocUploadServiceException {
        Attachment response = null;
        RetrieveResponseDTO documentResponse = retrieveDocumentationDAO
                .previewRequest(documentationServiceMapper.populateRetrieveDocumentRequest(caseDto));
        if (validateResponse(documentResponse.getRetrieveDocumentationResponseDTO())
                && validateFileAttachment(documentResponse.getFile())) {
            response = populateFileDetails(documentResponse);
        }
        return response;
    }

    private boolean validateFileAttachment(Attachment file) {
        boolean validAttachment = false;
        try {
            if (file != null && file.getDataHandler() != null && file.getDataHandler().getInputStream() != null
                    && file.getDataHandler().getContentType() != null) {
                validAttachment = true;
            } else {
                logTrace("RetrieveDocumentaitonService return empty file");
                throwDocUploadException(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
            }
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return validAttachment;
    }

    private Attachment populateFileDetails(RetrieveResponseDTO documentResponse) {
        RetrieveDocumentationResponseDTO responseDTO = documentResponse.getRetrieveDocumentationResponseDTO();
        Attachment modifiedFile = null;
        try {
            MultivaluedMap<String, String> headers = setContentTypeAndFileName(
                    responseDTO.getDocumentationItem().getDocumentationProfile(),
                    documentResponse.getFile().getHeaders());
            modifiedFile = new Attachment(documentResponse.getFile().getDataHandler().getInputStream(), headers);
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return modifiedFile;
    }

    private MultivaluedMap<String, String> setContentTypeAndFileName(DocumentationProfileDTO documentationProfileDTO,
            MultivaluedMap<String, String> headers) {
        if (documentationProfileDTO.getContentType() != null) {
            headers.putSingle(DocUploadConstant.CONTENT_TYPE, documentationProfileDTO.getContentType());
        }
        if (documentationProfileDTO.getName() != null) {
            headers.putSingle(DocUploadConstant.CONTENT_DISPOSITION,
                    getContentDispositionAttachment(documentationProfileDTO.getName()));
        }
        return headers;
    }

    private String getContentDispositionAttachment(String filename) {
        StringBuilder builder = new StringBuilder(DocUploadConstant.CONTENT_DISPOSITION_VALUE);
        if (filename != null) {
            builder.append(filename).append('\"');
        }
        return builder.toString();
    }

    private boolean validateResponse(RetrieveDocumentationResponseDTO responseDTO) {
        boolean isValid = false;
        if (responseDTO != null && validateResponsecode(responseDTO.getResponseHeader())
                && validateInformataionContent(responseDTO.getDocumentationItem())) {
            isValid = true;
        } else {
            if (logger.debugEnabled()) {
                logger.logDebug(this.getClass(), "RetrieveDocumentaitonService return empty response");
            }
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return isValid;
    }

    private boolean validateResponsecode(ResponseHeaderDTO responseHeaderDTO) {
        boolean validResponse = false;
        if (responseHeaderDTO != null && DocUploadConstant.OK.equalsIgnoreCase(responseHeaderDTO.getCmdStatus())
                && DocUploadConstant.SUCCESS.equalsIgnoreCase(responseHeaderDTO.getReturnCode())) {
            validResponse = true;
        } else {
            if (responseHeaderDTO != null && responseHeaderDTO.getResultCondition() != null) {
                logTrace("RetrieveDocumentaitonService Error: " + responseHeaderDTO.getResultCondition().getReasonCode()
                        + responseHeaderDTO.getResultCondition().getReasonText());
            }
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return validResponse;
    }

    private boolean validateInformataionContent(DocumentationItemDTO documentationItemDTO) {
        boolean validInfoId = false;
        if (documentationItemDTO != null && documentationItemDTO.getInformationContent() != null
                && !documentationItemDTO.getInformationContent().isEmpty()
                && documentationItemDTO.getDocumentationProfile() != null) {
            validInfoId = true;
        } else {
            logTrace("RetrieveDocumentaitonService returns empty information content");
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE);
        }
        return validInfoId;
    }

    /**
     * @param errorCode
     * @throws DocUploadServiceException
     */
    private void throwDocUploadException(String errorCode) throws DocUploadServiceException {
        throw new DocUploadServiceException(resolver.resolve(errorCode));
    }

    private void logTrace(String msg) {
        TraceEvent event = new TraceEvent(DocUploadConstant.RETRIEVE_DOC_EVENT);
        event.setClassName(this.getClass().getName());
        event.setEventId(DocUploadConstant.RETRIEVE_DOC_EVENT);
        event.setMessage(msg);
        logger.logProductionTrace(event);
    }

}
