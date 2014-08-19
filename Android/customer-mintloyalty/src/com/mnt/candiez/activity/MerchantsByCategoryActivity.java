package com.mnt.candiez.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MerchantsByCategoryActivity extends MenuActivity {
	
	private ListView merchantsList;
	private ProgressDialog dialog;
	private String categoryValue,cityValue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	   setContentView(R.layout.mycitymerchant);
	   final SlideHolder slideHolder= (SlideHolder) findViewById(R.id.slideHolder);
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
	   
	   if (categoryValue != null && cityValue != null) 
	     {    	
	    	dialog.show();
			CityMerchantTask task = new CityMerchantTask();
		    task.execute(); 
	   }
        
	}        
	
	private class CityMerchantTask extends AsyncTask<Void, Void, Void>
	 {
	
		AllCityMerchants response;
	 	
		@Override
		protected Void doInBackground(Void... params)
		{ 			
			String url =Domain.allCityMerchantUrl + "city=" + cityValue + "&category=" + categoryValue;
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
					 showVersionChangeMsg(MerchantsByCategoryActivity.this);
					 return;
				 }
				
				for (AllCityMerchant re : this.response.candies) 
				{
					merchantData.add(re);
				}
				
				MerchantAdapter adapter=new MerchantAdapter(MerchantsByCategoryActivity.this,R.id.name,merchantData);			
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView=convertView;
			try{
				if(rowView==null){
					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.allcitymerchantrow, parent, false);
				}
				
				LinearLayout layoutView = (LinearLayout) rowView.findViewById(R.id.layout6);
				layoutView.removeAllViews();
				TextView nameView = (TextView) rowView.findViewById(R.id.name);
				TextView adressView = (TextView) rowView.findViewById(R.id.adress);
				ImageView caticonImageView = (ImageView) rowView.findViewById(R.id.image);
				ImageView mapiconImageView = (ImageView) rowView.findViewById(R.id.mappin);
				TextView dateView = (TextView) rowView.findViewById(R.id.date);
				TextView amountView = (TextView) rowView.findViewById(R.id.amount);
				
				nameView.setTypeface(font);
				adressView.setTypeface(font);
				amountView.setTypeface(font);
				nameView.setText(getItem(position).merchantName);
				adressView.setText(getItem(position).merchantLocality);
				String imageUrl =Domain.imageUrl+getItem(position).merchantLogo;
				imageDownloader.download(imageUrl, (ImageView) caticonImageView);
				amountView.setText("Rs."+getItem(position).amount+"/"+getItem(position).programType);
				dateView.setText(getDate(getItem(position).validTill));
				
				for(int i=0;i<getItem(position).candiez.size();i++){
					TextView candiez = new TextView(MerchantsByCategoryActivity.this);
					ImageView iconView = new ImageView(MerchantsByCategoryActivity.this);
					LinearLayout View = new LinearLayout(MerchantsByCategoryActivity.this);
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
				System.out.println("Adress -"+getItem(position).merchantLocality);
				
				mapiconImageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							 Intent i = new Intent();
							 i.setClass(MerchantsByCategoryActivity.this,MapActivity.class);
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
						// TODO Auto-generated method stub
						 Intent i = new Intent();
						 i.setClass(MerchantsByCategoryActivity.this,DetailScreenActivity.class);
						 i.putExtra("Offer",jsonOffers);
						 i.putExtra("City-Value",cityValue);
			        	 i.putExtra("Category-Value",categoryValue);
					     startActivity(i);
					     finish();
						
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
		   intent.setClass(MerchantsByCategoryActivity.this,CityCategoryActivity.class);
	       startActivity(intent);
	       finish(); 
	}
	   
   

}
