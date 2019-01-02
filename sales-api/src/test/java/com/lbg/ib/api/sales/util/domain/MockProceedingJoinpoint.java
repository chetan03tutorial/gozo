package com.lbg.ib.api.sales.util.domain;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

public class MockProceedingJoinpoint implements ProceedingJoinPoint {

	public String toShortString() {
		return null;
	}

	public String toLongString() {
		return null;
	}

	public Object getThis() {
		return null;
	}

	public Object getTarget() {
		return null;
	}

	public Object[] getArgs() {
		return null;
	}

	public Signature getSignature() {
		return null;
	}

	public SourceLocation getSourceLocation() {
		return null;
	}

	public String getKind() {
		return null;
	}

	public StaticPart getStaticPart() {
		return null;
	}

	public void set$AroundClosure(AroundClosure arc) {
	}

	public Object proceed() throws Throwable {
		return null;
	}

	public Object proceed(Object[] args) throws Throwable {
		return null;
	}

}
