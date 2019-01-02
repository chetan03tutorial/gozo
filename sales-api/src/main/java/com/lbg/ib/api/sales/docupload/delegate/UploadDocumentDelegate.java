/**
 * 
 */
package com.lbg.ib.api.sales.docupload.delegate;

import java.io.InputStream;

import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 8735182
 *
 */
public interface UploadDocumentDelegate {
    String uploadDocument(CaseDTO caseDto, InputStream fileInByte) throws DocUploadServiceException;
}
