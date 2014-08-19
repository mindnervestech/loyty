package com.mnt.core.ui.component;

public interface UIButton {
	
	String url();
	boolean visibility();
	ButtonActionType target();
	String label();
	String id();
	
	
	enum ButtonActionType{
		ACTION,
		SUBMIT,
		MODAL,
		IMODAL,
		NEW,
		POPUP
	}

}
