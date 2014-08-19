package controllers;

import static play.data.Form.form;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import models.merchant.Merchant;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

import com.custom.emails.Email;
import com.mnt.core.auth.EDHelper;
import com.mnt.core.menu.MenuBarFixture;
import com.mnt.core.utils.ImageCropper;
import com.mnt.custom.module.merchant.generator.MerchantSave;

public class Application extends Controller {
	@Security.Authenticated(Secured.class)
	public static Result index() {
		return ok(index.render(MenuBarFixture.build(request().username()),Merchant.findByUserName(request().username())));
    }
    //Login Page
	public static Result login() {
		return ok(views.html.login._login.render(false));
   }
	
	//Landing Page
	public static Result landing() {
		return ok(views.html.login._login.render(false));//redirect("/assets/mobile/landing.html");
	}
	// Profile Page
	@Security.Authenticated(Secured.class)
	public static Result profilePage() {
		return ok(views.html.merchant.profilePage.render(MenuBarFixture.build(request().username()),Merchant.findByUserName(request().username())));
   }
	
	//Merchant Registration
    public static Result registration() {
    	return ok(views.html.login._registration.render());
    }
	
    //First Login set Password 
    public static Result setPassword()
	{
	    	return ok(views.html.login.setPassword.render());
	}
    
    //Count
    public static int pendingCount(){
    	int count = Merchant.find().is("status","Pending").count(); 
    	return count;
    }
    
    //upload merchant image
    @Security.Authenticated(Secured.class)
    public static Result upload()
	{
    	try{
    		MultipartFormData body = request().body().asMultipartFormData();
    		MultipartFormData.FilePart picture = body.getFile("merchantImage");
    		Merchant merchant = Merchant.findByUserName(request().username());
    		
    		Object _id = processMerchantImage(picture);
    		if(picture.getFilename().isEmpty())
    		{
    			return ok(views.html.merchant.profilePage.render(MenuBarFixture.build(request().username()),Merchant.findByUserName(request().username())));
    		}
    		Merchant.deleteImageById(merchant.image);
    		merchant.image = _id.toString();
    		merchant.update();
		} catch (Exception ei) {
			ei.printStackTrace();
		}   
		return ok(views.html.merchant.profilePage.render(MenuBarFixture.build(request().username()),Merchant.findByUserName(request().username())));
	}
       
    private static Object processMerchantImage(MultipartFormData.FilePart picture) throws Exception {
		File file = picture.getFile();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Thumbnails.of(file).width(174).height(106).toOutputStream(baos);
 		return Merchant.saveImage(baos, picture.getFilename());
	}
    
    
    //Authentication 
    public static Result authenticate() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
    	
    	 DynamicForm form = new DynamicForm().bindFromRequest();
         String email = form.get("email");
         String password = form.get("password");  
         if((email == null || email.isEmpty()) && (password == null || password.isEmpty()) ){

     		 flash("error","Please Enter E-mail and Password");
          	return badRequest(views.html.login._login.render(false));
         }
         
         if(email == null || email.isEmpty())
         {
        	 return badRequest(views.html.login._login.render(false));
         }
         try{
         	Merchant merchant = Merchant.find().is("email", email).next();
         	session().clear();
            response().setCookie("email", form.get("email"));
            session("email", form.get("email"));
            
            String originalPassword = EDHelper.doDecryption(merchant.password);
            
//            String originalPassword = merchant.password;
            
            if(!originalPassword.equals(password))
            {
         		 flash("error","Please Enter Valid Password");
              	return badRequest(views.html.login._login.render(false));
         	}
            else{
            	if(merchant.status.equals("Pending"))
            	{
            		 flash("error","Your Status Is Pending");
            		 return badRequest(views.html.login._login.render(false));
            	}
            	else if(merchant.status.equals("Disapproved"))
            	{
            		 flash("error","Your Status Is Currently Disapproved");
            		 return badRequest(views.html.login._login.render(false));
            	}
            	else if(merchant.tempPassword.equals("false"))
                	return redirect(routes.Application.index());
                else
                	 return ok(views.html.login._login.render(true));
            }
         }   
         catch (NoSuchElementException e) {
        	 flash("error","You have Entered Username that does not exists");
         	return badRequest(views.html.login._login.render(false));
		}
         
    }
    
  // reset Password 
    @Security.Authenticated(Secured.class)
    public static Result resetPassword(){
    	try {
    		DynamicForm form = DynamicForm.form().bindFromRequest();
    		Merchant merchant = Merchant.findByUserName(request().username());
    		String password = form.get("password");
    		merchant.password = password;
    		merchant.tempPassword = "false";
    		merchant.update();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    		return redirect(routes.Application.index());
    }
    
    // reset Password 
    @Security.Authenticated(Secured.class)
    public static Result _resetPassword(){
    	try {
    		DynamicForm form = DynamicForm.form().bindFromRequest();
    		Merchant merchant = Merchant.findById(form.get("query"));
    		String password = generatePassword();
    		String epassword = EDHelper.doEncryption(password);
    		merchant.password = epassword;
    		merchant.tempPassword = "false";
    		merchant.update();
    		String recipients = "";
	    	String subject = "";
	    	String body = "";
			
	    	recipients = merchant.email;
	    	subject = "Account created by Super Admin email";
	    	body = "Your Login Details :";
	    	body += "\nUser Name :" + merchant.email;
	    	body += "\nPassword  :" + password;
	    	Email.sendOnlyMail(recipients, subject, body);

    		
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return ok("Password reset successfully.");
//    	return redirect(routes.Application.index());
    }
    
    //Change Password
    @Security.Authenticated(Secured.class)
    public static Result changePassword(){
    	try {
    		DynamicForm form = DynamicForm.form().bindFromRequest();
    		Merchant merchant = Merchant.findByUserName(request().username());
    		String password = EDHelper.doEncryption(form.get("password"));
    		merchant.password = password;
    		merchant.update();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    		return redirect(routes.Application.index());
    }
    
    @Security.Authenticated(Secured.class)
    public static Result resetMerchantCode() {
    	try {
    		DynamicForm form = DynamicForm.form().bindFromRequest();
    		Merchant merchant = Merchant.findByUserName(request().username());
    		String code = form.get("code");
    		merchant.code = code;
    		merchant.update();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    		return redirect(routes.Application.index());
    }
    
    //to show forgot password page
    public static Result forgotpassword()
	{
			return ok(views.html.forgotpassword.emailvalidate.render());
	}
    
    public static Result finduser() throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, IOException
	{
			DynamicForm formObj = form().bindFromRequest();
			String emailid= formObj.get("inputemail");
			try{ 	
				Merchant merchant_email = Merchant.find().is("email", emailid).next();
	         	String recipients = "";
		    	String subject = "";
		    	String body = "";
		    	String passWord = generatePassword();
		    	recipients = merchant_email.email;
		    	subject = "Password recovery email";
		    	body = "Your Login Details :";
		    	body += "\nUser Name :" + merchant_email.email;
		    	body += "\nPassword  :" + passWord;
		    	merchant_email.password = EDHelper.doEncryption(passWord);
		    	merchant_email.tempPassword = "true";
		    	merchant_email.update();
		    	Email.sendOnlyMail(recipients, subject, body);
		    	return ok();	         	
			}
			catch (NoSuchElementException e) {
				return badRequest();
			}					
	}
    
    @Security.Authenticated(Secured.class)
    public static Result checkOldPassword(){
    	DynamicForm form = DynamicForm.form().bindFromRequest();
		String q = form.get("q");
		Merchant merchant = Merchant.findByUserName(request().username());
		if(q.equals(merchant.password))
		{
			return ok(Json.toJson(true));
		}
		else
			return ok(Json.toJson("Wrong Password"));
    } 
    
    
    
    
  // Registration  Form   
    public static Result createAccount(){
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	String code,email;
    	email = form.get("email");
    	Merchant merchant = null;
    	
		int count = Merchant.find().is("email", email).count();
		if(count == 0 ) {
			try {
				MultipartFormData body = request().body().asMultipartFormData();
		 		MultipartFormData.FilePart picture = body.getFile("merchantImage");
		 		Map<String,String> extraParam = new HashMap<String,String>();
		 		if (picture != null) {
		 			File file = picture.getFile();
		 			Object _id = Merchant.saveImage(file, picture.getFilename());
		 			extraParam.put("image", _id.toString());
		 		}
				extraParam.put("status", "Pending");
				extraParam.put("designation", "Merchant");
				extraParam.put("tempPassword", "false");
				MerchantSave saveUtils = new MerchantSave(extraParam);
				saveUtils.doSave(false);
			} catch (Exception ec) {
				ec.printStackTrace();
			}
			return redirect(routes.Application.login());
		} else {
			flash("registered", "Email id already exists");
			return redirect(routes.Application.registration());
		}
		
    }
    
    public static Result checkUserEmailAvailability(){
    	DynamicForm form = DynamicForm.form().bindFromRequest();
		String email = form.get("q");
		try{
			Merchant merchant = Merchant.find().is("email", email).next();
			return ok(Json.toJson("Email ID is in use"));
		}
		catch (Exception e) {
			// TODO: handle exception
			return ok(Json.toJson(true));
		}		
}
    
    public static Result logout() {
        session().clear();
        response().discardCookie("userid");
        flash("success", "You've been logged out");
        return redirect(routes.Application.login());
    }
    
    public static String generatePassword()
    {
    String alphaNumerics = "qwertyuiopasdfghjklzxcvbnm1234567890";
	String t = "";
	for (int i = 0; i < 6; i++) {
	    t += alphaNumerics.charAt((int) (Math.random() * alphaNumerics.length()));
	}
	return t;
    }
    
    
}
