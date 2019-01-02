package com.lbg.ib.api.alligator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lbg.ib.api.alligator.core.RequestDataHandler;
import com.lbg.ib.api.alligator.core.RequestDataHandler.DefaultDataHandler;
import com.lbg.ib.api.alligator.web.request.LocalFileReader;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBody {

	Class<?> reader() default LocalFileReader.class;

	Class<? extends RequestDataHandler> dataHandler() default DefaultDataHandler.class;
	
	boolean file() default false;

	/*
	 * static final class DefaultFormParameterDataHandler implements
	 * RequestDataHandler {
	 * 
	 * @Override public String doHandle(String data) { return data; } }
	 */

}
