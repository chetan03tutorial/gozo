package com.lbg.ib.api.sales.common.rest;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
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

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.filter.ColleagueIdExtractionFilter;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.exceptions.interfaces.IBError;
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
import com.lloydstsb.ea.logging.interfaces.LoggingService;

/*
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Subject.class, WSSubject.class, LoggingServiceFactory.class, KerberosPrincipal.class })
public class ColleagueIdExtractionFilterTest {

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

    @Mock
    private LoggerDAO            logger;

    @Mock
    FilterChain                  filterChain;

    @Test
    public void testKerberosPrincipal() throws ServiceException, ErrorInfo, RemoteException {
        Subject subject = PowerMockito.mock(Subject.class);
        KerberosPrincipal principle = PowerMockito.mock(KerberosPrincipal.class);
        when(principle.getName()).thenReturn("userid@domain");
        when(request.getSession()).thenReturn(httpSession);
        PowerMockito.mockStatic(WSSubject.class);
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        ColleagueIdExtractionFilter colleagueExtractionFilter = new ColleagueIdExtractionFilter();
        try {
            when(WSSubject.getCallerSubject()).thenReturn(subject);
            Set<Principal> set = new HashSet<Principal>();
            set.add(principle);
            subject.getPrincipals().add(principle);
            when(subject.getPrincipals()).thenReturn(set);
            colleagueExtractionFilter.doFilter(request, response, filterChain);
        } catch (WSSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ServletException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @Test
    public void testOtherPrincipal() throws ServiceException, ErrorInfo, RemoteException {
        Subject subject = PowerMockito.mock(Subject.class);
        Principal principle = PowerMockito.mock(Principal.class);
        when(principle.getName()).thenReturn("userid/domain");
        when(request.getSession()).thenReturn(httpSession);
        PowerMockito.mockStatic(WSSubject.class);
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        ColleagueIdExtractionFilter colleagueExtractionFilter = new ColleagueIdExtractionFilter();
        try {
            when(WSSubject.getCallerSubject()).thenReturn(subject);
            Set<Principal> set = new HashSet<Principal>();
            set.add(principle);
            subject.getPrincipals().add(principle);
            when(subject.getPrincipals()).thenReturn(set);
            colleagueExtractionFilter.doFilter(request, response, filterChain);
        } catch (WSSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ServletException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @Test
    public void testWSException() throws ServiceException, ErrorInfo, RemoteException {
        Subject subject = PowerMockito.mock(Subject.class);
        Principal principle = PowerMockito.mock(Principal.class);
        when(principle.getName()).thenThrow(WSSecurityException.class);
        when(request.getSession()).thenReturn(httpSession);
        PowerMockito.mockStatic(WSSubject.class);
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        ColleagueIdExtractionFilter colleagueExtractionFilter = new ColleagueIdExtractionFilter();
        try {
            when(WSSubject.getCallerSubject()).thenReturn(subject);
            Set<Principal> set = new HashSet<Principal>();
            set.add(principle);
            subject.getPrincipals().add(principle);
            when(subject.getPrincipals()).thenReturn(set);
            colleagueExtractionFilter.doFilter(request, response, filterChain);
        } catch (WSSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ServletException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @Test
    public void testinit() throws ServiceException, ErrorInfo, RemoteException {
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        ColleagueIdExtractionFilter colleagueExtractionFilter = new ColleagueIdExtractionFilter();
        try {
            colleagueExtractionFilter.init(filterConfig);
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testdestroy() throws ServiceException, ErrorInfo, RemoteException {
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        ColleagueIdExtractionFilter colleagueExtractionFilter = new ColleagueIdExtractionFilter();
        colleagueExtractionFilter.destroy();
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
*/
