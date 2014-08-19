package models.visit;

import javax.persistence.Entity;
import javax.persistence.Id;

import net.vz.mongodb.jackson.ObjectId;
import play.db.ebean.Model;

@Entity
public class VisitDetails extends Model {
	@Id
	@ObjectId
	public String id;
	public Integer no_of_visit;
	public String description;
	@ObjectId
	public String visit_image;
}
