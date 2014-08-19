package com.mnt.mintloyalty.merchant.candy;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Toast;
public class SplashScreen extends Activity{
	 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);
        
		new Handler().postDelayed(new Runnable() {
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	            @Override
	            public void run() {
	    
		            if(checkConnectionAvailability()){	
		            		Intent i = new Intent(SplashScreen.this, ProgramsActivity.class);
			                startActivity(i);
			                finish();
		            }else{
		            	Toast.makeText(SplashScreen.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
		             }
	            }
	        }, SPLASH_TIME_OUT);

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

