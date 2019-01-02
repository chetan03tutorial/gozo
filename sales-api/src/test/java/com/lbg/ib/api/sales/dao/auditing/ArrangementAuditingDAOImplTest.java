/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.auditing;

import static com.lbg.ib.api.sales.common.auditing.ThreatAuditor.FAIL_TM_CALL;
import static com.lbg.ib.api.sales.common.auditing.ThreatAuditor.SUCCESS_TM_CALL;
import static com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAOImpl.AFDI_EVENT;
import static com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAOImpl.AFDI_SUB_EVENT_TYPE_FAILURE_OR_ERROR;
import static com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAOImpl.AFDI_SUB_EVENT_TYPE_SUCCESS;
import static com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAOImpl.AFDI_SUB_EVENT_TYPE_SUCCESS_NODEVICEID;
import static com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAOImpl.NO_DEVICEID;
import static com.lbg.ib.api.sales.dao.auditing.ArrangementAuditingDAOImpl.VALUE_TO_ADD;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB555AWServMIS;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB555BWServMIS;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.SystemPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.TAudit;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.TMisUpdate;

@RunWith(MockitoJUnitRunner.class)
public class ArrangementAuditingDAOImplTest {

    public static final String   SESSIONS_ID          = "sisId";

    public static final String   AUDITING_MESSAGE     = "auditingMessage";

    public static final String   AUDITING_MESSAGE1    = "auditingMessage," + SUCCESS_TM_CALL + "," + NO_DEVICEID;

    public static final String   AUDITING_MESSAGE2    = "auditingMessage," + SUCCESS_TM_CALL;

    public static final String   AUDITING_MESSAGE3    = "auditingMessage," + FAIL_TM_CALL;

    public static final int      HAPPY_PATH_CODE_ZERO = 0;

    private static final int     SAD_PATH_CODE        = 1;

    public static final String   ERROR_MESSAGE        = "err";

    @InjectMocks
    ArrangementAuditingDAOImpl   arraAuditingDAOImpl  = new ArrangementAuditingDAOImpl();

    @Mock
    private SessionManagementDAO session;

    @Mock
    private LoggerDAO            logger;

    @Mock
    private SystemPortType       systemPortType;

    @Before
    public void setup() {
        when(session.getSessionId()).thenReturn(SESSIONS_ID);
        when(session.getUserContext()).thenReturn(userContent());
    }

    @Test
    public void shouldAuditGivenAuditText() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit(AUDITING_MESSAGE, "SZ03");
        verify(systemPortType).b555WServMIS(argThat(expectedValues()));
    }

    @Test
    public void shouldAuditGivenAuditTextForFailedSiraConnect() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues5()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit(AUDITING_MESSAGE3, "SZ06");
        verify(systemPortType).b555WServMIS(argThat(expectedValues5()));
    }

    @Test
    public void shouldAuditGivenAuditTextForSuccessSiraConnect() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues4()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit("SUCCESS", "SZ06");
        verify(systemPortType).b555WServMIS(argThat(expectedValues4()));
    }

    @Test
    public void shouldAuditGivenAuditTextForSiraAcceptDecision() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues6Accept()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit("ACCEPT", "SZ07");
        verify(systemPortType).b555WServMIS(argThat(expectedValues6Accept()));
    }

    @Test
    public void shouldAuditGivenAuditTextForSiraReferDecision() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues7Refer()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit("REFER", "SZ07");
        verify(systemPortType).b555WServMIS(argThat(expectedValues7Refer()));
    }

    @Test
    public void shouldAuditGivenAuditTextForSiraDeclineDecision() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues8Decline()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit("DECLINE", "SZ07");
        verify(systemPortType).b555WServMIS(argThat(expectedValues8Decline()));
    }

    @Test
    public void shouldAuditGivenAuditTextForSiraSubmitDecision() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues9Submit()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit("SUBMITTED", "SZ07");
        verify(systemPortType).b555WServMIS(argThat(expectedValues9Submit()));
    }

    @Test
    public void shouldAuditCorrectAFDIEventSubtypeForTMSuccessAndNoDeviceIDReasonCodePresent() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues1()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit(AUDITING_MESSAGE1, "SZ04");
        verify(systemPortType).b555WServMIS(argThat(expectedValues1()));
    }

    @Test
    public void shouldAuditCorrectAFDIEventSubtypeForTMSuccessAndNoDeviceIDReasonCodeNotPresent() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues2()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit(AUDITING_MESSAGE2, "SZ04");
        verify(systemPortType).b555WServMIS(argThat(expectedValues2()));
    }

    @Test
    public void shouldAuditCorrectAFDIEventSubtypeForTMFailureOrError() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues3()))).thenReturn(response(HAPPY_PATH_CODE_ZERO));
        arraAuditingDAOImpl.audit(AUDITING_MESSAGE3, "SZ04");
        verify(systemPortType).b555WServMIS(argThat(expectedValues3()));
    }

    @Test
    public void shouldLogErrorWhenResponseCodeIsOtherThanZero() throws Exception {
        when(systemPortType.b555WServMIS(argThat(expectedValues()))).thenReturn(response(SAD_PATH_CODE));
        arraAuditingDAOImpl.audit(AUDITING_MESSAGE, "SZ04");
        verify(logger).logError("1", ERROR_MESSAGE, ArrangementAuditingDAOImpl.class);
    }

    @Test
    public void shouldLogExceptionWhenAnExceptionIsThrown() throws Exception {
        IllegalStateException EXCEPTION = new IllegalStateException();
        when(systemPortType.b555WServMIS(argThat(expectedValues()))).thenThrow(EXCEPTION);
        arraAuditingDAOImpl.audit(AUDITING_MESSAGE, "SZ04");
        verify(logger).logException(ArrangementAuditingDAOImpl.class, EXCEPTION);
    }

    private StB555BWServMIS response(int code) {
        StB555BWServMIS response = new StB555BWServMIS();
        StError sterror = new StError();
        sterror.setErrorno(code);
        sterror.setErrormsg(ERROR_MESSAGE);
        response.setSterror(sterror);
        return response;
    }

    private Matcher<StB555AWServMIS> expectedValues() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(AFDI_SUB_EVENT_TYPE_FAILURE_OR_ERROR));
                return audit.getEvtlogtext().equals(AUDITING_MESSAGE) && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals(AFDI_EVENT)
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues1() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(AFDI_SUB_EVENT_TYPE_SUCCESS_NODEVICEID));
                return audit.getEvtlogtext().equals(AUDITING_MESSAGE1) && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals(AFDI_EVENT)
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);

            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues2() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(AFDI_SUB_EVENT_TYPE_SUCCESS));
                return audit.getEvtlogtext().equals(AUDITING_MESSAGE2) && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals(AFDI_EVENT)
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues3() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(AFDI_SUB_EVENT_TYPE_FAILURE_OR_ERROR));
                return audit.getEvtlogtext().equals(AUDITING_MESSAGE3) && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals(AFDI_EVENT)
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues4() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(4));
                return audit.getEvtlogtext().equals("SUCCESS") && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals("729")
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues5() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(5));
                return audit.getEvtlogtext().equals(AUDITING_MESSAGE3) && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals("729")
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues6Accept() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(6));
                return audit.getEvtlogtext().equals("ACCEPT") && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals("730")
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues7Refer() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(7));
                return audit.getEvtlogtext().equals("REFER") && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals("730")
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues8Decline() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(8));
                return audit.getEvtlogtext().equals("DECLINE") && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals("730")
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<StB555AWServMIS> expectedValues9Submit() {
        return new TypeSafeMatcher<StB555AWServMIS>() {
            @Override
            protected boolean matchesSafely(StB555AWServMIS request) {
                TAudit audit = request.getStaudit();
                StHeader header = request.getStheader();
                TMisUpdate[] tmisUpdate = request.getAstmisupdate();
                assertThat(tmisUpdate[0].getLSubtype(), is(9));
                return audit.getEvtlogtext().equals("SUBMITTED") && header.getSessionid().equals(SESSIONS_ID)
                        && header.getChanid().equals("cid") && tmisUpdate[0].getEvttype().equals("730")
                        && tmisUpdate[0].getUlValueToAdd().equals(VALUE_TO_ADD);
            }

            public void describeTo(Description description) {

            }
        };
    }

    private UserContext userContent() {
        return new UserContext("uid", "ip", SESSIONS_ID, "pid", "0", "cid", "cm", "agent", "ln", "inbox", "host");
    }

}