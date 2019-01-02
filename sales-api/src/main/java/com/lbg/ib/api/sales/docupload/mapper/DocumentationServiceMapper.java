/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: DocumentationServiceMapper
 * Author(s):8768724
 * Date: 18 Apr 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.mapper;

import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.document.*;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.docupload.dto.transaction.UploadDTO;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.dao.constants.DAOConstants;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 8768724
 *
 */
@Component
public class DocumentationServiceMapper {

    private ConfigurationService          configurationService;

    private HttpServletRequest            httRequest;

    private DocUploadRefDataServiceMapper docUploadServiceHelper;

    /**
     * @param configurationService
     * @param httRequest
     */
    @Autowired
    public DocumentationServiceMapper(ConfigurationService configurationService, @Context HttpServletRequest httRequest,
            DocUploadRefDataServiceMapper docUploadServiceHelper) {
        this.configurationService = configurationService;
        this.httRequest = httRequest;
        this.docUploadServiceHelper = docUploadServiceHelper;
    }

    private ContactPointDTO populateContactPoint(String creatorId) {
        ContactPointDTO contactPointDTO = new ContactPointDTO();
        contactPointDTO.setInitialOriginatorId((String) ApplicationRequestContext.get(ApplicationAttribute.IP_ADDRESS));
        contactPointDTO.setInitialOriginatorType(configurationService.getConfigurationValueAsString(
                DAOConstants.CONTACT_POINT_HEADER_DATA, DAOConstants.INITIAL_ORIGINATOR_TYPE));
        contactPointDTO.setContactPointId(configurationService.getConfigurationValueAsString(
                DAOConstants.CONTACT_POINT_ID, httRequest.getAttribute(DocUploadConstant.BRAND_VALUE).toString()));
        contactPointDTO.setContactPointType(configurationService
                .getConfigurationValueAsString(DAOConstants.CONTACT_POINT_DETAILS, DAOConstants.CONTACT_POINT_TYPE));
        contactPointDTO.setApplicationId(configurationService
                .getConfigurationValueAsString(DAOConstants.CONTACT_POINT_HEADER_DATA, DAOConstants.APPLICATION_ID));
        if (creatorId == null || creatorId.equalsIgnoreCase(DocUploadConstant.CUSTOMER)) {
            contactPointDTO.setOperatorType(configurationService
                    .getConfigurationValueAsString(DAOConstants.CONTACT_POINT_HEADER_DATA, DAOConstants.OPERATOR_TYPE));
        } else {
            contactPointDTO.setOperatorType(DocUploadConstant.STAFF);
        }
        return contactPointDTO;
    }

    /* *//**
     * Delete docupload request population
     *
     * @param caseDTO
     * @return DeleteDocumentationRequestDTO
     *//*
     * public DeleteDocumentationRequestDTO
     * populateDeleteDocUploadRequest(CaseDTO caseDto) {
     * DeleteDocumentationRequestDTO deleteDocumentRequestDTO = new
     * DeleteDocumentationRequestDTO(); DocumentationItemIdDTO
     * documentationItem = new DocumentationItemIdDTO();
     * InformationContentIdDTO informationContentIdDTO = new
     * InformationContentIdDTO();
     * informationContentIdDTO.setId(caseDto.getPartyDetails().get(0).
     * getAttachmentDetails() .get(0).getTmpSysFileRefNum());
     * documentationItem.setInformationContent(informationContentIdDTO);
     * deleteDocumentRequestDTO.setDocumentationItem(documentationItem);
     * deleteDocumentRequestDTO.setContactPoint(populateContactPoint(
     * DocUploadConstant.CUSTOMER));
     * deleteDocumentRequestDTO.setServiceRequest(
     * populateServiceRequestForDeleteDocument());
     * deleteDocumentRequestDTO.setRequestHeader(
     * populateRequestHeaderDTO()); return deleteDocumentRequestDTO; }
     */

    private ServiceRequestDTO populateServiceRequestForDeleteDocument() {
        ServiceRequestDTO serviceRequestDTO = new ServiceRequestDTO();
        serviceRequestDTO.setServiceName(DocUploadConstant.DOCUMENTATION_SERVICE);
        serviceRequestDTO.setAction("DeleteDocumentationItem");
        serviceRequestDTO.setFrom((String) ApplicationRequestContext.get(ApplicationAttribute.NODE_IP));

        return serviceRequestDTO;
    }

    private RequestHeaderDTO populateRequestHeaderDTO() {
        RequestHeaderDTO requestHeaderDTO = new RequestHeaderDTO();
        if (httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER) != null) {
            requestHeaderDTO.setId("Doc-" + httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER).toString());
        } else {
            requestHeaderDTO.setId("Doc-" + httRequest.getSession().getId());
        }
        return requestHeaderDTO;
    }

    public RetrieveDocumentationRequestDTO populateRetrieveDocumentRequest(CaseDTO caseDto) {
        RetrieveDocumentationRequestDTO retrieveDocumentRequestDTO = new RetrieveDocumentationRequestDTO();
        DocumentationItemIdDTO documentationItemValue = new DocumentationItemIdDTO();
        InformationContentIdDTO informationContentIdDTO = new InformationContentIdDTO();
        informationContentIdDTO
                .setId(caseDto.getPartyDetails().get(0).getAttachmentDetails().get(0).getTmpSysFileRefNum());
        documentationItemValue.setInformationContent(informationContentIdDTO);
        List<DocumentationItemIdDTO> documentationItem = new ArrayList<DocumentationItemIdDTO>();
        documentationItem.add(documentationItemValue);
        retrieveDocumentRequestDTO.setDocumentationItem(documentationItem);
        retrieveDocumentRequestDTO.setContactPoint(populateContactPoint(caseDto.getCreatorID()));
        retrieveDocumentRequestDTO.setServiceRequest(populateServiceRequestOfRetrieveDoc());
        retrieveDocumentRequestDTO.setRequestHeader(populateRequestHeaderDTOForRetrieve());
        return retrieveDocumentRequestDTO;
    }

    private ServiceRequestDTO populateServiceRequestOfRetrieveDoc() {
        ServiceRequestDTO serviceRequestDTO = new ServiceRequestDTO();
        serviceRequestDTO.setServiceName(DocUploadConstant.DOCUMENTATION_SERVICE);
        serviceRequestDTO.setAction("retrieveDocumentationItems");
        serviceRequestDTO.setFrom((String) ApplicationRequestContext.get(ApplicationAttribute.NODE_IP));
        if (httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER) != null) {
            serviceRequestDTO.setMessageId(httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER).toString());
        } else {
            serviceRequestDTO.setMessageId(httRequest.getSession().getId());
        }
        return serviceRequestDTO;
    }

    private RetrieveRequestHeaderDTO populateRequestHeaderDTOForRetrieve() {
        RetrieveRequestHeaderDTO requestHeaderDTO = new RetrieveRequestHeaderDTO();
        if (httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER) != null) {
            requestHeaderDTO.setId("Doc-" + httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER).toString());
        } else {
            requestHeaderDTO.setId("Doc-" + httRequest.getSession().getId());
        }
        requestHeaderDTO.setExternalApplicationId(configurationService
                .getConfigurationValueAsString(DAOConstants.CONTACT_POINT_HEADER_DATA, DAOConstants.APPLICATION_ID));
        requestHeaderDTO.setFunctionCode(DocUploadConstant.FUN_CODE);
        return requestHeaderDTO;
    }

    public CreateDocumentationRequestDTO populateCreateDocRequest(CaseDTO caseDto) {
        CreateDocumentationRequestDTO createDocumentRequestDTO = new CreateDocumentationRequestDTO();
        DocumentationItemDTO documentationItem = new DocumentationItemDTO();
        DocumentationProfileDTO documentationProfile = new DocumentationProfileDTO();
        UploadDTO uploaDto = caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails()
                .get(DocUploadConstant.ZERO);
        documentationProfile.setName(uploaDto.getFileName());
        documentationProfile.setContentType(uploaDto.getContentType());
        documentationProfile.setDescription(DocUploadConstant.MTOM);
        documentationItem.setDocumentationProfile(documentationProfile);
        documentationItem.setInformationContent(populateInformationContentDTOs(caseDto));
        createDocumentRequestDTO.setDocumentationItem(documentationItem);
        createDocumentRequestDTO.setContactPoint(populateContactPoint(caseDto.getCreatorID()));
        createDocumentRequestDTO.setServiceRequest(populateServiceRequestOfCreateDoc());
        createDocumentRequestDTO.setRequestHeader(populateRequestHeaderDTO());
        return createDocumentRequestDTO;
    }

    private ServiceRequestDTO populateServiceRequestOfCreateDoc() {
        ServiceRequestDTO serviceRequestDTO = new ServiceRequestDTO();
        serviceRequestDTO.setServiceName(DocUploadConstant.DOCUMENTATION_SERVICE);
        serviceRequestDTO.setAction("createDocumentationItem");
        serviceRequestDTO.setFrom((String) ApplicationRequestContext.get(ApplicationAttribute.NODE_IP));

        if (httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER) != null) {
            serviceRequestDTO.setMessageId(httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER).toString());
        } else {
            serviceRequestDTO.setMessageId(httRequest.getSession().getId());
        }

        return serviceRequestDTO;
    }

    private List<InformationContentDTO> populateInformationContentDTOs(CaseDTO caseDto) {
        List<InformationContentDTO> informationContentDTOs = new ArrayList<InformationContentDTO>();
        informationContentDTOs
                .add(popInformationContentDTO(DocUploadConstant.CE_BRAND, DocUploadConstant.SINGLETON_STRING,
                        httRequest.getAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE).toString()));
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_PROCESS_ID,
                DocUploadConstant.SINGLETON_STRING, caseDto.getProcessCode()));
        if (caseDto.getProcessCode() != null) {
            String[] data = caseDto.getProcessCode().split(DocUploadConstant.HYPEN);
            if (data != null && data.length >= 2) {
                informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_CHANNEL,
                        DocUploadConstant.SINGLETON_STRING, data[1]));
                informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_SRC_APP,
                        DocUploadConstant.SINGLETON_STRING, data[0]));
            } else {
                informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_SRC_APP,
                        DocUploadConstant.SINGLETON_STRING, data[0]));
            }
        }
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_CASE_REF_NO,
                DocUploadConstant.SINGLETON_STRING, caseDto.getCaseReferenceNo()));
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_EXPIRY_DATE, "SingletonDateTime",
                caseDto.getExpiryDate()));

        UploadDTO uploadDTO = caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getAttachmentDetails()
                .get(DocUploadConstant.ZERO);
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_CONTENT_TYPE,
                DocUploadConstant.SINGLETON_STRING, uploadDTO.getContentType()));
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_DOC_CLASSIFICATION,
                DocUploadConstant.SINGLETON_STRING, uploadDTO.getEvidenceTypeCode()));
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_DCO_TYPE,
                DocUploadConstant.SINGLETON_STRING, uploadDTO.getDocumentCode()));
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.DOC_TITLE,
                DocUploadConstant.SINGLETON_STRING, uploadDTO.getFileName()));
        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_EXT_REF,
                DocUploadConstant.SINGLETON_STRING, uploadDTO.getSrcSessionId()));
        ProcessDTO processDto = docUploadServiceHelper.getProcessDto();
        if (processDto != null) {
            informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.CE_PROJECT_DATA,
                    DocUploadConstant.SINGLETON_STRING, processDto.getBusinessProcessName()));
        }

        informationContentDTOs.add(popInformationContentDTO(DocUploadConstant.DOCUMENT_CLASS_ID,
                DocUploadConstant.SINGLETON_STRING, DocUploadConstant.DU_SESSIONS));

        informationContentDTOs
                .add(popInformationContentDTO(DocUploadConstant.CE_PARTY_DETAILS, DocUploadConstant.SINGLETON_STRING,
                        caseDto.getPartyDetails().get(DocUploadConstant.ZERO).getCasePartyID().toString()));

        return informationContentDTOs;
    }

    private InformationContentDTO popInformationContentDTO(String name, String type, String value) {
        InformationContentDTO informationContentDTO = new InformationContentDTO();
        informationContentDTO.setName(name);
        informationContentDTO.setType(type);
        informationContentDTO.setValue(value);
        return informationContentDTO;
    }

}
