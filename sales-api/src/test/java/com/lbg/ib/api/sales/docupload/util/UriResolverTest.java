package com.lbg.ib.api.sales.docupload.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UriResolverTest {

    @InjectMocks
    private UriResolver              uriResolver;
    @Mock
    private Map<SalsaEndpoints, URL> endpoints;

    private URL                      getCaseEndpoint;
    private static final String      GET_CASE_ENDPOINT = "http://dns/context/getCaseEndpoint";

    @Before
    public void setup() {
        try {
            getCaseEndpoint = new URL(GET_CASE_ENDPOINT);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetURL() {
        when(endpoints.get(any(SalsaEndpoints.class))).thenReturn(getCaseEndpoint);
        String url = uriResolver.getEndpoint(SalsaEndpoints.GET_CASE_ENDPOINT);
        assertEquals(GET_CASE_ENDPOINT, url);
    }

}
