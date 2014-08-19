package models.customer;

import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import models.Consumer.Consumer;
import models.merchant.Merchant;
import models.stamp.Stamp;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

import com.mnt.custom.domain.Category;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


@Entity
public class Customer extends Model{

	private static JacksonDBCollection<Customer, String> coll = 
			MongoDB.getCollection(Customer.class.getSimpleName(), Customer.class, String.class);	
	
	static{
		//coll.ensureIndex(new BasicDBObject("phoneNo", 1), "phoneNo", true);
	}
	
	@Id
	@ObjectId
	public String id;
	
	public String phoneNo;
	public String firstName;
	public String lastName;
	public String address;
	public String email;
	
	public String username;
	
	@Embedded
	public List<Stamp> stamps;
	
	@Embedded
	public Merchant merchant;
	
	
	
	@Override
	public void save() {
		String _sid = coll.save(this).getSavedId();
		this.id = _sid;
		new Consumer(this).saveOrUpdate();
	}
	
	@Override
    public void update(){
            coll.updateById(this.id, this);
    }
	
	public void updateStamp(DBObject stamp) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("$push",new BasicDBObject("stampBuckets",stamp));
		
		BasicDBObject basicDBObject1 = new BasicDBObject();
		basicDBObject1.put("_id",new org.bson.types.ObjectId(this.id));
		
		coll.update(basicDBObject1, basicDBObject);
	}
	
	
	
	public void insert1() {
		BasicDBObject stampDBObject = new BasicDBObject();
		stampDBObject.put("candy.",new org.bson.types.ObjectId("51f01715d61599c1804d78b6"));
		
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("$push",new BasicDBObject("stampBuckets",stampDBObject));
		
		BasicDBObject basicDBObject1 = new BasicDBObject();
		basicDBObject1.put("_id",new org.bson.types.ObjectId(this.id));
		
		 
		coll.update(basicDBObject1,basicDBObject);
	}
	
	public static Customer findById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll.find(basicDBObject).next();
	}
	
	public static DBObject getDBObjById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll.find(basicDBObject).getQuery();
	}
	
	public static DBCursor<Customer> find(BasicDBObject... o) {
		if(o != null && o.length>0){
			return coll.find(o[0]);
		}
		return coll.find();
	}

}
