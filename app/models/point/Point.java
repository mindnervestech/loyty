package models.point;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import play.db.ebean.Model;

@Entity
public class Point extends Model {

	public Double minimum_amount_for_point;
	@Embedded
	public List<PointDetails> point_details = new ArrayList<PointDetails>();
	
}
