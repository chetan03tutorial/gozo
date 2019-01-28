package org.gozo.funkysanta.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandContext {
    public static final String MESSAGE_MAPPER= "messageMapper";
    public static final String SERVICE_VALIDATOR = "serviceValidator";
    public static final String METHOD_ARGUMENTS = "args";
    public static final String SERVICE_CONFIG = "serviceConfiguration";
    public static final String SERVICE_REQUEST = "serviceRequest";
    public static final String SERVICE_RESPONSE = "serviceResponse";
    public static final String SOAP_MESSAGE_INTERCEPTORS = "messageHandler";
    private Map<Object, Object> hashMap = new HashMap<Object, Object>();


    public void set(Object aKey, Object aCtx) {
        hashMap.put(aKey, aCtx);
    }

    public void setReceiver(Object receiver){
        set("RECEIVER",receiver);
    }

     public <T> T getReceiver(Class<T> receiverClazz){
        Object receiver = get("RECEIVER");
        if(receiver != null){
            return (T)receiver;
        }
        return null;
     }

    public <T> T get(Object aKey, Class<T> classType) {
        return (T)hashMap.get(aKey);
    }

    public Object get(Object aKey) {
        return hashMap.get(aKey);
    }

    public void remove(Object aKey) {
        hashMap.remove(aKey);
    }

    public void clear() {
        hashMap.clear();
    }
}
