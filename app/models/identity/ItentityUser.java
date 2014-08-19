package models.identity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import net.vz.mongodb.jackson.JacksonDBCollection;
import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

import com.core.domain.Designation;
import com.mongodb.BasicDBObject;

@Entity
@Embeddable
public class ItentityUser extends Model  {

	private static JacksonDBCollection<ItentityUser, String> coll = MongoDB.getCollection(ItentityUser.class.getSimpleName(), ItentityUser.class, String.class);	
	
	static{
		coll.ensureIndex(new BasicDBObject("username", 1), "username", true);
	}
	@Id
	public String id;
	
	public String username;
	public String password;
	@Enumerated(EnumType.STRING)
	public Designation designation;
	
	@Override
	public void save() {
		coll.save(this);
	}
	
	
	
	
}
