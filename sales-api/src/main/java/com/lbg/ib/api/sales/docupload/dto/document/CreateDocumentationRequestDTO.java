/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: CreateDocumentationRequestDTO
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 18 Mar 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.document;

/**
 * @author 1146728 This documentation DTO is used for SOA services to retrieve
 *         service response
 * 
 */
public class CreateDocumentationRequestDTO {

    private ContactPointDTO      contactPoint;

    private ServiceRequestDTO    serviceRequest;

    private RequestHeaderDTO     requestHeader;

    private DocumentationItemDTO documentationItem;

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
    public RequestHeaderDTO getRequestHeader() {
        return requestHeader;
    }

    /**
     * @param requestHeader
     *            the requestHeader to set
     */
    public void setRequestHeader(RequestHeaderDTO requestHeader) {
        this.requestHeader = requestHeader;
    }

    /**
     * @return the documentationItem
     */
    public DocumentationItemDTO getDocumentationItem() {
        return documentationItem;
    }

    /**
     * @param documentationItem
     *            the documentationItem to set
     */
    public void setDocumentationItem(DocumentationItemDTO documentationItem) {
        this.documentationItem = documentationItem;
    }

}
