package com.lbg.ib.api.sales.docupload.dao.helper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import com.ibm.websphere.ssl.JSSEHelper;
import com.ibm.websphere.ssl.SSLException;
import com.ibm.ws.ssl.config.SSLConfigManager;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;


@Component
public class DynamicOutboundSSLSocketFactoryLocator extends SimpleClientHttpRequestFactory {
    @Autowired
    private LoggerDAO logger;

    public SSLSocketFactory getInstance(URL url) {
        if (JSSEHelper.getInstance() == null) {
            logger.logDebug(this.getClass(), "JSSEHelper instance not found");
            return null;
        }

        final Map connectionInfo = new HashMap();
        connectionInfo.put(JSSEHelper.CONNECTION_INFO_DIRECTION, JSSEHelper.DIRECTION_OUTBOUND);
        connectionInfo.put(JSSEHelper.CONNECTION_INFO_REMOTE_HOST, url.getHost());
        connectionInfo.put(JSSEHelper.CONNECTION_INFO_REMOTE_PORT, Integer.toString(url.getPort()));

        try {
            return JSSEHelper.getInstance().getSSLSocketFactory(connectionInfo,
                SSLConfigManager.getInstance()
                    .getPropertiesFromDynamicSelectionInfo(connectionInfo));
        } catch (SSLException e) {
            logger.logException(this.getClass(), e);
            return null;
        }
    }
}