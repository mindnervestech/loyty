package com.mnt.candiez.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.mnt.candiez.data.Cities;
import com.mnt.candiez.data.City;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.RetrieverJsonData;

public class ChooseCityActvity extends Activity{
	
	private ProgressDialog dialog;
	private ImageView myCityBtn;
	private String cityFromLocal = "1";
	Typeface font;
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.candyhome);  
        myCityBtn=(ImageView) findViewById(R.id.mycity); 
        font = Typeface.createFromAsset(getAssets(), "SegoePrint.ttf");  
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading please wait...");
    
        myCityBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.show();
				GetCityTask  task = new GetCityTask();
		    	task.execute(); 
			}
        });
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String city = preferences.getString("City", null);
        cityFromLocal = preferences.getString("cityFromLocal", "1");
        if (city != null) 
        {
        	((EditText)findViewById(R.id.city)).setText(city);
        }
        
    }
   
   private void navigateCity(){
	   Intent i = new Intent();
	   i.setClass(ChooseCityActvity.this,CityCategoryActivity.class);
       startActivity(i);
       finish();
	   
   }
   private class GetCityTask extends AsyncTask<Void, Void, Void>
	 {
	 
	 	Cities response;
	 	
	 	@Override
	 	protected Void doInBackground(Void... params)
	 	{ 			
	 		String url =Domain.cityUrl;
	 		RetrieverJsonData<Cities> asyncTaskData = new RetrieverJsonData<Cities>();
	 		try 
	 		{
	 			if(!"1".equals(cityFromLocal)) {
	 				response = asyncTaskData.execute(Cities.class, url);
	 			} else {
	 				response = new Cities();
	 				response.cities.add(new City("Delhi", "Delhi/NCR"));
	 				response.cities.add(new City("Pune", "Pune"));
	 				response.cities.add(new City("Mumbai", "Mumbai"));
	 				response.cities.add(new City("Bangalore", "Bangalore"));
	 				response.cities.add(new City("Chandighar", "Chandighar"));
	 				response.cities.add(new City("Hyderabad", "Hyderabad"));
	 				response.cities.add(new City("Chennai", "Chennai"));
	 				response.cities.add(new City("Kolkata", "Kolkata"));
	 				response.cities.add(new City("Bhubneshwar", "Bhubneshwar"));
	 				response.cities.add(new City("Ahmedabad", "Ahmedabad"));
	 				response.cities.add(new City("Surat", "Surat"));
	 				response.cities.add(new City("Jaipur", "Jaipur"));
	 				
	 			}
	 		} 
	 		catch (Exception e)
	 		{
	 			e.printStackTrace();
	 		}
	 		return null;
	 	}
	 	
	 	@Override
	 	protected void onPostExecute(Void result)
	 	{
	 		List<City> cityData=new ArrayList<City>();
	 		List<String> cityDataTemp=new ArrayList<String>();
	 		try{
	 			for (City re : this.response.cities) 
	 			{
	 				cityData.add(re);
	 				cityDataTemp.add(re.text);					
	 			}
	 			
	 			CharSequence[] cs = cityDataTemp.toArray(new CharSequence[cityDataTemp.size()]);
	 			cityPopup(cs,cityData);
	 		}catch (Exception e) {
	 			
	           e.printStackTrace();
	 		}
	 		finally{
	 			dialog.dismiss();
	 		}
	 		
	 	}
	    	
	 }
   
  private void cityPopup(final CharSequence[] City_options,final List<City> cityDataTemp){
	  
	      final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	      int selectedIndex=getCityIndex(City_options,preferences.getString("City", null));
	 	  AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	      alt_bld.setTitle("Select your city");
	      alt_bld.setSingleChoiceItems(City_options, selectedIndex, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int item) {
	        	  ((EditText)findViewById(R.id.city)).setText(City_options[item]);
	        	 
	           	  final SharedPreferences.Editor editor = preferences.edit();
	           	  editor.putString("City",City_options[item].toString());
	           	  for(int i=0;i<cityDataTemp.size();i++){        		  
	           		  if(cityDataTemp.get(i).text.equalsIgnoreCase(City_options[item].toString())){
	           			 editor.putString("City-Value",cityDataTemp.get(i).value);
	           			 break;
	           		  }
	           	  }
	 			  editor.commit();
	        	  dialog.dismiss();
	        	  navigateCity();
	          }
	      });
	      AlertDialog alert = alt_bld.create();
	      alert.show();
	  
  } 
  
  
  private int getCityIndex(final CharSequence[] City_options,String selectedCity){
	  
	  int index=-1;
	  for(int i=0;i<City_options.length;i++){        		  
   		  if(City_options[i].toString().equalsIgnoreCase(selectedCity)){
   			 index=i;
   			 break;
   		  }
   	  }
	  
	  return index;
  }
	    
}

