package com.mnt.custom.module.stamp.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.audit.Audit;
import models.audit.Audit.EntityAction;
import models.audit.Audit.EntityType;
import models.candy.Candy;
import models.customer.Customer;
import models.merchant.Merchant;
import models.point.PointDetails;
import models.stamp.Stamp;
import models.visit.VisitDetails;

import org.bson.types.ObjectId;

import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.customer.stamp;

import com.core.domain.LoyaltyType;
import com.mnt.custom.module.stamp.generator.StampSave;

import controllers.Secured;
@Security.Authenticated(Secured.class)
public class Stamps extends Controller {

	public static Result index(){
		DynamicForm form = new DynamicForm().bindFromRequest();
		String customer_id = form.get("query");
		if(customer_id.equals("1"))
		{
			return ok();
		}
	    return ok(stamp.render(customer_id));
	}
	
	public static Result getVisitCandies(){
		DynamicForm form = new DynamicForm().bindFromRequest();
		Merchant merchant = Merchant.findByUserName(request().username());
		List<Candy> candies = Candy.find().
				is("merchant._id",new ObjectId(merchant.id)).
				is("loyaltyType", LoyaltyType.VISIT).toArray();
		List<CandyModel> candiesModel = new ArrayList<CandyModel>();
		
		for(Candy c: candies){
				CandyModel candyModel = new CandyModel();
				CandyModel.CustomerData customerData = new CandyModel.CustomerData();
				candyModel.customerData =customerData;
				candyModel.customerData.candy = c;
				
				for(VisitDetails visitDetails : c.visits.visit_details){
					if(visitDetails.no_of_visit != null){
						if(!visitDetails.description.equals("")){
							candyModel.customerData.noOfVisitImage +=1;
						}
					}
				}
				Stamp stamp = Stamp.findWith(form.get("customer.id"), c.id);
				if(stamp!=null){
					customerData.howMany = (long) stamp.howMany;
					customerData.isHaving = true;
				}
				candiesModel.add(candyModel);
		}
		Map map = new HashMap();
		map.put("candies", candiesModel);
		return ok(Json.toJson(map)).as("JSON");
	}
	

	
	public static Result getPointCandies(){
		DynamicForm form = new DynamicForm().bindFromRequest();
		Merchant merchant = Merchant.findByUserName(request().username());
		List<Candy> candies = Candy.find().
				is("merchant._id",new ObjectId(merchant.id)).
				is("loyaltyType", LoyaltyType.POINT).toArray();
		List<CandyModel> candiesModel = new ArrayList<CandyModel>();
		
		for(Candy c: candies){
				CandyModel candyModel = new CandyModel();
				CandyModel.CustomerData customerData = new CandyModel.CustomerData();
				candyModel.customerData =customerData;
				candyModel.customerData.candy = c;
				
				for(PointDetails pointDetails : c.points.point_details){
					if(pointDetails.point_image != null){
						if(!pointDetails.point_image.equals("")){
							candyModel.customerData.noOfPointImage +=1;
						}
					}
				}
				
				Stamp stamp = Stamp.findWith(form.get("customer.id"), c.id);
				if(stamp!=null){
					//System.out.println(c.points);
					customerData.howMany = (long) (stamp.howMany/c.points.minimum_amount_for_point);
					customerData.isHaving = true;
				}
				candiesModel.add(candyModel);
				break;
		}
		Map map = new HashMap();
		map.put("candies", candiesModel);
		return ok(Json.toJson(map)).as("JSON");
	}
	
	public static class CandyModel{
		
		public CustomerData customerData;
		
		public static class CustomerData{
			public Candy candy;
			public int noOfVisitImage = 0;
			public int noOfPointImage = 0;
			public boolean isHaving = false;
			public long howMany = 0;
		}
	}
	public static Result stampVisit() throws Exception{
		DynamicForm form = new DynamicForm().bindFromRequest();
		Candy candy = Candy.findById(new ObjectId(form.get("candy.id")));
		if(candy.valid_till.before(new Date())) {
			return badRequest("Cannot Stamp your candy !!! Candy has Expired on " + candy.valid_till);
		}
		Merchant merchant = Merchant.findByUserName(request().username());
		Stamp stamp = Stamp.findWith(form.get("customer.id"), form.get("candy.id"));
		Customer customer = Customer.findById(new org.bson.types.ObjectId(form.get("customer.id")));
		double expenditure = candy.visits.minimum_amount_for_visit * 1;
		if(stamp==null){
			Map<String, String> extraParams =  new HashMap<String, String>();
			extraParams.put("howMany", "1");
			extraParams.put("candy.id", candy.id);
			extraParams.put("customer.id", customer.id);
			StampSave stampSave = new StampSave(extraParams);
			Stamp stampSaved = (Stamp) stampSave.doSave(false);
			
			Audit audit = new Audit(candy.merchant.id,stampSaved.id,candy.id,EntityType.STAMP,EntityAction.VISIT_UPDATED,String.valueOf(1),customer.phoneNo,merchant.category);
			audit.expenditure += expenditure;
			audit.save();
			
		}else{
			stamp.howMany++;
			Audit audit = new Audit(candy.merchant.id,stamp.id,candy.id,EntityType.STAMP,EntityAction.VISIT_UPDATED,String.valueOf(1),customer.phoneNo,merchant.category);
			audit.expenditure += expenditure;
			audit.save();
			stamp.update();	
		}	
		return ok("Stamp has been marked on your candy !!! Incase you wish you apply new stamp, close stamp pop-up");
	}
	
	public static Result stampPoint() throws Exception{
		DynamicForm form = DynamicForm.form().bindFromRequest();
		Candy candy = Candy.findById(new ObjectId(form.get("candy.id")));
		if(candy.valid_till.before(new Date())) {
			return badRequest("Cannot Stamp your candy !!! Candy has Expired on " + candy.valid_till);
		}
		Merchant merchant = Merchant.findByUserName(request().username());
		double amount = Double.parseDouble(form.get("amount"));
		Stamp stamp = Stamp.findWith(form.get("customer.id"), form.get("candy.id"));
		Customer customer = Customer.findById(new org.bson.types.ObjectId(form.get("customer.id")));
		if(stamp==null){
			Map<String, String> extraParams =  new HashMap<String, String>();
			extraParams.put("howMany", String.valueOf(amount));
			extraParams.put("candy.id", candy.id);
			extraParams.put("customer.id", customer.id);
			StampSave stampSave = new StampSave(extraParams);
			Stamp stampSaved = (Stamp) stampSave.doSave(false);
			Audit audit = new Audit(candy.merchant.id,stampSaved.id,candy.id,EntityType.STAMP,EntityAction.POINT_UPDATED,String.valueOf(amount),customer.phoneNo,merchant.category);
			audit.expenditure += amount;
			audit.save();
			
		}else{
			stamp.howMany = (long) (stamp.howMany + amount);
			Audit audit = new Audit(candy.merchant.id,stamp.id,candy.id,EntityType.STAMP,EntityAction.POINT_UPDATED,String.valueOf(amount),customer.phoneNo,merchant.category);
			audit.expenditure += amount;
			audit.save();
			stamp.update();	
		}	
		return getPointCandies();
		
	}
	
	public static Result redeemPoints() throws Exception{
		DynamicForm form = DynamicForm.form().bindFromRequest();
		Candy candy = Candy.findById(new ObjectId(form.get("candy.id")));
		if(candy.valid_till.before(new Date())) {
			return badRequest("Cannot Stamp your candy !!! Candy has Expired on " + candy.valid_till);
		}
		
		double amount = Double.parseDouble(form.get("amount"));
		Stamp stamp = Stamp.findWith(form.get("customer.id"), form.get("candy.id"));
		
		stamp.howMany = (long) (stamp.howMany + amount);
		new Audit(candy.merchant.id,stamp.id,candy.id,EntityType.STAMP,EntityAction.CLAIMED,String.valueOf(amount)).save();
		stamp.update();	
		return getPointCandies();
		
	}
}
