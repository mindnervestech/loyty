package com.mnt.candiez.activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agimind.widget.SlideHolder;
import com.mnt.candiez.data.Candiez;
import com.mnt.candiez.data.Candy;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.ImageDownloader;
import com.mnt.candiez.data.Programs;
import com.mnt.candiez.data.RetrieverJsonData;
import com.mnt.candiez.data.Stamp;

public class MerchantProgramActivity extends MenuActivity{	 
	
		private GridView marchantsList;
		private ProgressDialog dialog;
		private String mobile,merchantProgId;
		private List<Candy>   candyData=new ArrayList<Candy>();
		private String programId;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.merchantscreen);
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
	        marchantsList = (GridView) findViewById(R.id.candyview);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		    mobile = preferences.getString("Mobile", null);
			dialog = new ProgressDialog(this);
		    dialog.setMessage("Loading please wait...");
		    
		    if(getIntent().getExtras()!=null){ 
		    	merchantProgId=getIntent().getExtras().getString("programid");
		   	}
		    
		    dialog.show();
			GetCategoryTask task = new GetCategoryTask();
		    task.execute();

 }
	    
	    
	private class GetCategoryTask extends AsyncTask<Void, Void, Void>
	 {
		
		Programs response;
		@Override
		protected Void doInBackground(Void... params)
		{ 
			String url =Domain.candyInfoUrl + merchantProgId + "/" + mobile;
			RetrieverJsonData<Programs> asyncTaskData = new RetrieverJsonData<Programs>();
			try 
			{
				response = asyncTaskData.execute(Programs.class, url);
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
			super.onPostExecute(result);	
			List<Candiez> merchantData=new ArrayList<Candiez>();
			List<Stamp>   stamp=new ArrayList<Stamp>();
			 try {
				   for (Candiez re : this.response.programs) 
					{
						merchantData.add(re);
						programId=re.programId;
						candyData=re.candiez;
						stamp=re.stamps;
    					CandyAdapter adapter=new CandyAdapter(MerchantProgramActivity.this,R.id.name,stamp);
	    			    marchantsList.setAdapter(adapter);
					}
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			   				 
				dialog.dismiss();	  
		}
	    	
	}
		
		
	public class CandyAdapter extends ArrayAdapter<Stamp>
	{

		private Context context;
		List<Stamp> data;
		private final ImageDownloader imageDownloader = new ImageDownloader();

		public CandyAdapter(Context context, int textViewResourceId,
				List<Stamp> dataObject)
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
					rowView = inflater.inflate(R.layout.candyrow, parent, false);
				}
				
				ImageView caticonImageView = (ImageView) rowView.findViewById(R.id.image);
				TextView indexView = (TextView) rowView.findViewById(R.id.index);
				
				indexView.setTypeface(font);
				
				final int index=position;
				//populate images and text
				if(getItem(position).stampURL.length()>1){
					String imageUrl=Domain.candyiconUrl+getItem(position).stampURL;
					imageDownloader.download(imageUrl, (ImageView) caticonImageView);
					for (int i = 0; i < candyData.size(); i++){
						Candy obj=candyData.get(i);
						if(obj.imageUrl.equalsIgnoreCase(getItem(position).stampURL)){
							 indexView.setText(obj.candyDescription);
							 indexView.setTextSize(11);
							 indexView.setWidth(80);
							 indexView.setTextColor(Color.WHITE);
							 indexView.setGravity(Gravity.TOP);
							 FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
							 LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							 params.gravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;
							 indexView.setLayoutParams(params);							 
						}							
					}
				}else{
					  if(getItem(position).isSealed){
							caticonImageView.setImageResource(R.drawable.sealed_stamp);
					  }else{
							caticonImageView.setImageResource(R.drawable.empty_stamp);
						}						  
					  indexView.setTextSize(30);
					  indexView.setTextColor(Color.GRAY);
					  FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					  LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					  params.gravity = Gravity.CENTER;
					  indexView.setLayoutParams(params);		
					  indexView.setText(Integer.toString(getItem(position).index)); 
				}	
				
				//Type Visit
				//if canbestamped true  make it clickable 
					if(getItem(position).canBeStamped){
							caticonImageView.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									
									// TODO Auto-generated method stub
									if(getItem(index).stampURL.equalsIgnoreCase("")){
									   postStampAlertBox("Assign Stamp",Domain.stampOnUrl,getItem(index).index);
									 }
										
								   if(!getItem(index).stampURL.equalsIgnoreCase("")){
									   postStampAlertBox("Redeem Stamp",Domain.redeemurl,getItem(index).index);
									 }
								   
								}							
					         });		
					  }else{
					    	//nothing
					     }
				}catch (Exception e) {
			            e.printStackTrace();
			    }
			return rowView;
		}
		
	}
	
	
	private void postStampAlertBox(String title,final String url,final int type)
	{
		final PostData postObj = new PostData();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title);
		alert.setMessage("Enter Merchant Code");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Stamp", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			 String value = input.getText().toString();
			 if(value.length()==6){
				  postObj.postData(value,mobile,programId,type,url);
			 }else{
				  Toast.makeText(MerchantProgramActivity.this,"Code should be  of 6 character",Toast.LENGTH_SHORT).show();
			   }
			 }
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}
	
	
	public void implimentStampResponse(String getResponse){
		try {
			JSONObject responseObject = new JSONObject(getResponse);
		    String responseCode=responseObject.getString("code");
		    String responseMsg=responseObject.getString("message");	
		    if(responseCode.equalsIgnoreCase("200")){
			  refreshActivity();
		    }else{
		      Toast.makeText(MerchantProgramActivity.this,responseMsg,Toast.LENGTH_SHORT).show();    	
		    }
		    Toast.makeText(MerchantProgramActivity.this,getResponse,Toast.LENGTH_SHORT).show(); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@Override
	public void onBackPressed() {
	   Intent intent = new Intent();
	   intent.setClass(MerchantProgramActivity.this,MyStampActivity.class);
       startActivity(intent);
       finish(); 
	}
	
	private void refreshActivity(){
	   Intent intent = new Intent();
	   intent.setClass(MerchantProgramActivity.this,MerchantProgramActivity.class);
	   intent.putExtra("programid",merchantProgId);
       startActivity(intent);
       finish(); 
	}

	
	
//Class to post data for stamp	
    public class PostData{
		String merchantCode;
		String customer;
		String programID;
	    String getresponse;
		int index;
		String Url;
		public void postData(String merchantcode,String _customer,String programid,int inde,String url) {
			this.merchantCode=merchantcode;
			this.customer=_customer;
			this.programID=programid;
			this.index=inde;
			Url=url;
			dialog.show();
			PostTask task = new PostTask();
		    task.execute();
		} 
		
		private class PostTask extends AsyncTask<Void, Void, Void>
		 {
			
			@Override
			protected Void doInBackground(Void... params)
			{ 
				
				HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost(Url);
			    try {
			        // Add your data
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("merchantCode", merchantCode));
			        nameValuePairs.add(new BasicNameValuePair("phone", customer));
			        nameValuePairs.add(new BasicNameValuePair("programID", programID));
			        nameValuePairs.add(new BasicNameValuePair("visit",Integer.toString(index)));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        // Execute HTTP Post Request
			        HttpResponse response = httpclient.execute(httppost);
			        getresponse=EntityUtils.toString(response.getEntity());
			    } catch (ClientProtocolException e) {
			    	e.printStackTrace();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				super.onPostExecute(result);	
				implimentStampResponse(getresponse);
				dialog.dismiss();	
			}
		}
	}
			
}
