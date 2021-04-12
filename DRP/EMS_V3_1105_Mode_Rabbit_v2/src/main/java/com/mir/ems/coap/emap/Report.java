package com.mir.ems.coap.emap;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.coap.emap.SessionSetup.SessionType;
import com.mir.ems.coap.emap.SessionSetup.Type;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.ems.hashMap.ESS_values;
import com.mir.ems.hashMap.PV_values;
import com.mir.ems.mqtt.Publishing;
import com.mir.update.database.cema_database;
import com.mir.update.database.device_total_database;

public class Report extends CoapResource {

	enum Type {
		REGISTERREPORT, UPDATEREPORT
	}

	public Report(String name) {
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

		// System.out.println(exchange.getRequestText().toString());
		// System.out.println(getPath());

		if (getPath().contains(global.version)) {
			try {
				String version = "EMAP" + getPath().split("/")[3];

				JSONObject json = new JSONObject(exchange.getRequestText().toString());
				String service = json.getString("service");

				new ReportType(getName(), service, exchange, version).start();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (getPath().contains(global.openADRVersion)) {

			try {

				String version = "OpenADR" + getPath().split("/")[3];

				JSONObject json = new JSONObject(exchange.getRequestText().toString());
				String service = json.getString("service");
				service = service.replaceAll("oadr", "");

				new ReportType(getName(), service, exchange, version).start();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {
			new ReportType(getName(), exchange).start();

		}

	}

	class ReportType extends Thread {

		CoapExchange exchange;
		String incomingType, requestText, setPayload;

		Emap_Cema_Profile emaProfile;
		Emap_Device_Profile deviceProfile;
		private JSONObject jsonObj;
		private JSONObject sub1JsonObj;
		private JSONObject sub2JsonObj;

		private String version, service;

		ReportType(String incomingType, String service, CoapExchange exchange, String version) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
			this.version = version;
			this.service = service;
		}

		ReportType(String incomingType, CoapExchange exchange) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
		}

		@Override
		public void run() {
			// try {
			// sleep(10);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// Type type = Type.valueOf(incomingType.toUpperCase());

			Type type;

			if (version.equals("EMAP1.0b")) {
				type = Type.valueOf(service.toUpperCase());

			}

			else if (version.equals("OpenADR2.0b")) {
				type = Type.valueOf(service.toUpperCase());

			}

			else {
				type = Type.valueOf(incomingType.toUpperCase());

			}

			switch (type) {

			case UPDATEREPORT:
				this.setPayload = acknowledgeUPDATEREPORT(requestText);
				break;
			default:
				this.setPayload = "TYPE WRONG";
				break;
			}

			if (this.setPayload.equals("TYPE WRONG"))
				this.exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
			else if (this.setPayload.toUpperCase().equals("NORESPONSE")) {

			} else
				this.exchange.respond(ResponseCode.CONTENT, this.setPayload, MediaTypeRegistry.APPLICATION_JSON);

		}

		public String acknowledgeUPDATEREPORT(String requestText) {
			JSONObject drmsg = new JSONObject();

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

							JSONObject decr = new JSONObject(decrArr.get(0).toString());

							String qos, registrationID, state, minTime, maxTime;
							double margin, minValue, maxValue, avgValue, power, generate, storage;
							int priority, dimming;
							boolean pullModel;

							qos = decr.getString("qos");
							registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
							// margin = decr.getDouble("margin");
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

							// Threshold 기준 80%이상을 사용할 경우 이벤트 시그널

							margin = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getMargin();

							if (power > 10 && power >= margin * global.EXPERIMENTPERCENT) {
								setEvent(jsonObj.getString("SrcEMA"), margin, power);
							}

							Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP", jsonObj.getString("SrcEMA"),
									registrationID, qos, state, power, dimming, margin, generate, storage, maxValue,
									minValue, avgValue, maxTime, minTime, priority, pullModel, timeTable, realTimetable,
									"CONNECT");

							global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);

							cema_database cd = new cema_database();
							cd.buildup(jsonObj.getString("SrcEMA"), qos, state, power, dimming, margin, generate,
									storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

						}

						else if (jsonObj.getString("type").equals("Explicit") || 1 < decrArr.length()) {

							for (int j = 0; j < decrArr.length(); j++) {

								JSONObject decr = new JSONObject(decrArr.get(j).toString());

								String qos, registrationID, state, minTime, maxTime, rID;
								double margin, minValue, maxValue, avgValue, power, generate, storage;
								int priority, dimming;
								boolean pullModel;

								if (decr.getString("deviceType").contains("EMA")) {
									qos = decr.getString("qos");
									registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
											.getRegistrationID();
									// margin = decr.getDouble("margin");
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
									boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
											.isTableChanged();

									// Threshold 기준 80%이상을 사용할 경우 이벤트 시그널

									margin = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getMargin();

									if (power >= margin * global.EXPERIMENTPERCENT) {

										setEvent(jsonObj.getString("SrcEMA"), margin, power);

									}

									Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP",
											jsonObj.getString("SrcEMA"), registrationID, qos, state, power, dimming,
											margin, generate, storage, maxValue, minValue, avgValue, maxTime, minTime,
											priority, pullModel, timeTable, realTimetable, "CONNECT");

									global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);

									cema_database cd = new cema_database();
									cd.buildup(jsonObj.getString("SrcEMA"), qos, state, power, dimming, margin,
											generate, storage, maxValue, minValue, avgValue, maxTime, minTime,
											priority);

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

									deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"), rID,
											decr.getString("deviceType"), qos, state, power, dimming, margin, generate,
											storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

									if (!global.emaProtocolCoAP_Device.containsKey(rID)) {
										global.emaProtocolCoAP_Device.put(rID, deviceProfile);
									} else {
										global.emaProtocolCoAP_Device.replace(rID, deviceProfile);
									}

									device_total_database dtd = new device_total_database();
									dtd.buildUp(jsonObj.getString("SrcEMA"), rID, decr.getString("deviceType"), state,
											dimming, priority, power, new Date(System.currentTimeMillis()).toString());

								}

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
								}

							}

						}

					}

					com.mir.ems.profile.emap.v2.UpdatedReport udt = new com.mir.ems.profile.emap.v2.UpdatedReport();
					udt.setDestEMA(emaProfile.getEmaID());
					udt.setRequestID(emaProfile.getRequestID());
					udt.setResponseCode(200);
					udt.setResponseDescription("OK");
					udt.setService("UpdatedReport");
					udt.setSrcEMA(global.SYSTEM_ID);
					udt.setTime(new Date(System.currentTimeMillis()).toString());
					udt.setType(global.reportType);

					String payload = udt.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText);

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("venID")).setRequestID(jsonObj.getString("requestID"));

					JSONArray reportArr = new JSONArray(jsonObj.getString("oadrReport"));

					for (int i = 0; i < reportArr.length(); i++) {

						JSONArray decrArr = new JSONArray(
								new JSONObject(reportArr.get(i).toString()).getString("oadrReportDescription"));
						//
						// if (jsonObj.getString("type").equals("Implicit") || 1
						// ==
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
						// qos, state, power, dimming, margin, generate,
						// storage,
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
						// else if (jsonObj.getString("type").equals("Explicit")
						// ||
						// 1 < decrArr.length()) {
						//
						// System.err.println("Explicit");
						//
						// for (int j = 0; j < decrArr.length(); j++) {
						//
						// JSONObject decr = new
						// JSONObject(decrArr.get(j).toString());
						//
						// String qos, registrationID, state, minTime, maxTime,
						// rID;
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
						// decr.getString("deviceType"), qos, state, power,
						// dimming,
						// margin, generate,
						// storage, maxValue, minValue, avgValue, maxTime,
						// minTime,
						// priority);
						//
						// global.emaProtocolCoAP_Device.put(rID,
						// deviceProfile);
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

					com.mir.ems.profile.openadr.recent.UpdatedReport rdt = new com.mir.ems.profile.openadr.recent.UpdatedReport();
					rdt.setDestEMA(emaProfile.getEmaID());
					rdt.setRequestID(emaProfile.getRequestID());
					rdt.setResponseCode(200);
					rdt.setResponseDescription("OK");
					rdt.setService("oadrUpdatedReport");

					String payload = rdt.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else {
				try {
					emaProfile = new Emap_Cema_Profile();
					jsonObj = new JSONObject(requestText);
					sub2JsonObj = new JSONObject(jsonObj.getString("EMAupdatedMgnInfo"));

					emaProfile.setEmaID(jsonObj.getString("SrcEMA")).setRequestID(jsonObj.getString("requestID"));

					Emap_Cema_Profile originVal = global.emaProtocolCoAP.get(jsonObj.getString("SrcEMA"));
					JSONObject topologyMgn = new JSONObject();

					// Original
					// emaProfile = new
					// EMAP_CoAP_EMA(jsonObj.getString("SrcEMA"),
					// originVal.getqOs(),
					// originVal.getRegistrationID(),
					// sub2JsonObj.getInt("emaCNT"),
					// originVal.getCustomerPriority(),
					// sub2JsonObj.getDouble("margin"),
					// sub2JsonObj.getDouble("minValue"),
					// sub2JsonObj.getDouble("maxValue"),
					// sub2JsonObj.getDouble("avgValue"),
					// sub2JsonObj.getDouble("power"),
					// sub2JsonObj.getDouble("generate"),
					// sub2JsonObj.getDouble("storage"),
					// jsonObj.getJSONObject("EMAupdatedDRInfo"),
					// jsonObj.getJSONObject("EMAupdatedMgnInfo"));

					double threshold = ((global.AVAILABLE_THRESHOLD / 20) * 1000) - (int) (Math.random() * (500 - 250))
							+ 250;

					emaProfile = new Emap_Cema_Profile(jsonObj.getString("SrcEMA"), originVal.getqOs(),
							originVal.getRegistrationID(), sub2JsonObj.getInt("emaCNT"),
							originVal.getCustomerPriority(), sub2JsonObj.getDouble("margin"),
							sub2JsonObj.getDouble("minValue"), sub2JsonObj.getDouble("maxValue"),
							sub2JsonObj.getDouble("avgValue"), threshold, sub2JsonObj.getDouble("generate"),
							sub2JsonObj.getDouble("storage"), jsonObj.getJSONObject("EMAupdatedDRInfo"),
							jsonObj.getJSONObject("EMAupdatedMgnInfo"), "CONNECT");

					if (sub2JsonObj.getInt("emaCNT") > 0) {
						// System.out.println("개수는
						// "+sub2JsonObj.getInt("emaCNT"));
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
							global.emaProtocolCoAP_Device.replace(sub1JsonObj.getString("deviceEMAID"), deviceProfile);
						}

						topologyMgn.put("emaCNT", sub2JsonObj.getInt("emaCNT"));
						topologyMgn.put("topology", topologyInfo);
					} else if (sub2JsonObj.getInt("EMACNT") <= 0) {
						topologyMgn.put("emaCNT", sub2JsonObj.getInt("emaCNT"));

					}

					global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), emaProfile);

					/*
					 * 수정해야되요!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@
					 */
					JSONObject energyInfo = new JSONObject();

					energyInfo.put("threshold", 100.1);
					energyInfo.put("currentValue", 100.1);
					energyInfo.put("capacity", 100.1);
					energyInfo.put("priority", 1);

					// JSONObject topologyInfo = new JSONObject();
					//
					// topologyInfo.put("emaCNT", sub2JsonObj.getInt("EMACNT"));

					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("EMAEnergyinfo", energyInfo);
					drmsg.put("EMATopologyinfo", topologyMgn);
					drmsg.put("service", "UpdatedReport");
					drmsg.put("time", new Date(System.currentTimeMillis()));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

	}

	public void setEvent(String srcEMA, double margin, double power) {
		Calendar now = Calendar.getInstance();

		int sYear = now.get(Calendar.YEAR);
		int sMonth = now.get(Calendar.MONTH) + 1;
		int sDate = now.get(Calendar.DATE);
		String strYMD = sYear + "" + sMonth + "" + sDate;
		int sHour = now.get(Calendar.HOUR_OF_DAY);
		int sMin = now.get(Calendar.MINUTE);

		String sTime = sHour + "" + sMin + "" + "11";
		String eTime = (sHour + 1) + "" + sMin + "" + "11";

		double threshold = margin * (global.EXPERIMENTPERCENT);

		// PULL MODEL
		if (global.emaProtocolCoAP.get(srcEMA).isPullModel()) {
			global.emaProtocolCoAP_EventFlag.get(srcEMA).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
					.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
					.setEndTime(Integer.parseInt(eTime)).setThreshold(threshold);
		}

		// PUSH MODEL
		if (!global.emaProtocolCoAP.get(srcEMA).isPullModel()) {

			global.initiater.eventOccur(srcEMA, 1, Integer.parseInt(strYMD), Integer.parseInt(sTime + "11"),
					Integer.parseInt(strYMD), Integer.parseInt(eTime + "11"), threshold);

		}

	}

}
