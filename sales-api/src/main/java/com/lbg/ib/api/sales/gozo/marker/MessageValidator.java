package com.lbg.ib.api.sales.gozo.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.lbg.ib.api.sales.gozo.validators.ServiceValidator;



@Retention(RetentionPolicy.RUNTIME)
public @interface MessageValidator {
	Class<? extends ServiceValidator> value();
}
