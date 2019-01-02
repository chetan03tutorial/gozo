package com.lbg.ib.api.sales.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dao.switches.SwitchesManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Arrangement;

/**
 * Created by dbhatt on 9/19/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class CommonUtilsTest {
    @InjectMocks
    private CommonUtils   commonUtils;

    @Mock
    ChannelBrandingDAO    channelService;

    @Mock
    SwitchesManagementDAO switchesDAO;

    @Mock
    SessionManagementDAO  session;

    @Test
    public void testWhenBranchContextPresent() {
        Mockito.when(session.getBranchContext()).thenReturn(new BranchContext());
        boolean result = commonUtils.getSiraSwitchStatus(channelService, switchesDAO, session);
        assertFalse(result);
    }

    @Test
    public void testWhenBranchContextAbsent() {
        Mockito.when(session.getBranchContext()).thenReturn(null);
        Mockito.when(channelService.getChannelBrand())
                .thenReturn(DAOResponse.withResult(new ChannelBrandDTO("DUMMY_CHANNEL", "DUMMY_BRAND", "DUMMY_ID")));
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("SW_EnSIRAFrdChk", true);
        Mockito.when(switchesDAO.getSwitches("DUMMY_ID")).thenReturn(DAOResponse.withResult(map));
        boolean result = commonUtils.getSiraSwitchStatus(channelService, switchesDAO, session);
        assertTrue(result);
    }

    @Test
    public void testWhenBranchContextAndSiraSwitchAreAbsent() {
        Mockito.when(session.getBranchContext()).thenReturn(null);
        Mockito.when(channelService.getChannelBrand())
                .thenReturn(DAOResponse.withResult(new ChannelBrandDTO("DUMMY_CHANNEL", "DUMMY_BRAND", "DUMMY_ID")));
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        Mockito.when(switchesDAO.getSwitches("DUMMY_ID")).thenReturn(DAOResponse.withResult(map));
        boolean result = commonUtils.getSiraSwitchStatus(channelService, switchesDAO, session);
        assertFalse(result);
    }

    @Test
    public void testIsBranchContextForTrue() {
        Mockito.when(session.getBranchContext()).thenReturn(new BranchContext());
        boolean result = commonUtils.isBranchContext();
        assertTrue(result);
    }

    @Test
    public void testIsBranchContextForFalse() {
        Mockito.when(session.getBranchContext()).thenReturn(null);
        boolean result = commonUtils.isBranchContext();
        assertFalse(result);
    }

    @Test
    public void testIsAuthForTrue() {
        Mockito.when(session.getUserInfo()).thenReturn(new Arrangement());
        boolean result = commonUtils.isAuth(session);
        assertTrue(result);
    }

    @Test
    public void testIsAuthForFalse() {
        Mockito.when(session.getUserInfo()).thenReturn(null);
        boolean result = commonUtils.isAuth(session);
        assertFalse(result);
    }

    @Test
    public void testDefaultZeroForNull() {
        Integer result = commonUtils.defaultZero(null);
        assertThat(result.intValue(), is(0));
    }

    @Test
    public void testDefaultZeroForNonZero() {
        Integer result = commonUtils.defaultZero(new Integer(1));
        assertThat(result.intValue(), is(1));
    }

}
