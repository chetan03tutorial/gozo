/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.threatmatrix;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.threatmatrix.StubThreatMatrix.threadMatrix;
import static com.lbg.ib.api.sales.dao.threatmatrix.ThreatMatrixDAOImpl.MAX_LENGTH;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.soapapis.threatmatrix.schema.enterprise.lcsm.ErrorInfo;

@RunWith(MockitoJUnitRunner.class)
public class ThreatMatrixDAOImplTest {

    public static final String   PRIM_CUR_ACC    = "prim_cur_acc";
    private static final int     FULL_LENGTH     = 81;
    public static final String   OCIS_ID         = "ocis";

    @InjectMocks
    private ThreatMatrixDAOImpl  threatMatrixDAO = new ThreatMatrixDAOImpl();

    @Mock
    private ApiServiceProperties properties;

    @Mock
    private ChannelBrandingDAO   channelBRanding;

    @Mock
    private SessionManagementDAO session;

    @Mock
    private LoggerDAO            logger;

    @Before
    public void setUp() throws Exception {
        when(properties.getThreatMatrixDetailsFromConfig("ThreatMetrixOrgID", "LLOYDS")).thenReturn("orgId");
        when(properties.getThreatMatrixDetailsFromConfig("ThreatMetrixApiKey", "LLOYDS"))
                .thenReturn("irit8diagzvsojwmttfduhaj3tv9yuig");
        when(session.getUserContext()).thenReturn(userContextWithOcis(OCIS_ID));
        when(session.getSessionId()).thenReturn("sessionId");
        when(channelBRanding.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("LTSBRetail", "LLOYDS", "IBL")));
    }

    @Test
    public void shouldCreateRequestBlobAndParseResponseBlob() throws Exception {
        StubThreatMatrix service = threadMatrix().returnsResponse(exampleResponse("reasonCode"))
                .forRequest(expectedRequest());
        threatMatrixDAO.setThreatMatrixService(service);

        DAOResponse<Map<String, String>> results = threatMatrixDAO.retrieveThreadMatrixResults("applicationId",
                PRIM_CUR_ACC, OCIS_ID);

        assertThat(results.getResult(), is(expectedResults()));
    }

    @Test
    public void shouldCreateRequestBlobAndParseResponseBlobAndTrimReasonCodeLongerThan64Character() throws Exception {
        StubThreatMatrix service = threadMatrix()
                .returnsResponse(
                        exampleResponse("reasonCodeLongerThan64CharactersReasonCodeLongerThan64CharactersReason"))
                .forRequest(expectedRequest());
        threatMatrixDAO.setThreatMatrixService(service);

        DAOResponse<Map<String, String>> results = threatMatrixDAO.retrieveThreadMatrixResults("applicationId",
                PRIM_CUR_ACC, OCIS_ID);

        assertThat(results.getResult().get("reason_code").length(), is(MAX_LENGTH));
        assertThat(results.getResult().get("reason_code_complete").length(), is(FULL_LENGTH));
    }

    @Test
    public void shouldLogErrorResponse() throws Exception {
        StubThreatMatrix service = threadMatrix().throwz(new ErrorInfo("type", "code", "msg", "state"));
        threatMatrixDAO.setThreatMatrixService(service);

        DAOResponse<Map<String, String>> results = threatMatrixDAO.retrieveThreadMatrixResults("applicationId",
                PRIM_CUR_ACC, OCIS_ID);
        assertThat(results.getError(), notNullValue());
        verify(logger).logError(eq("code"), anyString(), eq(ThreatMatrixDAOImpl.class));
    }

    @Test
    public void shouldLogExceptionResponse() throws Exception {
        RemoteException EXCEPTION = new RemoteException("msg");
        StubThreatMatrix service = threadMatrix().throwz(EXCEPTION);
        threatMatrixDAO.setThreatMatrixService(service);

        DAOResponse<Map<String, String>> results = threatMatrixDAO.retrieveThreadMatrixResults("applicationId",
                PRIM_CUR_ACC, OCIS_ID);
        assertThat(results.getError(), notNullValue());
        verify(logger).logException(ThreatMatrixDAOImpl.class, EXCEPTION);
    }

    private UserContext userContextWithOcis(String ocis) {
        return new UserContext("", "", "", "", ocis, "", "", "", "", "", "");
    }

    private Map<String, String> expectedResults() {
        return new HashMap<String, String>() {
            {
                put("account_login", "ocis");
                put("custom_policy_score", "-5");
                put("event_type", "account_creation");
                put("org_id", "orgId");
                put("policy", "default");
                put("policy_score", "-5");
                put("reason_code", "NoDeviceID:reasonCode");
                put("reason_code_complete", "NoDeviceID:reasonCode");
                put("request_duration", "8");
                put("request_id", "592D8DEC%2d4400%2d4522%2d9B8C%2dF1C0809A69B6");
                put("request_result", "success");
                put("review_status", "pass");
                put("risk_rating", "low");
                put("service_type", "session-policy");
                put("session_id", "sessionId");
                put("summary_risk_score", "-5");
                put("unencrypted_condition_attrib_1", "ltsbretail");
                put("unknown_session", "yes");
            }
        };
    }

    private String expectedRequest() {
        return "account_login=ocis&" + "session_id=sessionI&" + "org_id=orgId&"
                + "api_key=irit8diagzvsojwmttfduhaj3tv9yuig&" + "event_type=account_creation&"
                + "service_type=session-policy&" + "transaction_id=applicationId&" + "condition_attrib_5=prim_cur_acc&"
                + "condition_attrib_6=stp&" + "policy=afdipolicy";
    }

    private String exampleResponse(final String reasonCode) {
        return "account_login=ocis&" + "custom_policy_score=%2d5&" + "event_type=account_creation&" + "org_id=orgId&"
                + "policy=default&" + "policy_score=%2d5&" + "reason_code=NoDeviceID&" + "reason_code=" + reasonCode
                + "&" + "request_duration=8&" + "request_id=592D8DEC%2d4400%2d4522%2d9B8C%2dF1C0809A69B6&"
                + "request_result=success&" + "review_status=pass&" + "risk_rating=low&"
                + "service_type=session%2dpolicy&" + "session_id=sessionId&" + "summary_risk_score=%2d5&"
                + "unencrypted_condition_attrib_1=ltsbretail&" + "unknown_session=yes";
    }

}