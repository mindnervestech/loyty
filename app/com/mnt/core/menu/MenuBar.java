package com.mnt.core.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuBar {
	
	public List<MenuItem> items;
	
	public MenuBar(List<MenuItem> item){
		this.items = item;
	}

	public static class MenuItem {
		
		public String name;
		public List<Role> accessRight;
		public String url;
		public List<MenuItem> subMenu;
		public List<ObjectBehaviour> entiryObject;
		
		public MenuItem(String name, List<Role> roles, String url,List<MenuItem> subMenu,List<ObjectBehaviour> entiryObject){
			this.name = name;
			this.accessRight = roles;
			this.url = url;
			this.subMenu = subMenu;
			this.entiryObject = entiryObject;
		}
		
		public MenuItem(String name, String url){
			this.name = name;
			this.url = url;
		}
		
		public void addSubMenu(String name, String url){
			if(subMenu == null){
				subMenu = new ArrayList<MenuItem>();
			}
			subMenu.add(new MenuItem(name,url));
		}
		
		public boolean isSubMenu(){
			return (subMenu == null || subMenu.size() == 0);
		}
		
		
	}
	
	public static class Role{
		String name;
		public Role(String name){
			this.name = name;
		}
	}
	
	public  static class ObjectBehaviour{
		public Class object;
		public List<String> behavoiur;
	}
}
