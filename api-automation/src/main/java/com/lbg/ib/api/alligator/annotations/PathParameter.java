package com.lbg.ib.api.alligator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lbg.ib.api.alligator.core.RequestDataHandler;
import com.lbg.ib.api.alligator.core.RequestDataHandler.DefaultDataHandler;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParameter {
	String param();
	Class<? extends RequestDataHandler> dataHandler() default DefaultDataHandler.class;
}
