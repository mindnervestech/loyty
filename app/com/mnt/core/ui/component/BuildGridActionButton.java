package com.mnt.core.ui.component;

import com.mnt.core.ui.component.UIButton.ButtonActionType;

public class BuildGridActionButton implements GridActionButton{

	private String url;
	private boolean visibility = false;
	private String label = "Save";
	private String id = "SaveID";
	private String tooltip = "";
	private IconType icon = IconType.Tick;
	private ButtonActionType target = ButtonActionType.ACTION;
	private BuildGridActionButton(){}
	
	@Override
	public String tooltip() {
		return tooltip;
	}
	@Override
	public String url() {
		return url;
	}
	@Override
	public ButtonActionType target() {
		return target;
	}
	
	@Override
	public boolean visibility() {
		return visibility;
	}

	
	@Override
	public String label() {
		return label;
	}

	@Override
	public String id() {
		return id;
	}
	
	@Override
	public IconType icon() {
		return icon;
	}
	
	public static BuildGridActionButton me(){
		return new BuildGridActionButton();
	}
	
	public BuildGridActionButton withUrl(String url){
		this.url = url;
		return this;
	}
	
	public BuildGridActionButton withTooltip(String tooltip){
		this.tooltip = tooltip;
		return this;
	}
	
	public BuildGridActionButton withVisibility(boolean visibility){
		this.visibility = visibility;
		return this;
	}
	
	public BuildGridActionButton withVisibilityTrue(){
		this.visibility = true;
		return this;
	}
	public BuildGridActionButton withIcon(IconType icon){
		this.icon = icon;
		return this;
	}
	
	
	public BuildGridActionButton withLabel(String label){
		this.label = label;
		return this;
	}
	public BuildGridActionButton withID(String id){
		this.id = id;
		return this;
	}
	
	public BuildGridActionButton withTargetModal(ButtonActionType actionType){
		this.target = actionType;
		return this;
	}


	


	
}
