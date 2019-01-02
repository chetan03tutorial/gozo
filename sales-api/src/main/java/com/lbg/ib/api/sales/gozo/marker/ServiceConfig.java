package com.lbg.ib.api.sales.gozo.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceConfig {
	String value();
}
