package com.mnt.custom.domain;

import com.core.domain.DomainEnum;


public enum AdminStatus implements DomainEnum {
	Pending("Pending"),
	Approved("Approved"),
	Disapproved("Disapproved");
	
	public String forName;
	
	private AdminStatus(String forname)
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
