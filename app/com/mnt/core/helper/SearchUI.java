package com.mnt.core.helper;

import java.util.List;

import play.data.DynamicForm;

import com.mnt.core.ui.annotation.SearchColumn;
import com.mnt.core.ui.component.FieldType;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.utils.GridViewModel;


public interface SearchUI {
	
	List<FieldType> getFilter();
	
	List<SearchColumn> getColumn();
	
	String searchUrl();
	
	String triggerUrl();
	
	String entityName();
	
	SearchUI withFieldNamePrefix(String fieldName);
	
	boolean isMultiSelectSearch();
	
	GridViewModel doSearch(DynamicForm form);
	
	public enum SearchType{
		Like,
		Exact
	}
	
	List<UIButton> getButtonActions();
	
	List<UIButton> getGridActions();
	
}
