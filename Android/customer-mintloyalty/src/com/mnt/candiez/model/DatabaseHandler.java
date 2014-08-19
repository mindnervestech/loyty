package com.mnt.candiez.model;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract.Contacts.Data;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    
    // Database Name
    private static final String DATABASE_NAME = "CustomerManager";
    public static final String KEY_ID = "_id";
    
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    private static final String TABLE_Category =
            "create table if not exists " + "Category" +
            " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name" + " TEXT not null UNIQUE COLLATE NOCASE, " +  //1
            "imageurl" + " TEXT not null, " +  //2
            "appimage" + " TEXT not null);";//3
    
    private static final String TABLE_Candies =
            "create table if not exists " + "Candy" +
            " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "category_Id" + " TEXT not null, " +  //1
            "name" + " TEXT not null, " +  //2
            "description" + " TEXT not null, " +  //3
            "min_amount" + " TEXT not null, " +  //4
            "validTill" + " TEXT not null, " +  //5
            "content" + " TEXT not null);";//6
    
    private static final String TABLE_Merchants =
            "create table if not exists " + "Merchants" +
            " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "category_Id" + " TEXT not null, " +  //1
            "merchantName" + " TEXT not null, " +  //2
            "merchantAddress" + " INTEGER, " +    //3
            "merchantCity" + " TEXT not null, " +//4
            "merchantPin" + " TEXT not null, " + //5
            "imagUrl" + " TEXT not null, " + //6
            "appImage" + " TEXT not null, " +     //7
            "candy_id" + " TEXT not null);";//8
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    
    	db.execSQL(TABLE_Category);
    	db.execSQL(TABLE_Merchants);
    	db.execSQL(TABLE_Candies);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
       
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Category);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Merchants);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Candies);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
  
    public void addItemCategoryTable(String name,String imageurl,String appimage) {
        SQLiteDatabase db = this.getWritableDatabase();
       
        ContentValues values = new ContentValues();
        values.put("name",name); //1
        values.put("imageurl",imageurl);
        values.put("appimage",appimage);
        db.insert("Category", null, values);
        db.close(); // Closing database connection
    }
    
    
    public void addItemMerchantTable(String category_Id,String merchantName,String merchantAddress,String merchantCity,String merchantPin,String imagUrl,String appImage,String candy_id) {
        SQLiteDatabase db = this.getWritableDatabase();
       
        ContentValues values = new ContentValues();
        values.put("category_Id",category_Id); //1
        values.put("merchantName",merchantName);//2
        values.put("merchantAddress",merchantAddress);//3
        values.put("merchantCity",merchantCity); ////4
        values.put("merchantPin",merchantPin);//5
        values.put("imagUrl",imagUrl);//6
        values.put("appImage",appImage);//7
        values.put("candy_id",candy_id);//8
        db.insert("Merchants", null, values);//9
        db.close(); // Closing database connection
    }
    
    
    public void addItemCandiesTable(String category_Id,String name,String description,String min_amount,String validTill,String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_Id",category_Id); //1
        values.put("name",name); //1
        values.put("description",description);//2
        values.put("min_amount",min_amount);//3
        values.put("validTill",validTill); ////4
        values.put("content",content);//5
        db.insert("Candy", null, values);//9
        db.close(); // Closing database connection
    }
    
//    public List<Category> getCategory(){
//    	SQLiteDatabase db = this.getReadableDatabase();
//        // Select All Query
//    	List<Category> data = new ArrayList<Category>();
//        String selectQuery ="SELECT  * FROM " + "Category";
//        Cursor cursor = db.rawQuery(selectQuery, null);        
//        System.out.println("cursor-"+cursor.getCount());
//        if (cursor.moveToFirst()) {
//            do {
//            	
//            	data.add(new Category(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
//           	   
//            } while (cursor.moveToNext());
//        }
//        
//        db.close();
//        if(cursor.getCount()>0){
//         	return data;
//         }
//         else{
//         return null;
//         }
//    	
//    }

   public void deleteTableData(){
	   SQLiteDatabase db = this.getReadableDatabase();
	   db.delete("Category", null, null);
	   db.delete("Candy", null, null);
	   db.delete("Merchants", null, null);
   }
   
 
   
  
}
