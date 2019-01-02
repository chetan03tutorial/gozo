/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.dao;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StDeviceApp;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Map;

@Component
public class FoundationServerUtil {

    @Autowired
    private ConfigurationDAO        configDAO;

    /**
     * String for Comma.
     */
    public static final String      COMMA    = ",";

    private static final String     PARTY_ID = "UNAUTHSALE";

    private static final BigInteger OCIS_ID  = new BigInteger("0");

    public StParty createStParty(UserContext clientContext) {
        StParty stParty = new StParty();
        stParty.setHost(CommonConstants.HOST);
        stParty.setPartyid(PARTY_ID);
        stParty.setOcisid(OCIS_ID);

        return stParty;
    }

    public StHeader createStHeader(UserContext clientContext) {
        StParty stParty = createStParty(clientContext);

        StHeader stHeader = new StHeader();
        stHeader.setUseridAuthor(clientContext.getUserId());
        stHeader.setStpartyObo(stParty);
        stHeader.setChanid(clientContext.getChannelId());
        stHeader.setSessionid(clientContext.getSessionId());
        stHeader.setUserAgent(clientContext.getUserAgent());
        stHeader.setInboxidClient(clientContext.getInboxIdClient());
        stHeader.setAcceptLanguage(clientContext.getLanguage());
        stHeader.setStdeviceapp(new StDeviceApp[] { createStDeviceApp() });
        stHeader.setChansecmode(clientContext.getChansecmode());
        stHeader.setIpAddressCaller(ApplicationRequestContext.get(ApplicationAttribute.NODE_IP).toString().concat(COMMA)
                .concat(clientContext.getIpAddress()));
        stHeader.setChanctxt(new BigInteger("0"));
        stHeader.setEncVerNo(new BigInteger("0"));
        return stHeader;
    }

    private StDeviceApp createStDeviceApp() {
        final StDeviceApp app = new StDeviceApp();
        app.setDeviceappname(toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_NAME));
        app.setDeviceappversion(toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_VERSION));
        app.setDeviceplatform(toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_PLATFORM));
        app.setDevicesubchannel(toSoapValueFromArc(ApplicationAttribute.ST_DEVICE_APP_SUB_CHANNEL));
        return app;
    }

    public String toSoapValue(final String value) {
        if (value == null) {
            return null;
        }
        final String trimmed = value.trim();
        if (trimmed.length() == 0) {
            return null;
        }
        if (ApplicationRequestContext.DEFAULT.equals(trimmed)) {
            return null;
        }
        // Yes, that's right, the result value is not trimmed, as it is expected
        // by some backend
        // API.
        return value;
    }

    private String toSoapValueFromArc(final ApplicationAttribute attribute) {
        return toSoapValue((String) ApplicationRequestContext.get(attribute));
    }

    public StHeader createDefaultHeader(String channelId) {
        return createStHeader(getDefaultUserContext(channelId));
    }

    public UserContext getDefaultUserContext(String channelId) {
        Map<String, Object> systemParameter = configDAO.getConfigurationItems(CommonConstant.SYSTEM_PARAMETER_PROP);

        return new UserContext(CommonConstants.UNAUTH_USER_ID, getValue(ApplicationAttribute.DEVICE_IP),
                CommonConstants.DEFAULT_SESSION_ID, getValue(systemParameter, CommonConstant.PARTY_ID),
                getValue(systemParameter, CommonConstant.OCISID_ID), channelId,
                getValue(systemParameter, CommonConstant.CHANSEC_MODE), getValue(ApplicationAttribute.DEVICE_TYPE),
                getValue(systemParameter, CommonConstant.LANGUAGE),
                getValue(systemParameter, CommonConstant.INBOXID_CLIENT),
                getValue(systemParameter, CommonConstant.SERVICE_SRC_HOST));
    }

    private String getValue(Map<String, Object> systemParameter, String param) {
        Object property = systemParameter.get(param);
        return property != null ? property.toString() : null;
    }

    private String getValue(ApplicationAttribute param) {
        Object property = ApplicationRequestContext.get(param);
        return property != null ? property.toString() : null;
    }
}
