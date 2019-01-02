package com.lbg.ib.api.sales.configuration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;

@RunWith(MockitoJUnitRunner.class)
public class ApiServicePropertiesTest {
    @Mock
    private ConfigurationDAO     configuration;
    private ApiServiceProperties apiServiceProperties;
    public static final String   CHANNEL = "channel";
    private ConfigurationDAO     config  = mock(ConfigurationDAO.class);

    @Test
    public void shouldReturnContentUrlAsExpectedAndSlashAtTheEndOfPAthShouldNotMatter() throws Exception {
        ApiServiceProperties properties = new ApiServiceProperties(config);
        when(config.getConfigurationValue("IIS_CMS_SERVER_URL_NSP", CHANNEL)).thenReturn("http://sit.group/wps")
                .thenReturn("http://sit.group/wps/");
        when(config.getConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", CHANNEL))
                .thenReturn("devpreview_teamsite_brand_personal_banking");
        when(config.getConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", CHANNEL))
                .thenReturn("BrandPersonalBanking/cwa/");
        when(config.getConfigurationValue("Locale", "Language")).thenReturn("en/");

        URL contentUrl = properties.getUrlEndPointForContent(CHANNEL);
        assertThat(contentUrl.toString(),
                is("http://sit.group/wps/devpreview_teamsite_brand_personal_banking/en/BrandPersonalBanking/cwa/"));
        contentUrl = properties.getUrlEndPointForContent(CHANNEL);
        assertThat(contentUrl.toString(),
                is("http://sit.group/wps/devpreview_teamsite_brand_personal_banking/en/BrandPersonalBanking/cwa/"));

    }

    @Before
    public void setup() throws IOException {
        apiServiceProperties = new ApiServiceProperties(configuration);
    }

    @Test
    public void testGetConfigurationItems() throws IOException {

        Map<String, Object> configValue = new HashMap<String, Object>();
        when(config.getConfigurationItems(any(String.class))).thenReturn(configValue);
        assertEquals(apiServiceProperties.getConfigurationItems("Test"), configValue);
    }

    @Test
    public void testGetUriEndpointForProducSelectorContent() {
        when(configuration.getConfigurationValue("IIS_CMS_SERVER_URL_NSP", "LTB"))
                .thenReturn("https://apigw.lbg.eu-gb.bluemix.net/");
        when(configuration.getConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", "LTB"))
                .thenReturn("sandbox-team-dev/sb/CG/content_secure/testpreview_teamsite_lloyds_personal_banking/");
        when(configuration.getConfigurationValue("Locale", "Language")).thenReturn(apiServiceProperties.getLocale());
        when(configuration.getConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", "LTB"))
                .thenReturn("LloydsPersonalBanking/cwa/");
        when(configuration.getConfigurationValue("PRODUCT_SELECTOR_CONTENT", "CONTENT_PATH"))
                .thenReturn("savings/product/product_mnemonic");
        URL url = apiServiceProperties.getUriEndpointForProducSelectorContent("LTB");
        assertTrue(url != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUriEndpointForProducSelectorContentWithException() {
        when(configuration.getConfigurationValue("IIS_CMS_SERVER_URL_NSP", "LTB"))
                .thenReturn("httpsxxx://apigw.lbg.eu-gb.bluemix.net/");
        when(configuration.getConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", "LTB"))
                .thenReturn("sandbox-team-dev/sb/CG/content_secure/testpreview_teamsite_lloyds_personal_banking/");
        when(configuration.getConfigurationValue("Locale", "Language")).thenReturn(apiServiceProperties.getLocale());
        when(configuration.getConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", "LTB"))
                .thenReturn("LloydsPersonalBanking/cwa/");
        when(configuration.getConfigurationValue("PRODUCT_SELECTOR_CONTENT", "CONTENT_PATH"))
                .thenReturn("savings/product/product_mnemonic");
        URL url = apiServiceProperties.getUriEndpointForProducSelectorContent("LTB");
        assertTrue(url != null);
    }

    @Test
    public void testGetUriEndpointForPendingProductArrangementService() {
        when(configuration.getConfigurationValue("API_SERVICE_ENDPOINTS", "PRODUCT_PENDING_ARRANGEMENT_SERVICE"))
                .thenReturn("http://www.google.com");

        URI uri = apiServiceProperties.getUriEndpointForPendingProductArrangementService();
        assertTrue(uri != null);
    }

    @Test
    public void testGetUriEndpointForPendingProductArrangementServiceInvalidUri() {
        when(configuration.getConfigurationValue("API_SERVICE_ENDPOINTS", "PRODUCT_PENDING_ARRANGEMENT_SERVICE"))
                .thenReturn("http://www.google.com");

        URI uri = apiServiceProperties.getUriEndpointForPendingProductArrangementService();
        assertTrue(uri != null);
    }

    @Test
    public void returnTheDeviceURLForABrand() {
        String deviceURL = "http://someBrands_DeviceURL";
        String sectionName = "DEVICE_URL";
        String brand = "someBrand";
        mockGetConfigurationValue(sectionName, brand, deviceURL);
        assertEquals(deviceURL, apiServiceProperties.getDeviceUrl(brand));
    }

    private void mockGetConfigurationValue(String sectionName, String tagName, String returnVal) {
        when(configuration.getConfigurationValue(sectionName, tagName)).thenReturn(returnVal);
    }

    private URL getUrl(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void returnThreatMatrixOrgId() {
        String deviceURL = "20";
        String sectionName = "ThreatMetrixOrgID";
        String brand = "someBrand";
        mockGetConfigurationValue(sectionName, brand, deviceURL);
        assertEquals(deviceURL, apiServiceProperties.getThreatMatrixDetailsFromConfig(sectionName, brand));
    }

    @Test
    public void returnUriEndpointForPostcodeCheckAddress() throws URISyntaxException {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "ADDRESS_SEARCH";
        String uri = "http://postcodecheckaddressURL";
        mockGetConfigurationValue(sectionName, tagName, uri);
        assertEquals(getUrl(uri).toURI(), apiServiceProperties.getUriEndpointForPostcodeCheckAddress());
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionWhenURIEndpointForPostcodeCheckAddressReturnedIsInvalid() {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "ADDRESS_SEARCH";
        String uri = "http://^%$#%";
        mockGetConfigurationValue(sectionName, tagName, uri);
        apiServiceProperties.getUriEndpointForPostcodeCheckAddress();
    }

    @Test
    public void returnUriEndpointForProductHolding() throws URISyntaxException {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "PRODUCT_HOLDING";
        String uri = "http://productHoldingURL";
        mockGetConfigurationValue(sectionName, tagName, uri);
        assertEquals(getUrl(uri).toURI(), apiServiceProperties.getUriEndpointForProductHolding());
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionWhenURIEndpointForProductHoldingReturnedIsInvalid() {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "PRODUCT_HOLDING";
        String uri = "http://^%$#%";
        mockGetConfigurationValue(sectionName, tagName, uri);
        apiServiceProperties.getUriEndpointForProductHolding();
    }

    @Test
    public void returnUriEndpointForRetrieveArrangmentQuestionnaire() throws URISyntaxException {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "RETRIEVE_ARRANGEMENT_QUESTIONNAIRE_SERVICE";
        String uri = "http://retrieveURL";
        mockGetConfigurationValue(sectionName, tagName, uri);
        assertEquals(getUrl(uri).toURI(),
                apiServiceProperties.getUriEndpointForRetrieveArrangmentQuestionnaireService());
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionWhenURIEndpointForRetrieveArrangmentQuestionnaireReturnedIsInvalid() {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "RETRIEVE_ARRANGEMENT_QUESTIONNAIRE_SERVICE";
        String uri = "http://^%$#%";
        mockGetConfigurationValue(sectionName, tagName, uri);
        apiServiceProperties.getUriEndpointForRetrieveArrangmentQuestionnaireService();
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionWhenURIEndpointForRetrieveArrangmentQuestionnaireReturnedIsInvalidMalformedUrl() {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "RETRIEVE_ARRANGEMENT_QUESTIONNAIRE_SERVICE";
        String uri = "httpasjdf://^%$#%";
        mockGetConfigurationValue(sectionName, tagName, uri);
        apiServiceProperties.getUriEndpointForRetrieveArrangmentQuestionnaireService();
    }

    @Test
    public void returnUriEndpointForTokenValidation() throws URISyntaxException {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "TOKEN_VALIDATION";
        String uri = "http://tokenURL";
        mockGetConfigurationValue(sectionName, tagName, uri);
        assertEquals(getUrl(uri).toURI(), apiServiceProperties.getUriEndpointForTokenValidation());
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionWhenURIEndpointForTokenValidationReturnedIsInvalid() {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "TOKEN_VALIDATION";
        String uri = "http://^%$#%";
        mockGetConfigurationValue(sectionName, tagName, uri);
        apiServiceProperties.getUriEndpointForTokenValidation();
    }

    @Test
    public void returnUriEndpointForRecordQuestionnaire() throws URISyntaxException {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "RECORD_ARRANGEMENT_QUESTIONNAIRE_SERVICE";
        String uri = "http://recordURL";
        mockGetConfigurationValue(sectionName, tagName, uri);
        assertEquals(getUrl(uri).toURI(), apiServiceProperties.getUriEndpointForRecordQuestionnaire());
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionWhenURIEndpointForRecordQuestionnaireReturnedIsInvalid() {
        String sectionName = "API_SERVICE_ENDPOINTS";
        String tagName = "RECORD_ARRANGEMENT_QUESTIONNAIRE_SERVICE";
        String uri = "http://^%$#%";
        mockGetConfigurationValue(sectionName, tagName, uri);
        apiServiceProperties.getUriEndpointForRecordQuestionnaire();
    }

    @Test
    public void returnContentUrlAsExpectedAndSlashAtTheEndOfPAthShouldNotMatter() throws Exception {
        String channel = "channel";
        mockGetConfigurationValue("IIS_CMS_SERVER_URL_NSP", channel, "http://sit.group/wps");
        mockGetConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", channel,
                "devpreview_teamsite_brand_personal_banking");
        mockGetConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", channel, "BrandPersonalBanking/cwa/");
        mockGetConfigurationValue("Locale", "Language", "en/");

        URL contentUrl = apiServiceProperties.getUrlEndPointForContent(channel);
        assertThat(contentUrl.toString(),
                is("http://sit.group/wps/devpreview_teamsite_brand_personal_banking/en/BrandPersonalBanking/cwa/"));
        contentUrl = apiServiceProperties.getUrlEndPointForContent(channel);
        assertThat(contentUrl.toString(),
                is("http://sit.group/wps/devpreview_teamsite_brand_personal_banking/en/BrandPersonalBanking/cwa/"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenReturnContentUrlIsNull() throws Exception {
        String channel = "channel";
        mockGetConfigurationValue("IIS_CMS_SERVER_URL_NSP", channel, null);

        mockGetConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", channel,
                "devpreview_teamsite_brand_personal_banking");
        mockGetConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", channel, "BrandPersonalBanking/cwa/");
        mockGetConfigurationValue("Locale", "Language", "en/");

        mockGetConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", channel,
                "devpreview_teamsite_brand_personal_banking");
        mockGetConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", channel, "BrandPersonalBanking/cwa/");
        mockGetConfigurationValue("Locale", "Language", "en/");

        URL contentUrl = apiServiceProperties.getUrlEndPointForContent(channel);
    }

    @Test
    public void returnsLocaleAsEnglish() {
        assertEquals(Locale.ENGLISH, apiServiceProperties.getLocale());
    }

    @Test
    public void returnHostForContentForChannel() {
        String channel = "somechannel";
        String someHost = "http://someHost";
        mockGetConfigurationValue("IIS_CMS_SERVER_URL_NSP", channel, someHost);
        assertEquals(someHost, apiServiceProperties.getHostForContent(channel).toString());
    }

    @Test
    public void returnSoapCommunicatorTimeoutInMillis() {
        String timeout = "10000";
        mockGetConfigurationValue("TIMEOUTS", "SOAP_COMMUNICATOR_TIMEOUT_IN_MILLIS", timeout);
        assertEquals(new Integer(timeout), apiServiceProperties.soapCommunicatorTimeoutInMillis());
    }

    @Test
    public void returnContentServiceBackendCallTimeoutInMillis() {
        String timeout = "10000";
        mockGetConfigurationValue("TIMEOUTS", "CONTENT_SERVICE_BACKEND_CALL_TIMEOUT_IN_MILLIS", timeout);
        assertEquals(new Integer(timeout), apiServiceProperties.contentServiceBackendCallTimeoutInMillis());
    }

    @Test
    public void returnClientId() {
        mockGetConfigurationValue("CONTENTSERVICE_CLIENT_ID", "channel", "ddd443a0-02ef-499b-9fb5-b5b9b736487d");
        assertEquals("ddd443a0-02ef-499b-9fb5-b5b9b736487d", apiServiceProperties.getclientID("channel"));
    }

    @Test
    public void returnSecretKey() {
        mockGetConfigurationValue("CONTENTSERVICE_CLIENT_SECRET", "channel", "b5b9b736487d");
        assertEquals("b5b9b736487d", apiServiceProperties.getSecretKey("channel"));
    }

    @Test
    public void returnUriEndpointForProducSelectorContent() throws URISyntaxException {
        String uri = "http://postcodecheckaddressURL";
        String expected = "http://postcodecheckaddressURL/test";
        mockGetConfigurationValue("IIS_CMS_SERVER_URL_NSP", "channel", uri);
        mockGetConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", "channel", uri);
        mockGetConfigurationValue("IIS_CMS_SERVER_URL_NSP", "channel", uri);
        mockGetConfigurationValue("Locale", "Language", "EN");
        mockGetConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", "channel", uri);
        mockGetConfigurationValue("PRODUCT_SELECTOR_CONTENT", "CONTENT_PATH", "test");
        URL uriActual = apiServiceProperties.getUriEndpointForProducSelectorContent("channel");
        assertEquals(getUrl(expected).toString(), uriActual.toString());

    }

    @Test(expected = Exception.class)

    public void returnUriEndpointForProducSelectorContent2() {
        String uri = "http://postcodecheckaddressURL";
        String expected = "http://postcodecheckaddressURL/test";
        mockGetConfigurationValue("IIS_CMS_SERVER_URL_NSP", "channel", uri);
        mockGetConfigurationValue("CONTENTSERVICE_URL_RESOURCE_BUNDLE_NSP", "channel", uri);
        mockGetConfigurationValue("IIS_CMS_SERVER_URL_NSP", "channel", uri);
        mockGetConfigurationValue("Locale", "Language", "EN");
        mockGetConfigurationValue("CONTENTSERVICE_URL_BRAND_NSP", "channel", uri);
        // mockGetConfigurationValue("PRODUCT_SELECTOR_CONTENT","CONTENT_PATH","test");
        apiServiceProperties.getUriEndpointForProducSelectorContent("channel");
        // assertEquals(getUrl(expected).toString(), uriActual.toString());

    }

    @Test
    public void returnUriEndpointForPendingProductArrangement() throws URISyntaxException {
        String uri = "http://postcodecheckaddressURL";
        String expected = "http://postcodecheckaddressURL";
        mockGetConfigurationValue("API_SERVICE_ENDPOINTS", "PRODUCT_PENDING_ARRANGEMENT_SERVICE", uri);
        URI uriActual = apiServiceProperties.getUriEndpointForPendingProductArrangementService();
        assertEquals(getUrl(expected).toString(), uriActual.toString());

    }

    @Test(expected = RuntimeException.class)
    public void returnUriEndpointForPendingProductArrangement2() {
        String uri = "http://postcodecheckaddressURL";
        String expected = "http://postcodecheckaddressURL";
        // mockGetConfigurationValue("API_SERVICE_ENDPOINTS","PRODUCT_PENDING_ARRANGEMENT_SERVICE",uri);
        URI uriActual = apiServiceProperties.getUriEndpointForPendingProductArrangementService();
        assertEquals(getUrl(expected).toString(), uriActual.toString());

    }

}
