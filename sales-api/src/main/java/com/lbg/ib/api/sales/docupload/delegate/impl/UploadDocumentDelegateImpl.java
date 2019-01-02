/**
 *
 */

package com.lbg.ib.api.sales.docupload.delegate.impl;

import java.io.InputStream;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dao.CreateDocumentationDAO;
import com.lbg.ib.api.sales.docupload.delegate.UploadDocumentDelegate;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.document.DocumentationItemIdDTO;
import com.lbg.ib.api.sales.docupload.dto.document.ResponseHeaderDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocumentationServiceMapper;
import com.lloydstsb.ea.logging.event.TraceEvent;

/**
 * @author 8735182
 *
 */
@Component
public class UploadDocumentDelegateImpl implements UploadDocumentDelegate {

    private ResponseErrorCodeMapper    resolver;

    private LoggerDAO                  logger;

    private CreateDocumentationDAO     createDocumentationDAO;

    private DocumentationServiceMapper mapper;

    /**
     * @param resolver
     * @param logger
     * @param externalRestApiClientDAO
     * @param createCaseEndPoint
     */
    @Autowired
    public UploadDocumentDelegateImpl(ResponseErrorCodeMapper resolver, LoggerDAO logger,
            CreateDocumentationDAO createDocumentationDAO, DocumentationServiceMapper mapper) {
        this.resolver = resolver;
        this.logger = logger;
        this.createDocumentationDAO = createDocumentationDAO;
        this.mapper = mapper;
    }

    public String uploadDocument(CaseDTO caseDto, InputStream fileInByte) throws DocUploadServiceException {
        CreateDocumentationRequestDTO uploadDocumentRequestDTO = mapper.populateCreateDocRequest(caseDto);
        CreateDocumentationResponseDTO createDocumentationResponseDTO = createDocumentationDAO
                .createDocumentation(uploadDocumentRequestDTO, fileInByte);
        return getDocumantationId(createDocumentationResponseDTO);
    }

    private String getDocumantationId(CreateDocumentationResponseDTO createDocumentationResponseDTO) {
        String idGenerated = null;
        if (createDocumentationResponseDTO != null
                && validateResponsecode(createDocumentationResponseDTO.getResponseHeader())
                && validateInformataionContent(createDocumentationResponseDTO.getDocumentationItem())) {

            idGenerated = createDocumentationResponseDTO.getDocumentationItem().getInformationContent().getId();
        } else {
            logTrace("CreateDocumentaitonService return empty Resposne");
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        }

        return idGenerated;
    }

    private boolean validateResponsecode(ResponseHeaderDTO responseHeaderDTO) {
        boolean validResponse = false;
        if (responseHeaderDTO != null && DocUploadConstant.OK.equalsIgnoreCase(responseHeaderDTO.getCmdStatus())
                && DocUploadConstant.SUCCESS.equalsIgnoreCase(responseHeaderDTO.getReturnCode())) {
            validResponse = true;
        } else {
            if (responseHeaderDTO != null && responseHeaderDTO.getResultCondition() != null) {
                logTrace("CreateDocumentaitonService Error: " + responseHeaderDTO.getResultCondition().getReasonCode()
                        + responseHeaderDTO.getResultCondition().getReasonText());
            }
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
        }
        return validResponse;
    }

    private boolean validateInformataionContent(DocumentationItemIdDTO documentationItemIdDTO) {
        boolean validInfoId = false;
        if (documentationItemIdDTO != null && documentationItemIdDTO.getInformationContent() != null
                && !StringUtils.isEmpty(documentationItemIdDTO.getInformationContent().getId())) {
            validInfoId = true;
        } else {
            logTrace("CreateDocumentaitonService return empty id");
            throwDocUploadException(ResponseErrorConstants.FAILED_TO_UPLOAD_FILE);
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
        TraceEvent event = new TraceEvent(DocUploadConstant.CREATE_DOC_EVENT);
        event.setClassName(this.getClass().getName());
        event.setEventId(DocUploadConstant.CREATE_DOC_EVENT);
        event.setMessage(msg);
        logger.logProductionTrace(event);
    }

}
