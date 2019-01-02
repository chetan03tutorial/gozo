package com.lbg.ib.api.sales.dao.content;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Component
public class ContentTargetUrlResolver {
    private final ApiServiceProperties properties;
    private LoggerDAO logger;

    private static final String PRODUCT_MNEMONIC = "product_mnemonic";

    @Autowired
    public ContentTargetUrlResolver(ApiServiceProperties properties, LoggerDAO logger) {
        this.properties = properties;
        this.logger = logger;
    }

    public URI contentPath(String channel, String key) {
        URL base = properties.getUrlEndPointForContent(channel);
        try {
            // return base == null ? null : appendToURI(base, key);
            URI baseURI = base == null ? null : appendToURI(base, key);
            if (baseURI != null) {
                if (!baseURI.toString().contains("mock")) {
                    String clientId = properties.getclientID(channel);
                    String secretKey = properties.getSecretKey(channel);
                    if (StringUtils.isNotEmpty(clientId) && StringUtils.isNotEmpty(secretKey)) {
                        return appendToURI(baseURI, clientId + "&" + secretKey);
                    }
                }
            }
            return baseURI;
            // return baseURI;
        } catch (URISyntaxException e) {
            logger.logException(ContentTargetUrlResolver.class, e);
            return null;
        }
    }
    
    
    public URI contentPathURI(String tag, String key) {

        URL base = properties.getUrlEndPointForContentManger(tag);
        try {
            URI baseURI = base == null ? null : appendToURI(base, key);
            if (baseURI != null && !"lloyds.client.web.application.container.uri".equals(tag)) {
                if (!baseURI.toString().contains("mock")) {
                    String clientId = properties.getclientID(tag);
                    String secretKey = properties.getSecretKey(tag);
                    if (StringUtils.isNotEmpty(clientId) && StringUtils.isNotEmpty(secretKey)) {
                        return appendToURI(baseURI, clientId + "&" + secretKey);
                    }
                }
            }
            return baseURI;
        } catch (URISyntaxException e) {
            logger.logException(ContentTargetUrlResolver.class, e);
            return null;
        }
    
    }

    private URI appendToURI(URI baseURI, String clientIDSecretKey) throws URISyntaxException {
        if (baseURI != null) {
            String path = baseURI.toString();
            return new URI(path + "?" + clientIDSecretKey);
        } else {
            return null;
        }
    }

    public String hostPath(String channel) {
        String host = properties.getHostForContent(channel).toString();
        if (host.contains("https://")) {
            host = host.replace("https://", "");
        } else {
            host = host.replace("http://", "");
        }
        host = host.substring(0, host.indexOf("/"));
        return host;
    }
    
    public String hostPathContentManager(String channel) {
        String host = properties.getUrlEndPointForContentManger(channel).toString();
        if (host.contains("https://")) {
            host = host.replace("https://", "");
        } else {
            host = host.replace("http://", "");
        }
        if(host.contains("/")) {
            host = host.substring(0, host.indexOf("/")); 
        }
        
        
        return host;
    }
    
    public String hostPathManager(String channel) {
        String host = properties.getHostForContent(channel).toString();
        if (host.contains("https://")) {
            host = host.replace("https://", "");
        } else {
            host = host.replace("http://", "");
        }
        host = host.substring(0, host.indexOf("/"));
        return host;
    }

    private URI appendToURI(URL base, String key) throws URISyntaxException {
        String path = base.toString();
        if (path.endsWith("/")) {
            return new URI(path + key);
        } else {
            return new URI(path + "/" + key);
        }
    }

    public URI getUriEndpointForProducSelectorContent(String channel, String key, String path) {
        String customKey = "pca/common/" + key + "/product_details_br001";
        if (null != path && !path.isEmpty()) {
            customKey = "pca/common/" + key + "/" + path;
        }
        URL base = properties.getUrlEndPointForContent(channel);
        try {
            // return base == null ? null : appendToURI(base, key);
            URI baseURI = base == null ? null : appendToURI(base, customKey);
            if (baseURI != null) {
                if (!baseURI.toString().contains("mock")) {
                    String clientId = properties.getclientID(channel);
                    String secretKey = properties.getSecretKey(channel);
                    if (StringUtils.isNotEmpty(clientId) && StringUtils.isNotEmpty(secretKey)) {
                        return appendToURI(baseURI, clientId + "&" + secretKey);
                    }
                }
            }
            return baseURI;
            // return baseURI;
        } catch (URISyntaxException e) {
            logger.logException(ContentTargetUrlResolver.class, e);
            return null;
        }
    }

    private URI updateURI(URL base, String key, String lookup) throws URISyntaxException {
        String path = base.toString();
        String trimKey = key;
        if (StringUtils.isNotEmpty(key)) {
            trimKey = key.trim();
        }
        path = StringUtils.replace(path, lookup, trimKey);
        try {
            URL url = new URL(path);
            return url.toURI();
        } catch (MalformedURLException e) {
            logger.logException(this.getClass(), e);
        }

        return null;
    }
    
    
    
    public URI contentManager(String channel, String key) {
        URL base = properties.getUrlEndPointForContentManger(channel);
        try {
            // return base == null ? null : appendToURI(base, key);
            URI baseURI = base == null ? null : appendToURI(base, key);
            if (baseURI != null) {
                if (!baseURI.toString().contains("mock")) {
                    String clientId = properties.getclientID(channel);
                    String secretKey = properties.getSecretKey(channel);
                    if (StringUtils.isNotEmpty(clientId) && StringUtils.isNotEmpty(secretKey)) {
                        return appendToURI(baseURI, clientId + "&" + secretKey);
                    }
                }
            }
            return baseURI;
            // return baseURI;
        } catch (URISyntaxException e) {
            logger.logException(ContentTargetUrlResolver.class, e);
            return null;
        }
    }


}
