package com.mnt.core.ui.component;

public class BuildUIButton implements UIButton{

	private String url;
	private boolean visibility = false;
	private ButtonActionType target = ButtonActionType.ACTION;
	private String label = "Save";
	private String id = "SaveID";
	
	private BuildUIButton(){}
	
	
	@Override
	public String url() {
		return url;
	}

	@Override
	public boolean visibility() {
		return visibility;
	}

	@Override
	public ButtonActionType target() {
		return target;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public String id() {
		return id;
	}
	
	public static BuildUIButton me(){
		return new BuildUIButton();
	}
	
	public BuildUIButton withUrl(String url){
		this.url = url;
		return this;
	}
	public BuildUIButton withVisibility(boolean visibility){
		this.visibility = visibility;
		return this;
	}
	
	public BuildUIButton withVisibilityTrue(){
		this.visibility = true;
		return this;
	}
	public BuildUIButton withTarget(ButtonActionType target){
		this.target = target;
		return this;
	}
	
	public BuildUIButton withTargetModal(){
		this.target = ButtonActionType.MODAL;
		return this;
	}
	public BuildUIButton withLabel(String label){
		this.label = label;
		return this;
	}
	public BuildUIButton withID(String id){
		this.id = id;
		return this;
	}
}
