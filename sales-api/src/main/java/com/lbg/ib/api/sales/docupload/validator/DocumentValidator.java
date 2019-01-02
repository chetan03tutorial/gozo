/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name:   DocumentValidator 
 *   
 * Author(s): 1146728
 *  
 * Date: 09 Apr 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
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
public class DocumentValidator {

    @Autowired
    private DocUploadRefDataServiceMapper docUploadServiceHelper;

    @Autowired
    private ResponseErrorCodeMapper       errorCodeMapper;

    public boolean validateDocumentService(CaseDTO caseDTO, boolean isPreview) {
        boolean isPreviewServiceValid = false;
        if (uploadFieldsValidation(caseDTO, isPreview)) {
            isPreviewServiceValid = true;
        }
        return isPreviewServiceValid;
    }

    /**
     * @param caseDTO
     * @return
     * @throws DocUploadServiceException
     */
    public boolean validateCaseDtoWithPartyDtls(CaseDTO caseDTO) throws DocUploadServiceException {
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
    public boolean validateCasePartyId(CaseDTO caseDTO) throws DocUploadServiceException {
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
    public boolean uploadFieldsValidation(CaseDTO caseDTO, boolean isPreview) throws DocUploadServiceException {
        boolean isValidField = false;
        if (null != caseDTO && null != caseDTO.getPartyDetails()) {
            for (PartyDTO partyDTO : caseDTO.getPartyDetails()) {
                if (null != partyDTO && null != partyDTO.getAttachmentDetails()) {
                    isValidField = isUploadFieldsVaild(partyDTO.getAttachmentDetails(), isPreview);
                }
            }
        }

        return isValidField;

    }

    /**
     * @param uploadList
     * @return
     */
    private boolean isUploadFieldsVaild(List<UploadDTO> uploadList, boolean isPreview)
            throws DocUploadServiceException {
        boolean isUploadFieldValid = false;
        for (UploadDTO uploadDTO : uploadList) {
            if (isPreview) {
                isUploadFieldValid = checkForPreviewService(uploadDTO);
            }
            if (!isPreview) {
                isUploadFieldValid = checkForDeleteService(uploadDTO);
            }
        }
        return isUploadFieldValid;
    }

    /**
     * @return
     * @throws DocUploadServiceException
     */
    private boolean checkForPreviewService(UploadDTO uploadDTO) throws DocUploadServiceException {
        boolean isPreviewValid = false;
        if (checkForFileTmpSys(uploadDTO)) {
            isPreviewValid = true;
        }
        return isPreviewValid;
    }

    /**
     * @param uploadDTO
     * @return
     * @throws DocUploadServiceException
     */
    private boolean checkForDeleteService(UploadDTO uploadDTO) throws DocUploadServiceException {
        boolean isDeleteValid = false;
        if (checkForUploadSequenceNo(uploadDTO) && checkForFileTmpSys(uploadDTO)) {
            isDeleteValid = true;
        }
        return isDeleteValid;
    }

    /**
     * @param uploadList
     * @return
     */
    private boolean checkForFileTmpSys(UploadDTO uploadDTO) throws DocUploadServiceException {
        boolean isValid = false;
        if (null != uploadDTO && StringUtils.isNotEmpty(uploadDTO.getTmpSysFileRefNum())) {
            isValid = Pattern.matches(DocUploadConstant.ALLOWED_SPECIAL_SOME_CHARACTERS,
                    uploadDTO.getTmpSysFileRefNum());
        }
        if (!isValid) {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_TMP_SYS_FILE_REF_NUM));
        }
        return isValid;
    }

    /**
     * @param uploadList
     * @return
     */
    private boolean checkForUploadSequenceNo(UploadDTO uploadDTO) throws DocUploadServiceException {
        boolean fieldValid = false;
        if (null != uploadDTO && null != uploadDTO.getUploadSequenceNo()) {
            fieldValid = true;
        }
        if (!fieldValid) {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_SEQUENCE_NO));
        }
        return fieldValid;
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
        return requestValid;
    }

}
