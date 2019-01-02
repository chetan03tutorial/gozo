/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: DocumentationItemRequestDTO
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 18 Mar 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.document;

import java.util.List;

/**
 * @author 1146728 This documentation DTO is used for SOA services to retrieve
 *         service response
 * 
 */
public class DocumentationItemDTO {

    private DocumentationProfileDTO     documentationProfile;

    private List<InformationContentDTO> informationContent;

    /**
     * @return the informationContent
     */
    public List<InformationContentDTO> getInformationContent() {
        return informationContent;
    }

    /**
     * @param informationContent
     *            the informationContent to set
     */
    public void setInformationContent(List<InformationContentDTO> informationContent) {
        this.informationContent = informationContent;
    }

    /**
     * @return the documentationProfile
     */
    public DocumentationProfileDTO getDocumentationProfile() {
        return documentationProfile;
    }

    /**
     * @param documentationProfile
     *            the documentationProfile to set
     */
    public void setDocumentationProfile(DocumentationProfileDTO documentationProfile) {
        this.documentationProfile = documentationProfile;
    }

}
