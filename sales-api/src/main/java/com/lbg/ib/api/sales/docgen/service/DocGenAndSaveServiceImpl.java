package com.lbg.ib.api.sales.docgen.service;
/*
Created by Rohit.Soni at 07/05/2018 12:52
*/

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveResponseParty;
import com.lbg.ib.api.sales.docgen.mapper.CreateCaseMapper;
import com.lbg.ib.api.sales.docgen.mapper.RecordMetaDataContentMapper;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.RecordMetaDataContent;
import com.lbg.ib.api.sales.docmanager.service.DocumentMetaContentService;
import com.lbg.ib.api.sales.docupload.delegate.SearchCaseDelegateService;
import com.lbg.ib.api.sales.docupload.domain.Case;
import com.lbg.ib.api.sales.docupload.domain.ServiceResponse;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.mapper.CaseServiceMapper;
import com.lbg.ib.api.sales.docupload.service.CaseDetailsService;
import com.lbg.ib.api.sales.docupload.validator.UploadDocumentValidator;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DocGenAndSaveServiceImpl implements DocGenAndSaveService{

    /**
     * Session object.
     */
    @Autowired
    private SessionManagementDAO session;
    /**
     * Logger object.
     */
    @Autowired
    private LoggerDAO logger;

    @Autowired
    private GenerateDocumentService generateDocumentService;

    @Autowired
    private CaseDetailsService caseDetailsService;

    @Autowired
    SearchCaseDelegateService searchCaseDelegateService;

    @Autowired
    private ResponseErrorCodeMapper errorCodeMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private UploadDocumentValidator uploadDocumentValidator;

    @Autowired
    private CaseServiceMapper caseServiceHelper;

    @Autowired
    private DocumentMetaContentService documentMetaContentService;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private RecordMetaDataContentMapper recordMetaDataContentMapper;

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private CreateCaseMapper createCaseMapper;

    private static final String CONTENT_TYPE = "application/pdf";

    @TraceLog
    public List<DocGenAndSaveResponseParty> generateAndSaveDocument(DocGenAndSaveRequest docGenAndSaveRequest) {
        logger.traceLog(this.getClass(), "Entered generateAndSaveDocument");
        List<DocGenAndSaveResponseParty> docGenAndSaveResponseList = new ArrayList<DocGenAndSaveResponseParty>();
        Map<String, PartyDetails> parties = sessionManager.getAllPartyDetailsSessionInfo();
        if(parties!=null){
            for(Map.Entry<String, PartyDetails> partyEntrySet : parties.entrySet()) {
                String partyOcisId = partyEntrySet.getKey();
                PartyDetails party = partyEntrySet.getValue();
                DocGenAndSaveResponseParty docGenAndSaveResponseParty = new DocGenAndSaveResponseParty();
                logger.traceLog(this.getClass(), "Invoking generateAndSaveDocument");
                final byte[] documentAsByteArray = doGenerateDocument(docGenAndSaveRequest, docGenAndSaveResponseParty, party);
                if((!docGenAndSaveResponseParty.isError())&&(documentAsByteArray!=null)){
                    logger.traceLog(this.getClass(), "Generate document successful. Invoking doCreateCase");
                    final Case enrichedCase = doCreateCase(docGenAndSaveRequest, docGenAndSaveResponseParty, party);
                    if((!docGenAndSaveResponseParty.isError())&&(enrichedCase.getCaseId()!=null)){
                        logger.traceLog(this.getClass(), "Create case was successful. Invoking doUploadDocument");
                        doUploadDocument(documentAsByteArray,docGenAndSaveRequest,docGenAndSaveResponseParty, enrichedCase);
                        if((!docGenAndSaveResponseParty.isError())&&(docGenAndSaveResponseParty.getFileRefId()!=null)){
                            logger.traceLog(this.getClass(), "Document upload was successful with FileRefId: " + docGenAndSaveResponseParty.getFileRefId());
                            logger.traceLog(this.getClass(), "Invoking doRecordCustomerDocumentInfo");
                            doRecordCustomerDocumentInfo(docGenAndSaveResponseParty.getFileRefId(), docGenAndSaveRequest,
                                    docGenAndSaveResponseParty, partyOcisId);
                        }
                    }
                }
                if(party.isPrimaryParty()){
                    docGenAndSaveResponseParty.setPrimaryParty(Boolean.TRUE);
                }
                docGenAndSaveResponseList.add(docGenAndSaveResponseParty);
            }
        }else{
            throw new ServiceException(new ResponseError(ResponseErrorConstants.NO_PARTIES_IN_SESSION, "No parties info retrieved from session"));
        }

        logger.traceLog(this.getClass(), "Exiting generateAndSaveDocument");
        return docGenAndSaveResponseList;
    }

    /**
     * Generate document
     * @param docGenAndSaveRequest
     * @param docGenAndSaveResponseParty
     * @return
     */
    @TraceLog
    private byte[] doGenerateDocument(DocGenAndSaveRequest docGenAndSaveRequest, DocGenAndSaveResponseParty docGenAndSaveResponseParty, PartyDetails party) {
        logger.traceLog(this.getClass(), "Entered doGenerateDocument");
        byte[] document = null;
        try {
            document= generateDocumentService.generateDocument(docGenAndSaveRequest, party);
            if(document==null){
                setResponseForParty(docGenAndSaveResponseParty, Boolean.TRUE, "Error while generating document");
            }
        } catch (Exception ex) {
            logger.logException(this.getClass(),ex);
            logger.traceLog(this.getClass(), "Error while generating document");
            setResponseForParty(docGenAndSaveResponseParty, Boolean.TRUE, "Error while generating document");
        }
        logger.traceLog(this.getClass(), "Exiting doGenerateDocument");
        return document;

    }

    @TraceLog
    private Case doCreateCase(DocGenAndSaveRequest docGenAndSaveRequest, DocGenAndSaveResponseParty docGenAndSaveResponseParty, PartyDetails party){
        logger.traceLog(this.getClass(), "Entering create case");
        Case caseDetails = createCaseMapper.createCaseCreateRequest(docGenAndSaveRequest, party);
        ServiceResponse<Case> createCaseResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String caseDetailsAsJsonString = null;
        try {
            caseDetailsAsJsonString = mapper.writeValueAsString(caseDetails);
            createCaseResponse = caseDetailsService.createCase(caseDetailsAsJsonString);
            if((createCaseResponse==null)||(!createCaseResponse.getSuccess())){
                logger.traceLog(this.getClass(),"Error in create case response: " +createCaseResponse.getError().getMessage());
                setResponseForParty(docGenAndSaveResponseParty, Boolean.TRUE, "Create case was not successful");
            }else{
                logger.traceLog(this.getClass(),"Create case was successful with case id: " +createCaseResponse.getResult().getCaseId());
                caseDetails.setCaseId(createCaseResponse.getResult().getCaseId());
                caseDetails.getPartyDetails().get(0).setCasePartyID(createCaseResponse.getResult().getPartyDetails().get(0).getCasePartyID());
            }
        } catch (IOException ex) {
            logger.logException(this.getClass(),ex);
            logger.traceLog(this.getClass(), "Error while creating request for create case");
            setResponseForParty(docGenAndSaveResponseParty, Boolean.TRUE, "Error while creating request for create case");
        }catch(Exception ex){
            logger.traceLog(this.getClass(), "Error occured while invoking create case service");
            logger.logException(this.getClass(),ex);
            setResponseForParty(docGenAndSaveResponseParty, Boolean.TRUE, "Error occured while invoking create case service");
        }
        logger.traceLog(this.getClass(), "Exiting create case");
        return caseDetails;
    }

    @TraceLog
    private void doUploadDocument(byte[] document, DocGenAndSaveRequest docGenAndSaveRequest, DocGenAndSaveResponseParty docGenAndSaveResponseParty, Case enrichedCase){
        logger.traceLog(this.getClass(), "Entering doUploadDocument");
        CaseDTO response = null;
        try{
            InputStream file = new ByteArrayInputStream(document);
            final CaseDTO caseDto = caseServiceHelper.mapToCaseDTO(enrichedCase);
            caseDto.setCaseId(enrichedCase.getCaseId());
            caseDto.getPartyDetails().get(DocUploadConstant.ZERO).setCasePartyID(enrichedCase.getPartyDetails().get(DocUploadConstant.ZERO).getCasePartyID());
            caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails().get(DocUploadConstant.ZERO).setContentType(CONTENT_TYPE);
            int actualFileSize = file.available() / DocUploadConstant.KB;
            CaseDTO caseSnapshot = (CaseDTO) httpRequest.getSession().getAttribute("createdCase");
            if (uploadDocumentValidator.validateUploadDetails(caseDto, actualFileSize, caseSnapshot)) {
                response = caseDetailsService.uploadProcess(caseDto, caseSnapshot, file, actualFileSize);
            }
            final String fileRefId = response.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails().get(DocUploadConstant.ZERO).getTmpSysFileRefNum();
            docGenAndSaveResponseParty.setFileRefId(fileRefId);
        }catch (Exception e) {
            logger.logException(this.getClass(), e);
            setResponseForParty(docGenAndSaveResponseParty, Boolean.TRUE, "Error occured while invoking upload document");;
        }
        logger.traceLog(this.getClass(), "Exiting doUploadDocument");
    }

    private void doRecordCustomerDocumentInfo(String fileRefId, DocGenAndSaveRequest docGenAndSaveRequest,
                                              DocGenAndSaveResponseParty docGenAndSaveResponseParty,
                                              String partyOcisId){
        logger.traceLog(this.getClass(), "Entering doRecordCustomerDocumentInfo");
        try{
            final RecordMetaDataContent recordMetaDataContent = recordMetaDataContentMapper.mapRecordCustomerDocumentRequest(fileRefId, docGenAndSaveRequest, partyOcisId);
            final CustomerDocumentInfo customerDocumentInfo = recordMetaDataContent.getCustomerDocumentInfo().get(DocUploadConstant.ZERO);
            CustomerDocumentInfoResponse customerDocumentInfoResponse = documentMetaContentService.recordDocumentMetaContent(customerDocumentInfo);
            if(customerDocumentInfoResponse.getIsRecorded()){
                docGenAndSaveResponseParty.setDocumentRecordingSuccessful(Boolean.TRUE);
                docGenAndSaveResponseParty.setError(Boolean.FALSE);
            }else{
                logger.traceLog(this.getClass(), "Recording customer document was successful with message :"+ customerDocumentInfoResponse.getMessage());
            }
        }catch(Exception ex){
            logger.traceLog(this.getClass(), "Error occured while invoking document meta content service");
            logger.logException(this.getClass(), ex);
            setResponseForParty(docGenAndSaveResponseParty,Boolean.TRUE, "Error occured while invoking document meta content service");
            docGenAndSaveResponseParty.setDocumentRecordingSuccessful(Boolean.FALSE);
        }
        logger.traceLog(this.getClass(), "Exiting doRecordCustomerDocumentInfo");
    }

    private void setResponseForParty(DocGenAndSaveResponseParty docGenAndSaveResponseParty, Boolean isError, String errorMessage){
        docGenAndSaveResponseParty.setError(isError);
        docGenAndSaveResponseParty.setErrorMessage(errorMessage);
    }

}
