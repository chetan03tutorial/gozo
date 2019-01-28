package org.gozo.funkysanta.lang.method;



import org.apache.commons.collections4.CollectionUtils;

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


	public  <T> T resolveAnnotation(Class<T> clazz) {
		for (Annotation annotation : this.getAnnotations()) {
			if (clazz.isAssignableFrom(annotation.getClass())) {
				return (T)annotation;
			}
		}
		return null;
	}

	public Object[] getMethodArguments() {
		if (this.getMethodParams() != null
				&& CollectionUtils.isNotEmpty(this.getMethodParams().values())) {
			Object args[] = new Object[this.getMethodParams().size()];
			int index = 0;
			for (MethodParameter param : this.getMethodParams().values()) {
				args[index++] = param.getParamValue();
			}
			return args;
		}
		return null;
	}

}
