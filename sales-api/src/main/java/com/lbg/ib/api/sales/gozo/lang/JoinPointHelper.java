package com.lbg.ib.api.sales.gozo.lang;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

public class JoinPointHelper {

    public static MethodDetails getMethodDetails(JoinPoint joinPoint) {

        MethodDetails methodDetails = new MethodDetails();
        final Signature signature = joinPoint.getStaticPart().getSignature();
        final Object[] args = joinPoint.getArgs();

        if (signature instanceof MethodSignature) {
            final MethodSignature ms = (MethodSignature) signature;
            methodDetails.setMethodParams(MethodDetailExtractor.getMethodParameters(ms, args));
            methodDetails.setAnnotations(MethodDetailExtractor.getAnnotationsAppliedOverMethod(ms));
        }
        return methodDetails;
    }
    
    public static Object[] getArgs(MethodDetails methodDetails ) {
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
