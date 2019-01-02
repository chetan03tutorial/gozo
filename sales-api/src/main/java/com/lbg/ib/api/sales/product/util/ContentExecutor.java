package com.lbg.ib.api.sales.product.util;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.content.ContentTargetUrlResolver;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
public class ContentExecutor {

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    private ContentTargetUrlResolver resolver;
    public static final String HOST_HEADER = "Host";

    private LoggerDAO logger;

    private ApiServiceProperties properties;

    private HttpClient httpClient;

    private HttpClientFactory httpClientFactory;

    @Autowired
    public ContentExecutor(ContentTargetUrlResolver resolver, LoggerDAO logger, ApiServiceProperties properties) {
        this.poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        this.resolver = resolver;
        this.logger = logger;
        this.properties = properties;
        this.httpClientFactory = new DefaultHttpClientFactory(properties);

    }

    /**
     * @param refDataItems
     * @param channel
     * @return
     */

    public Map<String, String> requestContent(List<Object> refDataItems, String channel, String path) {
        String host = resolver.hostPath(channel);
        ExecutorService executorService = Executors.newFixedThreadPool(refDataItems.size());
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);
        Map<String, String> productContents = new HashMap<String, String>();
        int timeOut = this.properties.contentServiceBackendCallTimeoutInMillis();
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
        for (Object referenceDataItem : refDataItems) {
            String productKey = (String) referenceDataItem;
            if (null != productKey && !"".equals(productKey)) {
                URI contentPath = resolver.getUriEndpointForProducSelectorContent(channel, productKey, path);
                HttpGet httpGet = new HttpGet(contentPath);
                httpGet.setHeader(HOST_HEADER, host);
                httpClient = httpClientFactory.httpClient();
                Callable<String> getContentThread = new GetContentThread(httpClient, httpGet);
                completionService.submit(getContentThread);
                try {
                    Future<String> future = completionService.take();
                    String productContent = future.get();
                    if (null != productContent) {
                        productContents.put(productKey, productContent);
                    }
                } catch (InterruptedException e) {
                    logger.logException(this.getClass(), e);
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    logger.logException(this.getClass(), e);
                }
            }
        }
        executorService.shutdown();

        return productContents;
    }

    /**
     * A thread that performs a GET.
     */
    public class GetContentThread implements Callable<String> {

        private static final String UTF_8 = "UTF-8";
        private final HttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;

        public GetContentThread(HttpClient httpClient, HttpGet httpget) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        public String call() {
            InputStream responseStream = null;
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpget, context);
                int statusCode = response.getStatusLine().getStatusCode();
                String actualContentType = null;
                Header contentType = response.getEntity().getContentType();
                if (null != contentType) {

                    actualContentType = contentType.getValue();
                }
                if (statusCode == 200) {
                    DAOResponse<Response> entityResponse = withResult(new ResponseBuilderImpl().status(200).type(actualContentType).entity(response.getEntity().getContent()).build());
                    responseStream = (InputStream) entityResponse.getResult().getEntity();
                    byte[] bytes = extractContent(responseStream);
                    String responseAsString = new String(bytes, UTF_8);
                    return responseAsString;
                } else {
                    if (logger.debugEnabled()) {
                        logger.logDebug(this.getClass(), "content not found for product", httpget.getURI().toString());
                    }

                }

            } catch (UnsupportedEncodingException e) {
                logger.logException(this.getClass(), e);
            } catch (IllegalStateException e) {
                logger.logException(this.getClass(), e);
            } catch (IOException e) {
                logger.logException(this.getClass(), e);
            } finally {
                if (null != responseStream) {
                    try {
                        responseStream.close();
                    } catch (IOException e) {
                        logger.logException(this.getClass(), e);
                    }
                }
            }
            return null;

        }

        private byte[] extractContent(InputStream content) {
            try {
                return IOUtils.toByteArray(content);
            } catch (IOException e) {
                logger.logException(this.getClass(), e);

                return "Content Unreadable".getBytes();
            }
        }
    }

    private class DefaultHttpClientFactory implements HttpClientFactory {
        private ApiServiceProperties properties;

        public DefaultHttpClientFactory(ApiServiceProperties properties) {
            this.properties = properties;
        }

        public HttpClient httpClient() {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, properties.contentServiceBackendCallTimeoutInMillis());
            return new DefaultHttpClient(params);
        }
    }

    interface HttpClientFactory {
        HttpClient httpClient();
    }
}