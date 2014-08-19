package models.candy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import models.merchant.Merchant;
import models.point.Point;
import models.visit.Visit;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBQuery.Query;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.data.format.Formats;
import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

import com.core.domain.LoyaltyType;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Entity
@Embeddable
public class Candy extends Model implements Serializable{
	private static JacksonDBCollection<Candy, String> coll() {
		return MongoDB.getCollection(Candy.class.getSimpleName(), Candy.class, String.class);
	}
		
	@Id
	@ObjectId
	public String id;
	
	
	public String code;
	public String name;
	
	@Enumerated(EnumType.STRING)
	public LoyaltyType loyaltyType;
	
	@Formats.DateTime(pattern="dd-MM-yyyy")
	public Date valid_till;
	
	@Embedded
	public Merchant merchant;
	
	@Embedded
	public Point points;
	@Embedded
	public Visit visits;
	
	
	@Override
	public void save() {
		String _sid = coll().save(this).getSavedId();
		this.id = _sid;
		
	}
	
	@Override
	public void update(){
		coll().updateById(this.id, this);
	}
	
	public static void deleteImageById(String _id){
		GridFS gridFS = new GridFS(coll().getDB(),"candyImage");
		gridFS.remove(new org.bson.types.ObjectId(_id));
	}
	public static Candy findById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll().find(basicDBObject).next();
	}
	
	public static DBObject getDBObjById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll().find(basicDBObject).getQuery();
	}
	
	public static DBCursor<Candy> find(BasicDBObject... o) {
		if(o != null && o.length>0){
			return coll().find(o[0]);
		}
		return coll().find();
	}
	
	public static Object saveImage(File file,String filename) throws IOException{
		
		GridFS gridFS = new GridFS(coll().getDB(),"candyImage");
		GridFSInputFile gfsFile = gridFS.createFile(file);
		gfsFile.setFilename(filename);
		gfsFile.setContentType("image/jpg");
		gfsFile.save();
		return gfsFile.getId();
	}
	
	public static Object saveImage(ByteArrayOutputStream  os,String filename) throws IOException{
		
		GridFS gridFS = new GridFS(coll().getDB(),"candyImage");
		GridFSInputFile gfsFile = gridFS.createFile(os.toByteArray());
		gfsFile.setFilename(filename);
		gfsFile.setContentType("image/jpg");
		gfsFile.save();
		return gfsFile.getId();
	}
	
	public static InputStream getImage(String id) throws IOException{
		
		GridFS gridFS = new GridFS(coll().getDB(),"candyImage");
		GridFSDBFile imageForOutput = gridFS.findOne(new org.bson.types.ObjectId(id));
		return imageForOutput.getInputStream();
	}
	
	public static Candy findUniqueCandyCode(String candyCode,String merchantId){
		return coll().findOne(DBQuery.and(DBQuery.is("merchant._id", new org.bson.types.ObjectId(merchantId)),DBQuery.is("code", candyCode)));
	}
	
	public static List<Candy> getAll() {
		return coll().find().toArray();
	}
	
	//Get All Candies of Logged In user
	public static List<Candy> getAllCandiesOfMerchant(String merchantId){
		Query query = DBQuery.is("merchant._id", new org.bson.types.ObjectId(merchantId));
		return coll().find(query).toArray();
	}

	public Candy findMe() {
		return Candy.findById(new org.bson.types.ObjectId(this.id));
	}
	
	

}
