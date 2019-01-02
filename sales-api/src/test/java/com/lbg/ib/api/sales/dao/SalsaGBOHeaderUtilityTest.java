package com.lbg.ib.api.sales.dao;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
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
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lloydstsb.ib.salsa.crs.messages.SOAPHeader;

@RunWith(MockitoJUnitRunner.class)
public class SalsaGBOHeaderUtilityTest {

    @Mock
    private SessionManagementDAO    session;

    @Mock
    private ConfigurationDAO        configDAO;

    @Mock
    private ReferenceDataServiceDAO referenceDataService;

    @Mock
    private ChannelBrandingDAO      channelBrandingDAO;

    @InjectMocks
    SalsaGBOHeaderUtility           salsaGBOHeaderUtility;

    @Test
    public void testPrepareSoapHeader() {
        when(session.getUserContext())
                .thenReturn(new UserContext(null, null, null, null, null, null, null, null, null, null, null));
        when(channelBrandingDAO.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("INTERNET", "LLOYDS", "DUMMY")));

        List<SOAPHeader> soapHeader = salsaGBOHeaderUtility.prepareSoapHeader("dummy", "dummy");
        assertTrue(soapHeader != null);
    }
}