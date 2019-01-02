/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.switches.service;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.lloydstsb.ea.application.switching.GlobalApplicationSwitches;
import com.lloydstsb.ea.application.switching.data.ApplicationSwitch;
import com.lloydstsb.ea.exceptions.interfaces.IBError;
import com.lloydstsb.ea.logging.event.*;
import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;
import com.lloydstsb.ea.logging.interfaces.LoggingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.exception.SwitchLoadFailedException;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.switches.SwitchesManagementDAO;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggingServiceFactory.class })
public class SwitchesManagementServiceImplTest {

    @InjectMocks
    private SwitchesManagementServiceImpl switchesManagementServiceImpl;

    @Mock
    private ConfigurationDAO              configDAO;

    @Mock
    private SwitchesManagementDAO         switchesDAO;

    @Mock
    private GalaxyErrorCodeResolver       resolver;

    private static final String           ERROR_CODE              = "9910008";

    private static final String           ERROR_MESSAGE           = "Switches Not Found";

    private static final String           SUPPORTED_CHANNEL_ENTRY = "LTSBRetail";

    private static final String           CHANNEL_ID              = "IBL";

    @Test(expected = SwitchLoadFailedException.class)
    public void shouldThrowSwitchNotFoundExceptionWhenSwitchesNotFetched() throws Exception {
        HashMap<String, Object> supportedChannelsMap = new HashMap<String, Object>();
        supportedChannelsMap.put(SUPPORTED_CHANNEL_ENTRY, "1");

        DAOError error = new DAOError(ERROR_CODE, ERROR_MESSAGE);

        when(configDAO.getConfigurationItems(CommonConstants.SUPPORTED_CHANNEL_SECTION))
                .thenReturn(supportedChannelsMap);
        when(configDAO.getConfigurationStringValue(CommonConstants.CHANNEL_ID, SUPPORTED_CHANNEL_ENTRY))
                .thenReturn(CHANNEL_ID);
        when(switchesDAO.getSwitches(CHANNEL_ID)).thenReturn(DAOResponse.<HashMap<String, Boolean>> withError(error));
        when(resolver.resolve(ERROR_CODE)).thenReturn(new ResponseError(ERROR_CODE, ERROR_MESSAGE));

        switchesManagementServiceImpl.loadSwitches();
    }

    @Test
    public void shouldPopulateGlobalApplicationSwitchesForTheChannel() throws Exception {
        HashMap<String, Object> supportedChannelsMap = new HashMap<String, Object>();
        supportedChannelsMap.put(SUPPORTED_CHANNEL_ENTRY, "1");

        HashMap<String, Boolean> switchMap = new HashMap<String, Boolean>();
        switchMap.put("switch1", true);
        switchMap.put("switch2", false);
        when(configDAO.getConfigurationItems(CommonConstants.SUPPORTED_CHANNEL_SECTION))
                .thenReturn(supportedChannelsMap);
        when(configDAO.getConfigurationStringValue(CommonConstants.CHANNEL_ID, SUPPORTED_CHANNEL_ENTRY))
                .thenReturn(CHANNEL_ID);
        when(switchesDAO.getSwitches(CHANNEL_ID))
                .thenReturn(DAOResponse.<HashMap<String, Boolean>> withResult(switchMap));

        PowerMockito.mockStatic(LoggingServiceFactory.class);
        when(LoggingServiceFactory.getLoggingService()).thenReturn(createLoggingService());
        switchesManagementServiceImpl.loadSwitches();
        ConcurrentMap<String, ApplicationSwitch> applicationSwitches = GlobalApplicationSwitches
                .getApplicationSwitches(SUPPORTED_CHANNEL_ENTRY);
        Assert.assertThat(applicationSwitches.containsKey("switch1"), is(true));
        Assert.assertThat(applicationSwitches.containsKey("switch2"), is(true));
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
                return false;
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