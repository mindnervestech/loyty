package com.mnt.services;

import java.util.List;

import com.mnt.candiez.activity.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class LocationListnerService extends Service {

	private static final String TAG = "Service";
	Context con;
	LocationManager locationManager;
	MyLocationListener listener;
	Location previousBestLocation = null;
	private static final int TEN_SECONDS = 1000 * 10;
	int counter = 0;
	GPSTracker gpsTracker;

	@Override
	public void onCreate() {
		con = getBaseContext();
//		Toast.makeText(con, "OnCreate", Toast.LENGTH_SHORT).show();
		gpsTracker = new GPSTracker(con);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		listener = new MyLocationListener();        
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		try{	
			String message="Latitude-"+String.valueOf(gpsTracker.latitude)+" Longitude-"+String.valueOf(gpsTracker.longitude);
			Toast.makeText(con,message, Toast.LENGTH_SHORT).show();
			notfication("Candiez",message);
		 }catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		//			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, listener);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "Service onBind");
		return null;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service onDestroy");
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(final Location loc) {
		try{
			double lat= loc.getLatitude();
			double longt= loc.getLongitude();
			String message="Latitude-"+String.valueOf(lat)+" Longitude-"+String.valueOf(longt);
			Toast.makeText(con,message, Toast.LENGTH_SHORT).show();
		//	notfication("Candiez",message);
		 }catch (Exception e) {
				e.printStackTrace();
			}
		}


		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderDisabled(String provider) {
//			Toast.makeText(getApplicationContext(), "Location provider disable", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
//			Toast.makeText(getApplicationContext(), "Location provider enable", Toast.LENGTH_SHORT).show();
		}
		
		

	}
	
	private void notfication(String notificationTitle, String notificationMessage){
		    Intent MyIntent = new Intent(Intent.ACTION_VIEW);
	   	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    android.app.Notification notification = new android.app.Notification(R.drawable.ic_launcher, "A New Message from Mint Candiez!",
		    System.currentTimeMillis());
		    PendingIntent pendingIntent = PendingIntent.getActivity(con, 0, MyIntent, 0);
		    notification.setLatestEventInfo(con, notificationTitle, notificationMessage, pendingIntent);
		    notificationManager.notify(10001, notification);
	}

	
}