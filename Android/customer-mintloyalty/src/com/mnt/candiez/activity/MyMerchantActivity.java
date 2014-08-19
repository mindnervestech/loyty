package com.mnt.candiez.activity;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.ImageDownloader;
import com.mnt.candiez.data.Merchant;
import com.mnt.candiez.data.Merchants;
import com.mnt.candiez.data.RetrieverJsonData;
public class MyMerchantActivity extends MenuActivity{
	 
	private ListView merchantsList;
	private ProgressDialog dialog;
	private String mobile;
	private SharedPreferences preferences;
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
        buildCommonScreenContext(getApplication());
        merchantsList = (ListView) findViewById(R.id.merchantview);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mobile = preferences.getString("Mobile", null);
		dialog = new ProgressDialog(this);
	    dialog.setMessage("Loading please wait...");
	    
	    if (mobile != null) 
        {    	
	    	dialog.show();
			GetCategoryTask task = new GetCategoryTask();
		    task.execute(); 
        }
	    
	}        
    
	private class GetCategoryTask extends AsyncTask<Void, Void, Void>
	 {

	 	Merchants response;
		@Override
		protected Void doInBackground(Void... params)
		{ 			
			String url =Domain.domainUrl + Domain.merchantsUrl + mobile;
			RetrieverJsonData<Merchants> asyncTaskData = new RetrieverJsonData<Merchants>();
			try 
			{
				response = asyncTaskData.execute(Merchants.class, url);
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
			List<Merchant> merchantData=new ArrayList<Merchant>();
			try{
				if(response == null) {
				//	Toast.makeText(MainScreenActivity.this, "Not able to get the Results... Try later.", duration).show();
					return;
				}
				for (Merchant re : this.response.merchants) 
				{
					merchantData.add(re);
				}
				
				MerchantAdapter adapter=new MerchantAdapter(MyMerchantActivity.this,R.id.name,merchantData);			
				merchantsList.setAdapter(adapter);
			
			}catch (Exception e) {
				
	           e.printStackTrace();
			}
			finally{
				dialog.dismiss();
			}
			
		}
	    	
	}
	
	
	public class MerchantAdapter extends ArrayAdapter<Merchant>
	{

		private Context context;
		List<Merchant> data;
		private final ImageDownloader imageDownloader = new ImageDownloader();
		private final SharedPreferences.Editor editor = preferences.edit();

		public MerchantAdapter(Context context, int textViewResourceId,
				List<Merchant> dataObject)
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
					rowView = inflater.inflate(R.layout.merchantrow, parent, false);
				}
				LinearLayout layout=(LinearLayout) rowView.findViewById(R.id.layout);
				LinearLayout fulllayoutView = (LinearLayout) rowView.findViewById(R.id.layout2);
				TextView nameView = (TextView) rowView.findViewById(R.id.name);
				TextView adressView = (TextView) rowView.findViewById(R.id.adress);
				ImageView caticonImageView = (ImageView) rowView.findViewById(R.id.image);
				
				nameView.setTypeface(font);
				adressView.setTypeface(font);
				nameView.setText(getItem(position).merchantName);
				adressView.setText(getItem(position).merchantAddress);
				String imageUrl =Domain.imageUrl+getItem(position).merchantImage;
				imageDownloader.download(imageUrl, (ImageView) caticonImageView);
				
				final String id=getItem(position).merchantId;
				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						editor.putString("Merchant-Id",id);
						editor.commit();
						Intent i = new Intent();
		        	    i.setClass(MyMerchantActivity.this,ProgramsActivity.class);
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
		}	 }	
	   
	  @Override
	  public void onBackPressed() {
		  Intent i = new Intent();
		  i.setClass(MyMerchantActivity.this,CityCategoryActivity.class);
		  startActivity(i);
		  finish();
	  }
	
}


