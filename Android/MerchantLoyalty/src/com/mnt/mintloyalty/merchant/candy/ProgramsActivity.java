package com.mnt.mintloyalty.merchant.candy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mnt.candiez.data.Candiez;
import com.mnt.candiez.data.Candy;
import com.mnt.candiez.data.Domain;
import com.mnt.candiez.data.Programs;
import com.mnt.candiez.data.RetrieverGetJsonData;
import com.mnt.candiez.data.RetrieverPostJsonData;

public class ProgramsActivity extends Activity{	 

	private ListView programssList;
	private ProgressDialog progressDialog;
	String mobile,merchantId;
	List<Candy>   candyData=new ArrayList<Candy>();
	String programId,programType;
	ImageView myCityCandyBtn;
	Typeface font;
	List<Candiez> merchantData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.programscreen);

		programssList = (ListView) findViewById(R.id.programview); 
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LoginActivity.handleLogout(ProgramsActivity.this);
				
			}
		});
		font = Typeface.createFromAsset(getAssets(), "SegoePrint.ttf");  

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		merchantId = preferences.getString("MerchantId", null);

		progressDialog = new ProgressDialog(this);
	}

	public void searchCustomer(View v){
		progressDialog.setMessage("Loading please wait...");
		progressDialog.show();

		merchantData = new ArrayList<Candiez>();

		EditText mobileEdt = (EditText) findViewById(R.id.mobile);

		mobile = mobileEdt.getText().toString();

		if( mobile.length() == 10 ){
			GetCategoryTask task = new GetCategoryTask();
			task.execute();
		} else {
			Toast.makeText(ProgramsActivity.this,"Enter valid mobile number",Toast.LENGTH_SHORT).show();
		}
	}

	private class GetCategoryTask extends AsyncTask<Void, Void, Void>
	{
		Programs response;

		@Override
		protected Void doInBackground(Void... params)
		{ 
			String url = Domain.merchantPrgmUrl + merchantId + "/" + mobile;;

			RetrieverGetJsonData<Programs> asyncTaskData = new RetrieverGetJsonData<Programs>();
			try {
				response = asyncTaskData.execute(Programs.class, url);
				System.out.println(response);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);	

			try {
				if("false".equalsIgnoreCase(this.response.status)){
					showCustomDialog();
					//					Toast.makeText(ProgramsActivity.this, "Customer Not Registered", Toast.LENGTH_SHORT).show();

				} else {

					for (Candiez re : this.response.programs) {
						merchantData.add(re);
					}

					CandyAdapter adapter=new CandyAdapter(ProgramsActivity.this,R.id.name,merchantData);
					programssList.setAdapter(adapter);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			progressDialog.dismiss();

		}

	}


	public class CandyAdapter extends ArrayAdapter<Candiez> {

		private Context context;
		List<Candiez> data;
		Domain domainObj;

		public CandyAdapter(Context context, int textViewResourceId, List<Candiez> dataObject) {
			super(context, textViewResourceId, dataObject);
			this.context=context;
			this.data=dataObject;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView=convertView;
			try{
				//if(rowView==null){
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.programsrow, parent, false);
				//}

				LinearLayout layoutView = (LinearLayout) rowView.findViewById(R.id.layout6);
				LinearLayout fulllayoutView = (LinearLayout) rowView.findViewById(R.id.layout);
				TextView descriptionView = (TextView) rowView.findViewById(R.id.name);
				TextView amountView = (TextView) rowView.findViewById(R.id.amount);
				TextView howManyView = (TextView) rowView.findViewById(R.id.howmany);
				
				ImageView yourStampImageView = (ImageView) rowView.findViewById(R.id.myImageView);
				if(getItem(position).programType.equalsIgnoreCase("Point")){
					yourStampImageView.setImageResource(R.drawable.your_points);
				}
				
				if(getItem(position).programType.equalsIgnoreCase("Visit")){
					yourStampImageView.setImageResource(R.drawable.your_visits);
				}
				amountView.setTypeface(font);
				descriptionView.setTypeface(font);
				howManyView.setTypeface(font);
				howManyView.setText(""+(int)getItem(position).howMany);
				descriptionView.setText(getItem(position).programDescription);
				amountView.setText("Rs."+getItem(position).amount+"/"+getItem(position).programType);

				for(int i=0;i<getItem(position).candiez.size();i++) {				
					TextView candiez = new TextView(ProgramsActivity.this);
					candiez.setTypeface(font);
					ImageView iconView = new ImageView(ProgramsActivity.this);
					LinearLayout View = new LinearLayout(ProgramsActivity.this);

					View.setOrientation(LinearLayout.HORIZONTAL);

					if(getItem(position).programType.equalsIgnoreCase("VISIT")){
						candiez.setText(getItem(position).candiez.get(i).candyDescription+" On "+Integer.toString(getItem(position).candiez.get(i).number)+" Visit");
					}

					if(getItem(position).programType.equalsIgnoreCase("Point")){
						candiez.setText(getItem(position).candiez.get(i).candyDescription+" At "+Integer.toString(getItem(position).candiez.get(i).number)+" Point");
					}

					iconView.setImageResource(R.drawable.tick);
					candiez.setTextSize(11);
					candiez.setPadding(0, 5, 0, 0);
					candiez.setTextColor(Color.WHITE);
					View.addView(iconView);
					View.addView(candiez);
					layoutView.addView(View);

				}
				final int index=position;
				//populate images and text
				fulllayoutView.setOnClickListener(new OnClickListener() {
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
	public void onBackPressed(){
		finish(); 
	}

	private void navigateToVisit(String id){
		Intent intent = new Intent();
		intent.setClass(ProgramsActivity.this,StampActivity.class);  
		intent.putExtra("programid", id);
		intent.putExtra("Mobile", mobile);
		startActivity(intent);
		finish(); 

	}

	private void navigateToPoint(String id){
		Intent intent = new Intent();
		intent.setClass(ProgramsActivity.this,PointActivity.class);  
		intent.putExtra("programid", id);
		intent.putExtra("Mobile", mobile);
		startActivity(intent);
		finish(); 
	}

	public void showCustomDialog(){
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Alert Message");
		dialog.setCancelable(false);
		TextView titleTxt = (TextView) dialog.findViewById(R.id.alertTitleTxt);
		titleTxt.setText("Phone no "+ mobile +" is not yet register!");

		TextView subTitleTxt = (TextView) dialog.findViewById(R.id.alertSubTitleTxt);
		subTitleTxt.setText("Do you want to register and continue ?");

		Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
		yesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomerRegistration register = new CustomerRegistration();
				register.execute();
				dialog.dismiss();
				progressDialog.setMessage("Loading please wait...");
				progressDialog.show();
			}
		});

		Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				merchantData = new ArrayList<Candiez>();
				CandyAdapter adapter=new CandyAdapter(ProgramsActivity.this,R.id.name,merchantData);
				programssList.setAdapter(adapter);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private class CustomerRegistration extends AsyncTask<Void, Void, Void>
	{
		Programs response;

		@Override
		protected Void doInBackground(Void... params)
		{ 
			String url = Domain.customerRegisterUrl;
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("merchantID", merchantId));
			nameValuePairs.add(new BasicNameValuePair("phoneNo", mobile));

			RetrieverPostJsonData<Programs> asyncTaskData = new RetrieverPostJsonData<Programs>();
			try {
				response = asyncTaskData.execute(Programs.class, url,nameValuePairs);
				System.out.println(response);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);	

			progressDialog.dismiss();
			//System.out.println(this.response);
			try {
				if("false".equalsIgnoreCase(this.response.status)){
					showCustomDialog();

				} else {

					for (Candiez re : this.response.programs) {
						merchantData.add(re);
					}

					CandyAdapter adapter=new CandyAdapter(ProgramsActivity.this,R.id.name,merchantData);
					programssList.setAdapter(adapter);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			progressDialog.dismiss();

		}

	}

}
