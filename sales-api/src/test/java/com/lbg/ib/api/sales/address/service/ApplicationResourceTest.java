package com.lbg.ib.api.sales.address.service;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.application.ApplicationResource;
import com.lbg.ib.api.sales.application.service.ApplicationService;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationResourceTest {

    @InjectMocks
    ApplicationResource          applicationResource = null;

    @Mock
    private LoggerDAO            logger;

    @Mock
    private ApiServiceProperties apiServiceProperties;

    @Mock
    private ApplicationService   applicationService;

    @Test
    public void testAppConfigForKeepSessionLloyds() {
        Map<String, Object> versionInfoMap = new HashMap<String, Object>();
        versionInfoMap.put("accreditionFlag", false);
        versionInfoMap.put("ApplicationName", "PCA-SAVINGS");
        versionInfoMap.put("ApplicationName", "version");
        versionInfoMap.put("TimeStamp", "timestamp");

        Map<String, Object> brandMap = new HashMap<String, Object>();
        brandMap.put("HALIFAX", "false");
        brandMap.put("BOS", "false");
        brandMap.put("LLOYDS", "true");

        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(versionInfoMap);
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(brandMap);

        when(applicationService.getCurrentBrand()).thenReturn("LLOYDS");

        Response response = applicationResource.appConfig("keep");
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);

        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();

        assertTrue(response.getStatus() == 200);
        assertTrue(responseMap.containsKey(Constants.ACTIVE_X_FLAG));

    }

    @Test
    public void testAppConfigForNullResponse() {
        Map<String, Object> versionInfoMap = new HashMap<String, Object>();
        versionInfoMap.put("accreditionFlag", false);
        versionInfoMap.put("ApplicationName", "PCA-SAVINGS");
        versionInfoMap.put("ApplicationName", "version");
        versionInfoMap.put("TimeStamp", "timestamp");

        Map<String, Object> brandMap = new HashMap<String, Object>();
        brandMap.put("HALIFAX", "false");
        brandMap.put("BOS", "false");
        brandMap.put("LLOYDS", "true");
        Map<String, Object> map = new HashMap<String, Object>();
        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(map);
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(brandMap);

        when(applicationService.getCurrentBrand()).thenReturn("LLOYDS");

        Response response = applicationResource.getVersionInfo();
        Assert.assertNull(response);

    }

    @Test
    public void testAppConfigForNonKeepSessionLloyds() {
        Map<String, Object> versionInfoMap = new HashMap<String, Object>();
        versionInfoMap.put("accreditionFlag", false);
        versionInfoMap.put("ApplicationName", "PCA-SAVINGS");
        versionInfoMap.put("ApplicationName", "version");
        versionInfoMap.put("TimeStamp", "timestamp");

        Map<String, Object> brandMap = new HashMap<String, Object>();
        brandMap.put("HALIFAX", "false");
        brandMap.put("BOS", "false");
        brandMap.put("LLOYDS", "true");

        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(versionInfoMap);
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(brandMap);

        when(applicationService.getCurrentBrand()).thenReturn("LLOYDS");

        Response response = applicationResource.appConfig("abcd");
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);

        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();

        assertTrue(response.getStatus() == 200);
        assertTrue(responseMap.containsKey(Constants.ACTIVE_X_FLAG));

    }

    @Test
    public void testAppConfigLloyds() {
        Map<String, Object> versionInfoMap = new HashMap<String, Object>();
        versionInfoMap.put("accreditionFlag", false);
        versionInfoMap.put("ApplicationName", "PCA-SAVINGS");
        versionInfoMap.put("ApplicationName", "version");
        versionInfoMap.put("TimeStamp", "timestamp");

        Map<String, Object> brandMap = new HashMap<String, Object>();
        brandMap.put("HALIFAX", "false");
        brandMap.put("BOS", "false");
        brandMap.put("LLOYDS", "true");

        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(versionInfoMap);
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(brandMap);

        when(applicationService.getCurrentBrand()).thenReturn("LLOYDS");

        Response response = applicationResource.appConfig();
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);

        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();

        assertTrue(response.getStatus() == 200);
        assertTrue(responseMap.containsKey(Constants.ACTIVE_X_FLAG));
    }

    @Test
    public void testAppConfigBos() {
        Map<String, Object> versionInfoMap = new HashMap<String, Object>();
        versionInfoMap.put("accreditionFlag", false);
        versionInfoMap.put("ApplicationName", "PCA-SAVINGS");
        versionInfoMap.put("ApplicationName", "version");
        versionInfoMap.put("TimeStamp", "timestamp");

        Map<String, Object> brandMap = new HashMap<String, Object>();
        brandMap.put("HALIFAX", "false");
        brandMap.put("BOS", "true");
        brandMap.put("LLOYDS", "false");

        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(versionInfoMap);
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(brandMap);

        when(applicationService.getCurrentBrand()).thenReturn("BOS");

        Response response = applicationResource.appConfig();
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);

        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();

        assertTrue(response.getStatus() == 200);
        assertTrue(responseMap.containsKey(Constants.ACTIVE_X_FLAG));
    }

    @Test
    public void testAppConfigHalifax() {
        Map<String, Object> versionInfoMap = new HashMap<String, Object>();
        versionInfoMap.put("accreditionFlag", false);
        versionInfoMap.put("ApplicationName", "PCA-SAVINGS");
        versionInfoMap.put("ApplicationName", "version");
        versionInfoMap.put("TimeStamp", "timestamp");

        Map<String, Object> brandMap = new HashMap<String, Object>();
        brandMap.put("HALIFAX", "true");
        brandMap.put("BOS", "false");
        brandMap.put("LLOYDS", "false");

        when(apiServiceProperties.getConfigurationItems(Constants.APPLICATION_PROPERTIES)).thenReturn(versionInfoMap);
        when(apiServiceProperties.getConfigurationItems(Constants.ACTIVEX_PROPERTIES)).thenReturn(brandMap);

        when(applicationService.getCurrentBrand()).thenReturn("HALIFAX");

        Response response = applicationResource.appConfig();
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);

        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();

        assertTrue(response.getStatus() == 200);
        assertTrue(responseMap.containsKey(Constants.ACTIVE_X_FLAG));
    }
}