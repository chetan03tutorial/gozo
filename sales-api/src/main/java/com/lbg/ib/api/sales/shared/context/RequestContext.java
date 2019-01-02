package com.lbg.ib.api.sales.shared.context;

import java.util.concurrent.ConcurrentHashMap;

public class RequestContext {
	private static ThreadLocal<ConcurrentHashMap<Object, Object>> tLocal = new ThreadLocal<ConcurrentHashMap<Object, Object>>() {

		@Override
		protected ConcurrentHashMap<Object, Object> initialValue() {
			return new ConcurrentHashMap<Object, Object>();
		}

	};

	public static void setInRequestContext(Object aKey, Object aCtx) {
		ConcurrentHashMap<Object, Object> map = tLocal.get();
		map.put(aKey, aCtx);
	}

	public static Object getRequestContext() {
		return tLocal.get();
	}

	public static Object getInRequestContext(Object aKey) {
		ConcurrentHashMap<Object, Object> map = tLocal.get();
		Object obj = map.get(aKey);
		return obj != null ? obj : null;
	}

	public static void remove(Object aKey) {
		ConcurrentHashMap<Object, Object> map = tLocal.get();
		map.remove(aKey);
	}

	public static void clear() {
		tLocal.remove();
	}
}
