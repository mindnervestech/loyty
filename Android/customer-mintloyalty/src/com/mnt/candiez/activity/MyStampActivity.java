package com.mnt.candiez.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.google.gson.Gson;
import com.mnt.candiez.data.AllCityMerchant;
import com.mnt.candiez.data.AllCityMerchants;
import com.mnt.candiez.data.Candy;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.ImageDownloader;
import com.mnt.candiez.data.RetrieverJsonData;

public class MyStampActivity extends MenuActivity {
	
	private ListView merchantsList;
	private ProgressDialog dialog;
	private String categoryValue,cityValue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	   setContentView(R.layout.mycitymerchant);
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
	   merchantsList = (ListView) findViewById(R.id.merchantview); 
	   dialog = new ProgressDialog(this);
	   dialog.setMessage("Loading please wait...");
	   
	   if(getIntent().getExtras()!=null){ 
		   categoryValue=getIntent().getExtras().getString("Category-Value");
		   cityValue=getIntent().getExtras().getString("City-Value");
	   }
	   
	       	
	    	dialog.show();
			StampsDetailsTask task = new StampsDetailsTask();
		    task.execute(); 
	   
        
	}        
	
	private class StampsDetailsTask extends AsyncTask<Void, Void, Void>
	 {
	
		AllCityMerchants response;
	 	
		@Override
		protected Void doInBackground(Void... params)
		{ 			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		    String mobile = preferences.getString("Mobile", null);
			String url =Domain.myStamps  + mobile ;
			RetrieverJsonData<AllCityMerchants> asyncTaskData = new RetrieverJsonData<AllCityMerchants>();
			try 
			{
				response = asyncTaskData.execute(AllCityMerchants.class, url);
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
			List<AllCityMerchant> merchantData=new ArrayList<AllCityMerchant>();
			try{
				if(response == null) {
				//	Toast.makeText(MainScreenActivity.this, "Not able to get the Results... Try later.", duration).show();
					return;
				}
				 if(needUpdateVersion(this.response.androidversion)){
					 showVersionChangeMsg(MyStampActivity.this);
					 return;
				 }
				
				for (AllCityMerchant re : this.response.candies) 
				{
					merchantData.add(re);
				}
				
				MerchantAdapter adapter=new MerchantAdapter(MyStampActivity.this,R.id.name,merchantData);			
				merchantsList.setAdapter(adapter);
			
			}catch (Exception e) {
				
	           e.printStackTrace();
			}
			finally{
				dialog.dismiss();
			}
			
		}
	    	
	}
	
	
	public class MerchantAdapter extends ArrayAdapter<AllCityMerchant>
	{
	
		private Context context;
		List<AllCityMerchant> data;
		private final ImageDownloader imageDownloader = new ImageDownloader();
		public MerchantAdapter(Context context, int textViewResourceId,
				List<AllCityMerchant> dataObject)
		{
			super(context, textViewResourceId, dataObject);
			this.context=context;
			this.data=dataObject;
	    }
		  
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View rowView=convertView;
			try{
				if(rowView==null){
					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.mystampsmerchantrow, parent, false);
				}
				
				LinearLayout layoutView = (LinearLayout) rowView.findViewById(R.id.layout6);
				layoutView.removeAllViews();
				TextView nameView = (TextView) rowView.findViewById(R.id.name);
				TextView adressView = (TextView) rowView.findViewById(R.id.adress);
				ImageView caticonImageView = (ImageView) rowView.findViewById(R.id.image);
				ImageView mapiconImageView = (ImageView) rowView.findViewById(R.id.mappin);
				ImageView yourStampImageView = (ImageView) rowView.findViewById(R.id.myImageView);
				TextView dateView = (TextView) rowView.findViewById(R.id.date);
				TextView amountView = (TextView) rowView.findViewById(R.id.amount);
				TextView howManyView = (TextView) rowView.findViewById(R.id.howmany);
				
				nameView.setTypeface(font);
				adressView.setTypeface(font);
				amountView.setTypeface(font);
				howManyView.setTypeface(font);
				nameView.setText(getItem(position).merchantName);
				adressView.setText(getItem(position).merchantLocality);
				String imageUrl =Domain.imageUrl+getItem(position).merchantLogo;
				imageDownloader.download(imageUrl, (ImageView) caticonImageView);
				amountView.setText("Rs."+getItem(position).amount+"/"+getItem(position).programType);
				if(getItem(position).programType.equalsIgnoreCase("Point")){
					yourStampImageView.setImageResource(R.drawable.your_points);
				}
				
				if(getItem(position).programType.equalsIgnoreCase("Visit")){
					yourStampImageView.setImageResource(R.drawable.your_visits);
				}
				
				howManyView.setText(""+(int)getItem(position).howMany);
				dateView.setText(getDate(getItem(position).validTill));
				
				for(int i=0;i<getItem(position).candiez.size();i++){
					TextView candiez = new TextView(MyStampActivity.this);
					ImageView iconView = new ImageView(MyStampActivity.this);
					LinearLayout View = new LinearLayout(MyStampActivity.this);
					View.setOrientation(LinearLayout.HORIZONTAL);
					candiez.setText("  "+getItem(position).candiez.get(i).candyDescription);
					iconView.setImageResource(R.drawable.tick);
					candiez.setTextSize(17);
	                candiez.setPadding(0, 5, 0, 0);
					candiez.setTextColor(Color.WHITE);
					View.addView(iconView);
					View.addView(candiez);
					layoutView.addView(View);
				
				}
				 
				final double[] mapCordinates=getItem(position).merchantPosition;
				final List<Candy> candiez=getItem(position).candiez;
				final String adress=getItem(position).merchantLocality;
				final String title=getItem(position).merchantName;
				
				mapiconImageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							 Intent i = new Intent();
							 i.setClass(MyStampActivity.this,MapActivity.class);
							 i.putExtra("Latitude", mapCordinates[1]);
							 i.putExtra("Longitude", mapCordinates[0]);
							 i.putExtra("City-Value",cityValue);
				        	 i.putExtra("Category-Value",categoryValue);
				        	 i.putExtra("Adress",adress);
				        	 i.putExtra("Title",title);
						     startActivity(i);
						     finish();
							
						}
				    });
				
				final String jsonOffers = new Gson().toJson(candiez);
				layoutView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
					    if(getItem(position).programType.equalsIgnoreCase("Point")){
								navigateToPoint(getItem(position).programId);
						}
							
						if(getItem(position).programType.equalsIgnoreCase("Visit")){
								navigateToVisit(getItem(position).programId);
						}	
						
					}
			    });
				
				
				
			
			}catch (Exception e) {
		            e.printStackTrace();
		    }
			return rowView;
		}
	}	
	   
   @Override
   public void onBackPressed() {
		   Intent intent = new Intent();
		   intent.setClass(MyStampActivity.this,CityCategoryActivity.class);
	       startActivity(intent);
	       finish(); 
	}
	   
   private void navigateToVisit(String id){
	    Intent intent = new Intent();
		intent.setClass(this,MerchantProgramActivity.class);  
		intent.putExtra("programid", id);
		startActivity(intent);
		finish(); 
		 
	 }
	 
	private void navigateToPoint(String id){
		Intent intent = new Intent();
		intent.setClass(this,PointActivity.class);  
		intent.putExtra("programid", id);
		startActivity(intent);
		finish(); 
	 }

}
