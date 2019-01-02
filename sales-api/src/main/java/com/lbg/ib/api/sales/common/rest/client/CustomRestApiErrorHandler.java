package com.lbg.ib.api.sales.common.rest.client;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.exception.PaoServiceException;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author dbhatt
 *
 * This is a customised error handling class which will handle the Exception and print out the error
 */
@Component
public class CustomRestApiErrorHandler implements ResponseErrorHandler {

    @Autowired
    private GalaxyErrorCodeResolver resolver;
    @Autowired
    private JsonBodyResolver        jsonResolver;
    @Autowired
    private LoggerDAO logger;

    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return !isSuccessfulResponse(clientHttpResponse);
    }

    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        String result = IOUtils.toString(clientHttpResponse.getBody(), "UTF-8");
        logger.traceLog(this.getClass(), "Error Response of CustomRestApiErrorHandler : "+result);
        HashMap<String, Object> errorResponse = new HashMap<String, Object>();
        try {
            errorResponse = jsonResolver.resolve(result, HashMap.class);
        } catch (InvalidFormatException ex) {
            logger.logError(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR,
                    "Cannot case Json into ResponseError class", this.getClass());
            logger.logException(this.getClass(), ex);
            throw new PaoServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR));
        }
        logger.logError(errorResponse.toString(), errorResponse.toString(), this.getClass());
        throw new ServiceException(resolver.customResolve(ResponseErrorConstants.SERVICE_EXCEPTION, errorResponse.toString()));
    }

    private boolean isSuccessfulResponse(ClientHttpResponse clientHttpResponse) throws IOException {
        return HttpStatus.Series.SUCCESSFUL.equals(clientHttpResponse.getStatusCode().series());
    }
}
