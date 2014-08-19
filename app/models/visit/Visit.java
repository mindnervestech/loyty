package models.visit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import play.db.ebean.Model;

@Entity
public class Visit extends Model {

	public Double minimum_amount_for_visit;
	@Embedded
	public List<VisitDetails> visit_details = new ArrayList<VisitDetails>();
}
