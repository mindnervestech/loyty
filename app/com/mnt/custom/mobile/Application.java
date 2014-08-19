package com.mnt.custom.mobile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import models.Consumer.Consumer;
import models.Consumer.RegistrationFailedUserNameTakenException;
import models.audit.Audit;
import models.audit.Audit.EntityAction;
import models.audit.Audit.EntityType;
import models.candy.Candy;
import models.customer.Customer;
import models.merchant.Merchant;
import models.point.PointDetails;
import models.stamp.Stamp;
import models.visit.VisitDetails;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBQuery.Query;

import org.bson.types.ObjectId;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.core.domain.LoyaltyType;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mnt.core.auth.EDHelper;
import com.mnt.custom.domain.Category;
import com.mnt.custom.domain.City;
import com.mnt.custom.mobile.data.Candiez;
import com.mnt.custom.mobile.data.MessageWrapper;
import com.mnt.custom.mobile.data.MessageWrapper.Code;
import com.mnt.custom.mobile.data.Point;
import com.mnt.custom.module.customer.generator.CustomerSave;
import com.mnt.custom.module.stamp.generator.StampSave;

public class Application extends Controller{
	
	public static Result cities(String country, String state) {
		
		List<CityModel> cities = new ArrayList<Application.CityModel>();
		for(City _c : City.values()) {
			cities.add(new CityModel( _c.name(), _c.getName()));
		}
		play.mvc.Http.Request _r =  request();
		String agent = _r.getHeader("User-Agent");
		if(agent.contains("Mozilla")) { 
			
			return ok(Json.toJson(cities)).as("application/json");
		} else {
			Map<String,List<CityModel>> _map = new HashMap<String, List<CityModel>>();
			_map.put("cities", cities);
			return ok(Json.toJson(_map)).as("application/json");

		}
	}
	
	static class CityModel  {
		public String value;
		public String text;
		
		public CityModel(String value, String text) {
			this.value = value;
			this.text = text;
		}
	}
	


	
	public static Result categories(String city) {
		
		Set<Category> availableCategories = new HashSet<Category>();
		List<Merchant> merchants = Merchant.find().is("city", city).toArray();
		for(Merchant _m : merchants) {
			if(_m.category != null)
				availableCategories.add(_m.category);
		}
		
		List<com.mnt.custom.mobile.data.Category> categories  = new ArrayList<com.mnt.custom.mobile.data.Category>();
		for(Category cdomian :availableCategories) {
			categories.add(new com.mnt.custom.mobile.data.Category(cdomian.forName, cdomian.imageUrl, cdomian.toString()));
		}
		play.mvc.Http.Request _r =  request();
		String agent = _r.getHeader("User-Agent");
		if(agent.contains("Mozilla")) { 
			
			return ok(Json.toJson(categories)).as("application/json");
		} else {
			Map<String,List<com.mnt.custom.mobile.data.Category>> _map = new HashMap<String, List<com.mnt.custom.mobile.data.Category>>();
			_map.put("categories", categories);
			return ok(Json.toJson(_map)).as("application/json");

		}
		
	}
	
	
	
	public static Result getCandyImage(String image) throws IOException{
		return ok(Candy.getImage(image)).as(("image/png"));
	}
	
	public static Result getMerchantImage(String image) throws IOException{
		return ok(Merchant.getImage(image)).as(("image/png"));
	}
	
	public static Result register(String phone) {
		if (Long.parseLong(phone) < 1000000000l || Long.parseLong(phone) > 9999999999l) {
			return ok("invalid number");
		}
		//int random = (int) (1000 + (Math.random() * 9000));
		int random = 9999;
		sendSMS();
		String returnStr = "{\"register\":{\"phone\":\"" + phone + "\",\"pin\":\"" + random +"\"}}";
		return ok(returnStr).as("application/json");
	}
	
	private static void sendSMS(){
		
	}
	
	public static Result consumerRegisteration() {
		
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String username = form.get("username");
		String password = form.get("password"); 
		String phoneNo = form.get("phoneNo");
		
		try {
		Consumer.ifUserNameNotPresent(username).then().register(password, phoneNo);
		} catch (RegistrationFailedUserNameTakenException exception) {
			return ok("{\"error\" :\"2001\",\"message\":\"Username Present\"}").as("application/json");
		}
		
		return ok("{\"success\" :\"200\",\"message\":\"Registration Successfull\"}").as("application/json");
	}
	
	/*public static Result consumerLogin(String username, String password, String phoneNo) {
		
	}*/
	
	public static Result merchantsInProximity(Double lon, Double lat, Double  meters) {
		List<Merchant> dbMerchants = Merchant.merchantsInProximity(new double[]{lon,lat}, meters/111120);
		return buildMerchantsResult(dbMerchants);
	}
	
	public static Result myStamps(String phone) {
		DBCursor<Customer> _q = Customer.find().is("phoneNo", phone);
		List<com.mnt.custom.mobile.data.Candiez> candiezData = Lists.newArrayList();
		for(Customer _c : _q) {
				List<Candy> candies = Candy.find().
						is("merchant._id",new ObjectId(_c.merchant.id))
						.toArray();
				Merchant _m = Merchant.findById(_c.merchant.id);
				for(Candy c: candies){
					com.mnt.custom.mobile.data.Candiez candiez = convertDBCandyToViewModel(c);
					Stamp stamp = Stamp.findWith(_c.id,c.id);
					if(stamp != null) {
						if("POINT".equals(candiez.programType)) {
							candiez.howMany = stamp.howMany/c.points.minimum_amount_for_point;
						} else {
							candiez.howMany = stamp.howMany;
						}
					}
					candiezData.add(candiez.setMerchantInformation(_m.businessName, _m.locality, _m.image, _m.position));
				}
		}
		Map<String,Object> _map = new HashMap<String, Object>();
		_map.put("candies", candiezData);
		_map.put("androidversion", 1);
		return ok(Json.toJson(_map)).as("application/json");
	}
	
	// My Merchants
	public static Result getCustomerRegisteredMerchants(String phone) {
		
		play.mvc.Http.Request _r =  request();
		String agent = _r.getHeader("User-Agent");
		List<com.mnt.custom.mobile.data.Merchant> merchants = new ArrayList<com.mnt.custom.mobile.data.Merchant>();
		DBCursor<Customer> _q = Customer.find().is("phoneNo", phone);
		while (_q.hasNext()) {
			Customer customer = _q.next();
			com.mnt.custom.mobile.data.Merchant merchantData = convertModelToViewModel(customer.merchant.findMe());
			merchants.add(merchantData);
		}
		if(agent.contains("Mozilla")) { 
			
			return ok(Json.toJson(merchants)).as("application/json");
		} else {
			Map<String,List<com.mnt.custom.mobile.data.Merchant>> _map = new HashMap<String, List<com.mnt.custom.mobile.data.Merchant>>();
			_map.put("merchants", merchants);
			return ok(Json.toJson(_map)).as("application/json");

		}
	}
	
	// My Stamp
	public static Result getCandiezInformationForCustomer(String programID, String phone){
		Candy candy = Candy.findById(new ObjectId(programID));
		List<com.mnt.custom.mobile.data.Candiez> candiezData = Lists.newArrayList();
		List<Customer> customers = Customer.find().is("merchant._id",new ObjectId(candy.merchant.id)).is("phoneNo", phone).toArray();
		Stamp stamp = Stamp.findWith(customers.get(0).id,candy.id);
		com.mnt.custom.mobile.data.Candiez candiez = new Candiez();
		candiez.programId = candy.id;
		candiez.programDescription = candy.name;
		candiez.validTill = candy.valid_till;
		candiez.programType = candy.loyaltyType.name().toLowerCase();
		candiez.stamps = new ArrayList<com.mnt.custom.mobile.data.Stamp>();
		if(candy.loyaltyType == LoyaltyType.VISIT ){
			List<com.mnt.custom.mobile.data.Stamp> stamps = Lists.newArrayList();
			candiez.stamps = new ArrayList<com.mnt.custom.mobile.data.Stamp>();
			int stampCount = 1;
			int prevVisitCount = 0;
			for(int vIndex = 0 ; vIndex < candy.visits.visit_details.size() ; vIndex++) {
				VisitDetails visitDetails = candy.visits.visit_details.get(vIndex);
				if(visitDetails == null || visitDetails.no_of_visit == null) {
					continue;
				}
				candiez.amount = candy.visits.minimum_amount_for_visit + "";
				candiez.howMany = (stamp==null) ? 0 : stamp.howMany;
				candiez.total = visitDetails.no_of_visit;
				candiez.candiez.add(new com.mnt.custom.mobile.data.Candy(visitDetails.id,visitDetails.description,visitDetails.no_of_visit,visitDetails.visit_image,""));
				int canBeStampedIndex = 1;
				if(stamp != null) {
					canBeStampedIndex = (int) stamp.howMany + 1;
				}
				for(int i = prevVisitCount; i < visitDetails.no_of_visit ; i++ ) {
					boolean isStamped =  stamp != null && (stampCount <= stamp.howMany)  ? true : false;
					String imageUrl = "";
					boolean isMileStone = false;
					boolean canBeStamped = stampCount == canBeStampedIndex?true:false; 
					if(i == visitDetails.no_of_visit - 1 ) {
						imageUrl = visitDetails.visit_image;
						isMileStone = true;
					}
					stamps.add(new com.mnt.custom.mobile.data.Stamp(isStamped, stampCount, "", "",isMileStone,canBeStamped));
					if(isMileStone) {
						if(isStamped) {
							stamps.add(new com.mnt.custom.mobile.data.Stamp(isStamped, stampCount, imageUrl, "",isMileStone,true));
						} else {
							stamps.add(new com.mnt.custom.mobile.data.Stamp(isStamped, stampCount, imageUrl, "",isMileStone,false));
						}
					}
					stampCount++;
				}
				prevVisitCount = visitDetails.no_of_visit;
			}
			candiez.stamps = stamps;
		}
		if(candy.loyaltyType == LoyaltyType.POINT ){
			candiez.points = new ArrayList<com.mnt.custom.mobile.data.Point>();
			if(candy.points !=null) {
				int prevPoints = 0;
				for(PointDetails pointDetails : candy.points.point_details){
					if(pointDetails == null || pointDetails.no_of_point == null) {
						continue;
					}
						candiez.programType = "POINT";
						candiez.amount = candy.points.minimum_amount_for_point + "";
						candiez.total = pointDetails.no_of_point;
						candiez.howMany = (stamp==null) ? 0 : (stamp.howMany / candy.points.minimum_amount_for_point);
						candiez.candiez.add(new com.mnt.custom.mobile.data.Candy(pointDetails.id,pointDetails.description,pointDetails.no_of_point,pointDetails.point_image,""));
						if(pointDetails.no_of_point  > candiez.howMany) {
							float precentageProgress = (float) (100 * (pointDetails.no_of_point - candiez.howMany) / (pointDetails.no_of_point - prevPoints));
							candiez.points.add(new Point(pointDetails.no_of_point, pointDetails.point_image, pointDetails.description, false,precentageProgress));
						} else {
							candiez.points.add(new Point(pointDetails.no_of_point, pointDetails.point_image, pointDetails.description, true,100.0f));
						}
					prevPoints = pointDetails.no_of_point;	
					
				}
			}
		}
		candiezData.add(candiez);
		play.mvc.Http.Request _r =  request();
		String agent = _r.getHeader("User-Agent");
		
		if(agent.contains("Mozilla")) {
			return ok(Json.toJson(candiezData)).as("application/json");
		} else {
			Map<String,List<com.mnt.custom.mobile.data.Candiez>> _map = new HashMap<String, List<Candiez>>();
			_map.put("programs", candiezData);
			return ok(Json.toJson(_map)).as("application/json");
		}
	}
	
	public static Result customerRegistration() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String merchantID = form.get("merchantID");
		String phone = form.get("phoneNo");
		List<Customer> customers = Customer.find().is("merchant._id",new ObjectId(merchantID)).is("phoneNo", phone).toArray();
		if(customers.size() == 0 ) {
			try {
				Map<String,String> extraParam = new HashMap<String, String>();
				extraParam.put("merchant.id", merchantID);
				extraParam.put("phoneNo", phone);
				CustomerSave saveUtils = new CustomerSave(extraParam);
				saveUtils.doSave(false);
				} catch (Exception e) {
					e.printStackTrace();
					return internalServerError();
				}
		}
		
		
		return getMerchantProgramForCustomer(merchantID, phone);
	}
	
	// My Merchant > Tab Merchant
	public static Result getMerchantProgramForCustomer(String merchantID, String phone) {
		List<Customer> customers = Customer.find().is("merchant._id",new ObjectId(merchantID)).is("phoneNo", phone).toArray();
		
		if(customers.size() == 0 ) {
			String returnStr = "{\"status\":\"false\"}";
			return ok(returnStr).as("application/json");
		}
		
		
		List<com.mnt.custom.mobile.data.Candiez> candiezData = Lists.newArrayList();
		
		List<Candy> candies = Candy.find().
				is("merchant._id",new ObjectId(merchantID))
				.toArray();
		
		for(Candy _c : candies) {
			com.mnt.custom.mobile.data.Candiez candiez = new Candiez();
			candiez.programId = _c.id;
			candiez.programDescription = _c.name;
			candiez.validTill = _c.valid_till;
			candiez.programType = _c.loyaltyType.name().toLowerCase();
			Stamp stamp = Stamp.findWith(customers.get(0).id,_c.id);
			if(_c.loyaltyType == LoyaltyType.VISIT ){
				List<com.mnt.custom.mobile.data.Stamp> stamps = Lists.newArrayList();
				candiez.stamps = new ArrayList<com.mnt.custom.mobile.data.Stamp>();
				for(int vIndex = 0 ; vIndex < _c.visits.visit_details.size() ; vIndex++) {
					VisitDetails visitDetails = _c.visits.visit_details.get(vIndex);
					candiez.amount = _c.visits.minimum_amount_for_visit + "";
					candiez.howMany = (stamp==null) ? 0 : stamp.howMany;
					candiez.total = visitDetails.no_of_visit; 
					candiez.candiez.add(new com.mnt.custom.mobile.data.Candy(visitDetails.id,visitDetails.description,visitDetails.no_of_visit,visitDetails.visit_image,""));
				}
			} 
			if(_c.loyaltyType == LoyaltyType.POINT ){
				candiez.points = new ArrayList<com.mnt.custom.mobile.data.Point>();
				if(_c.points !=null) {
					for(PointDetails pointDetails : _c.points.point_details){
						if(pointDetails.point_image != null){
							candiez.programType = "POINT";
							candiez.amount = _c.points.minimum_amount_for_point + "";
							candiez.howMany = (stamp==null) ? 0 : (stamp.howMany / _c.points.minimum_amount_for_point);
							candiez.total = pointDetails.no_of_point; 
							candiez.candiez.add(new com.mnt.custom.mobile.data.Candy(pointDetails.id,pointDetails.description,pointDetails.no_of_point,pointDetails.point_image,""));
						}
					}
				}
			}
			candiezData.add(candiez);
		}
		
		play.mvc.Http.Request _r =  request();
		String agent = _r.getHeader("User-Agent");
		
		if(agent.contains("Mozilla")) {
			return ok(Json.toJson(candiezData)).as("application/json");
		} else {
			Map<String,List<com.mnt.custom.mobile.data.Candiez>> _map = new HashMap<String, List<Candiez>>();
			_map.put("programs", candiezData);
			return ok(Json.toJson(_map)).as("application/json");
		}
		
	}

	public static class stampOnRequest {
		public String programID;
		public String type;
		public long visit;
		public long point;
		public String merchantCode;
		public String phone;
		public String amount;
	}
	/*
	 * Response: programId, amount (if applicable), phone, merchantCode
	 */
	public static Result stampOn(){
		
		Form<stampOnRequest> form = DynamicForm.form(stampOnRequest.class).bindFromRequest();
		Candy candy = Candy.findById(new ObjectId(form.get().programID));
		Merchant merchant = Merchant.findById(new ObjectId(candy.merchant.id));
		if(!form.get().merchantCode.equals(merchant.code)){
			 return badRequest(Json.toJson(new MessageWrapper(Code.MERCHANTCODE_NOT_MATCH, "Mechant Code does not match"))).as("application/json");
		}
				
		List<Customer> customers = Customer.find().is("merchant._id",new ObjectId(merchant.id))
												  .is("phoneNo", form.get().phone)
												  .toArray();
		
		if(candy.valid_till.before(new Date())) {
			return badRequest(Json.toJson(new MessageWrapper(Code.STAMP_EXPIRED, "Cannot Stamp your candy !!! Candy has Expired on " + candy.valid_till))).as("application/json");
		}
		
		if(candy.loyaltyType == LoyaltyType.VISIT) {
			Stamp stamp = Stamp.findWith(customers.get(0).id, form.get().programID);
			if(stamp==null){
				Map<String, String> extraParams =  new HashMap<String, String>();
				extraParams.put("howMany", "1");
				extraParams.put("candy.id", candy.id);
				extraParams.put("customer.id", customers.get(0).id);
				
				StampSave stampSave = new StampSave(extraParams);
				try {
					Stamp stampSaved = (Stamp) stampSave.doSave(false);
					new Audit(candy.merchant.id,stampSaved.id, candy.id, EntityType.STAMP,EntityAction.POINT_UPDATED,String.valueOf(1)).save();
				} catch (Exception e) {
					return badRequest(Json.toJson(new MessageWrapper(Code.CANNOT_STAMP_VISIT, "Cannot Stamp your candy !!!"))).as("application/json");
				}
			}else{
				//if(visit-1 == stamp.howMany){ No tested code.
					stamp.howMany++;
					new Audit(merchant.id,stamp.id, candy.id, EntityType.STAMP,EntityAction.VISIT_UPDATED,String.valueOf(1)).save();
					stamp.update();	
				//}
			}
		}
		
		if(candy.loyaltyType == LoyaltyType.POINT) {
			double amount = Double.parseDouble(form.get().amount);
			Stamp stamp = Stamp.findWith(customers.get(0).id, form.get().programID);
			
			if(stamp==null){
				Map<String, String> extraParams =  new HashMap<String, String>();
				extraParams.put("howMany", String.valueOf(amount));
				extraParams.put("candy.id", candy.id);
				extraParams.put("customer.id", customers.get(0).id);
				
				StampSave stampSave = new StampSave(extraParams);
				try {
					Stamp stampSaved = (Stamp) stampSave.doSave(false);
					new Audit(candy.merchant.id,stampSaved.id, candy.id, EntityType.STAMP,EntityAction.POINT_UPDATED,String.valueOf(amount)).save();
				} catch (Exception e) {
					return badRequest(Json.toJson(new MessageWrapper(Code.CANNOT_MARK_POINT, "Cannot Stamp your candy !!!"))).as("application/json");
				}
			}else{
				stamp.howMany = (long) (stamp.howMany + amount);
				new Audit(merchant.id,stamp.id, candy.id, EntityType.STAMP,EntityAction.VISIT_UPDATED,String.valueOf(amount)).save();
				
				stamp.update();	
			}
		}
		return ok(Json.toJson(new MessageWrapper(Code.SUCCESS, "Congrats!!!"))).as("application/json");
	}
	
	public static Result redeemCoupon(){
		Form<stampOnRequest> form = DynamicForm.form(stampOnRequest.class).bindFromRequest();
		
		Candy candy = Candy.findById(new ObjectId(form.get().programID));
		Merchant merchant = Merchant.findById(new ObjectId(candy.merchant.id));
		if(!form.get().merchantCode.equals(merchant.code)){
			//TODO: return badRequest(Json.toJson(new MessageWrapper(Code.MERCHANTCODE_NOT_MATCH, "Mechant Code does not match"))).as("application/json");
		}
				
		List<Customer> customers = Customer.find().is("merchant._id",new ObjectId(merchant.id))
												  .is("phoneNo", form.get().phone)
												  .toArray();
		
		if(candy.valid_till.before(new Date())) {
			return badRequest(Json.toJson(new MessageWrapper(Code.STAMP_EXPIRED, "Cannot Stamp your candy !!! Candy has Expired on " + candy.valid_till))).as("application/json");
		}
		Stamp stamp = Stamp.findWith(customers.get(0).id, form.get().programID);
		
		long visit = (form.get().visit);
		long point = (form.get().point);
		
		if(candy.loyaltyType == LoyaltyType.POINT) {
			if(stamp.howMany >= point * candy.points.minimum_amount_for_point)
				stamp.howMany = stamp.howMany - (point * candy.points.minimum_amount_for_point);
		}
		
		if(candy.loyaltyType == LoyaltyType.VISIT) {
			if(stamp.howMany >= visit)
				stamp.howMany = stamp.howMany - visit;
		}
		new Audit(merchant.id,stamp.id,candy.id,EntityType.STAMP,EntityAction.CLAIMED,"").save();
		stamp.update();
		return ok(Json.toJson(new MessageWrapper(Code.SUCCESS, "Congrats!!!"))).as("application/json");
	}
	
	
	private static com.mnt.custom.mobile.data.Merchant convertModelToViewModel(Merchant _m) {
		return  new com.mnt.custom.mobile.data.Merchant(_m.businessName,
				_m.locality,
				_m.state,
				_m.city.name(),
				_m.pin,
				_m.phoneNo,
				_m.id,
				_m.image);
	}
	
	
	private static List<com.mnt.custom.mobile.data.Candiez> candiez(String merchantID, StringBuilder information) {
		
		List<Candy> candies = Candy.find().
				is("merchant._id",new ObjectId(merchantID))
				.toArray();
		List<com.mnt.custom.mobile.data.Candiez> candiezData = Lists.newArrayList();
		
		for(Candy c: candies){
			com.mnt.custom.mobile.data.Candiez candiez = convertDBCandyToViewModel(c);
			candiezData.add(candiez);
		}
		
		return candiezData;
	}
	
	public static Result candiez() {
		
		DynamicForm form  = new DynamicForm().bindFromRequest();
		
		List<Candy> candies = Candy.find().
				is("merchant._id",new ObjectId(form.data().get("merchant")))
				.toArray();
		List<com.mnt.custom.mobile.data.Candiez> candiezData = Lists.newArrayList();
		StringBuilder builder = new StringBuilder();
		for(Candy c: candies){
			com.mnt.custom.mobile.data.Candiez candiez = convertDBCandyToViewModel(c);
			candiezData.add(candiez);
		}
		play.mvc.Http.Request _r =  request();
		String agent = _r.getHeader("User-Agent");
		
		if(agent.contains("Mozilla")) {
			return ok(Json.toJson(candiezData)).as("application/json");
		} else {
			Map<String,List<com.mnt.custom.mobile.data.Candiez>> _map = new HashMap<String, List<com.mnt.custom.mobile.data.Candiez>>();
			_map.put("candies", candiezData);
			return ok(Json.toJson(_map)).as("application/json");
		}
	}
	
	

	private static com.mnt.custom.mobile.data.Candiez convertDBCandyToViewModel(
			Candy c) {
		com.mnt.custom.mobile.data.Candiez candiez = new Candiez();
		candiez.programId = c.id;
		candiez.programDescription = c.name;
		candiez.validTill = c.valid_till;
		//information.append(c.name).append("  ");
		candiez.candiez = new ArrayList<com.mnt.custom.mobile.data.Candy>();
		if(c.visits !=null) {
			for(VisitDetails visitDetails : c.visits.visit_details){
				if(visitDetails.no_of_visit != null){
					candiez.programType = "VISIT";
					candiez.amount = c.visits.minimum_amount_for_visit + "";
					candiez.candiez.add(new com.mnt.custom.mobile.data.Candy(visitDetails.id,visitDetails.description,visitDetails.no_of_visit,visitDetails.visit_image,""));
					//information.append("  |" + visitDetails.description + " at " + visitDetails.no_of_visit + " visit | ");
				}
				
			}
		}
		
		if(c.points !=null) {
			for(PointDetails pointDetails : c.points.point_details){
				if(pointDetails.point_image != null){
					candiez.programType = "POINT";
					candiez.amount = c.points.minimum_amount_for_point + "";
					candiez.candiez.add(new com.mnt.custom.mobile.data.Candy(pointDetails.id,pointDetails.description,pointDetails.no_of_point,pointDetails.point_image,""));
					//information.append("  |" + pointDetails.description + " on " + pointDetails.no_of_point + " points | ");
				}
			}
		}
		return candiez;
	}
	
	public static Result candiezByCityAndCategory() {
		List<Merchant> merchants= null;
		DynamicForm form  = new DynamicForm().bindFromRequest();
		String city = form.get("city");
		String category = form.get("category");
		merchants = getMerchantByCityAndCategory(city, category);
		
		return buildMerchantsResult(merchants);
	}



	private static Result buildMerchantsResult(List<Merchant> merchants) {
		List<com.mnt.custom.mobile.data.Candiez> candiezData = Lists.newArrayList();
		
		for(Merchant _m : merchants) {
			List<Candy> candies = Candy.find().
					is("merchant._id",new ObjectId(_m.id))
					.toArray();
			for(Candy c: candies){
				com.mnt.custom.mobile.data.Candiez candiez = convertDBCandyToViewModel(c);
				candiezData.add(candiez.setMerchantInformation(_m.businessName, _m.locality, _m.image, _m.position));
			}
		}
		
		Map<String,Object> _map = new HashMap<String, Object>();
		_map.put("candies", candiezData);
		_map.put("androidversion", 1);
		return ok(Json.toJson(_map)).as("application/json");
	}
	
	//For Admin
	public static Result updateMerchantPostion(){
		DynamicForm form  = new DynamicForm().bindFromRequest();
		String lon = form.get("lon");
		String lat = form.get("lat");
		String admin = form.get("admin");
		if("#qwer123".equals(admin)) {
			String username = form.get("username");
			try {
			Merchant.findByUserName(username).updatePosition(Double.parseDouble(lon), Double.parseDouble(lat));
			} catch(Exception e) {
				return badRequest();
			}
		} else {
			return ok("check input parameter");
		}
		return ok();
	}
	
	//For Admin
	public static Result updateMerchantCode(){
		DynamicForm form  = new DynamicForm().bindFromRequest();
		String code = form.get("code");
		String admin = form.get("admin");
		if("#qwer123".equals(admin)) {
			String username = form.get("username");
			try {
			Merchant.findByUserName(username).updateCode(code);
			} catch(Exception e) {
				return badRequest();
			}
		} else {
			return ok("check input parameter");
		}
		return ok();
	}
	
	//For Admin
	public static Result merchantPassword(){
			DynamicForm form  = new DynamicForm().bindFromRequest();
			String code = form.get("password");
			String mode = form.get("mode");
			String admin = form.get("admin");
			String username = form.get("username");
			if("#qwer123".equals(admin)) {
				try {
					if("post".equals(mode)){
						Merchant.findByUserName(username).updatePassword(code);
					}
					
					if("get".equals(mode)){
						code = Merchant.findByUserName(username).decrypetdPassword();
					}
				} catch(Exception e) {
					return badRequest();
				}
			} else {
				return ok("check input parameter");
			}
			return ok(code);
	}
	
	public static Result merchants() {
		play.mvc.Http.Request _r =  request();
		String agent = _r.getHeader("User-Agent");
		
		List<Merchant> results= null;
		DynamicForm form  = new DynamicForm().bindFromRequest();
		String city = form.get("city");
		String category = form.get("category");
		results = getMerchantByCityAndCategory(city, category);
		
		List<com.mnt.custom.mobile.data.Merchant> merchantData = Lists.newArrayList();
		for(Merchant _m : results) {
			List<com.mnt.custom.mobile.data.Candiez> featured =null;
			boolean isFeature = true;
			StringBuilder information = new StringBuilder();
			if(isFeature && agent.contains("Mozilla")){
				featured = candiez(_m.id, information);
			}
			merchantData.add(new com.mnt.custom.mobile.data.Merchant(_m.businessName,
					_m.locality,_m.street,
					_m.state,
					_m.city.name(),
					_m.pin,
					_m.phoneNo,
					_m.id,
					_m.image,
					isFeature,
					featured,
					information.toString()));
			
		}
		
		
		if(agent.contains("Mozilla")) {
			return ok(Json.toJson(merchantData)).as("application/json");
		} else {
			Map<String,List<com.mnt.custom.mobile.data.Merchant>> _map = new HashMap<String, List<com.mnt.custom.mobile.data.Merchant>>();
			_map.put("merchants", merchantData);
			return ok(Json.toJson(_map)).as("application/json");
		}
	}



	private static List<Merchant> getMerchantByCityAndCategory(String city,
			String category) {
		List<Merchant> results;
		List<Query> _qs = new ArrayList<Query>();
		
		if(city != null){
			Pattern regex = Pattern.compile(city,Pattern.CASE_INSENSITIVE);
			_qs.add(DBQuery.regex("city", regex));
		}
		
		if(category != null){
			_qs.add(DBQuery.is("category", category));
		}
		_qs.add(DBQuery.is("designation", "Merchant"));
		Query query =DBQuery.and();
		for(Query q : _qs){
			query = query.and(q);
		}
		
		/** Builder End */		
		if(_qs.isEmpty())
		{
			results = Merchant.find().toArray();
		}
		else
		{
			results = Merchant.find().and(query).toArray();
		}
		return results;
	}
	
	
	
	public static Result getTime() {
		String time = System.currentTimeMillis()+""; 
		return ok("{'time':" +time+"}").as("application/json");
	}
	
	public static Result authenticate() {
		DynamicForm form = new DynamicForm().bindFromRequest();
        String email = form.get("email");
        String password = form.get("password");  
        
        if(Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(password) ){
         	return badRequest(Json.toJson(new MessageWrapper(Code.MERCHANT_CREDENTIAL_NEEDED,"Please Enter E-mail and Password")))
         			.as("application/json");
        }
        
           Merchant merchant = Merchant.find().is("email", email).next();
           String passwordFromDB;
		   try {
				passwordFromDB = EDHelper.doDecryption(merchant.password);
		   } catch (Exception e) {
				return badRequest(Json.toJson(new MessageWrapper(Code.BAD_PASSWORD_ALGO,"Please Enter Valid Password")))
              			.as("application/json");
		   }
		   
           if(!passwordFromDB.equals(password))
           {
        		 return badRequest(Json.toJson(new MessageWrapper(Code.MERCHANT_BAD_PASSWORD,"Please Enter Valid Password")))
              			.as("application/json");
           }
           else{
        	   Map jsonWrapper = new HashMap<String, String>();
        	   jsonWrapper.put("id", merchant.id);
               return ok(Json.toJson(jsonWrapper))
               			.as("application/json");
           }
	}

}
