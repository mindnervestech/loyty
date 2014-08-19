package com.core.domain;

import java.util.ArrayList;
import java.util.List;



public enum Menu {
	Home("/ ",null,"Home",Designation.Merchant),
	LoyaltyProgram("/candy",null,"My Candies",Designation.Merchant),
	ActionItem("/status",null,"Action Item",Designation.SuperAdmin),
	RegisterCustomer("/customer",null,"Customer Center",Designation.Merchant),
	RegisterMerchant("/merchant",null,"Register Merchant",Designation.SuperAdmin),
	Reports("/indexReport",null,"Reports",Designation.SuperAdmin);
	
	private String url;
	private Menu parent;
	private String uiDisplay;
	private List<Designation> blackListedDesignation = new ArrayList<Designation>();
 	
	
	private Menu(String _url,Menu parent,String uiDisplay,Designation ...designation ){
		this.url=_url;
		this.parent = parent;
		this.uiDisplay = uiDisplay;
		for(Designation d: designation){
			blackListedDesignation.add(d);
		}
	}
	
	public String url(){
		return url;
	}
	
		
	public Menu parent(){
		return parent;
	}
	
	public String display(){
		return uiDisplay;
	}
	
	public List<Designation> blackListedDesignations(){
		return blackListedDesignation;
	}
	
}
