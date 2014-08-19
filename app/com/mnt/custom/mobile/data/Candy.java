package com.mnt.custom.mobile.data;


public class Candy  {
	
	public String candyDescription;
	public Integer number;
	public String imageUrl;
	public String appUrl;
	
	public Candy(String candyId, String candyDescription, Integer number,
			String imageUrl, String appUrl) {
		super();
		this.candyDescription = candyDescription;
		this.number = number;
		this.imageUrl = imageUrl;
		this.appUrl = appUrl;
	}
	
}
