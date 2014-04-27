package com.choicecaller.main;

public class Contact {

	// private variables
	int _id;
	String _name;
	String _phone_number;
	String _activated ;
	String _ringOrVibrate;

	// Empty constructor
	public Contact() {

	}

	// constructor
	public Contact(int id, String name, String _phone_number , String _activated , String _ringOrVibrate ) {
		this._id = id;
		this._name = name;
		this._phone_number = _phone_number;
		this._activated = _activated;
		this._ringOrVibrate = _ringOrVibrate;
	}

	// constructor
	public Contact(String name, String _phone_number, String _activated , String _ringOrVibrate ) {
		this._name = name;
		this._phone_number = _phone_number;
		this._activated = _activated;
		this._ringOrVibrate = _ringOrVibrate;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting name
	public String getName() {
		return this._name;
	}

	// setting name
	public void setName(String name) {
		this._name = name;
	}

	// getting phone number
	public String getPhoneNumber() {
		return this._phone_number;
	}

	// setting phone number
	public void setPhoneNumber(String phone_number) {
		this._phone_number = phone_number;
	}
	
	//setting vibrateorring
	
	public void setVibrateOrRing(String _ringOrVibrate){
		this._ringOrVibrate = _ringOrVibrate;
	}
	
	public String getVibrateOrRing(){
		return this._ringOrVibrate;
	}
	
	// setting activated 
	public void setActivated (String _activated){
		this._activated = _activated;
	}
	
	public String getActivated (){
		return this._activated;
	}
	
	
}
