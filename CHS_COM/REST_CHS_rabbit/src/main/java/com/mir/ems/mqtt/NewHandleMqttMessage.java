package com.mir.ems.mqtt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.ServerEmaProfile;
import com.mir.ems.deviceProfile.EMA_tab_temp;
import com.mir.ems.globalVar.global;

public class NewHandleMqttMessage extends Thread {
	String sortTopic, processTopic;
	JSONObject payload;
	JSONObject drmsg = new JSONObject();

	final String oadrRegistration = "oadrRegistration";
	final String oadrPeriodical = "oadrPeriodical";
	final String oadrEventBased = "oadrEventBased";
	final String oadrReport = "oadrReport";

	String p_topic, m_msg;

	String venID, optType, type, qos, state, tempTime, tempMaxTime, tempMinTime;
	int emaCnt, dimming, priority;
	double threshold, exceedPower, capacity, power, maxValue, minValue, avgValue;
	Date timeStamp, maxTime, minTime;

	SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ServerEmaProfile serverEmaProfile;
	MqttClient client;

	public static HashMap<String, Boolean> eventFlag = new HashMap<String, Boolean>();

	public NewHandleMqttMessage(MqttClient client, String sortTopic, String processTopic, JSONObject payload) {
		this.client = client;
		this.sortTopic = sortTopic;
		this.processTopic = processTopic;
		this.payload = payload;
	}

	@Override
	public void run() {
		
//		HandleMqttMessage.processStatus.setProcess_value(processTopic);

		if (sortTopic.equals(oadrRegistration)) {
//			System.out.println("aaa");
			if (processTopic.equals("ConnectRegistration")) {
				
				try {
					
					venID = (String) payload.get("venID");
					type = (String) payload.get("type");
					priority = (Integer) payload.get("priority");
					qos = (String) payload.get("QoS");
					tempTime = (String) payload.get("time");
					emaCnt = (Integer) payload.get("emaCNT");

					int requestID = (Integer) payload.get("requestID");

					timeStamp = transFormat.parse(tempTime);
					System.out.println("@@");
					System.out.println("venid"+venID);
					System.out.println("@@");
					priority = (global.EMASEQ += 1);
					serverEmaProfile = new ServerEmaProfile("DEVICE"+venID.split("R")[1], "MQTT", "null", type, qos, emaCnt, "ON", 0,
							priority, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, timeStamp, maxTime, minTime);

					global.openADRHashMap.put("DEVICE"+venID.split("R")[1], serverEmaProfile);

					// EMA_tab_temp.modify_EMA_table();

					eventFlag.put(venID, false);

					/*
					 * ????????? ???????????????@@@
					 */
					// venID = venID + (int) (Math.random() * (100 - 1)) + 1;
					// venID = DigestUtils.md2Hex(venID);

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("transportName", "MQTT");
					drmsg.put("requestID", requestID);
					drmsg.put("registrationID", DigestUtils.md2Hex(String.valueOf(requestID)));
					drmsg.put("duration", 60);
					drmsg.put("responseCode", 200);
					drmsg.put("version", "version_1.1");
					drmsg.put("profileName", "MIR_EnergyManagement_System");
					drmsg.put("responseDescription", "OK");
					drmsg.put("priority", priority);
					drmsg.put("type", type);
					drmsg.put("QoS", qos);
					drmsg.put("time", timeStamp);
					drmsg.put("emaCNT", emaCnt);

					p_topic = "CEMA/" + requestID + "/oadrRegistration/ConnectedPartyRegistration";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (processTopic.equals("CreatePartyRegistration")) {

				try {
					venID = (String) payload.get("venID");
					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);

					int requestID = (Integer) payload.get("requestID");

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("transportName", "MQTT");
					drmsg.put("responseDescription", "OK");
					drmsg.put("version", "version_1.1");
					drmsg.put("responseCode", 200);
					drmsg.put("requestID", requestID);
					drmsg.put("duration", 60);
					drmsg.put("profileName", "MIR_EnergyManagement_System");
					drmsg.put("registrationID", DigestUtils.md2Hex(String.valueOf(requestID)));
					drmsg.put("time", timeStamp);

					p_topic = "CEMA/" + requestID + "/oadrRegistration/CreatedPartyRegistration";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (processTopic.equals("RegisterReport")) {
				try {

					venID = (String) payload.get("venID");
					state = (String) payload.get("state");
					dimming = (Integer) payload.get("dimming");
					power = (double) payload.get("power");
					maxValue = (double) payload.get("maxValue");
					minValue = (double) payload.get("minValue");
					avgValue = (double) payload.get("avgValue");

					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);

					tempMaxTime = (String) payload.get("maxTime");
					maxTime = transFormat.parse(tempMaxTime);

					tempMinTime = (String) payload.get("minTime");
					minTime = transFormat.parse(tempMinTime);

					String originVal = global.openADRHashMap.get("DEVICE"+venID.split("R")[1]).toString();
					String[] parseOriginVal = originVal.split("/");
					/*
					 * ???????????? ?????????~
					 * 
					 */
					double provideThreshold = (global.AVAILABLE_THRESHOLD / 20) * 1000;
					serverEmaProfile = new ServerEmaProfile("DEVICE"+venID.split("R")[1], parseOriginVal[1], "null", parseOriginVal[3],
							parseOriginVal[4], Integer.parseInt(parseOriginVal[5]), "ON", dimming,
							Integer.parseInt(parseOriginVal[8]), provideThreshold, 0.0, provideThreshold, power,
							maxValue, minValue, avgValue, timeStamp, maxTime, minTime);
					global.openADRHashMap.replace("DEVICE"+venID.split("R")[1], serverEmaProfile);
					// EMA_tab_temp.modify_EMA_table();

					int requestID = (Integer) payload.get("requestID");

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("transportName", "MQTT");
					drmsg.put("requestID", requestID);
					drmsg.put("responseCode", 200);
					drmsg.put("version", "version_1.1");
					drmsg.put("responseDescription", "OK");
					drmsg.put("threshold", priority);
					drmsg.put("time", timeStamp);

					p_topic = "CEMA/" + requestID + "/oadrRegistration/RegisteredReport";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (processTopic.equals("Poll")) {
				try {

					venID = (String) payload.get("venID");
					tempTime = (String) payload.get("time");
					int requestID = (Integer) payload.get("requestID");
					timeStamp = transFormat.parse(tempTime);

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("transportName", "MQTT");
					drmsg.put("requestID", requestID);
					drmsg.put("responseCode", 200);
					drmsg.put("version", "version_1.1");
					drmsg.put("responseDescription", "OK");
					drmsg.put("time", timeStamp);
					drmsg.put("duration", 60);

					p_topic = "CEMA/" + requestID + "/oadrRegistration/RegisterReport";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (processTopic.equals("RegisteredReport")) {
				try {

					venID = (String) payload.get("venID");
					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);
					int requestID = (Integer) payload.get("requestID");

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("responseCode", 200);
					drmsg.put("version", "version_1.1");
					drmsg.put("responseDescription", "OK");
					drmsg.put("time", timeStamp);
					drmsg.put("requestID", requestID);

					p_topic = "CEMA/" + requestID + "/oadrRegistration/Response";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (processTopic.equals("RequestEvent")) {
				try {

					venID = (String) payload.get("venID");
					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);
					int requestID = (Integer) payload.get("requestID");

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("responseCode", 200);
					drmsg.put("version", "version_1.1");
					drmsg.put("responseDescription", "OK");
					drmsg.put("time", timeStamp);
					drmsg.put("requestID", requestID);

					p_topic = "CEMA/" + requestID + "/oadrRegistration/DistributeEvent";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (processTopic.equals("CancelPartyRegistration")) {
				try {

//					venID = (String) payload.get("venID");
//					tempTime = (String) payload.get("time");
//					timeStamp = transFormat.parse(tempTime);
					int requestID = (Integer) payload.get("requestID");
					venID = "VEN_MIR" + requestID;	
					
					String originVal = global.openADRHashMap.get("DEVICE"+venID.split("R")[1]).toString();
					String[] parseOriginVal = originVal.split("/");

					serverEmaProfile = new ServerEmaProfile("DEVICE"+venID.split("R")[1], parseOriginVal[1], "null", parseOriginVal[3],
							parseOriginVal[4], Integer.parseInt(parseOriginVal[5]), "OFF", dimming,
							Integer.parseInt(parseOriginVal[8]), 0.0, 0.0, 0.0, power,
							maxValue, minValue, avgValue, timeStamp, maxTime, minTime);
					global.openADRHashMap.replace("DEVICE"+venID.split("R")[1], serverEmaProfile);
					
//					global.openADRHashMap.remove(venID);
					drmsg.put("venID", venID);
					drmsg.put("requestID", requestID);
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("time", timeStamp);

					p_topic = "CEMA/" + requestID + "/oadrRegistration/RegisteredReport";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
//				catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}

		} else if (sortTopic.equals(oadrPeriodical)) {
			// oadrPeriodicalProcess(processTopic, payload);
			if (processTopic.equals("Poll")) {

				try {
					venID = (String) payload.get("venID");
					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);
					int requestID = (Integer) payload.get("requestID");

					/*
					 * sin ???????????? ?????? ???????????? ???????????? ?????? ????????? ??????????????? ????????? ????????????. venID??? ??????
					 * ???????????? ????????????????????? ??????? ????????? ?????? updateReport??? ?????? ???????????? ?????? ?????? ??????
					 * ???????????? threshold?????? ??????????????????. ?????? ???????????? threshold value??? ema???
					 * ???????????? ???????????? ???????????? ????????? ????????????. if?????? ?????? ?????????????????????.
					 */
					double provideThreshold = (global.AVAILABLE_THRESHOLD / 20) * 1000;

					if (!eventFlag.get(venID)) {

						drmsg.put("venID", venID);
						drmsg.put("vtnID", global.SYSTEM_ID);
						drmsg.put("responseDescription", "OK");
						drmsg.put("responseCode", 200);
						drmsg.put("time", timeStamp);
						drmsg.put("threshold", provideThreshold);

						p_topic = "CEMA/" + requestID + "/oadrPeriodical/Response";

					} else if (eventFlag.get(venID)) {

						SimpleDateFormat eventYMD = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat eventTime = new SimpleDateFormat("hh:mm:ss");
						long currentTime = System.currentTimeMillis();

						drmsg.put("venID", venID);
						drmsg.put("vtnID", global.SYSTEM_ID);
						drmsg.put("requestID", requestID);
						drmsg.put("responseCode", 200);
						drmsg.put("profileName", "MIR_EnergyManagement_System");
						drmsg.put("responseDescription", "OK");
						drmsg.put("eventID", 1129);
						drmsg.put("modificationNumber", 0);
						drmsg.put("testEvent", true);
						drmsg.put("marketContext", "Provider_MIR");
						drmsg.put("eventStatus", "null");
						drmsg.put("signalName", "oadrEventBased");
						drmsg.put("signalType", "DistributeEvent");
						drmsg.put("signalID", 0);
						drmsg.put("currentValue", 0);
						drmsg.put("duration", (currentTime + 2000) - currentTime);
						drmsg.put("uid", 0);
						drmsg.put("startYMD", eventYMD.format(currentTime));
						drmsg.put("startTime", eventTime.format(currentTime));
						drmsg.put("threshold", provideThreshold);
						drmsg.put("capacity", provideThreshold - power);
						drmsg.put("priority", 0);
						drmsg.put("time", timeStamp);

						p_topic = "CEMA/" + requestID + "/oadrPeriodical/DistributeEvent";
						eventFlag.replace(venID, false);

					}

					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (processTopic.equals("createdEvent")) {

				try {
					venID = (String) payload.get("venID");
					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);
					optType = (String) payload.get("optType");
					int requestID = (Integer) payload.get("requestID");

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("responseDescription", "OK");
					drmsg.put("responseCode", 200);
					drmsg.put("requestID", requestID);
					drmsg.put("time", timeStamp);

					p_topic = "CEMA/" + requestID + "/oadrPeriodical/Response";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (sortTopic.equals(oadrEventBased)) {
			// oadrEventBasedProcess(processTopic, payload);
			if (processTopic.equals("RequestEvent")) {
				try {
					venID = (String) payload.get("venID");
					threshold = (double) payload.get("threshold");
					power = (double) payload.get("power");
					exceedPower = (double) payload.get("exceedPower");
					type = (String) payload.get("type");

					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);

					int requestID = (Integer) payload.get("requestID");

					double provideThreshold = (global.AVAILABLE_THRESHOLD / 20) * 1000;

					SimpleDateFormat eventYMD = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat eventTime = new SimpleDateFormat("hh:mm:ss");
					long currentTime = System.currentTimeMillis();

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("requestID", requestID);
					drmsg.put("responseCode", 200);
					drmsg.put("profileName", "MIR_EnergyManagement_System");
					drmsg.put("responseDescription", "OK");
					drmsg.put("eventID", 1129);
					drmsg.put("modificationNumber", 0);
					drmsg.put("testEvent", true);
					drmsg.put("marketContext", "Provider_MIR");
					drmsg.put("eventStatus", "null");
					drmsg.put("signalName", "oadrEventBased");
					drmsg.put("signalType", "DistributeEvent");
					drmsg.put("signalID", 0);
					drmsg.put("currentValue", 0);
					drmsg.put("duration", (currentTime + 2000) - currentTime);
					drmsg.put("uid", 0);
					drmsg.put("startYMD", eventYMD.format(currentTime));
					drmsg.put("startTime", eventTime.format(currentTime));
					drmsg.put("threshold", provideThreshold);
					drmsg.put("capacity", provideThreshold - power);
					drmsg.put("priority", 0);
					drmsg.put("time", timeStamp);

					p_topic = "CEMA/" + requestID + "/oadrEventBased/DistributeEvent";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (sortTopic.equals(oadrReport)) {
			// oadrReportProcess(processTopic, payload);
			if (processTopic.equals("UpdateReport")) {
				try {
					venID = (String) payload.get("venID");
					emaCnt = (Integer) payload.getInt("emaCNT");
					type = (String) payload.get("type");
					dimming = (Integer) payload.get("dimming");
					state = (String) payload.get("state");
					power = (double) payload.get("power");
					maxValue = (double) payload.get("maxValue");
					minValue = (double) payload.get("minValue");
					avgValue = (double) payload.get("avgValue");
					threshold = (double) payload.get("threshold");

					tempTime = (String) payload.get("time");
					timeStamp = transFormat.parse(tempTime);

					tempMaxTime = (String) payload.get("maxTime");
					maxTime = transFormat.parse(tempMaxTime);

					tempMinTime = (String) payload.get("minTime");
					minTime = transFormat.parse(tempMinTime);

					int requestID = (Integer) payload.get("requestID");

					/*
					 * ???????????? ?????????, ???????????? ??????????????? ? ????????? ?????? ?????????????????? 20?????? ????????? ???????????? ????????????
					 * ???????????? ??????
					 * 
					 */

					String originVal = global.openADRHashMap.get("DEVICE"+venID.split("R")[1]).toString();
					String[] parseOriginVal = originVal.split("/");
					serverEmaProfile = new ServerEmaProfile("DEVICE"+venID.split("R")[1], parseOriginVal[1], "null", parseOriginVal[3],
							parseOriginVal[4], Integer.parseInt(parseOriginVal[5]), "ON", dimming,
							Integer.parseInt(parseOriginVal[8]), threshold, 0.0, threshold - power, power, maxValue,
							minValue, avgValue, timeStamp, maxTime, minTime);
					global.openADRHashMap.replace("DEVICE"+venID.split("R")[1], serverEmaProfile);
					// EMA_tab_temp.update_EMA_table();

					if (power >= (threshold * 0.9)) {

						// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

						eventFlag.replace(venID, true);
					}

					drmsg.put("venID", venID);
					drmsg.put("vtnID", global.SYSTEM_ID);
					drmsg.put("responseDescription", "OK");
					drmsg.put("responseCode", 200);
					drmsg.put("requestID", requestID);
					drmsg.put("threshold", threshold);
					drmsg.put("currentValue", power);
					drmsg.put("capacity", threshold - power);
					drmsg.put("priority", Integer.parseInt(parseOriginVal[8]));
					drmsg.put("time", timeStamp);

					p_topic = "CEMA/" + requestID + "/oadrReport/UpdatedReport";
					m_msg = drmsg.toString();
					sendMessage(p_topic, m_msg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void oadrRegistrationProcess(String processTopic, JSONObject payload) {

	}

	public void oadrPeriodicalProcess(String processTopic, JSONObject payload) {
	}

	public void oadrEventBasedProcess(String processTopic, JSONObject payload) {

	}

	public void oadrReportProcess(String topic, JSONObject payload) {

	}

	public void sendMessage(String topic, String payload) {

		new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

		
	}
}
