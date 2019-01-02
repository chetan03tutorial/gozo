/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: UploadDocumentDAO
 *
 * Author(s):chandra kachhawaha
 *
 * Date: 01 Oct 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dao;

import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;

/**
 * @author chandra kachhawaha
 * 
 */
public interface DocUploadDAO {

    /**
     * Methods to retrieve docUploadRefData for file format from salsa service
     * 
     * @param retrieveDocUploadProcessRefDataRequest
     * 
     * @return DocUploadRefDataForProcess
     * 
     */
    ProcessDTO retrieveDocUploadProcessRefData(String processCode, String brandFromCache);

}
