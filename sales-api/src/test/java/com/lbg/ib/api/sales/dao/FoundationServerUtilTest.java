package com.lbg.ib.api.sales.dao;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;

@RunWith(MockitoJUnitRunner.class)
public class FoundationServerUtilTest {

    @Mock
    private ConfigurationDAO configDAO;

    @InjectMocks
    FoundationServerUtil     foundationServerUtil;

    @Test
    public void testCreateStParty() {
        StParty stParty = foundationServerUtil.createStParty(null);
        assertTrue(stParty != null);
    }

    @Test
    public void testCreateStHeader() {
        Map<String, Object> systemParameter = new HashMap<String, Object>();
        when(configDAO.getConfigurationItems(CommonConstant.SYSTEM_PARAMETER_PROP)).thenReturn(systemParameter);
        foundationServerUtil.createDefaultHeader("dummy");
        UserContext userContext = foundationServerUtil.getDefaultUserContext("Dummy");
        StHeader stHeader = foundationServerUtil.createStHeader(userContext);
        assertTrue(stHeader != null);
    }

    @Test
    public void testCreateStHeaderWithValidMapValues() {
        Map<String, Object> systemParameter = new HashMap<String, Object>();
        systemParameter.put("INIT_OCISID_ID", "dummy");
        when(configDAO.getConfigurationItems(CommonConstant.SYSTEM_PARAMETER_PROP)).thenReturn(systemParameter);
        UserContext userContext = foundationServerUtil.getDefaultUserContext("Dummy");
        StHeader stHeader = foundationServerUtil.createStHeader(userContext);
        assertTrue(stHeader != null);
    }

    @Test
    public void testToSoapValue() {
        assertTrue(foundationServerUtil.toSoapValue(null) == null);
        assertTrue(foundationServerUtil.toSoapValue("") == null);
        assertTrue(foundationServerUtil.toSoapValue("...") == null);
        assertTrue(foundationServerUtil.toSoapValue("dummy") != null);

    }
}
