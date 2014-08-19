package com.mnt.candiez.activity;
import java.util.ArrayList;
import java.util.List;

import com.agimind.widget.SlideHolder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;



public class CityCategoryActivity  extends MenuActivity{
	 
	private ProgressDialog dialog;
	private String cityValue;
	private GridView merchantsList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.city);
        final SlideHolder slideHolder= (SlideHolder) findViewById(R.id.slideHolder);
        slideHolder.setEnabled(false);
        ImageView menuIcon = (ImageView)findViewById(R.id.menuicon);
 	   	menuIcon.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				slideHolder.toggle();
 			}
 	   	});
        buildCommonScreenContext(this);
        merchantsList = (GridView) findViewById(R.id.merchantview);
        
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading please wait...");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String city = preferences.getString("City", null);
        cityValue = preferences.getString("City-Value", null);
        
        if (city != null || cityValue != null) 
        {
        	dialog.show();
        	GetCategoryTask  task = new GetCategoryTask();
	    	task.execute(); 
        }else{
        	Toast.makeText(this,"No Result",Toast.LENGTH_SHORT).show();	
        }
       
    }
    
    
      
    private class GetCategoryTask extends AsyncTask<Void, Void, Void>
	 {
		
		@Override
		protected Void doInBackground(Void... params)
		{ 
			dialog.dismiss();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);	
			List<String> categoryData=new ArrayList<String>();
			try{
			     categoryData.add("resturants");
			     categoryData.add("service");
			     categoryData.add("cafe");
			     categoryData.add("bakes");
			     categoryData.add("apparel");
			     categoryData.add("departmental");
			     categoryData.add("care");
			     categoryData.add("others");
				 MerchantAdapter adapter=new MerchantAdapter(CityCategoryActivity.this,R.id.name,categoryData);			
				 merchantsList.setAdapter(adapter);
			   } catch (Exception e) {
			      e.printStackTrace();
			 }
			   				 
				dialog.dismiss();	  
		}
	    	
	}
    
    public class MerchantAdapter extends ArrayAdapter<String>
	{

		private Context context;
		List<String> data;
		
		public MerchantAdapter(Context context, int textViewResourceId,
				List<String> dataObject)
		{
			super(context, textViewResourceId, dataObject);
			this.context=context;
			this.data=dataObject;
	    }
		  
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView=convertView;
			try{
				
				if(rowView==null){
					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.categorybycityrow, parent, false);
				}
				
				LinearLayout layout=(LinearLayout) rowView.findViewById(R.id.layout);
				ImageView iconView = (ImageView) rowView.findViewById(R.id.image);
				
			    final String iconValue = getItem(position);
				iconView.setImageResource(getResources().getIdentifier(iconValue, "drawable", getPackageName()));
				
				
				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent();
		        	    i.setClass(CityCategoryActivity.this,MerchantsByCategoryActivity.class);
		        	    i.putExtra("City-Value",cityValue);
		        	    i.putExtra("Category-Value",iconValue);
		                startActivity(i);
		                finish();
					}
		        });
				
				
			}catch (Exception e) {
		        //	ExceptionNotifier.sendException(e,FinderActivity.this);
		            Log.e("Error", e.getMessage());
		            e.printStackTrace();
		    }
			return rowView;
		}
	 }	
   
  @Override
  public void onBackPressed() {
       finish(); 
	}
  
 
      
}
