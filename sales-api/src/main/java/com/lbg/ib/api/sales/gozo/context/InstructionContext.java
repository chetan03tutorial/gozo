package com.lbg.ib.api.sales.gozo.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstructionContext {

	public static final String MESSAGE_MAPPER= "messageMapper";
	public static final String SERVICE_VALIDATOR = "serviceValidator";
	public static final String METHOD_ARGUMENTS = "args";
	public static final String SERVICE_CONFIG = "serviceConfiguration";
	public static final String SERVICE_REQUEST = "serviceRequest";
	public static final String SERVICE_RESPONSE = "serviceResponse";
	public static final String SOAP_MESSAGE_INTERCEPTORS = "messageHandler";

	private static ThreadLocal<ConcurrentHashMap<Object, Object>> tLocal = new ThreadLocal<ConcurrentHashMap<Object, Object>>() {
		@Override
		protected ConcurrentHashMap<Object, Object> initialValue() {
			return new ConcurrentHashMap<Object, Object>();
		}
	};

	public void set(Object aKey, Object aCtx) {
		
		if(aCtx == null) {
			return ;
		}
		ConcurrentHashMap<Object, Object> hashMap = (ConcurrentHashMap<Object, Object>) tLocal.get();
		hashMap.put(aKey, aCtx);
	}

	public Object get() {
		return (tLocal.get());
	}

	public <T> T get(Object aKey, Class<T> classType) {
		Map<Object, Object> map = (ConcurrentHashMap<Object, Object>) tLocal.get();
		return (T)map.get(aKey);
	}
	
	public Object get(Object aKey) {
		Map<Object, Object> map = (ConcurrentHashMap<Object, Object>) tLocal.get();
		return map.get(aKey);
	}
	
	public void remove(Object aKey) {
		ConcurrentHashMap<Object, Object> map = tLocal.get();
		map.remove(aKey);
	}

	public void clear() {
		tLocal.remove();
	}
}
