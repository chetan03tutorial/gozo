/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.switches.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.exception.SwitchLoadFailedException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.sales.dao.switches.SwitchesManagementDAO;
import com.lloydstsb.ea.application.switching.GlobalApplicationSwitches;
import com.lloydstsb.ea.application.switching.data.ApplicationSwitch;

@Component
public class SwitchesManagementServiceImpl implements SwitchesManagementService {

    @Autowired
    private SwitchesManagementDAO   switchesDAO;

    @Autowired
    private ConfigurationDAO        configDAO;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @TraceLog
    public void loadSwitches() throws SwitchLoadFailedException {

        Map<String, Object> supportedChannelsMap = new HashMap<String, Object>();
        supportedChannelsMap = configDAO.getConfigurationItems(CommonConstants.SUPPORTED_CHANNEL_SECTION);

        for (Map.Entry<String, Object> channelsEntry : supportedChannelsMap.entrySet()) {
            if (channelsEntry.getValue().toString().equals(CommonConstants.SUPPORTED_CHANNEL_TRUE_VALUE)) {

                String channelId = configDAO.getConfigurationStringValue(CommonConstants.CHANNEL_ID,
                        channelsEntry.getKey());

                DAOResponse<HashMap<String, Boolean>> response = switchesDAO.getSwitches(channelId);
                if (response != null && response.getResult() != null) {
                    populateGlobalApplicationSwitches(channelsEntry.getKey(), response.getResult());
                } else if ((response != null && response.getError() != null) || response == null) {
                    throw new SwitchLoadFailedException();
                }
            }
        }
    }

    private void populateGlobalApplicationSwitches(String channelId, HashMap<String, Boolean> switchesMap) {
        ConcurrentHashMap<String, ApplicationSwitch> applicationSwitches = new ConcurrentHashMap<String, ApplicationSwitch>(
                switchesMap.size());

        for (Map.Entry<String, Boolean> switchEntry : switchesMap.entrySet()) {
            ApplicationSwitch appSwitch = new ApplicationSwitch();
            appSwitch.setSwitchName(switchEntry.getKey());
            appSwitch.setSwitchValue(switchEntry.getValue());

            applicationSwitches.put(switchEntry.getKey(), appSwitch);
        }
        GlobalApplicationSwitches.setApplicationSwitches(channelId, applicationSwitches);
    }

}
