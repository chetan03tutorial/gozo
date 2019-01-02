package com.lbg.ib.api.sales.gozo.lang;

import java.lang.annotation.Annotation;

import org.apache.commons.collections.CollectionUtils;

public class MethodDetailHelper {

	
	
	public static Annotation resolveAnnotation(MethodDetails methodDetails, Class<?> clazz) {
		for (Annotation annotation : methodDetails.getAnnotations()) {
			if (clazz.isAssignableFrom(annotation.getClass())) {
				return annotation;
			}
		}
		return null; // Handle it gracefully
	}
	
	public static Object[] getMethodArguments(MethodDetails methodDetails) {
		if (methodDetails.getMethodParams() != null
				&& CollectionUtils.isNotEmpty(methodDetails.getMethodParams().values())) {
			Object args[] = new Object[methodDetails.getMethodParams().size()];
			int index = 0;
			for (MethodParameter param : methodDetails.getMethodParams().values()) {
				args[index++] = param.getParamValue();
			}
			return args;
		}
		return null;
	}
}
