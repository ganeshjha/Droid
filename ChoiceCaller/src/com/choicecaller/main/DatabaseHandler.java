package com.choicecaller.main;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManagerRingOrVibrate";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "ContactsVibrateRingTable";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_ACTIVATED = "activated";
    private static final String KEY_VIBRATE_RING = "vibrateorring";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    //	db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
    	
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT,"
                + KEY_ACTIVATED + " TEXT,"
                + KEY_VIBRATE_RING + " TEXT" +
                " )";
        
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
 
        // Create tables again
        onCreate(db);
    }
 
    public void listAllTable(){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	
    	
    	
    	
        int i  = db.delete("MESSAGES", null, null);
        System.out.println(" > > MESSAGES > "+i);
        
        /*i =  db.delete("EmailProviderBody.db", null, null);
        System.out.println(" > > EmailProviderBody.db > "+i);
        
        i =  db.delete("webview.db", null, null);
        System.out.println(" > > webview.db > "+i);
        
        i =  db.delete("WebviewCatche.db", null, null);
        System.out.println(" > > WebviewCatche.db > "+i);*/
        
        /*
         * SQLiteDatabase db = this.getWritableDatabase();
    	
    	String selectQuery = "select name from sqlite_master where type = 'table'";   	 
        
         * Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Log.e("ColumnName_1", cursor.getColumnName(0));
               
            } while (cursor.moveToNext());
        }*/
        
                
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        values.put(KEY_ACTIVATED, contact.getActivated()); // contact activated ?
        values.put(KEY_VIBRATE_RING, contact.getVibrateOrRing());
        
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2) , cursor.getString(3), cursor.getString(4));
        // return contact
        return contact;
    }
     
    
    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contact.setActivated(cursor.getString(3));
                contact.setVibrateOrRing(cursor.getString(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        cursor.close();
        db.close();
        
        return contactList;
    }
 
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
        values.put(KEY_ACTIVATED , contact.getActivated());
        values.put(KEY_VIBRATE_RING, contact.getVibrateOrRing());
        
        
        
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_PH_NO + " = ?",
                new String[] { String.valueOf(contact.getPhoneNumber()) });
    }
    
    //select contact based on contact Number
    public List<Contact> getMatchedRows(String contactNumber){
    	List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "+KEY_PH_NO+ " = "+contactNumber;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contact.setActivated(cursor.getString(3));
                contact.setVibrateOrRing(cursor.getString(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }
    
 // Updating single contact
    public int updateContactAllFields(Contact contact, String oldID) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
        values.put(KEY_ID, contact.getID());
        values.put(KEY_ACTIVATED, contact.getActivated());
        values.put(KEY_VIBRATE_RING, contact.getVibrateOrRing());
        
        
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(oldID) });
    }
    
 // Deleting single contact
    public void deleteAllContact() {
    	
        SQLiteDatabase db = this.getWritableDatabase();
            int i  = db.delete(TABLE_CONTACTS, null, null);
            System.out.println(" > > > "+i);
    }
    
 
    // Deleting single contact
    public void deleteContact(Contact contact) {
    	
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }
 
 
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}