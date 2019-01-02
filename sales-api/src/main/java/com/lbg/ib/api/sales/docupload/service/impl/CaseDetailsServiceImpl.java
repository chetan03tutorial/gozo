/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: GetCaseServiceImpl
 * Author(s):8768724
 * Date: 04 Jan 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.service.impl;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.docupload.delegate.*;
import com.lbg.ib.api.sales.docupload.domain.Case;
import com.lbg.ib.api.sales.docupload.domain.ServiceResponse;
import com.lbg.ib.api.sales.docupload.dto.transaction.*;
import com.lbg.ib.api.sales.docupload.mapper.CaseServiceMapper;
import com.lbg.ib.api.sales.docupload.mapper.DocUploadRefDataServiceMapper;
import com.lbg.ib.api.sales.docupload.mapper.RequestBodyMapper;
import com.lbg.ib.api.sales.docupload.service.CaseDetailsService;
import com.lbg.ib.api.sales.docupload.validator.CaseServiceValidator;
import com.lbg.ib.api.sales.docupload.validator.DocumentValidator;
import com.lbg.ib.api.sales.docupload.validator.UploadDocumentValidator;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.config.exceptions.ConfigurationServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

/**
 * @author 8768724
 *
 */
@Component
public class CaseDetailsServiceImpl implements CaseDetailsService {

    @Autowired
    private CaseServiceValidator          validator;

    @Autowired
    private DocUploadRefDataServiceMapper serviceHelper;

    @Autowired
    private ConfigurationService          configurationService;

    @Autowired
    private RequestBodyMapper             requestBodyMappingHelper;

    @Autowired
    private ResponseErrorCodeMapper       errorCodeMapper;

    @Autowired
    private HttpServletRequest            httpRequest;

    @Autowired
    private DocumentValidator             documentValidator;

    @Autowired
    private PreviewDocumentDelegate       previewDocumentDelegate;

    @Autowired
    private UpdateCaseDelegate            updateCaseDelegate;

    @Autowired
    private LoggerDAO                     logger;

    @Autowired
    private UploadDocumentDelegate        uploadDocumentDelegate;

    @Autowired
    private UploadDocumentValidator       uploadDocumentValidator;

    @Autowired
    private CaseDelegate                  caseService;

    @Autowired
    private CaseServiceMapper             caseServiceHelper;

    @Autowired
    SearchCaseDelegateService searchCaseDelegateService;

    public static final SimpleDateFormat  ZULUFORMATTER = new SimpleDateFormat(DocUploadConstant.DATE_FORMAT_ZULU);

    private static final String RESUME_APPLICATION_NAME = "RESUME_APP";

    public ServiceResponse<Case> createCase(CaseDTO caseDto)  {
        CaseDTO caseDtoResponse = null;
        try{
            if (validator.validationForCreateCase(caseDto)) {
                if (validator.validationForCreateCase(caseDto)) {
                    // To-do Change it to colleague. Need business
                    // confirmation
                    // about if there would be any case for which it can be
                    // something else

                    if (StringUtils.isEmpty(caseDto.getCreatorID())) {
                        caseDto.setCreatorID(DocUploadConstant.CUSTOMER);
                    } else if (!caseDto.getCreatorID().equalsIgnoreCase(DocUploadConstant.CUSTOMER)) {
                        throw new DocUploadServiceException(
                                errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CREATOR_ID));
                    }
                    caseServiceHelper.setBrand(caseDto);
                    // caseServiceHelper.setUploadedSession(caseDto);
                    Integer defaultExpDate = serviceHelper.getProcessDto().getDefaultExpiryPeriod();
                    caseDto.setExpiryDate(caseServiceHelper.calculateExpiryDate(defaultExpDate));

                    CreateCaseRequestDTO createCaseRequestDTO = new CreateCaseRequestDTO();
                    createCaseRequestDTO.setCaseDetails(caseDto);
                    caseDtoResponse = caseService.createCase(createCaseRequestDTO);
                }
            }
        }catch (DocUploadServiceException ex) {
            return ServiceResponse.withError(ex);
        }

        httpRequest.getSession().setAttribute("createdCase", caseDtoResponse);

        return ServiceResponse.withResult(caseServiceHelper.mapToCase(caseDtoResponse));

    }

    /*
     *
     * @see com.lbg.ib.api.sales.docupload.service.CaseDetailsService#createCase(java.lang.String)
     * The objective of the function is to first try creating the case. If creation is not successful then search Case and then pass the response back to the user.
     */
    public ServiceResponse<Case> createCase(String requestBody) throws DocUploadServiceException {
        Case caseDetails = requestBodyMappingHelper.mapRequestBody(requestBody, Case.class);
        CaseDTO caseDto = caseServiceHelper.mapToCaseDTO(caseDetails);
        ServiceResponse<Case> response = createCase(caseDto);
        CaseDTO caseDtoResponse = null;
        if(!response.getSuccess()){
            List<CaseDTO> casesList = null;
            if(caseDto!=null){
                DAOResponse<CaseResponseDTO> caseResponse = searchCaseDelegateService
                        .searchCaseDeleteService(caseDto.getCaseReferenceNo(),caseDto.getProcessCode());
                casesList = caseResponse.getResult() != null ? caseResponse.getResult().getCaseDetails() : null;
                if (!CollectionUtils.isEmpty(casesList)){
                    caseDtoResponse = casesList.get(0);
                    httpRequest.getSession().setAttribute("createdCase", caseDtoResponse);
                    response = ServiceResponse.withResult(caseServiceHelper.mapToCase(caseDtoResponse));
                }else{
                    return response;
                }
            }
        }
        return response;
    }

    public Response previewDocument(String fileNetRefId) {
        Response response = null;
        logger.traceLog(this.getClass(), "FileNetRefId : " + fileNetRefId);
        try {
            CaseDTO caseDto = getPreviewDocRequest(fileNetRefId);
            if (documentValidator.validateDocumentService(caseDto, Boolean.TRUE)) {
                Attachment file = previewDocumentDelegate.retrieveDocumentation(caseDto);

                response = convertFilesToByes(file, response);
                response.getMetadata().putSingle(HttpHeaders.CONTENT_TYPE, file.getContentType());
            }
        } catch (DocUploadServiceException exp) {

            logger.logException(this.getClass(), exp);
            response = respond(Status.OK, exp.getResponseError());
        }

        return response;
    }

    public Response convertFilesToByes(Attachment file, Response response) {
        try {
            if (file != null && file.getDataHandler() != null) {
                InputStream stream = file.getDataHandler().getInputStream();
                byte[] imageBytes = IOUtils.toByteArray(stream);
                stream.close();
                return respond(Status.OK, imageBytes);
            } else {
                throw new DocUploadServiceException(
                        errorCodeMapper.resolve(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE));
            }
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE));
        }
    }

    public ServiceResponse<Case> uploadDocument(String requestBody, MultipartBody multipartBody) {
        /*
         * ServiceResponse<Case> caseFromCreateCase=null; try{
         * caseFromCreateCase = createCase(requestBody); }catch
         * (DocUploadServiceException ex) { return
         * ServiceResponse.withError(ex); } Integer caseId=
         * caseFromCreateCase.getResult().getCaseId();
         */

        Case caseDetails = requestBodyMappingHelper.mapRequestBody(requestBody, Case.class);
        CaseDTO caseDto = caseServiceHelper.mapToCaseDTO(caseDetails);
        // caseDto.setCaseId(caseId);

        // caseServiceHelper.setUploadedSession(caseDto);
        CaseDTO response = null;
        try {

            List<Attachment> attachments = multipartBody.getAllAttachments().subList(DocUploadConstant.ONE,
                    multipartBody.getAllAttachments().size());
            InputStream file = manipulateFileAttached(attachments);

            int actualFileSize = file.available() / DocUploadConstant.KB;
            /*
             * Case getCaseResponse = caseFromCreateCase.getResult(); CaseDTO
             * caseSnapshot = caseServiceHelper.mapToCaseDTO(getCaseResponse);
             */
            CaseDTO caseSnapshot = (CaseDTO) httpRequest.getSession().getAttribute("createdCase");
            if (uploadDocumentValidator.validateUploadDetails(caseDto, actualFileSize, caseSnapshot)) {
                response = uploadProcess(caseDto, caseSnapshot, file, actualFileSize);
            }

            return ServiceResponse.withResult(caseServiceHelper.mapToCase(response));
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE));
        } catch (ConfigurationServiceException e) {
            logger.logException(this.getClass(), e);
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE));
        } catch (DocUploadServiceException ex) {
            // ex.printStackTrace();
            return ServiceResponse.withError(ex);
        }

    }

    public String futureDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 20);
        return ZULUFORMATTER.format(cal.getTime());
    }

    public CaseDTO uploadProcess(CaseDTO caseDto, CaseDTO getCaseResponse, InputStream file, int actualFileSize) {
        CaseDTO response = null;
        if (uploadDocumentValidator.validateFileUploadCount(caseDto, getCaseResponse)) {
            caseDto.setProcessCode(serviceHelper.getProcessCode());
            // caseDto.setExpiryDate(getCaseResponse.getExpiryDate()); //
            // "2027-05-27T00:00:00Z"
            caseDto.setExpiryDate(getCaseResponse != null ? getCaseResponse.getExpiryDate() : futureDate());
            String srcSessionId = httpRequest.getSession().getId();
            caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails().get(DocUploadConstant.ZERO)
                    .setSrcSessionId(srcSessionId);

            String docId = uploadDocumentDelegate.uploadDocument(caseDto, file);
            caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails().get(DocUploadConstant.ZERO)
                    .setTmpSysFileRefNum(docId);
            if (caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails().get(DocUploadConstant.ZERO)
                    .getFileSize() == null) {
                caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails().get(DocUploadConstant.ZERO)
                        .setFileSize(actualFileSize);
            }
            /*
             * caseDto= prepareForUpdate(caseDto,getCaseResponse); response =
             * updateCaseDelegate.updateCaseDetails(caseDto,
             * ConfigSectionNameConstants.OPERATION_TYPE_UPLOAD);
             */

            response = caseDto;
        }

        return response;
    }

    /*
     * private CaseDTO getCaseDetail(CaseDTO casedto) { CaseDTO getCaseResponse
     * = null; if (casedto != null) { SearchCaseRequestDTO searchCaseDTO = new
     * SearchCaseRequestDTO(); if (casedto.getCaseId() != null) {
     * searchCaseDTO.setCaseId(casedto.getCaseId()); }
     * searchCaseDTO.setCaseReferenceNo(casedto.getCaseReferenceNo());
     * searchCaseDTO.setCaseStatus(casedto.getCaseStatus()); getCaseResponse =
     * caseService.retrieveCase(searchCaseDTO); } return getCaseResponse;
     *
     * }
     */

    /*
     * private CaseDTO prepareForUpdate(CaseDTO caseDto, CaseDTO
     * getCaseResponse){ if(caseDto.getCaseId()==null){
     * caseDto.setCaseId(getCaseResponse.getCaseId()); }
     * if(caseDto.getCaseStatus()==null){
     * caseDto.setCaseStatus(getCaseResponse.getCaseStatus()); }
     * if(caseDto.getPartyDetails().get(0).getCasePartyID()==null){
     * caseDto.getPartyDetails().get(0).setCasePartyID(getCaseResponse.
     * getPartyDetails().get(0).getCasePartyID()); }
     * if(caseDto.getCreatorID()==null){
     *
     * caseDto.setCreatorID(getCaseResponse.getCreatorID()); } return caseDto; }
     */

    public InputStream manipulateFileAttached(List<Attachment> attachments) {
        Integer seqOrder = 0;
        InputStream wholeFile = null;
        List<InputStream> fileArray = new ArrayList<InputStream>();
        if (!attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                populatefileSegementsInArray(attachment, fileArray, seqOrder);
                seqOrder = seqOrder + 1;
            }
            if (fileArray.size() == attachments.size()) {
                wholeFile = combineFiles(fileArray);
            }
        } else {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE));
        }
        return wholeFile;

    }

    private void populatefileSegementsInArray(Attachment attachment, List<InputStream> fileSegmentsInOrder,
            Integer seqOrder) {
        if (attachment.getDataHandler() != null) {
            try {
                InputStream uploadedInputStream = attachment.getDataHandler().getInputStream();
                if (uploadedInputStream != null) {
                    fileSegmentsInOrder.add(uploadedInputStream);
                }

            } catch (IOException e) {
                logger.logException(this.getClass(), e);
                throw new DocUploadServiceException(
                        errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public InputStream combineFiles(List<InputStream> fileSegments) {
        InputStream uploadedInputStream = null;
        Enumeration<InputStream> enumeration = new IteratorEnumeration(fileSegments.iterator());
        uploadedInputStream = new SequenceInputStream(enumeration);
        return uploadedInputStream;
    }

    /*
     * Method not being used
     private MultipartBody populateErrorResponse(ResponseErrorVO errorResponse) {
        MultipartBody response = null;
        List<Attachment> atts = new LinkedList<Attachment>();
        atts.add(new Attachment(DocUploadConstant.PREVIEW_JSON_PART, MediaType.APPLICATION_JSON_VALUE, errorResponse));
        response = new MultipartBody(atts);
        return response;
    }
     */


    /*
     * public Case submitAndGetCase(String submitCaseRequest) throws
     * DocUploadServiceException { CaseDTO response = null;
     *
     * CaseDTO caseDto =
     * requestBodyMappingHelper.mapRequestBody(submitCaseRequest,
     * CaseDTO.class);
     *
     * if (validator.validateSubmitCaseDto(caseDto)) { // validate and set
     * process code form jwt token
     * caseDto.setProcessCode(serviceHelper.getProcessCode()); response =
     * submitCaseDelegate.submitCaseAndGetData(caseDto); }
     *
     * return caseServiceHelper.mapToCase(response);
     *
     * }
     */

    /*
     * private SearchCaseRequestDTO createSearchCaseRequest(String
     * caseReferenceNumber, String status, boolean isCustomerJourney) {
     * List<CasePsfDTO> casePsfList = null; String caseStatus = status;
     * SearchCaseRequestDTO searchCaseRequest = new SearchCaseRequestDTO(); if
     * (caseStatus == null || DocUploadConstant.EMPTY_STRING.equals(status)) {
     * caseStatus =
     * configurationService.getConfigurationValueAsString(DocUploadConstant.
     * CASE_STATUS_SECTION, DocUploadConstant.CASE_STATUS_OPEN); }
     * searchCaseRequest.setCaseStatus(caseStatus);
     *
     *
     * if (caseId != null) { searchCaseRequest.setCaseId(caseId); }
     *
     *
     * if (!isCustomerJourney) { casePsfList =
     * setProcessSpecificDetails(caseReferenceNumber); if
     * (CollectionUtils.isEmpty(casePsfList)) {
     * searchCaseRequest.setCaseReferenceNo(caseReferenceNumber); }
     * searchCaseRequest.setCasePSF(casePsfList); } else {
     * searchCaseRequest.setCaseReferenceNo(caseReferenceNumber); }
     * searchCaseRequest.setProcessCode(serviceHelper.getProcessCode()); return
     * searchCaseRequest; }
     */

    /*
     * public UpdateCaseRequestDTO createUpdateCaseRequest(CaseDTO caseDto) {
     * UpdateCaseRequestDTO updateCaseRequestDTO = new UpdateCaseRequestDTO();
     * List<CaseDTO> caselist = new ArrayList<CaseDTO>(); caselist.add(caseDto);
     * updateCaseRequestDTO.setCaseDetails(caselist);
     *
     * updateCaseRequestDTO.setOperationType(configurationService.
     * getConfigurationValueAsString(
     * ConfigSectionNameConstants.UPDATE_OPERATION_TYPE,
     * ConfigSectionNameConstants.OPERATION_TYPE_SUBMIT));
     *
     * return updateCaseRequestDTO; }
     */

    /*
     * private List<CasePsfDTO> setProcessSpecificDetails(String
     * caseReferenceNumber) { CasePsfDTO casePsfDTO = null; List<CasePsfDTO>
     * casePsfList = null; ProcessDTO process = serviceHelper.getProcessDto();
     * List<ProcessPsfDTO> processSpecifics = process.getProcessSpecificField();
     * for (ProcessPsfDTO processSpecific : processSpecifics) { if
     * (processSpecific.getSearchField().equalsIgnoreCase(DocUploadConstant.
     * Y_VALUE) ||
     * processSpecific.getSearchField().equalsIgnoreCase(DocUploadConstant.YES))
     * { casePsfList = new LinkedList<CasePsfDTO>(); casePsfDTO = new
     * CasePsfDTO(); casePsfDTO.setCasePSFName(processSpecific.getName());
     * casePsfDTO.setCasePSFValue(caseReferenceNumber);
     * casePsfList.add(casePsfDTO); break; } } return casePsfList; }
     */

    public void setCaseServiceHelper(CaseServiceMapper caseServiceHelper) {
        this.caseServiceHelper = caseServiceHelper;
    }

    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }

    private CaseDTO getPreviewDocRequest(String fileNetRefId) {
        CaseDTO caseDto = new CaseDTO();
        List<PartyDTO> partyDetails = new ArrayList<PartyDTO>();
        PartyDTO partyDetail = new PartyDTO();
        List<UploadDTO> attachmentDetails = new ArrayList<UploadDTO>();
        UploadDTO uploadDto = new UploadDTO();
        uploadDto.setTmpSysFileRefNum(fileNetRefId);
        attachmentDetails.add(uploadDto);
        partyDetail.setAttachmentDetails(attachmentDetails);
        partyDetails.add(partyDetail);
        caseDto.setPartyDetails(partyDetails);
        return caseDto;
    }

}
