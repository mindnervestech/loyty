package com.mnt.candiez.activity;
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
import com.mnt.candiez.data.Register;
import com.mnt.candiez.data.RegisterApi;
import com.mnt.candiez.data.RetrieverJsonData;

// This is first activity which is called for first time
public class LoginActivity extends Activity{
	
	public static final String APP_ID="403752899726883";
	private ProgressDialog dialog;
	private EditText enterPin;
	private ImageView activateBtn;
	private SharedPreferences preferences;	
	private LinearLayout activateLayout;
	private int flagIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login);
        ImageView loginBtn=(ImageView) findViewById(R.id.ViewLogin);
        activateBtn=(ImageView) findViewById(R.id.activate);
        activateLayout=(LinearLayout) findViewById(R.id.loginwindow2);
        enterPin=(EditText) findViewById(R.id.pin);    
        
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
     	final SharedPreferences.Editor editor = preferences.edit();
     	dialog = new ProgressDialog(this);
        dialog.setMessage("Loading please wait...");
        String mobile = preferences.getString("Mobile", null);
        String pin = preferences.getString("Pin", null);
        String flag = preferences.getString("Activate", null);
        
        if (mobile != null && pin != null && flag != null) 
        {
        	
			enterPin.requestFocus();
        	if(flag.equalsIgnoreCase("1")){
        	  navigate();
        	}
        }  
        
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if( ((EditText)findViewById(R.id.mobile)).getText().toString().length()==10)
				{	
			      if(checkConnectionAvailability()){
					dialog.show();
					GetPinTask task = new GetPinTask();
				    task.execute(); 
			      }else{
						 Toast.makeText(LoginActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
					}
					
				}else{
					 Toast.makeText(LoginActivity.this,"Enter a field",Toast.LENGTH_SHORT).show();
				}
			}
        });
        
        activateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String _pin = preferences.getString("Pin", null);
				flagIndex++;
				if( enterPin.getText().toString().equalsIgnoreCase(_pin))
				{	
				   editor.putString("Activate","1");
				   editor.commit();	
				   navigate();
				}else{
					 if(flagIndex==3){
						 editor.putString("Pin",null);
						 editor.putString("Activate",null);
						 editor.commit();
						 Toast.makeText(LoginActivity.this,"Retry",Toast.LENGTH_SHORT).show();
						 activateBtn.setVisibility(View.INVISIBLE);
						 activateLayout.setVisibility(View.INVISIBLE);
					 }else{
						 Toast.makeText(LoginActivity.this,"Enter Valid Pin",Toast.LENGTH_SHORT).show();	 
					 }
					 
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
   
   private class GetPinTask extends AsyncTask<Void, Void, Void>
   {
 
	 	RegisterApi response;
	 	
	 	@Override
	 	protected Void doInBackground(Void... params)
	 	{ 			
	 		
	 		String url =Domain.getPinUrl + ((EditText)findViewById(R.id.mobile)).getText().toString();
			RetrieverJsonData<RegisterApi> asyncTaskData = new RetrieverJsonData<RegisterApi>();
			try 
			{
				response = asyncTaskData.execute(RegisterApi.class, url);
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
	 		final SharedPreferences.Editor editor = preferences.edit();
	 	
			try{
				if(response == null) {
					dialog.dismiss();
					Toast.makeText(LoginActivity.this, "Not able to get the Results... Try later.", Toast.LENGTH_LONG).show();
					return;
				}
				     Register re =this.response.register ;
					 editor.putString("Mobile",re.phone);
					 editor.putString("Pin",re.pin);
					 editor.putString("cityFromLocal",re.cityFromLocal);
					 editor.putString("Activate","0");
					 editor.commit();
					 //Toast.makeText(LoginActivity.this,re.pin, Toast.LENGTH_SHORT).show();
					 if(re.pin.equalsIgnoreCase("9999")){
						editor.putString("Activate","1");
						editor.commit();
						navigate(); 
					 }else{
					   activateBtn.setVisibility(View.VISIBLE);
					   activateLayout.setVisibility(View.VISIBLE);
					   enterPin.requestFocus();
					 }
	 			
	 		}catch (Exception e) {
	 			
	           e.printStackTrace();
	 		}
	 		finally{
	 			dialog.dismiss();
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
   
}
