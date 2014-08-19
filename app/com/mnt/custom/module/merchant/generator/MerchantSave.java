package com.mnt.custom.module.merchant.generator;


import java.util.Map;

import models.merchant.Merchant;

import com.mnt.core.helper.SaveModel;

public class MerchantSave extends SaveModel<Merchant> {

	public MerchantSave() {
		ctx = Merchant.class;
	}
	
	public MerchantSave(Map<String,String> extraMap) {
		ctx = Merchant.class;
		extraParams = extraMap;
	}	
}