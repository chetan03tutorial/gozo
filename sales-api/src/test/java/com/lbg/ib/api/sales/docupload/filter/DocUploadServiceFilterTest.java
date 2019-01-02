/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.filter;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lloydstsb.ea.application.switching.GlobalApplicationSwitches;
import com.lloydstsb.ea.application.switching.data.ApplicationSwitch;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

/**
 * @author 8735182
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GlobalApplicationSwitches.class, ApplicationRequestContext.class })
@SuppressStaticInitializationFor({ "com.lloydstsb.ea.application.switching.GlobalApplicationSwitches",
        "com.lloydstsb.ea.logging.helper.ApplicationRequestContext" })
public class DocUploadServiceFilterTest {
    private static final String     channelId                       = "LTSBRetail";

    private static final String     switchName                      = "SW_EnDcUpldRApi";

    private static final String     DOC_UPLOAD_ALLOWED_CORS_URLS    = "DOC_UPLOAD_ALLOWED_CORS_URLS";

    private static final String     CORS_URL                        = "CORS_URL";

    private static final String     DOC_UPLOAD_ALLOWED_CORS_HEADERS = "DOC_UPLOAD_ALLOWED_CORS_HEADERS";

    private static final String     CORS_HEADERS                    = "CORS_HEADERS";

    private static final String     DOC_UPLOAD_SWITCH               = "DOC_UPLOAD_SWITCH";

    private static final String     BRANDED_SWITCH                  = "BRANDED_SWITCH";

    private static final String     urls                            = "http://localhost:8090";

    private static final String     headers                         = "X-ACCESS-TOKEN";

    @InjectMocks
    private DocUploadServiceFilter  docUploadFilter;

    @Mock
    private ConfigurationService    configurationService;

    @Mock
    private LoggerDAO               logger;

    @Mock
    private HttpServletRequest      mockServletRequest;

    @Mock
    private HttpServletResponse     mockServletResponse;

    @Mock
    private FilterChain             mockFilterChain;

    @Mock
    private HttpSession             httpSession;

    @Mock
    private ResponseErrorCodeMapper responseErrorCodeMapper;

    @Before
    public void setUp() throws Exception {
        mockStatic(GlobalApplicationSwitches.class);
        mockStatic(ApplicationRequestContext.class);
        ConcurrentHashMap<String, ApplicationSwitch> switches = new ConcurrentHashMap<String, ApplicationSwitch>();
        ApplicationSwitch switchValue = new ApplicationSwitch();
        switchValue.setSwitchName(switchName);
        switchValue.setSwitchValue(true);
        switches.put(switchName, switchValue);
        when(ApplicationRequestContext.get(ApplicationAttribute.CHANNEL)).thenReturn(channelId);
        when(GlobalApplicationSwitches.getApplicationSwitches(channelId)).thenReturn(switches);

        when(configurationService.getConfigurationValueAsString(DOC_UPLOAD_SWITCH, BRANDED_SWITCH))
                .thenReturn(switchName);
        when(configurationService.getConfigurationValueAsString(DOC_UPLOAD_ALLOWED_CORS_HEADERS, CORS_HEADERS))
                .thenReturn(headers);

        when(configurationService.getConfigurationValueAsString(DOC_UPLOAD_ALLOWED_CORS_URLS, CORS_URL))
                .thenReturn(urls);

    }

    @Test
    public void testOfExceptionCase() throws IOException, ServletException {
        PowerMockito.doThrow(new IOException()).when(mockFilterChain).doFilter(Mockito.any(ServletRequest.class),
                Mockito.any(ServletResponse.class));

        docUploadFilter.doFilter(mockServletRequest, mockServletResponse, mockFilterChain);
        Mockito.verify(logger, Mockito.atLeastOnce()).logException(Mockito.eq(DocUploadServiceFilter.class),
                Mockito.any(IOException.class));
    }

    /*
     * @Test public void testDoFilter() throws IOException, ServletException {
     *
     * when(mockServletRequest.getHeader(DocUploadConstant.HEADER_ORIGIN)).
     * thenReturn(urls); docUploadFilter.doFilter(mockServletRequest,
     * mockServletResponse, mockFilterChain);
     * Mockito.verify(mockFilterChain).doFilter(Mockito.any(ServletRequest.class
     * ), Mockito.any(ServletResponse.class)); }
     *
     * @Test public void whenNullOriginThenThrowNullException() throws
     * IOException, ServletException {
     *
     * when(mockServletRequest.getHeader(DocUploadConstant.HEADER_ORIGIN)).
     * thenThrow( new NullPointerException());
     *
     * docUploadFilter.doFilter(mockServletRequest, mockServletResponse,
     * mockFilterChain); Mockito.verify(logger,
     * Mockito.atLeastOnce()).logException(
     * Mockito.eq(DocUploadServiceFilter.class),
     * Mockito.any(NullPointerException.class)); }
     */

    /*
     * @Test public void
     * whenIncorrectConfigUrlsThenThrowConfigurationException() throws
     * IOException, ServletException {
     *
     * when( configurationService.getConfigurationValueAsString(
     * DOC_UPLOAD_ALLOWED_CORS_URLS, CORS_URL)).thenThrow(new
     * ConfigurationException());
     *
     * docUploadFilter.doFilter(mockServletRequest, mockServletResponse,
     * mockFilterChain); Mockito.verify(logger,
     * Mockito.atLeastOnce()).logException(
     * Mockito.eq(DocUploadServiceFilter.class),
     * Mockito.any(ConfigurationException.class)); }
     */

}
