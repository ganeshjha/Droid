package com.choicecaller.main;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class SendDailyCallHistoryDetailsToServer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("$$ Send data :$", "started Module ");
		SharedPreferences localSharedPreferences = getApplicationContext()
				.getSharedPreferences(
						GlobalApplicationStatusActivity.PREFS_NAME,
						MODE_PRIVATE);

		String OnOff = localSharedPreferences.getString(
				GlobalApplicationStatusActivity.PREF_ONOFF, "off");

		SharedPreferences UseriDShared = getSharedPreferences(
				ChoiceCallerMainActivity.PREFS_NAME, MODE_PRIVATE);

		String User_ID = UseriDShared.getString(
				ChoiceCallerMainActivity.PREF_User_Id, "NO_ID");
		
		if(OnOff.trim().equalsIgnoreCase("on")){
			Log.e("### STAUS ON FOR SENDING DATA ", "TRUE");
			
			DataBaseCallHistoryChoiceCaller dataBaseCallHistoryChoiceCaller = new DataBaseCallHistoryChoiceCaller(
					this);
			CallHistory callHistory = dataBaseCallHistoryChoiceCaller
					.getCallHistoryRow(User_ID);
			if(callHistory != null){
				new SendCallhistoryData(User_ID, ""+callHistory.get_callSilent() , ""+callHistory.get_callVibrate(), ""+callHistory._callRinged).execute();
			}
			
		}

	}

	
	protected class SendCallhistoryData extends AsyncTask<Void, Integer, Void>  
    {			
		
		String  UserID , callRinged , callVibrate , callSilent ;
		
		public SendCallhistoryData(String   UserID, String  callSilent  , String  callVibrate  ,String  callRinged ) {
			// TODO Auto-generated constructor stub		
			this.UserID = UserID;
			this.callRinged = callRinged;
			this.callSilent = callSilent;
			this.callVibrate = callVibrate;
		}
		
        //Before running code in separate thread  
        @Override  
        protected void onPreExecute()  
        {  
        	 
        }
  
        //The code to be executed in a background thread.  
        @Override  
        protected Void doInBackground(Void... params)  
        {  
              
                synchronized (this)  
                {
            
                	sendDataToServer(callRinged, callVibrate, callSilent, UserID);
                }  
              
            return null;  
        }  
  
        //Update the progress  
        @Override  
        protected void onProgressUpdate(Integer... values)  
        {  
              
        }  
  
        //after executing the code in the thread  
        @Override  
        protected void onPostExecute(Void result)  
        {  
            //close the progress dialog 
           
        }  
    }
	
	private void sendDataToServer(String callRinged , String callVibrate , String callSilent, String UserId) {
		
				HttpClient httpclient = new DefaultHttpClient();				
				try {
					//user_id=2&call_silent=10&call_vibrate=21&call_ringed=13&call_date=2013-07-13
					String Year = ""+Calendar.getInstance().get(Calendar.YEAR);
					String Month = ""+ ( Calendar.getInstance().get(Calendar.MONTH) + 1 );
					String Day = ""+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					
					if(Month.trim().length() == 1){
						Month = "0"+Month;						
					}
					
					if(Day.trim().length() == 1){
						Day = "0"+Day;						
					}
					
					String DateFormat = Year+"-"+Month+"-"+Day;
					
					String tempParam = "user_id=%s&call_silent=%s&call_vibrate=%s&call_ringed=%s&call_date=%s";
					
					tempParam = String.format(tempParam, UserId, callSilent ,  callVibrate,callRinged , DateFormat);
					System.out.println(tempParam);
					URL url;
					String webUrlToFetch = "http://choicecaller.com/phoneapp/data.php"
							+ tempParam;
					url = new URL(webUrlToFetch);

					// URL url = new URL(imageUrl);
					HttpGet httpRequest = null;

					httpRequest = new HttpGet(url.toURI());

					// Execute HTTP Post Request
					// HttpResponse response = httpclient.execute(httppost);
					HttpResponse response = httpclient.execute(httpRequest);
					HttpEntity entity = response.getEntity();

					BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
							entity);
					InputStream inputStream = bufHttpEntity.getContent();

					StringBuilder sb = new StringBuilder();
					// Parse it line by line
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					String temp;
					while ((temp = bufferedReader.readLine()) != null) {
						// Parsing of data here;
						sb.append(temp + " \n");

					}

					Log.e("### Login Response", " " + sb.toString());

					JSONObject jsonObject = new JSONObject(sb.toString());
					String success_msg = jsonObject.getString("success_msg");
					System.out.println("success_msg: " + success_msg);


					if (success_msg.toString().toLowerCase()
							.contains("Data Sent Successfully")) {
						Log.e("DataSent",  "Successfully Sent...");
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
}
