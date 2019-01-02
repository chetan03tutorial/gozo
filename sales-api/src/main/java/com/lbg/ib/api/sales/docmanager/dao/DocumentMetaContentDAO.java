package com.lbg.ib.api.sales.docmanager.dao;


import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.ErrorInfo;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lloydstsb.www.RetrieveDocumentMetaContentResponse;

import java.rmi.RemoteException;

/**
 * DAO interface which interacts with SOA to retrieve/update the document meta content
 * @author 8903735
 *
 */
public interface DocumentMetaContentDAO {
    
    /**
     * This method gets the document meta content from SOA and maps the response to domain response.
     * @param ocisId
     * @return {@link DAOResponse}
     */
    public DAOResponse<DocumentMetaContentResponse> retrieveDocumentMetaContent(String ocisId);
    
    public RetrieveDocumentMetaContentResponse retrieveDocumentMetaContentFromSOA(String ocisId)
            throws RemoteException, ErrorInfo;
    
    public DAOResponse<CustomerDocumentInfoResponse> recordDocumentMetaContentFromSOA(
            CustomerDocumentInfo customerDocumentInfo);
    
}
