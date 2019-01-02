/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: RetrieveDocumentationResponseDTO
 *
 * Author(s):8735182
 *
 * Date: 28 Mar 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.document;

import java.util.List;

/**
 * @author 8735182 This documentation DTO is used for SOA services to retrieve
 *         service response
 * 
 */
public class RetrieveDocumentationRequestDTO {
    private ContactPointDTO              contactPoint;

    private ServiceRequestDTO            serviceRequest;

    private RetrieveRequestHeaderDTO     requestHeader;

    private List<DocumentationItemIdDTO> documentationItem;

    /**
     * @return the contactPoint
     */
    public ContactPointDTO getContactPoint() {
        return contactPoint;
    }

    /**
     * @param contactPoint
     *            the contactPoint to set
     */
    public void setContactPoint(ContactPointDTO contactPoint) {
        this.contactPoint = contactPoint;
    }

    /**
     * @return the serviceRequest
     */
    public ServiceRequestDTO getServiceRequest() {
        return serviceRequest;
    }

    /**
     * @param serviceRequest
     *            the serviceRequest to set
     */
    public void setServiceRequest(ServiceRequestDTO serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    /**
     * @return the requestHeader
     */
    public RetrieveRequestHeaderDTO getRequestHeader() {
        return requestHeader;
    }

    /**
     * @param requestHeader
     *            the requestHeader to set
     */
    public void setRequestHeader(RetrieveRequestHeaderDTO requestHeader) {
        this.requestHeader = requestHeader;
    }

    /**
     * @return the documentationItem
     */
    public List<DocumentationItemIdDTO> getDocumentationItem() {
        return documentationItem;
    }

    /**
     * @param documentationItem
     *            the documentationItem to set
     */
    public void setDocumentationItem(List<DocumentationItemIdDTO> documentationItem) {
        this.documentationItem = documentationItem;
    }

}
