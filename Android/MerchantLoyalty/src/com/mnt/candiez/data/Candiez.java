package com.mnt.candiez.data;

import java.util.ArrayList;
import java.util.List;

public class Candiez{
	public String programId;
	public String programDescription;
	public String programType;
	public String amount;
	public String total;
	public Long validTill;
	public double howMany;
	public List<Candy> candiez = new ArrayList<Candy>();
	public List<Stamp> stamps = new ArrayList<Stamp>();
	public List<Point> points = new ArrayList<Point>();
}
