package com.mir.ems.coap.emap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;


public class EventACK extends CoapResource{

	
	public EventACK(String name) {
		super(name);
	}
	
	public void handlePUT(CoapExchange exchange) {
		
	}
	
}
