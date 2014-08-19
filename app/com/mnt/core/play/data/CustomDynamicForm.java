package com.mnt.core.play.data;

import java.util.Map;

import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Http;

public class CustomDynamicForm<T extends Model> extends Form<T>{

	Map<String,String> extraParams;
	
	public CustomDynamicForm(Class<T> arg0,Map<String,String> extraParams) {
		super(arg0);
		this.extraParams = extraParams;
		
	}
	
	public CustomDynamicForm(Class<T> arg0) {
		super(arg0);
	}

	 protected Map<String,String> requestData(Http.Request request) {
		 Map<String,String> requestData =  super.requestData(request);
		 
		 if(extraParams!=null){
			 for(String key : extraParams.keySet()){
				 if(extraParams.get(key)==null){
					 requestData.remove(key);
				 }else{
					 requestData.put(key, extraParams.get(key));
				 }
			 }
		 }
		 return requestData;
	 }

}
