package com.lbg.ib.api.alligator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormParameter {
	
	String param();
	
	//Class<?> reader() default LocalFileReader.class;
	
	//Class<? extends RequestDataHandler> dataHandler() default DefaultDataHandler.class;

	/*static final class DefaultFormParameterDataHandler implements RequestDataHandler {
		@Override
		public String doHandle(String data) {
			return data;
		}
	}*/
}
