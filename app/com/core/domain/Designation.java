package com.core.domain;

import java.util.ArrayList;
import java.util.List;



public enum Designation implements DomainEnum{
	Option("Choose option"),
    SuperAdmin("Super Admin", true),
    Merchant("Merchant",true);
	
	 private String forName;
     private boolean uiHidden = false;
     List<Designation> reportingTo = new ArrayList<Designation>();
    
     public List<Designation> getReportingTo() {
            return reportingTo;
     }
    
     @Override
     public boolean uiHidden() {
            return uiHidden;
     }
    
     private Designation(String forName, Designation ...objects ){
            this.forName=forName;
            for(Designation designation : objects){
            	reportingTo.add(designation);
            }
     }
    
     private Designation(String forName,boolean uiHidden){
            this.forName=forName;
            this.uiHidden = uiHidden;
     }
    
     private Designation(String forName){
            this.forName=forName;
            this.uiHidden = false;
     }
    
     public String getName(){
            return forName;
     }
	
}
