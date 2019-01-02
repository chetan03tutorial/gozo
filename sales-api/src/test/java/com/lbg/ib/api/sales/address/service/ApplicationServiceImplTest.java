package com.lbg.ib.api.sales.address.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.application.service.ApplicationServiceImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private LoggerDAO              logger;

    @Mock
    private ChannelBrandingDAO     channelBrandingDAO;
    
    @Mock
    private ConfigurationDAO configurationDAO;

    @Mock
    private SessionManagementDAO   sessionManagementDAO;

    @Test
    public void testGetCurrentBrandLloyds() {
        when(channelBrandingDAO.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("INTERNET", "LLOYDS", "DUMMY")));
        assertTrue(applicationService.getCurrentBrand().equals("LLOYDS"));
    }

    @Test
    public void testGetCurrentBrandBos() {
        when(channelBrandingDAO.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("INTERNET", "BOS", "DUMMY")));
        assertTrue(applicationService.getCurrentBrand().equals("BOS"));
    }

    @Test
    public void testGetCurrentBrandHalifax() {
        when(channelBrandingDAO.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("INTERNET", "HALIFAX", "DUMMY")));
        assertTrue(applicationService.getCurrentBrand().equals("HALIFAX"));
    }

    @Test
    public void testClearPipeline() {
        // sessionManagementDAO.clearSessionAttributeForPipelineChasing();
        applicationService.clearPipeLineData();
    }
    
    @Test
    public void testGetReverseBrandName()
    {
        when(channelBrandingDAO.getChannelBrand())
        .thenReturn(withResult(new ChannelBrandDTO("INTERNET", "LLOYDS", "DUMMY")));
        Map<String, Object> map = new HashMap<String, Object>(); 
        map.put("LLOYDS", new String("LLOYDS"));
        when(configurationDAO.getConfigurationItems(anyString())).thenReturn(map);
        assertNotNull(applicationService.getReverseBrandName().equals("HALIFAX"));
    }
}