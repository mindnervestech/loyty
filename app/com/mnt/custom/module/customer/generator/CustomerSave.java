package com.mnt.custom.module.customer.generator;


import java.util.Map;

import models.customer.Customer;

import com.mnt.core.helper.SaveModel;

public class CustomerSave extends SaveModel<Customer> {

	public CustomerSave() {
		ctx = Customer.class;
	}
	
	public CustomerSave(Map<String,String> extraMap) {
		ctx = Customer.class;
		extraParams = extraMap;
	}
}