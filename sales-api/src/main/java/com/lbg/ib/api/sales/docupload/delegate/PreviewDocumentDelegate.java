/**
 * 
 */
package com.lbg.ib.api.sales.docupload.delegate;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.lbg.ib.api.sales.docupload.dto.transaction.CaseDTO;

/**
 * @author 8735182
 *
 */
public interface PreviewDocumentDelegate {
    Attachment retrieveDocumentation(CaseDTO caseDto);
}
