package com.choicecaller.main;

public class CallHistory {
	
	int _callSilent;
	int _callRinged;
	int _callVibrate;
	
		
	
	
	public CallHistory(int _callSilent, int _callRinged, int _callVibrate) {
		super();
		
		this._callSilent = _callSilent;
		this._callRinged = _callRinged;
		this._callVibrate = _callVibrate;
	}
	
	public int get_callSilent() {
		return _callSilent;
	}
	public void set_callSilent(int _callSilent) {
		this._callSilent = _callSilent;
	}
	public int get_callRinged() {
		return _callRinged;
	}
	public void set_callRinged(int _callRinged) {
		this._callRinged = _callRinged;
	}
	public int get_callVibrate() {
		return _callVibrate;
	}
	public void set_callVibrate(int _callVibrate) {
		this._callVibrate = _callVibrate;
	}
	
}
