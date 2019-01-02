package com.lbg.ib.api.sales.docmanager.service;

import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;

/**
 * Interface for the retrieval for document meta content
 * @author 8903735
 *
 */
public interface DocumentMetaContentService {

    /**
     * Returns the document meta content response
     * @param partyId
     * @return {@link DocumentMetaContentResponse}
     */
    public DocumentMetaContentResponse retrieveDocumentMetaContent(String partyId);

    /**
     * Returns the document meta content response after Recording
     * @param customerDocumentInfo
     * @return {@link CustomerDocumentInfoResponse}
     */
    public CustomerDocumentInfoResponse recordDocumentMetaContent(CustomerDocumentInfo customerDocumentInfo);

}
