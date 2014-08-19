package com.mnt.core.ui.annotation;


public interface SearchColumn extends Comparable<SearchColumn> {
	int rank();
	String colName();
	String name();
	int width();
}
