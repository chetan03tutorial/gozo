/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: UploaDocumentDelegateImpl
 *
 * Author(s):chandra kachhawaha, Kiran Arora
 *
 * Date: 01 Oct 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docupload.dao.DocUploadDAO;
import com.lbg.ib.api.sales.docupload.delegate.DocUploadDelegate;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author sthak4
 *
 *
 */
@Component
public class DocUploadDelegateImpl implements DocUploadDelegate {

    /**
     * Instance of UploadDocumentDAO
     *
     */
    @Autowired
    private DocUploadDAO            docUploadDAO;

    @Autowired
    private LoggerDAO               logger;

    @Autowired
    private ResponseErrorCodeMapper resolver;

    /**
     * Methods to retrieveDocUploadProcessRefData from salsa service
     *
     * @return DocUploadRefTypeDto
     *
     */
    public ProcessDTO retrieveDocUploadProcessRefData(String processCode) throws DocUploadServiceException {

        ProcessDTO responseFromDAO = null;
        try {
            responseFromDAO = docUploadDAO.retrieveDocUploadProcessRefData(processCode, null);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.logException(this.getClass(), illegalArgumentException);
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_REF_DATA_SERVICE));
        }

        return responseFromDAO;
    }

}
