package com.choicecaller.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class RingerModeStateChangeReceiver extends BroadcastReceiver {
	public void onReceive(Context paramContext, Intent paramIntent) {
		
		SharedPreferences localSharedPreferences = paramContext
				.getSharedPreferences(
						GlobalApplicationStatusActivity.PREFS_NAME,
						paramContext.MODE_PRIVATE);

		String OnOff = localSharedPreferences.getString(
				GlobalApplicationStatusActivity.PREF_ONOFF, "off");
		
		Bundle extras = paramIntent.getExtras();
		if (extras == null)
			return;
		String state = extras.getString(TelephonyManager.EXTRA_STATE);
		if (state == null)
			return;
		
		if (OnOff.trim().equalsIgnoreCase("on")) {
			AudioManager am = (AudioManager) paramContext
					.getSystemService(Context.AUDIO_SERVICE);
			System.out.println("Ringer mode Silent "+am.getRingerMode() +" x "+AudioManager.RINGER_MODE_SILENT);
			if (am.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
				Log.i("MyApp", "Ringer Mode Changing...");
				am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				Toast.makeText(paramContext, "ChoiceCaller: Silent Mode Activated...", Toast.LENGTH_SHORT).show();
			}
		}

	}
}
