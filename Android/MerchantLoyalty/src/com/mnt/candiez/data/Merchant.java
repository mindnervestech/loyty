package com.mnt.candiez.data;

import java.util.List;

public class Merchant  {	
	public String merchantName;
	public String merchantAddress;
	public String merchantState;
	public String merchantCity;
	public String merchantPin;
	public String merchantId;
	public String merchantPhone;
	public String merchantImage;
	public boolean isFeatured;
	public Merchant(String merchantName, String merchantAddress,
			String merchantState, String merchantCity, String merchantPin,
			String merchantPhone, String merchantId,String merchantImage, boolean isFeature) {
		super();
		this.merchantName = merchantName;
		this.merchantAddress = merchantAddress;
		this.merchantState = merchantState;
		this.merchantCity = merchantCity;
		this.merchantPin = merchantPin;
		this.merchantPhone = merchantPhone;
		this.merchantId = merchantId;
		this.merchantImage = merchantImage;
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
	
//	@Override
//	public int hashCode() {
//		return merchantProgId.hashCode();
//	}
	
	@Override
	public boolean equals(Object obj) {
		return merchantId.equals(((Merchant)obj).merchantId);
	}
}
