package com.mnt.custom.module.candy.generator;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import play.data.format.Formats;
import play.db.ebean.Model;

import com.mnt.core.ui.annotation.SearchColumnOnUI;
import com.mnt.core.ui.annotation.SearchFilterOnUI;
import com.mnt.core.ui.annotation.UIFields;
import com.mnt.core.ui.annotation.Validation;
import com.mnt.core.ui.annotation.WizardCardUI;
import com.mnt.custom.domain.LoyaltyProgram;
import com.mnt.custom.domain.Status;
public class CandiesProxyUI   extends Model{

	@UIFields(order=1,label="Code")
	@SearchFilterOnUI(label="Code")
	@SearchColumnOnUI(colName="Code",rank=1)
	@WizardCardUI(name="Define Loyalty",step=1)
	@Validation(digits=true,required=true)
	public String code;
	
	@UIFields(order=2,label="Description")
	@SearchFilterOnUI(label="Description")
	@WizardCardUI(name="Define Loyalty",step=1)
	@SearchColumnOnUI(colName="Description",rank=2)
	@Validation(required=true)
	public String name;
	
	@UIFields(order=2,label="Status")
	@SearchFilterOnUI(label="Status")
	@WizardCardUI(name="Define Loyalty",step=1)
	@SearchColumnOnUI(colName="Status",rank=2)
	@Enumerated(EnumType.STRING)
	@Validation(required=true)
	public Status status;
	
	@UIFields(order=3,label="Valid Till")
	@WizardCardUI(name="Define Loyalty",step=1)
	@SearchColumnOnUI(colName="Valid Till",rank=3)
	@Validation(required=true)
	@Formats.DateTime(pattern="dd-mm-yyyy")
	public Date valid_till;
			
	@UIFields(order=4,label="Type of Loyalty Program")
	@WizardCardUI(name="Define Loyalty",step=1)
	@Validation(required=true)
	@Enumerated(EnumType.STRING)
	public LoyaltyProgram type_of_loyalty_program;
	
	@UIFields(order=5,label="Loyalty Type")
	@WizardCardUI(name="Loyalty Type",step=1)
	@SearchColumnOnUI(colName="Loyalty Type",rank=4)
	public String loyaltyType;
}
