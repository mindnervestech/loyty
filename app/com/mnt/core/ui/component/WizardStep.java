package com.mnt.core.ui.component;

import java.util.SortedSet;


public interface WizardStep extends Comparable<WizardStep>{
	
	String name();
	int step();
	SortedSet<FieldType> card();
	
	

}
