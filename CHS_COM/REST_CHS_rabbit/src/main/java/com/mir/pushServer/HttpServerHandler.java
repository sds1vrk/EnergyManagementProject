package com.mir.pushServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import org.json.JSONException;

import com.mir.ems.globalVar.global;
import com.mir.ems.profile.emap.v2.Summary;
import com.mir.ems.profile.emap.v2.SummaryReport;
import com.mir.pushEventListener.EventResponder;
import com.mir.vtn.global.Global;
import com.mir.vtn.profile.registered.venIpInfo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpServerHandler {

	// public static ConcurrentHashMap<String, venRegister> map = new
	// ConcurrentHashMap<>();
	public static int cnt = 0;

	@SuppressWarnings("restriction")
	public static class EventObserveHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {

			new EventObserveHandle(he).start();

		}
	}

	@SuppressWarnings("restriction")
	public static class RootHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {

			new Temp(he).start();

		}
	}

	@SuppressWarnings("restriction")
	public static class SummaryHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {

			new SummaryHandlerThread(he).start();

		}
	}

	
	
	
	
	@SuppressWarnings("restriction")
	public static class EventACK implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {

			new EventACK_Thread().start();

//			new Temp(he).start();

		}
	}

}

@SuppressWarnings("restriction")

class EventObserveHandle extends Thread {
	HttpExchange he;

	EventObserveHandle(HttpExchange he) {
		this.he = he;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		String venID = he.getRequestURI().toString().split("=")[1];
		String ip = he.getRemoteAddress().toString().split("/")[1].split(":")[0];
		String hashedVenID = "";

		Iterator<String> it = Global.registerVEN.keySet().iterator();


		while (it.hasNext()) {

			String key = it.next();

			String strVenID = Global.registerVEN.get(key).getStrVenID();

			if (venID.equals(strVenID)) {
				hashedVenID = key;
				break;
			}

		}

		// Register Event Listener
		EventResponder responder = new EventResponder(he, venID);
		Global.initiater.addListener(responder);


		// Add on Global Map
		Global.pushRegisterVEN.put(venID, new venIpInfo(ip, venID, he, hashedVenID));

		System.out.println("PUSH REGISTER");
		
	}

}

class EventACK_Thread extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}

@SuppressWarnings("restriction")
class SummaryHandlerThread extends Thread {
	HttpExchange he;
	public SummaryHandlerThread(HttpExchange he) {
		// TODO Auto-generated constructor stub
		this.he = he;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			
			Thread.sleep(global.summaryInterval);
						
			Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();
			Summary sm = new Summary();

			while (it.hasNext()) {

				String key = it.next();

				sm.addsummaryParam(key, global.emaProtocolCoAP.get(key).getMargin(),
						global.emaProtocolCoAP.get(key).getAvgValue(), global.emaProtocolCoAP.get(key).getMaxValue(),
						global.emaProtocolCoAP.get(key).getGenerate(), global.emaProtocolCoAP.get(key).getStorage(),
						global.emaProtocolCoAP.get(key).getPower());

			}

			SummaryReport sr = new SummaryReport();
			sr.setDestEMA(he.getRequestURI().toString().split("/")[5]);
			sr.setRequestID("requestID");
			sr.setService("SummaryReport");
			sr.setSrcEMA(global.SYSTEM_ID);
			sr.setSummary(sm.getEventParams());
			sr.setSummaryType("SummaryReport");			
			
			he.getResponseHeaders().set("Content-Type",  "application/json");
			
			he.sendResponseHeaders(200,  sr.toString().length());
			
			OutputStream os= he.getResponseBody();
			os.write(sr.toString().getBytes());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
		
	}

}

@SuppressWarnings("restriction")
class Temp extends Thread {
	HttpExchange he;
	String emaID;

	Temp(HttpExchange he) {
		this.he = he;
//		this.emaID = emaID;
	}

	@Override
	public void run() {

		// TODO Auto-generated method stub
		super.run();

		String ip = he.getRemoteAddress().toString().split("/")[1].split(":")[0];
		String venID = "";
		String hashedVenID = "";

		System.out.println("heere");
		System.out.println(he.getRequestURI());

		Iterator<String> it = Global.registerVEN.keySet().iterator();

		while (it.hasNext()) {

			String key = it.next();

			String ipADDR = Global.registerVEN.get(key).getIpADDR();

			if (ipADDR.equals(ip)) {
				venID = Global.registerVEN.get(key).getStrVenID();
				hashedVenID = key;

				System.out.println("original");
				System.out.println(venID);
				System.out.println(hashedVenID);
				break;
			}

		}

		// Register Event Listener
		EventResponder responder = new EventResponder(he, ip);
		Global.initiater.addListener(responder);

		// Add on Global Map
		Global.pushRegisterVEN.put(ip, new venIpInfo(ip, venID, he, hashedVenID));

//		// Register Event Listener
//		EventResponder responder = new EventResponder(he, emaID);
//		Global.initiater.addListener(responder);
//
//		// Add on Global Map
//		Global.pushRegisterVEN.put(ip, new venIpInfo(emaID, venID, he, hashedVenID));

	}

}