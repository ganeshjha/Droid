package com.choicecaller.main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class GlobalApplicationStatusActivity extends Activity {

	ListView listViewContact;
	Button logoutBtn;
	ToggleButton toggleButtonOfOff;
	ArrayList<Bitmap> contactImage = new ArrayList<Bitmap>();
	ArrayList<String> First_lastName = new ArrayList<String>();
	ArrayList<String> phone_Number = new ArrayList<String>();
	ArrayList<String> dbContactSettingListing = new ArrayList<String>();

	ArrayList<String> seachAjaxSortList = new ArrayList<String>();

	protected static final String PREFS_NAME = "PrefChoiceCallerONOFF";
	protected static final String PREF_ONOFF = "off";
	EditText editTextAjxSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gbl_application_status);

		logoutBtn = (Button) findViewById(R.id.buttonLogOut);
		logoutBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GlobalApplicationStatusActivity.this.finish();
			}
		});

		DatabaseHandler db = new DatabaseHandler(this);
		listViewContact = (ListView) findViewById(R.id.listViewcontactsettings);

		editTextAjxSearch = (EditText) findViewById(R.id.editTextAjaxSearch);

		editTextAjxSearch.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				Log.e("KeyPressed", "" + editTextAjxSearch.getText());
				seachAjaxSortList.clear();

				for (int i = 0; i < dbContactSettingListing.size(); i++) {
					if (dbContactSettingListing
							.get(i)
							.toLowerCase()
							.contains(
									editTextAjxSearch.getText().toString()
											.toLowerCase())) {
						seachAjaxSortList.add(dbContactSettingListing.get(i));
					}
				}

				listViewContact.setAdapter(new ContactListAdapter(
						seachAjaxSortList,
						GlobalApplicationStatusActivity.this,
						new DatabaseHandler(getApplicationContext()),
						seachAjaxSortList));

				System.out.println("Seach Size: " + seachAjaxSortList.size());
				return false;
			}
		});

		// db.deleteAllContact();

		SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		String gblOnOff = pref.getString(PREF_ONOFF, "Off");

		toggleButtonOfOff = (ToggleButton) findViewById(R.id.toggleButtonOnOff);

		if (gblOnOff.equalsIgnoreCase("on")) {
			toggleButtonOfOff.setChecked(true);
			AudioManager am = (AudioManager) getApplicationContext()
					.getSystemService(Context.AUDIO_SERVICE);
			Log.i("GlobalApplication", "Silent mode");
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			
			sendDataIntent(getApplicationContext());
		} else {
			AudioManager am = (AudioManager) getApplicationContext()
					.getSystemService(Context.AUDIO_SERVICE);
			Log.i("MyApp", "General mode");
			am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			toggleButtonOfOff.setChecked(false);
		}

		toggleButtonOfOff.setText("");

		toggleButtonOfOff
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {
							getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
									.edit().putString(PREF_ONOFF, "on")
									.commit();
							
							sendDataIntent(getApplicationContext());
							
						} else {
							getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
									.edit().putString(PREF_ONOFF, "off")
									.commit();
						}

					}
				});

		// DatabaseHandler db = new DatabaseHandler(this);
		Log.d("Reading From Create: ", "Reading all contacts..");
		List<Contact> contacts = null;
		try {
			contacts = db.getAllContacts();
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (contacts == null || contacts.size() == 0) {
			new LoadContacts().execute();
		} else {
			Toast.makeText(getApplicationContext(),
					contacts.size() + " Contacts found...", Toast.LENGTH_LONG)
					.show();
			dbContactSettingListing.clear();
			for (Contact cn : contacts) {
				dbContactSettingListing.add(cn.getName() + ":"
						+ cn.getPhoneNumber() + ":" + cn.getActivated() + ":"
						+ cn.getVibrateOrRing() + ":" + cn.getID());

				Log.e("Contact Detail On Create: ",
						cn.getID() + ":" + cn.getName() + ":"
								+ cn.getPhoneNumber() + ":" + cn.getActivated()
								+ ":" + cn.getVibrateOrRing());
			}

			listViewContact.refreshDrawableState();
			listViewContact.setBackgroundColor(Color.BLACK);
			Collections.sort(dbContactSettingListing);

			listViewContact.setAdapter(new ContactListAdapter(
					dbContactSettingListing,
					GlobalApplicationStatusActivity.this, db,
					dbContactSettingListing));
			// db.close();

		}

	}

	public static String toInitCap(String param) {
		if (param != null && param.length() > 0) {
			char[] charArray = param.toCharArray(); // convert into char array
			charArray[0] = Character.toUpperCase(charArray[0]);
			return new String(charArray); // return desired output
		} else {
			return "";
		}
	}

	public void GetContactNamePhoneNumber() {

		First_lastName.clear();
		phone_Number.clear();
		DatabaseHandler db = new DatabaseHandler(this);

		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);

		while (phones.moveToNext()) {
			String name = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			Log.e("Name : ", name);
			First_lastName.add(toInitCap(name));

			Log.e("Number : ", phoneNumber);

			if (phoneNumber.length() >= 10) {
				phoneNumber = phoneNumber.replace("-", "");
				phoneNumber = phoneNumber.substring(phoneNumber.length() - 10,
						phoneNumber.length());
				System.out.println(phoneNumber);
			} else {
				phoneNumber = phoneNumber.replace("-", "");
				phoneNumber = phoneNumber.replace("+", "");
				System.out.println(phoneNumber);
			}

			phone_Number.add(phoneNumber);

			db.addContact(new Contact(name, phoneNumber, "false", "false"));

		}

		phones.close();
		// db.close();

	}

	public class ContactListAdapter extends BaseAdapter {

		ArrayList<String> _data;
		Context _c;
		DatabaseHandler db;
		ArrayList<String> list;

		ContactListAdapter(ArrayList<String> data, Context c,
				DatabaseHandler db, ArrayList<String> list) {
			_data = data;
			_c = c;
			this.db = db;
			this.list = list;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return _data.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return _data.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) _c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.item_of_contact_list, null);
			}

			ImageView imageView = (ImageView) v
					.findViewById(R.id.imageViewContact);

			imageView.setImageResource(R.drawable.ic_launcher);
			// ImageView imageView = new ImageView(_c);

			TextView textView_Contact_Name_last = (TextView) v
					.findViewById(R.id.textViewContactName);

			textView_Contact_Name_last
					.setText(list.get(position).split(":")[0]);

			ToggleButton toogleButtonOnOffVirbateOrRing = (ToggleButton) v
					.findViewById(R.id.toggleButtonOnOffVibrateOrRing);

			if (list.get(position).split(":")[2].equalsIgnoreCase("false")) {
				toogleButtonOnOffVirbateOrRing.setChecked(false);
			} else {
				toogleButtonOnOffVirbateOrRing.setChecked(true);
			}

			toogleButtonOnOffVirbateOrRing
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							// Toast.makeText(getApplicationContext(),"" +
							// isChecked, Toast.LENGTH_SHORT).show();
							System.out.println("Clicked  : "
									+ buttonView.isPressed());
							if (buttonView.isPressed()) {
								int updatedRow = db.updateContact(new Contact(
										list.get(position).split(":")[0], list
												.get(position).split(":")[1],
										"" + isChecked, dbContactSettingListing
												.get(position).split(":")[3]));

								Toast.makeText(getApplicationContext(),
										"Row updaetd: " + updatedRow,
										Toast.LENGTH_LONG).show();
							}

						}
					});

			ToggleButton toggleButtonVibrateRing = (ToggleButton) v
					.findViewById(R.id.toggleButtonVibrateRing);

			if (list.get(position).split(":")[3].equalsIgnoreCase("false")) {
				toggleButtonVibrateRing.setChecked(false);
			} else {
				toggleButtonVibrateRing.setChecked(true);
			}

			toggleButtonVibrateRing
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// Toast.makeText(getApplicationContext(),""" +
							// isChecked, Toast.LENGTH_SHORT).show();
							if (buttonView.isPressed()) {
								int updatedRow = db.updateContact(new Contact(
										list.get(position).split(":")[0], list
												.get(position).split(":")[1],
										list.get(position).split(":")[2], ""
												+ isChecked));

								Toast.makeText(getApplicationContext(),
										"Row updaetd: " + updatedRow,
										Toast.LENGTH_LONG).show();
							}

						}
					});

			return v;
		}
	}

	private ProgressDialog progressDialog;

	private class LoadContacts extends AsyncTask<Void, Integer, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(
					GlobalApplicationStatusActivity.this,
					"Refreshing Contact list ....", "Please wait...", false,
					false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			synchronized (this) {
				GetContactNamePhoneNumber();
			}

			return null;
		}

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values) {
			// set the current progress of the progress dialog
			progressDialog.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			progressDialog.dismiss();
			listViewContact.refreshDrawableState();
			listViewContact.setBackgroundColor(Color.BLACK);

			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			Log.d("Reading: ", "Reading all contacts..");
			List<Contact> contacts = db.getAllContacts();

			dbContactSettingListing.clear();

			for (Contact cn : contacts) {
				dbContactSettingListing.add(cn.getName() + ":"
						+ cn.getPhoneNumber() + ":" + cn.getActivated() + ":"
						+ cn.getVibrateOrRing() + ":" + cn.getID());

				Log.e("Contact Detail : ",
						cn.getName() + ":" + cn.getPhoneNumber() + ":"
								+ cn.getActivated() + ":"
								+ cn.getVibrateOrRing() + ":" + cn.getID());
			}

			Collections.sort(dbContactSettingListing);
			listViewContact.setAdapter(new ContactListAdapter(
					dbContactSettingListing,
					GlobalApplicationStatusActivity.this, db,
					dbContactSettingListing));

			db.close();
		}
	}

	public void sendDataIntent(Context context) {
		
		Log.e("####","Send data Intent"); 
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context,
				SendDailyCallHistoryDetailsToServer.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				0, AlarmManager.INTERVAL_DAY,
				pendingIntent);
	}
}
