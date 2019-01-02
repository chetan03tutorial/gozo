package com.lbg.ib.api.sales.common.rest;

import static javax.ws.rs.core.Response.fromResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.ext.ResponseHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Provider
@Component
public class DebugLoggerFilter implements ResponseHandler, RequestHandler {
    public static final String    MESSAGE_ID             = "messageId";
    private static final Response DO_NOT_MODIFY_RESPONSE = null;
    private LoggerDAO             logger;
    private HttpServletRequest    request;
    @Autowired
    private ApiServiceProperties  apiServiceProperties;
    private static final String   CACHE_CONFIGURATION    = "CACHE_CONFIGURATION";

    @Autowired
    public DebugLoggerFilter(LoggerDAO logger, @Context HttpServletRequest request) {
        this.logger = logger;
        this.request = request;
    }

    private byte[] extractContent(InputStream content) {
        try {
            return IOUtils.toByteArray(content);
        } catch (IOException e) {
            logger.logException(this.getClass(), e);

            return "Content Unreadable".getBytes();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.cxf.jaxrs.ext.RequestHandler#handleRequest(org.apache.cxf.
     * message.Message, org.apache.cxf.jaxrs.model.ClassResourceInfo)
     */
    public Response handleRequest(Message message, ClassResourceInfo resource) {
        try {
            String messageId = UUID.randomUUID().toString();
            request.setAttribute(MESSAGE_ID, messageId);
            String method = message.get(Message.HTTP_REQUEST_METHOD).toString();
            String requestUrl = message.get(Message.REQUEST_URL).toString();
            byte[] content = extractContent(message.getContent(InputStream.class));
            message.setContent(InputStream.class, new ByteArrayInputStream(content));
            String calledResource = resource != null ? resource.getResourceProvider().getResourceClass().getName()
                    : "Unknown Resource";
            if (logger.debugEnabled()) {

                logger.logDebug(this.getClass(), "Id:%s Resource Class:%s, Request: %s %s %s ", messageId,
                        calledResource, method, requestUrl, new String(content));
            }
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
            return DO_NOT_MODIFY_RESPONSE;
        }
        return DO_NOT_MODIFY_RESPONSE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.cxf.jaxrs.ext.ResponseHandler#handleResponse(org.apache.cxf.
     * message.Message org.apache.cxf.jaxrs.model.OperationResourceInfo,
     * javax.ws.rs.core.Response)
     */
    public Response handleResponse(Message message, OperationResourceInfo resource, Response response) {
        try {
            Integer status = response.getStatus();
            String headers = response.getMetadata().toString();
            ResponseBuilder responseBuilder = fromResponse(response);
            String content;
            if (response.getEntity() instanceof InputStream) {
                byte[] bytes = extractContent((InputStream) response.getEntity());
                responseBuilder = responseBuilder.entity(new ByteArrayInputStream(bytes));
                content = new String(bytes);
            } else {
                content = response.getEntity().toString();
            }
            if (logger.debugEnabled()) {
                logger.logDebug(this.getClass(), "Id:%s Resource Class:%s, Response: %s %s %s ",
                        request.getAttribute(MESSAGE_ID),
                        resource != null
                                ? resource.getClassResourceInfo().getResourceProvider().getResourceClass().getName()
                                : "",
                        status, headers, content);
            }
            successStatusCode(response, responseBuilder);
            Response responseLocal = responseBuilder.build();
            return responseLocal;
        } catch (Exception e) {
            logger.logException(this.getClass(), e);

        }
        return response;
    }

    /**
     * @param response
     * @param responseBuilder
     */
    private void successStatusCode(Response response,
            ResponseBuilder responseBuilder) {
        if (200 == response.getStatus()) {
            String urlLookUp = request.getRequestURI();
            if (null != urlLookUp) {
                Map<String, Object> cacheConfiguration = apiServiceProperties
                        .getConfigurationItems(CACHE_CONFIGURATION);
                boolean cacheFlag = false;
                for (Entry<String, Object> cacheEntries : cacheConfiguration.entrySet()) {
                    if (urlLookUp.contains(cacheEntries.getKey())) {
                        cacheFlag = true;
                        String cacheMaxAge = (String) cacheEntries.getValue();
                        if (StringUtils.isNotEmpty(cacheMaxAge)) {
                            CacheControl cachecontrol = new CacheControl();
                            cachecontrol.setMaxAge(Integer.valueOf(cacheMaxAge));
                            responseBuilder.cacheControl(cachecontrol);
                        }
                        break;
                    }
                }

                if (!cacheFlag) {
                    CacheControl cachecontrol = new CacheControl();
                    cachecontrol.setNoCache(true);
                    cachecontrol.setNoStore(true);
                    responseBuilder.cacheControl(cachecontrol);
                }
            }
        }
    }
}