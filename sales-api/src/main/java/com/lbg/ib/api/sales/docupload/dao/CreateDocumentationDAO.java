/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ExternalRestApiClientDAOImpl
 *
 * Author(s):8735182
 *
 * Date: 29 Dec 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dao;

import java.io.InputStream;

import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationRequestDTO;
import com.lbg.ib.api.sales.docupload.dto.document.CreateDocumentationResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 8735182
 * 
 */
public interface CreateDocumentationDAO {

    CreateDocumentationResponseDTO createDocumentation(CreateDocumentationRequestDTO uploadDocumentRequestDTO,
            InputStream fileInByte) throws DocUploadServiceException;
}
