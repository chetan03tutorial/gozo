/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: UploadDocumentDAOImpl
 *
 * Author(s):chandra kachhawaha
 *
 * Date: 01 Oct 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dao.impl;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dao.DocUploadDAO;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessRefDataResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.util.DocuploadUtils;

/**
 * @author chandra kachhawaha
 *
 */
@Component
public class DocUploadDAOImpl implements DocUploadDAO {

    private LoggerDAO                logger;

    private ExternalRestApiClientDAO externalRestApiClientDAO;

    private URL                      refDocUploadEndPoint;

    private ResponseErrorCodeMapper  resolver;

    @Autowired
    public DocUploadDAOImpl(LoggerDAO logger, ExternalRestApiClientDAO externalRestApiClientDAO,
            URL refDocUploadEndPoint, ResponseErrorCodeMapper resolver) {
        super();
        this.logger = logger;
        this.externalRestApiClientDAO = externalRestApiClientDAO;
        this.refDocUploadEndPoint = refDocUploadEndPoint;
        this.resolver = resolver;
    }

    /**
     * Method to retrieve ref data from salsa service
     *
     * @param processCode
     *
     * @param brand
     *            needs to be passed from cache if it is not placed in
     *            httpRequest(during the time of invocation from scheduler)
     *
     * @return DocUploadRefDataForProcess
     */
    public ProcessDTO retrieveDocUploadProcessRefData(String processCode, String brandFromCache) {
        ProcessRefDataResponseDTO docUploadProcessRefDataResponse = null;
        ProcessDTO response = null;
        try {
            // PC: new code added
            URL url = DocuploadUtils.appendURLPath(refDocUploadEndPoint.toString(), processCode);
            String refDataEndPointUrl = "";
            if (null != url && !url.toString().equals(DocUploadConstant.EMPTY_STRING)) {
                refDataEndPointUrl = url.toString();
            }
            // String refDataEndPointUrl = refDocUploadEndPoint.toString() +
            // DocUploadConstant.SLASH_CHAR + processCode

            docUploadProcessRefDataResponse = externalRestApiClientDAO.getRefDataValue(refDataEndPointUrl,
                    ProcessRefDataResponseDTO.class, true, brandFromCache);
            response = validatedResponse(docUploadProcessRefDataResponse);
        } catch (ClassCastException castException) {
            logger.logException(this.getClass(), castException);
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_REF_DATA_SERVICE));
        } catch (URISyntaxException uriSyntaxException) {
            logger.logException(this.getClass(), uriSyntaxException);
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_REF_DATA_SERVICE));
        } catch (MalformedURLException malformedURLException) {
            logger.logException(this.getClass(), malformedURLException);
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_REF_DATA_SERVICE));
        }
        return response;
    }

    private ProcessDTO validatedResponse(ProcessRefDataResponseDTO response) {
        if (response != null && response.getDocUploadRefDataResponse() != null
                && DocUploadConstant.SUCCESS
                        .equalsIgnoreCase(response.getDocUploadRefDataResponse().getServiceResponse())
                && response.getDocUploadRefDataResponse().getDocUploadRefDataForProcess() != null) {
            return response.getDocUploadRefDataResponse().getDocUploadRefDataForProcess();
        } else {
            throw new DocUploadServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_RESPONSE_FORM_REF_DATA_SERVICE));
        }

    }

}
