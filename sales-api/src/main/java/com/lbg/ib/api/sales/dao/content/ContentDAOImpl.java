package com.lbg.ib.api.sales.dao.content;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static javax.ws.rs.core.MediaType.TEXT_HTML_TYPE;
import static org.apache.http.util.EntityUtils.consume;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Component
public class ContentDAOImpl implements ContentDAO {

    public static final String             CONTENT_NOT_FOUND        = "9910002";
    public static final String             UNEXPECTED_RESPONSE_CODE = "unexpectedResponseCode";
    public static final String             CONNECTION_ERROR         = "connectionError";
    public static final String             HOST_HEADER              = "Host";
    private final ContentTargetUrlResolver resolver;
    private final HttpClientFactory        httpClientFactory;
    private final LoggerDAO                logger;
    private CacheControl                   cacheControl             = new CacheControl();
    
    @Autowired
    private  RestTemplate restTemplate;
    
    @Autowired
    private GalaxyErrorCodeResolver galaxyErrorCodeResolver;

    @Autowired
    public ContentDAOImpl(ApiServiceProperties properties, ContentTargetUrlResolver resolver, LoggerDAO logger) {
        this.resolver = resolver;
        this.httpClientFactory = new DefaultHttpClientFactory(properties);
        this.logger = logger;
        populateCacheHeaders();
    }

    void populateCacheHeaders() {
        // set to 12 hours
        this.cacheControl.setMaxAge(43200);
    }

    // used by unit test only
    ContentDAOImpl(ContentTargetUrlResolver resolver, HttpClientFactory factory, LoggerDAO logger) {
        this.resolver = resolver;
        this.httpClientFactory = factory;
        this.logger = logger;
    }

    @TraceLog
    public DAOResponse<Response> content(String brand, String key) {
        URI targetUrl = resolver.contentPath(brand, key);
        String host = resolver.hostPath(brand);
        try {
            HttpGet httpGet = new HttpGet(targetUrl);
            httpGet.setHeader(HOST_HEADER, host);
            HttpResponse response = httpClientFactory.httpClient().execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                ResponseBuilder responseBuilder = new ResponseBuilderImpl().status(200).type(TEXT_HTML_TYPE);
                responseBuilder.cacheControl(cacheControl);
                return withResult(responseBuilder.entity(response.getEntity().getContent()).build());
            } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                consume(response.getEntity());
                return withError(new DAOError(CONTENT_NOT_FOUND, "Content not found for: " + targetUrl.toString()));
            } else {
                consume(response.getEntity());
                logger.logError(UNEXPECTED_RESPONSE_CODE, "Response returns with status code" + statusCode,
                        this.getClass());
                return withError(new DAOError(UNEXPECTED_RESPONSE_CODE,
                        "Unexpected http code '" + 500 + "' for: " + targetUrl.toString()));
            }
        } catch (ClientProtocolException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(CONNECTION_ERROR, "Cannot connect to: " + targetUrl.toString()));
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(CONNECTION_ERROR, "Cannot connect to: " + targetUrl.toString()));
        }
    }
    
    
    @TraceLog
    public <T> T getContent(String tag, String key, Class<T> t) {
        URI targetUrl = resolver.contentPathURI(tag, key);
        logger.traceLog(this.getClass(), ":::Fetching content from "+targetUrl.getHost()+":::PATH:::"+targetUrl.getPath());
        Object obj;
        try {
            obj = restTemplate.getForObject(targetUrl, Class.forName(t.getName()));
            if(obj!=null) {
                logger.traceLog(this.getClass(), "::: Found File & Created the Object of "+obj.getClass().getName());
            }
            return (T) obj;
        } catch (RestClientException e) {
            logger.logException(this.getClass(), e);
            throw new ServiceException(galaxyErrorCodeResolver.resolve(CONTENT_NOT_FOUND));
        } catch (ClassNotFoundException e) {
            logger.logException(this.getClass(), e);
            throw new ServiceException(galaxyErrorCodeResolver.resolve(ResponseErrorConstants.BACKEND_ERROR));
            
        }
    }
    
    
    
    public DAOResponse<Response> contentManager(String brand, String key) {
        
        URI targetUrl = resolver.contentManager(brand, key);
        
        String host = resolver.hostPathContentManager(brand);
        try {
            HttpGet httpGet = new HttpGet(targetUrl);
            httpGet.setHeader(HOST_HEADER, host);
            HttpResponse response = httpClientFactory.httpClient().execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                ResponseBuilder responseBuilder = new ResponseBuilderImpl().status(200).type(TEXT_HTML_TYPE);
                responseBuilder.cacheControl(cacheControl);
                return withResult(responseBuilder.entity(response.getEntity().getContent()).build());
            } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                consume(response.getEntity());
                return withError(new DAOError(CONTENT_NOT_FOUND, "Content not found for: " + targetUrl.toString()));
            } else {
                consume(response.getEntity());
                logger.logError(UNEXPECTED_RESPONSE_CODE, "Response returns with status code" + statusCode, this.getClass());
                return withError(new DAOError(UNEXPECTED_RESPONSE_CODE, "Unexpected http code '" + 500 + "' for: " + targetUrl.toString()));
            }
        } catch (ClientProtocolException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(CONNECTION_ERROR, "Cannot connect to: " + targetUrl.toString()));
        } catch (IOException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(CONNECTION_ERROR, "Cannot connect to: " + targetUrl.toString()));
        }

    }

    class DefaultHttpClientFactory implements HttpClientFactory {
        private final ApiServiceProperties properties;

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
