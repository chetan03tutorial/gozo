package com.lbg.ib.api.sales.docmanager.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Bean class for the document meta content
 * 
 * @author 8903735
 *
 */
public class DocumentMetaContent {
    
    private String documentReferenceIndex;
    
    private String documentType;
    
    private String additionalInfo;
    
    /**
     * @return the documentReferenceIndex
     */
    public String getDocumentReferenceIndex() {
        return documentReferenceIndex;
    }
    
    /**
     * @param documentReferenceIndex
     *            the documentReferenceIndex to set
     */
    public void setDocumentReferenceIndex(String documentReferenceIndex) {
        this.documentReferenceIndex = documentReferenceIndex;
    }
    
    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }
    
    /**
     * @param documentType
     *            the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
    /**
     * @return the additionalInfo
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    
    /**
     * @param additionalInfo
     *            the additionalInfo to set
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
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
