package com.mir.pushEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mir.vtn.profile.EventDetail;

public class EventInitiater {
	private List<PushEventListener> listeners = new ArrayList<PushEventListener>();
	private ConcurrentHashMap<String, String> dup = new ConcurrentHashMap<>();
	
	public void addListener(PushEventListener toAdd) {

		EventResponder input = (EventResponder) toAdd;
		String venID = input.getVenID();
		
		if(!dup.containsKey(venID)) {
			dup.put(venID, venID);
			listeners.add(toAdd);
		}

	}

	public void eventOccur(EventDetail eventDetail) {

		// Notify everybody that may be interested.
		
		for (PushEventListener hl : listeners) {
			
			hl.eventNotification(eventDetail);

		}
		
	}
}