package com.mnt.custom.module.customer.generator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import models.customer.Customer;
import models.merchant.Merchant;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBQuery.Query;

import org.bson.types.ObjectId;

import play.data.DynamicForm;

import com.google.common.base.Function;
import com.mnt.core.helper.ASearchContext;
import com.mnt.core.ui.component.BuildUIButton;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.ui.component.UIButton.ButtonActionType;
import com.mnt.core.utils.GridViewModel;
import com.mnt.core.utils.GridViewModel.PageData;
import com.mnt.core.utils.GridViewModel.RowViewModel;

public class CustomerProxyUIContext extends  ASearchContext<Customer>{

	public static CustomerProxyUIContext getInstance(){
		return new CustomerProxyUIContext();
	}
	
	public CustomerProxyUIContext() {
		super(CustomerProxyUI.class,null);
	}
	
	public CustomerProxyUIContext(Customer m) {
		super(CustomerProxyUI.class,m);
	}

	@Override
	public String searchUrl() {
		return "/searchCustomer";
	}

	@Override
	public String editUrl() {
		return "/editUrlCustomer";
	}
	
	@Override
	public String showEditUrl() {
		return "/showEditCustomer";
	}

	@Override
	public String createUrl() {
		return "/createCustomer";
	}

	@Override
	public String deleteUrl() {
		return null;
	}

	@Override
	public String entityName() {
		return CustomerProxyUI.ENTITY_NAME;
	}

public GridViewModel doSearch(DynamicForm form) {
		
		//Expression exp =  super.doSearchExpression(form, SearchType.Like);
		int page = Integer.parseInt(form.get("page"));
		int limit = Integer.parseInt(form.get("rows"));
		GridViewModel.PageData pageData = new PageData(limit,page);
		
		Merchant merchant = Merchant.findByUserName(form.data().get("email"));
		String phoneNo = form.get("phoneNo");
		String first_name = form.get("firstName");
		String last_name = form.get("lastName");
		
		int count = 0;
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
		if(phoneNo != null){
			Pattern phone_regex = Pattern.compile(phoneNo+".*");
			_qs.add(DBQuery.regex("phoneNo", phone_regex));
		}
		_qs.add(DBQuery.is("merchant._id", new ObjectId(merchant.id)));
		
		Query query =DBQuery.and();
		for(Query q : _qs){
			query = query.and(q);
		}
		
		count = Customer.find().and(query).count();
		double min = Double.parseDouble(form.get("rows"));
		int total_pages=0;
		
		if(count > 0){
			total_pages = (int) Math.ceil(count/min);
		}
		else{
			return new GridViewModel(pageData, count, null);
		}
		
		if(page > total_pages){
			page = total_pages;
		}
		
		int start = limit*page - limit;
		
		if((limit*page) > count)
		{
			limit = count;
		}
		List<Customer> results = null;
		results =  Customer.find().and(query).toArray().subList(start, limit);
		List<GridViewModel.RowViewModel> rows = transform(results, toJqGridFormat()) ;
		GridViewModel gridViewModel = new GridViewModel(pageData, count, rows);
		return gridViewModel;
	}
	
	private  Function<Customer,RowViewModel> toJqGridFormat() {
		return new Function<Customer, RowViewModel>() {
			@Override
			public RowViewModel apply(Customer customer) {
				try {
					return new GridViewModel.RowViewModel(((customer.id)), newArrayList(getResultStr(customer)));
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return null;
			}
		};
	}


	@Override
	protected void buildButton() {
		getButtonActions().add(BuildUIButton.me()
				.withVisibilityTrue()
				.withLabel("Assign Stamp")
				.withUrl("/stampit")
				.withTargetModal());
		
		super.getButtonActions().add(new UIButton() {

			@Override
			public String url() {
				return "/analyzeCustomerData";
			}

			@Override
			public boolean visibility() {
				return true;
			}

			@Override
			public ButtonActionType target() {
				return ButtonActionType.NEW;
			}

			@Override
			public String label() {
				return "Analyze Customer";
			}

			@Override
			public String id() {
				return "analyzeCustomer";
			}
			
		});
		
		super.getButtonActions().add(new UIButton() {

			@Override
			public String url() {
				return "/avgExpenditure";
			}

			@Override
			public boolean visibility() {
				return true;
			}

			@Override
			public ButtonActionType target() {
				return ButtonActionType.NEW;
			}

			@Override
			public String label() {
				return "Average Expenditure";
			}

			@Override
			public String id() {
				return "avgExpenditure";
			}
			
		});


	}
	
	
//	@Override
//	public UIButton showEditButton(){
//		return BuildUIButton.me();
//	}
	
	@Override
	public void buildGridButton() {
		//getGridActions().add(BuildGridActionButton.me().withVisibilityTrue().withIcon(GridActionButton.IconType.Tick).withUrl("/userStatusApprove"));
		
	}
	@Override
	public String displayName() {
		return "My Customers";
	}
}
