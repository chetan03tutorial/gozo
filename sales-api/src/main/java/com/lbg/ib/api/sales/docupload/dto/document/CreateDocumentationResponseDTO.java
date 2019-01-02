/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: UploadDocResponseDTO
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
public class CreateDocumentationResponseDTO {
    private ResponseHeaderDTO      responseHeader;

    private DocumentationItemIdDTO documentationItem;

    /**
     * @return the responseHeader
     */
    public ResponseHeaderDTO getResponseHeader() {
        return responseHeader;
    }

    /**
     * @param responseHeader
     *            the responseHeader to set
     */
    public void setResponseHeader(ResponseHeaderDTO responseHeader) {
        this.responseHeader = responseHeader;
    }

    /**
     * @return the documentationItem
     */
    public DocumentationItemIdDTO getDocumentationItem() {
        return documentationItem;
    }

    /**
     * @param documentationItem
     *            the documentationItem to set
     */
    public void setDocumentationItem(DocumentationItemIdDTO documentationItem) {
        this.documentationItem = documentationItem;
    }

}
