package com.mnt.custom.module.merchant.generator;

import java.sql.Blob;

import javax.persistence.Id;

import play.db.ebean.Model;

import com.mnt.core.ui.annotation.SearchColumnOnUI;
import com.mnt.core.ui.annotation.SearchFilterOnUI;
import com.mnt.core.ui.annotation.UIFields;
import com.mnt.core.ui.annotation.Validation;
import com.mnt.core.ui.annotation.WizardCardUI;
import com.mnt.custom.domain.Category;
import com.mnt.custom.domain.City;

public class MerchantProxyUI extends Model{
	public static String ENTITY_NAME = "Merchant";
	
	@Id
	@WizardCardUI(name="Step 1",step=1)
	@UIFields(order=0,label="id",hidden=true)
	public String id;
	
	@UIFields(order=1,label="First Name")
	@SearchFilterOnUI(label="First Name")
	@SearchColumnOnUI(colName="First Name",rank=1)
	@WizardCardUI(name="Step 1",step=1)
	@Validation(required=true)
	public String firstName;
	
	@UIFields(order=2,label="Last Name")
	@SearchFilterOnUI(label="Last Name")
	@SearchColumnOnUI(colName="Last Name",rank=2)
	@WizardCardUI(name="Step 1",step=1)
	@Validation(required=true)
	public String lastName;
	
	@UIFields(order=3,label="Business Name")
	@SearchFilterOnUI(label="Business Name")
	@SearchColumnOnUI(colName="Business Name",rank=3)
	@WizardCardUI(name="Step 1",step=1)
	@Validation(required=true)
	public String businessName;

	@UIFields(order=4,label="Category")
	@SearchColumnOnUI(colName="Category",rank=9)
	@WizardCardUI(name="Step 1",step=1)
	public Category category;
	
	@UIFields(order=5,label="Email Id")
	@SearchColumnOnUI(colName="Email Id",rank=4)
	@WizardCardUI(name="Step 1",step=1)
	@Validation(required=true,remote="/emailAvailability",email=true, messages="Username is not available")
	public String email;
	
  	@UIFields(order=6,label="Password")
	@WizardCardUI(name="Step 1",step=1)
	@Validation(required=true)
	public String password;

	
	@UIFields(order=1,label="Locality",type="TEXTAREA")
	@SearchColumnOnUI(colName="Locality",rank=5)
	@WizardCardUI(name="Step 2",step=2)
	@Validation(required=true)
	public String locality;
	
	@UIFields(order=2,label="Street",type="TEXTAREA")
	@WizardCardUI(name="Step 2",step=2)
	@Validation(required=true)
	public String street;
	
	@UIFields(order=3,label="City")
	@SearchFilterOnUI(label="City")
	@SearchColumnOnUI(colName="City",rank=6)
	@WizardCardUI(name="Step 2",step=2)
	@Validation(required=true)
	public City city;
	
	
	@UIFields(order=4,label="Pin")
	@SearchColumnOnUI(colName="Pin",rank=7)
	@WizardCardUI(name="Step 2",step=2)
	@Validation(required=true,digits=true,maxlength=6)
	public String pin;
	
	@UIFields(order=5,label="Phone No")
	@SearchColumnOnUI(colName="Phone No",rank=8)
	@WizardCardUI(name="Step 2",step=2)
	@Validation(digits=true,maxlength=10)
	public String phoneNo;
	
	
	
	@UIFields(order=6,label="Select Image")
	@WizardCardUI(name="Step 2",step=2)
	//@Validation(required=true)
	public Blob image;
}
