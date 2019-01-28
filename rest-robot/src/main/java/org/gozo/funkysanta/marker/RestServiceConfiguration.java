package org.gozo.funkysanta.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RestServiceConfiguration {
	String value();
}
