package com.lbg.ib.api.sales.docupload.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @InjectMocks
    private DocuploadUtils      utils;

    private static final String ENDPOINT    = "http://myDevServer.com";

    private static final String APPEND_PATH = "test";

    /*
     * @Before public void setUp() throws Exception {
     * 
     * utils = new DocuploadUtils();
     * PowerMockito.mockStatic(DocuploadUtils.class);
     * 
     * }
     */

    @Test
    public void testAppendURLPath() throws URISyntaxException, MalformedURLException {

        URL url = DocuploadUtils.appendURLPath(ENDPOINT, APPEND_PATH);
        Assert.notNull(url);
        String urlStr = url.toString();
        Assert.isTrue(urlStr.endsWith(APPEND_PATH));

    }

}
