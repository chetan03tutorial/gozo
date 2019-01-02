package com.lbg.ib.api.sales.dao.device;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.device.DeviceDTO;
import org.junit.Test;

import static com.lbg.ib.api.sales.dao.device.DeviceDAOImpl.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class DeviceDAOImplTest {
    private static String        ThreatMetrixOrgID = "ThreatMetrixOrgID";
    private ApiServiceProperties properties        = mock(ApiServiceProperties.class);
    private SessionManagementDAO session           = mock(SessionManagementDAO.class);
    private LoggerDAO            logger            = mock(LoggerDAO.class);
    private ChannelBrandDTO      dto               = new ChannelBrandDTO("channel", "dto", "cid");

    @Test
    public void shouldPutDefinedDeviceUrlDeviceIdAndLast4CharsOfJSessionIdToTheDTO() throws Exception {
        when(properties.getDeviceUrl("dto")).thenReturn("server");
        when(properties.getThreatMatrixDetailsFromConfig(ThreatMetrixOrgID, "dto")).thenReturn("org");
        when(session.getSessionId()).thenReturn("morethan4chars");

        DAOResponse<DeviceDTO> device = new DeviceDAOImpl(properties, session, logger).getDevice(dto);

        assertThat(device.getResult(), is(new DeviceDTO("server", "org", "morethan")));
    }

    @Test
    public void shouldReturnErrorCodeWhenWeCannotFindMatchingDeviceUrl() throws Exception {
        when(properties.getDeviceUrl("dto")).thenReturn(null);
        when(properties.getThreatMatrixDetailsFromConfig(ThreatMetrixOrgID, "dto")).thenReturn("org");
        when(session.getSessionId()).thenReturn("morethan4chars");

        DAOResponse<DeviceDTO> device = new DeviceDAOImpl(properties, session, logger).getDevice(dto);

        assertThat(device.getError(), is(new DAOError(NO_MARTCHING_URL, "No matching url for key 'dto'")));
        verify(logger).logError(NO_MARTCHING_URL,
                "No matching url for key 'dto', Called Method: getService, Method Call Params: ChannelBrandDTO{channel='channel', brand='dto', channelId='cid'}",
                DeviceDAOImpl.class);
    }

    @Test
    public void shouldReturnErrorCodeWhenWeCannotFindMatchingTmOrgId() throws Exception {
        when(properties.getDeviceUrl("dto")).thenReturn("server");
        when(properties.getThreatMatrixDetailsFromConfig(ThreatMetrixOrgID, "dto")).thenReturn(null);
        when(session.getSessionId()).thenReturn("morethan4chars");

        DAOResponse<DeviceDTO> device = new DeviceDAOImpl(properties, session, logger).getDevice(dto);

        assertThat(device.getError(),
                is(new DAOError(NO_MARTCHING_TM_ORG_ID, "No matching threat metrix org id for key 'dto'")));
        verify(logger).logError(NO_MARTCHING_TM_ORG_ID,
                "No matching threat metrix org id for key 'dto', Called Method: getService, Method Call Params: ChannelBrandDTO{channel='channel', brand='dto', channelId='cid'}",
                DeviceDAOImpl.class);
    }

    @Test
    public void shouldReturnErrorCodeWhenThereIsNoSessionId() throws Exception {
        when(properties.getDeviceUrl("dto")).thenReturn("server");
        when(properties.getThreatMatrixDetailsFromConfig(ThreatMetrixOrgID, "dto")).thenReturn("org");

        DAOResponse<DeviceDTO> device = new DeviceDAOImpl(properties, session, logger).getDevice(dto);

        assertThat(device.getError(), is(new DAOError(NO_SESSION_ID, "No session id found")));
        verify(logger).logError(NO_SESSION_ID,
                "No session id found, Called Method: getService, Method Call Params: ChannelBrandDTO{channel='channel', brand='dto', channelId='cid'}",
                DeviceDAOImpl.class);
    }

    @Test
    public void shouldReturnErrorCodeWhenSessionIdLessThan4chars() throws Exception {
        when(properties.getDeviceUrl("dto")).thenReturn("server");
        when(properties.getThreatMatrixDetailsFromConfig(ThreatMetrixOrgID, "dto")).thenReturn("org");
        when(session.getSessionId()).thenReturn("ars");

        DAOResponse<DeviceDTO> device = new DeviceDAOImpl(properties, session, logger).getDevice(dto);

        assertThat(device.getError(),
                is(new DAOError(INVALID_SESSION_ID, "'ars' is not a valid session id (less than 4 chars)")));
        verify(logger).logError(INVALID_SESSION_ID,
                "'ars' is not a valid session id (less than 4 chars), Called Method: getService, Method Call Params: ChannelBrandDTO{channel='channel', brand='dto', channelId='cid'}",
                DeviceDAOImpl.class);
    }
}