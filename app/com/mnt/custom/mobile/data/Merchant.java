package com.mnt.custom.mobile.data;

import java.util.List;


public class Merchant  {
	
	public String merchantName;
	public String merchantAddress;
	public String merchantState;
	public String merchantStreet;
	public String merchantCity;
	public String merchantPin;
	public String merchantId;
	public String merchantPhone;
	public String merchantImage;
	public List<Candiez> featured;
	public List<Candiez> program;
	public boolean isFeatured;
	public String information;
	public Merchant(String merchantName, String merchantAddress, String merchantStreet,
			String merchantState, String merchantCity, String merchantPin,
			String merchantPhone, String merchantId,String merchantImage, boolean isFeature, List<Candiez> featured,
			String information) {
		super();
		this.merchantName = merchantName;
		this.merchantAddress = merchantAddress;
		this.merchantState = merchantState;
		this.merchantCity = merchantCity;
		this.merchantPin = merchantPin;
		this.merchantPhone = merchantPhone;
		this.merchantId = merchantId;
		this.merchantImage = merchantImage;
		this.featured = featured;
		this.merchantStreet = merchantStreet;
		this.information = information;
		this.isFeatured = isFeatured;
	}
	
	public Merchant(String businessName, String address, String state, String merchantCity,
			String pin, String phoneNo, String id, String image) {
		this.merchantName = businessName;
		this.merchantAddress = address;
		this.merchantState = state;
		this.merchantCity = merchantCity;
		this.merchantPin = pin;
		this.merchantPhone = phoneNo;
		this.merchantId = id;
		this.merchantImage = image;
	}
	
	@Override
	public int hashCode() {
		return merchantId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return merchantId.equals(((Merchant)obj).merchantId);
	}
}
