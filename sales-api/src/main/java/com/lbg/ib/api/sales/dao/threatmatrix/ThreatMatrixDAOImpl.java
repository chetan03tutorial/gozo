/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.threatmatrix;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;
import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.shared.util.logger.ServiceLogger.formatMessage;
import static org.apache.commons.lang.StringUtils.join;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.common.Blob;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.common.BlobResponse;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.common.ThreadMatrix;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.common.ThreadMatrixRequest;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.common.ThreadMatrixResponse;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.lcsm.ErrorInfo;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.lcsm.RequestHeader;

@Component
public class ThreatMatrixDAOImpl implements ThreatMatrixDAO {

    private static final String  QUERY_PARAMETER_SEPARATOR = "&";
    private static final String  EQUALS                    = "=";
    public static final int      MAX_LENGTH                = 64;

    @Autowired
    private ThreadMatrix         service;

    @Autowired
    private ChannelBrandingDAO   channelBranding;

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private ApiServiceProperties properties;

    @Autowired
    private LoggerDAO            logger;

    @TraceLog
    public DAOResponse<Map<String, String>> retrieveThreadMatrixResults(String applicationId, String applicationType,
            String ocisId) {
        try {
            ThreadMatrixResponse serviceResponse = service
                    .invoke(threatMatrixRequest(applicationId, applicationType, ocisId));
            BlobResponse blob = serviceResponse.getBlob();

            if (blob == null) {
                return withError(new DAOError("0", "No response recieved from threat matrix"));
            }

            Map<String, String> resultMap = parseResponse(blob);
            for (String key : resultMap.keySet()) {
                if ("request_result".equalsIgnoreCase(key) && resultMap.get(key).startsWith("fail")) {
                    String errMsg = "Failure response received from Threat Matrix with the following values "
                            + ":: Request ID = " + resultMap.get("request_id") + ":: Request Result = "
                            + resultMap.get("request_result") + ":: Unknown Session = "
                            + resultMap.get("unknown_session") + ":: Error Detail = " + resultMap.get("error_detail");
                    logger.logError("0", formatMessage(errMsg, "threatMatrix"), ThreatMatrixDAOImpl.class);
                    return withError(new DAOError("0", errMsg));
                }
            }
            return withResult(resultMap);
        } catch (ErrorInfo e) {
            logger.logException(this.getClass(), e);
            logger.logError(e.getErrorCode(), formatMessage(e.getErrorMessageText(), "threatMatrix"),
                    ThreatMatrixDAOImpl.class);
            return withError(new DAOError(e.getErrorCode(), e.getErrorMessageText()));
        } catch (UnsupportedEncodingException e) {
            logger.logException(this.getClass(), e);
            return withError(
                    new DAOError("encodingException", "Unsupported encoding exception in threat matrix service"));
        } catch (RemoteException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(REMOTE_EXCEPTION,
                    "Remote connection exception in threat matrix service during invocation"));
        }
    }

    private ThreadMatrixRequest threatMatrixRequest(String applicationId, String applicationType, String ocisId) {
        ThreadMatrixRequest request = new ThreadMatrixRequest();
        request.setRequestHeader(new RequestHeader());
        Blob blob = new Blob();
        blob.setBlob(prepareRequestParameters(applicationId, applicationType, ocisId));
        request.setBlob(blob);
        return request;
    }

    private String prepareRequestParameters(String id, String applicationType, String ocisId) {
        ChannelBrandDTO channelBrand = channelBranding.getChannelBrand().getResult();
        StringBuilder stringBuilder = new StringBuilder();
        appendString(stringBuilder, "account_login", ocisId);
        appendString(stringBuilder, "session_id", get8CharsOfSessionID());
        appendString(stringBuilder, "org_id", properties.getThreatMatrixDetailsFromConfig("ThreatMetrixOrgID",
                channelBrand == null ? "" : channelBrand.getBrand()));
        appendString(stringBuilder, "api_key", properties.getThreatMatrixDetailsFromConfig("ThreatMetrixApiKey",
                channelBrand == null ? "" : channelBrand.getBrand()));
        appendString(stringBuilder, "event_type", "account_creation");
        appendString(stringBuilder, "service_type", "session-policy");
        appendString(stringBuilder, "transaction_id", id);
        appendString(stringBuilder, "condition_attrib_5", applicationType);
        appendString(stringBuilder, "condition_attrib_6", "stp");
        stringBuilder.append("policy" + EQUALS + "afdipolicy");
        return stringBuilder.toString();
    }

    private String get8CharsOfSessionID() {
        return session.getSessionId().substring(0, 8);
    }

    private Map<String, String> parseResponse(BlobResponse blob) throws UnsupportedEncodingException {
        final Map<String, String> resultMap = new LinkedHashMap<String, String>();
        final String[] pairs = blob.getBlob().split(QUERY_PARAMETER_SEPARATOR);
        final List<String> reasonCodes = new ArrayList<String>();
        for (String pair : pairs) {
            final String[] keyValue = pair.split(EQUALS);
            final String key = keyValue[0], value = keyValue[1];
            if ("reason_code".equals(key)) {
                reasonCodes.add(value);
            } else if ("request_id".equals(key)) {
                resultMap.put(key, value);
            } else {
                resultMap.put(key, URLDecoder.decode(value, "UTF-8"));
            }
        }
        String reasonCodesComplete = join(reasonCodes, ":");
        resultMap.put("reason_code_complete", reasonCodesComplete);
        resultMap.put("reason_code", reasonCodesComplete.length() > MAX_LENGTH
                ? reasonCodesComplete.substring(0, MAX_LENGTH) : reasonCodesComplete);
        return resultMap;
    }

    private void appendString(StringBuilder stringBuilder, String key, String value) {
        stringBuilder.append(key + EQUALS);
        stringBuilder.append(value);
        stringBuilder.append(QUERY_PARAMETER_SEPARATOR);
    }

    public void setThreatMatrixService(ThreadMatrix service) {
        this.service = service;
    }

}