package com.mnt.core.ui.component;


public class AddButton implements UIButton {

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
		return "Create";
	}

	@Override
	public String id() {
		return "_addButton";
	}

	

}
