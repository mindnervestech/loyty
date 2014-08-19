package com.mnt.core.helper;

import java.util.List;
import java.util.Set;

import com.mnt.core.ui.component.FieldType;
import com.mnt.core.ui.component.UIButton;

public interface UIModel {
	
	List<FieldType> getFields();
	List<UIButton> getButtonActions();
	String pageName();
	
	String pageTitle();
	String pageSubTitle();
	
	List<Set<FieldType>> getFieldLayout();
	
}
