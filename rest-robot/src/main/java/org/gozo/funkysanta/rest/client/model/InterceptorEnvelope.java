package org.gozo.funkysanta.rest.client.model;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import java.util.Map;

public class InterceptorEnvelope {

    private Handler handler;
    private int order;
    private Map<String, Object> config;
    private QName[] qualifierNames;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public QName[] getQualifierNames() {
        return qualifierNames;
    }

    public void setQualifierNames(QName[] qualifierNames) {
        this.qualifierNames = qualifierNames;
    }
}
