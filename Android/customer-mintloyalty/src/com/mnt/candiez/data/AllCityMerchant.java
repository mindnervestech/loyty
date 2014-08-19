package com.mnt.candiez.data;

import java.util.ArrayList;
import java.util.List;

public class AllCityMerchant {
	public String programId;
    public String programDescription;
    public String programType;
    public String amount;
    public String validTill;
    public double howMany;
    public List<Candy> candiez = new ArrayList<Candy>();
    public List<Stamp> stamps = new ArrayList<Stamp>();
    public List<Point> points = new ArrayList<Point>();
    public Integer total;
    public String merchantName;
    public String merchantLocality;
    public String merchantLogo;
    public double[] merchantPosition;
    public AllCityMerchant(String programid, String programdescription, String programtype, String amoun,
			String validtill, double howmany, List<Candy> candie , List<Stamp> stamp, List<Point> point
			, Integer total, String merchantname,String merchantlocality,String merchantlogo,double[] merchantposition) {
		this.programId = programid;
		this.programDescription = programdescription;
		this.programType = programtype;
		this.amount = amoun;
		this.validTill = validtill;
		this.howMany = howmany;
		this.candiez = candie;
		this.stamps = stamp;
		this.points = point;
		this.total = total;
		this.merchantName = merchantname;
		this.merchantLocality = merchantlocality;
		this.merchantLogo = merchantlogo;
		this.merchantPosition = merchantposition;
	}

}
