package com.mnt.core.helper;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.OneToOne;
import javax.persistence.Transient;

import play.data.DynamicForm;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.core.domain.DomainEnum;
import com.mnt.core.ui.annotation.SearchFilterOnUI;
import com.mnt.core.ui.annotation.UIFields;
import com.mnt.core.ui.annotation.Validation;
import com.mnt.core.ui.annotation.WizardCardUI;
import com.mnt.core.ui.component.FieldType;
import com.mnt.core.ui.component.FieldType.Type;
import com.mnt.core.ui.component.ProxyModel;
import com.mnt.core.ui.component.UIButton;
import com.mnt.core.ui.component.ValueWrapper;
import com.mnt.core.ui.component.WizardStep;

public abstract class ProxyModelContext<M extends ProxyModel> implements SearchContext {
	protected Class<? extends ProxyModel> ctx;
	protected Map<String,WizardStep> wizards;
	protected ProxyModel model;
	List<UIButton> buttonList = new ArrayList<UIButton>();
	protected ProxyModelContext(Class<? extends ProxyModel> ctx, M model){
		this.ctx = ctx;
		this.model = model;
	}
	
	
	
	
	
	public SearchContext build(){
		buildWizard(ctx);
		buildButton();
		
		return this;
	}
	
	protected abstract void buildButton();
	
	
	@Override 
	public final List<UIButton> getButtonActions(){
		
		return buttonList;
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
	
	
	
	
	
	
	
	protected void buildWizard1(Class<? extends ProxyModel> ctx){
		
	}
	
	
	
	
	protected void buildWizard(Class<? extends ProxyModel> ctx){
		for(Field f :ctx.getFields()){
			if(f.isAnnotationPresent(WizardCardUI.class)||f.isAnnotationPresent(UIFields.class)){
				WizardCardUI wizardCardUI = null;
				UIFields fieldUI = null;
				Validation validation =null;
				ValueWrapper value= new ValueWrapper();
				if(model != null){
					try {
						value.o = f.get(model);
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
					addWizard(wizardCardUI,fieldUI,validation,(f.getName()),
							Type.INPUT,null,value);
				}
				if(f.getType().isAssignableFrom(Date.class)){
					value.dt  = (Date)value.o;
					addWizard(wizardCardUI,fieldUI,validation,(f.getName()),
							Type.DATE,null,value);
				}
				if(f.getType().isEnum()){
					if(value.o!=null){
						value.display = ((Enum)value.o).name();
					}
					DomainEnum[] e = new DomainEnum[f.getType().getEnumConstants().length];
					for(int i = 0 ; i<e.length;i++){
						e[i] = (DomainEnum) f.getType().getEnumConstants()[i];
					}
					addWizard(wizardCardUI,fieldUI,validation,(f.getName())
							,Type.SELECT_OPTION,e,value);
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
	
	
	protected Expression doSearchExpression(DynamicForm form,SearchType searchType){
		List<Expression> expressions = new ArrayList<Expression>();
		
		for(Field f :ctx.getFields()){
			if(f.isAnnotationPresent(SearchFilterOnUI.class) && !f.isAnnotationPresent(Transient.class)){
				String fieldVal = form.get((f.getName()));
				if(f.getType().isEnum()){
					if("All".equals(fieldVal)){
						continue;
					}
				}
				if(fieldVal!=null && fieldVal.length()>0){
					expressions.add( Expr.ilike((f.getName()), "%"+fieldVal+"%"));
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
	
		
	
	
	

}
