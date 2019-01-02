package com.lbg.ib.api.sales.docupload.filter;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.exceptions.interfaces.IBError;
import com.lloydstsb.ea.filters.securityfilter.enums.ContextDetails;
import com.lloydstsb.ea.lcsm.ErrorInfo;
import com.lloydstsb.ea.logging.event.BusinessActivityEvent;
import com.lloydstsb.ea.logging.event.ErrorEvent;
import com.lloydstsb.ea.logging.event.ExceptionEvent;
import com.lloydstsb.ea.logging.event.MetricEvent;
import com.lloydstsb.ea.logging.event.PerformanceEvent;
import com.lloydstsb.ea.logging.event.StateEvent;
import com.lloydstsb.ea.logging.event.SystemActivityEvent;
import com.lloydstsb.ea.logging.event.TraceEvent;
import com.lloydstsb.ea.logging.event.WarningEvent;
import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import com.lloydstsb.ea.logging.interfaces.LoggingService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggingServiceFactory.class, ApplicationRequestContext.class })
public class AuthenticateCustomFilterTest {

    @Mock
    private SessionManagementDAO session;

    @Mock
    private ChannelBrandingDAO   channelBrandingDAO;

    @Mock
    private HttpServletRequest   request;

    @Mock
    private HttpServletResponse  response;

    @Mock
    private HttpSession          httpSession;

    @Mock
    private FilterConfig         filterConfig;

    @Mock
    private ConfigurationDAO     configurationDAO;

    @Mock
    private ConfigurationService configurationService;

    /*
     * @Mock private ApplicationRequestContext context;
     */

    @Mock
    private LoggerDAO            logger;

    @Mock
    FilterChain                  filterChain;

    @Test
    public void testFilter() throws ServiceException, ErrorInfo, RemoteException {

        when(request.getSession()).thenReturn(httpSession);
        PowerMockito.mockStatic(ApplicationRequestContext.class);
        when(ApplicationRequestContext.get(ContextDetails.LEGACY_CHANNEL.code())).thenReturn("testString");

        when(request.getRequestURI()).thenReturn("customurl/v1/authorize/test");
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        AuthenticateCustomerFilter customFilter = new AuthenticateCustomerFilter(logger, configurationService);
        try {
            customFilter.doFilter(request, response, filterChain);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test()
    public void testFilterWithError() {

        when(request.getSession()).thenReturn(httpSession);
        PowerMockito.mockStatic(ApplicationRequestContext.class);
        // ApplicationRequestContext c =
        // PowerMockito.mock(ApplicationRequestContext.class);
        when(ApplicationRequestContext.get(ContextDetails.LEGACY_CHANNEL.code())).thenThrow(NullPointerException.class);

        when(request.getRequestURI()).thenReturn("customurl/v1/authorize/test");
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        AuthenticateCustomerFilter customFilter = new AuthenticateCustomerFilter(logger, configurationService);
        try {
            customFilter.doFilter(request, response, filterChain);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test()
    public void testFilterWithElseCondition() {

        when(request.getSession()).thenReturn(httpSession);
        PowerMockito.mockStatic(ApplicationRequestContext.class);
        // ApplicationRequestContext c =
        // PowerMockito.mock(ApplicationRequestContext.class);
        when(ApplicationRequestContext.get(ContextDetails.LEGACY_CHANNEL.code())).thenThrow(NullPointerException.class);

        when(request.getRequestURI()).thenReturn("customurl/test");
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        AuthenticateCustomerFilter customFilter = new AuthenticateCustomerFilter(logger, configurationService);
        try {
            customFilter.doFilter(request, response, filterChain);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private LoggingService createLoggingService() {
        return new LoggingService() {
            public void logBusinessActivity(BusinessActivityEvent businessActivityEvent) {

            }

            public void logProductionTrace(TraceEvent traceEvent) {

            }

            public void logComponentState(StateEvent stateEvent) {

            }

            public void logPerformanceMetric(PerformanceEvent performanceEvent) {

            }

            public void logApplicationMetric(MetricEvent metricEvent) {

            }

            public void logError(IBError ibError) {

            }

            public void logError(ErrorEvent errorEvent) {

            }

            public void logException(Exception e) {

            }

            public void logException(ExceptionEvent exceptionEvent) {

            }

            public void logWarning(WarningEvent warningEvent) {

            }

            public void logDebug(String s, String s1, Object... objects) {

            }

            public void logComponentActivity(SystemActivityEvent systemActivityEvent) {

            }

            public boolean isDebugEnabled() {
                return true;
            }

            public boolean isTraceEnabled(String s) {
                return false;
            }

            public boolean isBusinessActivityLogEnabled(String s) {
                return false;
            }

            public boolean isComponentStateLogEnabled(String s) {
                return false;
            }

            public boolean isComponentActivityLogEnabled(String s) {
                return false;
            }

            public boolean isApplicationMetricLogEnabled(String s) {
                return false;
            }

            public boolean isPerformanceMetricLogEnabled(String s) {
                return false;
            }

            public boolean isWarningLogEnabled(String s) {
                return false;
            }

            public boolean isErrorLogEnabled() {
                return false;
            }

            public boolean isLoggingAvailable() {
                return false;
            }
        };
    }

}
