package com.mnt.core.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface UIFields {
	int order() default -1;
	String label() default "";
	boolean hidden() default false;
	boolean autocomplete() default false;
	String autocompleteOn() default "id";
	boolean searchable() default false;
	String name() default "";
	boolean mandatory() default false;
	int row() default 0;
	int col() default 0;
	String cssClass() default "";
	String htmlAttrib() default "";
	String mappedKey() default "";
	String type() default "";
}
