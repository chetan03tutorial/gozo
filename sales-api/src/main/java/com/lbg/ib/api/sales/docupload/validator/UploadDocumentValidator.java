/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name:   UploadServiceValidator 
 *   
 * Author(s): 1146728
 *  
 * Date: 09 Apr 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.refdata.FileFormatsDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.PartyDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocUploadRefDataServiceMapper;

/**
 * @author 1146728
 * 
 */
@Component
public class UploadDocumentValidator {

    @Autowired
    private DocUploadRefDataServiceMapper docUploadServiceHelper;

    @Autowired
    private ResponseErrorCodeMapper       errorCodeMapper;

    @Autowired
    private HttpServletRequest            httpRequest;

    private int                           uploadFileLimitForDocType;

    private int                           exisitngFileCountForDocType;

    public boolean validateToUpdateScanStatus(CaseDTO caseDTO) {
        boolean isValidUpload = false;
        if (validateCaseDtoWithPartyDtls(caseDTO) && validateCasePartyId(caseDTO) && validUploadDetails(caseDTO)) {
            isValidUpload = true;
        }
        return isValidUpload;

    }

    private boolean validUploadDetails(CaseDTO caseDTO) {
        boolean isUploadflag = false;
        for (PartyDTO partyDTO : caseDTO.getPartyDetails()) {
            if (null != partyDTO && partyDTO.getAttachmentDetails() != null
                    && !partyDTO.getAttachmentDetails().isEmpty()) {
                isUploadflag = true;
            } else {
                throw new DocUploadServiceException(
                        errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_DETAILS));
            }
        }
        return isUploadflag;
    }

    public boolean validateUploadDetails(CaseDTO caseDTO, int actualFileSize, CaseDTO getCaseRessponse)
            throws DocUploadServiceException {
        boolean isValidUpload = false;

        if (validateCaseStatus(getCaseRessponse) && validateCaseDtoWithPartyDtls(caseDTO)
                && validateCasePartyId(caseDTO) && mandatoryCheckForUploadValues(caseDTO, actualFileSize)) {

            isValidUpload = true;
        }
        return isValidUpload;
    }

    /**
     * @param caseDTO
     * @return
     * @throws DocUploadServiceException
     */
    private boolean validateCaseDtoWithPartyDtls(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isPartyDetailsValid = false;
        if (null != caseDTO && null != caseDTO.getPartyDetails()) {
            isPartyDetailsValid = true;
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }
        return isPartyDetailsValid;
    }

    /**
     * @param partyDTO
     * @return
     */
    private boolean validateCasePartyId(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isValidCasePartyId = false;
        if (null != caseDTO && null != caseDTO.getPartyDetails()) {
            for (PartyDTO partyList : caseDTO.getPartyDetails()) {
                if (null != partyList.getCasePartyID()) {
                    isValidCasePartyId = true;
                }
            }
        }
        if (!isValidCasePartyId) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASE_PARTY_ID));
        }
        return isValidCasePartyId;
    }

    /**
     * @param caseDTO
     * @return
     * @throws DocUploadServiceException
     */
    private boolean mandatoryCheckForUploadValues(CaseDTO caseDTO, int actualFileSize)
            throws DocUploadServiceException {
        boolean isUploadValid = true;
        if (null != caseDTO && null != caseDTO.getPartyDetails() && caseDTO.getPartyDetails().size() == 1) {
            isUploadValid = checkForUploadValues(caseDTO.getPartyDetails(), actualFileSize);
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }

        return isUploadValid;
    }

    /**
     * @param partyDetails
     * @return
     */
    private boolean checkForUploadValues(List<PartyDTO> partyDetails, int actualFileSize)
            throws DocUploadServiceException {
        boolean isUploadflag = true;
        for (PartyDTO partyDTO : partyDetails) {
            if (null != partyDTO && null != partyDTO.getAttachmentDetails()
                    && partyDTO.getAttachmentDetails().size() == 1) {
                isUploadflag = uploadValueVerification(partyDTO.getAttachmentDetails(), actualFileSize);
            } else {
                throw new DocUploadServiceException(
                        errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_DETAILS));
            }
        }
        return isUploadflag;
    }

    /**
     * @param partyDTO
     * @return isValidated
     * @throws DocUploadServiceException
     */
    private boolean uploadValueVerification(List<UploadDTO> attachmentDetails, int actualFileSize)
            throws DocUploadServiceException {
        boolean isValid = false;
        for (UploadDTO uploadDTO : attachmentDetails) {
            isValid = checkUploadDetails(uploadDTO, actualFileSize);
        }
        if (!isValid) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_DETAILS));
        }
        return isValid;
    }

    /**
     * @param uploadDTO
     * @return
     */
    private boolean checkUploadDetails(UploadDTO uploadDTO, int actualFileSize) throws DocUploadServiceException {
        boolean isFlag = false;
        if (null != uploadDTO && checkMandatoryFields(uploadDTO) && checkMandatoryDocCode(uploadDTO)) {
            isFlag = uploadDTOValuesComparsion(uploadDTO, actualFileSize);
        }
        return isFlag;
    }

    private boolean checkMandatoryDocCode(UploadDTO uploadDTO) {
        boolean isAvailable = false;
        if (StringUtils.isNotEmpty(uploadDTO.getEvidenceTypeCode())
                && StringUtils.isNotEmpty(uploadDTO.getDocumentCode())) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private boolean checkMandatoryFields(UploadDTO uploadDTO) {
        boolean isAvailable = false;
        if (StringUtils.isNotEmpty(uploadDTO.getFileName())
        /*
         * uncomment it later &&
         * StringUtils.isNotEmpty(uploadDTO.getSrcSessionId())
         */) {
            isAvailable = true;
        }
        return isAvailable;
    }

    /**
     * @param uploadDTO
     * @return validFlag
     * @throws DocUploadServiceException
     */
    private boolean uploadDTOValuesComparsion(UploadDTO uploadDTO, int actualFileSize)
            throws DocUploadServiceException {
        ProcessDTO processDTO = docUploadServiceHelper.getProcessDto();
        boolean validFlag = false;
        if (null != processDTO) {
            validFlag = validateUploadDetailsWithRefData(processDTO, uploadDTO, actualFileSize);
        }
        return validFlag;
    }

    private boolean validateUploadDetailsWithRefData(ProcessDTO processDTO, UploadDTO uploadDTO, int actualFileSize) {
        boolean validFlag = false;
        if (/*
             * splittingUploadDTOvaluesComparsion(processDTO, uploadDTO) &&
             */ validateContentType(processDTO, uploadDTO) && checkFileSize(processDTO, uploadDTO, actualFileSize)) {
            validFlag = true;
        }
        return validFlag;
    }

    private boolean checkFileSize(ProcessDTO processDTO, UploadDTO uploadDTO, int actualFileSize) {
        boolean validFlag = false;
        if (validateFileSizePassed(actualFileSize, uploadDTO.getFileSize())
                && checkFileSizeAllowed(processDTO, actualFileSize)) {
            validFlag = true;
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_FILE_SIZE));
        }
        return validFlag;
    }

    private boolean validateFileSizePassed(int actualFileSize, Integer fileSizePassed) {
        boolean validFlag = false;
        if (fileSizePassed != null) {
            if (actualFileSize <= fileSizePassed.intValue()) {
                validFlag = true;
            }
        } else {
            validFlag = true;
        }
        return validFlag;
    }

    private boolean checkFileSizeAllowed(ProcessDTO processDTO, int actualFileSize) {
        boolean validSize = false;
        if (processDTO.getUploadSizeLimit() != null && actualFileSize <= processDTO.getUploadSizeLimit().intValue()) {
            validSize = true;
        }
        return validSize;
    }

    /**
     * @param uploadDTO
     * @param processDTO
     * @return
     */
    private boolean validateContentType(ProcessDTO processDTO, UploadDTO uploadDTO) throws DocUploadServiceException {
        boolean validContentTye = false;
        if (StringUtils.isNotEmpty(uploadDTO.getContentType()) && null != processDTO.getFileFormat()) {
            validContentTye = checkContentType(uploadDTO, processDTO.getFileFormat());
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CONTENT_TYPE));
        }
        return validContentTye;
    }

    /**
     * @param uploadDTO
     * @param fileFormat
     * @return
     */
    private boolean checkContentType(UploadDTO uploadDTO, List<FileFormatsDTO> fileFormat)
            throws DocUploadServiceException {
        boolean isValidContentType = false;
        for (FileFormatsDTO formatsDTO : fileFormat) {
            if (null != formatsDTO && StringUtils.isNotEmpty(formatsDTO.getContentType())
                    && uploadDTO.getContentType().equals(formatsDTO.getContentType())) {
                isValidContentType = true;
                break;
            }
        }
        if (!isValidContentType) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CONTENT_TYPE));
        }
        return isValidContentType;
    }

    /**
     * @param caseDTO
     * @return
     */
    public boolean validateFileUploadCount(CaseDTO caseDTO, CaseDTO getCaseResponse) throws DocUploadServiceException {
        boolean isFileLimit = false;
        int count = 0;
        ProcessDTO processDTO = docUploadServiceHelper.getProcessDto();
        UploadDTO uploaddto = caseDTO.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails()
                .get(DocUploadConstant.ZERO);
        if (null != getCaseResponse) {
            count = countUploadDetails(getCaseResponse, uploaddto);
        }
        if (count <= processDTO.getDocUploadFileLimit()) {
            isFileLimit = true;
        } else {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.NUMBER_OF_FILES_EXCEEDED));
        }
        return isFileLimit;
    }

    private int countUploadDetails(CaseDTO getCaseResponse, UploadDTO uploaddto) {
        int count = 0;
        exisitngFileCountForDocType = 0;
        if (null != getCaseResponse && null != getCaseResponse.getPartyDetails()) {
            for (PartyDTO partyDTO : getCaseResponse.getPartyDetails()) {
                count += countForEachParty(partyDTO, uploaddto);
            }
        }
        return count;
    }

    private int countForEachParty(PartyDTO partyDto, UploadDTO uploaddto) {
        int count = 0;
        if (null != partyDto && null != partyDto.getAttachmentDetails()) {
            count = partyDto.getAttachmentDetails().size();
            countForDocType(uploaddto, partyDto.getAttachmentDetails());
        }
        return count;
    }

    private void countForDocType(UploadDTO uploaddto, List<UploadDTO> attacheDetailsAvailable) {
        for (UploadDTO uploadDTOavailable : attacheDetailsAvailable) {
            if (uploadDTOavailable.getDocumentCode().equalsIgnoreCase(uploaddto.getDocumentCode())) {
                exisitngFileCountForDocType += 1;
            }
        }
    }

    /**
     * @param isValidString
     * @return
     * @throws DocUploadServiceException
     */
    public boolean validateRequestParameters(String isValidString) throws DocUploadServiceException {
        boolean requestValid = false;
        if (null != isValidString) {
            Pattern pattern = Pattern.compile(DocUploadConstant.ALLOWED_CHARCTERS);
            Matcher ma = pattern.matcher(isValidString);
            requestValid = ma.matches();
        }
        if (!requestValid) {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE));
        }
        return requestValid;
    }

    private boolean validateCaseStatus(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isCaseStatusPresent = false;
        if (caseDTO != null && caseDTO.getCaseStatus() != null
                && caseDTO.getCaseStatus().equalsIgnoreCase(DocUploadConstant.UPLOAD_STATUS_OPEN)) {
            isCaseStatusPresent = true;
        }
        if (!isCaseStatusPresent) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASE_STATUS));
        }
        return isCaseStatusPresent;
    }
}
