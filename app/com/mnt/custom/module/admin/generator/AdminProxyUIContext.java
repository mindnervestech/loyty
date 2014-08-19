package com.mnt.custom.module.admin.generator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.util.List;

import models.merchant.Merchant;
import play.data.DynamicForm;

import com.google.common.base.Function;
import com.mnt.core.helper.ASearchContext;
import com.mnt.core.ui.component.BuildUIButton;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.utils.GridViewModel;
import com.mnt.core.utils.GridViewModel.PageData;
import com.mnt.core.utils.GridViewModel.RowViewModel;

public class AdminProxyUIContext extends  ASearchContext<Merchant>{

	public static AdminProxyUIContext getInstance(){
		return new AdminProxyUIContext();
	}
	
	public AdminProxyUIContext() {
		super(AdminProxyUI.class,null);
	}
	
	public AdminProxyUIContext(Merchant m) {
		super(AdminProxyUI.class,m);
	}

	@Override
	public String searchUrl() {
		return "/searchAdminMerchant";
	}

	@Override
	public String editUrl() {
		return "/editUrlAdminMerchant";
	}
	
	@Override
	public String showEditUrl() {
		return "/showEditAdminMerchant";
	}
	@Override
	public String createUrl() {
		return "/createAdminMerchant";
	}

	@Override
	public String deleteUrl() {
		return null;
	}

	@Override
	public String entityName() {
		return AdminProxyUI.ENTITY_NAME;
	}

public GridViewModel doSearch(DynamicForm form) {
		
		//Expression exp =  super.doSearchExpression(form, SearchType.Like);
		int page = Integer.parseInt(form.get("page"));
		int limit = Integer.parseInt(form.get("rows"));
		GridViewModel.PageData pageData = new PageData(limit,
				page);
		
		//Pagination of Grid 
		int start = limit*page - limit;
		
		int count_merchant = Merchant.find().count();
		
		if((limit*page) > count_merchant)
		{
			limit = count_merchant;
		}
		
		String status = form.get("status");
		List<Merchant> results= null;
		
		if(status == null || status.isEmpty())
		{
			results =  Merchant.find().is("status", "Pending").toArray();
		}
		else if(status.equals("Approved")){
			results =  Merchant.find().is("status", status).toArray();
		}
		else if(status.equals("Pending")){
			results =  Merchant.find().is("status", status).toArray();
		}
		else {
			results =  Merchant.find().is("status", status).toArray();
		}	
		
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
    public void buildButton() {
		
			super.getButtonActions().add(new UIButton() {
	              @Override
	              public boolean visibility() {
	                    return true;
	              }
	              @Override
	              public String url() {
	                    return "/approve";
	              }
	             
	              @Override
	              public ButtonActionType target() {
	                    return ButtonActionType.ACTION;
	              }
	             
	              @Override
	              public String label() {
	                    return "Approve";
	              }
	             
	              @Override
	              public String id() {
	                    return "companyApproveButton";
	              }
	           });
			super.getButtonActions().add(new UIButton() {
				
	            @Override
	            public boolean visibility() {
	                  return true;
	            }
	            @Override
	            public String url() {
	                  return "/disapprove";
	            }
	           
	            @Override
	            public ButtonActionType target() {
	                  return ButtonActionType.ACTION;
	            }
	           
	            @Override
	            public String label() {
	                  return "Disapprove";
	            }
	           
	            @Override
	            public String id() {
	                  return "companyDisapproveButton";
	            }
	         });
}
	
	@Override
	public UIButton showEditButton(){
		return 	BuildUIButton.me();
	}
	
	@Override
	public UIButton showAddButton(){
		return BuildUIButton.me();
	}

	@Override
	public void buildGridButton() {
		//getGridActions().add(BuildGridActionButton.me().withVisibilityTrue().withIcon(GridActionButton.IconType.Tick).withUrl("/userStatusApprove"));
		
	}
	@Override
	public String displayName() {
		return "My Actions";
	}
}
