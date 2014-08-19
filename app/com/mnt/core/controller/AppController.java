package com.mnt.core.controller;

import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public abstract class AppController extends Controller{

	public class Secured extends Security.Authenticator {

	    @Override
	    public String getUsername(Context ctx) {
	    	if(checkIdentity(ctx.session().get("email"))){
	    		return ctx.session().get("email");
	    	}
	    	return null;
	    }

	    @Override
	    public Result onUnauthorized(Context ctx) {
	        return redirect(loginURL());
	    }
	}

	protected abstract boolean checkIdentity(String string);
	protected abstract String loginURL();

}
