package com.mnt.custom.module.merchant.generator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import models.merchant.Merchant;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBQuery.Query;
import play.data.DynamicForm;

import com.google.common.base.Function;
import com.mnt.core.helper.ASearchContext;
import com.mnt.core.ui.component.AddButton;
import com.mnt.core.ui.component.BuildUIButton;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.utils.GridViewModel;
import com.mnt.core.utils.GridViewModel.PageData;
import com.mnt.core.utils.GridViewModel.RowViewModel;

public class MerchantProxyUIContext extends  ASearchContext<Merchant>{

	public static MerchantProxyUIContext getInstance(){
		return new MerchantProxyUIContext();
	}
	
	public MerchantProxyUIContext() {
		super(MerchantProxyUI.class,null);
	}
	
	public MerchantProxyUIContext(Merchant m) {
		super(MerchantProxyUI.class,m);
	}

	@Override
	public String searchUrl() {
		return "/searchMerchant";
	}

	@Override
	public String editUrl() {
		return "/editUrlMerchant";
	}
	
	@Override
	public String showEditUrl() {
		return "/showEditMerchant";
	}
	@Override
	public String createUrl() {
		return "/createMerchant";
	}

	@Override
	public String deleteUrl() {
		return null;
	}

	@Override
	public String entityName() {
		return MerchantProxyUI.ENTITY_NAME;
	}

public GridViewModel doSearch(DynamicForm form) {
		
		//Expression exp =  super.doSearchExpression(form, SearchType.Like);
		int page = Integer.parseInt(form.get("page"));
		int limit = Integer.parseInt(form.get("rows"));
		GridViewModel.PageData pageData = new PageData(limit,page);
		
		String first_name = form.get("firstName");
		String last_name = form.get("lastName");
		String business_name = form.get("businessName");
		String city = form.get("city");
		
		int start = limit*page - limit;
	
		int count_merchant = Merchant.find().count();
	
		if((limit*page) > count_merchant)
		{
			limit = count_merchant;
		}

	// Search Start 
		
		List<Merchant> results= null;
		
		/** Builder Start */
		List<Query> _qs = new ArrayList<Query>();
		
		if(first_name != null){
			Pattern regex = Pattern.compile(first_name+".*",Pattern.CASE_INSENSITIVE);
			_qs.add(DBQuery.regex("firstName", regex));
		}
		
		if(last_name != null){
			Pattern regex = Pattern.compile(last_name+".*",Pattern.CASE_INSENSITIVE);
			_qs.add(DBQuery.regex("lastName", regex));
		}
	
		if(business_name != null){
			Pattern regex = Pattern.compile(business_name+".*",Pattern.CASE_INSENSITIVE);
			_qs.add(DBQuery.regex("businessName", regex));
		}
		if(city != null){
			Pattern regex = Pattern.compile(city+".*",Pattern.CASE_INSENSITIVE);
			_qs.add(DBQuery.regex("city", regex));
		}
	
		Query query =DBQuery.and();
		for(Query q : _qs){
			query = query.and(q);
		}
		
		/** Builder End */		
		if(_qs.isEmpty())
		{
		results = Merchant.find().toArray().subList(start, limit);
	}
	else
	{
		results = Merchant.find().and(query).toArray().subList(start, limit);
	}
// Search End 	 	
		List<GridViewModel.RowViewModel> rows = transform(results, toJqGridFormat()) ;
		GridViewModel gridViewModel = new GridViewModel(pageData, count_merchant, rows);
		return gridViewModel;
	}
	
	private  Function<Merchant,RowViewModel> toJqGridFormat() {
		return new Function<Merchant, RowViewModel>() {
			@Override
			public RowViewModel apply(Merchant customer) {
				try {
					return new GridViewModel.RowViewModel(((customer.id)+""), newArrayList(getResultStr(customer)));
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return null;
			}
		};
	}
	
	
	@Override
	protected void buildButton() {
		super.getButtonActions().add(new UIButton() {
			
			@Override
			public boolean visibility() {
				return true;
			}
			
			@Override
			public String url() {
				return "/resetPwd";
			}
			
			@Override
			public ButtonActionType target() {
				return ButtonActionType.NEW;
			}
			
			@Override
			public String label() {
				return "Reset Password";
			}
			
			@Override
			public String id() {
				return "resetPassword";
			}
		});
	}
	
	@Override
	public UIButton showAddButton(){
		return new AddButton();
	}


	@Override
	public void buildGridButton() {
		//getGridActions().add(BuildGridActionButton.me().withVisibilityTrue().withIcon(GridActionButton.IconType.Tick).withUrl("/userStatusApprove"));
		
	}
	@Override
	public String displayName() {
		return "My Merchants";
	}
}
