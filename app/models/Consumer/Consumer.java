package models.Consumer;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.mnt.custom.domain.Category;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import models.customer.Customer;
import models.merchant.Merchant;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;

import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

@Entity
public class Consumer extends Model{

	private static JacksonDBCollection<Consumer, String> coll = 
			MongoDB.getCollection(Consumer.class.getSimpleName(), Consumer.class, String.class);
	
	@Id
	@ObjectId
	public String id;
	
	public String phoneNo;
	
	public Consumer(String phoneNo, String firstName, String lastName,
			String address, String email) {
		super();
		this.phoneNo = phoneNo;
	}

	
	public String username;
	public String password;
	
	public double[] position;	
	
	public Consumer(String username, String password, String phoneNo) {
		this.username = username;
		this.password = "";
		this.phoneNo = "";
	}
	
	public Consumer(Customer customer) {
		super();
		this.phoneNo = customer.phoneNo;
	}
	
	public void saveOrUpdate() {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("phoneNo",phoneNo);
		
		DBCursor<Consumer> cursor = find(basicDBObject);
		if (cursor == null || cursor.count() == 0) {
			save();
		}
	}
	
	
	@Override
	public void save() {
		String _sid = coll.save(this).getSavedId();
		this.id = _sid;
	}
	
	@Override
    public void update(){
            coll.updateById(this.id, this);
    }
	
	public static Consumer findById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll.find(basicDBObject).next();
	}
	
	public static DBObject getDBObjById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll.find(basicDBObject).getQuery();
	}
	
	public static DBCursor<Consumer> find(BasicDBObject... o) {
		if(o != null && o.length>0){
			return coll.find(o[0]);
		}
		return coll.find();
	}

	public static Consumer ifUserNameNotPresent(String username) throws RegistrationFailedUserNameTakenException {
		
		List<Consumer>  consumers = coll.find().is("username", username).toArray();
		if (consumers != null && consumers.size() > 0) {
			throw new RegistrationFailedUserNameTakenException();
		}
		return new Consumer(username,"","");
	}
	
	public Consumer then() {
		return this;
	}

	public Consumer register(String password, String phoneNo) {
		this.password = password;
		this.phoneNo = phoneNo;
		this.save();
		return this;
	}
	
	public static void updatePostion(String phoneNo, double[] position) {
		List<Consumer>  consumers = coll.find().is("phoneNo", phoneNo).toArray();
		if (consumers != null && consumers.size() > 0) {
			consumers.get(0).position = position;
			consumers.get(0).save();
		}
	}
	
	public static List<Consumer> consumersInProximity(double[] position, int meters){
		BasicDBObject filter = new BasicDBObject("$near", position);
		filter.put("$maxDistance", meters);
		BasicDBObject query = new BasicDBObject("position", filter);
		return coll.find(query).toArray();
	}
}
