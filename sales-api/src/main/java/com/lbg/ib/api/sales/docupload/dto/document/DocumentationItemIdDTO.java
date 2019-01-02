/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: DocumentationItemResponseDTO
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
public class DocumentationItemIdDTO {

    private InformationContentIdDTO informationContent;

    /**
     * @return the informationContent
     */
    public InformationContentIdDTO getInformationContent() {
        return informationContent;
    }

    /**
     * @param informationContent
     *            the informationContent to set
     */
    public void setInformationContent(InformationContentIdDTO informationContent) {
        this.informationContent = informationContent;
    }

}
