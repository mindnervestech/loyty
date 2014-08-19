package com.mnt.custom.module.candy.generator;

import java.util.Map;

import models.candy.Candy;
import play.data.Form;
import play.db.ebean.Model;

import com.mnt.core.helper.SaveModel;

public class CandySave extends SaveModel<Candy> {

	public CandySave() {
		ctx = Candy.class;
	}
	
	public CandySave(Map<String,String> extraMap) {
		ctx = Candy.class;
		extraParams = extraMap;
	}
	
	
	@Override
	protected void preSave(Form<? extends Model> form) throws Exception {
		
		super.preSave(form);
	}

}
