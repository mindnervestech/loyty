package com.mnt.custom.mobile.data;

public class Point {
	
	public int milestoneAt;
	public String pointURL;
	public String description;
	public boolean canBeStamped;
	public float progressInPercent;
	public Point(int milestoneAt, String pointURL, String description,
			boolean canBeStamped, float progressInPercent) {
		super();
		this.milestoneAt = milestoneAt;
		this.pointURL = pointURL;
		this.description = description;
		this.canBeStamped = canBeStamped;
		this.progressInPercent = progressInPercent;
	}
	
	
}
