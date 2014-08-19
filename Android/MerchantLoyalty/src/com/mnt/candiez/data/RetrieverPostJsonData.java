package com.mnt.candiez.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;

public class RetrieverPostJsonData<T>{

	public T response;

	public RetrieverPostJsonData() { }

	public T execute(Class<T> classz,String url, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException{

		HttpPost postRequest = new HttpPost(url);		
		HttpClient httpClient = new DefaultHttpClient();
		postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse getResponse = httpClient.execute(postRequest);
		int statusCode = getResponse.getStatusLine().getStatusCode();			

		if (statusCode != HttpStatus.SC_OK) { 
			Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " 
					+ getResponse.getStatusLine().getReasonPhrase()); 
			return null;
		}

		HttpEntity getResponseEntity = getResponse.getEntity();
		InputStream httpResponseStream = getResponseEntity.getContent();
		Reader inputStreamReader = new InputStreamReader(httpResponseStream);
		Gson gson = new Gson();
		response =  gson.fromJson(inputStreamReader, classz);
		System.out.println("response--"+response.toString());	        
		return response;
	}
}
