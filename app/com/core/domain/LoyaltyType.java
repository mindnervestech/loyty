package com.core.domain;

public enum LoyaltyType {

	VISIT("Visit"),
	POINT("Point");
	
	private LoyaltyType(String forName){
        this.forName=forName;
	}
	
	private String forName;
	
	public String getName(){
        return forName;
 }
}
