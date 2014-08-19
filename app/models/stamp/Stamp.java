package models.stamp;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import models.candy.Candy;
import models.customer.Customer;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBUpdate.Builder;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Entity
public class Stamp extends Model{

	private static JacksonDBCollection<Stamp, String> coll = MongoDB.getCollection(Stamp.class.getSimpleName(), Stamp.class, String.class);	
	
	@Id
	@ObjectId
	public String id;
	@Embedded
	public Candy candy;
	@Embedded
	public Customer customer;
	public double howMany;
	
	
	
	
	@Override
	public void save() {
		String _sid = coll.save(this).getSavedId();
		this.id = _sid;
		
	}
	
	public static Stamp findById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll.find(basicDBObject).next();
	}
	
	public static Stamp findByCandyId(String candyId){
		if(coll.findOne(DBQuery.is("candy._id", new org.bson.types.ObjectId(candyId)))==null){
			return null;
		}
		return coll.findOne(DBQuery.is("candy._id", new org.bson.types.ObjectId(candyId)));
	}
	
	public static Stamp findWith(String withCustomer, String withCandy) {
		if(!coll.find().and(DBQuery.is("candy._id", new org.bson.types.ObjectId(withCandy)),
				DBQuery.is("customer._id", new org.bson.types.ObjectId(withCustomer))).hasNext())
			return null;
		
		return coll.find().and(DBQuery.is("candy._id", new org.bson.types.ObjectId(withCandy)),
				DBQuery.is("customer._id", new org.bson.types.ObjectId(withCustomer))).next();
	}
	
	public static List<Stamp> findWithCustomer(String withCustomer) {
		return coll.find().and(DBQuery.is("customer._id", new org.bson.types.ObjectId(withCustomer))).toArray();
	}
	
	public static DBObject getDBObjById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll.find(basicDBObject).getQuery();
	}
	
	public static DBObject getDBObjById(String objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",new org.bson.types.ObjectId(objectId));
		return coll.find(basicDBObject).getQuery();
	}
	
	@Override
	public void update() {
		Builder update = new Builder();
		update.set("howMany", this.howMany);
		coll.update(getDBObjById(this.id),update );
		
	}
	
	public static DBCursor<Stamp> find(BasicDBObject... o) {
		if(o != null && o.length>0){
			return coll.find(o[0]);
		}
		return coll.find();
	}

}
