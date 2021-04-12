package com.mir.pushEventListener;

import com.mir.vtn.profile.EventDetail;

public interface PushEventListener {

	//When c-ema status disconnected
	void eventNotification(EventDetail eventDetail);
	
	
}
