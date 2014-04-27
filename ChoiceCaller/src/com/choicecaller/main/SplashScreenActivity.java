package com.choicecaller.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);
		TimerTask splashTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				Intent	i = new Intent(SplashScreenActivity.this, ChoiceCallerMainActivity.class);						        
				
				startActivity(i);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
			}
		};
		Timer run = new Timer();
		run.schedule(splashTask, 3*1000);
	}
}
