package com.mnt.candiez.activity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(11)
public class MapActivity extends Activity {

	// Google Map
	private GoogleMap googleMap;
	double lat,longi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
		try {
			// Loading map
			 if(getIntent().getExtras()!=null){ 
				 lat=getIntent().getExtras().getDouble("Latitude");
				 longi=getIntent().getExtras().getDouble("Longitude");
				 initilizeMap();
				
				 }
			 
		} catch (Exception e) {
			e.printStackTrace();
			 Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			
		}
	}
	
	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.setMyLocationEnabled(true);
			
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(lat,longi), 17));
			

			
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
				
			}
			
						
			googleMap.addMarker(new MarkerOptions()
	        .position(new LatLng(lat, longi))
	        .title(getIntent().getExtras().getString("Title")) 
	        .snippet(getIntent().getExtras().getString("Adress"))
	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
	        .visible(true));
		}
	}
	
	 @Override
	  public void onBackPressed() {
		 Intent i = new Intent();
 	     i.setClass(MapActivity.this,MerchantsByCategoryActivity.class);
 	     i.putExtra("City-Value",getIntent().getExtras().getString("City-Value"));
 	     i.putExtra("Category-Value",getIntent().getExtras().getString("Category-Value"));
 	     if(getIntent().getExtras().getBoolean("Near-Mint")){
 	    	i.setClass(MapActivity.this,NearMintActivity.class);
 	     }
         startActivity(i);
         finish();
		}
	
}
