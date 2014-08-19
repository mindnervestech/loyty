package com.mnt.custom.mobile.data;

import java.util.Date;


public class Program  {
	
	public Program(String id, String programType, String programDescription, Date valid_till) {
		this.programId = id;
		this.programType = programType;
		this.programDescription =  programDescription;
		this.validTill = valid_till;
				
	}
	
	public String programId;
	public String programDescription;
	public String programType;
	public Date validTill;
	
}
