/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
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
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessPsfDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CasePsfDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.PartyDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.DocUploadRefDataServiceMapper;

/**
 * @author 8735182
 * 
 */
@Component
public class CaseServiceValidator {

    @Autowired
    private DocUploadRefDataServiceMapper docUploadServiceHelper;

    @Autowired
    private ResponseErrorCodeMapper       errorCodeMapper;

    @Autowired
    private HttpServletRequest            httpRequest;

    public static final String            EMAIL         = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9]"
            + "(?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9]{2}(?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";

    public static final String            MOBILE        = "[0-9]{10}";

    private static final String           OUTWARD_REGEX = "(GIR)|(((([A-Z-[QVX]][A-Z-[IJZ]][0-9][ABEHMNPRVWXY])"
            + "|[A-Z-[QVX]][0-9][0-9]?)|(([A-Z-[QVX]][A-Z-[IJZ]][0-9][0-9]?)|(([A-Z-[QVX]][0-9][A-HJKSTUW]?)))))";

    private static final String           INWARD_REGEX  = "[0-9][ABD-HJLNP-UW-Z]{2}";

    /**
     * @param processCdFromReq
     * @return
     * @throws DocUploadServiceException
     */
    public boolean validateProcessCodeWithJwt(String processCdFromReq) throws DocUploadServiceException {
        boolean isProcessCodeValid = false;
        if (processCdFromReq != null && processCdFromReq.equalsIgnoreCase(docUploadServiceHelper.getProcessCode())
                && !DocUploadConstant.EMPTY_STRING.equals(processCdFromReq)) {
            isProcessCodeValid = true;
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PROCESS_CODE));
        }
        return isProcessCodeValid;
    }

    /**
     * @param caseDTO
     * @return
     * @throws DocUploadServiceException
     */
    public boolean validateCaseDtoWithPartyDtls(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isCaseDetailsValid = false;
        if (null != caseDTO && null != caseDTO.getPartyDetails()) {
            isCaseDetailsValid = true;
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }
        return isCaseDetailsValid;
    }

    /**
     * Methods to get case Reference Number If case Reference Number is not set
     * in JWT filter/ empty then
     * 
     * @throws DocUploadServiceException
     * @return boolean
     */
    public boolean validateCaseReferenceNumber(String caseRefNumber) throws DocUploadServiceException {
        String jwtCaseRefNo = null;
        boolean validCaseRefNo = false;
        if (null != httpRequest && null != httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER)) {
            // fetch caseReferenceNumber from HttpRequest attribute
            jwtCaseRefNo = httpRequest.getAttribute(DocUploadConstant.CASE_REFERENCE_NO_HEADER).toString();
            // match the caseReferenceNumber passed in path param to the
            // caserefNo given in jwt
            // token(which was set in HttpServletRequest)
            if (!DocUploadConstant.EMPTY_STRING.equals(jwtCaseRefNo) && null != caseRefNumber
                    && jwtCaseRefNo.equals(caseRefNumber)) {
                validCaseRefNo = true;
            }
        }
        if (!validCaseRefNo) {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASE_REFERENCE_NUMBER));
        }
        return validCaseRefNo;
    }

    /**
     * @param caseDTO
     * @return
     */
    public boolean validationForCreateCase(CaseDTO caseDTO) {
        boolean isValidationPassed = false;
        if (validateCaseDtoWithPartyDtls(caseDTO) && validatePartyDetailsFields(caseDTO)) {
            isValidationPassed = true;
        }
        return isValidationPassed;
    }

    /**
     * @param caseDTO
     * @return
     */
    private boolean validatePartyDetailsFields(CaseDTO caseDTO) {
        boolean isValid = false;
        if (validationForPartyDetails(caseDTO) && validationCheckForCreateCase(caseDTO)) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * @param caseDTO
     * @return
     */
    private boolean validationCheckForCreateCase(CaseDTO caseDTO) {
        boolean isValid = false;
        if (mandatoryCheckForUploadValues(caseDTO) && mandatoryCheckForTitleSurandForeNames(caseDTO)
                && mandatoryCasePsfCheck(caseDTO) && validateCasePsfs(caseDTO)) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * @return
     */
    public boolean isOnline() {
        boolean isOnlineFlow = false;
        ProcessDTO processDTO = docUploadServiceHelper.getProcessDto();
        if (null != processDTO && null != processDTO.getBusinessProcessTypeName()
                && processDTO.getBusinessProcessTypeName().equalsIgnoreCase(DocUploadConstant.ONLINE)) {
            isOnlineFlow = true;
        }
        return isOnlineFlow;
    }

    /**
     * @param caseDTO
     * @return isTileSurandForeNameRequired
     * @throws DocUploadServiceException
     */
    public boolean mandatoryCheckForTitleSurandForeNames(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isTileSurandForeNameRequired = false;
        if (null != caseDTO && null != caseDTO.getPartyDetails()) {
            for (PartyDTO partyDTO : caseDTO.getPartyDetails()) {
                if ((StringUtils.isNotEmpty(partyDTO.getTitle())) && (StringUtils.isNotEmpty(partyDTO.getForeName()))
                        && (StringUtils.isNotEmpty(partyDTO.getSurName()))) {
                    isTileSurandForeNameRequired = true;
                } else {
                    isTileSurandForeNameRequired = false;
                    break;
                }
            }
        }
        if (!isTileSurandForeNameRequired) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }
        return isTileSurandForeNameRequired;
    }

    /**
     * @param caseDTO
     * @return
     * @throws DocUploadServiceException
     */
    public boolean validationForPartyDetails(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isSuccess = false;
        if (null != caseDTO && null != caseDTO.getPartyDetails()) {
            for (PartyDTO partyDTO : caseDTO.getPartyDetails()) {
                if (null != partyDTO) {
                    isSuccess = partyDetailsCheck(partyDTO);
                }
            }
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }
        return isSuccess;
    }

    /**
     * @return
     * @throws DocUploadServiceException
     */
    private boolean partyDetailsCheck(PartyDTO partyDTO) throws DocUploadServiceException {
        boolean isPassed = false;
        if (validationCheckForEmail(partyDTO.getEmailId()) && validationCheckForMobileNo(partyDTO.getMobileNumber())
                && validationCheckForPostCode(partyDTO.getPostCode())) {
            isPassed = true;
        }
        return isPassed;
    }

    /**
     * @param emailId
     * @return
     */
    private boolean validationCheckForEmail(String emailId) throws DocUploadServiceException {
        boolean isValid = false;
        if (StringUtils.isNotEmpty(emailId)) {
            Pattern pattern = Pattern.compile(EMAIL);
            Matcher m = pattern.matcher(emailId);
            isValid = m.matches();
        } else {
            isValid = true;
        }
        if (!isValid) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }
        return isValid;
    }

    /**
     * @param mobileNo
     * @return
     */
    private boolean validationCheckForMobileNo(String mobileNo) throws DocUploadServiceException {
        boolean isMobileNoValid = false;
        if (StringUtils.isNotEmpty(mobileNo)) {
            Pattern pattern = Pattern.compile(MOBILE);
            Matcher ma = pattern.matcher(mobileNo);
            isMobileNoValid = ma.matches();
        } else {
            isMobileNoValid = true;
        }
        if (!isMobileNoValid) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }
        return isMobileNoValid;
    }

    /**
     * @param postCode
     * @return
     */
    private boolean validationCheckForPostCode(String postCode) throws DocUploadServiceException {
        boolean postCodeValid = false;

        if (StringUtils.isNotEmpty(postCode)) {
            String postCodeCheck = postCode.replaceAll(DocUploadConstant.SPACE_REMOVER, DocUploadConstant.EMPTY_STRING);
            postCodeValid = isValidLength(postCodeCheck);
            if (postCode.length() > DocUploadConstant.SIX) {
                postCodeValid = validatePostCodeForSevenCharacters(postCodeCheck);
            } else {
                postCodeValid = validatePostCodeForSixCharacters(postCodeCheck);
            }
            if (!postCodeValid) {
                throw new DocUploadServiceException(
                        errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
            }
        } else {
            postCodeValid = true;
        }

        return postCodeValid;
    }

    /**
     * @param postCode
     * @return
     */
    private boolean isValidLength(String postCode) {
        return postCode.length() >= DocUploadConstant.FIVE && postCode.length() <= DocUploadConstant.SEVEN;
    }

    /**
     * @param postCode
     * @return
     */
    private boolean validatePostCodeForSixCharacters(String postCode) {
        boolean isValid = false;
        if (postCode.length() == DocUploadConstant.SIX) {
            String firstPart = postCode.substring(DocUploadConstant.ZERO, DocUploadConstant.THREE);
            String secondPart = postCode.substring(DocUploadConstant.THREE);
            if (validateFirstPartPostCode(firstPart) && validateSecondPartPostalCode(secondPart)) {
                isValid = true;
            }
        }
        return isValid;
    }

    /**
     * @param postCode
     * @return
     */
    private boolean validatePostCodeForSevenCharacters(String postCode) {
        boolean isValid = false;
        if (postCode.length() == DocUploadConstant.SEVEN) {
            String firstPart = postCode.substring(DocUploadConstant.ZERO, DocUploadConstant.FOUR);
            String secondPart = postCode.substring(DocUploadConstant.FOUR);
            if (validateFirstPartPostCode(firstPart) && validateSecondPartPostalCode(secondPart)) {
                isValid = true;
            }
        }
        return isValid;
    }

    /**
     * @param postCode
     * @return
     */
    private boolean validateFirstPartPostCode(String postCode) {
        boolean firstCode = false;
        Pattern pattern = Pattern.compile(OUTWARD_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher ma = pattern.matcher(postCode);
        firstCode = ma.matches();
        return firstCode;
    }

    /**
     * @param PostalCode
     * @return
     */
    private boolean validateSecondPartPostalCode(String postalCode) {
        boolean secondCode = false;
        Pattern pattern = Pattern.compile(INWARD_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher ma = pattern.matcher(postalCode);
        secondCode = ma.matches();
        return secondCode;

    }

    /**
     * @param caseDTO
     * @return isCaseProcessSpecificRequired
     * @throws DocUploadServiceException
     */

    private boolean mandatoryCasePsfCheck(CaseDTO caseDTO) {
        boolean mandatoryCasePSFpresent = true;
        ProcessDTO processDTO = docUploadServiceHelper.getProcessDto();
        if (null != processDTO && processDTO.getProcessSpecificField() != null) {
            mandatoryCasePSFpresent = isMandatoryCasePsfAvailable(processDTO.getProcessSpecificField(),
                    caseDTO.getCasePSF());
        }
        if (!mandatoryCasePSFpresent) {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASEPSF_DETAILS));
        }
        return mandatoryCasePSFpresent;
    }

    /**
     * @param processPsfDTOs
     * @param casePsfDTOs
     * @return
     */
    private boolean isMandatoryCasePsfAvailable(List<ProcessPsfDTO> processPsfDTOs, List<CasePsfDTO> casePsfDTOs) {
        boolean mandatoryCasePSFpresent = true;
        for (ProcessPsfDTO processPsfDTO : processPsfDTOs) {
            if (processPsfDTO.getMandatoryFlag().equalsIgnoreCase(DocUploadConstant.Y_VALUE)) {
                mandatoryCasePSFpresent = mandatoryCasePSFAvailable(casePsfDTOs, processPsfDTO);
                if (!mandatoryCasePSFpresent) {
                    break;
                }
            }
        }
        return mandatoryCasePSFpresent;
    }

    /**
     * @param casePsfDTOs
     * @param mandatoryProcessPsfDTO
     * @return
     */
    private boolean mandatoryCasePSFAvailable(List<CasePsfDTO> casePsfDTOs, ProcessPsfDTO mandatoryProcessPsfDTO) {
        boolean isAvailable = false;
        if (casePsfDTOs != null) {
            for (CasePsfDTO casepsf : casePsfDTOs) {
                if (mandatoryProcessPsfDTO.getName().equals(casepsf.getCasePSFName())) {
                    isAvailable = true;
                    break;
                }
            }
        }
        return isAvailable;
    }

    /**
     * @param caseDTO
     * @return
     */
    private boolean validateCasePsfs(CaseDTO caseDTO) {
        boolean validCasePsf = false;
        if (caseDTO.getCasePSF() != null) {
            for (CasePsfDTO casePsf : caseDTO.getCasePSF()) {
                validCasePsf = validateForCasePsfNameandValue(casePsf);
            }
        } else {
            validCasePsf = true;
        }
        return validCasePsf;
    }

    /**
     * @param casePsfDTO
     * @return isCasePsfValid
     * @throws DocUploadServiceException
     */
    private boolean validateForCasePsfNameandValue(CasePsfDTO casePsfDTO) throws DocUploadServiceException {
        boolean isCasePsfValid = false;
        ProcessDTO processDTO = docUploadServiceHelper.getProcessDto();
        if (null != processDTO && processDTO.getProcessSpecificField() != null) {
            for (ProcessPsfDTO processPsfDTO : processDTO.getProcessSpecificField()) {
                if (processPsfDTO.getName() != null && processPsfDTO.getName().equals(casePsfDTO.getCasePSFName())) {
                    isCasePsfValid = validateForCasePSFName(processPsfDTO, casePsfDTO);
                    break;
                }
            }
        }
        if (!isCasePsfValid) {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASEPSF_DETAILS));
        }
        return isCasePsfValid;
    }

    /**
     * @param processPsfDTO
     * @param casePsfDTO
     * @return
     */
    private boolean validateForCasePSFName(ProcessPsfDTO processPsfDTO, CasePsfDTO casePsfDTO) {
        boolean isCasePsfValid = false;
        if (casePsfDTO.getCasePSFValue().length() == processPsfDTO.getSize()
                && Pattern.matches(processPsfDTO.getFormat(), casePsfDTO.getCasePSFValue())) {
            isCasePsfValid = true;
        }
        return isCasePsfValid;
    }

    /**
     * @param caseDTO
     * @return isUploadValid
     * @throws DocUploadServiceException
     */
    public boolean mandatoryCheckForUploadValues(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isUploadValid = true;
        if (null != caseDTO && null != caseDTO.getPartyDetails()) {
            isUploadValid = checkForUploadValues(caseDTO.getPartyDetails());
        }

        return isUploadValid;
    }

    /**
     * @param partyDetails
     * @return
     */
    private boolean checkForUploadValues(List<PartyDTO> partyDetails) {
        boolean isUploadflag = true;
        for (PartyDTO partyDTO : partyDetails) {
            if (null != partyDTO && null != partyDTO.getAttachmentDetails()) {
                isUploadflag = uploadValueVerification(partyDTO.getAttachmentDetails());
                if (!isUploadflag) {
                    break;
                }
            }
        }
        return isUploadflag;
    }

    /**
     * @param partyDTO
     * @return isValidated
     * @throws DocUploadServiceException
     */
    private boolean uploadValueVerification(List<UploadDTO> attachmentDetails) throws DocUploadServiceException {
        boolean isValidated = false;
        for (UploadDTO uploadDTO : attachmentDetails) {
            isValidated = checkUploadDetails(uploadDTO);
            if (!isValidated) {
                break;
            }
        }
        if (!isValidated) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_DETAILS));
        }
        return isValidated;
    }

    /**
     * @param uploadDTO
     * @return
     */
    private boolean checkUploadDetails(UploadDTO uploadDTO) {
        boolean isFlagValid = false;
        if (null != uploadDTO && (StringUtils.isNotEmpty(uploadDTO.getEvidenceTypeCode()))
                && (StringUtils.isNotEmpty(uploadDTO.getDocumentCode()))) {
            // isFlagValid = uploadDTOValuesComparsion(uploadDTO);
            isFlagValid = true;
        }
        return isFlagValid;
    }

    /**
     * @param caseDTO
     * @return
     * @throws DocUploadServiceException
     */
    public boolean validateSubmitCaseDto(CaseDTO caseDTO) throws DocUploadServiceException {
        boolean isCaseDetailsValid = false;

        if (null != caseDTO && null != caseDTO.getCaseReferenceNo()
                && validateCaseReferenceNumber(caseDTO.getCaseReferenceNo())) {
            if (null != caseDTO.getPartyDetails()) {
                for (PartyDTO party : caseDTO.getPartyDetails()) {
                    isCaseDetailsValid = validateUplaodSequence(party);
                }
            } else {
                isCaseDetailsValid = true;
            }
        } else {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASE_REFERENCE_NUMBER));
        }
        return isCaseDetailsValid;
    }

    /**
     * @param PartyDTO
     * @return boolean
     * @throws DocUploadServiceException
     */
    public boolean validateUplaodSequence(PartyDTO partyDTO) throws DocUploadServiceException {
        boolean isCaseDetailsValid = false;
        if (null != partyDTO.getCasePartyID() && null != partyDTO.getAttachmentDetails()) {
            for (UploadDTO upload : partyDTO.getAttachmentDetails()) {
                if (null != upload && null != upload.getUploadSequenceNo()) {
                    isCaseDetailsValid = true;
                } else {
                    throw new DocUploadServiceException(
                            errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_SEQUENCE_NO));
                }
            }
        } else {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_PARTY_DETAILS));
        }

        return isCaseDetailsValid;
    }

    /**
     * @param CaseDTO,
     *            CaseDTO
     * @return boolean
     * @throws DocUploadServiceException
     */
    public boolean validateGetCaseDetails(CaseDTO getCaseResponse, CaseDTO caseDto) {
        boolean validPartyId = false;
        boolean validUploadSequenceNo = false;
        PartyDTO requestPartyDetails = caseDto.getPartyDetails().get(0);
        UploadDTO requestAttachmentDetails = requestPartyDetails.getAttachmentDetails().get(0);
        for (PartyDTO partyDto : getCaseResponse.getPartyDetails()) {
            if (null != partyDto && partyDto.getCasePartyID().equals(requestPartyDetails.getCasePartyID())) {
                validPartyId = true;
                validUploadSequenceNo = validateAttachmentDetails(partyDto, requestAttachmentDetails);
                break;
            }
        }

        if (!validPartyId) {
            throw new DocUploadServiceException(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CASE_PARTY_ID));
        }

        return validUploadSequenceNo;

    }

    /**
     * @param PartyDTO,
     *            UploadDTO
     * @return boolean
     * @throws DocUploadServiceException
     */
    private boolean validateAttachmentDetails(PartyDTO partyDto, UploadDTO requestAttachmentDetails) {
        boolean validAttachmentDetails = false;
        for (UploadDTO uploadDto : partyDto.getAttachmentDetails()) {
            if (null != uploadDto
                    && uploadDto.getUploadSequenceNo().equals(requestAttachmentDetails.getUploadSequenceNo())) {
                validAttachmentDetails = validateTmpSysfileRef(uploadDto.getTmpSysFileRefNum(),
                        requestAttachmentDetails.getTmpSysFileRefNum());
                break;
            }
        }
        if (!validAttachmentDetails) {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_UPLOAD_SEQUENCE_NO));
        }
        return validAttachmentDetails;

    }

    private boolean validateTmpSysfileRef(String fileRefFromGetCase, String fileRefInReq) {
        boolean validTmpsysfileRefNo = false;
        if (fileRefFromGetCase != null && fileRefFromGetCase.equals(fileRefInReq)) {
            validTmpsysfileRefNo = true;
        } else {
            throw new DocUploadServiceException(
                    errorCodeMapper.resolve(ResponseErrorConstants.INVALID_TMP_SYS_FILE_REF_NUM));
        }
        return validTmpsysfileRefNo;
    }

}
