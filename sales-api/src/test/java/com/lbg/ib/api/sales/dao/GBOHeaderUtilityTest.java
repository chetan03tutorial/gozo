package com.lbg.ib.api.sales.dao;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;

@RunWith(MockitoJUnitRunner.class)
public class GBOHeaderUtilityTest {

    @InjectMocks
    GBOHeaderUtility        gboHeaderUtility;

    @Mock
    SessionManagementDAO    session;

    @Mock
    ChannelBrandingDAO      channelBrandingDAO;

    @Mock
    ConfigurationDAO        configDAO;

    @Mock
    ReferenceDataServiceDAO referenceDataService;

    @Test
    public void testConstructor() {
        GBOHeaderUtility gboHeaderUtility = new GBOHeaderUtility(session, channelBrandingDAO, configDAO,
                referenceDataService);
        assertTrue(gboHeaderUtility != null);
    }

    @Test
    public void testPrepareServiceRequestHeader() {
        when(session.getUserContext())
                .thenReturn(new UserContext(null, null, null, null, null, null, null, null, null, null, null));
        List<SOAPHeader> soapHeader = gboHeaderUtility.prepareSoapHeader("dummy", "dummy");
        assertTrue(soapHeader != null);
    }
}
