package com.lbg.ib.api.sales.common.rest.client;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.error.ResponseErrorFromOcis;
import com.lbg.ib.api.sales.common.exception.PaoServiceException;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @author cshar8
 */
@Component
public class SalsaRestApiErrorHandler implements ResponseErrorHandler {
    
    private static final String     EMPTY_STRING = "";
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
        final ResponseErrorFromOcis errorResponse;
        try {
            String result = IOUtils.toString(clientHttpResponse.getBody(), "UTF-8");
            logger.traceLog(this.getClass(), result);
            errorResponse = jsonResolver.resolve(result, ResponseErrorFromOcis.class);
            if (errorResponse != null
                    && (errorResponse.getErrMsg() != null || EMPTY_STRING.equals(errorResponse.getErrMsg()))) {
                logger.logError(errorResponse.getMessage(), errorResponse.getErrMsg(), this.getClass());
                throw new PaoServiceException(resolver.customResolve(
                        ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR, errorResponse.getErrMsg()));
            } else {
                throw new InvalidFormatException(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR);
            }
        } catch (InvalidFormatException ex) {
            logger.logError(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR,
                    "Cannot case Json into ResponseError class", this.getClass());
            logger.logException(this.getClass(), ex);
            throw new PaoServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR));
        } catch (IOException ex) {
            logger.logError(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR,
                    "cannot read error response from rest service", this.getClass());
            logger.logException(this.getClass(), ex);
            throw new PaoServiceException(
                    resolver.resolve(ResponseErrorConstants.INVALID_EXTERNAL_EXCEPTION_FORMAT_ERROR));
            
        }
    }
    
    private boolean isSuccessfulResponse(ClientHttpResponse clientHttpResponse) throws IOException {
        return HttpStatus.Series.SUCCESSFUL.equals(clientHttpResponse.getStatusCode().series());
    }
}
