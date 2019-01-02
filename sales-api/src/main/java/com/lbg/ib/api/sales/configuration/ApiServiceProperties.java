package com.lbg.ib.api.sales.configuration;

import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static javax.ws.rs.core.UriBuilder.fromUri;

@Component
public class ApiServiceProperties {
    private static final String API_SERVICE_ENDPOINTS = "API_SERVICE_ENDPOINTS";

    private ConfigurationDAO    configuration;

    @Autowired
    public ApiServiceProperties(ConfigurationDAO configuration) throws IOException {
        this.configuration = configuration;
    }

    public String getDeviceUrl(String brand) {
        return configuration.getConfigurationValue("DEVICE_URL", brand).toString();
    }

    public String getThreatMatrixDetailsFromConfig(String sectionTag, String brand) {
        return configuration.getConfigurationValue(sectionTag, brand).toString();
    }

    public URI getUriEndpointForPostcodeCheckAddress() {
        try {
            return getUrl(configuration.getConfigurationValue("API_SERVICE_ENDPOINTS", "ADDRESS_SEARCH").toString())
                    .toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public URI getUriEndpointForProductHolding() {
        try {
            return getUrl(configuration.getConfigurationValue("API_SERVICE_ENDPOINTS", "PRODUCT_HOLDING").toString())
                    .toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public URI getUriEndpointForRetrieveArrangmentQuestionnaireService() {
        URI uri = null;
        try {
            uri = getUrl(configuration
                    .getConfigurationValue("API_SERVICE_ENDPOINTS", "RETRIEVE_ARRANGEMENT_QUESTIONNAIRE_SERVICE")
                    .toString()).toURI();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }

    public URL getUrlEndPointForContent(String channel) {
        try {
            return fromUri(configuration.getConfigurationValue("IIS_CMS_SERVER_URL_NSP", channel).toString())
                    .path(configuration.getConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", channel)
                            .toString())
                    .path(configuration.getConfigurationValue("Locale", "Language").toString())
                    .path(configuration.getConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", channel).toString())
                    .build().toURL();
            
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    
    

    public URI getUriEndpointForTokenValidation() {
        try {
            return getUrl(configuration.getConfigurationValue("API_SERVICE_ENDPOINTS", "TOKEN_VALIDATION").toString())
                    .toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getclientID(String channel) {
        return configuration.getConfigurationValue("CONTENTSERVICE_CLIENT_ID", channel).toString();
    }

    public String getSecretKey(String channel) {
        return configuration.getConfigurationValue("CONTENTSERVICE_CLIENT_SECRET", channel).toString();
    }

    public URL getHostForContent(String channel) {
        String path = configuration.getConfigurationValue("IIS_CMS_SERVER_URL_NSP", channel).toString();
        return path == null ? null : getUrl(path);
    }
    

    public URI getUriEndpointForRecordQuestionnaire() {
        try {
            return getUrl(configuration
                    .getConfigurationValue("API_SERVICE_ENDPOINTS", "RECORD_ARRANGEMENT_QUESTIONNAIRE_SERVICE")
                    .toString()).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer soapCommunicatorTimeoutInMillis() {
        String content = configuration.getConfigurationValue("TIMEOUTS", "SOAP_COMMUNICATOR_TIMEOUT_IN_MILLIS")
                .toString();
        return content == null ? 5000 : parseInt(content);
    }

    public Integer contentServiceBackendCallTimeoutInMillis() {
        String content = configuration
                .getConfigurationValue("TIMEOUTS", "CONTENT_SERVICE_BACKEND_CALL_TIMEOUT_IN_MILLIS").toString();
        return content == null ? 5000 : parseInt(content);
    }

    public URL getUriEndpointForProducSelectorContent(String channel) {
        try {
            return fromUri(configuration.getConfigurationValue("IIS_CMS_SERVER_URL_NSP", channel).toString())
                    .path(configuration.getConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", channel)
                            .toString())
                    .path(configuration.getConfigurationValue("Locale", "Language").toString())
                    .path(configuration.getConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", channel).toString())
                    .path(configuration.getConfigurationValue("PRODUCT_SELECTOR_CONTENT", "CONTENT_PATH").toString())
                    .build().toURL();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Map<String, Object> getConfigurationItems(String applicationProperties) {
        return configuration.getConfigurationItems(applicationProperties);
    }

    public Locale getLocale() {
        return Locale.ENGLISH;
    }

    private URL getUrl(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public URI getUriEndpointForPendingProductArrangementService() {
        URI uri = null;
        try {
            uri = getUrl(configuration
                    .getConfigurationValue(API_SERVICE_ENDPOINTS, "PRODUCT_PENDING_ARRANGEMENT_SERVICE").toString())
                            .toURI();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }
    
    
    public URL getUrlEndPointForContentManger(String tag) {
    	 try {
             return fromUri(configuration.getConfigurationValue("com.lloydstsb.gx.GalaxySystemParameter", tag).toString())
                    .build().toURL();
         } catch (Exception e) {
             throw new IllegalArgumentException(e);
         }
    }
    
    public String getFilePath(String section,String brand) {
    	String path = configuration.getConfigurationValue(section, brand).toString();
    	return path;
    }
  
}