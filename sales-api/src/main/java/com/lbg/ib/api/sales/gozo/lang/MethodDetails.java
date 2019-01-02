package com.lbg.ib.api.sales.gozo.lang;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class MethodDetails {
 
	private List<Annotation> annotations;
	private Map<String, MethodParameter> methodParams;
	public List<Annotation> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
	public Map<String, MethodParameter> getMethodParams() {
		return methodParams;
	}
	public void setMethodParams(Map<String, MethodParameter> methodParams) {
		this.methodParams = methodParams;
	}
	
	
}
