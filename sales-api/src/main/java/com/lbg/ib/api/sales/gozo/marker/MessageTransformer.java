package com.lbg.ib.api.sales.gozo.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.lbg.ib.api.sales.gozo.mappers.MessageMapper;

@Retention(RetentionPolicy.RUNTIME)
public @interface MessageTransformer {
	Class<? extends MessageMapper> value();
}
