
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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agimind.widget.SlideHolder;
import com.mnt.candiez.data.Candiez;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.ImageDownloader;
import com.mnt.candiez.data.Point;
import com.mnt.candiez.data.Programs;
import com.mnt.candiez.data.RetrieverJsonData;

public class PointActivity extends MenuActivity{
	
	private ListView pointsList;
	private ProgressDialog dialog;
	private String mobile,merchantProgId,programId;
	private SharedPreferences preferences;
	private ProgressBar progressBar;
	private TextView amountView,pointsView;
	private Button goBtn;
	private EditText setAmountEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pointscreen);
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressBar=(ProgressBar)findViewById(R.id.myProgress);
        setAmountEdt=(EditText)findViewById(R.id.setamount);
        amountView=(TextView)findViewById(R.id.amount);
        pointsView=(TextView)findViewById(R.id.points);  
        goBtn=(Button)findViewById(R.id.go); 
        pointsList = (ListView) findViewById(R.id.pointsview);
        
        Resources res = getResources();
        progressBar.setProgressDrawable(res.getDrawable(R.drawable.progressbar));
        
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mobile = preferences.getString("Mobile", null);
        
        if(getIntent().getExtras()!=null){ 
	    	merchantProgId=getIntent().getExtras().getString("programid");
	   	}  
        
		goBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Domain domainObj=new Domain();
				if(!setAmountEdt.getText().toString().equalsIgnoreCase("")){
					postStampAlertBox("Assign Stamp", domainObj.stampOnUrl, 0);
				 }else{
					Toast.makeText(PointActivity.this,"Enter Value",Toast.LENGTH_SHORT).show();
				   }
			}							
         });
		
		 
		 
		dialog = new ProgressDialog(this);
	    dialog.setMessage("Loading please wait...");
	    dialog.show();
	    
		GetCategoryTask task = new GetCategoryTask();
		task.execute();  
}
 
	private class GetCategoryTask extends AsyncTask<Void, Void, Void>
	 {
		Programs response;
		String total;
		@Override
		protected Void doInBackground(Void... params)
		{ 
			String url =Domain.candyInfoUrl + merchantProgId + "/" + mobile;
			System.out.println(url);
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
			List<Point> points = new ArrayList<Point>();
			
			 try {
				   for (Candiez re : this.response.programs) 
					{
					   points=re.points;
					   total=re.total;
					   programId=re.programId;
					   progressBar.setProgress((int)re.howMany);
					   pointsView.setText("( "+String.valueOf((int)re.howMany)+"/"+re.total+" )");
					   amountView.setText("( 1 point="+re.amount+" )");
					}
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			 
			    progressBar.setMax(Integer.parseInt(total));
			    CandyAdapter adapter=new CandyAdapter(PointActivity.this,R.id.image,points);
			    pointsList.setAdapter(adapter);	 
				dialog.dismiss();	  
		}
	    	
	}
		
		
	public class CandyAdapter extends ArrayAdapter<Point>
	{

		private Context context;
		List<Point> data;
		private final ImageDownloader imageDownloader = new ImageDownloader();

		public CandyAdapter(Context context, int textViewResourceId,
				List<Point> dataObject)
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
					rowView = inflater.inflate(R.layout.pointsrow, parent, false);
				}
				
				LinearLayout layout=(LinearLayout) rowView.findViewById(R.id.layout);
				ImageView caticonImageView = (ImageView) rowView.findViewById(R.id.image);
				ImageView clickImageView = (ImageView) rowView.findViewById(R.id.click);
				TextView descView=(TextView) rowView.findViewById(R.id.name);
				TextView pointsView=(TextView) rowView.findViewById(R.id.points);		
				
				pointsView.setTypeface(font);
				descView.setTypeface(font);
				final int index=position;
				String imageUrl=Domain.candyiconUrl+getItem(position).pointURL;
				imageDownloader.download(imageUrl, (ImageView) caticonImageView);
				descView.setText(getItem(position).description);
				pointsView.setText(Integer.toString(getItem(position).milestoneAt)+" points");
				
				if(getItem(position).canBeStamped){
					clickImageView.setVisibility(View.VISIBLE);
					clickImageView.setImageResource(R.drawable.redeem_btn);
					clickImageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							postStampAlertBox("Redeem Stamp", Domain.redeemurl, getItem(index).milestoneAt);
						}							
			         });	
				} else {
					clickImageView.setVisibility(View.INVISIBLE);
				}
				
				}catch (Exception e) {
			            e.printStackTrace();
			    }
			return rowView;
		}
	}
	
	private void postStampAlertBox(final String title,final String url,final int index)
	{
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(title);
			alert.setMessage("Enter Merchant Code");
			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);
			alert.setPositiveButton("Stamp", new DialogInterface.OnClickListener() {
				
			  public void onClick(DialogInterface dialog, int whichButton) {
				 String value = input.getText().toString();
				 PostData postObj = new PostData();
				 if(value.length()>1){
					 
					 if(title.equalsIgnoreCase("Assign Stamp")){
					     postObj.postDataStamp(input.getText().toString(),mobile,programId,setAmountEdt.getText().toString(),url);
					 }else{
						 postObj.postDataRedeem(input.getText().toString(),mobile,programId,index,url);	 
					 }
					 
				 }else{
					  Toast.makeText(PointActivity.this,"Please enter merchant code",Toast.LENGTH_SHORT).show();
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
	
	//Class to post data for stamp	
    public class PostData{
		String merchantCode;
		String customer;
		String programID;
	    String amount;
	    int index;;
		String Url;
		public void postDataStamp(String merchantcode,String _customer,String programid,String amoun,String url) {
			this.merchantCode=merchantcode;
			this.customer=_customer;
			this.programID=programid;
			this.amount=amoun;
			Url=url;
			dialog.show();
			PostTaskStamp task = new PostTaskStamp();
		    task.execute();
		} 
		public void postDataRedeem(String merchantcode,String _customer,String programid,int inde,String url) {
			this.merchantCode=merchantcode;
			this.customer=_customer;
			this.programID=programid;
			this.index=inde;
			Url=url;
			dialog.show();
			PostTaskRedeem task = new PostTaskRedeem();
		    task.execute();
		} 
		
		private class PostTaskStamp extends AsyncTask<Void, Void, Void>
		 {
			String getresponse;
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
			        nameValuePairs.add(new BasicNameValuePair("amount",amount));
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
				Toast.makeText(PointActivity.this,getresponse,Toast.LENGTH_SHORT).show();
				dialog.dismiss();	
				refreshActivity();
				
			}
			 	
	 }
		
		private class PostTaskRedeem extends AsyncTask<Void, Void, Void>
		 {
			String getresponse;
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
			        nameValuePairs.add(new BasicNameValuePair("point",Integer.toString(index)));
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
				Toast.makeText(PointActivity.this,getresponse,Toast.LENGTH_SHORT).show();
				dialog.dismiss();	
				refreshActivity();
				
			}
			 	
		}	

	}
    
    private void refreshActivity(){
    	Intent intent = new Intent();
	    intent.setClass(PointActivity.this,PointActivity.class);
	    intent.putExtra("programid",programId);
        startActivity(intent);
        finish(); 
  	}
    
    @Override
	public void onBackPressed() {
	   Intent intent = new Intent();
	   intent.setClass(PointActivity.this,MyStampActivity.class);
       startActivity(intent);
       finish(); 
	}
    
    
}