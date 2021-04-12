package com.mir.ems.mqtt.emap;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.GUI.MainFrame;
import com.mir.ems.algorithm.Greedy;
import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.EventResponder;
import com.mir.ems.mqtt.ProcessStatus;
import com.mir.ems.mqtt.Publishing;
import com.mir.ems.profile.emap.v2.Event;
import com.mir.ems.profile.emap.v2.EventResponse;
import com.mir.ems.profile.emap.v2.EventSignals;
import com.mir.ems.profile.emap.v2.Intervals;
import com.mir.ems.profile.emap.v2.Profile;
import com.mir.ems.profile.emap.v2.Transports;

public class SessionSetup extends Thread {

	enum Type {
		CONNECTREGISTRATION, CREATEPARTYREGISTRATION, REGISTERREPORT, POLL, REGISTEREDREPORT, REQUESTEVENT, CANCELPARTYREGISTRATION, PUSH
	}

	MqttClient client;
	String sortTopic;
	String processTopic;
	String payload;
	String setTopic, setPayload;

	Emap_Cema_Profile emaProfile;
	Emap_Device_Profile deviceProfile;
	private JSONObject jsonObj;
	private JSONObject sub1JsonObj;
	private JSONObject sub2JsonObj;
	private String service;

	private String version = null;

	public SessionSetup(MqttClient client, String service, JSONObject payload, String version) {

		this.client = client;
		this.service = service;
		this.payload = payload.toString();
		this.version = version;

	}

	public SessionSetup(MqttClient client, String sortTopic, String processTopic, JSONObject payload) {
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

		else if (version.equals("OpenADR2.0b")) {
			type = Type.valueOf(service.toUpperCase());

		}

		else {
			type = Type.valueOf(processTopic.toUpperCase());

		}
		switch (type) {
		case CONNECTREGISTRATION:
			this.setPayload = acknowledgeCONNECTREGISTRATION(payload);
			break;
		case CREATEPARTYREGISTRATION:
			this.setPayload = acknowledgeCREATEPARTYREGISTRATION(payload);
			break;
		case REGISTERREPORT:
			this.setPayload = acknowledgeREGISTERREPORT(payload);
			break;
		case POLL:
			this.setPayload = acknowledgePOLL(payload);
			break;
		case REGISTEREDREPORT:
			this.setPayload = acknowledgeREGISTEREDREPORT(payload);
			break;
		case REQUESTEVENT:
			this.setPayload = acknowledgeREQUESTEVENT(payload);
			break;
		case CANCELPARTYREGISTRATION:
			this.setPayload = acknowledgeCANCELPARTYREGISTRATION(payload);

			break;
		default:
			this.setPayload = "TYPE WRONG";
			break;
		}

		if (this.setPayload.equals("TYPE WRONG")) {

		} else if (this.setPayload.toUpperCase().equals("NORESPONSE")) {

		} else {
			String[] payloadSet = this.setPayload.split("&&");
			String srcID = payloadSet[2];
			String topic = "CEMA/" + srcID + "/" + "SessionSetup" + "/" + payloadSet[0];
			new Publishing().publishThread(client, topic, global.qos, payloadSet[1].getBytes());

		}
	}

	public String acknowledgeCONNECTREGISTRATION(String requestText) {



		ProcessStatus processStatus = new ProcessStatus();
		processStatus.setProcess_value(processTopic);
		JSONObject drmsg = new JSONObject();
		String srcEMA = null;

		if (version.equals("EMAP1.0b")) {

			

			
			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("SRCEMA");

				
				EventResponder responder = new EventResponder(this.client, version, srcEMA);
				global.initiater.addListener(responder);
				
				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

				Transports tr = new Transports().addTransportNameParams("MQTT").addTransportNameParams("CoAP");

				Profile p = new Profile().addProfileParams("EMAP1.0B", tr.getTransportNameParams())
						.addProfileParams("EMAP1.0A", tr.getTransportNameParams());

				com.mir.ems.profile.emap.v2.ConnectedPartyRegistration cr = new com.mir.ems.profile.emap.v2.ConnectedPartyRegistration();

				if (global.emaRegister.keySet().contains(srcEMA)) {

					cr.setResponseCode(200);
					cr.setResponseDescription("OK");
				} else {
					cr.setResponseCode(400);
					cr.setResponseDescription("Bad Request");
				}
				cr.setDestEMA(emaProfile.getEmaID());
				cr.setDuration(global.duration);
				cr.setProfile(p.getProfileParams());
				cr.setRequestID(emaProfile.getRequestID());
				// cr.setResponseCode(200);
				// cr.setResponseDescription("OK");
				cr.setService("ConnectedRegistration");
				cr.setSrcEMA(global.SYSTEM_ID);
				cr.setTime(new Date(System.currentTimeMillis()).toString());
				cr.setVersion(version);

				// String topic = "/EMAP/CEMA/1.0b/SessionSetup";

				String topic = "/EMAP/" + emaProfile.getEmaID() + "/1.0b/SessionSetup";

				String payload = cr.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else if (version.equals("OpenADR2.0b")) {
			

			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("VENID");

				
				EventResponder responder = new EventResponder(this.client, version, srcEMA);
				global.initiater.addListener(responder);
				
				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("VENID")).setRequestID(jsonObj.getString("REQUESTID"));

				com.mir.ems.profile.openadr.recent.Transports tr = new com.mir.ems.profile.openadr.recent.Transports().addTransportNameParams("MQTT").addTransportNameParams("CoAP");

				com.mir.ems.profile.openadr.recent.Profile p = new com.mir.ems.profile.openadr.recent.Profile().addProfileParams("OpenADR1.0b", tr.getTransportNameParams())
						.addProfileParams("OpenADR2.0b", tr.getTransportNameParams());

				com.mir.ems.profile.openadr.recent.ConnectedPartyRegistration cr = new com.mir.ems.profile.openadr.recent.ConnectedPartyRegistration();

				
				global.venRegisterFlag.put(srcEMA, 0);
				
				if (global.emaRegister.keySet().contains(srcEMA)) {

					cr.setResponseCode(200);
					cr.setResponseDescription("OK");
				} else {
					cr.setResponseCode(400);
					cr.setResponseDescription("Bad Request");
				}
				cr.setDestEMA(emaProfile.getEmaID());
				cr.setDuration(global.duration);
				cr.setProfile(p.getProfileParams());
				cr.setRequestID(emaProfile.getRequestID());
				cr.setService("oadrCreatedPartyRegistration");
				cr.setSrcEMA(global.SYSTEM_ID);

				String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiRegisterParty";

				String payload = cr.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {
			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("SRCEMA");

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
						.setType(jsonObj.getString("TYPE")).setqOs(jsonObj.getString("QOS"))
						.setVersion(jsonObj.getString("VERSION"))
						.setCustomerPriority(jsonObj.getInt("CUSTOMERPRIORITY"));

				// Security(Hash Algorithm - MD5 Apache Commons)
				String registrationID = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));

				drmsg.put("SrcEMA", global.SYSTEM_ID);
				drmsg.put("DestEMA", emaProfile.getEmaID());
				drmsg.put("responseCode", 200);
				drmsg.put("responseDescription", "OK");
				drmsg.put("requestID", emaProfile.getRequestID());
				drmsg.put("registrationID", registrationID);
				drmsg.put("transportName", "MQTT");
				drmsg.put("type", global.reportType);
				drmsg.put("QoS", emaProfile.getqOs());
				drmsg.put("version", emaProfile.getVersion());
				drmsg.put("duration", 1000);
				drmsg.put("customerPriority", emaProfile.getCustomerPriority());
				drmsg.put("time", new Date(System.currentTimeMillis()));
				drmsg.put("profileName", global.profileName);
				drmsg.put("service", "ConnectedRegistration");

				String qos = jsonObj.getString("QOS");

				emaProfile = new Emap_Cema_Profile(jsonObj.getString("SRCEMA"), qos, registrationID, 0,
						jsonObj.getInt("CUSTOMERPRIORITY"), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, new JSONObject(),
						new JSONObject(), "CONNECT");

				global.putEmaProtocolCoAP(jsonObj.getString("SRCEMA"), emaProfile);

				global.emaProtocolCoAP_EventFlag.put(jsonObj.getString("SRCEMA"), new EMAP_CoAP_EMA_DR());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "ConnectedRegistration" + "&&" + drmsg.toString() + "&&" + srcEMA;
	}

	public String acknowledgeCREATEPARTYREGISTRATION(String requestText) {
		JSONObject drmsg = new JSONObject();
		String srcEMA = null;

		if (version.equals("EMAP1.0b")) {

			try {

				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("SRCEMA");

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
						.setVersion(jsonObj.getString("PROFILENAME"))
						.setTransportName(jsonObj.getString("TRANSPORTNAME"))
						.setPullModel(Boolean.parseBoolean(jsonObj.getString("HTTPPULLMODEL")));

				com.mir.ems.profile.emap.v2.CreatedPartyRegistration cdp = new com.mir.ems.profile.emap.v2.CreatedPartyRegistration();

				String registrationID = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));

				Transports tr = new Transports().addTransportNameParams("MQTT").addTransportNameParams("CoAP");

				Profile p = new Profile().addProfileParams("EMAP1.0B", tr.getTransportNameParams())
						.addProfileParams("EMAP1.0A", tr.getTransportNameParams());

				cdp.setDestEMA(emaProfile.getEmaID());
				cdp.setDuration(global.duration);
				cdp.setProfile(p.getProfileParams());
				cdp.setRequestID(emaProfile.getRequestID());
				cdp.setResponseCode(200);
				cdp.setResponseDescription("OK");
				cdp.setService("CreatedPartyRegistration");
				cdp.setSrcEMA(global.SYSTEM_ID);
				cdp.setTime(new Date(System.currentTimeMillis()).toString());
				cdp.setVersion(version);
				cdp.setRegistrationID(registrationID);

				String topic = "/EMAP/" + emaProfile.getEmaID() + "/1.0b/SessionSetup";

				String payload = cdp.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				boolean realTimetableChanged = !emaProfile.isPullModel() && global.realTimeTableHasChanged ? true
						: false;

				Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", emaProfile.getEmaID(), registrationID, null, null, 0,
						0, 0, 0, 0, 0, 0, 0, null, null, 0, emaProfile.isPullModel(), global.tableHasChanged,
						realTimetableChanged, "CONNECT");

				global.emaProtocolCoAP.put(emaProfile.getEmaID(), profile);
				global.emaProtocolCoAP_EventFlag.put(emaProfile.getEmaID(), new EMAP_CoAP_EMA_DR());

				return "NORESPONSE";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else if (version.equals("OpenADR2.0b")) {

			try {

				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("OADRVENNAME");

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("OADRVENNAME")).setRequestID(jsonObj.getString("REQUESTID"))
						.setPullModel(Boolean.parseBoolean(jsonObj.getString("OADRHTTPPULLMODEL")));

				com.mir.ems.profile.openadr.recent.CreatedPartyRegistration cdp = new com.mir.ems.profile.openadr.recent.CreatedPartyRegistration();

				String registrationID = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));

				com.mir.ems.profile.openadr.recent.Transports tr = new com.mir.ems.profile.openadr.recent.Transports().addTransportNameParams("MQTT").addTransportNameParams("CoAP");

				com.mir.ems.profile.openadr.recent.Profile p = new com.mir.ems.profile.openadr.recent.Profile().addProfileParams("OpenADR1.0b", tr.getTransportNameParams())
						.addProfileParams("OpenADR2.0b", tr.getTransportNameParams());

				cdp.setDestEMA(emaProfile.getEmaID());
				cdp.setDuration(global.duration);
				cdp.setProfile(p.getProfileParams());
				cdp.setRequestID(emaProfile.getRequestID());
				cdp.setResponseCode(200);
				cdp.setResponseDescription("OK");
				cdp.setService("oadrCreatedPartyRegistration");
				cdp.setSrcEMA(global.SYSTEM_ID);
				cdp.setRegistrationID(registrationID);

				String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiRegisterParty";

				String payload = cdp.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				boolean realTimetableChanged = !emaProfile.isPullModel() && global.realTimeTableHasChanged ? true
						: false;

				Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", emaProfile.getEmaID(), registrationID, null, null, 0,
						0, 0, 0, 0, 0, 0, 0, null, null, 0, emaProfile.isPullModel(), global.tableHasChanged,
						realTimetableChanged, "CONNECT");

				global.emaProtocolCoAP.put(emaProfile.getEmaID(), profile);
				global.emaProtocolCoAP_EventFlag.put(emaProfile.getEmaID(), new EMAP_CoAP_EMA_DR());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {
			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("SRCEMA");

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
						.setVersion(jsonObj.getString("VERSION")).setProfileName(jsonObj.getString("PROFILENAME"))
						.setRegistrationID(jsonObj.getString("REGISTRATIONID"));

				drmsg.put("SrcEMA", global.SYSTEM_ID);
				drmsg.put("DestEMA", emaProfile.getEmaID());
				drmsg.put("responseCode", 200);
				drmsg.put("responseDescription", "OK");
				drmsg.put("requestID", emaProfile.getRequestID());
				drmsg.put("registrationID", emaProfile.getRegistrationID());
				drmsg.put("transportName", "MQTT");
				drmsg.put("version", emaProfile.getVersion());
				drmsg.put("duration", 1000);
				drmsg.put("profileName", emaProfile.getProfileName());
				drmsg.put("time", new Date(System.currentTimeMillis()));
				drmsg.put("service", "CreatedPartyRegistration");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "CreatedPartyRegistration" + "&&" + drmsg.toString() + "&&" + srcEMA;
	}

	public String acknowledgeREGISTERREPORT(String requestText) {

		JSONObject drmsg = new JSONObject();
		String srcEMA = null;

		if (version.equals("EMAP1.0b")) {

			try {
				jsonObj = new JSONObject(requestText);

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SrcEMA")).setRequestID(jsonObj.getString("requestID"));

				JSONArray reportArr = new JSONArray(jsonObj.getString("report"));

				for (int i = 0; i < reportArr.length(); i++) {

					JSONArray decrArr = new JSONArray(
							new JSONObject(reportArr.get(i).toString()).getString("reportDescription"));

					if (jsonObj.getString("type").equals("Implicit") || 1 == decrArr.length()) {

//						System.err.println("IMPLCIT");

						JSONObject decr = new JSONObject(decrArr.get(0).toString());

						String qos, registrationID, state, minTime, maxTime;
						double margin, minValue, maxValue, avgValue, power, generate, storage;
						int priority, dimming;
						boolean pullModel;
						qos = decr.getString("qos");
						registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
						margin = decr.getDouble("margin");
						minValue = decr.getDouble("minValue");
						maxValue = decr.getDouble("maxValue");
						avgValue = decr.getDouble("avgValue");
						minTime = decr.getString("minTime");
						maxTime = decr.getString("maxTime");
						power = decr.getDouble("power");
						generate = decr.getDouble("generate");
						storage = decr.getDouble("storage");
						state = decr.getString("state");
						priority = decr.getInt("priority");
						dimming = decr.getInt("dimming");
						pullModel = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();

						boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
								.isRealTimetableChanged();
						boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isTableChanged();

						Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", jsonObj.getString("SrcEMA"), registrationID,
								qos, state, power, dimming, margin, generate, storage, maxValue, minValue, avgValue,
								maxTime, minTime, priority, pullModel, timeTable, realTimetable, "CONNECT");

						global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);
						global.emaThresholdManage.put(jsonObj.getString("SrcEMA"), avgValue);

					}

					else if (jsonObj.getString("type").equals("Explicit") || 1 < decrArr.length()) {

//						System.err.println("Explicit");

						for (int j = 0; j < decrArr.length(); j++) {

							JSONObject decr = new JSONObject(decrArr.get(j).toString());

							String qos, registrationID, state, minTime, maxTime, rID;
							double margin, minValue, maxValue, avgValue, power, generate, storage;
							int priority, dimming;
							boolean pullModel;

							if (decr.getString("deviceType").equals("EMA")) {
								qos = decr.getString("qos");
								registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
								margin = decr.getDouble("margin");
								minValue = decr.getDouble("minValue");
								maxValue = decr.getDouble("maxValue");
								avgValue = decr.getDouble("avgValue");
								minTime = decr.getString("minTime");
								maxTime = decr.getString("maxTime");
								power = decr.getDouble("power");
								generate = decr.getDouble("generate");
								storage = decr.getDouble("storage");
								state = decr.getString("state");
								priority = decr.getInt("priority");
								dimming = decr.getInt("dimming");
								pullModel = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();

								boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
										.isRealTimetableChanged();
								boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isTableChanged();

								Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", jsonObj.getString("SrcEMA"),
										registrationID, qos, state, power, dimming, margin, generate, storage, maxValue,
										minValue, avgValue, maxTime, minTime, priority, pullModel, timeTable,
										realTimetable, "CONNECT");

								global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);
								global.emaThresholdManage.put(jsonObj.getString("SrcEMA"), avgValue);

							}

							else {

								rID = decr.getString("rID");
								qos = decr.getString("qos");
								margin = decr.getDouble("margin");
								minValue = decr.getDouble("minValue");
								maxValue = decr.getDouble("maxValue");
								avgValue = decr.getDouble("avgValue");
								minTime = decr.getString("minTime");
								maxTime = decr.getString("maxTime");
								power = decr.getDouble("power");
								generate = decr.getDouble("generate");
								storage = decr.getDouble("storage");
								state = decr.getString("state");
								priority = decr.getInt("priority");
								dimming = decr.getInt("dimming");

//								System.err.println("POWER" + power);

								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"), rID,
										decr.getString("deviceType"), qos, state, power, dimming, margin, generate,
										storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

								global.emaProtocolCoAP_Device.put(rID, deviceProfile);

							}

							JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

							for (int k = 0; k < powerAtts.length(); k++) {
								JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
								// System.out.println(powerAtt.getString("hertz"));
							}

						}

					}

				}

				com.mir.ems.profile.emap.v2.RegisteredReport rdt = new com.mir.ems.profile.emap.v2.RegisteredReport();
				rdt.setDestEMA(emaProfile.getEmaID());
				rdt.setRequestID(emaProfile.getRequestID());
				rdt.setResponseCode(200);
				rdt.setResponseDescription("OK");
				rdt.setService("RegisteredReport");
				rdt.setSrcEMA(global.SYSTEM_ID);
				rdt.setTime(new Date(System.currentTimeMillis()).toString());
				// rdt.setType(global.reportType);

				// String topic = "/EMAP/CEMA/1.0b/SessionSetup";
				//
				String topic = "/EMAP/" + emaProfile.getEmaID() + "/1.0b/SessionSetup";

				String payload = rdt.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (version.equals("OpenADR2.0b")) {

			try {
				jsonObj = new JSONObject(requestText);

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("venID")).setRequestID(jsonObj.getString("requestID"));

				JSONArray reportArr = new JSONArray(jsonObj.getString("oadrReport"));

				for (int i = 0; i < reportArr.length(); i++) {

					JSONArray decrArr = new JSONArray(
							new JSONObject(reportArr.get(i).toString()).getString("oadrReportDescription"));
					//
					// if (jsonObj.getString("type").equals("Implicit") || 1 ==
					// decrArr.length()) {
					//
					// System.err.println("IMPLCIT");
					//
					// JSONObject decr = new
					// JSONObject(decrArr.get(0).toString());
					//
					// String qos, registrationID, state, minTime, maxTime;
					// double margin, minValue, maxValue, avgValue, power,
					// generate, storage;
					// int priority, dimming;
					// boolean pullModel;
					// qos = decr.getString("qos");
					// registrationID =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
					// margin = decr.getDouble("margin");
					// minValue = decr.getDouble("minValue");
					// maxValue = decr.getDouble("maxValue");
					// avgValue = decr.getDouble("avgValue");
					// minTime = decr.getString("minTime");
					// maxTime = decr.getString("maxTime");
					// power = decr.getDouble("power");
					// generate = decr.getDouble("generate");
					// storage = decr.getDouble("storage");
					// state = decr.getString("state");
					// priority = decr.getInt("priority");
					// dimming = decr.getInt("dimming");
					// pullModel =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();
					//
					// boolean realTimetable =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isRealTimetableChanged();
					// boolean timeTable =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isTableChanged();
					//
					// Emap_Cema_Profile profile = new
					// Emap_Cema_Profile(jsonObj.getString("SrcEMA"),
					// registrationID,
					// qos, state, power, dimming, margin, generate, storage,
					// maxValue, minValue, avgValue,
					// maxTime, minTime, priority, pullModel, timeTable,
					// realTimetable, "CONNECT");
					//
					// global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"),
					// profile);
					// global.emaThresholdManage.put(jsonObj.getString("SrcEMA"),
					// avgValue);
					//
					// }
					//
					// else if (jsonObj.getString("type").equals("Explicit") ||
					// 1 < decrArr.length()) {
					//
					// System.err.println("Explicit");
					//
					// for (int j = 0; j < decrArr.length(); j++) {
					//
					// JSONObject decr = new
					// JSONObject(decrArr.get(j).toString());
					//
					// String qos, registrationID, state, minTime, maxTime, rID;
					// double margin, minValue, maxValue, avgValue, power,
					// generate, storage;
					// int priority, dimming;
					// boolean pullModel;
					//
					// if (decr.getString("deviceType").equals("EMA")) {
					// qos = decr.getString("qos");
					// registrationID =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
					// margin = decr.getDouble("margin");
					// minValue = decr.getDouble("minValue");
					// maxValue = decr.getDouble("maxValue");
					// avgValue = decr.getDouble("avgValue");
					// minTime = decr.getString("minTime");
					// maxTime = decr.getString("maxTime");
					// power = decr.getDouble("power");
					// generate = decr.getDouble("generate");
					// storage = decr.getDouble("storage");
					// state = decr.getString("state");
					// priority = decr.getInt("priority");
					// dimming = decr.getInt("dimming");
					// pullModel =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();
					//
					//
					// boolean realTimetable =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isRealTimetableChanged();
					// boolean timeTable =
					// global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isTableChanged();
					//
					// Emap_Cema_Profile profile = new
					// Emap_Cema_Profile(jsonObj.getString("SrcEMA"),
					// registrationID, qos, state, power, dimming, margin,
					// generate, storage, maxValue,
					// minValue, avgValue, maxTime, minTime, priority,
					// pullModel, timeTable, realTimetable, "CONNECT");
					//
					// global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"),
					// profile);
					// global.emaThresholdManage.put(jsonObj.getString("SrcEMA"),
					// avgValue);
					//
					// }
					//
					// else {
					//
					// rID = decr.getString("rID");
					// qos = decr.getString("qos");
					// margin = decr.getDouble("margin");
					// minValue = decr.getDouble("minValue");
					// maxValue = decr.getDouble("maxValue");
					// avgValue = decr.getDouble("avgValue");
					// minTime = decr.getString("minTime");
					// maxTime = decr.getString("maxTime");
					// power = decr.getDouble("power");
					// generate = decr.getDouble("generate");
					// storage = decr.getDouble("storage");
					// state = decr.getString("state");
					// priority = decr.getInt("priority");
					// dimming = decr.getInt("dimming");
					//
					// System.err.println("POWER"+power);
					//
					//
					// deviceProfile = new
					// Emap_Device_Profile(jsonObj.getString("SrcEMA"), rID,
					// decr.getString("deviceType"), qos, state, power, dimming,
					// margin, generate,
					// storage, maxValue, minValue, avgValue, maxTime, minTime,
					// priority);
					//
					// global.emaProtocolCoAP_Device.put(rID, deviceProfile);
					//
					// }
					//
					// JSONArray powerAtts = new
					// JSONArray(decr.getString("powerAttributes"));
					//
					// for (int k = 0; k < powerAtts.length(); k++) {
					// JSONObject powerAtt = new
					// JSONObject(powerAtts.get(k).toString());
					// // System.out.println(powerAtt.getString("hertz"));
					// }
					//
					// }
					//
					// }

				}

				com.mir.ems.profile.openadr.recent.RegisteredReport rdt = new com.mir.ems.profile.openadr.recent.RegisteredReport();
				rdt.setDestEMA(emaProfile.getEmaID());
				rdt.setRequestID(emaProfile.getRequestID());
				rdt.setResponseCode(200);
				rdt.setResponseDescription("OK");
				rdt.setService("oadrRegisteredReport");
				//
				String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiReport";

				String payload = rdt.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				jsonObj = new JSONObject(requestText);

				srcEMA = jsonObj.getString("SrcEMA");

				emaProfile = new Emap_Cema_Profile();

				JSONObject jsonObj = new JSONObject(requestText);
				JSONObject sub2JsonObj = new JSONObject(jsonObj.getString("EMAregisteredMgnInformation"));

				emaProfile.setEmaID(jsonObj.getString("SrcEMA")).setRequestID(jsonObj.getString("requestID"));

				String emaID = jsonObj.getString("SrcEMA");

				Emap_Cema_Profile originVal = global.getEmaProtocolCoAP().get(emaID);

				String qos, registrationID;
				int emaCnt, customerPriority;
				double margin, minValue, maxValue, avgValue, power, generate, storage;
				JSONObject EMAregisteredDRInformation, EMAregisteredMgnInformation;

				qos = originVal.getqOs().toString();
				registrationID = originVal.getRegistrationID();
				emaCnt = sub2JsonObj.getInt("emaCNT");
				customerPriority = originVal.getCustomerPriority();
				margin = sub2JsonObj.getDouble("margin");
				minValue = sub2JsonObj.getDouble("minValue");
				maxValue = sub2JsonObj.getDouble("maxValue");
				avgValue = sub2JsonObj.getDouble("avgValue");
				power = sub2JsonObj.getDouble("power");
				generate = sub2JsonObj.getDouble("generate");
				storage = sub2JsonObj.getDouble("storage");
				EMAregisteredDRInformation = jsonObj.getJSONObject("EMAregisteredDRInformation");
				EMAregisteredMgnInformation = jsonObj.getJSONObject("EMAregisteredMgnInformation");

				Emap_Cema_Profile emaProfile = new Emap_Cema_Profile(emaID, qos, registrationID, emaCnt,
						customerPriority, margin, minValue, maxValue, avgValue, power, generate, storage,
						EMAregisteredDRInformation, EMAregisteredMgnInformation, "CONNECT");

				if (sub2JsonObj.getInt("emaCNT") > 0) {
					if (!sub2JsonObj.isNull("topology")) {

						JSONArray topologyInfo = new JSONArray(sub2JsonObj.getString("topology"));
						for (int i = 0; i < topologyInfo.length(); i++) {

							sub1JsonObj = new JSONObject(topologyInfo.get(i).toString());

							if (sub1JsonObj.getString("deviceType").equals("LED")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), sub1JsonObj.getInt("dimming"), 0,
										sub1JsonObj.getInt("priority"), sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0,
										0.0, 0.0);
							} else if (sub1JsonObj.getString("deviceType").equals("PV")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, 0, sub1JsonObj.getInt("priority"),
										sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0, 0.0, 0.0);
							} else if (sub1JsonObj.getString("deviceType").equals("ESS")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, sub1JsonObj.getInt("mode"),
										sub1JsonObj.getInt("priority"), sub1JsonObj.getDouble("power"),
										sub1JsonObj.getDouble("capacity"), sub1JsonObj.getDouble("volt"),
										sub1JsonObj.getDouble("hz"), sub1JsonObj.getDouble("changedEnergy"),
										sub1JsonObj.getDouble("soc"));
							} else if (sub1JsonObj.getString("deviceType").equals("RECLOSER")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, 0, sub1JsonObj.getInt("priority"),
										sub1JsonObj.getDouble("power"), 0.0, sub1JsonObj.getDouble("volt"),
										sub1JsonObj.getDouble("hz"), 0.0, 0.0);
							} else if (sub1JsonObj.getString("deviceType").equals("RESOURCE")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, 0, sub1JsonObj.getInt("priority"),
										sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0, 0.0, 0.0);
							}
							global.emaProtocolCoAP_Device.put(sub1JsonObj.getString("deviceEMAID"), deviceProfile);

						}
					}
				}
				global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), emaProfile);

				drmsg.put("SrcEMA", global.SYSTEM_ID);
				drmsg.put("DestEMA", emaProfile.getEmaID());
				drmsg.put("responseCode", 200);
				drmsg.put("responseDescription", "OK");
				drmsg.put("requestID", emaProfile.getRequestID());
				drmsg.put("transportName", "MQTT");
				drmsg.put("version", emaProfile.getVersion());
				drmsg.put("threshold", sub2JsonObj.getDouble("margin"));
				drmsg.put("time", new Date(System.currentTimeMillis()));
				drmsg.put("service", "RegisteredReport");

				// System.out.println(drmsg.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "RegisteredReport" + "&&" + drmsg.toString() + "&&" + srcEMA;

	}

	public String acknowledgePOLL(String requestText) {
		JSONObject drmsg = new JSONObject();
		String srcEMA = null;
		if (version.equals("EMAP1.0b")) {

			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				emaProfile = new Emap_Cema_Profile();
				emaProfile.setEmaID(jsonObj.getString("SRCEMA"));


				com.mir.ems.profile.emap.v2.RegisterReport rt2 = new com.mir.ems.profile.emap.v2.RegisterReport();
				rt2.setSrcEMA(global.SYSTEM_ID);
				rt2.setDestEMA(emaProfile.getEmaID());
				rt2.setRequestID(emaProfile.getRequestID());
				rt2.setService("RegisterReport");
				rt2.setTime(new Date(System.currentTimeMillis()).toString());

				// String topic = "/EMAP/CEMA/1.0b/SessionSetup";

				String topic = "/EMAP/" + emaProfile.getEmaID() + "/1.0b/SessionSetup";

				String payload = rt2.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "NORESPONSE";

		} else if (version.equals("OpenADR2.0b")) {


			try {
				jsonObj = new JSONObject(requestText.toUpperCase());
				
				srcEMA = jsonObj.getString("VENID");
				global.venRegisterFlag.replace(srcEMA, 1);

				emaProfile = new Emap_Cema_Profile();
				emaProfile.setEmaID(jsonObj.getString("VENID"));

				com.mir.ems.profile.openadr.recent.RegisterReport rt2 = new com.mir.ems.profile.openadr.recent.RegisterReport();
				rt2.setDestEMA(emaProfile.getEmaID());
				rt2.setRequestID("REQUESTID");
				rt2.setService("oadrRegisterReport");

				String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiReport";
				String payload = rt2.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "NORESPONSE";

		}

		else {
			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("SRCEMA");

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setType(jsonObj.getString("TYPE"));

				if (emaProfile.getType().toUpperCase().equals("REGISTRATION")) {
					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("version", emaProfile.getVersion());
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("reportName", "InitReport");
					drmsg.put("reportType", "Registration");
					drmsg.put("EMAregisteredDRInformation",
							global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getEMARegisteredInfo());
					drmsg.put("EMAregisteredMgnInformation",
							global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getEMARegisteredMgnInfo());

					drmsg.put("time", new Date(System.currentTimeMillis()));
					drmsg.put("service", "RegisterReport");

					return "RegisterReport" + "&&" + drmsg.toString() + "&&" + srcEMA;

				} else {
					return "NORESPONSE";
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "NORESPONSE";

			}
		}
		// return drmsg.toString();
	}

	public String acknowledgeREGISTEREDREPORT(String requestText) {
		JSONObject drmsg = new JSONObject();
		String srcEMA = null;

		if (version.equals("EMAP1.0b")) {

			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

				com.mir.ems.profile.emap.v2.Response response = new com.mir.ems.profile.emap.v2.Response();
				response.setDestEMA(emaProfile.getEmaID());
				response.setRequestID(emaProfile.getRequestID());
				response.setResponseCode(200);
				response.setResponseDescription("OK");
				response.setService("Response");
				response.setSrcEMA(global.SYSTEM_ID);
				response.setTime(new Date(System.currentTimeMillis()).toString());

				// String topic = "/EMAP/CEMA/1.0b/SessionSetup";
				String topic = "/EMAP/" + emaProfile.getEmaID() + "/1.0b/SessionSetup";

				String payload = response.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "NORESPONSE";

		}

		else if (version.equals("OpenADR2.0b")) {

			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("VENID")).setRequestID(jsonObj.getString("REQUESTID"));

				com.mir.ems.profile.openadr.recent.Response response = new com.mir.ems.profile.openadr.recent.Response();
				response.setDestEMA(emaProfile.getEmaID());
				response.setRequestID(emaProfile.getRequestID());
				response.setResponseCode(200);
				response.setResponseDescription("OK");
				response.setService("oadrResponse");

				// String topic = "/EMAP/CEMA/1.0b/SessionSetup";
				String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiReport";

				String payload = response.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "NORESPONSE";

		}

		else {
			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("SRCEMA");

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
						.setVersion(jsonObj.getString("VERSION"));

				drmsg.put("SrcEMA", global.SYSTEM_ID);
				drmsg.put("DestEMA", emaProfile.getEmaID());
				drmsg.put("responseCode", 200);
				drmsg.put("responseDescription", "OK");
				drmsg.put("requestID", emaProfile.getRequestID());
				drmsg.put("version", emaProfile.getVersion());
				drmsg.put("time", new Date(System.currentTimeMillis()));
				drmsg.put("service", "oadrResponse");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "Response" + "&&" + drmsg.toString() + "&&" + srcEMA;
	}

	@SuppressWarnings("deprecation")
	public String acknowledgeREQUESTEVENT(String requestText) {
		JSONObject drmsg = new JSONObject();
		String srcEMA = null;

		if (version.equals("EMAP1.0b")) {
			// Threshold 분배 알고리즘 시작, Thread 아님, 반드시 이게 끝나고 분배해줘야 하기 때문
			new Greedy().start();

			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

				Intervals interval = new Intervals();

				interval.addIntervalsParams(global.duration, "uid", 0.0);

				EventSignals es = new EventSignals();
				Event event = new Event();

				// Threshold 정보 전달

				event.addEventParams("eventID",
						new EventSignals().addEventSignalsParams(interval.getIntervalsParams(), "signalName",
								"Control Event", "signalID", 0,
								global.emaThresholdManage.get(jsonObj.getString("SRCEMA")).doubleValue(), 0, 0,
								"KW/WON").getEventSignalsParams(),
						1, "modificationReason", -1, "mirLab", new Date(System.currentTimeMillis()).toString(),
						"eventStatus", false, "SessionSetup", "properties", "components", emaProfile.getEmaID(),
						new Date(System.currentTimeMillis()).toString(), "PT1H", "tolerance", "notification", "rampUp",
						"recovery");

				// Passive 기본 가격정보 전송

				if (global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).isTableChanged()) {

					for (int i = 0; i < global.priceTable.size(); i++) {

						if (global.priceTable.get(i).getType() != null) {
							if (global.priceTable.get(i).getType().equals("Industrial1")) {

								for (int j = 0; j < 3; j++) {

									String vtnComment = "";
									double price = 0;

									if (j == 0) {
										vtnComment = "Summer";
										price = global.priceTable.get(i).getSummer();
									} else if (j == 1) {
										vtnComment = "Spring/Fall";
										price = global.priceTable.get(i).getSpringFall();
									} else if (j == 2) {
										vtnComment = "Winter";
										price = global.priceTable.get(i).getWinter();
									}

									event.addEventParams("eventID",
											new EventSignals()
													.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
															"Price Event", "signalID", 0, 0, 0, price, "KW/WON")
													.getEventSignalsParams(),
											1, "modificationReason", -1,
											"http://cyber.kepco.co.kr/ckepco/front/jsp/CY/E/E/CYEEHP00103.jsp",
											global.createTableDate, "eventStatus", true, vtnComment, "properties",
											"components", emaProfile.getEmaID(), "dtStart", "duration", "tolerance",
											"notification", "rampUp", "recovery");

								}
							}

							else if (global.priceTable.get(i).getType().equals("Industrial2")) {
								// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
								// ElectricityPrice.txt 정보 참조
							} else if (global.priceTable.get(i).getType().equals("Industrial3")) {
								// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
								// ElectricityPrice.txt 정보 참조
							}
						}
					}

					global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).setTableChanged(false);
				}

				// Active Mode 일때 실시간 가격정보 전달
				if (MainFrame.rdbtnmntmNewRadioItem_1.isSelected()) {

					if (global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).isRealTimetableChanged()) {

						Calendar now = Calendar.getInstance();
						int cHour = now.get(Calendar.HOUR_OF_DAY);
						int year = now.get(Calendar.YEAR);
						int month = now.get(Calendar.MONTH) + 1;
						int date = now.get(Calendar.DATE);

						for (int i = 0; i < global.realTimePriceTable.size(); i++) {

							int time = Integer.parseInt(global.realTimePriceTable.get(i).getStrTime().split(":")[0]);

							if (time >= cHour) {

								double price = global.realTimePriceTable.get(i).getPrice();

								event.addEventParams("eventID",
										new EventSignals()
												.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
														"Price Event", "signalID", 0, 0, 0, price, "KW/WON")
												.getEventSignalsParams(),
										1, "modificationReason", -1,
										"https://hourlypricing.comed.com/live-prices/?date=20180826",
										new Date(System.currentTimeMillis()).toString(), "eventStatus", true,
										"RealTimePricing", "properties", "components", emaProfile.getEmaID(),
										new Date(year, month, date, time, 0).toString(), "PT1H", "tolerance",
										"notification", "rampUp", "recovery");
							}
						}

						global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).setRealTimetableChanged(false);

					}
				}

				/*-----------------여기까지가 Active Mode --------------*/

				EventResponse er = new EventResponse();
				er.setRequestID(emaProfile.getRequestID());
				er.setResponseCode(200);
				er.setResponseDescription("OK");

				com.mir.ems.profile.emap.v2.DistributeEvent drE = new com.mir.ems.profile.emap.v2.DistributeEvent();

				drE.setDestEMA(emaProfile.getEmaID());
				drE.setEvent(event.getEventParams());
				drE.setRequestID(emaProfile.getRequestID());
				drE.setResponse(er.toString());
				drE.setService("DistributeEvent");
				drE.setSrcEMA(global.SYSTEM_ID);
				drE.setTime(new Date(System.currentTimeMillis()).toString());
				drE.setResponseRequired("Always");

				String topic = "/EMAP/" + emaProfile.getEmaID() + "/1.0b/SessionSetup";

				// String topic = "/EMAP/CEMA/1.0b/SessionSetup";
				String payload = drE.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "NORESPONSE";

		}

		else if (version.equals("OpenADR2.0b")) {

			// Threshold 분배 알고리즘 시작, Thread 아님, 반드시 이게 끝나고 분배해줘야 하기 때문
			new Greedy().start();

			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("VENID")).setRequestID(jsonObj.getString("REQUESTID"));

				Intervals interval = new Intervals();
				interval.addIntervalsParams(global.duration, "uid", 1234.0);
				
				com.mir.ems.profile.openadr.recent.Event event = new com.mir.ems.profile.openadr.recent.Event();
				// Threshold 정보 전달

				event.addEventParams("eventID",
						new com.mir.ems.profile.openadr.recent.EventSignals().addEventSignalsParams(interval.getIntervalsParams(), "signalName",
								"Control Event", "signalID", 0).getEventSignalsParams(),
						1, "modificationReason", -1, "mirLab", new Date(System.currentTimeMillis()).toString(),
						"eventStatus", false, "SessionSetup", "properties", "components", emaProfile.getEmaID(),
						new Date(System.currentTimeMillis()).toString(), "PT1H", "tolerance", "notification", "rampUp",
						"recovery");

				com.mir.ems.profile.openadr.recent.EventResponse er = new com.mir.ems.profile.openadr.recent.EventResponse();
				er.setRequestID(emaProfile.getRequestID());
				er.setResponseCode(200);
				er.setResponseDescription("OK");

				com.mir.ems.profile.openadr.recent.DistributeEvent drE = new com.mir.ems.profile.openadr.recent.DistributeEvent();

				drE.setSrcEMA(global.SYSTEM_ID);
				drE.setEvent(event.getEventParams());
				drE.setRequestID(emaProfile.getRequestID());
				drE.setResponse(er.toString());
				drE.setService("oadrDistributeEvent");
				drE.setResponseRequired("Always");

				String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiEvent";

				// String topic = "/EMAP/CEMA/1.0b/SessionSetup";
				String payload = drE.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "NORESPONSE";

		} else {

			try {
				jsonObj = new JSONObject(requestText.toUpperCase());

				srcEMA = jsonObj.getString("SRCEMA");

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

				drmsg.put("SrcEMA", global.SYSTEM_ID);
				drmsg.put("DestEMA", emaProfile.getEmaID());
				drmsg.put("requestID", emaProfile.getRequestID());
				drmsg.put("responseCode", 200);
				drmsg.put("responseDescription", "OK");
				drmsg.put("time", new Date(System.currentTimeMillis()));
				drmsg.put("type", "Registration");

				Calendar calendar = Calendar.getInstance();
				String startYMD = "" + calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1) + ""
						+ calendar.get(Calendar.DATE);
				String startTime = "" + calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) + ""
						+ calendar.get(Calendar.SECOND);

				JSONArray eventInfo = new JSONArray();
				JSONObject childFormat = new JSONObject();
				childFormat.put("testEvent", false);
				childFormat.put("marketContext", 1);
				childFormat.put("eventStatus", "Registration");
				childFormat.put("priority", 0);
				childFormat.put("eventID", 0);
				childFormat.put("startYMD", Integer.parseInt(startYMD));
				childFormat.put("startTime", Integer.parseInt(startTime));
				childFormat.put("endYMD", Integer.parseInt(startYMD));
				childFormat.put("endTime", Integer.parseInt(startTime));
				childFormat.put("profileName", global.profileName);
				childFormat.put("duration", 1000);
				childFormat.put("modificationNumber", 0);
				childFormat.put("uid", 0);
				double currentValue = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getPower();
				double threshold = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getMargin();
				childFormat.put("currentValue", currentValue);
				childFormat.put("signalName", "Initial");
				childFormat.put("signalType", "DistributeEvent");
				childFormat.put("signalID", 1);
				childFormat.put("threshold", threshold);
				childFormat.put("capacity", threshold - currentValue);
				eventInfo.put(childFormat);
				drmsg.put("EMADREventInformation", eventInfo);

				eventInfo = new JSONArray();
				childFormat = new JSONObject();
				childFormat.put("price", 1000);
				childFormat.put("unit", "KW");
				eventInfo.put(childFormat);
				drmsg.put("EMADRPriceInformation", eventInfo);

				drmsg.put("service", "DistributeEvent");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "DistributeEvent" + "&&" + drmsg.toString() + "&&" + srcEMA;
	}

	public String acknowledgeCANCELPARTYREGISTRATION(String requestText) {

		JSONObject drmsg = new JSONObject();

		try {
			emaProfile = new Emap_Cema_Profile();
			jsonObj = new JSONObject(requestText.toUpperCase());

			drmsg.put("SrcEMA", global.SYSTEM_ID);
			drmsg.put("DestEMA", jsonObj.getString("SRCEMA"));
			drmsg.put("requestID", jsonObj.getString("REQUESTID"));
			drmsg.put("registrationID", jsonObj.getString("REGISTRATIONID"));
			drmsg.put("service", "CanceledPartyRegistration");

			emaProfile = new Emap_Cema_Profile(jsonObj.getString("SRCEMA"), null, null, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, new JSONObject(), new JSONObject(), "DISCONNECT");

			global.putEmaProtocolCoAP(jsonObj.getString("SRCEMA"), emaProfile);

			global.emaProtocolCoAP_EventFlag.put(jsonObj.getString("SRCEMA"), new EMAP_CoAP_EMA_DR());

			// global.emaProtocolCoAP.remove(jsonObj.get("SRCEMA"));

		} catch (JSONException e) {

		}
		return "a";
	}

}
