package com.mnt.custom.module.candy.controllers;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import models.audit.Audit;
import models.audit.Audit.EntityAction;
import models.audit.Audit.EntityType;
import models.candy.Candy;
import models.merchant.Merchant;
import models.stamp.Stamp;
import net.coobird.thumbnailator.Thumbnails;

import org.bson.types.ObjectId;

import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.mvc.Security;
import views.html.candy.customCandy;
import views.html.candy.customEditCandy;
import views.html.candy.indexCandy;

import com.core.domain.LoyaltyType;
import com.mnt.core.auth.BasicAuth;
import com.mnt.core.menu.MenuBarFixture;
import com.mnt.core.utils.ImageCropper;
import com.mnt.custom.module.candy.generator.CandiesProxyUIContext;
import com.mnt.custom.module.candy.generator.CandySave;
import com.mongodb.DBObject;

import controllers.Secured;
@Security.Authenticated(Secured.class)
@BasicAuth
public class Candies extends Controller {
	public static Result index(){		

		return ok(indexCandy.render(CandiesProxyUIContext.getInstance().build(),
				MenuBarFixture.build(request().username()),
				Merchant.findByUserName(request().username())));
	}
	
	
	public static Result create(){
		try {
	 		MultipartFormData body = request().body().asMultipartFormData();
	 		MultipartFormData.FilePart picture = body.getFile("image");
	 		File file = picture.getFile();
	 		Object _id = Candy.saveImage(file, picture.getFilename());
 	       	Map<String,String> extraParam = new HashMap<String,String>();
 	        extraParam.put("image", _id.toString());
 	        extraParam.put("merchant.id", request().username());
 	        
			CandySave saveUtils = new CandySave(extraParam);
			Candy candy = (Candy)saveUtils.doSave(false);
			Merchant m = Merchant.findByUserName(request().username());
			DBObject c = Candy.getDBObjById(new ObjectId(candy.id));
			m.updateCandy(c);
		} catch (Exception e) {
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
		return ok();
	}
	
	public static Result createCandy(){
		
		Merchant merchant = Merchant.findByUserName(request().username());
		List<Candy> candies = Candy.find().is("merchant._id",new ObjectId(merchant.id)).is("loyaltyType", LoyaltyType.POINT).toArray();
		return ok(customCandy.render(candies.size()>0));
	}
	
	private static Object processImage(MultipartFormData.FilePart picture) throws Exception {
		File file = picture.getFile();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Thumbnails.of(file).size(100, 100).keepAspectRatio(false).toOutputStream(baos);
 		return Candy.saveImage(baos, picture.getFilename());
	}
	
	public static Result saveCandy(){
	   try {
		    DynamicForm form = DynamicForm.form().bindFromRequest();
		    String loyaltyType = form.get("loyaltyType");
		   	
		    Map<String,String> extraParam = new HashMap<String,String>();
			MultipartFormData body = request().body().asMultipartFormData();
			
			if(loyaltyType.equals("VISIT")) {
				extraParam.put("points", null);
				
		 		MultipartFormData.FilePart picture0 = body.getFile("visit_image[0]");
		 		MultipartFormData.FilePart picture1 = body.getFile("visit_image[1]");
		 		MultipartFormData.FilePart picture2 = body.getFile("visit_image[2]");
		 		MultipartFormData.FilePart picture3 = body.getFile("visit_image[3]");
		 		
		 		if(picture0 != null && picture0.getContentType().startsWith("image")){
			 		Object _id = processImage(picture0);
				    extraParam.put("visits.visit_details[0].visit_image", _id.toString());
		 		}
		 		
		 		if(picture1 != null && picture1.getContentType().startsWith("image")){
		 			Object _id = processImage(picture1);	
				    extraParam.put("visits.visit_details[1].visit_image", _id.toString());
		 		}
		 		
		 		if(picture2 != null && picture2.getContentType().startsWith("image")){
		 			Object _id = processImage(picture2);	
				    extraParam.put("visits.visit_details[2].visit_image", _id.toString());
		 		}
		 		
		 		if(picture3 != null && picture3.getContentType().startsWith("image")){
		 			Object _id = processImage(picture3);	
				    extraParam.put("visits.visit_details[3].visit_image", _id.toString());
		 		}
			} else {
				extraParam.put("visits", null);
				
				MultipartFormData.FilePart picture0 = body.getFile("points_image[0]");
		 		MultipartFormData.FilePart picture1 = body.getFile("points_image[1]");
		 		MultipartFormData.FilePart picture2 = body.getFile("points_image[2]");
		 		MultipartFormData.FilePart picture3 = body.getFile("points_image[3]");
		 		
		 		//picture 0 
		 		if(picture0 != null && picture0.getContentType().startsWith("image")){
		 			Object _id = processImage(picture0);	
				    extraParam.put("points.point_details[0].point_image", _id.toString());
		 		}
		 		
		 		if(picture1 != null && picture1.getContentType().startsWith("image")){
		 			Object _id = processImage(picture1);	
				    extraParam.put("points.point_details[1].point_image", _id.toString());
		 		}
		 		
		 		if(picture2 != null && picture2.getContentType().startsWith("image")){
		 			Object _id = processImage(picture2);	
				    extraParam.put("points.point_details[2].point_image", _id.toString());
		 		}
		 		
		 		if(picture3 != null && picture3.getContentType().startsWith("image")){
		 			Object _id = processImage(picture3);	
				    extraParam.put("points.point_details[3].point_image", _id.toString());
		 		}
			}
			Merchant merchant = Merchant.findByUserName(request().username());
			extraParam.put("merchant.id", merchant.id);
	    	CandySave saveUtils = new CandySave(extraParam);
	    	Candy candy = (Candy)saveUtils.doSave(false);
	    	
			DBObject c = Candy.getDBObjById(new ObjectId(candy.id));
			merchant.updateCandy(c);
	   } catch (Exception e) {
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
		return redirect(routes.Candies.index());
	}
	
	public static Result editCandyPopup(){
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String id = form.get("query");
		ObjectId objectId = new ObjectId(id);
		Candy candy = Candy.findById(objectId);
		return ok(customEditCandy.render(candy));
	}
	
	public static Result editCandy(){
		DynamicForm form = DynamicForm.form().bindFromRequest();
	    
		String o_id = form.get("id");
		ObjectId objectId = new ObjectId(o_id);
		Candy candy = Candy.findById(objectId);
		
		String loyaltyType = candy.loyaltyType.name();
	    Map<String,String> extraParam = new HashMap<String,String>();
		MultipartFormData body = request().body().asMultipartFormData();
		try{
			if(loyaltyType.equals("VISIT")){
				extraParam.put("points", null);
				
				String image0_id = form.get("image_0_id");
			    String image1_id = form.get("image_1_id");
			    String image2_id = form.get("image_2_id");
			    String image3_id = form.get("image_3_id");
			    
				MultipartFormData.FilePart picture0 = body.getFile("visit_image[0]");
		 		MultipartFormData.FilePart picture1 = body.getFile("visit_image[1]");
		 		MultipartFormData.FilePart picture2 = body.getFile("visit_image[2]");
		 		MultipartFormData.FilePart picture3 = body.getFile("visit_image[3]");
		 		
		 		extraParam.put("visits.visit_details[0].visit_image", candy.visits.visit_details.get(0).visit_image);
		 		extraParam.put("visits.visit_details[1].visit_image", candy.visits.visit_details.get(1).visit_image);
		 		extraParam.put("visits.visit_details[2].visit_image", candy.visits.visit_details.get(2).visit_image);
		 		extraParam.put("visits.visit_details[3].visit_image", candy.visits.visit_details.get(3).visit_image);
		 		
		 		if( picture0!= null && picture0.getContentType().startsWith("image")){
			 			if(image0_id.equals("null")){
			 				if(candy.visits.visit_details.get(0).visit_image != null)
			 					Candy.deleteImageById(candy.visits.visit_details.get(0).visit_image);
			 			}
			 			Object _id = processImage(picture0);
					    extraParam.put("visits.visit_details[0].visit_image", _id.toString());
		 		}
		 		
		 		if(picture1 != null && picture1.getContentType().startsWith("image")){
			 			if(image1_id.equals("null")){
			 				if(candy.visits.visit_details.get(1).visit_image != null)
			 					Candy.deleteImageById(candy.visits.visit_details.get(1).visit_image);
			 				
			 			}
				 		
						Object _id = processImage(picture1);
					    extraParam.put("visits.visit_details[1].visit_image", _id.toString());
		 		}
		 		
		 		if(picture2 != null && picture2.getContentType().startsWith("image")){
		 			
			 			if(image2_id.equals("null")){
			 				if(candy.visits.visit_details.get(2).visit_image != null)
			 					Candy.deleteImageById(candy.visits.visit_details.get(2).visit_image);
			 			}
						Object _id = processImage(picture2);
					    extraParam.put("visits.visit_details[2].visit_image", _id.toString());
		 		}
		 		
		 		if(picture3 != null && picture3.getContentType().startsWith("image")){
			 			if(image3_id.equals("null")){
			 				if(candy.visits.visit_details.get(3).visit_image != null)
			 					Candy.deleteImageById(candy.visits.visit_details.get(3).visit_image);
			 			}
						Object _id = processImage(picture3);
					    extraParam.put("visits.visit_details[3].visit_image", _id.toString());
					    
		 		}
			}
			
			//if loyaltyType="POINT"
			else{
				extraParam.put("visits", null);
				
				String image0_id = form.get("p_image_0_id");
			    String image1_id = form.get("p_image_1_id");
			    String image2_id = form.get("p_image_2_id");
			    String image3_id = form.get("p_image_3_id");
			    
				MultipartFormData.FilePart picture0 = body.getFile("points_image[0]");
		 		MultipartFormData.FilePart picture1 = body.getFile("points_image[1]");
		 		MultipartFormData.FilePart picture2 = body.getFile("points_image[2]");
		 		MultipartFormData.FilePart picture3 = body.getFile("points_image[3]");
		 		
		 		extraParam.put("points.point_details[0].point_image", candy.points.point_details.get(0).point_image);
		 		extraParam.put("points.point_details[1].point_image", candy.points.point_details.get(1).point_image);
		 		extraParam.put("points.point_details[2].point_image", candy.points.point_details.get(2).point_image);
		 		extraParam.put("points.point_details[3].point_image", candy.points.point_details.get(3).point_image);
		 		//picture 0 
		 		if(picture0 != null && picture0.getContentType().startsWith("image")){
		 			if(image0_id.equals("null")){
		 				if(candy.points.point_details.get(0).point_image != null)
		 					Candy.deleteImageById(candy.points.point_details.get(0).point_image);
		 			}
		 			Object _id = processImage(picture0);
				    extraParam.put("points.point_details[0].point_image", _id.toString());
		 		}
		 		
		 		if(picture1 != null && picture1.getContentType().startsWith("image")){
		 			if(image1_id.equals("null")){
		 				if(candy.points.point_details.get(1).point_image != null)
		 					Candy.deleteImageById(candy.points.point_details.get(1).point_image);
		 			}
		 			Object _id = processImage(picture1);
		 			extraParam.put("points.point_details[1].point_image", _id.toString());
		 		}
		 		
		 		if(picture2 != null && picture2.getContentType().startsWith("image")){
		 			if(image2_id.equals("null")){
		 				if(candy.points.point_details.get(2).point_image != null)
		 					Candy.deleteImageById(candy.points.point_details.get(2).point_image);
		 			}
		 			Object _id = processImage(picture2);
		 			extraParam.put("points.point_details[2].point_image", _id.toString());
		 		}
		 		
		 		if(picture3 != null && picture3.getContentType().startsWith("image")){
		 			if(image3_id.equals("null")){
		 				if(candy.points.point_details.get(3).point_image != null)
		 					Candy.deleteImageById(candy.points.point_details.get(3).point_image);
		 			}
		 			Object _id = processImage(picture3);
		 			extraParam.put("points.point_details[3].point_image", _id.toString());
		 		}
			}
			Merchant merchant = Merchant.findByUserName(request().username());
			extraParam.put("merchant.id", merchant.id);
			extraParam.put("loyaltyType", loyaltyType);
			CandySave saveUtils = new CandySave(extraParam);
			Candy candy1 = (Candy)saveUtils.doSave(true);
			DBObject c = Candy.getDBObjById(new ObjectId(candy1.id));
			merchant.updateCandy(c);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
		return redirect(routes.Candies.index());
	}
		
	
	public static Result getCandyImage(String image) throws IOException{
		return ok(Candy.getImage(image)).as(("picture/stream"));
	}
	
	public static Result getCandyGrayImage(String image) throws IOException{
		InputStream inputStream = Candy.getImage(image);
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		BufferedImage grayImage = ImageCropper.avg(bufferedImage);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(grayImage, "jpg", baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return ok(is).as(("picture/stream"));
	}

	public static Result redeemYourCoupon(){
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String candyId = form.get("candyId");
		String custId = form.get("customerId");
		Merchant merchant = Merchant.findByUserName(request().username());
		long milestone = Long.parseLong(form.get("milestone"));
		Stamp stamp = Stamp.findWith(custId, candyId);
		if(stamp!=null){
			stamp.howMany = stamp.howMany - milestone;
			stamp.update();
			new Audit(merchant.id,stamp.id,candyId,EntityType.STAMP,EntityAction.CLAIMED,"").save();
			
		}
		return ok("You have Redeemed your candy!!");
	}
	public static Result checkCandyCode(){
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String candyCode = form.get("q");
		Merchant merchant = Merchant.findByUserName(request().username());
		Candy candy=null;
		if(merchant != null){
			candy = Candy.findUniqueCandyCode(candyCode,merchant.id);
		}
		if(candy != null){
			return ok(Json.toJson(false));
		}
		return ok(Json.toJson(true));
	}
	
	
	public static Result checkExpiredDate(){
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String candyCode = form.get("q");
		System.out.println("candyCode ="+candyCode);
		return ok(Json.toJson(false));
	}
	
	public static Result search() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		form.data().put("email", request().username());
		return ok(Json.toJson(CandiesProxyUIContext.getInstance().build().doSearch(form)));
    }
}
