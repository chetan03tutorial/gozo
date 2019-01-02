package com.lbg.ib.api.sales.docmanager.dao;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.Constants.RetrieveDocumentErrorConstant;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;
import com.lbg.ib.api.sales.docmanager.mapper.DocumentMetaContentRequestMapper;
import com.lbg.ib.api.sales.docmanager.mapper.DocumentMetaContentResponseMapper;
import com.lbg.ib.api.sales.docmanager.util.DocumentManagerHeaderUtility;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.ErrorInfo;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.ResponseHeader;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.ResultCondition;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.www.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.GENERAL_EXCEPTION;
import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;


/**
 * This class interacts with SOA to get the Document meta content
 *
 * @author 8903735
 *
 */
@Component
public class DocumentMetaContentDAOImpl implements DocumentMetaContentDAO {

    private static final String                SERVICE_ACTION        = "retrieveDocumentMetaContent";

    @Autowired
    private DocumentMetaContentRequestMapper   requestMapper;

    @Autowired
    private DocumentMetaContentResponseMapper  responseMapper;

    @Autowired
    private DocumentationManagerServiceLocator documentationManagerServiceLocator;

    @Autowired
    private DocumentationManager               documentationManager;

    // different JNDI for retrieve document
    @Autowired
    private DocumentationManager               retrieveDocumentationManager;

    @Autowired
    private DocumentManagerHeaderUtility       headerUtility;

    @Autowired
    private DAOExceptionHandler daoExceptionHandler;

    @Autowired
    private LoggerDAO logger;

    private static final String                RECORD_SERVICE_ACTION = "DocumentationManagerService";

    private static final String ERROR_MESSAGE_RETRIEVE_DOCUMENT_META_CONTENT = "Error while retrieve document meta content.";

    private static final String  ERROR_MESSAGE_RECORD_DOCUMENT_META_CONTENT = "Error while record document meta content.";

    /**
     * This method gets the document meta content from SOA and maps the response
     * to domain response.
     *
     * @param ocisId
     * @return {@link DAOResponse}
     */
    @TraceLog
    public DAOResponse<DocumentMetaContentResponse> retrieveDocumentMetaContent(String ocisId) {
        logger.traceLog(this.getClass(), "Retrieve documents for ocis id - " + ocisId);
        DAOError daoError = null;
        DAOResponse<DocumentMetaContentResponse> response = null;
        try {
            RetrieveDocumentMetaContentResponse documentMetaContentFromSOA = retrieveDocumentMetaContentFromSOA(
                    ocisId);

            daoError = validateResponse(documentMetaContentFromSOA);

            if (daoError != null) {
                logger.logError(daoError.getErrorCode(), daoError.getErrorMessage(), this.getClass());
                response = withError(daoError);
            } else {
                DocumentMetaContentResponse documentMetaContentResponse = responseMapper
                        .prepareDocumentMetaContentResponse(documentMetaContentFromSOA);
                response = withResult(documentMetaContentResponse);
            }
        } catch (Exception e) {
            daoError = daoExceptionHandler.handleException(e, this.getClass(), "retrieveDocumentMetaContent", ocisId);
            response = withError(daoError);
        }
        return response;
    }

    /**
     * Validates the response from the DocumentationManagerService
     *
     * @param documentMetaContentResponse
     * @return daoError
     */
    private DAOResponse.DAOError validateResponse(RetrieveDocumentMetaContentResponse documentMetaContentResponse) {
        DAOResponse.DAOError error = null;
        if (null == documentMetaContentResponse) {
            error = new DAOResponse.DAOError(GENERAL_EXCEPTION, ERROR_MESSAGE_RETRIEVE_DOCUMENT_META_CONTENT);
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
        } else {
            ResponseHeader responseHeader = documentMetaContentResponse.getResponseHeader();
            // is this understanding correct
            if (responseHeader != null) {
                ResultCondition resultConditions = responseHeader.getResultConditions();
                if (null != resultConditions) {
                    ResultCondition[] extraConditions = resultConditions.getExtraConditions();
                    error = checkForExtraConditions(extraConditions);

                    if (error == null) {
                        error = checkForResultCondition(resultConditions);
                    }
                }
            }
        }
        return error;

    }

    /**
     * Validates the response from the DocumentationManagerService
     *
     * @param documentMetaContentResponse
     * @return daoError
     */
    private DAOError validateRecordDocumentResponse(RecordDocumentMetaContentResponse documentMetaContentResponse) {
        DAOError error = null;
        if (null == documentMetaContentResponse) {
            error = new DAOError(GENERAL_EXCEPTION, ERROR_MESSAGE_RECORD_DOCUMENT_META_CONTENT);
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
        } else {
            ResponseHeader responseHeader = documentMetaContentResponse.getResponseHeader();
            // is this understanding correct
            if (responseHeader != null) {
                ResultCondition resultConditions = responseHeader.getResultConditions();
                if (null != resultConditions) {
                    ResultCondition[] extraConditions = resultConditions.getExtraConditions();
                    error = checkForExtraConditions(extraConditions);

                    if (error == null) {
                        error = checkForResultCondition(resultConditions);
                    }
                }
            }
        }
        return error;

    }

    private DAOError checkForResultCondition(ResultCondition resultCondition) {
        DAOError error = null;
        if (StringUtils.isNotBlank(resultCondition.getReasonCode())) {
            logger.logError(resultCondition.getReasonCode().toString(), resultCondition.getReasonText(),
                    this.getClass());
            error = new DAOResponse.DAOError(resultCondition.getReasonCode().toString(),
                    resultCondition.getReasonText());
        }
        return error;
    }

    private DAOResponse.DAOError checkForExtraConditions(ResultCondition[] extraConditions) {
        DAOError error = null;
        if (ArrayUtils.isNotEmpty(extraConditions)) {
            for (ResultCondition extraCondition : extraConditions) {
                if (extraCondition != null && StringUtils.isNotBlank(extraCondition.getReasonCode())) {
                    String errorCode = extraCondition.getReasonCode();
                    String reasonText = extraCondition.getReasonText();
                    logger.logError(errorCode.toString(), reasonText, this.getClass());
                    if (RetrieveDocumentErrorConstant.RETRIEVE_ERROR_CODES.contains(errorCode)) {
                        error = new DAOResponse.DAOError(errorCode, reasonText);
                        break;
                    }
                }
            }
        }
        return error;
    }

    /**
     * This method interacts with SOA and gets the meta content for documents
     *
     * @param ocisId
     * @return {@link RetrieveDocumentMetaContentResponse}
     * @throws ErrorInfo
     * @throws RemoteException
     */
    public RetrieveDocumentMetaContentResponse retrieveDocumentMetaContentFromSOA(String ocisId)
            throws RemoteException, ErrorInfo {
        headerUtility.setupAndGetDataHandler(documentationManagerServiceLocator, SERVICE_ACTION);
        RetrieveDocumentMetaContentRequest retrieveDocumentMetaContentRequest = requestMapper
                .prepareRetrieveDocumentMetaContentRequest(ocisId);
        RetrieveDocumentMetaContentResponse documentMetaContent = retrieveDocumentationManager
                .retrieveDocumentMetaContent(retrieveDocumentMetaContentRequest);
        return documentMetaContent;
    }

    @TraceLog
    public DAOResponse<CustomerDocumentInfoResponse> recordDocumentMetaContentFromSOA(
            CustomerDocumentInfo customerDocumentInfo) {
        DAOError daoError = null;
        DAOResponse<CustomerDocumentInfoResponse> daoResponse = null;
        logger.traceLog(this.getClass(), "Record customer documents to OCIS.");
        try {
            headerUtility.setupAndGetDataHandler(documentationManagerServiceLocator, RECORD_SERVICE_ACTION);
            RecordDocumentMetaContentRequest recordDocumentMetaContentRequest = requestMapper
                    .prepareRecordDocumentMetaContentRequest(customerDocumentInfo);
            logger.traceLog(this.getClass(), "Send the request to documentationManager service");
            RecordDocumentMetaContentResponse response = documentationManager
                    .recordDocumentMetaContent(recordDocumentMetaContentRequest);

            daoError = validateRecordDocumentResponse(response);
            if (daoError != null) {
                logger.logError(daoError.getErrorCode(), daoError.getErrorMessage(), this.getClass());
                daoResponse = withError(daoError);
            } else {
                daoResponse = withResult(new CustomerDocumentInfoResponse(customerDocumentInfo.getOcisId(),
                        Constants.RecordContant.SUCCESS_MSG, true));
            }

        } catch (Exception e) {
            logger.logException(this.getClass(), e);
            daoError = daoExceptionHandler.handleException(e, this.getClass(), "recordDocumentMetaContentFromSOA",
                    customerDocumentInfo);
            daoResponse = withError(daoError);
        }
        return daoResponse;
    }

}
