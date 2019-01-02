/**
 * 
 */
package com.lbg.ib.api.sales.docupload.dto.document;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

/**
 * @author 8735182
 *
 */
public class RetrieveResponseDTO {
    private Attachment                       file;
    private RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO;

    /**
     * @return the file
     */
    public Attachment getFile() {
        return file;
    }

    /**
     * @param file
     *            the file to set
     */
    public void setFile(Attachment file) {
        this.file = file;
    }

    /**
     * @return the retrieveDocumentationResponseDTO
     */
    public RetrieveDocumentationResponseDTO getRetrieveDocumentationResponseDTO() {
        return retrieveDocumentationResponseDTO;
    }

    /**
     * @param retrieveDocumentationResponseDTO
     *            the retrieveDocumentationResponseDTO to set
     */
    public void setRetrieveDocumentationResponseDTO(RetrieveDocumentationResponseDTO retrieveDocumentationResponseDTO) {
        this.retrieveDocumentationResponseDTO = retrieveDocumentationResponseDTO;
    }

}
