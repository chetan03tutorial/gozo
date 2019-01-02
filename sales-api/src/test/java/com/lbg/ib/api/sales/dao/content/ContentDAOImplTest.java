package com.lbg.ib.api.sales.dao.content;

import static com.lbg.ib.api.sales.dao.content.ContentDAOImpl.CONNECTION_ERROR;
import static com.lbg.ib.api.sales.dao.content.ContentDAOImpl.CONTENT_NOT_FOUND;
import static com.lbg.ib.api.sales.dao.content.ContentDAOImpl.UNEXPECTED_RESPONSE_CODE;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.content.ContentDAOImpl.DefaultHttpClientFactory;
import com.lbg.ib.api.sales.dao.content.ContentDAOImpl.HttpClientFactory;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.channel.ChannelServiceFactory;
import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;

/*import com.lbg.ib.api.sales.dao.content.ContentDAOImpl.DefaultHttpClientFactory;
*/
/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggingServiceFactory.class, ChannelServiceFactory.class })
public class ContentDAOImplTest {
    public static final IOException             IO_EXCEPTION       = new IOException();
    public static final ClientProtocolException PROTOCOL_EXCEPTION = new ClientProtocolException();
    private ContentTargetUrlResolver            resolver           = mock(ContentTargetUrlResolver.class);
    private InputStream                         INPUT_STREAM       = mock(InputStream.class);
    private HttpClient                          httpClient         = mock(HttpClient.class);
    private LoggerDAO                           logger             = mock(LoggerDAO.class);
    private URI                                 TARGET_URL;
    private BasicHttpResponse                   SUCCESSFUL_RESPONSE;
    private BasicHttpResponse                   NOT_FOUND_RESPONSE;
    private BasicHttpResponse                   UNEXPECTED_RESPONSE;

    @Mock
    private ApiServiceProperties                properties;

    @Before
    public void setUp() throws Exception {
        TARGET_URL = new URI("http://target");
        SUCCESSFUL_RESPONSE = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
        SUCCESSFUL_RESPONSE.setEntity(new StringEntity("success content"));
        NOT_FOUND_RESPONSE = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 404, "NOT FOUND");
        NOT_FOUND_RESPONSE.setEntity(new InputStreamEntity(INPUT_STREAM, 1));
        UNEXPECTED_RESPONSE = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 500, "NOT FOUND");
        UNEXPECTED_RESPONSE.setEntity(new InputStreamEntity(INPUT_STREAM, 1));
    }

    @Test
    public void testConstructor() throws Exception {
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        ContentDAOImpl impl = new ContentDAOImpl(properties, resolver, logger);
    }

    @Test
    public void testInnerClass() throws Exception {
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        Class clazz = Whitebox.getInnerClassType(ContentDAOImpl.class, "DefaultHttpClientFactory");
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor = constructors[0];
        ContentDAOImpl impl = new ContentDAOImpl(properties, resolver, logger);
        DefaultHttpClientFactory innerClass = (DefaultHttpClientFactory) constructor.newInstance(impl, properties);
        /*
         * DefaultHttpClientFactory innerClass = (DefaultHttpClientFactory)
         * constructor .newInstance(new
         * ContentDAOImpl(properties,resolver,logger));
         */
        innerClass.httpClient();
    }

    @Test
    public void shouldReturnInputStreamWhenResponseIsSuccessful() throws Exception {
        when(resolver.contentPath("brand", "key")).thenReturn(TARGET_URL);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(SUCCESSFUL_RESPONSE);
        DAOResponse<Response> response = getContentDAO().content("brand", "key");
        assertThat(response.getError(), is(nullValue()));
        assertThat(IOUtils.toString((InputStream) response.getResult().getEntity()), is("success content"));
    }

    @Test
    public void shouldReturnContentNotFoundError() throws Exception {
        when(resolver.contentPath("brand", "key")).thenReturn(TARGET_URL);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(NOT_FOUND_RESPONSE);
        DAOResponse<Response> response = getContentDAO().content("brand", "key");
        assertThat(response.getError(),
                is(new DAOError(CONTENT_NOT_FOUND, "Content not found for: " + TARGET_URL.toString())));
        verify(INPUT_STREAM).close();
    }

    @Test
    public void shouldReturnUnexpectedResponseCodeError() throws Exception {
        when(resolver.contentPath("brand", "key")).thenReturn(TARGET_URL);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(UNEXPECTED_RESPONSE);
        DAOResponse<Response> response = getContentDAO().content("brand", "key");
        assertThat(response.getError(), is(new DAOError(UNEXPECTED_RESPONSE_CODE,
                "Unexpected http code '" + 500 + "' for: " + TARGET_URL.toString())));
        verify(INPUT_STREAM).close();
    }

    @Test
    public void shouldReturnConnectionErrorWenHttpClientThrowsIOException() throws Exception {
        when(resolver.contentPath("brand", "key")).thenReturn(TARGET_URL);
        when(httpClient.execute(any(HttpGet.class))).thenThrow(IO_EXCEPTION);
        DAOResponse<Response> response = getContentDAO().content("brand", "key");
        assertThat(response.getError(),
                is(new DAOError(CONNECTION_ERROR, "Cannot connect to: " + TARGET_URL.toString())));
        verify(logger).logException(ContentDAOImpl.class, IO_EXCEPTION);
    }

    @Test
    public void shouldReturnConnectionErrorWenHttpClientThrowsProtocolException() throws Exception {
        when(resolver.contentPath("brand", "key")).thenReturn(TARGET_URL);
        when(httpClient.execute(any(HttpGet.class))).thenThrow(PROTOCOL_EXCEPTION);
        DAOResponse<Response> response = getContentDAO().content("brand", "key");
        assertThat(response.getError(),
                is(new DAOError(CONNECTION_ERROR, "Cannot connect to: " + TARGET_URL.toString())));
        verify(logger).logException(ContentDAOImpl.class, PROTOCOL_EXCEPTION);
    }

    private ContentDAOImpl getContentDAO() {
        return new ContentDAOImpl(resolver, factory(httpClient), logger);
    }

    private HttpClientFactory factory(final HttpClient httpClient) {
        return new HttpClientFactory() {
            public HttpClient httpClient() {
                return httpClient;
            }
        };
    }
}