package org.gozo.funkysanta.rest.client.model;

import java.util.List;

public class MetaData {

    private String serviceName;
    private String locator;
    private String address;
    private String operation;
    private List<HandlerEnvelope> interceptors;

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<HandlerEnvelope> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<HandlerEnvelope> interceptors) {
        this.interceptors = interceptors;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
