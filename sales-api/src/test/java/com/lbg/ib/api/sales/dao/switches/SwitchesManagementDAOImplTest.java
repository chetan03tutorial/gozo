/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.switches;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StSwitchValue;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB500AServiceMonitor;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB500BServiceMonitor;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.SystemPortType;

@RunWith(MockitoJUnitRunner.class)
public class SwitchesManagementDAOImplTest {

    @InjectMocks
    private SwitchesManagementDAOImpl    switchesManagementDAOImpl;

    @Mock
    private SystemPortType               service;

    @Mock
    private StB500BServiceMonitor        response;

    @Mock
    private StHeader                     header;

    @Mock
    private ConfigurationDAO             configDAO;

    @Mock
    private DAOExceptionHandler          daoExceptionHandler;

    @Mock
    private FoundationServerUtil         foundationServerUtil;

    private static final RemoteException REMOTE_EXCEPTION = new RemoteException();
    private static final String          CHANNEL_ID       = "IBL";

    @Test
    public void shouldReturnTeaLeafSwitchValueAsTrueWhenSwitchIsON() throws Exception {
        StSwitchValue switchValue1 = new StSwitchValue();
        switchValue1.setSwitchname(CommonConstants.VISUAL_MI_SWITCH);
        switchValue1.setBSwitchValue(true);

        StSwitchValue[] switchValues = new StSwitchValue[] { switchValue1 };

        Map<String, Object> switchesMap = new HashMap<String, Object>();
        switchesMap.put("SW_EnTLfFldTgs", "SW_EnTLfFldTgs");

        when(configDAO.getConfigurationItems(CommonConstants.RETAIL_SWITCH)).thenReturn(switchesMap);
        when(foundationServerUtil.createDefaultHeader(CHANNEL_ID)).thenReturn(header);
        when(service.b500ServiceMonitor(any(StB500AServiceMonitor.class))).thenReturn(response);
        when(response.getSterror()).thenReturn(null);
        when(response.getAstswitchvalue()).thenReturn(switchValues);

        switchesManagementDAOImpl.init();
        DAOResponse<HashMap<String, Boolean>> switchesMapResponse = switchesManagementDAOImpl.getSwitches(CHANNEL_ID);

        assertNotNull(switchesMapResponse.getResult());
        assertTrue(switchesMapResponse.getResult().get(CommonConstants.VISUAL_MI_SWITCH));
    }

    @Test
    public void shouldNotReturnTeaLeafSwitchValueAsTrueWhenSwitchIsONInCaseOffailure() throws Exception {
        StSwitchValue switchValue1 = new StSwitchValue();
        switchValue1.setSwitchname(CommonConstants.VISUAL_MI_SWITCH);
        switchValue1.setBSwitchValue(true);
        Map<String, Object> switchesMap = new HashMap<String, Object>();
        switchesMap.put("SW_EnTLfFldTgs", "SW_EnTLfFldTgs");

        when(configDAO.getConfigurationItems(CommonConstants.RETAIL_SWITCH)).thenReturn(switchesMap);
        when(foundationServerUtil.createDefaultHeader(CHANNEL_ID)).thenReturn(header);
        when(service.b500ServiceMonitor(any(StB500AServiceMonitor.class))).thenReturn(response);
        StError error = new StError();
        error.setErrormsg("errormsg");
        error.setErrorno(12345);
        when(response.getSterror()).thenReturn(error);
        when(response.getAstswitchvalue()).thenReturn(null);

        switchesManagementDAOImpl.init();
        DAOResponse<HashMap<String, Boolean>> switchesMapResponse = switchesManagementDAOImpl.getSwitches(CHANNEL_ID);

        assertEquals(switchesMapResponse.getError().getErrorCode(), "12345");
        assertEquals(switchesMapResponse.getError().getErrorMessage(), "errormsg");
    }

    @Test
    public void shouldReturnTeaLeafSwitchValueAsFalseWhenSwitchIsOFF() throws Exception {
        StSwitchValue switchValue1 = new StSwitchValue();
        switchValue1.setSwitchname(CommonConstants.VISUAL_MI_SWITCH);
        switchValue1.setBSwitchValue(false);

        StSwitchValue[] switchValues = new StSwitchValue[] { switchValue1 };

        Map<String, Object> switchesMap = new HashMap<String, Object>();
        switchesMap.put("SW_EnTLfFldTgs", "SW_EnTLfFldTgs");

        when(configDAO.getConfigurationItems(CommonConstants.RETAIL_SWITCH)).thenReturn(switchesMap);
        when(foundationServerUtil.createDefaultHeader(CHANNEL_ID)).thenReturn(header);
        when(service.b500ServiceMonitor(any(StB500AServiceMonitor.class))).thenReturn(response);
        when(response.getSterror()).thenReturn(null);
        when(response.getAstswitchvalue()).thenReturn(switchValues);

        switchesManagementDAOImpl.init();
        DAOResponse<HashMap<String, Boolean>> switchesMapResponse = switchesManagementDAOImpl.getSwitches(CHANNEL_ID);

        assertNotNull(switchesMapResponse.getResult());
        assertFalse(switchesMapResponse.getResult().get(CommonConstants.VISUAL_MI_SWITCH));
    }

    @Test
    public void shouldReturnDaoErrorFromExceptionHandlerWhenServerReturnsWithAnError() throws Exception {
        Map<String, Object> switchesMap = new HashMap<String, Object>();
        switchesMap.put("SW_EnTLfFldTgs", "SW_EnTLfFldTgs");
        when(configDAO.getConfigurationItems(CommonConstants.RETAIL_SWITCH)).thenReturn(switchesMap);
        when(service.b500ServiceMonitor(any(StB500AServiceMonitor.class))).thenThrow(REMOTE_EXCEPTION);

        DAOError expectedDaoError = new DAOError("code", "message");

        when(daoExceptionHandler.handleException(REMOTE_EXCEPTION, SwitchesManagementDAOImpl.CLASS_NAME, "getSwitches",
                null)).thenReturn(expectedDaoError);

        DAOError error = switchesManagementDAOImpl.getSwitches(CHANNEL_ID).getError();

        assertThat(error, is(expectedDaoError));
    }

}
