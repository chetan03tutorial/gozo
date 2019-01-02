package com.lbg.ib.api.sales.common.rest.client;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

public class SalsaRestTemplate extends RestTemplate {

    @Autowired
    private LoggerDAO logger;

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
            final ResponseExtractor<T> responseExtractor) throws RestClientException {

        T results = null;
        try {
            getErrorHandler().toString();
            results = super.doExecute(url, method, requestCallback, new ResponseExtractor<T>() {
                public T extractData(ClientHttpResponse response) throws IOException {
                    final HttpStatus httpStatus = response.getStatusCode();
                    //String dummy = IOUtils.toString(response.getBody(), "UTF-8");
                    logger.traceLog(this.getClass(), response.getBody());
                    
                    if (httpStatus != HttpStatus.OK) {
                        getErrorHandler().handleError(response);
                    }
                    if (responseExtractor != null) {
                        T data = responseExtractor.extractData(response);
                        return data;
                    } else {
                        return null;
                    }
                }
            });

        } catch (RestClientException e) {
            logger.logException(this.getClass(), e);
            throw e;
        }
        return results;
    }

}
