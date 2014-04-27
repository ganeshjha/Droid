package com.choicecaller.main;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

/**
 * Class Receives the intent from BroadcastReceiver when phone restarts
 * 
 * @author Team AGA
 * 
 */

public class BootCompletedReceiver extends BroadcastReceiver {
	public void onReceive(Context paramContext, Intent paramIntent) {
		SharedPreferences pref = paramContext.getSharedPreferences(GlobalApplicationStatusActivity.PREFS_NAME,paramContext.MODE_PRIVATE);
		String gblOnOff = pref.getString(GlobalApplicationStatusActivity.PREF_ONOFF, "Off");

		if (gblOnOff.trim().equalsIgnoreCase("on")) {
			
			AudioManager am = (AudioManager) paramContext.getSystemService(Context.AUDIO_SERVICE);
			Log.i("MyApp", "Silent mode");
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

			AlarmManager alarmManager = (AlarmManager) paramContext
					.getSystemService(Context.ALARM_SERVICE);

			Intent intent = new Intent(paramContext,
					SendDailyCallHistoryDetailsToServer.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					paramContext, 0, intent, 0);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 2);
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
					pendingIntent);

		}
	}
}
