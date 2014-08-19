package com.mnt.core.helper;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Transient;

import play.data.DynamicForm;
import play.db.ebean.Model;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.Query;
import com.core.domain.DomainEnum;
import com.mnt.core.ui.annotation.SearchColumn;
import com.mnt.core.ui.annotation.SearchColumnOnUI;
import com.mnt.core.ui.annotation.SearchFilterOnUI;
import com.mnt.core.ui.component.FieldType;
import com.mnt.core.ui.component.FieldType.Type;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.ui.component.ValueWrapper;

public abstract class SearchUIHelper implements SearchUI {
	protected List<FieldType> filter;
	protected Set<SearchColumn> searchColumns;
	protected Class ctx ;
	protected Map<String,SearchContext> subSearches;
	protected String fieldNamePrefix;
	
	protected	SearchUIHelper(Class ctx){
		this.ctx = ctx;
	}
	
	private List<UIButton> gridActionList = new ArrayList<UIButton>();
	@Override
	public List<UIButton> getGridActions() {
		return gridActionList;
	}
	
	public SearchUI build(){
		buildFilter();
		buildColumns();
		buildButton();
		return this;
	}
	
	private List<UIButton> buttonList = new ArrayList<UIButton>();
	@Override 
	public List<UIButton> getButtonActions(){
		
		return buttonList;
	}
	
	@Override
	public boolean isMultiSelectSearch(){
		return false;
	}
	
	protected abstract void buildButton();
	
	@Override
	public  SearchUIHelper withFieldNamePrefix(String fieldName){
		fieldNamePrefix = fieldName;
		return this;
	}
	
	private String fieldNameWithPrefix(String fieldName){
		if(fieldNamePrefix==null){
			return fieldName;
		}else{
			return fieldNamePrefix+"."+fieldName;
		}
	}
	
	@Override
	public List<FieldType> getFilter() {
		return filter;
	}
	
	
	protected void buildFilter(){
		for(Field f :ctx.getFields()){
			if(f.isAnnotationPresent(SearchFilterOnUI.class)){
				String label = ((SearchFilterOnUI)f.getAnnotation(SearchFilterOnUI.class)).label();
				
				if(f.getType().isAssignableFrom(String.class)||f.getType().isPrimitive()){
					addFilters(Type.INPUT, label,((f.getName())), null);
				}
				if(f.getType().isAssignableFrom(Date.class)){
					addFilters(Type.DATE, label,((f.getName())), null);
				}
				
				if(f.getType().isEnum()){
					DomainEnum[] e = new DomainEnum[f.getType().getEnumConstants().length];
					for(int i = 0 ; i<e.length;i++){
						e[i] = (DomainEnum) f.getType().getEnumConstants()[i];
					}
					addFilters(Type.SELECT_OPTION, label,((f.getName())), e);
				}
			}
		}
	}
	
	
	
	
	
	
	protected void buildColumns(){
		for(Field f :ctx.getFields()){
			if(f.isAnnotationPresent(SearchColumnOnUI.class)){
				String label = ((SearchColumnOnUI)f.getAnnotation(SearchColumnOnUI.class)).colName();
				int rank = ((SearchColumnOnUI)f.getAnnotation(SearchColumnOnUI.class)).rank();
				int width = ((SearchColumnOnUI)f.getAnnotation(SearchColumnOnUI.class)).width();
				
				if(f.getType().isAssignableFrom(String.class)||f.getType().isPrimitive()||f.getType().isEnum()
						||f.getType().isAssignableFrom(Long.class)
						||f.getType().isAssignableFrom(Float.class)||f.getType().isAssignableFrom(Double.class)
						||f.getType().isAssignableFrom(Integer.class)||
						f.getType().isAssignableFrom(Date.class)){
					addColumns(rank, label,(f.getName()),width);
				}
				
			}
		}
	}
	
	protected Query<? extends Model> getQuery(DynamicForm form,SearchType searchType){
		Query<? extends Model> q = null;
		for(Field f :ctx.getFields()){
//			if(f.isAnnotationPresent(SearchFilterOnUI.class) && !f.isAnnotationPresent(Transient.class)){
//				if(((SearchFilterOnUI)f.getAnnotation(SearchFilterOnUI.class)).rootBean()){
//					q = Ebean.find(((SearchFilterOnUI)f.getAnnotation(SearchFilterOnUI.class)).model());
//					break;
//				}
//				
//			}
		}
		
		for(Field f :ctx.getFields()){
//			if(f.isAnnotationPresent(SearchFilterOnUI.class) && !f.isAnnotationPresent(Transient.class)){
//				String modelAttr = ((SearchFilterOnUI)f.getAnnotation(SearchFilterOnUI.class)).modelAttibute();
//				if(!((SearchFilterOnUI)f.getAnnotation(SearchFilterOnUI.class)).rootBean() && modelAttr!=null){
//					q=q.fetch(modelAttr.substring(0,modelAttr.lastIndexOf(".")));
//				}
//			}
		}
		return q;
		
	}
	
	protected void mapToContextList(DynamicForm form,SearchType searchType) throws Exception{
		Expression exp =  doSearchExpression(form, SearchType.Like);
		List<? extends Model> l = getQuery(form, SearchType.Like).where(exp).findList();
		for(Model m : l){
			Model m1 = (Model)ctx.newInstance();
			for(Field f :ctx.getFields()){
				//String modelAttr = ((SearchFilterOnUI)f.getAnnotation(SearchFilterOnUI.class)).modelAttibute();
				//f.set(m1, ReflectUtils.getInstance().getFieldValue(m, modelAttr));
			}
		}
		
	}
	
	protected Expression doSearchExpression(DynamicForm form,SearchType searchType){
		List<Expression> expressions = new ArrayList<Expression>();
		
		for(Field f :ctx.getFields()){
			if(f.isAnnotationPresent(SearchFilterOnUI.class) && !f.isAnnotationPresent(Transient.class)){
				String fieldVal = form.get(fieldNameWithPrefix(f.getName()));
				if(f.getType().isEnum()){
					if("All".equals(fieldVal)){
						continue;
					}
				}
				if(fieldVal!=null && fieldVal.length()>0){
					expressions.add( Expr.ilike(fieldNameWithPrefix(f.getName()), "%"+fieldVal+"%"));
				}
			}
		}
		
		Expression exp=null;
		if(expressions.size()!=0)
		{
			exp = expressions.get(0);
			for(int i =1;i<expressions.size();i++)
			{
				exp = Expr.and(exp, expressions.get(i));
			}
		}
		
		return exp;
		
	}
	
	protected <T extends Model> List<String> getResultStr(T o) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		List<SearchColumn> list = new ArrayList<SearchColumn>();
		list.addAll(searchColumns);
		List<String> resultStr = newArrayList();
		Class ctx = o.getClass();
		for(SearchColumn col :list){
			if(ctx.getField(col.name()).get(o) != null){
				resultStr.add(ctx.getField(col.name()).get(o).toString());
			}
		}
		return 	resultStr;
		
	}
	
	@Override
	public List<SearchColumn> getColumn() {
		List<SearchColumn> list = new ArrayList<SearchColumn>();
		list.addAll(searchColumns);
		return list;
	};
	
	protected void addColumns(final int rank,final String colName,final String name, final int width){
		if(searchColumns == null){
			searchColumns = new TreeSet<SearchColumn>();
		 }
		
		searchColumns.add(new SearchColumn() {
			
			@Override
			public int rank() {
				return rank;
			}
			
			@Override
			public String colName() {
				return colName;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public int compareTo(SearchColumn o) {
				return Integer.valueOf(rank()).compareTo(o.rank());
			}
			
			public String toString(){
				return colName;
			}

			@Override
			public int width() {
				return width;
			}
		});
	}
	
	protected <T extends DomainEnum> void  addFilters(final Type type,final String label,final String name,
			final T[] options){
		 
		if(filter == null){
			 filter = new ArrayList<FieldType>();
		 }
		 
			 filter.add(new FieldType() {
				
				@Override
				public Type ctype() {
					return type;
				}
				
				@Override
				public boolean isSearchable() {
					return false;
				}
				
				@Override
				public DomainEnum[] options() {
					return (options);
				}
				
				@Override
				public String label() {
					return label;
				}

				@Override
				public String name() {
					return name;
				}

				@Override
				public int compareTo(FieldType o) {
					return 0;
				}
				
				@Override
				public boolean hidden() {
					return false;
				}

				@Override
				public int order() {
					return 0;
				}

				@Override
				public boolean autocomplete() {
					return false;
				}

				@Override
				public ValueWrapper  value() {
					return null;
				}

				@Override
				public boolean mandatory() {
					return false;
				}

				@Override
				public JQValidation validation() {
					return null;
				}

				@Override
				public int row() {
					return 0;
				}

				@Override
				public int col() {
					return 0;
				}

				@Override
				public String cssClass() {
					return null;
				}

				@Override
				public String htmlAttrib() {
					return null;
				}
			});
		 
		
	}
	
}
