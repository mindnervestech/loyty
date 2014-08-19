/**
 * 
 */
package com.mnt.custom.domain;

import com.core.domain.DomainEnum;

public enum Status implements DomainEnum {
	Active("Active"),
    Expired("Expired");
	
	public String forName;
	
	private Status(String forname)
	{
		this.forName=forname;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return forName;
	}

	@Override
	public boolean uiHidden() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
