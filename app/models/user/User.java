package models.user;

import javax.persistence.Entity;
import javax.persistence.Id;

import net.vz.mongodb.jackson.ObjectId;
import play.db.ebean.Model;


@Entity
public class User extends Model implements com.core.domain.Identity{
	
	@Id
	@ObjectId
	public long id;
	
	public String username;
	public String password;
	
	public String firstName;
	public String lastName;
	public String email;
	public String companyName;
	
	
	
	
	public static User findByUserName(String userName){
		return new User();
	}

	@Override
	public String userName() {
		return username;
	}

	@Override
	public String firstName() {
		return firstName;
	}
}
