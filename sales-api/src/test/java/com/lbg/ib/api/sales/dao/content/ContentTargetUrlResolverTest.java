package com.lbg.ib.api.sales.dao.content;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ContentTargetUrlResolverTest {
    private ApiServiceProperties properties = mock(ApiServiceProperties.class);
    private LoggerDAO logger = mock(LoggerDAO.class);

    @Test
    public void shouldCreateExpectedTargetUrlProductSelectorContent() throws Exception {
        when(properties.getUriEndpointForProducSelectorContent(any(String.class))).thenReturn(new URL("http://host/target"));
        when(properties.getUrlEndPointForContent("brand")).thenReturn(new URL("http://host/target"));
        URI path = new ContentTargetUrlResolver(properties, null).getUriEndpointForProducSelectorContent("brand",
                "key", any(String.class));

        assertTrue(path.toString().contains("http://host/target"));
    }

    @Test
    public void shouldCreateExpectedTargetUrl() throws Exception {
        when(properties.getUrlEndPointForContent("brand")).thenReturn(new URL("http://host/target"));

        URI path = new ContentTargetUrlResolver(properties, null).contentPath("brand", "key");

        assertThat(path, is(new URI("http://host/target/key")));
    }

    @Test
    public void shouldCreateExpectedTargetUrlWhenBaseUrlEndsWithSlash() throws Exception {
        when(properties.getUrlEndPointForContent("brand")).thenReturn(new URL("http://host/target/"));

        URI path = new ContentTargetUrlResolver(properties, null).contentPath("brand", "key");

        assertThat(path, is(new URI("http://host/target/key")));
    }

    @Test
    public void shouldReturnNullWhenThereIsNoTargetUrlSpecifiedForTheBrand() throws Exception {
        URI path = new ContentTargetUrlResolver(properties, null).contentPath("brand", "key");

        assertThat(path, is(nullValue()));
    }

    @Test
    public void shouldExtractHostInformationFromHttpURI() throws Exception {
        when(properties.getHostForContent("brand1")).thenReturn(new URL("http://host/target/"));
        when(properties.getHostForContent("brand2")).thenReturn(new URL("http://host:8080/target/"));

        String path = new ContentTargetUrlResolver(properties, null).hostPath("brand1");
        assertThat(path, is("host"));

        path = new ContentTargetUrlResolver(properties, null).hostPath("brand2");
        assertThat(path, is("host:8080"));
    }

    @Test
    public void shouldExtractHostInformationFromHttpsURI() throws Exception {
        when(properties.getHostForContent("brand1")).thenReturn(new URL("https://host/target/"));
        when(properties.getHostForContent("brand2")).thenReturn(new URL("https://host:8080/target/"));

        String path = new ContentTargetUrlResolver(properties, null).hostPath("brand1");
        assertThat(path, is("host"));

        path = new ContentTargetUrlResolver(properties, null).hostPath("brand2");
        assertThat(path, is("host:8080"));
    }

    @Test
    public void shouldCreateExpectedTargetUrl2() throws Exception {
        when(properties.getUrlEndPointForContent("brand")).thenReturn(new URL("http://host/target"));
        when(properties.getclientID(any(String.class))).thenReturn("232323");
        when(properties.getSecretKey(any(String.class))).thenReturn("232323");

        URI path = new ContentTargetUrlResolver(properties, null).contentPath("brand", "key");

        assertThat(path, is(new URI("http://host/target/key?232323&232323")));
    }

    @Test
    public void shouldCreateExpectedTargetUrlProductSelectorContent2() throws Exception {

        when(properties.getUriEndpointForProducSelectorContent("brand")).thenReturn(new URL("http://host/target/mock"));
        when(properties.getUrlEndPointForContent("brand")).thenReturn(new URL("http://host/target"));
        when(properties.getclientID(any(String.class))).thenReturn("232323");
        when(properties.getSecretKey(any(String.class))).thenReturn("232323");

        URI path = new ContentTargetUrlResolver(properties, logger).getUriEndpointForProducSelectorContent("brand",
                "key", any(String.class));

        assertTrue(path.toString().contains("http://host/target"));
    }

    @Test(expected = Exception.class)
    public void shouldCreateExpectedTargetUrlProductSelectorContentException() throws Exception {
        URL url = PowerMockito.mock(URL.class);
        when(properties.getUriEndpointForProducSelectorContent("brand")).thenReturn(url);
        when(properties.getUrlEndPointForContent("brand")).thenReturn(new URL("http://host/target"));
        when(properties.getclientID(any(String.class))).thenThrow(Exception.class);
        when(properties.getSecretKey(any(String.class))).thenReturn("232323");

        URI path = new ContentTargetUrlResolver(properties, logger).getUriEndpointForProducSelectorContent("brand",
                "key", any(String.class));

        Assert.assertNull(path);
    }

    @Test(expected = Exception.class)
    public void shouldCreateExpectedTargetUrlProductSelectorContentException2() throws Exception {
        URL url = PowerMockito.mock(URL.class);
        when(properties.getUriEndpointForProducSelectorContent("brand")).thenReturn(url);
        when(properties.getUrlEndPointForContent("brand")).thenReturn(new URL("http://host/target"));
        when(properties.getclientID(any(String.class))).thenThrow(Exception.class);
        when(properties.getSecretKey(any(String.class))).thenReturn("232323");

        URI path = new ContentTargetUrlResolver(properties, logger).getUriEndpointForProducSelectorContent("brand",
                "key", any(String.class));

        Assert.assertNull(path);
    }

    @Test
    public void shouldCreateExpectedTargetUrlWithException() throws Exception {
        URL url = PowerMockito.mock(URL.class);
        when(properties.getUrlEndPointForContent("brand")).thenReturn(url/*
                                                                          * new
                                                                          * URL("http://host/target/"
                                                                          * )
                                                                          */);
        when(properties.getclientID(any(String.class))).thenThrow(Exception.class);
        when(properties.getSecretKey(any(String.class))).thenReturn("232323");

        URI path = new ContentTargetUrlResolver(properties, logger).contentPath("brand", "key");

        Assert.assertNull(path);
    }

}