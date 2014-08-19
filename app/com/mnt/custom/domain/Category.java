/**
 * 
 */
package com.mnt.custom.domain;

import com.core.domain.DomainEnum;

public enum Category implements DomainEnum {
	resturants("Resturants/Lounge","resturants"),
	service("Services/Maintenance","service"),
	cafe("Cafe/Ice-Creams","cafe"),
	bakes("bakes/confectionery","bakes"),
	apparel("Apparel/accesories","apparel"),
	departmental("Departmental/Grocery","departmental"),
	care("Personal Care","care");
	
	public String forName;
	public String imageUrl;
	
	private Category(String forname,String imageUrl)
	{
		this.forName=forname;
		this.imageUrl =imageUrl;
	}
	@Override
	public String getName() {
		return forName;
	}

	@Override
	public boolean uiHidden() {
		return false;
	}
	
	
}
