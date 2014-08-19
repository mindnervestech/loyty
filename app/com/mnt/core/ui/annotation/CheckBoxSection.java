package com.mnt.core.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CheckBoxSection {
	
	int orientation() default 0;
	boolean isTabular() default false;
	int rows() default 1;
	int cols() default 0;

}
