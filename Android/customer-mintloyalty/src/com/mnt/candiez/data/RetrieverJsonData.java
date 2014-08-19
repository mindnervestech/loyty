package com.mnt.candiez.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;


public class RetrieverJsonData<T>{
	public T response;

	public RetrieverJsonData() {
	}
	
	public  T execute(Class<T> classz,String url) throws ClientProtocolException, IOException{
		
		    HttpGet getRequest = new HttpGet(url);
		    System.out.println(url);
		    getRequest.addHeader("User-Agent", "Android");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse getResponse = httpClient.execute(getRequest);
		    int statusCode = getResponse.getStatusLine().getStatusCode();			
			if (statusCode != HttpStatus.SC_OK)
			{ 
	            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + getResponse.getStatusLine().getReasonPhrase()); 
	            return null;
	        }
			HttpEntity getResponseEntity = getResponse.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			Reader inputStreamReader = new InputStreamReader(httpResponseStream);
			Gson gson = new Gson();
	        response =  gson.fromJson(inputStreamReader, classz);
	    	//System.out.println("response--"+response.toString());	        
	        return response;
		
	}
	

}
