package com.mnt.core.helper;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import play.data.DynamicForm;
import play.db.ebean.Model;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.core.domain.DomainEnum;
import com.mnt.core.ui.annotation.SearchColumn;
import com.mnt.core.ui.annotation.SearchColumnOnUI;
import com.mnt.core.ui.annotation.SearchFilterOnUI;
import com.mnt.core.ui.annotation.UIFields;
import com.mnt.core.ui.annotation.Validation;
import com.mnt.core.ui.annotation.WizardCardUI;
import com.mnt.core.ui.component.AddButton;
import com.mnt.core.ui.component.EditButton;
import com.mnt.core.ui.component.FieldType;
import com.mnt.core.ui.component.FieldType.Type;
import com.mnt.core.ui.component.GridActionButton;
import com.mnt.core.ui.component.ProxyModel;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.ui.component.ValueWrapper;
import com.mnt.core.ui.component.WizardStep;


/*import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
*/
public abstract class ASearchContext<M extends Model> implements SearchContext {
	protected List<FieldType> filter;
	protected Set<SearchColumn> searchColumns;
	protected Class<? extends Model> ctx;
	protected Map<String,WizardStep> wizards;
	protected Map<String,SearchContext> subSearches;
	protected String fieldNamePrefix;
	protected Model model;
	List<UIButton> buttonList = new ArrayList<UIButton>();
	protected ASearchContext(Class<? extends Model> ctx, M model){
		this.ctx = ctx;
		this.model = model;
	}
	
	private List<GridActionButton> gridActionList = new ArrayList<GridActionButton>();
	@Override
	public List<GridActionButton> getGridActions() {
		return gridActionList;
	}
	
	@Override
	public boolean isMultiSelectSearch(){
		return false;
	}
	
	@Override
	public String displayName() {
		return "";
	}
	@Override
	public String generateExcel() {
		return "#";
	}
	
	@Override
	public String onDoubleRowClickCallID(){
		return null;
	}
	public SearchContext build(){
		buildFilter();
		buildGridButton();
		buildColumns();
		buildWizard(ctx);
		buildButton();
		
		return this;
	}
	
	@Override 
	public String entityDisplayName(){
		if(entityName()!=null){
			return entityName().replace("_", " "); 
		}
		return null; 
	}
	
	protected abstract void buildButton();
	
	protected  void buildGridButton(){};
	
	@Override 
	public final List<UIButton> getButtonActions(){
		
		return buttonList;
	}
	
	@Override
	public  SearchContext withFieldNamePrefix(String fieldName){
		fieldNamePrefix = fieldName;
		return this;
	}
	
	@Override
	public UIButton showEditButton(){
		return new EditButton();
	}
	
	@Override
	public UIButton showAddButton(){
		return new AddButton();
	}
	
	
	
	protected void manageOneToOne(){
		manageOneToOne();
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
	
	@Override
	public List<WizardStep> getWizards() {
		if(wizards==null){
			return new  ArrayList<WizardStep>();
		}
		ArrayList<WizardStep> wizardSteps = newArrayList(wizards.values());
		Collections.sort(wizardSteps);
		
		return wizardSteps;
	}
	
	@Override
	public Map<String,SearchContext> getSearchContexts() {
		return subSearches;
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
	
	protected void buildWizard(Class<? extends Model> ctx){
		for(Field f :ctx.getFields()){
			if(f.isAnnotationPresent(WizardCardUI.class)||f.isAnnotationPresent(UIFields.class)){
				WizardCardUI wizardCardUI = null;
				UIFields fieldUI = null;
				Validation validation =null;
				ValueWrapper value= new ValueWrapper();
				if(model != null){
					try {
						
						value.o = model.getClass().getField(f.getName()).get(model);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(f.isAnnotationPresent(Validation.class)){
					validation = ((Validation)f.getAnnotation(Validation.class));
				}
				if(f.isAnnotationPresent(WizardCardUI.class)){
					wizardCardUI = ((WizardCardUI)f.getAnnotation(WizardCardUI.class));
				}
				
				if(f.isAnnotationPresent(UIFields.class)){
					fieldUI = ((UIFields)f.getAnnotation(UIFields.class));
				}
				
				if(f.getType().isAssignableFrom(String.class)||f.getType().isPrimitive()||f.getType().isAssignableFrom(Long.class)
						||f.getType().isAssignableFrom(Float.class)||f.getType().isAssignableFrom(Double.class)
						||f.getType().isAssignableFrom(Integer.class)||
						
						f.isAnnotationPresent(OneToOne.class)){
					
					if(f.isAnnotationPresent(OneToOne.class))
					{
						try 
						{
							if(value.o != null){
								Class clazz = f.getType();
								value.id = clazz.getDeclaredField("id").get(value.o);
							}
						}catch (Exception e)
						{
							e.printStackTrace();
						} 
					}
					addWizard(wizardCardUI,fieldUI,validation,fieldNameWithPrefix(f.getName()),
							Type.INPUT,null,value);
				}
				if(f.getType().isAssignableFrom(Date.class)){
					value.dt  = (Date)value.o;
					addWizard(wizardCardUI,fieldUI,validation,fieldNameWithPrefix(f.getName()),
							Type.DATE,null,value);
				}
				if(f.getType().isAssignableFrom(Blob.class)){
					addWizard(wizardCardUI,fieldUI,validation,fieldNameWithPrefix(f.getName()),
							Type.BLOB,null,value);
				}
				if(f.getType().isEnum()){
					if(value.o!=null){
						value.display = ((Enum)value.o).name();
					}
					DomainEnum[] e = new DomainEnum[f.getType().getEnumConstants().length];
					for(int i = 0 ; i<e.length;i++){
						e[i] = (DomainEnum) f.getType().getEnumConstants()[i];
					}
					addWizard(wizardCardUI,fieldUI,validation,fieldNameWithPrefix(f.getName())
							,Type.SELECT_OPTION,e,value);
				}
				if(f.isAnnotationPresent(ManyToMany.class)){
					Class<?> listClass = null;
					try {
						if(subSearches == null){
							subSearches = new HashMap<String,SearchContext>(); 
						}
						if(f.getType().isAssignableFrom(List.class)){
							ParameterizedType listType = (ParameterizedType)f.getGenericType();
							 listClass = (Class<?>) listType.getActualTypeArguments()[0];
							 subSearches.put(fieldNameWithPrefix(f.getName()), 
									 ((SearchContext)listClass.getMethod("getSearchContext", String.class)
									 .invoke(null, f.getName())).build());
						}
						
						if(value.o!=null){
							for( ProxyModel m : (List<? extends ProxyModel>)value.o){
								if(listClass!=null){
									ValueWrapper innerValue = new ValueWrapper();
									innerValue.o = m; 
										innerValue.id = listClass.getField("id").get(m);
									if(value.li==null){
										value.li = new ArrayList<ValueWrapper>();
									}
										value.li.add(innerValue);	
								}
							}
							
						}
						
					addWizard(wizardCardUI,fieldUI,validation,fieldNameWithPrefix(f.getName()),
							Type.SEARCH,null,value);
					
						
						
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		}
	}
	
	
	protected <T extends DomainEnum> void addWizard(final WizardCardUI wizardCardUI, final UIFields field,final Validation validation ,final String nameForMaping,final FieldType.Type type,
			final T[] options,final ValueWrapper value) {
		if(wizards == null){
			wizards = new HashMap<String,WizardStep>();
		}
		
		final FieldType card = new FieldType() {
			
			@Override
			public int compareTo(FieldType o) {
				return (Integer.valueOf(order()).compareTo(o.order()));
			}
			
			
			@Override
			public int order() {
				return field.order();
			}
			
			@Override
			public String label() {
				return field.label();
			}

			@Override
			public Type ctype() {
				return type;
			}
			
			@Override
			public boolean hidden(){
				return field.hidden();
			}

			@Override
			public String name() {
				return "".equals(field.mappedKey())?nameForMaping:field.mappedKey();
			}
			
			@Override
			public boolean autocomplete(){
				return field.autocomplete();
			}

			@Override
			public DomainEnum[] options() {
				return (options);
			}
			
			@Override
			public String toString() {
				return label();
			}

			@Override
			public boolean isSearchable() {
				return field.searchable();
			}

			@Override
			public ValueWrapper value() {
				return value;
			}


			@Override
			public boolean mandatory() {
				return field.mandatory();
			}


			@Override
			public JQValidation validation() {
				return validation==null?null:new JQValidation() {
					
					@Override
					public boolean required() {
						return validation.required();
					}
					
					@Override
					public int minlength() {
						return validation.minlength();
					}
					
					@Override
					public String messages() {
						return validation.messages();
					}
					
					@Override
					public int maxlength() {
						return validation.maxlength();
					}
					
					@Override
					public boolean email() {
						return validation.email();
					}
					
					@Override
					public boolean date() {
						return validation.date();
					}

					@Override
					public boolean number() {
						return validation.number();
					}

					@Override
					public boolean digits() {
						return validation.digits();
					}

					@Override
					public String remote() {
						return validation.remote();
					}
				};
			}


			@Override
			public int row() {
				// TODO Auto-generated method stub
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
				return field.htmlAttrib();
			}


			

			
		};
		
		
		
		if(wizards.containsKey(wizardCardUI.name()))
		{
			wizards.get(wizardCardUI.name()).card().add(card);
		}
		else
		{
			WizardStep wizardStep = new WizardStep() {
				
				SortedSet<FieldType> fields;
				@Override
				public int step() {
					return wizardCardUI.step();
				}
				
				@Override
				public String name() {
					return wizardCardUI.name();
				}
				
				@Override
				public SortedSet<FieldType> card() {
					if(fields==null){
						fields = new TreeSet<FieldType>();
					}else{
						//fields.add(card);
					}
					return fields;
				}

				@Override
				public int compareTo(WizardStep o) {
					return (Integer.valueOf(step()).compareTo(o.step()));
				}
				
				@Override
				public boolean equals(Object arg0) {
					return name().equals(arg0.toString());
				}
				
				@Override
				public int hashCode() {
					return name().hashCode();
				}
				
				@Override
				public String toString() {
					return name();
				}
			};
			wizardStep.card().add(card);
			wizards.put(wizardCardUI.name(), wizardStep);
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
	
	protected <T extends Model> List<String> getResultStr(T o) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ParseException{
		List<SearchColumn> list = new ArrayList<SearchColumn>();
		list.addAll(searchColumns);
		List<String> resultStr = newArrayList();
		SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
		System.out.println(o);
			for(SearchColumn col :list){
				if(o.getClass().getField(col.name()).get(o) != null){
					if(o.getClass().getField(col.name()).getType().isAssignableFrom(Date.class)){
						Date date = (Date)o.getClass().getField(col.name()).get(o);
						String newDate = formatDate.format(date);
						resultStr.add(newDate);
						
					}
					else if(o.getClass().getField(col.name()).get(o) instanceof DomainEnum){
						resultStr.add(((DomainEnum)o.getClass().getField(col.name()).get(o)).getName());
					}
					else{
						resultStr.add(o.getClass().getField(col.name()).get(o).toString());
					}
				}else{
					resultStr.add("");
				}
			}
			
//			if(gridActionList.size() !=0){
//				for(UIButton uibtn:gridActionList){
//					
//				}
//			}
			resultStr.add(o.getClass().getField("id").get(o).toString());
		return 	resultStr;
		
	}
	/*@Override
	public HSSFWorkbook doExcel(DynamicForm form){
		return null;
	}
	
	public <T extends Model> HSSFWorkbook getExcelExport(List<T> os) throws Exception{
		
		List<SearchColumn> list = new ArrayList<SearchColumn>();
		list.addAll(searchColumns);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet("Report");
		HSSFRow header = sheet.createRow(0);
		HSSFFont font = hssfWorkbook.createFont();
        font.setBoldweight((short) 700);
        HSSFCellStyle headerStyle = hssfWorkbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        for(SearchColumn col :list){
	        HSSFCell cell = header.createCell(col.rank());
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue(col.colName());
        }
        int rowNum = 1;
        for(T o: os){
        	HSSFRow _row = sheet.createRow(rowNum++);
            for(SearchColumn col :list){
    			if(ctx.getField(col.name()).get(o) != null){
    				_row.createCell(col.rank()).setCellValue(ctx.getField(col.name()).get(o).toString());
    			}else{
    				_row.createCell(col.rank()).setCellValue("");
    			}
    			
    		}
        }
        
        for(SearchColumn col :list){
        	sheet.autoSizeColumn(col.rank());
        }
        
        
        return hssfWorkbook;
		
	}*/
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
			public int width() {
				return width;
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
					//return EnumUtils.enumNameToStringArray(options);
					return options;
				}
				
				@Override
				public String label() {
					return label;
				}

				@Override
				public String name() {
					return  name;
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
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public JQValidation validation() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int row() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int col() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public String cssClass() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String htmlAttrib() {
					// TODO Auto-generated method stub
					return null;
				}
			});
		 
		
	}
	
	@Override
	public Map<String, String> autoCompleteUrls() {
		return null;
	}

}
