package com.lbg.ib.api.sales.docupload.dao.helper;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.net.URL;


@Component
public class DynamicOutboundTLSClientParametersFactory {
    @Autowired
    DynamicOutboundSSLSocketFactoryLocator sslSocketFactoryLocator;

    public TLSClientParameters getInstance(URL url) {
        SSLSocketFactory socketFactory = sslSocketFactoryLocator.getInstance(url);
        TLSClientParameters tlsClientParameters = new TLSClientParameters();
        tlsClientParameters.setDisableCNCheck(true);
        tlsClientParameters.setSSLSocketFactory(socketFactory != null ? socketFactory :
            HttpsURLConnection.getDefaultSSLSocketFactory());
        return tlsClientParameters;
    }
}