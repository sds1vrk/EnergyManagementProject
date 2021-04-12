package com.mir.ems.coap.emap;

import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.EMAP_CoAP_Schedule;
import com.mir.ems.globalVar.global;

public class Opt extends CoapResource {

	enum Type {
		CREATEOPT, CANCELOPT
	}

	EMAP_CoAP_Schedule scheduleProfile;
	Emap_Cema_Profile emaProfile;
	Emap_Device_Profile deviceProfile;
	private JSONObject jsonObj;

	public Opt(String name) {
		super(name);
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePOST(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePUT(CoapExchange exchange) {
		
		System.out.println(exchange.getRequestText().toString());
		System.out.println(getName());
		
		exchange.respond(ResponseCode.CONTENT, "Opt");
		
//		new OptType(getName(), exchange).start();

	}

	class OptType extends Thread{
		CoapExchange exchange;
		String incomingType, requestText, setPayload;

		OptType(String incomingType, CoapExchange exchange){
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
		}
		
		@Override
		public void run(){

			Type type = Type.valueOf(incomingType.toUpperCase());

			switch (type) {
			case CREATEOPT:
				this.setPayload = acknowledgeCREATEOPT(requestText);
				break;
			case CANCELOPT:
				this.setPayload = acknowledgeCANCELOPT(requestText);
				break;
			default:
				this.setPayload = "TYPE WRONG";
				break;
			}			
			
			if (this.setPayload.equals("TYPE WRONG"))
				this.exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");

			else
				this.exchange.respond(ResponseCode.CONTENT, setPayload, MediaTypeRegistry.APPLICATION_JSON);

		}
		
	}

	public String acknowledgeCREATEOPT(String requestText) {
		JSONObject drmsg = new JSONObject();

		try {

			emaProfile = new Emap_Cema_Profile();

			jsonObj = new JSONObject(requestText.toUpperCase());

			scheduleProfile = new EMAP_CoAP_Schedule(jsonObj.getString("SRCEMA"), jsonObj.getString("OPTID"),
					jsonObj.getString("OPTTYPE"), jsonObj.getDouble("REQUESTPOWER"), jsonObj.getInt("STARTYMD"),
					jsonObj.getInt("STARTTIME"), jsonObj.getInt("ENDYMD"), jsonObj.getInt("ENDTIME"));

			global.emaProtocolCoAP_Schedule.put(jsonObj.getString("OPTID"), scheduleProfile);

			drmsg.put("SrcEMA", global.SYSTEM_ID);
			drmsg.put("DestEMA", jsonObj.getString("SRCEMA"));
			drmsg.put("responseCode", 200);
			drmsg.put("responseDescription", "OK");
			drmsg.put("requestID", jsonObj.getInt("REQUESTID"));
			drmsg.put("optID", jsonObj.getString("OPTID"));
			drmsg.put("optStatus", "CoAP");
			drmsg.put("service", "CreatedOpt");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return drmsg.toString();
	}

	public String acknowledgeCANCELOPT(String requestText) {
		JSONObject drmsg = new JSONObject();

		try {

			jsonObj = new JSONObject(requestText.toUpperCase());

			global.emaProtocolCoAP_Schedule.remove(jsonObj.getString("OPTID"));

			drmsg.put("SrcEMA", global.SYSTEM_ID);
			drmsg.put("DestEMA", jsonObj.getString("SRCEMA"));
			drmsg.put("optID", jsonObj.getString("OPTID"));
			drmsg.put("responseCode", 200);
			drmsg.put("responseDescription", "OK");
			drmsg.put("requestID", jsonObj.getString("REQUESTID"));
			drmsg.put("time", new Date(System.currentTimeMillis()));
			drmsg.put("service", "CanceledOpt");


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return drmsg.toString();
	}
}
