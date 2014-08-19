package com.mnt.custom.mobile.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Candiez  {
	
	public String programId;
	public String programDescription;
	public String programType;
	public String amount;
	public Date validTill;
	public double howMany;
	public List<Candy> candiez = new ArrayList<Candy>();
	public List<Stamp> stamps = new ArrayList<Stamp>();
	public List<Point> points = new ArrayList<Point>();
	public Integer total;
	
	public String merchantName;
	public String merchantLocality;
	public String merchantLogo;
	public List<Double> merchantPosition;
	
	public Candiez setMerchantInformation(String merchantName,String merchantLocality,String merchantLogo,List<Double>  merchantPosition ){
		this.merchantLocality = merchantLocality;
		this.merchantLogo = merchantLogo;
		this.merchantName = merchantName;
		this.merchantPosition = merchantPosition;
		return this;
	}
	
}
