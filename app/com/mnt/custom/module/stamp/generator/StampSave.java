package com.mnt.custom.module.stamp.generator;

import java.util.Map;

import models.stamp.Stamp;
import play.data.Form;
import play.db.ebean.Model;

import com.mnt.core.helper.SaveModel;

public class StampSave extends SaveModel<Stamp> {

	public StampSave() {
		ctx = Stamp.class;
	}
	
	public StampSave(Map<String,String> extraMap) {
		ctx = Stamp.class;
		extraParams = extraMap;
	}
	
	
	@Override
	protected void preSave(Form<? extends Model> form) throws Exception {
		
		super.preSave(form);
	}

}
