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

package com.lbg.ib.api.sales.dao.restclient;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.lbg.ib.api.sales.common.exception.PaoServiceException;
import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.client.SalsaRestTemplate;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.enums.RequestHeaderDetails;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.handler.ExternalRestApiErrorHandler;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

/**
 * @author 8735182
 */
@Component
public class ExternalRestApiClientDAOImpl implements ExternalRestApiClientDAO {

    private RestTemplate restTemplate;

    private LoggerDAO logger;

    private HttpServletRequest httRequest;

    private ResponseErrorCodeMapper resolver;

    private ObjectMapper objectMapper;

    private ConfigurationService configurationService;

    @Autowired
    private GalaxyErrorCodeResolver galaxyResolver;

    @Autowired
    private SalsaRestTemplate customRestTemplate;

    /**
     * @param restTemplate
     * @param logger
     * @param httRequest
     * @param resolver
     * @param objectMapper
     */
    @Autowired
    public ExternalRestApiClientDAOImpl(RestTemplate restTemplate, LoggerDAO logger,
            @Context HttpServletRequest httRequest, ResponseErrorCodeMapper resolver, ObjectMapper objectMapper,
            ConfigurationService configurationService) {
        this.restTemplate = restTemplate;
        this.logger = logger;
        this.httRequest = httRequest;
        this.resolver = resolver;
        this.objectMapper = objectMapper;
        this.configurationService = configurationService;
    }

    public <X> X get(String url, Class<X> responseType, boolean isDefHeaderReqr) throws DocUploadServiceException {
        return request(url, HttpMethod.GET, responseType, null, isDefHeaderReqr, null);
    }

    public <X> X customGet(RestContext context, Class<X> responseType)
            throws DocUploadServiceException {
        return exchangeRequest(context, customRestTemplate, HttpMethod.GET, responseType, null);
    }

    public <X, Y> X post(String url, Class<X> responseType, Y requestBody, boolean isDefHeaderReqr)
            throws DocUploadServiceException {
        return request(url, HttpMethod.POST, responseType, requestBody, isDefHeaderReqr, null);
    }

    public <X> X customPost(RestContext context, Class<X> responseType)
            throws DocUploadServiceException {
        return exchangeRequest(context, customRestTemplate, HttpMethod.POST, responseType, null);
    }

    public <X, Y> X request(String url, HttpMethod method, Class<X> responseType, Y requestBody, boolean isDefHeaderReq,
            String brandFromCache) throws DocUploadServiceException {

        try {
            HttpEntity<Y> entity = setRequestBody(requestBody, isDefHeaderReq, brandFromCache);
            restTemplate.setErrorHandler(new ExternalRestApiErrorHandler(objectMapper, resolver, isDefHeaderReq));

            printRequest(requestBody, url);
            final ResponseEntity<X> forEntity = restTemplate.exchange(url, method, entity, responseType);
            return forEntity.getBody();
        } catch (HttpMessageConversionException e) {
            logger.logException(this.getClass(), e);
            throwDocUploadException(ResponseErrorConstants.EXTERNAL_SERVICE_DOWN);
        } catch (ResourceAccessException e) {
            logger.logException(this.getClass(), e);
            throwDocUploadException(ResponseErrorConstants.EXTERNAL_SERVICE_UNAVAILABLE);
        } catch (RestClientException e) {
            logger.logException(this.getClass(), e);
            throwDocUploadException(ResponseErrorConstants.EXTERNAL_SERVICE_DOWN);
        }
        /*
         * to do add more exceptions after getting from Salsa and DP eg: datanotFoundeXception for
         * refdatarequestBody
         */
        return null;
    }

    @TraceLog
    private <X, Y> X exchangeRequest(RestContext context, SalsaRestTemplate restTemplate, HttpMethod method,
            Class<X> responseType, Y requestBody) {
        try {
            String url = buildUrlWithParameters(context);
            HttpEntity<Object> entity = buildEntity(context);
            final ResponseEntity<X> forEntity = restTemplate.exchange(url, method, entity, responseType);
            return forEntity.getBody();
        } catch (HttpMessageConversionException e) {
            logger.logException(this.getClass(), e);
            throw new PaoServiceException(galaxyResolver.resolve(com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.INTERNAL_SERVER_ERROR));
        } catch (ResourceAccessException e) {
            logger.logException(this.getClass(), e);
            return null;
        } catch (RestClientException e) {
            logger.logException(this.getClass(), e);
            throw new PaoServiceException(galaxyResolver.resolve(com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.INTERNAL_SERVER_ERROR));
        }
    }

    private HttpEntity<Object> buildEntity(RestContext context) {
        return new HttpEntity<Object>(context.requestBody(), context.headers());
    }

    public <Y> void printRequest(Y requestBody, String url) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonRequest = mapper.writeValueAsString(requestBody);
            logger.traceLog(this.getClass(), "JSON Request::" + jsonRequest + "\nURL::" + url);
        } catch (JsonGenerationException e) {
            logger.logException(this.getClass(), e);
        } catch (JsonMappingException e) {
            logger.logException(this.getClass(), e);
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
        }
    }

    public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
    }

    private MultiValueMap<String, String> generateDefaultHeader() {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_CHANNEL.code(),
                httRequest.getAttribute(DocUploadConstant.BRAND_VALUE).toString()); // IBH

        if (httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER) != null) {

            setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_SESSION_ID.code(),
                    httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER).toString());
        }
        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_DEVICE_IP.code(), httRequest.getRemoteAddr());

        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_XBRAND.code(),
                httRequest.getAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE).toString()); // HAL

        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_CONTENT_TYPE.code(), MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * @param errorCode
     * @throws DocUploadServiceException
     */
    private void throwDocUploadException(String errorCode) throws DocUploadServiceException {
        throw new DocUploadServiceException(resolver.resolve(errorCode));
    }

    /**
     * set request body adds salsaheader if it is of Salsa service
     * @param requestBody
     * @param isDefHeaderReqr
     * @return
     */
    public <Y> HttpEntity<Y> setRequestBody(Y requestBody, boolean isDefHeaderReqr, String brandFromCache) {
        HttpEntity<Y> entity = null;
        if (requestBody != null && !isDefHeaderReqr) {
            entity = new HttpEntity<Y>(requestBody);
        } else if (isDefHeaderReqr) {
            MultiValueMap<String, String> headers = generateHeaders(brandFromCache);
            if (requestBody != null) {
                entity = new HttpEntity<Y>(requestBody, headers);
            } else {
                entity = new HttpEntity<Y>(headers);
            }
        }
        return entity;
    }

    public MultiValueMap<String, String> generateHeaders(String brandFromCache) {
        MultiValueMap<String, String> headers;
        if (StringUtils.isNotEmpty(brandFromCache)) {
            headers = generateSchdulerHeaders(brandFromCache);
        } else {
            headers = generateDefaultHeader();
        }
        return headers;
    }

    private MultiValueMap<String, String> generateSchdulerHeaders(String brand) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_CHANNEL.code(), brand);
        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_SESSION_ID.code(),
                DocUploadConstant.SCHEDULER_CALL);
        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_DEVICE_IP.code(),
                ApplicationRequestContext.get(ApplicationAttribute.DEVICE_IP).toString());
        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_XBRAND.code(), brand);
        setHeaderConfigValues(headers, RequestHeaderDetails.KEY_HEADER_CONTENT_TYPE.code(), MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * method to set the header based on header value and tag name for config passed
     * @param tagName
     * @param headerValue
     * @return String
     */
    private void setHeaderConfigValues(MultiValueMap<String, String> headers, String tagName, String headerValue) {

        headers.add(configurationService.getConfigurationValueAsString(
                RequestHeaderDetails.SECTION_REQUEST_HEADER_DETAILS.code(), tagName), headerValue);

    }

    public <X> X getRefDataValue(String url, Class<X> responseType, boolean isDefHeaderReq, String brandFromCache)
            throws DocUploadServiceException {
        return request(url, HttpMethod.GET, responseType, null, isDefHeaderReq, brandFromCache);
    }

    private String buildUrlWithParameters(RestContext context) {

        Map<String, String> queryParameter = context.queryParams();
        Map<String, String> pathParameter = context.pathParams();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(context.endpoint());
        for (Map.Entry<String, String> entry : queryParameter.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }

        // set the path params to the url
        UriComponents uriComponentsWithPathParams = null;
        if (pathParameter.size() != 0) {
            uriComponentsWithPathParams = uriBuilder.buildAndExpand(pathParameter);
        }
        if (uriComponentsWithPathParams != null) {
            String pathWithParams = uriComponentsWithPathParams.getPath();
            uriBuilder = uriBuilder.replacePath(pathWithParams);
        }
        return uriBuilder.build().toUriString();
    }
}