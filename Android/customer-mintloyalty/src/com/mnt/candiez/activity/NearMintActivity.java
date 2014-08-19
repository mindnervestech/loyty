package com.mnt.candiez.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.google.gson.Gson;
import com.mnt.candiez.data.AllCityMerchant;
import com.mnt.candiez.data.AllCityMerchants;
import com.mnt.candiez.data.Candy;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.ImageDownloader;
import com.mnt.candiez.data.RetrieverJsonData;

public class NearMintActivity extends MenuActivity{	 
	
	private ListView merchantsList;
	private ProgressDialog dialog;
	int distance=2500;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nearmint);
        final SlideHolder slideHolder= (SlideHolder) findViewById(R.id.slideHolder);
        slideHolder.setEnabled(false);
        ImageView menuIcon = (ImageView)findViewById(R.id.menuicon);
 	   	menuIcon.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				slideHolder.toggle();
 			}
 	   	});
        buildCommonScreenContext(getApplication());
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
        final TextView seekBarValue = (TextView)findViewById(R.id.distancemeter);
        merchantsList = (ListView) findViewById(R.id.merchantview); 
        
        seekBar.setProgress(2500);
     
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	   
        	   @Override
        	   public void onStopTrackingTouch(SeekBar seekBar) {
        		   dialog = new ProgressDialog(NearMintActivity.this);
        		   dialog.setMessage("Loading please wait...");
        		   if(dialog!=null){
        			   dialog.show();
        		   }
        		   
       			   GetCategoryTask task = new GetCategoryTask();
       		       task.execute(); 
        	   }
        	   
        	   @Override
        	   public void onStartTrackingTouch(SeekBar seekBar) {
        	   }
        	   
        	   @Override
        	   public void onProgressChanged(SeekBar seekBar, int progress,
        	     boolean fromUser) {
        		     distance= ((int)Math.round(progress/200))*200;
        		     distance=distance+200;
        	         seekBarValue.setText(Integer.toString(distance)+"meter");
        	         seekBar.setProgress(progress);
        		   
        		  }
        	  });
        
        getingStarted();
    } 
    
    
    private class GetCategoryTask extends AsyncTask<Void, Void, Void>
	 {
	
		AllCityMerchants response;
		@Override
		protected Void doInBackground(Void... params)
		{ 			
			
			 try {
		            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		            Location location =lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		            String longitude = Double.toString(location.getLatitude());
		            String latitude = Double.toString(location.getLongitude());

                    //If you put /mobile/merchants-in-proximity/73.901398/18.506048
		           // latitude="73.901398";
		           // longitude="18.506048";
					String url =Domain.nearMeUrl + latitude + "/" + longitude + "/" + Integer.toString(distance);
					RetrieverJsonData<AllCityMerchants> asyncTaskData = new RetrieverJsonData<AllCityMerchants>();
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
				for (AllCityMerchant re : this.response.candies) 
				{
					merchantData.add(re);
				}
				MerchantAdapter adapter=new MerchantAdapter(NearMintActivity.this,R.id.name,merchantData);			
				merchantsList.setAdapter(adapter);
			
			}catch (Exception e) {
				
	           e.printStackTrace();
			}
			
			finally{
				if (dialog != null) { 
					if (dialog.isShowing()){
					dialog.dismiss();
					}
			   }
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
				
				nameView.setText(getItem(position).merchantName);
				adressView.setText(getItem(position).merchantLocality);
				String imageUrl =Domain.imageUrl+getItem(position).merchantLogo;
				imageDownloader.download(imageUrl, (ImageView) caticonImageView);
				amountView.setText("Rs."+getItem(position).amount+"/"+getItem(position).programType);
				dateView.setText(getDate(getItem(position).validTill));
				
				for(int i=0;i<getItem(position).candiez.size();i++){
					TextView candiez = new TextView(NearMintActivity.this);
					ImageView iconView = new ImageView(NearMintActivity.this);
					LinearLayout View = new LinearLayout(NearMintActivity.this);
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
							 i.setClass(NearMintActivity.this,MapActivity.class);
							 i.putExtra("Latitude", mapCordinates[1]);
							 i.putExtra("Longitude", mapCordinates[0]);
				        	 i.putExtra("Adress",adress);
				        	 i.putExtra("Title",title);
				        	 i.putExtra("Near-Mint", true);
						     startActivity(i);
						     finish();
							
						}
				    });
				
				final String jsonOffers = new Gson().toJson(candiez);
				layoutView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						 Intent i = new Intent();
						 i.setClass(NearMintActivity.this,NearDetailActivity.class);
						 i.putExtra("Offer",jsonOffers);
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
    
   
   private void getingStarted(){
	   dialog = new ProgressDialog(NearMintActivity.this);
	   dialog.setMessage("Loading please wait...");
	   if(dialog!=null){
		   dialog.show();
	   }
	   GetCategoryTask task = new GetCategoryTask();
	   task.execute(); 
   }
   
   
   @Override
   public void onBackPressed() {
		  Intent i = new Intent();
		  i.setClass(NearMintActivity.this,CityCategoryActivity.class);
		  startActivity(i);
		  finish();
	}
    

}
