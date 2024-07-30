package com.generic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface ComparableField {
    GreaterThan greaterThan() default GreaterThan.IS_POSITIVE;

    enum GreaterThan {
      IS_NEGATIVE,
      IS_POSITIVE
    }
}
