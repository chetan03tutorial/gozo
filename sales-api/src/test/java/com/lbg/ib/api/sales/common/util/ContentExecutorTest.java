package com.lbg.ib.api.sales.common.util;

/**
 * Created by ksingh on 03/13/2017.
 */

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.content.ContentTargetUrlResolver;
import com.lbg.ib.api.sales.product.util.ContentExecutor;
import com.lbg.ib.api.sales.product.util.ContentExecutor.GetContentThread;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;
import com.lloydstsb.ea.referencedata.ReferenceDataItem;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CloseableHttpClient.class})
@PowerMockIgnore("org.apache.http.conn.ssl.*")
public class ContentExecutorTest {

    @Mock
    private LoggerDAO logger;

    @Mock
    private ApiServiceProperties properties;

    @Mock
    private HttpGet httpGet;

    @Mock
    private HttpContext context;

    @Mock
    private ContentTargetUrlResolver resolver;

    @Mock
    CloseableHttpClient httpClient;

    @Mock
    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    @Test
    public void returnValueIfTheKeyIsValid() throws IOException {
        ContentExecutor executor = new ContentExecutor(resolver, logger, properties);
        ReferenceDataItem item = Mockito.mock(ReferenceDataItem.class);
        when(item.getValue()).thenReturn("someValue");
        List<ReferenceDataItem> items = new ArrayList<ReferenceDataItem>();
        items.add(item);
        URI uri = PowerMockito.mock(URI.class);
        when(resolver.getUriEndpointForProducSelectorContent(any(String.class), any(String.class), any(String.class))).thenReturn(uri);
        when(resolver.hostPath(any(String.class))).thenReturn("mytest.abc.net/cwa");
        List<Object> list = new ArrayList<Object>();
        list.add("dummy");
        executor.requestContent(list, "https://mytest.abc.net/cwa", any(String.class));
        Properties applicationProperties = ApplicationProperties.getProperties();
        String value = applicationProperties.getProperty("sales-api.icobs.urca.arrangement.question.ids");
        Assert.assertEquals("WWTI,MOPI,AAUR,HECP,UQ1,UQ2,UQ6", value);
    }

    @Test
    public void returnValueIfTheKeyIsNull() throws IOException {
        ContentExecutor executor = new ContentExecutor(resolver, logger, properties);
        ReferenceDataItem item = Mockito.mock(ReferenceDataItem.class);
        when(item.getValue()).thenReturn(null);
        List<ReferenceDataItem> items = new ArrayList<ReferenceDataItem>();
        items.add(item);
        URI uri = PowerMockito.mock(URI.class);
        when(resolver.getUriEndpointForProducSelectorContent(any(String.class), any(String.class), any(String.class))).thenReturn(uri);
        when(resolver.hostPath(any(String.class))).thenReturn("mytest.abc.net/cwa");
        List<Object> list = new ArrayList<Object>();
        list.add("dummy");
        executor.requestContent(list, "https://mytest.abc.net/cwa", any(String.class));
        Properties applicationProperties = ApplicationProperties.getProperties();
        String value = applicationProperties.getProperty("sales-api.icobs.urca.arrangement.question.ids");
        Assert.assertEquals("WWTI,MOPI,AAUR,HECP,UQ1,UQ2,UQ6", value);
    }

    @Test
    public void returnValueIfTheKeyIsEmptyString() throws IOException {
        ContentExecutor executor = new ContentExecutor(resolver, logger, properties);
        ReferenceDataItem item = Mockito.mock(ReferenceDataItem.class);
        when(item.getValue()).thenReturn("");
        List<ReferenceDataItem> items = new ArrayList<ReferenceDataItem>();
        items.add(item);
        URI uri = PowerMockito.mock(URI.class);
        when(resolver.getUriEndpointForProducSelectorContent(any(String.class), any(String.class), any(String.class))).thenReturn(uri);
        when(resolver.hostPath(any(String.class))).thenReturn("mytest.abc.net/cwa");
        List<Object> list = new ArrayList<Object>();
        list.add("dummy");
        executor.requestContent(list, "https://mytest.abc.net/cwa", null);
        Properties applicationProperties = ApplicationProperties.getProperties();
        String value = applicationProperties.getProperty("sales-api.icobs.urca.arrangement.question.ids");
        Assert.assertEquals("WWTI,MOPI,AAUR,HECP,UQ1,UQ2,UQ6", value);
    }

    @Test
    public void returnValueIfTheKeyIsValid2() throws IOException {
        ContentExecutor executor = new ContentExecutor(resolver, logger, properties);
        ReferenceDataItem item = Mockito.mock(ReferenceDataItem.class);
        when(item.getValue()).thenReturn("someValue");
        List<ReferenceDataItem> items = new ArrayList<ReferenceDataItem>();
        items.add(item);
        URI uri = PowerMockito.mock(URI.class);
        when(resolver.getUriEndpointForProducSelectorContent(any(String.class), any(String.class), any(String.class))).thenReturn(uri);
        when(resolver.hostPath(any(String.class))).thenReturn("mytest.abc.net/cwa");
        List<Object> list = new ArrayList<Object>();
        list.add("dummy");
        executor.requestContent(list, "https://mytest.abc.net/cwa", null);
        CompletionService service = PowerMockito.mock(CompletionService.class);
        Future future = PowerMockito.mock(Future.class);
        try {
            when(service.take()).thenReturn(future);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Properties applicationProperties = ApplicationProperties.getProperties();
        String value = applicationProperties.getProperty("sales-api.icobs.urca.arrangement.question.ids");
        Assert.assertEquals("WWTI,MOPI,AAUR,HECP,UQ1,UQ2,UQ6", value);
    }

    @Test(expected = Exception.class)
    public void testInnerClass() throws Exception {
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        Class clazz = Whitebox.getInnerClassType(ContentExecutor.class, "GetContentThread");
        CloseableHttpClient client = PowerMockito.mock(CloseableHttpClient.class);
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor = constructors[0];
        ContentExecutor impl = new ContentExecutor(resolver, logger, properties);
        GetContentThread innerClass = (GetContentThread) constructor.newInstance(impl, client, httpGet);
        CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
        StatusLine line = PowerMockito.mock(StatusLine.class);
        when(line.getStatusCode()).thenReturn(200);
        response.setStatusLine(line);
        HttpEntity entity = PowerMockito.mock(HttpEntity.class);
        Header head = PowerMockito.mock(Header.class);
        when(head.getValue()).thenReturn("headerValue");
        when(entity.getContentType()).thenReturn(head);
        response.setEntity(entity);
        response.setStatusCode(200);
        when(response.getStatusLine()).thenReturn(line);
        when(response.getEntity()).thenReturn(entity);
        InputStream stream = PowerMockito.mock(InputStream.class);
        when(client.execute(any(HttpGet.class), any(BasicHttpContext.class))).thenReturn(response);
        innerClass.call();
    }

    @Test
    public void testInnerClasswithDifferentStatusCode() throws Exception {
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        Class clazz = Whitebox.getInnerClassType(ContentExecutor.class, "GetContentThread");
        CloseableHttpClient client = PowerMockito.mock(CloseableHttpClient.class);
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor = constructors[0];
        ContentExecutor impl = new ContentExecutor(resolver, logger, properties);
        GetContentThread innerClass = (GetContentThread) constructor.newInstance(impl, client, httpGet);
        CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
        StatusLine line = PowerMockito.mock(StatusLine.class);
        when(line.getStatusCode()).thenReturn(402);
        response.setStatusLine(line);
        HttpEntity entity = PowerMockito.mock(HttpEntity.class);
        Header head = PowerMockito.mock(Header.class);
        when(head.getValue()).thenReturn("headerValue");
        when(entity.getContentType()).thenReturn(head);
        response.setEntity(entity);
        response.setStatusCode(200);
        when(response.getStatusLine()).thenReturn(line);
        when(response.getEntity()).thenReturn(entity);
        InputStream stream = PowerMockito.mock(InputStream.class);
        // stream.toString()
        // when(response.getEntity().getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class), any(BasicHttpContext.class))).thenReturn(response);
        innerClass.call();
    }

    @Test
    public void testInnerClasswithDifferentContent() throws Exception {
        PowerMockito.mockStatic(LoggingServiceFactory.class);
        Class clazz = Whitebox.getInnerClassType(ContentExecutor.class, "GetContentThread");
        CloseableHttpClient client = PowerMockito.mock(CloseableHttpClient.class);
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor = constructors[0];
        ContentExecutor impl = new ContentExecutor(resolver, logger, properties);
        GetContentThread innerClass = (GetContentThread) constructor.newInstance(impl, client, httpGet);
        CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
        StatusLine line = PowerMockito.mock(StatusLine.class);
        when(line.getStatusCode()).thenReturn(200);
        response.setStatusLine(line);
        HttpEntity entity = PowerMockito.mock(HttpEntity.class);
        Header head = PowerMockito.mock(Header.class);
        when(head.getValue()).thenReturn("headerValue");
        when(entity.getContentType()).thenReturn(head);
        response.setEntity(entity);
        response.setStatusCode(200);
        when(response.getStatusLine()).thenReturn(line);
        when(response.getEntity()).thenReturn(entity);
        InputStream stream = PowerMockito.mock(InputStream.class);
        // PowerMockito.mockStatic(InputStream.class);
        byte[] b = new byte[20];

        when(stream.read(any(byte[].class))).thenReturn(-1);
        // stream.toString()org.apache.commons.io
        when(response.getEntity().getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class), any(BasicHttpContext.class))).thenReturn(response);
        PowerMockito.mockStatic(org.apache.commons.io.IOUtils.class);
        when(org.apache.commons.io.IOUtils.toByteArray(stream)).thenThrow(IOException.class);
        innerClass.call();
    }

}
