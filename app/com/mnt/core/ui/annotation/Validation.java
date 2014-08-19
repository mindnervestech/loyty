package com.mnt.core.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface Validation {
	boolean required() default false;
	int minlength() default 0;
	int maxlength() default 1000; 
	boolean email()  default false;
	boolean date() default false;
	String messages() default "";
	boolean number() default false;
	boolean digits() default false; 
	String remote() default ""; 
}
