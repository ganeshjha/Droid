package com.choicecaller.main;

import java.io.BufferedReader;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPassword extends Activity {

	Button btnForgotPassword;
	TextView tvLoginorRegister;
	EditText username, email;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forgot_password);
		
		TextView textViewChoiceCaller = (TextView) findViewById(R.id.textViewContactName);
		Typeface font = Typeface.createFromAsset(getAssets(), "brushscriptstd.otf");  
		textViewChoiceCaller.setTypeface(font); 
		
		btnForgotPassword = (Button)findViewById(R.id.btnSendEmail);
		
		btnForgotPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!checkParamBeforeSending( username.getText().toString().trim(), email.getText().toString().trim())){
					Toast.makeText(getApplicationContext(), "Please enter username or email address...", Toast.LENGTH_LONG).show();					
				}else{
					new  ForgotPasswordSent(username.getText().toString().trim(), email.getText().toString().trim()).execute();
				}
			}
		});
		
		username = (EditText) findViewById(R.id.unameEditText);
		email = (EditText) findViewById(R.id.emailEditText);
		
		tvLoginorRegister = (TextView)findViewById(R.id.loginorregister);
		tvLoginorRegister.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ForgotPassword.this.finish();
			}
		});
	}
	
	private ProgressDialog progressDialog;	
	
	protected class ForgotPasswordSent extends AsyncTask<Void, Integer, Void>  
    {			
		
		String  username,email;
		
		public ForgotPasswordSent(String   username, String  password) {
			// TODO Auto-generated constructor stub		
			this.username = username; 
			this.email = password;	
		}
		
        //Before running code in separate thread  
        @Override  
        protected void onPreExecute()  
        {  
        	progressDialog = ProgressDialog.show(ForgotPassword.this,"Sending email...",  
        		    "Please wait...", false, false); 
        }
  
        //The code to be executed in a background thread.  
        @Override  
        protected Void doInBackground(Void... params)  
        {  
              
                synchronized (this)  
                {   
                	passwordsentStatus = false;
                	sendForpasswordEmail(username, email);
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
            if(passwordsentStatus){
            	Toast.makeText(getApplicationContext(), "Email sent sucessfully...", Toast.LENGTH_LONG).show();
            }else{
            	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ForgotPassword.this);
				dlgAlert.setMessage("Failed to sent email...\nTry again.");
				dlgAlert.setTitle("Sent password failed");
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
	
	public boolean passwordsentStatus;
	public void sendForpasswordEmail( String   username, String  email) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();    

	    try {
	        
	        System.out.println("username :"+username+"\n" +
	        		"email: "+email);
	        
	        String tempParam  = "username=%s&email=%s";
	    	tempParam = String.format( tempParam , username , email);
	    	System.out.println(tempParam);
	    	URL url;
	    	String webUrlToFetch = "http://choicecaller.com/phoneapp/forgot.php?"+tempParam;
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

			Log.e("### Send Email Response", " "+sb.toString() );
	        
			if(sb.toString().toLowerCase().contains("password sent")){
				passwordsentStatus = true;
			}
	        	
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	    }
	} 
	
	
	public boolean checkParamBeforeSending(
			String username,
			String email
		){
		
		if(username .length() == 0||
				email .length() == 0
				){
			return false;
		}else{
			return true;
		}
		
	}
	
}
