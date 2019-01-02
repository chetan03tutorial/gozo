package com.lbg.ib.api.alligator.lang;

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
}
