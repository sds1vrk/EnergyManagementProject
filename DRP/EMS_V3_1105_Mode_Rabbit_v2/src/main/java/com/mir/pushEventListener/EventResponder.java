package com.mir.pushEventListener;


import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;
import com.mir.vtn.global.Global;
import com.mir.vtn.profile.EventDetail;
import com.mir.vtn.profile.openadr.DistributeEvent;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class EventResponder implements PushEventListener {

	private HttpExchange he;
	private String venID;
	
	public EventResponder(HttpExchange he, String venID) {
		setHe(he);
		setVenID(venID);
	}

	public HttpExchange getHe() {
		return he;
	}
	public void setHe(HttpExchange he) {
		this.he = he;
	}
	
	
//	public String getIpADDR() {
//		return ipADDR;
//	}
//	public void setIpADDR(String ipADDR) {
//		this.ipADDR = ipADDR;
//	}

	public String getVenID() {
		return venID;
	}

	public void setVenID(String venID) {
		this.venID = venID;
	}

	@Override
	public void eventNotification(EventDetail eventDetail) {
		// TODO Auto-generated method stub
		
		
//		if(eventDetail.getVenID().matches("Push|PUSH|push")) {
//			
//			System.out.println("여기는?");
//			
//			
//			String venID = Global.pushRegisterVEN.get(this.ipADDR).getHashedVenID();
//			System.out.println(venID);
////			String temp = "Event";
//			try {
//				DistributeEvent distributeEvent = new DistributeEvent();
//
//				System.out.println("@@");
//				System.out.println("푸쉬!");
//				System.out.println(venID);
//				
//				String strVenID = Global.registerVEN.get(venID).getStrVenID();
//				String dtStart = Global.eventInfo.get(strVenID).getDtStart();
//				int duration = Global.eventInfo.get(strVenID).getDuration();
//				String marketContextID = Global.eventInfo.get(strVenID).getMarket_context_id();
//				double payloadValue = Global.eventInfo.get(strVenID).getPayload_value();
//				int priority = Global.eventInfo.get(strVenID).getPriority();
//				String response_required_type_id = Global.eventInfo.get(strVenID).getResponse_required_type_id();
//				String signal_name_id = Global.eventInfo.get(strVenID).getSignal_name_id();
//				String signal_type_id = Global.eventInfo.get(strVenID).getSignal_type_id();
//				String vtn_comment = Global.eventInfo.get(strVenID).getVtn_comment();
//				boolean test_event = Global.eventInfo.get(strVenID).isTest_event();
//
//				String responseBody = distributeEvent.buildUp(venID, dtStart, duration, marketContextID, payloadValue, priority,
//						response_required_type_id, signal_name_id, signal_type_id, vtn_comment, test_event);
//				
//				HttpExchange he = Global.pushRegisterVEN.get(this.ipADDR).getEx();
//				
//				he.sendResponseHeaders(200, responseBody.length());
//				OutputStream os = he.getResponseBody();
//				os.write(responseBody.getBytes());
//				
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SAXException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParserConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (TransformerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
		if(Global.pushRegisterVEN.get(getVenID()).getVenID().equals(eventDetail.getVenID())) {
			
			String venID = Global.pushRegisterVEN.get(getVenID()).getHashedVenID();
//			String temp = "Event";
			
			System.out.println("===");
			System.out.println(venID);
			
			try {
				DistributeEvent distributeEvent = new DistributeEvent();

				String strVenID = Global.registerVEN.get(venID).getStrVenID();
				String dtStart = Global.eventInfo.get(strVenID).getDtStart();
				int duration = Global.eventInfo.get(strVenID).getDuration();
				String marketContextID = Global.eventInfo.get(strVenID).getMarket_context_id();
				double payloadValue = Global.eventInfo.get(strVenID).getPayload_value();
				int priority = Global.eventInfo.get(strVenID).getPriority();
				String response_required_type_id = Global.eventInfo.get(strVenID).getResponse_required_type_id();
				String signal_name_id = Global.eventInfo.get(strVenID).getSignal_name_id();
				String signal_type_id = Global.eventInfo.get(strVenID).getSignal_type_id();
				String vtn_comment = Global.eventInfo.get(strVenID).getVtn_comment();
				boolean test_event = Global.eventInfo.get(strVenID).isTest_event();
				String eventID = global.eventID.get(global.autoDRCNT);
				String responseBody = distributeEvent.buildUp(venID, dtStart, duration, marketContextID, payloadValue, priority,
						response_required_type_id, signal_name_id, signal_type_id, vtn_comment, test_event, eventID);
				
				HttpExchange he = Global.pushRegisterVEN.get(getVenID()).getEx();
				
				he.sendResponseHeaders(200, responseBody.length());
				he.getResponseHeaders().set("Content-Type",  "application/xml");
				OutputStream os = he.getResponseBody();
				os.write(responseBody.getBytes());
				
				global.autoDRCNT+=1;
				System.out.println(global.autoDRCNT+"====>");
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
