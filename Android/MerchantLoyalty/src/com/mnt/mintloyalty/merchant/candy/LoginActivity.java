package com.mnt.mintloyalty.merchant.candy;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mnt.candiez.data.Domain;
public class LoginActivity extends Activity{
	private ProgressDialog dialog;
	ImageView activateBtn;
	SharedPreferences preferences;	
	LinearLayout activateLayout;
	int flagIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login);
        ImageView loginBtn=(ImageView) findViewById(R.id.ViewLogin);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
     	dialog = new ProgressDialog(this);
        dialog.setMessage("Loading please wait...");
        String code = preferences.getString("MerchantId", null);
        if (code != null) 
        {
        	  navigate();
        }  
        
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if( ((EditText)findViewById(R.id.email)).getText().toString().length()>1 && ((EditText)findViewById(R.id.password)).getText().toString().length()>1)
				{	
			      if(checkConnectionAvailability()){
			    	  PostData postObj = new PostData();
			    	  postObj.postData( ((EditText)findViewById(R.id.email)).getText().toString(),  ((EditText)findViewById(R.id.password)).getText().toString());
			      }else{
						 Toast.makeText(LoginActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
					}
					
				}else{
					 Toast.makeText(LoginActivity.this,"Enter both the field",Toast.LENGTH_SHORT).show();
				}
			}
        
        });
    }
        
            
   private void navigate(){
	   Intent i = new Intent();
	   i.setClass(LoginActivity.this,SplashScreen.class);
       startActivity(i);
       finish();	   
   }
   
   public class PostData{
	   SharedPreferences.Editor editor = preferences.edit();
		String Email;
		String Password;
		String programID;
	    String getresponse;
		int index;
		String Url;
		public void postData(String email,String password) {
			this.Email=email;
			this.Password=password;
			Url=Domain.loginUrl;
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
			        nameValuePairs.add(new BasicNameValuePair("email", Email));
			        nameValuePairs.add(new BasicNameValuePair("password", Password));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        // Execute HTTP Post Request
			        HttpResponse response = httpclient.execute(httppost);
			        getresponse=EntityUtils.toString(response.getEntity());
			    }  catch (Exception e) {
			    	e.printStackTrace();
			    }
				return null;
				
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				super.onPostExecute(result);	
				try {
					JSONObject jsonObj = new JSONObject(getresponse);
					try {
						String code = jsonObj.getString("code");
						if(code != null) {
							String message = jsonObj.getString("message");
							Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
					}
					editor.putString("MerchantId",jsonObj.getString("id"));
					editor.commit();
					dialog.dismiss();	
					if(jsonObj.getString("id") != null && !jsonObj.getString("id").equalsIgnoreCase("")){
						navigate();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			 	
		}

	} 
   
   public boolean checkConnectionAvailability() {
 		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
 	        android.net.NetworkInfo wifi = cm
 	                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
 	        android.net.NetworkInfo datac = cm
 	                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
 	        
 	        if ((wifi != null & datac != null)
 	                && (wifi.isConnected() | datac.isConnected())) {
 	        	return true;
 	        }
 	        
 	    return false;
 	} 
   
   public static void handleLogout(Activity actvity) {
	   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(actvity);
	   SharedPreferences.Editor editor = preferences.edit();
	   editor.putString("MerchantId", null);
	   editor.commit();
	   Intent i = new Intent();
	   i.setClass(actvity,LoginActivity.class);
	   actvity.startActivity(i);
	   actvity.finish();
	   
   }
   
}
