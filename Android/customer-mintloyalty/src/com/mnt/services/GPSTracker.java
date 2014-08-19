package com.mnt.services;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener{

	private Context mContext;

	//flag for GPS Status
	boolean isGPSEnabled = false;

	//flag for network status
	boolean isNetworkEnabled = false;

	public boolean canGetLocation = false;

	Location location;
	public double latitude;
	public double longitude;

	//The minimum distance to change updates in metters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 metters

	//The minimum time beetwen updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	//Declaring a Location Manager
	protected LocationManager locationManager;

	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

			//getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			//getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;

				//First get location from Network Provider
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					Log.d("Network", "Network");

					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						updateGPSCoordinates();
					}
				} else {
					//nothing
				}

				//if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

						Log.d("GPS Enabled", "GPS Enabled");

						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							updateGPSCoordinates();
						}
					}
				} else {
				//nothing
				}
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
			Log.e("Error : Location", "Impossible to connect to LocationManager", e);
			
		}

		return location;
	}

	public void updateGPSCoordinates() {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Function to get latitude
	 */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		return latitude;
	}

	/**
	 * Function to get longitude
	 */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog
	 */
	
	/**
	 * Get list of address by latitude and longitude
	 * @return null or List<Address>
	 */
	public List<Address> getGeocoderAddress(Context context) {
		if (location != null) {
			Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
			try {
				List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
				return addresses;
			} 
			catch (IOException e) {
				//e.printStackTrace();
				Log.e("Error : Geocoder", "Impossible to connect to Geocoder", e);
		
			}
		}

		return null;
	}

	/**
	 * Try to get AddressLine
	 * @return null or addressLine
	 */
	public String getAddressLine(Context context) {
		List<Address> addresses = getGeocoderAddress(context);
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);
			String addressLine = address.getAddressLine(0);

			return addressLine;
		} else {
			return null;
		}
	}

	/**
	 * Try to get Locality
	 * @return null or locality
	 */
	public String getLocality(Context context) {
		List<Address> addresses = getGeocoderAddress(context);
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);
			String locality = address.getLocality();

			return locality;
		} else {
			return null;
		}
	}

	/**
	 * Try to get Postal Code
	 * @return null or postalCode
	 */
	public String getPostalCode(Context context) {
		List<Address> addresses = getGeocoderAddress(context);
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);
			String postalCode = address.getPostalCode();

			return postalCode;
		} else {
			return null;
		}
	}

	/**
	 * Try to get CountryName
	 * @return null or postalCode
	 */
	public String getCountryName(Context context) {
		List<Address> addresses = getGeocoderAddress(context);
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);
			String countryName = address.getCountryName();

			return countryName;
		} else {
			return null;
		}
	}

	/**
	 * Try to get Distance from current location 
	 * @return null or radial distance
	 */
	public double distance(double xLattitude, double xLongitude, char unit) {
		double theta = longitude - xLongitude;
		double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(xLattitude)) 
						+ Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(xLattitude)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}


	@Override
	public void onLocationChanged(Location location) {   
	}

	@Override
	public void onProviderDisabled(String provider) {   
	}

	@Override
	public void onProviderEnabled(String provider) {   
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {   
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
