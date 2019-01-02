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

package com.lbg.ib.api.sales.common.rest.handler;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 8735182
 * 
 */
public class ExternalRestApiErrorHandler implements ResponseErrorHandler {

    private ObjectMapper            objectMapper;

    private ResponseErrorCodeMapper resolver;

    private boolean                 isDefHeaderReq;

    /**
     * @param objectMapper
     * @param resolver
     * @param isDefHeaderReq
     */
    public ExternalRestApiErrorHandler(ObjectMapper objectMapper, ResponseErrorCodeMapper resolver,
            boolean isDefHeaderReq) {
        super();
        this.objectMapper = objectMapper;
        this.resolver = resolver;
        this.isDefHeaderReq = isDefHeaderReq;
    }

    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return !isSuccessfulResponse(clientHttpResponse);
    }

    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        if (isDefHeaderReq) {
            final ResponseErrorVO errorResponse;
            errorResponse = objectMapper.readValue(clientHttpResponse.getBody(), ResponseErrorVO.class);
            throw new DocUploadServiceException(errorResponse);
        } else {
            throw new DocUploadServiceException(resolver.resolve(ResponseErrorConstants.EXTERNAL_SERVICE_DOWN));
        }
    }

    private boolean isSuccessfulResponse(ClientHttpResponse clientHttpResponse) throws IOException {
        return HttpStatus.Series.SUCCESSFUL.equals(clientHttpResponse.getStatusCode().series());
    }
}
