package com.lbg.ib.api.sales.header.markers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;
import com.lbg.ib.api.sales.header.builder.BapiHeader;
import com.lbg.ib.api.sales.header.builder.ContactPointHeader;
import com.lbg.ib.api.sales.header.builder.SecurityHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;

@Retention(RetentionPolicy.RUNTIME)
public @interface PcaSoapHeaders {

	String serviceName() default "";

	String serviceAction() default "";

	Class<? extends AbstractBaseHeader>[] headers() default { ServiceRequestHeader.class, SecurityHeader.class,
			ContactPointHeader.class, BapiHeader.class };
}
