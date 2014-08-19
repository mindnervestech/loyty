package com.mnt.custom.module.candy.generator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import models.candy.Candy;
import models.merchant.Merchant;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBQuery.Query;

import org.bson.types.ObjectId;

import play.data.DynamicForm;

import com.google.common.base.Function;
import com.mnt.core.helper.ASearchContext;
import com.mnt.core.ui.component.BuildUIButton;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.utils.GridViewModel;
import com.mnt.core.utils.GridViewModel.PageData;
import com.mnt.core.utils.GridViewModel.RowViewModel;

public class CandiesProxyUIContext  extends ASearchContext<Candy>{

	
	public static CandiesProxyUIContext getInstance(){
		return new CandiesProxyUIContext();
	}
	
	public CandiesProxyUIContext() {
		super(CandiesProxyUI.class,null);
	}

	@Override
	public String searchUrl() {
		return "/searchCandies";
	}

	@Override
	public String editUrl() {
		return null;
	}

	@Override
	public String createUrl() {
		return "/createCandy";
	}

	@Override
	public String deleteUrl() {
		return null;
	}

	@Override
	public String entityName() {
		return "Candy";
	}

	@Override
	public String showEditUrl() {
		return null;
	}

	@Override
	public UIButton showAddButton() {
		return BuildUIButton.me().withVisibility(false);
	};
	
	@Override
	public UIButton showEditButton() {
		return BuildUIButton.me().withVisibility(false);
	};
	
	@Override
	protected void buildButton() {
		super.getButtonActions().add(new UIButton() {

			@Override
			public String url() {
				return "/add-candy";
			}

			@Override
			public boolean visibility() {
				return true;
			}

			@Override
			public ButtonActionType target() {
				return ButtonActionType.IMODAL;
			}

			@Override
			public String label() {
				return "New Candy Program";
			}

			@Override
			public String id() {
				return "createCandy";
			}
			
		});
		
		super.getButtonActions().add(new UIButton() {

			@Override
			public String url() {
				return "/showEditCandy";
			}

			@Override
			public boolean visibility() {
				return true;
			}

			@Override
			public ButtonActionType target() {
				return ButtonActionType.MODAL;
			}

			@Override
			public String label() {
				return "Edit Selected";
			}

			@Override
			public String id() {
				return "editCandy";
			}
			
		});
	}

public GridViewModel doSearch(DynamicForm form) {
		
		//Expression exp =  super.doSearchExpression(form, SearchType.Like);
		int page = Integer.parseInt(form.get("page"));
		int limit = Integer.parseInt(form.get("rows"));
	
		String code = form.get("code");
		String name = form.get("name");
		String status = form.get("status");
		
		String email = form.data().get("email");
		GridViewModel.PageData pageData = new PageData(limit,
				page);
		
		Merchant merchant = Merchant.findByUserName(email);
		
		/** Builder Start */
		List<Query> _qs = new ArrayList<Query>();
		
		if(name!=null){
			Pattern regex = Pattern.compile(name+".*",Pattern.CASE_INSENSITIVE);
			_qs.add(DBQuery.regex("name", regex));
		}
		
		if(code!=null){
			Pattern regex = Pattern.compile(code+".*",Pattern.CASE_INSENSITIVE);
			_qs.add(DBQuery.regex("code", regex));
		}
		
		Query query =DBQuery.is("merchant._id", new ObjectId(merchant.id));
		for(Query q : _qs){
			query = query.and(q);
		}
		/** Builder End */
		int count = 0;
		if(status == null)
		{
			count = Candy.find().and(query.greaterThanEquals("valid_till", new Date())).count();
		}
		else if(status.equalsIgnoreCase("Active")){
			count = Candy.find().and(query.greaterThanEquals("valid_till", new Date())).count();
		}
		else if(status.equalsIgnoreCase("Expired")){
			count = Candy.find().and(query.lessThan("valid_till", new Date())).count();
		}
		
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
		
		int start = limit*page - limit;//orderBy(sidx+" "+sord)
		
		if((limit*page) > count)
		{
			limit = count;
		}
		
		List<Candy> results= null;
		if(status == null)
		{
			results = Candy.find().and(query.greaterThanEquals("valid_till", new Date()))
					.toArray().subList(start, limit);
		}
		else if(status.equalsIgnoreCase("Active")){
			results = Candy.find().and(query.greaterThanEquals("valid_till", new Date()))
					.toArray().subList(start, limit);
		}
		else if(status.equalsIgnoreCase("Expired")){
			results = Candy.find().and(query.lessThan("valid_till", new Date()))
					.toArray().subList(start, limit);
		}
		
		else{
			results =  Candy.find().is("merchant._id", new ObjectId(merchant.id)).toArray().subList(start, limit);
		}
		
		List<GridViewModel.RowViewModel> rows = transform(results, toJqGridFormat()) ;
		GridViewModel gridViewModel = new GridViewModel(pageData, count, rows);
		return gridViewModel;
	}

	private  Function<Candy,RowViewModel> toJqGridFormat() {
		return new Function<Candy, RowViewModel>() {
			@Override
			public RowViewModel apply(Candy candy) {
				try {
					return new GridViewModel.RowViewModel(((candy.id)), newArrayList(getResultStr(candy)));
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return null;
			}
		};
	}

	@Override
	public String displayName() {
		return "My Candy Programs";
	}
}