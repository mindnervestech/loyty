package com.mnt.core.helper;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.core.domain.DomainEnum;
import com.mnt.core.ui.annotation.UIFields;
import com.mnt.core.ui.annotation.Validation;
import com.mnt.core.ui.component.FieldType;
import com.mnt.core.ui.component.FieldType.Type;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.ui.component.ValueWrapper;

public  abstract class UIModelHelper<M extends ProxyUI> implements UIModel {

	private Class<? extends ProxyUI> M;
	private ProxyUI valueObject;
	private List<FieldType> fieldTypes = new ArrayList<FieldType>();
	private List<UIButton> uiButtons = new ArrayList<UIButton>();
	private Map<Integer,Set<FieldType>> fieldLayout = new HashMap<Integer,Set<FieldType>>();
	private List<Set<FieldType>> fieldLayoutList =  new ArrayList<Set<FieldType>>();
	protected UIModelHelper(Class<? extends ProxyUI> object) {
		this.M  = object;
	}
	
	protected UIModelHelper(Class<? extends ProxyUI> object,ProxyUI valueObject) {
		this.M  = object;
		this.valueObject = valueObject;
	}
	
	public List<Set<FieldType>> getFieldLayout(){
		return fieldLayoutList;
	}
	

	
	public UIModel build() {
		buildFields();
		buildButtons();
		buildFieldLayout();
		return this;
	}
	
	protected void buildFieldLayout(){
		for(Integer i = 1 ; i<= fieldLayout.size();i++){
			fieldLayoutList.add(fieldLayout.get(i));
		}
		
	}
	
	protected abstract void buildButtons();
	
	protected void buildFields(){
		for(Field f :M.getFields()){
			UIFields fieldUI = null;
			Validation validation =null;
			ValueWrapper value= new ValueWrapper();
			if(valueObject != null){
				try {
					value.o = f.get(valueObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String mappingName = (f.getName());
			if(f.isAnnotationPresent(UIFields.class)){
				fieldUI = ((UIFields)f.getAnnotation(UIFields.class));
			}
			if(f.isAnnotationPresent(Validation.class)){
				validation = ((Validation)f.getAnnotation(Validation.class));
			}
			FieldType fieldType = null;
			
			if(f.getType().isAssignableFrom(String.class)||f.getType().isPrimitive()||
					f.getType().isAssignableFrom(Long.class)
					||f.getType().isAssignableFrom(Float.class)||f.getType().isAssignableFrom(Double.class)
					||f.getType().isAssignableFrom(Integer.class)
					){
				fieldType = (generateFeild(fieldUI,validation,Type.INPUT,null,value,mappingName ));
			}
			
			if(f.getType().isEnum()){
				if(value.o!=null){
					value.display = ((Enum)value.o).name();
				}
				DomainEnum[] e = new DomainEnum[f.getType().getEnumConstants().length];
				for(int i = 0 ; i<e.length;i++){
					e[i] = (DomainEnum) f.getType().getEnumConstants()[i];
				}
				fieldType = (generateFeild(fieldUI,validation,Type.SELECT_OPTION,e,value,mappingName));
			}
			
			if(f.getType().isAssignableFrom(Date.class)){

				SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");
				
				if(value.o !=null){
					String formattedDate = sdf.format((Date)value.o);
					value.display  = formattedDate;
				}
				fieldType = (generateFeild(fieldUI,validation,Type.DATE,null,value,mappingName ));
			}
			
			if(f.getType().isAssignableFrom(Boolean.class)){
				fieldType = (generateFeild(fieldUI,validation,Type.CHECKBOX,null,value,mappingName ));
			}
			
			
			if(f.getType().getSuperclass().getCanonicalName().equals(SearchUIHelper.class.getCanonicalName())){
				try {
					if(valueObject==null){
						value.o = f.getType().newInstance();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				fieldType = (generateFeild(fieldUI,validation,Type.SEARCH,null,value,mappingName ));
			}
			
			
			
			Set<FieldType> colList= fieldLayout.get(fieldType.row());
			if(colList == null ){
				colList = new TreeSet<FieldType>();
			}
			colList.add(fieldType);
			fieldLayout.put(fieldType.row(),colList);
		}
	}
	
	protected <T extends DomainEnum> FieldType generateFeild(final UIFields field, final Validation validation,
			final Type type,final T[] options,final ValueWrapper value, final String nameForMapping){
		return   new FieldType() {
			
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
				return nameForMapping;
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
				return field.row();
			}


			@Override
			public int col() {
				return field.col();
			}


			@Override
			public String cssClass() {
				return field.cssClass();
			}


			@Override
			public String htmlAttrib() {
				
				return field.htmlAttrib();
			}

			
		};
	}

	@Override
	public List<FieldType> getFields() {
		return fieldTypes;
	}

	@Override
	public List<UIButton> getButtonActions() {
		return uiButtons;
	}

	
	
	

}
