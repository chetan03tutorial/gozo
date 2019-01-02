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

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * @author 8735182 This documentation DTO is used for SOA services to retrieve
 *         service response
 * 
 */
public class RetrieveDocumentationResponseDTO {
    private ResponseHeaderDTO    responseHeader;

    private DocumentationItemDTO documentationItem;

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

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
