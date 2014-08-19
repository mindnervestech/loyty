package com.mnt.custom.domain;

import com.core.domain.DomainEnum;

public enum LoyaltyProgram implements DomainEnum {
	Visit_Basis("Visit Basis"),
	Point_Basis("Point Basis");
	
	public String forName;
	
	private LoyaltyProgram(String forname)
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
