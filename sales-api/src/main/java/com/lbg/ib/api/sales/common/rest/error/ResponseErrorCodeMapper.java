
package com.lbg.ib.api.sales.common.rest.error;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.exceptions.runtime.ApplicationRuntimeException;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Component
public class ResponseErrorCodeMapper {
    /**
     * CLASSNAME.
     */
    private static final String  CLASSNAME                     = ResponseErrorCodeMapper.class.getName();

    /**
     * Constant for tag name 'DOC_UPLOAD_ERROR_CODE' in error code configuration
     * file configErrorCodeMapping.xml under each section for all the error
     * codes.
     */
    private static final String  CONFIG_TAG_ERROR_CODE         = "DOC_UPLOAD_ERROR_CODE";

    private static final String  CONFIG_TAG_HTTP_RESPONSE_CODE = "HTTP_RESPONSE_CODE";

    private static final String  CONFIG_TAG_ERROR_MESSAGE      = "UI_ERROR_MAPPING";

    /**
     * Error message for default scenario.
     */
    private static final String  CONFIG_TAG_DEFAULT_ERROR_CODE = "DEFAULT_ERROR_CODE";

    @Autowired
    private ConfigurationService configurationService;

    public ResponseErrorVO resolve(String errorCode) {
        String ibErrorCode = configurationService.getConfigurationValueAsString(errorCode, CONFIG_TAG_ERROR_CODE);
        ibErrorCode = (ibErrorCode == null) ? getDefaultErrorCode() : ibErrorCode;
        String message = resolveErrorMessage(ibErrorCode);
        int status = resolveResponseStatus(ibErrorCode).getStatusCode();
        return (new ResponseErrorVO(ibErrorCode, message, status));
    }

    public String resolveErrorMessage(String errorCode) {
        String errorMessage = configurationService.getConfigurationValueAsString(errorCode, CONFIG_TAG_ERROR_MESSAGE);
        return (errorMessage == null ? getDefaultErrorMessage() : errorMessage);
    }

    public Status resolveResponseStatus(String errorCode) {
        Status status = null;
        String code = configurationService.getConfigurationValueAsString(errorCode, CONFIG_TAG_HTTP_RESPONSE_CODE);
        if (code == null) {
            status = getDefaultResponseCode();
        } else {
            status = resolveStatus(code);
        }
        return ((status != null) ? status : getDefaultResponseCode());
    }

    private String getDefaultErrorMessage() {
        String errorMessage = configurationService.getConfigurationValueAsString(CONFIG_TAG_DEFAULT_ERROR_CODE,
                CONFIG_TAG_ERROR_MESSAGE);
        return (errorMessage);
    }

    private String getDefaultErrorCode() {
        String errorMessage = configurationService.getConfigurationValueAsString(CONFIG_TAG_DEFAULT_ERROR_CODE,
                CONFIG_TAG_ERROR_CODE);
        return (errorMessage);
    }

    private Status getDefaultResponseCode() {
        String responseCode = configurationService.getConfigurationValueAsString(CONFIG_TAG_DEFAULT_ERROR_CODE,
                CONFIG_TAG_HTTP_RESPONSE_CODE);
        Status responseStatus = null;
        if (responseCode != null) {
            resolveStatus(responseCode);
        }
        return ((responseCode == null) ? responseStatus : Status.OK);
    }

    private static Status resolveStatus(String responseCode) {
        Status statusCode = null;
        if (responseCode != null) {
            try {
                statusCode = Status.fromStatusCode(Integer.parseInt(responseCode));
            } catch (NumberFormatException anException) {
                ApplicationRuntimeException excp = new ApplicationRuntimeException();
                excp.setNestedException(anException);
                excp.setClassName(CLASSNAME);
                excp.setMethodContext("Exception in  resolveStatus");
                throw excp;
            }
        }
        return (statusCode);
    }

}
