package com.mnt.candiez.activity;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuActivity extends Activity {
	private static final int _font_size = 20;
	private static final String play_store_url = "http://www.google.com";
	private TextView myCandyBtn;
	private TextView nearMintBtn; 
	private TextView citycandy;
	private TextView setting;
	private TextView logout;
	private SharedPreferences preferences;	
	
	
	protected Typeface font;
	
	public  boolean needUpdateVersion(int versionFromServer) {
	    int v = 0;
	    try {
	        v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
	    } catch (NameNotFoundException e) {
	        // Huh? Really?
	    }
	    return v < versionFromServer;
	    
	}
	
	public  void showVersionChangeMsg(final Activity ctx)
	{		    	
			
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

			dialogBuilder.setNeutralButton( "Update", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) 
				{
					Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(play_store_url));
					startActivity(i);
					dialog.dismiss();
				
				}
		    });
			

			dialogBuilder.setTitle("Update");

			dialogBuilder.setMessage("Mint Loyalty update is needed.PLease visit Google store");

			dialogBuilder.show();

	}
	
	protected String getDate(String milli){
		try {
			 return new SimpleDateFormat("dd-MM-yyyy").format(new Date(Long.valueOf(milli)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return "";
	 }
	
	protected String getDate(long milli){
		try {
			 return new SimpleDateFormat("dd-MM-yyyy").format(new Date(milli));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return "";
	 }
	
	protected void buildCommonScreenContext(final Context ctx) {
		font = Typeface.createFromAsset(getAssets(), "SegoePrint.ttf");
		
		LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.slidemenu, null);
		View insertPoint = findViewById(R.id.slideMenu);
		((ViewGroup) insertPoint).addView(v, 0);   
		
		myCandyBtn = (TextView) findViewById(R.id.mycandy);
		myCandyBtn.setTextSize(_font_size);
		myCandyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				navigateToMyMint(ctx);
			}
	    });
		
		nearMintBtn=(TextView) findViewById(R.id.nearmint);
		nearMintBtn.setTextSize(_font_size);
		nearMintBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				navigateToNearMint(ctx);
			}
        });
		
		citycandy=(TextView) findViewById(R.id.citycandy);
		citycandy.setTextSize(_font_size);
		citycandy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				navigateToMyCity(ctx);
			}
        });
		
		setting=(TextView) findViewById(R.id.setting);
		setting.setTextSize(_font_size);
		setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				 i.setClass(ctx,ChooseCityActvity.class);
			     startActivity(i);
			     finish();
			}
        });
		
		logout=(TextView) findViewById(R.id.logout);
		logout.setTextSize(_font_size);
		logout.setVisibility(View.INVISIBLE);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				navigateToLogin(ctx);
				
			}
        });
	}
	
	private void navigateToMyMint(Context ctx){
		  Intent i = new Intent();
		  i.setClass(ctx,MyStampActivity.class);//MyMerchantActivity.class);
		  startActivity(i);
		  finish();
	}
	  
	private void navigateToNearMint(Context ctx){
		  Intent i = new Intent();
		  i.setClass(ctx,NearMintActivity.class);
		  startActivity(i);
		  finish();
	}
	
	private void navigateToMyCity(Context ctx){
		 Intent i = new Intent();
		 i.setClass(ctx,CityCategoryActivity.class);
		 startActivity(i);
		 finish();
	}
	
	// Clear session before navigating.
	private void navigateToLogin(Context ctx){
		preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
		final SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Pin",null);
		editor.putString("Activate",null);
		editor.putString("Mobile", null);
		editor.putString("City", null);
		editor.putString("City-Value", null);
	        
		editor.commit();
		Intent i = new Intent();
		i.setClass(ctx,LoginActivity.class);
		startActivity(i);
		finish();
	} 
	  
	  

}
