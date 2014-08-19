package com.mnt.core.auth;

import models.merchant.Merchant;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import views.html.restrictedPage;

import com.core.domain.Designation;
import com.core.domain.Menu;

public class BasicAuthAction extends Action<BasicAuth>{
	public static final String DELIMITER = "|";
	@Override
	public Result call(Context ctx) throws Throwable {
		String userName = ctx.request().username();
		String uri = ctx.request().uri();
		
		if(isInPermission(userName, uri)){
               return delegate.call(ctx);
        }
        return ok(restrictedPage.render());
	}
	
	public static  boolean isInPermission(String username,String uri){
		Merchant user = Merchant.findByUserName(username);
		
		String[] adminPermissions = new String[2];
		adminPermissions[0] = Menu.LoyaltyProgram.name();
		adminPermissions[1] = Menu.RegisterCustomer.name();
		
		String[] merchantPermissions = new String[2];
		merchantPermissions[0] = Menu.ActionItem.name();
		merchantPermissions[1] = Menu.RegisterMerchant.name();
		
		if(user.designation.equals(Designation.SuperAdmin.getName())){
			Menu permissions;
			for(int i=0;i<adminPermissions.length;i++){
				permissions = Menu.valueOf(adminPermissions[i]);
				if(uri.startsWith(permissions.url())){
					return false;
				}
			}
		}
		else if(user.designation.equals(Designation.Merchant.getName())){
			Menu permissions;
			for(int i=0;i<merchantPermissions.length;i++){
				permissions = Menu.valueOf(merchantPermissions[i]);
				if(uri.startsWith(permissions.url())){
					return false;
				}
			}
		}
		return true;
	}
	
	
}
