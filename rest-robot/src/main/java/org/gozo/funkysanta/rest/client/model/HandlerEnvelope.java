package org.gozo.funkysanta.rest.client.model;

import java.util.HashMap;
import java.util.Map;

public class HandlerEnvelope {

    private String handler;
    private int order;
    private Map<String, Object> config;


    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
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

    public void setParameter(String key, Object value) {
        if (config == null) {
            config = new HashMap<String, Object>();
        }
        config.put(key, value);
    }

}
