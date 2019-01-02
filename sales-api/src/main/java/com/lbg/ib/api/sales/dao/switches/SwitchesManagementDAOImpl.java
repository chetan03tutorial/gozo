/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.switches;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StSwitchValue;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB500AServiceMonitor;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.StB500BServiceMonitor;
import com.lbg.ib.api.sales.soapapis.wsbridge.system.SystemPortType;

/**
 * The Class is Used for Calling B500 and fetch Application Switch statuses.
 *
 */
@Component
public class SwitchesManagementDAOImpl implements SwitchesManagementDAO {

    public static final Class<SwitchesManagementDAOImpl> CLASS_NAME  = SwitchesManagementDAOImpl.class;

    private static final String                          METHOD_NAME = "getSwitches";

    @Autowired
    private SystemPortType                               service;

    @Autowired
    private LoggerDAO                                    logger;

    @Autowired
    private SessionManagementDAO                         session;

    @Autowired
    private ConfigurationDAO                             configDAO;

    @Autowired
    private DAOExceptionHandler                          exceptionHandler;

    @Autowired
    private FoundationServerUtil                         foundationServerUtil;

    private Set<Object>                                  switchesToLoad;

    /**
     * This method will fetch the switch names to load from the
     * configApplicationSwitches.xml and keep it in the HashSet.
     *
     * No switches will be loaded if its entry not present in the config file.
     *
     * Annotation @PostConstruct will ensure this method will get called once
     * after the beans creation, so that the bean instance for ConfigurationDAO
     * class can be injected into it.
     */
    @PostConstruct
    public void init() {
        switchesToLoad = new HashSet<Object>();

        Map<String, Object> retailSwitches = configDAO.getConfigurationItems(CommonConstants.RETAIL_SWITCH);
        switchesToLoad.addAll(retailSwitches.values());

        Map<String, Object> commonSwitches = configDAO.getConfigurationItems(CommonConstants.COMMON_SWITCH);
        switchesToLoad.addAll(commonSwitches.values());
    }

    @TraceLog
    public DAOResponse<HashMap<String, Boolean>> getSwitches(String channelId) {

        StB500AServiceMonitor request = populateRequest(channelId);

        try {
            StB500BServiceMonitor response = service.b500ServiceMonitor(request);
            if (response != null && response.getAstswitchvalue() != null) {
                StSwitchValue[] switchValues = response.getAstswitchvalue();

                HashMap<String, Boolean> switchesMap = new HashMap<String, Boolean>();

                // Populating a map for the status of the switches present in
                // the "switchesToLoad" HashSet
                for (StSwitchValue switchValue : switchValues) {
                    if (switchesToLoad.contains(switchValue.getSwitchname().trim())) {
                        switchesMap.put(switchValue.getSwitchname().trim(), switchValue.isBSwitchValue());
                    }
                }
                return (withResult(switchesMap));
            } else if (response != null && response.getSterror() != null
                    && response.getSterror().getErrormsg() != null) {
                StError stError = response.getSterror();
                DAOError daoerror = new DAOError(String.valueOf(stError.getErrorno()), stError.getErrormsg());
                return withError(daoerror);
            }

            return null;

        } catch (Exception e) {
            DAOError daoError = exceptionHandler.handleException(e, CLASS_NAME, METHOD_NAME, null);
            return withError(daoError);
        }
    }

    private StB500AServiceMonitor populateRequest(String channelId) {
        StB500AServiceMonitor request = new StB500AServiceMonitor();
        request.setStheader(foundationServerUtil.createDefaultHeader(channelId));
        return request;
    }

}