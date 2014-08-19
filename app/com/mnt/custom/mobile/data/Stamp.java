package com.mnt.custom.mobile.data;


public class Stamp {
	
	public boolean isSealed;
	public boolean isMileStone;
	public int index;
	public String stampURL = null;
	public String description;
	public boolean canBeStamped;
	public Stamp(boolean isSealed, int index, String stampURL,
			String description, boolean isMileStone, boolean canBeStamped) {
		super();
		this.isSealed = isSealed;
		this.index = index;
		this.stampURL = stampURL;
		this.description = description;
		this.isMileStone = isMileStone;
		this.canBeStamped = canBeStamped;
	}
	
	
}
