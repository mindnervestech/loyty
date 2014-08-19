package models.merchant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;

import models.candy.Candy;
import models.identity.ItentityUser;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

import com.google.common.collect.Lists;
import com.mnt.core.auth.EDHelper;
import com.mnt.custom.domain.Category;
import com.mnt.custom.domain.City;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Merchant extends Model {

	private static JacksonDBCollection<Merchant, String> coll() {
		return MongoDB.getCollection(Merchant.class.getSimpleName(), Merchant.class, String.class);
		
	}
	static {
		coll().ensureIndex(new BasicDBObject("position", "2d"), "geospatialIdx");
	}
	
	@Id
	@ObjectId
	public String id;
	@JsonIgnore
	public String address;
	public String firstName;
	public String lastName;
	public String street;
	public String locality;
	public String password;
	public String businessName;
	public String state;
	@Enumerated(EnumType.STRING)
	public City city;
	public String pin;
	public String phoneNo;
	public String email;
	public String status;
	public String designation;
	
	@Lob
	public String comments;
	public String tempPassword;
	@Enumerated(EnumType.STRING)
	public Category category;
	
	public List<Double> position = Lists.newArrayList(0d, 0d);
	
	@ObjectId
	public String image;
	
	@Embedded
	public ItentityUser user;
	
	@Embedded
	public List<Candy> candies;

	public String  code;
	
	@Override
	public void save() {
		String _sid = coll().save(this).getSavedId();
		this.id = _sid;
	}
	
	@Override
    public void update(){
            coll().updateById(this.id, this);
    }
	
	
	public void updatePosition(double lon, double lat){
		this.position.set(0, lon);
		this.position.set(1, lat);
        coll().updateById(this.id, this);
	}
	
	public void updateCode(String code){
		this.code= code;
        coll().updateById(this.id, this);
	}
	
	public void updatePassword(String password) throws Exception {
		this.password = EDHelper.doEncryption(password);
		coll().updateById(this.id, this);
	}

	public String decrypetdPassword()  throws Exception {
		return EDHelper.doDecryption(this.password);
	}
	
	public void updateCandy(DBObject candy) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("$push",new BasicDBObject("candies",candy));
		
		BasicDBObject basicDBObject1 = new BasicDBObject();
		basicDBObject1.put("_id",new org.bson.types.ObjectId(this.id));
		
		coll().update(basicDBObject1, basicDBObject);
	}
	
	

	public static Merchant findById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll().find(basicDBObject).next();
	}
	
	public static Merchant findById(String objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",new org.bson.types.ObjectId(objectId));
		return coll().find(basicDBObject).next();
	}
	
	public static DBObject getDBObjById(org.bson.types.ObjectId objectId) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("_id",objectId);
		return coll().find(basicDBObject).getQuery();
	}

	public static DBCursor<Merchant> find(BasicDBObject... o) {
		if(o != null && o.length>0){
			return coll().find(o[0]);
		}
		return coll().find();
	}
	
	public static Object saveImage (File file,String filename) throws IOException {
		GridFS gridFS = new GridFS(coll().getDB(),"merchantImage");
		GridFSInputFile gfsFile = gridFS.createFile(file);
		gfsFile.setFilename(filename);
		gfsFile.setContentType("image/jpg");
		gfsFile.save();
		return gfsFile.getId();
	}
	
	public static Object saveImage (ByteArrayOutputStream  os,String filename) throws IOException {
		GridFS gridFS = new GridFS(coll().getDB(),"merchantImage");
		GridFSInputFile gfsFile = gridFS.createFile(os.toByteArray());
		gfsFile.setFilename(filename);
		gfsFile.setContentType("image/jpg");
		gfsFile.save();
		return gfsFile.getId();
	}
	
	public static void deleteImageById (String _id) {
		GridFS gridFS = new GridFS(coll().getDB(),"merchantImage");
		gridFS.remove(new org.bson.types.ObjectId(_id));
	}

	public static InputStream getImage(String id) throws IOException{
		GridFS gridFS = new GridFS(coll().getDB(),"merchantImage");
		GridFSDBFile imageForOutput = gridFS.findOne(new org.bson.types.ObjectId(id));
		return imageForOutput.getInputStream();
	}
	
	public static List<Merchant> findAll() {
		return coll().find().toArray();
	}
	
	public static Merchant findByUserName(String username) {
		return coll().find().is("email", username).count() == 0 ? null : coll().find().is("email", username).next();
	}

	public Merchant findMe() {
		return findById(this.id);
	}	
	
	public static List<Merchant> merchantsInProximity(double[] position, Double meters) {
		BasicDBObject filter = new BasicDBObject("$near", position);
		filter.put("$maxDistance", meters);
		BasicDBObject query = new BasicDBObject("position", filter);
		return coll().find(query).toArray();
	}


	
}
