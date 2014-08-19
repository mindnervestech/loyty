package com.mnt.core.ui.component;

import com.core.domain.DomainEnum;




public interface FieldType extends Comparable<FieldType>{
	Type ctype();
	DomainEnum[] options();
	
	String label();
	String name();
	int order();
	boolean hidden();
	boolean autocomplete();
	boolean isSearchable();
	boolean mandatory();
	ValueWrapper value();
	JQValidation validation();
	int row();
	int col();
	String cssClass();
	String htmlAttrib();
	
	
	public interface JQValidation{
		boolean required() ;
		int minlength() ;
		int maxlength() ; 
		boolean email() ;
		boolean date() ;
		String messages() ;
		boolean number();
		boolean digits();
		String remote();
	}
	
	enum Type{
		INPUT,
		SELECT_OPTION,
		LIST,
		SEARCH,
		DATE,
		CHECKBOX,
		TEXTAREA,
		BLOB
	}
}
