package com.mnt.candiez.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.mnt.candiez.data.Candy;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.ImageDownloader;

public class DetailScreenActivity extends MenuActivity{
	 
	private ListView offerssListView;
	private ProgressDialog dialog;
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.mainscreen);
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
       offerssListView = (ListView) findViewById(R.id.merchantview);
       dialog = new ProgressDialog(this);
	   dialog.setMessage("Loading please wait ...");
       
       if(getIntent().getExtras()!=null){ 
	    	dialog.show();
			GetDetailsTask task = new GetDetailsTask();
		    task.execute(); 
       }
	    
	}        
   
	private class GetDetailsTask extends AsyncTask<Void, Void, Void>
	 {

		List<Candy> candiesData=new ArrayList<Candy>();
		
		@Override
		protected Void doInBackground(Void... params)
		{
			
		 try{
			  
			String jsonOffers=getIntent().getExtras().getString("Offer");
			JSONArray jsonarr = new JSONArray(jsonOffers);
			
		    for(int i = 0; i < jsonarr.length(); i++){					    	
			    JSONObject jsonobj = jsonarr.getJSONObject(i);
			    String descp=jsonobj.getString("candyDescription");
			    String logo=jsonobj.getString("imageUrl");
			    String url=jsonobj.getString("appUrl");
			    int number =jsonobj.getInt("number");
			    candiesData.add(new Candy("",descp,number,logo,url));						    
		    }
		
		 } catch (Exception e) {
				e.printStackTrace();
			}
			 
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
				MerchantAdapter adapter=new MerchantAdapter(DetailScreenActivity.this,R.id.name,candiesData);			
				offerssListView.setAdapter(adapter);
				dialog.dismiss();
		}
	    	
	}
	
	
	public class MerchantAdapter extends ArrayAdapter<Candy>
	{

		private Context context;
		List<Candy> data;
		
		private final ImageDownloader imageDownloader = new ImageDownloader();

		public MerchantAdapter(Context context, int textViewResourceId,
				List<Candy> dataObject)
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
					rowView = inflater.inflate(R.layout.detailscreenrow, parent, false);
				}

				TextView descView = (TextView) rowView.findViewById(R.id.description);
				TextView pointsView = (TextView) rowView.findViewById(R.id.points);
				ImageView caticonImageView = (ImageView) rowView.findViewById(R.id.image);
				
				descView.setTypeface(font);
				pointsView.setTypeface(font);
				
				descView.setText(getItem(position).candyDescription);
				pointsView.setText(Integer.toString(getItem(position).number)+" Point");
				
				String imageUrl =Domain.candyiconUrl+getItem(position).imageUrl;
				imageDownloader.download(imageUrl, (ImageView) caticonImageView);
				
				
			}catch (Exception e) {
		  
		            e.printStackTrace();
		    }
			return rowView;
		}
	 }	

	   
	  @Override
	  public void onBackPressed() {
			 Intent i = new Intent();
		     i.setClass(DetailScreenActivity.this,MerchantsByCategoryActivity.class);
		     i.putExtra("City-Value",getIntent().getExtras().getString("City-Value"));
		     i.putExtra("Category-Value",getIntent().getExtras().getString("Category-Value"));
	         startActivity(i);
	         finish();
		}
	 
	
}

