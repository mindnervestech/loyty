package com.mnt.custom.module.customer.controllers;

import java.util.HashMap;
import java.util.Map;

import models.audit.Audit;
import models.audit.Audit.EntityAction;
import models.audit.Audit.EntityType;
import models.customer.Customer;
import models.merchant.Merchant;

import org.bson.types.ObjectId;

import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.editWizard;
import views.html.customer.customerIndex;

import com.mnt.core.auth.BasicAuth;
import com.mnt.core.menu.MenuBarFixture;
import com.mnt.custom.module.customer.generator.CustomerProxyUIContext;
import com.mnt.custom.module.customer.generator.CustomerSave;
import com.mongodb.DBObject;

import controllers.Secured;
@Security.Authenticated(Secured.class)
@BasicAuth
public class Customers extends Controller{

	
	public static Result index(){
		return ok(customerIndex.render(CustomerProxyUIContext.getInstance().build(),
				MenuBarFixture.build(request().username()),
				Merchant.findByUserName(request().username())));
	}
	
	public static Result search() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		form.data().put("email", request().username());
		return ok(Json.toJson(CustomerProxyUIContext.getInstance().build().doSearch(form)));
    }
	
	public static Result create(){
		try {
			Merchant merchant = Merchant.findByUserName(request().username());
			Map<String,String> extraParam = new HashMap<String, String>();
			extraParam.put("merchant.id", merchant.id);
			CustomerSave saveUtils = new CustomerSave(extraParam);
			Customer customer = (Customer) saveUtils.doSave(false);
			Audit audit = new Audit(merchant.id,customer.id,EntityType.CUSTOMER,EntityAction.CREATED,"");
			audit.save();
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError();
		}
		return ok("Customer Created Successfully ");
	}
	
	public static Result edit(){
		try {
			Merchant merchant = Merchant.findByUserName(request().username());
			Map<String,String> extraParam = new HashMap<String, String>();
			extraParam.put("merchant.id", merchant.id);
			
			CustomerSave saveUtils = new CustomerSave(extraParam);
			saveUtils.doSave(true);
		} catch (Exception e) {
			ok("Not able to Edit, Check Logs");
		}
		return ok("Customer Edited Successfully");
    }
	
	public static Result showEdit() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String id = null;
		try{
			id = (form.get("query"));
			return ok(editWizard.render(new CustomerProxyUIContext(Customer.findById(new ObjectId(id))).build()));
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}
		return ok("Not able to show Results, Check Logs");	
	}
	
}
