package com.lbg.ib.api.sales.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BigDecimalFieldValidation {
    //Taking String as only primitive types are allowed as annotation members.
    String max() default "999999999999.99";

    String min() default "0";;

}
