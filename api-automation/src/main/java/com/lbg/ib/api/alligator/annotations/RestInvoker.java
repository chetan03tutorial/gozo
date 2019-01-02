package com.lbg.ib.api.alligator.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.lbg.ib.api.alligator.constants.ContentType;
import com.lbg.ib.api.alligator.web.request.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
public @interface RestInvoker {
	String service() ;
	HttpMethod method();
	ContentType contentType() default ContentType.JSON;
}
