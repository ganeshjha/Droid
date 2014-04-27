package com.choicecaller.main;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCallStateListener extends BroadcastReceiver {
	private static boolean isRoaming;
	private static String callerNumber;
	

	private final static String WAS_RINGED = "RINGED_CALL";

	private final static String WAS_SILENT = "SILENT_CALL";

	private final static String WAS_VIBRATE = "VIBRATE_CALL";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras == null)
			return;
		String state = extras.getString(TelephonyManager.EXTRA_STATE);
		if (state == null)
			return;

		SharedPreferences localSharedPreferences = context
				.getSharedPreferences(
						GlobalApplicationStatusActivity.PREFS_NAME,
						context.MODE_PRIVATE);

		String OnOff = localSharedPreferences.getString(
				GlobalApplicationStatusActivity.PREF_ONOFF, "off");

		SharedPreferences UseriDShared = context.getSharedPreferences(
				ChoiceCallerMainActivity.PREFS_NAME, context.MODE_PRIVATE);

		String User_ID = UseriDShared.getString(
				ChoiceCallerMainActivity.PREF_User_Id, "NO_ID");

		// phone is ringing
		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			callerNumber = extras
					.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			isRoaming = tm.isNetworkRoaming();

			Log.d("TEST", "Is Roaming: " + isRoaming);
			Log.d("TEST", "caller number: " + callerNumber);

			Toast.makeText(context, "Incoming Call Number : " + callerNumber,
					Toast.LENGTH_SHORT).show();
			if (OnOff.trim().equalsIgnoreCase("on")) {

				if (callerNumber.startsWith("+")) {
					DatabaseHandler db = new DatabaseHandler(context);
					List<Contact> selectedList;

					if (callerNumber.length() >= 10) {
						callerNumber = callerNumber.replace("+", "");
						callerNumber = callerNumber.replace("-", "");
						callerNumber = callerNumber.substring(
								callerNumber.length() - 10,
								callerNumber.length());
					} else {
						callerNumber = callerNumber.replace("+", "");
						callerNumber = callerNumber.replace("-", "");
					}

					selectedList = db.getMatchedRows(callerNumber);

					for (int i = 0; i < selectedList.size(); i++) {

						String Temp = selectedList.get(i).getActivated() + ":"
								+ selectedList.get(i).getVibrateOrRing();
						System.out.println("###" + Temp);

						if (Temp.split(":")[0].trim().equalsIgnoreCase("true")) {
							if (Temp.split(":")[1].trim().equalsIgnoreCase(
									"true")) {
								Toast.makeText(context, "Found",
										Toast.LENGTH_SHORT).show();

								AudioManager am = (AudioManager) context
										.getSystemService(Context.AUDIO_SERVICE);
								Log.i("MyApp", "Vibrate mode");
								
								am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

								Toast.makeText(context,
										"" + am.getRingerMode(),
										Toast.LENGTH_SHORT).show();
								UpdateIncomingCallHistory(WAS_VIBRATE, User_ID,
										context);

							} else {
								AudioManager am = (AudioManager) context
										.getSystemService(Context.AUDIO_SERVICE);
								Log.i("MyApp", "Normal mode");
								
								am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

								UpdateIncomingCallHistory(WAS_RINGED, User_ID,
										context);
							}

						} else {
							AudioManager am = (AudioManager) context
									.getSystemService(Context.AUDIO_SERVICE);
							Log.i("MyApp", "Silent mode");
							
							am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
							UpdateIncomingCallHistory(WAS_SILENT, User_ID,
									context);
						}

					}

				} else {
					DatabaseHandler db = new DatabaseHandler(context);
					List<Contact> selectedList;

					if (callerNumber.length() >= 10) {
						callerNumber = callerNumber.replace("+", "");
						callerNumber = callerNumber.replace("-", "");
						callerNumber = callerNumber.substring(
								callerNumber.length() - 10,
								callerNumber.length());
					} else {
						callerNumber = callerNumber.replace("+", "");
						callerNumber = callerNumber.replace("-", "");
					}

					selectedList = db.getMatchedRows(callerNumber);

					Toast.makeText(context, "Found", Toast.LENGTH_SHORT).show();

					for (int i = 0; i < selectedList.size(); i++) {
						String Temp = selectedList.get(i).getActivated() + ":"
								+ selectedList.get(i).getVibrateOrRing();

						System.out.println("## set status #" + Temp);

						if (Temp.split(":")[0].trim().equalsIgnoreCase("true")) {
							if (Temp.split(":")[1].trim().equalsIgnoreCase(
									"true")) {
								Toast.makeText(context, "Found",
										Toast.LENGTH_SHORT).show();
								AudioManager am = (AudioManager) context
										.getSystemService(Context.AUDIO_SERVICE);
								Log.i("MyApp", "Vibrate mode");
								
								am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
								Toast.makeText(context,
										"" + am.getRingerMode(),
										Toast.LENGTH_SHORT).show();

								UpdateIncomingCallHistory(WAS_VIBRATE, User_ID,
										context);

							} else {
								AudioManager am = (AudioManager) context
										.getSystemService(Context.AUDIO_SERVICE);
								Log.i("MyApp", "Normal mode");
								
								am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

								UpdateIncomingCallHistory(WAS_RINGED, User_ID,
										context);
							}
						} else {
							AudioManager am = (AudioManager) context
									.getSystemService(Context.AUDIO_SERVICE);
							Log.i("MyApp", "Silent mode");
							
							am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

							UpdateIncomingCallHistory(WAS_SILENT, User_ID,
									context);
						}
					}

				}
			} else {
				UpdateIncomingCallHistory(WAS_RINGED, User_ID, context);
			}

			return;
		}

		// call was answered
		if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
			if (OnOff.equalsIgnoreCase("on")) {
				AudioManager am = (AudioManager) context
						.getSystemService(Context.AUDIO_SERVICE);
				Log.i("MyApp", "Silent mode");
				
				am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			} else {
				AudioManager am = (AudioManager) context
						.getSystemService(Context.AUDIO_SERVICE);
				Log.i("MyApp", "Silent mode");
				
				am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}

			return;
		}

		// call was ended
		if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

			if (OnOff.equalsIgnoreCase("on")) {
				AudioManager am = (AudioManager) context
						.getSystemService(Context.AUDIO_SERVICE);
				Log.i("MyApp", "Silent mode");
				
				am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

			} else {
				AudioManager am = (AudioManager) context
						.getSystemService(Context.AUDIO_SERVICE);
				Log.i("MyApp", "Silent mode");
				
				am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

			}

			return;
		}
	}

	public boolean UpdateIncomingCallHistory(String WhichCall, String UserId,
			Context context) {
		DataBaseCallHistoryChoiceCaller dataBaseCallHistoryChoiceCaller = new DataBaseCallHistoryChoiceCaller(
				context);
		CallHistory callHistory = dataBaseCallHistoryChoiceCaller
				.getCallHistoryRow(UserId);

		int _silent =  0;
		int _vibrate = 0 ;
		int _ringed = 0;

		if (callHistory != null) {

			if (WhichCall.trim().equalsIgnoreCase(WAS_RINGED)) {
				Log.e("Silent : ", "" + callHistory.get_callSilent());
				_silent = callHistory.get_callSilent();

				Log.e("Ringed : ", "" + callHistory.get_callRinged());
				_ringed = 1 + callHistory.get_callRinged();

				Log.e("Vibrate : ", "" + callHistory.get_callVibrate());
				_vibrate = callHistory.get_callVibrate();

			} else if (WhichCall.trim().equalsIgnoreCase(WAS_SILENT)) {

				Log.e("Silent : ", "" + callHistory.get_callSilent());
				_silent = 1 + callHistory.get_callSilent();

				Log.e("Ringed : ", "" + callHistory.get_callRinged());
				_ringed = callHistory.get_callRinged();

				Log.e("Vibrate : ", "" + callHistory.get_callVibrate());
				_vibrate = callHistory.get_callVibrate();

			} else if (WhichCall.trim().equalsIgnoreCase(WAS_VIBRATE)) {

				Log.e("Silent : ", "" + callHistory.get_callSilent());
				_silent = callHistory.get_callSilent();

				Log.e("Ringed : ", "" + callHistory.get_callRinged());
				_ringed = callHistory.get_callRinged();

				Log.e("Vibrate : ", "" + callHistory.get_callVibrate());
				_vibrate = 1 + callHistory.get_callVibrate();

			}

			int temp = dataBaseCallHistoryChoiceCaller.updateContactVibrate(
					new CallHistory(_silent, _ringed, _vibrate), UserId);
			Log.e("CallUpdate", "Updating Ringed Counter : " + temp);

		} else {

			if (WhichCall.trim().equalsIgnoreCase(WAS_RINGED)) {
				_ringed = 1;
			} else if (WhichCall.trim().equalsIgnoreCase(WAS_SILENT)) {
				_silent = 1;
			} else if (WhichCall.trim().equalsIgnoreCase(WAS_VIBRATE)) {
				_vibrate = 1;
			}

			Log.e("CallUpdate", "Inserting into DB... ");
			
			long temp = dataBaseCallHistoryChoiceCaller.addCallEntry(
					new CallHistory(_silent, _ringed, _vibrate), UserId);
			Log.e("CallUpdate", "Updating insert Counter : "+temp);
			
		}

		return true;
	}
}