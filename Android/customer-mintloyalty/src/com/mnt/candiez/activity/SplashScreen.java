package com.mnt.candiez.activity;
import com.mnt.services.LocationListnerService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
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
     
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String city = preferences.getString("City", null);
        String cityValue = preferences.getString("City-Value", null);
        if (city != null || cityValue != null) 
        {
        	flag=true;
        }
        
        navigate();
        
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

//        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//        	
//        //    buildAlertMessageNoGps();
//        	  navigate();
//        
//        }else{
//        	Intent startService  = new Intent(this,LocationListnerService.class);
//			startService(startService);
//			navigate();
//        }
        
		

    }
    
    private void navigate(){
    	new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            if(checkConnectionAvailability()){	
            	if(flag){
            		Intent i = new Intent(SplashScreen.this, CityCategoryActivity.class);
	                startActivity(i);
	                finish();
            	}else{
	                Intent i = new Intent(SplashScreen.this, ChooseCityActvity.class);
	                startActivity(i);
	                finish();
            	}
            }else{
            	Toast.makeText(SplashScreen.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
             }
            }
        }, SPLASH_TIME_OUT);
    }
    
    

//    public String jsonResponse(String url){
//		StringBuilder builder = new StringBuilder();
//	    HttpClient client = new DefaultHttpClient();
//	    HttpGet httpGet = new HttpGet(url);
//	    try {
//	      HttpResponse response = client.execute(httpGet);
//	      StatusLine statusLine = response.getStatusLine();
//	      int statusCode = statusLine.getStatusCode();
//	      if (statusCode == 200) {
//		        HttpEntity entity = response.getEntity();
//		        InputStream content = entity.getContent();
//		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//		        String line;
//		        while ((line = reader.readLine()) != null) {
//		        builder.append(line);
//	        }
//	      } else {
//	       // Log.e(ParseJSON.class.toString(), "Failed to download file");
//	      }
//	    } catch (ClientProtocolException e) {
//	      e.printStackTrace();
//	    } catch (IOException i) {
//	      i.printStackTrace();
//	    }
//	    return builder.toString();
//	} 
//    

    
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
    
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("In order to get atractive candies, Make sure to on GPS")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                       startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                       final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                       if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    	   Intent startService  = new Intent(SplashScreen.this,LocationListnerService.class);
                  		   startService(startService);
                       }
                       navigate();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        navigate();
                   }
               });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

