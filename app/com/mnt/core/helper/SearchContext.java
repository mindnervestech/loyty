package com.mnt.core.helper;

import java.util.List;
import java.util.Map;

import play.data.DynamicForm;

import com.mnt.core.ui.annotation.SearchColumn;
import com.mnt.core.ui.component.FieldType;
import com.mnt.core.ui.component.GridActionButton;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.ui.component.WizardStep;
import com.mnt.core.utils.GridViewModel;


public interface SearchContext {
	
	List<FieldType> getFilter();
	
	List<SearchColumn> getColumn();
	
	String searchUrl();
	
	String editUrl();
	
	String createUrl();
	
	String deleteUrl();
	
	String generateExcel();
	
	String entityName();
	String entityDisplayName();
	
	String onDoubleRowClickCallID();
	
	SearchContext withFieldNamePrefix(String fieldName);
	
	SearchContext build();
	
	boolean isMultiSelectSearch();
	
	GridViewModel doSearch(DynamicForm form);
	
	//HSSFWorkbook doExcel(DynamicForm form);
	
	Map<String, String> autoCompleteUrls(); 
	
	public enum SearchType{
		Like,
		Exact
	}

	List<WizardStep> getWizards();

	Map<String,SearchContext> getSearchContexts();

	String showEditUrl();

	List<UIButton> getButtonActions();
	List<GridActionButton> getGridActions();
	UIButton showAddButton();
	UIButton showEditButton();
	
	String displayName();

}
