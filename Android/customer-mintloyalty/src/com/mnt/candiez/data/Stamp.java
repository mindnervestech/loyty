package com.mnt.candiez.data;

public class Stamp{
	public boolean isSealed;
	public int index;
	public String stampURL;
	public String description;
	public boolean canBeStamped;
	public Stamp(boolean isSealed, int index, String stampURL,
			String description,boolean canBeStamped) {
		super();
		this.isSealed = isSealed;
		this.index = index;
		this.stampURL = stampURL;
		this.description = description;
		this.canBeStamped = canBeStamped;
	}
}
