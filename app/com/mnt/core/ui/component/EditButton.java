package com.mnt.core.ui.component;


public class EditButton implements UIButton {

	@Override
	public String url() {
		return "#";
	}

	@Override
	public boolean visibility() {
		return true;
	}

	@Override
	public ButtonActionType target() {
		return ButtonActionType.MODAL;
	}

	@Override
	public String label() {
		return "Edit Selected";
	}

	@Override
	public String id() {
		return "_editButton";
	}

	

}
