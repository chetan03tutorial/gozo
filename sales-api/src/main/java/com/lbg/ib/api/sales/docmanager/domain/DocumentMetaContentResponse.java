package com.lbg.ib.api.sales.docmanager.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * CWA response object for the DocumentMetaContent
 * 
 * @author 8903735
 *
 */
public class DocumentMetaContentResponse {
    
    private List<DocumentMetaContent> documents;
    
    /**
     * @return the documents
     */
    public List<DocumentMetaContent> getDocuments() {
        return documents;
    }
    
    /**
     * @param documents
     *            the documents to set
     */
    public void setDocuments(List<DocumentMetaContent> documents) {
        this.documents = documents;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
