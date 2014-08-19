package com.mnt.custom.module.merchant.controllers;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.bson.types.ObjectId;

import models.customer.Customer;
import models.merchant.Merchant;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.mvc.Security;
import views.html.editWizard;
import views.html.merchant.indexMerchant;

import com.custom.emails.Email;
import com.mnt.core.auth.BasicAuth;
import com.mnt.core.auth.EDHelper;
import com.mnt.core.menu.MenuBarFixture;
import com.mnt.core.utils.ImageCropper;
import com.mnt.custom.module.merchant.generator.MerchantProxyUIContext;
import com.mnt.custom.module.merchant.generator.MerchantSave;

import controllers.Secured;
@Security.Authenticated(Secured.class)
@BasicAuth
public class Merchants extends Controller{

	public static Result index(){
		return ok(indexMerchant.render(MerchantProxyUIContext.getInstance().build(),
				MenuBarFixture.build(request().username()),
				Merchant.findByUserName(request().username())));
	}
	
	public static Result search() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		return ok(Json.toJson(MerchantProxyUIContext.getInstance().build().doSearch(form)));
    }
	
	public static Result checkUniquePhone() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String phoneNo =form.get("q");
		Merchant merchant = Merchant.findByUserName(request().username());
		
		int customerNo = Customer.find().is("phoneNo", phoneNo).is("merchant._id", new ObjectId(merchant.id)).count();
		if(customerNo > 0) {
			return ok(Json.toJson(false));
		}
		return ok(Json.toJson(true));
	}

	public static Result create(){
		try {
	//Data from Super Admin 		
			MultipartFormData merchantImage = request().body().asMultipartFormData();
	 		MultipartFormData.FilePart picture = merchantImage.getFile("image");
	 		Map<String,String> extraParam = new HashMap<String,String>();
	 		
	 		if (picture != null) {
	 			File file = picture.getFile();

	 			Thumbnails.of(file).height(100).keepAspectRatio(true).toFile(file);
	 	 		
	 			Object _id = Merchant.saveImage(file, picture.getFilename());
	 			extraParam.put("image", _id.toString());
	 		}
			DynamicForm form = DynamicForm.form().bindFromRequest();
			String emailid =form.get("email");
			extraParam.put("status", "Approved");
			extraParam.put("designation", "Merchant");
			extraParam.put("tempPassword", "false");
			MerchantSave saveUtils = new MerchantSave(extraParam);
			saveUtils.doSave(false);

	// To send password on E-mail 		
			Merchant merchant = Merchant.find().is("email", emailid).next();
			String recipients = "";
	    	String subject = "";
	    	String body = "";
			
			String passWord = generatePassword();
	    	recipients = merchant.email;
	    	subject = "Account created by Super Admin email";
	    	body = "Your Login Details :";
	    	body += "\nUser Name :" + merchant.email;
	    	body += "\nPassword  :" + passWord;
	    	merchant.password =  EDHelper.doEncryption(passWord);
	    	merchant.tempPassword = "true";
	    	merchant.update();
	    	Email.sendOnlyMail(recipients, subject, body);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok("Merchant Created Successfully");
	}
	
	public static Result edit(){
		try {
			MultipartFormData merchantImage = request().body().asMultipartFormData();
	 		MultipartFormData.FilePart picture = merchantImage.getFile("image");
	 		Map<String,String> extraParam = new HashMap<String,String>();
	 		
	 		if (picture != null) {
	 			File file = picture.getFile();
	 			Thumbnails.of(file).height(100).keepAspectRatio(true).toFile(file);
	 			Object _id = Merchant.saveImage(file, picture.getFilename());
	 			extraParam.put("image", _id.toString());
	 		}
	 		
			extraParam.put("status", "Approved");
			extraParam.put("designation", "Merchant");
			
			MerchantSave saveUtils = new MerchantSave(extraParam);
			
			saveUtils.doSave(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok("Merchant Edited Successfully");
    }
	
	public static Result getMerchantImage(String image) throws IOException{
		return ok(Merchant.getImage(image)).as(("picture/stream"));
	}
	
	public static String generatePassword()
    {
		String alphaNumerics = "qwertyuiopasdfghjklzxcvbnm1234567890";
		String t = "";
	for (int i = 0; i < 8; i++) {
	    t += alphaNumerics.charAt((int) (Math.random() * alphaNumerics.length()));
	}
		return t;
    }
	
	public static Result showEdit() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String id = null;
		try{
			id = (form.get("query"));
			return ok(editWizard.render(new MerchantProxyUIContext(Merchant.findById(id)).build()));
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}
		return ok("Not able to show Results, Check Logs");	
	}
	
	public static Result emailAvailability()
	{
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String email = form.get("email");
		try{
			return Merchant.find().is("email", email).count() == 0 ? ok(Json.toJson(true)) : ok(Json.toJson(false));
		}
		catch (Exception e) {
			return ok(Json.toJson(true));
		}
		
	}
}
