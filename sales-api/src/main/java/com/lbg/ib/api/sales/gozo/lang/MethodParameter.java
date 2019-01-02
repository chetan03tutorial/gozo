package com.lbg.ib.api.sales.gozo.lang;

import java.lang.annotation.Annotation;
import java.util.List;

public class MethodParameter {

	private List<Annotation> annotations;
	private String paramName;
	private Class<?> returnType;
	private Object paramValue;
	
	
	public List<Annotation> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	public Object getParamValue() {
		return paramValue;
	}
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}
	
	public Annotation getAnnotation(Class<?> clazz)
	{
		Annotation annotation = null;
		for(Annotation paramAnnotation : annotations){
			if (clazz.isAssignableFrom(paramAnnotation.annotationType())){
				annotation = paramAnnotation;
				break;
			}
		}
		if(annotation == null){
			throw new IllegalArgumentException(clazz + " cannot be found on " + getParamName());
		}
		return annotation;
	}
	
}
