/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: GetCaseServiceHelper
 * Author(s):8768724
 * Date: 04 Jan 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.mapper;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.domain.Case;
import com.lbg.ib.api.sales.docupload.domain.Document;
import com.lbg.ib.api.sales.docupload.domain.Evidence;
import com.lbg.ib.api.sales.docupload.domain.Party;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.PartyDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 8768724
 *
 */
@Component
public class CaseServiceMapper {

    @Autowired
    private HttpServletRequest httpRequest;

    public CaseServiceMapper() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    public void setUploadedSession(CaseDTO caseDto) {
        for (PartyDTO partyDto : caseDto.getPartyDetails()) {
            if (CollectionUtils.isNotEmpty(partyDto.getAttachmentDetails())) {
                for (UploadDTO uploadDto : partyDto.getAttachmentDetails()) {
                    uploadDto.setSrcSessionId(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER).toString());
                }
            }
        }
    }

    public void setBrand(CaseDTO caseDto) {
        caseDto.setBrand(httpRequest.getAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE).toString());
    }

    public String calculateExpiryDate(Integer defaultExpDate) {
        String expiryDate = DocUploadConstant.EMPTY_STRING;
        if (defaultExpDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DocUploadConstant.DATE_FORMAT_ZULU);
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, defaultExpDate);
            expiryDate = sdf.format(c.getTime());
        }
        return expiryDate;
    }

    public Case mapToCase(CaseDTO caseResponse) {
        Case caseDetails = new Case();
        caseDetails.setBrand(caseResponse.getBrand());
        caseDetails.setCaseId(caseResponse.getCaseId());
        caseDetails.setCasePSF(caseResponse.getCasePSF());
        caseDetails.setCaseReferenceNo(caseResponse.getCaseReferenceNo());
        caseDetails.setCaseStatus(caseResponse.getCaseStatus());
        caseDetails.setCreationDate(caseResponse.getCreationDate());
        caseDetails.setCreatorID(caseResponse.getCreatorID());
        caseDetails.setExpiryDate(caseResponse.getExpiryDate());
        caseDetails.setProcessCode(caseResponse.getProcessCode());
        caseDetails.setTargetBatchReferenceNo(caseResponse.getTargetBatchReferenceNo());
        caseDetails.setUpdateDate(caseResponse.getUpdateDate());
        caseDetails.setPartyDetails(populatePartyDetails(caseResponse.getPartyDetails()));
        return caseDetails;
    }

    private List<Party> populatePartyDetails(List<PartyDTO> parties) {
        Party party;
        List<Party> partyList = new LinkedList<Party>();
        if (null != parties && !parties.isEmpty()) {

            for (PartyDTO partyDto : parties) {
                party = new Party();
                party.setCasePartyID(partyDto.getCasePartyID());
                party.setCreationDate(partyDto.getCreationDate());
                party.setEmailId(partyDto.getEmailId());
                party.setExternalPartyId(partyDto.getExternalPartyId());
                party.setExternalSystemId(partyDto.getExternalSystemId());
                party.setForeName(partyDto.getForeName());
                party.setMobileNumber(partyDto.getMobileNumber());
                party.setPostCode(partyDto.getPostCode());
                party.setSurName(partyDto.getSurName());
                party.setTitle(partyDto.getTitle());
                party.setUpdateDate(partyDto.getUpdateDate());

                Map<String, List<UploadDTO>> evidenceGroup = groupUploadedDocByEvidenceType(
                        partyDto.getAttachmentDetails());
                party.setEvidences(populateEvidence(evidenceGroup));
                partyList.add(party);
            }
        }
        return partyList;
    }

    private List<Evidence> populateEvidence(Map<String, List<UploadDTO>> evidenceGroup) {
        Evidence evidence = null;
        List<Evidence> evidenceList = new LinkedList<Evidence>();
        for (Map.Entry<String, List<UploadDTO>> entry : evidenceGroup.entrySet()) {
            evidence = new Evidence();
            evidence.setEvidenceTypeCode(entry.getKey());
            evidence.setDocuments(populateUploadedDocuments(entry.getValue()));
            evidenceList.add(evidence);
        }
        return evidenceList;
    }

    private List<Document> populateUploadedDocuments(List<UploadDTO> uploadDocList) {

        Document document;
        List<Document> documentList = new LinkedList<Document>();
        for (UploadDTO uploadDto : uploadDocList) {
            document = new Document();
            document.setEvidenceTypeCode(uploadDto.getEvidenceTypeCode());
            document.setContentType(uploadDto.getContentType());
            document.setCreatedBy(uploadDto.getCreatedBy());
            document.setCreatedDate(uploadDto.getCreatedDate());
            document.setDocumentCode(uploadDto.getDocumentCode());
            document.setFileComments(uploadDto.getFileComments());
            document.setFileName(uploadDto.getFileName());
            document.setFileSize(uploadDto.getFileSize());
            document.setOverrideDocument(uploadDto.getOverrideDocument());
            document.setOverrideEvidenceType(uploadDto.getOverrideEvidenceType());
            document.setPreviewCount(uploadDto.getPreviewCount());
            document.setSrcSessionId(uploadDto.getSrcSessionId());
            document.setStatus(uploadDto.getStatus());
            document.setTmpSysFileRefNum(uploadDto.getTmpSysFileRefNum());
            document.setTrgSysFileRefNum(uploadDto.getTrgSysFileRefNum());
            document.setUpdateDate(uploadDto.getUpdateDate());
            document.setUploadedInCurrentSession(uploadDto.isUploadedInCurrentSession());
            document.setUploadSequenceNo(uploadDto.getUploadSequenceNo());
            documentList.add(document);
        }
        return documentList;
    }

    private void isUploadedInSameSession(UploadDTO uploadDto) {
        if (DocUploadConstant.UPLOAD_STATUS_OPEN.equalsIgnoreCase(uploadDto.getStatus())
                && getSessionId().equalsIgnoreCase(uploadDto.getSrcSessionId())) {
            uploadDto.setUploadedInCurrentSession(Boolean.TRUE);
        } else {
            uploadDto.setUploadedInCurrentSession(Boolean.FALSE);
        }
    }

    private Map<String, List<UploadDTO>> groupUploadedDocByEvidenceType(List<UploadDTO> uploadList) {
        Map<String, List<UploadDTO>> evidences = new HashMap<String, List<UploadDTO>>();

        for (UploadDTO uploadDto : uploadList) {
            // Check if the Document has been uploaded in the same session
            // Shiv change
            // isUploadedInSameSession(uploadDto);

            List<UploadDTO> documents = evidences.get(uploadDto.getEvidenceTypeCode());
            if (documents == null) {
                documents = new LinkedList<UploadDTO>();
                evidences.put(uploadDto.getEvidenceTypeCode(), documents);
            }
            documents.add(uploadDto);
        }
        return evidences;
    }

    public CaseDTO mapToCaseDTO(Case caseDetail) {
        CaseDTO caseDto = new CaseDTO();
        caseDto.setBrand(caseDetail.getBrand());
        caseDto.setCaseId(caseDetail.getCaseId());
        caseDto.setCasePSF(caseDetail.getCasePSF());
        caseDto.setCaseReferenceNo(caseDetail.getCaseReferenceNo());
        caseDto.setCaseStatus(caseDetail.getCaseStatus());
        caseDto.setCreationDate(caseDetail.getCreationDate());
        caseDto.setCreatorID(caseDetail.getCreatorID());
        caseDto.setExpiryDate(caseDetail.getExpiryDate());
        caseDto.setPartyDetails(mapToPartyDTO(caseDetail.getPartyDetails()));
        caseDto.setProcessCode(caseDetail.getProcessCode());
        caseDto.setTargetBatchReferenceNo(caseDetail.getTargetBatchReferenceNo());
        caseDto.setUpdateDate(caseDetail.getUpdateDate());

        return caseDto;
    }

    private List<PartyDTO> mapToPartyDTO(List<Party> parties) {
        List<PartyDTO> partyList = new LinkedList<PartyDTO>();

        PartyDTO partyDto;

        for (Party party : parties) {
            partyDto = new PartyDTO();
            partyDto.setCasePartyID(party.getCasePartyID());
            partyDto.setCreationDate(party.getCreationDate());
            partyDto.setEmailId(party.getEmailId());
            partyDto.setExternalPartyId(party.getExternalPartyId());
            partyDto.setExternalSystemId(party.getExternalSystemId());
            partyDto.setForeName(party.getForeName());
            partyDto.setMobileNumber(party.getMobileNumber());
            partyDto.setPostCode(party.getPostCode());
            partyDto.setSurName(party.getSurName());
            partyDto.setTitle(party.getTitle());
            partyDto.setUpdateDate(party.getUpdateDate());
            partyDto.setAttachmentDetails(mapToUploadDTO(party.getEvidences()));
            partyList.add(partyDto);

            // Map<String, List<UploadDTO>> evidenceGroup =
            // groupUploadedDocByEvidenceType(partyDto.getAttachmentDetails(),caseRequest.getSessionId());
            // partyi.setEvidences(populateEvidence(evidenceGroup));

        }

        return partyList;
    }

    private List<UploadDTO> mapToUploadDTO(List<Evidence> evidences) {
        List<UploadDTO> uploadList = null;
        for (Evidence evidence : evidences) {
            uploadList = populateUploadDTO(evidence.getEvidenceTypeCode(), evidence.getDocuments());
        }
        return uploadList;
    }

    private List<UploadDTO> populateUploadDTO(String evidenceType, List<Document> documents) {
        List<UploadDTO> uploadList = new LinkedList<UploadDTO>();
        UploadDTO uploadDto;
        for (Document document : documents) {
            uploadDto = new UploadDTO();
            uploadDto.setContentType(document.getContentType());
            uploadDto.setCreatedBy(document.getCreatedBy());
            uploadDto.setCreatedDate(document.getCreatedDate());
            uploadDto.setDocumentCode(document.getDocumentCode());
            uploadDto.setFileComments(document.getFileComments());
            uploadDto.setFileName(document.getFileName());
            uploadDto.setFileSize(document.getFileSize());
            uploadDto.setOverrideDocument(document.getOverrideDocument());
            uploadDto.setOverrideEvidenceType(document.getOverrideEvidenceType());
            uploadDto.setPreviewCount(document.getPreviewCount());
            uploadDto.setSrcSessionId(document.getSrcSessionId());
            uploadDto.setStatus(document.getStatus());
            uploadDto.setTmpSysFileRefNum(document.getTmpSysFileRefNum());
            uploadDto.setTrgSysFileRefNum(document.getTrgSysFileRefNum());
            uploadDto.setUpdateDate(document.getUpdateDate());
            uploadDto.setUploadedInCurrentSession(document.isUploadedInCurrentSession());
            uploadDto.setUploadSequenceNo(document.getUploadSequenceNo());
            uploadDto.setEvidenceTypeCode(document.getEvidenceTypeCode());
            uploadList.add(uploadDto);
        }
        return uploadList;
    }

    public String getSessionId() {
        return httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER).toString();
    }

    public List<Case> mapToListCaseDTO(List<CaseDTO> caseDTOList) {
        List<Case> caseDtos = new ArrayList<Case>();
        if (CollectionUtils.isNotEmpty(caseDTOList)) {
            for (CaseDTO caseDTO : caseDTOList) {
                caseDtos.add(mapToCase(caseDTO));
            }
            Collections.sort(caseDtos, new DateComparator());
        }
        if (caseDtos.size() >= 18) {
            caseDtos = caseDtos.subList(0, 17);
        }
        return caseDtos;
    }

    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
}
