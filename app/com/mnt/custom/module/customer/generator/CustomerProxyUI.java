package com.mnt.custom.module.customer.generator;

import javax.persistence.Id;

import play.db.ebean.Model;

import com.mnt.core.ui.annotation.SearchColumnOnUI;
import com.mnt.core.ui.annotation.SearchFilterOnUI;
import com.mnt.core.ui.annotation.UIFields;
import com.mnt.core.ui.annotation.Validation;
import com.mnt.core.ui.annotation.WizardCardUI;
public class CustomerProxyUI extends Model{
	public static String ENTITY_NAME = "Customer";
	@Id
	@WizardCardUI(name="Step 1",step=1)
	@UIFields(order=0,label="id",hidden=true)
	public String id;
	
	@UIFields(order=1,label="Phone No")
	@SearchFilterOnUI(label="Customer Phone No")
	@SearchColumnOnUI(colName="Phone No",rank=1)
	@WizardCardUI(name="Step 1",step=1)
	@Validation(required=true,maxlength=10,digits=true,remote="/checkuniquephone", messages="Phone number already registered.")
	public String phoneNo;
	
	@UIFields(order=2,label="First Name")
	@SearchFilterOnUI(label="First Name")
	@SearchColumnOnUI(colName="First Name",rank=2)
	@WizardCardUI(name="Step 1",step=1)
	@Validation(required=true)
	public String firstName;
	
	@UIFields(order=3,label="Last Name")
	@SearchFilterOnUI(label="Last Name")
	@SearchColumnOnUI(colName="Last Name",rank=3)
	@WizardCardUI(name="Step 1",step=1)
	public String lastName;
	
	@UIFields(order=4,label="Email Id")
	@SearchColumnOnUI(colName="Email Id",rank=4)
	@WizardCardUI(name="Step 1",step=1)
	public String email;
	
	@UIFields(order=5,label="Address",type="TEXTAREA",row=4)
	@SearchColumnOnUI(colName="Address",rank=5)
	@WizardCardUI(name="Step 1",step=1)
	public String address;
	

}
