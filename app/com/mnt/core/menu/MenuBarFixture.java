package com.mnt.core.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.core.domain.Menu;
import com.mnt.core.auth.BasicAuthAction;
import com.mnt.core.menu.MenuBar.MenuItem;

public class MenuBarFixture {
	private static Map<String,MenuItem> map=null ;
	
	public static MenuBar build(String username){
		if(map==null){
			map = new LinkedHashMap<String,MenuItem>();
			for(Menu p :Menu.values()){
				if(p.parent() == null){
					if(!map.containsKey(p.name())){
						map.put(p.name(), new MenuItem(p.display(),  p.url()));
					}
				}else{
					if(map.containsKey(p.parent().name())){
						map.get(p.parent().name()).addSubMenu(p.display(), p.url());
					}
				}
			}
			map.put("Logout",new MenuItem("Logout", null, "/logout", null,null));
		}
		
		List<MenuItem> resultMenu = new ArrayList<MenuItem>();
		
		for(MenuItem mi : map.values()){
			if(!mi.isSubMenu()){
				List<MenuItem> resultSubMenus = new ArrayList<MenuItem>();
				for(MenuItem sm : mi.subMenu){
					if(BasicAuthAction.isInPermission(username, sm.url)){
						resultSubMenus.add(sm);
					}
				}
				 
				if(resultSubMenus.size() != 0){
					MenuItem item = new MenuItem(mi.name, null, mi.url, resultSubMenus, null);
					resultMenu.add(item);
				}
			}
			else{
				if(BasicAuthAction.isInPermission(username, mi.url)){
					resultMenu.add(mi);
				}
			}
		}
		return new MenuBar(resultMenu);
	}

}
