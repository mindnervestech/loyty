package com.mnt.custom.module.admin.generator;


import models.merchant.Merchant;

import com.mnt.core.helper.SaveModel;

public class AdminSave extends SaveModel<Merchant> {

	public AdminSave() {
		ctx = Merchant.class;
	}
	
}