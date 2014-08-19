package models.point;

import javax.persistence.Entity;
import javax.persistence.Id;

import net.vz.mongodb.jackson.ObjectId;
import play.db.ebean.Model;

@Entity
public class PointDetails extends Model {
	@Id
	@ObjectId
	public String id;
	public Integer no_of_point;
	public String description;
	@ObjectId
	public String point_image;
}
