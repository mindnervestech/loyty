package models.audit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import models.candy.Candy;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBQuery.Query;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

import com.google.gson.Gson;
import com.mnt.custom.domain.Category;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Entity

public class Audit extends Model implements Serializable {
	
	private static JacksonDBCollection<Audit, String> coll = MongoDB.getCollection(Audit.class.getSimpleName(), Audit.class, String.class);	
	
	@Id
	@ObjectId
	public String id;
	
	@ObjectId
	public String entityId;
	
	// Audit will be always be on some merchant
	@ObjectId
	public String merchantId;
	
	@ObjectId
	public String candyId;
	
	@Enumerated(EnumType.STRING)
	public EntityType entityType;
	
	@Enumerated(EnumType.STRING)
	public EntityAction entityAction;
	
	public String actionDescription;
	
	public String phoneNumber;
	
	public Double expenditure = 0.0;
	
	@Enumerated(EnumType.STRING)
	public Category category;
	
	public Date date;
	
	public String time;
	
	public Audit(String merchantId,String entityId, EntityType entityType,
			EntityAction entityAction, String actionDescription) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
		this.entityAction = entityAction;
		this.actionDescription = actionDescription;
		this.merchantId = merchantId;
	}
	
	public Audit(String merchantId,String entityId, String candyId, EntityType entityType,
			EntityAction entityAction, String actionDescription) {
		super();
		this.candyId = candyId;
		this.entityId = entityId;
		this.entityType = entityType;
		this.entityAction = entityAction;
		this.actionDescription = actionDescription;
		this.merchantId = merchantId;
	}
	
	public Audit(String merchantId,String entityId, String candyId, EntityType entityType,
			EntityAction entityAction, String actionDescription, String phoneNumber, Category category) {
		super();
		this.candyId = candyId;
		this.entityId = entityId;
		this.entityType = entityType;
		this.entityAction = entityAction;
		this.actionDescription = actionDescription;
		this.merchantId = merchantId;
		this.phoneNumber = phoneNumber;
		this.category = category;
	}
	
	public static enum EntityType {
		STAMP,
		CUSTOMER,
		CANDY
	}
	
	public static enum EntityAction {
		VISIT_UPDATED,
		POINT_UPDATED,
		CLAIMED,
		CREATED
	}
	
	@Override
	public void save() {
		new DateTime();
		String date = DateTime.now().toString("dd-MM-yyyy");
		String time = DateTime.now().toString("hh:mm:ss");
		this.date = DateTime.parse(date, DateTimeFormat.forPattern("dd-MM-yyyy")).toDate();
		this.time = time;
		String _sid = coll.save(this).getSavedId();
		this.id = _sid;
		
	}
	
	public static String getVisitsByMerchant(String merchantId){
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);
		
		//for range check
		Query dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(10).toDate()),
				DBQuery.lessThanEquals("date", new DateTime().plusDays(11).toDate()));

		//condn
		Query cond = DBQuery.and(DBQuery.is("merchantId",  new org.bson.types.ObjectId(merchantId)),
				DBQuery.is("entityAction","VISIT_UPDATED"), dateQuery, DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		String jsonResponse = "";
		jsonResponse = new Gson().toJson(groupFields);
		return jsonResponse;
	}

	public static String getPointsByMerchant(String merchantId){
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);

		//for range check
		Query dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(10).toDate()),
				DBQuery.lessThanEquals("date", new DateTime().plusDays(11).toDate()));

		//condn
		Query cond = DBQuery.and(DBQuery.is("merchantId",  new org.bson.types.ObjectId(merchantId)),
				DBQuery.is("entityAction","POINT_UPDATED"), dateQuery, DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);

		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		
		String jsonResponse = "";
		jsonResponse = new Gson().toJson(groupFields);
		
		return jsonResponse;
	}
	
	public static String getStampsByMerchant(String merchantId){
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);

		//for range check
		Query dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(10).toDate()),
				DBQuery.lessThanEquals("date", new DateTime().plusDays(11).toDate()));

		//condn
		Query cond = DBQuery.and(DBQuery.is("merchantId",  new org.bson.types.ObjectId(merchantId)),
				dateQuery,DBQuery.notEquals("entityAction", "CLAIMED"), DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);

		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		
		String jsonResponse = "";
		jsonResponse = new Gson().toJson(groupFields);
		
		return jsonResponse;
	}
	
	public static DBObject getCustomersByMerchant(String merchantId, Date fromDate, Date toDate){
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);

		Query dateQuery ;
		//for range check
		if(fromDate != null && toDate != null){
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", fromDate),
					DBQuery.lessThanEquals("date", toDate));
		}
		else {
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(15).toDate()),
					DBQuery.lessThanEquals("date", new DateTime().plusDays(15).toDate()));
		}
		
		//condn
		Query cond = DBQuery.and(DBQuery.is("merchantId",  new org.bson.types.ObjectId(merchantId)),
				DBQuery.is("entityAction","CREATED"), dateQuery, DBQuery.is("entityType","CUSTOMER"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);

		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		
		return groupFields;
	}
	
//	 select count(distinct(stamp_id)) ,distinct(stamp_id) from audit where merchat = ? and
//			 entityType = STAMP and (entityAction = Visit_Updated or entityAction = poit_Updated) groupby stampID;
	public static String getUniqueCustomerCountByMerchant(String merchantId){
		//by which field
		DBObject key = new BasicDBObject();
		key.put("entityId", 1);
		
		Query orQuery = DBQuery.or(DBQuery.is("entityAction", "POINT_UPDATED"),DBQuery.is("entityAction", "VISIT_UPDATED"));
		
		Query dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(6).toDate()),
								DBQuery.lessThanEquals("date", new DateTime().plusDays(11).toDate()));
		
		Query andQuery = DBQuery.and(DBQuery.is("merchantId",  new org.bson.types.ObjectId(merchantId)),
								orQuery, dateQuery, DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, andQuery, initial, reduce);
		//System.out.println(groupFields);
		return groupFields.toString();
	}

	public static DBObject getVisitDetails(String candyId, Date fromDate, Date toDate) {
		
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);
		
		//for range check
		Query dateQuery ;
		if(fromDate != null && toDate != null){
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", fromDate),
					DBQuery.lessThanEquals("date", toDate));
		}
		else {
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(15).toDate()),
					DBQuery.lessThanEquals("date", new DateTime().plusDays(15).toDate()));
		}
		
		//condn
		Query cond = DBQuery.and(DBQuery.is("candyId",  new org.bson.types.ObjectId(candyId)),
				DBQuery.is("entityAction","VISIT_UPDATED"), dateQuery, DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		return groupFields;
	}
	
	public static DBObject getPointDetails(String candyId, Date fromDate, Date toDate) {
		
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);
		
		//for range check
		Query dateQuery ;
		if(fromDate != null && toDate != null){
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", fromDate),
					DBQuery.lessThanEquals("date", toDate));
		}
		else {
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(15).toDate()),
					DBQuery.lessThanEquals("date", new DateTime().plusDays(15).toDate()));
		}
		
		//condn
		Query cond = DBQuery.and(DBQuery.is("candyId",  new org.bson.types.ObjectId(candyId)),
				DBQuery.is("entityAction","POINT_UPDATED"), dateQuery, DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		return groupFields;
	}
	
	public static DBObject getStampDetails(String candyId, Date fromDate, Date toDate) {
		
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);
		
		//for range check
		Query dateQuery ;
		if(fromDate != null && toDate != null){
			System.out.println(fromDate+" "+toDate);
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", fromDate),
					DBQuery.lessThanEquals("date", toDate));
		}
		else {
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(15).toDate()),
					DBQuery.lessThanEquals("date", new DateTime().plusDays(15).toDate()));
		}
		//condn
		Query cond = DBQuery.and(DBQuery.is("candyId",  new org.bson.types.ObjectId(candyId)),
				DBQuery.notEquals("entityAction","CLAIMED"), dateQuery, DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		return groupFields;
	}
	//Get Redeem Details
	public static DBObject getRedeemDetails(String candyId, Date fromDate, Date toDate) {
		//by which field
		DBObject key = new BasicDBObject();
		key.put("date", 1);
		
		Query dateQuery ;
		//for range check
		if(fromDate != null && toDate != null){
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", fromDate),
					DBQuery.lessThanEquals("date", toDate));
		}
		else {
			dateQuery = DBQuery.and(DBQuery.greaterThanEquals("date", new DateTime().minusDays(15).toDate()),
					DBQuery.lessThanEquals("date", new DateTime().plusDays(15).toDate()));
		}
		//condn
		Query cond = DBQuery.and(DBQuery.is("candyId",  new org.bson.types.ObjectId(candyId)),
				DBQuery.is("entityAction","CLAIMED"), dateQuery, DBQuery.is("entityType","STAMP"));
		
		//initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		return groupFields;
	}
	
	//Get Customer Analyze Details
	public static DBObject getCustomerAnalyzeData(String phoneNumber) {
		//by which field
		DBObject key = new BasicDBObject();
		key.put("category", 1);
		
		//for condition
		Query cond = DBQuery.and(DBQuery.is("phoneNumber", phoneNumber),
				DBQuery.notEquals("entityAction","CLAIMED"), DBQuery.is("entityType","STAMP"));
		
		// initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.count++}";
		
		DBObject groupFields = coll.group(key, cond, initial, reduce);
		
		return groupFields;
	}
	
	//Get Customer Analyze Details
	public static DBObject getCustomerExpenditureData(String phoneNumber) {
		//by which field
		DBObject key = new BasicDBObject();
		key.put("category", 1);
		
		//for condition
		Query cond = DBQuery.and(DBQuery.is("phoneNumber", phoneNumber),
				DBQuery.notEquals("entityAction","CLAIMED"), DBQuery.is("entityType","STAMP"));
		
		// initial value for computing purpose
		DBObject initial = new BasicDBObject();
		initial.put("expenditure",0);
		initial.put("count",0);
		
		//javascript function
		String reduce = "function(curr,result) {result.expenditure += curr.expenditure; result.count++}";
	
		//finalize function
		String finalize = "function(result) {result.avg =Math.round(result.expenditure / result.count);}";
		DBObject groupFields = coll.group(key, cond, initial, reduce, finalize);
		
		return groupFields;
	}
}
