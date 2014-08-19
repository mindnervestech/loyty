package com.mnt.candiez.data;

public class Point {
	 public int milestoneAt;
     public String pointURL;
     public String description;
     public boolean canBeStamped;
     public Point(int milestoneAt, String pointURL, String description,
             boolean canBeStamped) {
     super();
     this.milestoneAt = milestoneAt;
     this.pointURL = pointURL;
     this.description = description;
     this.canBeStamped = canBeStamped;
     }

}
