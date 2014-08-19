package com.mnt.candiez.data;

public class Candy{
	public String candyId;
	public String candyDescription;
	public Integer number;
	public String imageUrl;
	public String appUrl;
	
	public Candy(String candyId, String candyDescription, Integer number,
			String imageUrl, String appUrl) {
		super();
		this.candyId = candyId;
		this.candyDescription = candyDescription;
		this.number = number;
		this.imageUrl = imageUrl;
		this.appUrl = appUrl;
	}

}
