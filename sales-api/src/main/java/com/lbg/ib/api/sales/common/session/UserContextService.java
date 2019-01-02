package com.lbg.ib.api.sales.common.session;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

import java.util.Map;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class UserContextService {
    private SessionManagementDAO session;
    private ChannelBrandingDAO   channelBrandingDAO;
    private ConfigurationDAO     configurationDAO;

    public UserContextService(SessionManagementDAO session, ChannelBrandingDAO channelBrandingDAO,
            ConfigurationDAO configurationDAO) {
        this.session = session;
        this.channelBrandingDAO = channelBrandingDAO;
        this.configurationDAO = configurationDAO;
    }

    public UserContext userContext(String anUserId) {
        Map<String, Object> systemParameter = configurationDAO
                .getConfigurationItems(CommonConstant.SYSTEM_PARAMETER_PROP);
        DAOResponse<ChannelBrandDTO> channelBrand = channelBrandingDAO.getChannelBrand();
        return new UserContext(anUserId, value(ApplicationAttribute.DEVICE_IP), session.getSessionId(),
                value(systemParameter, CommonConstant.PARTY_ID), value(systemParameter, CommonConstant.OCISID_ID),
                channelBrand.getResult().getChannelId(), value(systemParameter, CommonConstant.CHANSEC_MODE),
                value(ApplicationAttribute.DEVICE_TYPE), value(systemParameter, CommonConstant.LANGUAGE),
                value(systemParameter, CommonConstant.INBOXID_CLIENT), "I");

    }

    private String value(ApplicationAttribute param) {
        Object property = ApplicationRequestContext.get(param);
        return property != null ? property.toString() : null;
    }

    private String value(Map<String, Object> systemParameter, String param) {
        Object property = systemParameter.get(param);
        return property != null ? property.toString() : null;
    }
}
