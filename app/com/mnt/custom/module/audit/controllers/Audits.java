package com.mnt.custom.module.audit.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.audit.Audit;
import models.candy.Candy;
import models.customer.Customer;
import models.merchant.Merchant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.helper.form;

import com.core.domain.LoyaltyType;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mnt.core.auth.BasicAuth;
import com.mnt.core.menu.MenuBarFixture;
import com.mnt.custom.domain.Category;
import com.mongodb.DBObject;

import controllers.Secured;

@Security.Authenticated(Secured.class)
@BasicAuth
public class Audits extends Controller {
	
	static Merchant merchant = Merchant.findByUserName(request().username());
	
	public static Result index() {
        return ok(views.html.audit.index.render(null,
        		MenuBarFixture.build(request().username()),
				Merchant.findByUserName(request().username())));
    }
	
	public static Result showVisitReport(){
		String jsonResopnce = "";
		jsonResopnce = Audit.getVisitsByMerchant(merchant.id);
		return ok(jsonResopnce).as("JSON");
	}
	
	public static Result showPointsReport(){
		String jsonResopnce = "";
		jsonResopnce = Audit.getPointsByMerchant(merchant.id);
		return ok(jsonResopnce).as("JSON");
	}
	
	public static Result showStampsReport(){
		String jsonResopnce = "";
		jsonResopnce = Audit.getStampsByMerchant(merchant.id);
		return ok(jsonResopnce).as("JSON");
	}
	
	//Show Visit health
	public static Result showVisitProgramHealth() {
		DynamicForm request =DynamicForm.form().bindFromRequest();
		String fromdate = request.get("fromdate");
		String todate = request.get("todate");
		String pattern = "dd-MM-yyyy";
		Date startdate = null;
		Date enddate = null;
		
		if(fromdate != null && todate != null){
			startdate = DateTime.parse(fromdate, DateTimeFormat.forPattern(pattern)).toDate();
			enddate = DateTime.parse(todate, DateTimeFormat.forPattern(pattern)).toDate();
		}
		
		List<Candy> candies = Candy.getAllCandiesOfMerchant(merchant.id);
		JsonArray jsonArray = new JsonArray();
		for(Candy candy : candies){
			if(candy.loyaltyType == LoyaltyType.VISIT){
				DBObject audits = Audit.getVisitDetails(candy.id, startdate, enddate);
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(new Gson().toJson(audits));
				
				JsonArray array = element.getAsJsonArray();
				List<List<Long>> data = Lists.newArrayList();
				ChartElement chartElement = new ChartElement();
				for(int i=0;i<array.size();i++){
					JsonObject object = array.get(i).getAsJsonObject();
					pattern = "MMM dd, yyyy hh:mm:ss aa";
					Date date = DateTime.parse(object.get("date").getAsString(), DateTimeFormat.forPattern(pattern)).toDate();
					List<Long> innerList = new ArrayList<Long>();
					innerList.add(date.getTime());
					innerList.add((long)object.get("count").getAsDouble());
					data.add(innerList);
				}
				chartElement.data = data;
				chartElement.label = candy.name;
				
				JsonElement  node =  new Gson().toJsonTree(chartElement);
				jsonArray.add(node);
			}
		}
		
		return ok(new Gson().toJson(jsonArray));
	}
	
	//Show Point health
	public static Result showPointProgramHealth() {
		DynamicForm request =DynamicForm.form().bindFromRequest();
		String fromdate = request.get("fromdate");
		String todate = request.get("todate");
		System.out.println(fromdate);
		String pattern = "dd-MM-yyyy";
		Date startdate = null;
		Date enddate = null;
		
		if(fromdate != null && todate != null){
			startdate = DateTime.parse(fromdate, DateTimeFormat.forPattern(pattern)).toDate();
			enddate = DateTime.parse(todate, DateTimeFormat.forPattern(pattern)).toDate();
		}
		
		List<Candy> candies = Candy.getAllCandiesOfMerchant(merchant.id);
		JsonArray jsonArray = new JsonArray();
		for(Candy candy : candies){
			if(candy.loyaltyType == LoyaltyType.POINT){
				DBObject audits = Audit.getPointDetails(candy.id, startdate, enddate);
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(new Gson().toJson(audits));
				
				JsonArray array = element.getAsJsonArray();
				List<List<Long>> data = Lists.newArrayList();
				ChartElement chartElement = new ChartElement();
				for(int i=0;i<array.size();i++){
					JsonObject object = array.get(i).getAsJsonObject();
					pattern = "MMM dd, yyyy hh:mm:ss aa";
					Date date = DateTime.parse(object.get("date").getAsString(), DateTimeFormat.forPattern(pattern)).toDate();
					List<Long> innerList = new ArrayList<Long>();
					innerList.add(date.getTime());
					innerList.add((long)object.get("count").getAsDouble());
					data.add(innerList);
				}
				chartElement.data = data;
				chartElement.label = candy.name;
				
				JsonElement  node =  new Gson().toJsonTree(chartElement);
				jsonArray.add(node);
			}
		}
		System.out.println(jsonArray);
		return ok(new Gson().toJson(jsonArray));
	}
	
	//Show Point health
	public static Result showStampsProgramHealth() {
		DynamicForm request =DynamicForm.form().bindFromRequest();
		String fromdate = request.get("fromdate");
		String todate = request.get("todate");
		String pattern = "dd-MM-yyyy";
		Date startdate = null;
		Date enddate = null;
		
		if(fromdate != null && todate != null){
			startdate = DateTime.parse(fromdate, DateTimeFormat.forPattern(pattern)).toDate();
			enddate = DateTime.parse(todate, DateTimeFormat.forPattern(pattern)).toDate();
		}
		
		List<Candy> candies = Candy.getAllCandiesOfMerchant(merchant.id);
		JsonArray jsonArray = new JsonArray();
		for(Candy candy : candies){
				DBObject audits = Audit.getStampDetails(candy.id, startdate, enddate);
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(new Gson().toJson(audits));
				
				JsonArray array = element.getAsJsonArray();
				List<List<Long>> data = Lists.newArrayList();
				ChartElement chartElement = new ChartElement();
				for(int i=0;i<array.size();i++){
					JsonObject object = array.get(i).getAsJsonObject();
					pattern = "MMM dd, yyyy hh:mm:ss aa";
					Date date = DateTime.parse(object.get("date").getAsString(), DateTimeFormat.forPattern(pattern)).toDate();
					List<Long> innerList = new ArrayList<Long>();
					innerList.add(date.getTime());
					innerList.add((long)object.get("count").getAsDouble());
					data.add(innerList);
				}
				chartElement.data = data;
				chartElement.label = candy.name;
				
				JsonElement  node =  new Gson().toJsonTree(chartElement);
				jsonArray.add(node);
		}
		//System.out.println(jsonArray);
		return ok(new Gson().toJson(jsonArray));
	}
	
	//show Redeem Graph
	public static Result showRedeemGraph() {
		DynamicForm request =DynamicForm.form().bindFromRequest();
		String fromdate = request.get("fromdate");
		String todate = request.get("todate");
		String pattern = "dd-MM-yyyy";
		Date startdate = null;
		Date enddate = null;
		
		if(fromdate != null && todate != null){
			startdate = DateTime.parse(fromdate, DateTimeFormat.forPattern(pattern)).toDate();
			enddate = DateTime.parse(todate, DateTimeFormat.forPattern(pattern)).toDate();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Candy> candies = Candy.getAllCandiesOfMerchant(merchant.id);
		JsonArray jsonArray = new JsonArray();
		for(Candy candy : candies){
			DBObject audits = Audit.getRedeemDetails(candy.id,startdate,enddate);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(new Gson().toJson(audits));
			
			JsonArray array = element.getAsJsonArray();
			List<List<Long>> data = Lists.newArrayList();
			ChartElement chartElement = new ChartElement();
			for(int i=0;i<array.size();i++){
				JsonObject object = array.get(i).getAsJsonObject();
				pattern = "MMM dd, yyyy hh:mm:ss aa";
				Date date = DateTime.parse(object.get("date").getAsString(), DateTimeFormat.forPattern(pattern)).toDate();
				List<Long> innerList = new ArrayList<Long>();
				innerList.add(date.getTime());
				innerList.add((long)object.get("count").getAsDouble());
				data.add(innerList);
			}
			chartElement.data = data;
			chartElement.label = candy.name;
				
			JsonElement  node =  new Gson().toJsonTree(chartElement);
			jsonArray.add(node);
		}
		System.out.println(jsonArray);
		return ok(new Gson().toJson(jsonArray));
	}
	
	//show Customer Graph
	public static Result showCustomerGraph() {
		DynamicForm request =DynamicForm.form().bindFromRequest();
		String fromdate = request.get("fromdate");
		String todate = request.get("todate");
		String pattern = "dd-MM-yyyy";
		Date startdate = null;
		Date enddate = null;
		
		if(fromdate != null && todate != null){
			startdate = DateTime.parse(fromdate, DateTimeFormat.forPattern(pattern)).toDate();
			enddate = DateTime.parse(todate, DateTimeFormat.forPattern(pattern)).toDate();
		}
		
		JsonArray jsonArray = new JsonArray();
		DBObject audits = Audit.getCustomersByMerchant(merchant.id, startdate, enddate);
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(new Gson().toJson(audits));
			
		JsonArray array = element.getAsJsonArray();
		List<List<Long>> data = Lists.newArrayList();
		ChartElement chartElement = new ChartElement();
		for(int i=0;i<array.size();i++){
			JsonObject object = array.get(i).getAsJsonObject();
			pattern = "MMM dd, yyyy hh:mm:ss aa";
			Date date = DateTime.parse(object.get("date").getAsString(), DateTimeFormat.forPattern(pattern)).toDate();
			List<Long> innerList = new ArrayList<Long>();
			innerList.add(date.getTime());
			innerList.add((long)object.get("count").getAsDouble());
			data.add(innerList);
		}
	
		chartElement.data = data;
		chartElement.label = "Customers";
				
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonElement  node =  new Gson().toJsonTree(chartElement);
		jsonArray.add(node);
		return ok(new Gson().toJson(jsonArray));
	}
	
	//Chart Element
	static class ChartElement {
		String label;
		List<List<Long>> data;
	}
	
	public static Result analyzeCustomer() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		Customer customer = Customer.findById(new org.bson.types.ObjectId(form.get("query")));
		
		JsonArray jsonArray = new JsonArray();
		DBObject audits = Audit.getCustomerAnalyzeData(customer.phoneNo);
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(new Gson().toJson(audits));
		List<List<String>> data = Lists.newArrayList();
		JsonArray array = element.getAsJsonArray();
		CustomerChartElement chartElement = new CustomerChartElement();
		
		for(int i=0;i<array.size();i++){
			JsonObject object = array.get(i).getAsJsonObject();
			List<String> innerList = new ArrayList<String>();
			String category = object.get("category").getAsString();
			Category cat = Category.valueOf(category);
			innerList.add(cat.getName());
			innerList.add(object.get("count").getAsString());
			data.add(innerList);
		}
		chartElement.data = data;
		
		JsonElement  node =  new Gson().toJsonTree(chartElement);
		jsonArray.add(node);
		return ok(new Gson().toJson(jsonArray));
	}
	
	public static Result averageExpenditure() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		Customer customer = Customer.findById(new org.bson.types.ObjectId(form.get("query")));
		
		JsonArray jsonArray = new JsonArray();
		DBObject audits = Audit.getCustomerExpenditureData(customer.phoneNo);
		System.out.println(audits);
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(new Gson().toJson(audits));
		List<List<String>> data = Lists.newArrayList();
		JsonArray array = element.getAsJsonArray();
		CustomerChartElement chartElement = new CustomerChartElement();
		
		for(int i=0;i<array.size();i++){
			JsonObject object = array.get(i).getAsJsonObject();
			List<String> innerList = new ArrayList<String>();
			String category = object.get("category").getAsString();
			Category cat = Category.valueOf(category);
			innerList.add(cat.getName());
			innerList.add(object.get("avg").getAsString());
			data.add(innerList);
		}
		chartElement.data = data;
		
		JsonElement  node =  new Gson().toJsonTree(chartElement);
		jsonArray.add(node);
		return ok(new Gson().toJson(jsonArray));
	}
	
	static class CustomerChartElement {
		String label;
		List<List<String>> data;
	}
	
}
