/**
 * 
 */
package com.mnt.custom.domain;

import com.core.domain.DomainEnum;

public enum City implements DomainEnum {
	Delhi("Delhi/NCR",""),
	Pune("Pune",""),
	Mumbai("Mumbai",""),
	Bangalore("Bangalore",""),
	Chandighar("Chandighar",""),
	Hyderabad("Hyderabad",""),
	Chennai("Chennai",""),
	Kolkata("Kolkata",""),
	Bhubneshwar("Bhubneshwar",""),
	Ahmedabad("Ahmedabad",""),
	Surat("Surat",""),
	Jaipur("Jaipur","");
	
	public String forName;
	public String imageUrl;
	
	private City(String forname,String imageUrl)
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
