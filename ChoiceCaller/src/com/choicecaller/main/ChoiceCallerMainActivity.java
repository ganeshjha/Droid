package com.choicecaller.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;



import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChoiceCallerMainActivity extends Activity {

	Button btnLogin, btnRegister;
	EditText username , password;
	public static final String PREFS_NAME = "PrefChoiceCaller";
	protected static final String PREF_USERNAME = "username";
	protected static final String PREF_PASSWORD = "password";
	protected static final String PREF_User_Id = "userID";
	TextView forgotpassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		
		
		btnLogin = (Button) findViewById(R.id.btnRegister);
		btnRegister = (Button)findViewById(R.id.registerbtn);
		
		username = (EditText)findViewById(R.id.unameEditText);
		password = (EditText)findViewById(R.id.emailEditText);
		
		TextView textViewChoiceCaller = (TextView) findViewById(R.id.textViewContactName);
		Typeface font = Typeface.createFromAsset(getAssets(), "brushscriptstd.otf");  
		textViewChoiceCaller.setTypeface(font); 
		
		
		forgotpassword = (TextView)findViewById(R.id.loginorregister);
		forgotpassword.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent forgotPassword = new Intent(ChoiceCallerMainActivity.this, ForgotPassword.class);
				startActivity(forgotPassword);
			}
		});
		
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);   
        String usernameStr = pref.getString(PREF_USERNAME, "");
        String passwordStr = pref.getString(PREF_PASSWORD, "");
        
        
        SharedPreferences prefONOFF =getApplicationContext(). getSharedPreferences(GlobalApplicationStatusActivity.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        String gblOnOff = prefONOFF.getString(GlobalApplicationStatusActivity.PREF_ONOFF, "Off");
        if(gblOnOff.trim().equalsIgnoreCase("on")){        	
        	AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        	Log.i("MyApp", "Boot Completed Silent mode");    	
        	am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
       		
		if(! usernameStr.trim().equalsIgnoreCase("") && 
				! passwordStr.trim().equalsIgnoreCase("")){
			
			username.setText(usernameStr);
			password.setText(passwordStr);
			new LoginData( username.getText().toString().trim() , password.getText().toString().trim()).execute();
		}
		
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isNetworkAvailable() == false){
					Toast.makeText(getApplicationContext(), "Network is not available. \n Please check your network settings.", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(!checkParamBeforeSending(username.getText().toString().trim(), password.getText().toString().trim())){
					Toast.makeText(getApplicationContext(), "Please enter Username or Password", Toast.LENGTH_LONG).show();
				}else{
					getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
			        .edit()
			        .putString(PREF_USERNAME, username.getText().toString().trim())
			        .putString(PREF_PASSWORD, password.getText().toString().trim())
			        .commit();					
					new LoginData( username.getText().toString().trim() , password.getText().toString().trim()).execute();
				}
			}
		});
		
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent registerIntent = new Intent(ChoiceCallerMainActivity.this , ChoiceCallerRegister.class);
				startActivity(registerIntent);
			}
		});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choice_caller_main, menu);
		return true;
	}

	private ProgressDialog progressDialog;
	private boolean loginStatus  = false;
	
	protected class LoginData extends AsyncTask<Void, Integer, Void>  
    {			
		
		String  username,password;
		
		public LoginData(String   username, String  password) {
			// TODO Auto-generated constructor stub		
			this.username = username; 
			this.password = password;	
		}
		
        //Before running code in separate thread  
        @Override  
        protected void onPreExecute()  
        {  
        	progressDialog = ProgressDialog.show(ChoiceCallerMainActivity.this,"Login In...",  
        		    "Please wait...", false, false); 
        }
  
        //The code to be executed in a background thread.  
        @Override  
        protected Void doInBackground(Void... params)  
        {  
              
                synchronized (this)  
                {   
                	loginStatus = false;
                	LoginToChoiceCaller(username, password);
                }  
              
            return null;  
        }  
  
        //Update the progress  
        @Override  
        protected void onProgressUpdate(Integer... values)  
        {  
            //set the current progress of the progress dialog  
            progressDialog.setProgress(values[0]);  
        }  
  
        //after executing the code in the thread  
        @Override  
        protected void onPostExecute(Void result)  
        {  
            //close the progress dialog  
            progressDialog.dismiss();  
            //initialize the View  
            if(loginStatus){
            	Toast.makeText(getApplicationContext(), "Login Sucessfully Done...", Toast.LENGTH_LONG).show();
            	Intent mainActivityAfterRegistration = new Intent(ChoiceCallerMainActivity.this, GlobalApplicationStatusActivity.class);
            	startActivity(mainActivityAfterRegistration);
            }else{
            	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ChoiceCallerMainActivity.this);
				dlgAlert.setMessage("Failed to register user...\nTry again.");
				dlgAlert.setTitle("Registration failed");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.setPositiveButton("OK",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {						        	
					          //dismiss the dialog  
					        }
					    });
				dlgAlert.create().show();
				
            }
        }  
    }
	
	public void LoginToChoiceCaller( String   username, String  password) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://choicecaller.com/phoneapp/login.php");

	    try {
	        
	        System.out.println("username :"+username+"\n" +
	        		"Pasword: "+password);
	        
	        String tempParam  = "username=%s&password=%s";
	    	tempParam = String.format( tempParam , username , password);
	    	System.out.println(tempParam);
	    	URL url;
	    	String webUrlToFetch = "http://choicecaller.com/phoneapp/login.php?"+tempParam;
			url = new URL(webUrlToFetch);

			// URL url = new URL(imageUrl);
			HttpGet httpRequest = null;

			httpRequest = new HttpGet(url.toURI());

			
	        // Execute HTTP Post Request
	    //    HttpResponse response = httpclient.execute(httppost);	        
			HttpResponse response = httpclient.execute(httpRequest);
	        HttpEntity entity = response.getEntity();

			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
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

			Log.e("### Login Response", " "+sb.toString() );
	        
			JSONObject jsonObject = new JSONObject(sb.toString());
			String User_ID = jsonObject.getString("Sno");
			System.out.println("UserID: "+User_ID);
			
			getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
	        .edit()
	        .putString(PREF_User_Id, User_ID.trim())	       
	        .commit();					
			
			if(sb.toString().toLowerCase().contains("login successful")){
				loginStatus = true;
			}
	        	
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	    }
	} 

	public boolean checkParamBeforeSending(
			String username,
			String password
		){
		
		if(username .length() == 0||
				password .length() == 0
				){
			return false;
		}else{
			return true;
		}
		
	}
	
	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}
	
}
