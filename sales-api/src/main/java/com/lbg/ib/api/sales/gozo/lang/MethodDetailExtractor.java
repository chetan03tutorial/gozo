package com.lbg.ib.api.sales.gozo.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.reflect.MethodSignature;

public class MethodDetailExtractor {

	private MethodDetailExtractor() {
	}

	public static List<Annotation> getAnnotationsAppliedOverMethod(MethodSignature ms) {
		final Method method = ms.getMethod();
		return Arrays.asList(method.getAnnotations());
	}

	public static Map<String, MethodParameter> getMethodParameters(MethodSignature methodSignature, Object[] args) {
		final Map<String, MethodParameter> methodParams = new HashMap<String, MethodParameter>();
		final Method method = methodSignature.getMethod();

		String[] paramNames = methodSignature.getParameterNames();
		Annotation[][] annotation = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();

		if (annotation.length != paramNames.length || annotation.length != parameterTypes.length)
			throw new IllegalArgumentException("Some serious error in MethodDetailExtractors");

		for (int index = 0; index < paramNames.length; index++) {
			MethodParameter methodParameter = new MethodParameter();
			methodParameter.setParamName(paramNames[index]);
			methodParameter.setParamValue(args[index]);
			methodParameter.setReturnType(parameterTypes[index]);
			methodParameter.setAnnotations(new ArrayList<Annotation>(Arrays.asList(annotation[index])));

			methodParams.put(paramNames[index], methodParameter);
		}
		return methodParams;
	}
}
