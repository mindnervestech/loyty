package com.mnt.candiez.activity;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.mnt.candiez.data.Candiez;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.Programs;
import com.mnt.candiez.data.RetrieverJsonData;

public class ProgramsActivity extends MenuActivity{	 
	
	private ListView programssList;
	private ProgressDialog dialog;
	private String mobile,merchantId;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.programscreen);
        final SlideHolder slideHolder= (SlideHolder) findViewById(R.id.slideHolder);
        slideHolder.setEnabled(false);
        ImageView menuIcon = (ImageView)findViewById(R.id.menuicon);
 	   	menuIcon.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				slideHolder.toggle();
 			}
 	   	});
        buildCommonScreenContext(getApplication());
        programssList = (ListView) findViewById(R.id.programview);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    mobile = preferences.getString("Mobile", null);
	    merchantId = preferences.getString("Merchant-Id", null);
		dialog = new ProgressDialog(this);
	    dialog.setMessage("Loading please wait...");
	    dialog.show();
		GetCategoryTask task = new GetCategoryTask();
	    task.execute();	
	    
	   
}
    
    
	private class GetCategoryTask extends AsyncTask<Void, Void, Void>
	 {
		Programs response;
	
		@Override
		protected Void doInBackground(Void... params)
		{ 
			String url =Domain.merchatPrgmUrl+merchantId+"/"+mobile;
			System.out.println(url);
			RetrieverJsonData<Programs> asyncTaskData = new RetrieverJsonData<Programs>();
			try 
			{
				response = asyncTaskData.execute(Programs.class, url);
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
			super.onPostExecute(result);	
			List<Candiez> merchantData=new ArrayList<Candiez>();
			 try {
				 
				   for (Candiez re : this.response.programs) 
					{
						merchantData.add(re);
					}
				 
				   CandyAdapter adapter=new CandyAdapter(ProgramsActivity.this,R.id.name,merchantData);
				   programssList.setAdapter(adapter);
			    
			 } catch (Exception e) {
			      e.printStackTrace();
			    }
			   				 
				dialog.dismiss();	  
		}
	    	
	}
		
	
	public class CandyAdapter extends ArrayAdapter<Candiez>
	{
	
		private Context context;
		List<Candiez> data;
		
		public CandyAdapter(Context context, int textViewResourceId,
				List<Candiez> dataObject)
		{
			super(context, textViewResourceId, dataObject);
			this.context=context;
			this.data=dataObject;
	    }
		  
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView=convertView;
			try{
				
				if(rowView==null){
					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.programrow, parent, false);
				}
				
				LinearLayout layoutView = (LinearLayout) rowView.findViewById(R.id.layout2);
				LinearLayout fulllayoutView = (LinearLayout) rowView.findViewById(R.id.layout);
				TextView descriptionView = (TextView) rowView.findViewById(R.id.name);
				TextView dateView = (TextView) rowView.findViewById(R.id.date);
				TextView amountView = (TextView) rowView.findViewById(R.id.amount);
				
				amountView.setTypeface(font);
				descriptionView.setTypeface(font);
				
				descriptionView.setText(getItem(position).programDescription);
				amountView.setText("Rs."+getItem(position).amount+"/"+getItem(position).programType);
				dateView.setText(getDate(getItem(position).validTill));
				
				for(int i=0;i<getItem(position).candiez.size();i++){
					
					TextView candiez = new TextView(ProgramsActivity.this);
					ImageView iconView = new ImageView(ProgramsActivity.this);
					LinearLayout View = new LinearLayout(ProgramsActivity.this);
					View.setOrientation(LinearLayout.HORIZONTAL);
					
					if(getItem(position).programType.equalsIgnoreCase("VISIT")){
					 candiez.setText("  "+getItem(position).candiez.get(i).candyDescription+" On "+Integer.toString(getItem(position).candiez.get(i).number)+" Visit");
					}
					
					if(getItem(position).programType.equalsIgnoreCase("Point")){
						 candiez.setText("  "+getItem(position).candiez.get(i).candyDescription+" At "+Integer.toString(getItem(position).candiez.get(i).number)+" Point");
					}
					
					
					iconView.setImageResource(R.drawable.tick);
					candiez.setTextSize(17);
	                candiez.setPadding(0, 5, 0, 0);
					candiez.setTextColor(Color.WHITE);
					View.addView(iconView);
					View.addView(candiez);
					layoutView.addView(View);
				
				}
				
				final int index=position;
				layoutView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
				
						if(getItem(index).programType.equalsIgnoreCase("Point")){
							navigateToPoint(getItem(index).programId);
						}
						
						if(getItem(index).programType.equalsIgnoreCase("Visit")){
							navigateToVisit(getItem(index).programId);
						}										
				
					}							
		         });
				
									
				}catch (Exception e) {
			            e.printStackTrace();
			    }
			return rowView;
		}
		
	}
	
	@Override
	public void onBackPressed() {
	   Intent intent = new Intent();
	   intent.setClass(ProgramsActivity.this,MyStampActivity.class);  
	   startActivity(intent);
	   finish(); 
	}

	 private void navigateToVisit(String id){
	    Intent intent = new Intent();
		intent.setClass(ProgramsActivity.this,MerchantProgramActivity.class);  
		intent.putExtra("programid", id);
		startActivity(intent);
		finish(); 
		 
	 }
	 
	private void navigateToPoint(String id){
		Intent intent = new Intent();
		intent.setClass(ProgramsActivity.this,PointActivity.class);  
		intent.putExtra("programid", id);
		startActivity(intent);
		finish(); 
	 }

	
	
    
    
	
//Class to post data for stamp	


}
