package com.lbg.ib.api.sales.common.rest;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.message.Message;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class DebugLoggerFilterTest {
    public static final Integer                     STATUS   = 200;
    public static final MetadataMap<String, Object> HEADERS  = new MetadataMap<String, Object>();
    private LoggerDAO                               logger   = mock(LoggerDAO.class);
    private Response                                response = mock(Response.class);
    private Message                                 message  = mock(Message.class);
    private HttpServletRequest                      request  = mock(HttpServletRequest.class);

    private Matcher<InputStream> inputStreamWithData(final String value) {
        return new TypeSafeMatcher<InputStream>() {
            boolean avoidMockitoDuplicateCall = false;

            public void describeTo(Description description) {
                description.appendText("Expected value is " + value);
            }

            @Override
            protected boolean matchesSafely(InputStream o) {
                try {
                    if (avoidMockitoDuplicateCall) {
                        return true;
                    }
                    avoidMockitoDuplicateCall = true;
                    return IOUtils.toString(o).equals(value);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    @Test
    public void shouldKeepTheContentDebuggingInResponse() throws Exception {
        when(logger.debugEnabled()).thenReturn(true);
        when(response.getStatus()).thenReturn(STATUS);
        when(response.getMetadata()).thenReturn(HEADERS);
        when(response.getEntity()).thenReturn(new ByteArrayInputStream("content".getBytes()));

        DebugLoggerFilter filter = new DebugLoggerFilter(logger, request);
        Response filteredResponse = filter.handleResponse(null, null, this.response);

        assertThat(IOUtils.toString((InputStream) filteredResponse.getEntity()), is("content"));
        logger.logDebug(any(Class.class), any(String.class),
                eq(new Object[] { STATUS.toString(), HEADERS.toString(), "content" }));
    }

    @Test
    public void whenDebugIsEnabledWeReadContentAndWePutItBack() throws Exception {
        DebugLoggerFilter filter = new DebugLoggerFilter(logger, request);
        when(logger.debugEnabled()).thenReturn(true);
        when(message.get(Message.HTTP_REQUEST_METHOD)).thenReturn("METHOD");
        when(message.get(Message.REQUEST_URL)).thenReturn("requestURL");
        when(message.getContent(InputStream.class)).thenReturn(new ByteArrayInputStream("hello".getBytes()));
        filter.handleRequest(message, null);
        verify(message, times(1)).setContent(eq(InputStream.class), argThat(inputStreamWithData("hello")));
    }
}
