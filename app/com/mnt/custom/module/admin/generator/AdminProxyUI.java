package com.mnt.custom.module.admin.generator;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import play.db.ebean.Model;

import com.mnt.core.ui.annotation.SearchColumnOnUI;
import com.mnt.core.ui.annotation.SearchFilterOnUI;
import com.mnt.core.ui.annotation.UIFields;
import com.mnt.core.ui.annotation.Validation;
import com.mnt.core.ui.annotation.WizardCardUI;
import com.mnt.custom.domain.AdminStatus;

public class AdminProxyUI extends Model{
	public static String ENTITY_NAME = "Merchant";
	
	@Id
	@WizardCardUI(name="Details",step=0)
	@UIFields(order=0,label="id",hidden=true)
	public String id;
	
	@UIFields(order=1,label="First Name")
	//@SearchFilterOnUI(label="First Name")
	@SearchColumnOnUI(colName="First Name",rank=1)
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true)
	public String firstName;
	
	@UIFields(order=2,label="Last Name")
	//@SearchFilterOnUI(label="Last Name")
	@SearchColumnOnUI(colName="Last Name",rank=2)
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true)
	public String lastName;
	
	@UIFields(order=3,label="Password")
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true)
	public String password;

	@UIFields(order=10,label="status")
	@SearchFilterOnUI(label="Status")
	@SearchColumnOnUI(colName="status",rank=10)
	@WizardCardUI(name="Details",step=1)
	@Enumerated(EnumType.STRING)
	@Validation(required=true)
	public AdminStatus status;

	@UIFields(order=4,label="Business Name")
	@SearchColumnOnUI(colName="Business Name",rank=3)
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true)
	public String businessName;

	@UIFields(order=5,label="Email Id")
	@SearchColumnOnUI(colName="Email Id",rank=4)
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true)
	public String email;
	
	@UIFields(order=6,label="Address",type="TEXTAREA")
	@SearchColumnOnUI(colName="Address",rank=5)
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true)
	public String address;
	
	@UIFields(order=7,label="City")
	@SearchColumnOnUI(colName="City",rank=6)
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true)
	public String city;
	
	
	@UIFields(order=8,label="Pin")
	@SearchColumnOnUI(colName="Pin",rank=7)
	@WizardCardUI(name="Details",step=1)
	@Validation(required=true,digits=true,maxlength=6)
	public String pin;
	
	@UIFields(order=9,label="Phone No")
	@SearchColumnOnUI(colName="Phone No",rank=8)
	@WizardCardUI(name="Details",step=1)
	@Validation(digits=true,maxlength=10)
	public String phoneNo;

}
