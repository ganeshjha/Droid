package com.choicecaller.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseCallHistoryChoiceCaller extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME_CALL_HISTORY = "choiceCallerHistoryDaily";

	// Contacts table name
	private static final String TABLE_CALL_HITSORY = "choiceCallerHistoryTable";

	// Contacts Table Columns names
	private static final String KEY_CALL_USER_ID = "userID";
	private static final String KEY_CALL_SILENT = "Silent";
	private static final String KEY_CALL_RINGED = "Ringed";
	private static final String KEY_CALL_VIBRATE = "Vibrate";

	public DataBaseCallHistoryChoiceCaller(Context context) {
		super(context, DATABASE_NAME_CALL_HISTORY, null, DATABASE_VERSION);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE_CALL_hISOTRY = "CREATE TABLE " + TABLE_CALL_HITSORY				
				+ " (" +
				KEY_CALL_USER_ID + " TEXT , "+
				KEY_CALL_RINGED + " INTEGER, " 
				+ KEY_CALL_SILENT+ " INTEGER, "
				+ KEY_CALL_VIBRATE + " INTEGER" + " )";

		db.execSQL(CREATE_TABLE_CALL_hISOTRY);
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL_HITSORY);
		// Create tables again
		onCreate(db);
		db.close();
	}

	// Adding new contact
	public long  addCallEntry(CallHistory callHistory , String UserID) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CALL_USER_ID, UserID);
		values.put(KEY_CALL_SILENT, callHistory.get_callSilent());
		values.put(KEY_CALL_RINGED, callHistory.get_callRinged());
		values.put(KEY_CALL_VIBRATE, callHistory.get_callVibrate());
		
		// Inserting Row
		long l  = db.insert(TABLE_CALL_HITSORY, null, values);
		Log.e("###Rows Added #", ""+l);		
		db.close(); // Closing database connection
		return l;
	}

	// Getting single contact
	public CallHistory getCallHistoryRow(String UserID) {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_CALL_HITSORY, new String[] {
					KEY_CALL_RINGED, KEY_CALL_SILENT ,  KEY_CALL_VIBRATE }, 
					KEY_CALL_USER_ID + " =  ? ",
					new String[]{UserID},					
					null, 
					null, 
					null);

			if (cursor != null)
				cursor.moveToFirst();

			CallHistory callHistory = new CallHistory(cursor.getInt(0),
					cursor.getInt(1), cursor.getInt(2));

			
			return callHistory;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		// return contact

	}

	public int updateContactSilent(CallHistory callHistory , String User_Id) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CALL_SILENT, callHistory.get_callSilent());
		values.put(KEY_CALL_RINGED, callHistory.get_callRinged());
		values.put(KEY_CALL_VIBRATE, callHistory.get_callVibrate());

		// updating row
		int i =  db.update(TABLE_CALL_HITSORY, values, KEY_CALL_USER_ID + " = ?",
				new String[] { User_Id  });
		
		db.close();
		return i;
		
	}

	public int updateContactRinged(CallHistory callHistory , String User_Id) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CALL_SILENT, callHistory.get_callSilent());
		values.put(KEY_CALL_RINGED, callHistory.get_callRinged());
		values.put(KEY_CALL_VIBRATE, callHistory.get_callVibrate());

		// updating row
		int i =  db.update(TABLE_CALL_HITSORY, values, KEY_CALL_USER_ID + " = ?",
				new String[] { User_Id  });
		
		db.close();
		return i;
		
	}

	public int updateContactVibrate(CallHistory callHistory ,  String User_Id) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CALL_SILENT, callHistory.get_callSilent());
		values.put(KEY_CALL_RINGED, callHistory.get_callRinged());
		values.put(KEY_CALL_VIBRATE, callHistory.get_callVibrate());

		// updating row
		int i =  db.update(TABLE_CALL_HITSORY, values, KEY_CALL_USER_ID + " = ?",
				new String[] { User_Id  });
		
		db.close();
		return i;
	}

}
