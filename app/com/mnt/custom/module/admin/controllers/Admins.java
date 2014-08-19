package com.mnt.custom.module.admin.controllers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import models.merchant.Merchant;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.editWizard;
import views.html.admin.indexAdmin;

import com.custom.emails.Email;
import com.mnt.core.auth.BasicAuth;
import com.mnt.core.auth.EDHelper;
import com.mnt.core.menu.MenuBarFixture;
import com.mnt.custom.module.admin.generator.AdminProxyUIContext;
import com.mnt.custom.module.merchant.generator.MerchantSave;

import controllers.Secured;
@Security.Authenticated(Secured.class)
@BasicAuth
public class Admins extends Controller{

	public static Result index(){
		return ok(indexAdmin.render(AdminProxyUIContext.getInstance().build(),
				MenuBarFixture.build(request().username()),
				Merchant.findByUserName(request().username())));
	}

	public static Result search() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		return ok(Json.toJson(AdminProxyUIContext.getInstance().build().doSearch(form)));
	}

	public static Result create(){
		try {
			Map<String,String> extraParam = new HashMap<String,String>();
			extraParam.put("status", "Disapproved");
			MerchantSave saveUtils = new MerchantSave(extraParam);
			saveUtils.doSave(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok("Merchant Created Successfully");
	}
	public static Result approved() throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, IOException{
		DynamicForm form = new DynamicForm().bindFromRequest();

		String id = (form.get("query"));
		Merchant merchant= Merchant.findById(id);
		merchant.status = "Approved";
		merchant.code = "aqk145";
		
		String _tempPassword = generatePassword();
		System.out.println(_tempPassword);
		merchant.password = EDHelper.doEncryption(_tempPassword);
		merchant.update();

		String recipients = "";
		String subject = "";
		String body = "";
		recipients = merchant.email;

		subject = "Super Admin Approved Your Application";
		body = "Your Application has been Approved :";
		body += "\nUser Name :" + merchant.email;
		body += "\nPassword :" + _tempPassword;
		body += "\nMerchant Code :" + merchant.code;		

		Email.sendOnlyMail(recipients, subject, body);

		return ok("Merchant has Approved");
	}
	public static Result disapproved(){
		DynamicForm form = new DynamicForm().bindFromRequest();
		String id = (form.get("query"));
		Merchant merchant= Merchant.findById(id);
		merchant.status = "Disapproved";
		merchant.update();
		String recipients = "";
		String subject = "";
		String body = "";
		recipients = merchant.email;

		subject = "Super Admin Approved Your Application";
		body = "Your Application has been Disapproved :";
		body += "\nUser Name :" + merchant.email;

		Email.sendOnlyMail(recipients, subject, body);

		return ok("Merchant has Disapproved");
	}


	public static Result edit(){
		try {
			MerchantSave saveUtils = new MerchantSave();
			saveUtils.doSave(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok("Merchant Edited Successfully");
	}

	public static Result showEdit() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String id = null;
		try{
			id = (form.get("query"));
			return ok(editWizard.render(new AdminProxyUIContext(Merchant.findById(id)).build()));
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}
		return ok("Not able to show Results, Check Logs");	
	}

	public static String generatePassword()
	{
		String alphaNumerics = "qwertyuiopasdfghjklzxcvbnm1234567890";
		String _randomPwd = "";
		for (int i = 0; i < 8; i++) {
			_randomPwd += alphaNumerics.charAt((int) (Math.random() * alphaNumerics.length()));
		}

		return _randomPwd;
	}
	
}
