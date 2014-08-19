package com.mnt.core.ui.component;

public class AutoComplete {
	
	public AutoComplete(String _1, String _2, String _3, long id) {
		this._1 = _1!=null?_1:"";
		this._2 = _2!=null?_2:"";
		this._3 = _3!=null?"("+_3+")":"";
		this.id = id;
	}
	public String _1;
	public String _2;
	public String _3;
	public long id;
	

}
