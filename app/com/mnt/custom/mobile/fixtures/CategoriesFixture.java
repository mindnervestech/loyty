package com.mnt.custom.mobile.fixtures;

import java.util.List;

import com.google.common.collect.Lists;
import com.mnt.custom.mobile.data.Category;

public class CategoriesFixture {
		public static List<Category> fixtureDe;
		public static List<Category> fixturePu;
		public static List<Category> fixtureMu;
		
		static {
			fixtureDe = Lists.newArrayList(new Category("Resturant","dining","1"),
					new Category("Night Life","clubing","2"),
					new Category("Coffee Shop","coffee","3"),
					new Category("Bakery/Cakes","cakes","3"),
					new Category("Fast Food","fastfood","5"),
					new Category("Ice Cream Palour","icecream","6"));
			fixturePu = Lists.newArrayList(new Category("Resturant","","1"),
					new Category("Bakery / Coffee Shop","","3"),
					new Category("Saloon / Spa","","5"),
					new Category("Hotels / Motels","","6"));
			fixtureMu = Lists.newArrayList(new Category("Resturant","","1"),
					new Category("Bakery / Coffee Shop","","3"),
					new Category("Saloon / Spa","","5"),
					new Category("Hotels / Motels","","6"),
					new Category("Night Life","","2"),
					new Category("Service","","2"));
		}
}
