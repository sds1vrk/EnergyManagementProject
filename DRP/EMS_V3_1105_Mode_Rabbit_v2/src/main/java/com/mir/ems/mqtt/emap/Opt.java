package com.mir.ems.mqtt.emap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.database.item.Schedule_Profile;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.EMAP_CoAP_Schedule;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.Publishing;
import com.mir.ems.mqtt.emap.SessionSetup.Type;
import com.mir.ems.schedule.ScheduleManager;

public class Opt extends Thread {

	enum Type {
		CREATEOPT, CANCELOPT
	}

	EMAP_CoAP_Schedule scheduleProfile;
	Emap_Cema_Profile emaProfile;
	Emap_Device_Profile deviceProfile;
	private JSONObject jsonObj;

	MqttClient client;
	String sortTopic, setPayload, payload;
	String processTopic;;

	private String service;
	private String version = null;

	public Opt(MqttClient client, String service, JSONObject payload, String version) {

		this.client = client;
		this.service = service;
		this.payload = payload.toString();
		this.version = version;
	}

	public Opt(MqttClient client, String sortTopic, String processTopic, JSONObject payload) {
		this.client = client;
		this.sortTopic = sortTopic;
		this.processTopic = processTopic;
		this.payload = payload.toString();

	}

	@Override
	public void run() {

		Type type;
		if (version.equals("EMAP1.0b")) {
			type = Type.valueOf(service.toUpperCase());

		}
		
		else if(version.equals("OpenADR2.0b")){
			type = Type.valueOf(service.toUpperCase());

		}
		
		else {
			type = Type.valueOf(processTopic.toUpperCase());

		}
		switch (type) {
		case CREATEOPT:
			this.setPayload = acknowledgeCREATEOPT(this.payload);
			break;
		case CANCELOPT:
			this.setPayload = acknowledgeCANCELOPT(this.payload);
			break;
		default:
			this.setPayload = "TYPE WRONG";
			break;
		}

		if (this.setPayload.equals("TYPE WRONG")) {

		} else if (this.setPayload.toUpperCase().equals("NORESPONSE")) {

		} else {
			System.out.println(this.setPayload);
			String[] payloadSet = this.setPayload.split("&&");
			String srcID = payloadSet[2];
			String topic = "CEMA/" + srcID + "/" + "Opt" + "/" + payloadSet[0];
			new Publishing().publishThread(client, topic, global.qos, payloadSet[1].getBytes());

		}

	}

	public String acknowledgeCREATEOPT(String requestText) {
		
		JSONObject drmsg = new JSONObject();

		try {

			emaProfile = new Emap_Cema_Profile();

			jsonObj = new JSONObject(requestText.toUpperCase());

			JSONArray arr = new JSONArray(jsonObj.getString("AVAILABLE"));

			boolean available = false;

			for (int i = 0; i < arr.length(); i++) {

				JSONObject parseArr = new JSONObject(arr.get(0).toString());

				String strYMD = parseArr.getInt("STARTYMD") + "";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date requestDate = sdf.parse(strYMD);
				double requestPower = parseArr.getDouble("REQUESTPOWER");

				available = new ScheduleManager().availableCheck(requestDate, requestPower);

				if (!available)
					break;

			}

			System.err.println("Schedule" + available);

			if (available) {

				// scheduleProfile = new
				// EMAP_CoAP_Schedule(jsonObj.getString("SRCEMA"),
				// jsonObj.getString("OPTID"),
				// jsonObj.getString("OPTTYPE"),
				// jsonObj.getDouble("REQUESTPOWER"),
				// jsonObj.getInt("STARTYMD"),
				// jsonObj.getInt("STARTTIME"), jsonObj.getInt("ENDYMD"),
				// jsonObj.getInt("ENDTIME"));
				//
				// global.emaProtocolCoAP_Schedule.put(jsonObj.getString("OPTID"),
				// scheduleProfile);

				com.mir.ems.profile.emap.v2.CreatedOpt createdOpt = new com.mir.ems.profile.emap.v2.CreatedOpt();
				createdOpt.setSrcEMA(global.SYSTEM_ID);
				createdOpt.setDestEMA(jsonObj.getString("SRCEMA"));
				createdOpt.setRequestID(jsonObj.getString("REQUESTID"));
				createdOpt.setService("CreatedOpt");
				createdOpt.setResponseCode(200);
				createdOpt.setResponseDescription("OK");
				createdOpt.setOptID(emaProfile.getRequestID());
				createdOpt.setOptStatus("Accept");

				String topic = "/EMAP/CEMA/1.0b/Opt";
				String payload = createdOpt.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				// PUT REQUEST VALUE

				for (int i = 0; i < arr.length(); i++) {

					JSONObject parseArr = new JSONObject(arr.get(0).toString());

					String strYMD = parseArr.getInt("STARTYMD") + "";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					Date requestDate = sdf.parse(strYMD);
					double requestPower = parseArr.getDouble("REQUESTPOWER");
					String optID = jsonObj.getString("OPTID");
					global.scheduleDate.put(jsonObj.getString("SRCEMA"), new Schedule_Profile(
							jsonObj.getString("SRCEMA"), optID, requestPower, requestDate.toString()));

				}

				return "NORESPONSE";

			} else if (!available) {


				com.mir.ems.profile.emap.v2.CreatedOpt createdOpt = new com.mir.ems.profile.emap.v2.CreatedOpt();

				createdOpt.setSrcEMA(global.SYSTEM_ID);
				createdOpt.setDestEMA(jsonObj.getString("SRCEMA"));
				createdOpt.setRequestID(jsonObj.getString("REQUESTID"));
				createdOpt.setService("CreatedOpt");
				createdOpt.setResponseCode(400);
				createdOpt.setResponseDescription("BAD REQUEST");
				createdOpt.setOptID(emaProfile.getRequestID());
				createdOpt.setOptStatus("Deny");

				String topic = "/EMAP/CEMA/1.0b/Opt";
				String payload = createdOpt.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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
