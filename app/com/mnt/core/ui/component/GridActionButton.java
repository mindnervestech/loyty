package com.mnt.core.ui.component;

import com.mnt.core.ui.component.UIButton.ButtonActionType;

public interface GridActionButton {
	
	String url();
	boolean visibility();
	String label();
	String id();
	IconType icon();
	String tooltip();
	ButtonActionType target();
	
	public enum IconType{
		Add,
		Delete,
		Edit,
		Tick,
		Cross
	}

}
