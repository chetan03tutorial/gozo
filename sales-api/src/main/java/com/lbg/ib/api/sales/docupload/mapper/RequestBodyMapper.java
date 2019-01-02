/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.mapper;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

/**
 * @author 8735182
 *
 */
@Component
public class RequestBodyMapper {

    private ResponseErrorCodeMapper resolver;

    private LoggerDAO               logger;

    private ObjectMapper            objectMapper;

    @Autowired
    public RequestBodyMapper(ObjectMapper objectMapper, LoggerDAO logger, ResponseErrorCodeMapper resolver) {
        this.objectMapper = objectMapper;
        this.resolver = resolver;
        this.logger = logger;
    }

    public <T> T mapRequestBody(String content, Class<T> clazz) throws DocUploadServiceException {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            logger.logException(clazz, e);
            throw new DocUploadServiceException(resolver.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE));
        }
    }

}
