/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: UploaDocumentDelegate
 *
 * Author(s):chandra kachhawaha, Kiran Arora
 *
 * Date: 01 Oct 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate;

import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author chandra kachhawaha
 * 
 */

public interface DocUploadDelegate {

    /**
     * Methods to retrieveDocUploadProcessRefData from salsa service
     * 
     * @return DocUploadRefTypeDto
     * 
     */

    ProcessDTO retrieveDocUploadProcessRefData(String processCode) throws DocUploadServiceException;

}
