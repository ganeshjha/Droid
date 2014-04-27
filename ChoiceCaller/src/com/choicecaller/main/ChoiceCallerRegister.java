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

public class ChoiceCallerRegister extends Activity {

	Button btnRegister;
	EditText email,username,password,mobileNumber, name;
	TextView choiceCaller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_new);

		
		btnRegister = (Button) findViewById(R.id.btnRegister);

		TextView textViewChoiceCaller = (TextView) findViewById(R.id.textViewContactName);
		Typeface font = Typeface.createFromAsset(getAssets(), "brushscriptstd.otf");  
		textViewChoiceCaller.setTypeface(font);
		

		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				email = (EditText)findViewById(R.id.emailEditText);
				username  = (EditText)findViewById(R.id.usernameEditText);
				password = (EditText)findViewById(R.id.passwordEditText);
				mobileNumber = (EditText)findViewById(R.id.mobileEditText);
				name = (EditText)findViewById(R.id.unameEditText);
				
				
				if(!checkParamBeforeSending(name.getText().toString().trim(),
						email.getText().toString().trim(),
						username.getText().toString().trim(),
						password.getText().toString().trim(),
						mobileNumber.getText().toString().trim()
						)){
					Toast.makeText(getApplicationContext(), "All fields are mandatory.\nPlease enter again.", Toast.LENGTH_LONG).show();
				}else{
					new RegisterData(email.getText().toString(),
							username.getText().toString() , 
							password .getText().toString(), 
							mobileNumber.getText().toString(), 
							name.getText().toString()).execute();
				}
				
				
			}
		});
	}
	
	public boolean checkParamBeforeSending(
			String name,
			String email,
			String username,
			String password,
			String mobile	
		){
		
		if(name.length() == 0 ||
				email.length() == 0 ||
				username .length() == 0||
				password .length() == 0||
				mobile.length() == 0
				){
			return false;
		}else{
			return true;
		}
		
	}
	
	private ProgressDialog progressDialog;
	private boolean registerStatus  = false;
	
	private class RegisterData extends AsyncTask<Void, Integer, Void>  
    {			
		
		String  email,username,password,mobileNumber, name;
		
		public RegisterData(String  email, String   username, String  password, String  mobileNumber, String name ) {
			// TODO Auto-generated constructor stub
			this.email = email;
			this.username = username; 
			this.password = password;
			this.mobileNumber = mobileNumber ;
			this.name = name;			
		}
		
        //Before running code in separate thread  
        @Override  
        protected void onPreExecute()  
        {  
        	progressDialog = ProgressDialog.show(ChoiceCallerRegister.this,"Registering user Details...",  
        		    "Please wait...", false, false); 
        }
  
        //The code to be executed in a background thread.  
        @Override  
        protected Void doInBackground(Void... params)  
        {  
              
                synchronized (this)  
                {   
                	registerStatus = false;
                	RegisterData(email, username, password, mobileNumber, name);
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
            if(registerStatus){
            	Toast.makeText(getApplicationContext(), "User Registered Sucessfully...", Toast.LENGTH_LONG).show();
            	Intent mainActivityAfterRegistration = new Intent(ChoiceCallerRegister.this, ChoiceCallerMainActivity.class);
            	startActivity(mainActivityAfterRegistration);
            }else{
            	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ChoiceCallerRegister.this);
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
	
	public void RegisterData(String  email, String   username, String  password, String  mobileNumber, String name) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    
	    
	    try {
	    	HttpPost httppost = new HttpPost("http://choicecaller.com/phoneapp/register.php");
	        // Add your data
	        /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair("name", name));
	        nameValuePairs.add(new BasicNameValuePair("email", email));
	        nameValuePairs.add(new BasicNameValuePair("username", username));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        nameValuePairs.add(new BasicNameValuePair("mobile", mobileNumber));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
*/
	    	String tempParam  = "name=%s&email=%s&username=%s&password=%s&mobile=%s";
	    	tempParam = String.format( tempParam , name, email , username , password, mobileNumber);
	    	System.out.println(tempParam);
	    	URL url;
	    	String webUrlToFetch = "http://choicecaller.com/phoneapp/register.php?"+tempParam;
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

			Log.e("### Registration Response", " "+sb.toString() );
			
			if(sb.toString().toLowerCase().contains("registration successful")){
				registerStatus = true;
			}
			
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
		 catch (Exception e) {
        // TODO Auto-generated catch block
    }
	} 

	
	
}
