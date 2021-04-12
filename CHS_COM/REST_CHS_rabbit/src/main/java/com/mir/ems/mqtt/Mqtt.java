package com.mir.ems.mqtt;

import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.emap.DemandResponseEvent;
import com.mir.ems.mqtt.emap.Opt;
import com.mir.ems.mqtt.emap.Report;
import com.mir.ems.mqtt.emap.SessionSetup;
import com.mir.smartgrid.simulator.profile.emap.DistributeEvent;

public class Mqtt extends Thread implements MqttCallback {

	// Private instance variables
	public MqttClient client;
	public String brokerUrl;
	private boolean quietMode;
	public MqttConnectOptions conOpt;
	private boolean clean;
	private String password;
	private String userName;
	public int state;
	public String clientId;

	public NewHandleMqttMessage newHandleMqttMessage;

	public Mqtt(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode, String userName,
			String password) throws MqttException {
		this.brokerUrl = brokerUrl;
		this.quietMode = quietMode;
		this.clean = cleanSession;
		this.password = password;
		this.userName = userName;
		this.state = 0;
		this.clientId = clientId;

		String tmpDir = System.getProperty("java.io.tmpdir");
		MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

		try {

			conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(clean);
			if (password != null) {
				conOpt.setPassword(this.password.toCharArray());
			}
			if (userName != null) {
				conOpt.setUserName(this.userName);
			}

			client = new MqttClient(this.brokerUrl, clientId, dataStore);
			client.setCallback(this);

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: " + e.toString());
			System.exit(1);
		}
	}

	@Override
	public boolean isInterrupted() {
		// TODO Auto-generated method stub
		return super.isInterrupted();	
	}

	public void subscribe(String topicName, int qos) throws MqttException {
		client.connect(conOpt);

		client.subscribe(topicName, qos);

	}

	private void log(String message) {
		if (!quietMode) {
			System.out.println(message);
		}
	}

	public void connectionLost(Throwable cause) {

		log("Connection to " + brokerUrl + " lost! " + cause);
		System.exit(1);
	}

	public void deliveryComplete(IMqttDeliveryToken token) {

	}

	public void messageArrived(String topic, MqttMessage message)
			throws MqttException, InterruptedException, ParseException {

		// new DemandResponseEvent(client);

//		String time = new Timestamp(System.currentTimeMillis()).toString();
//		System.out.println("Time:\t" + time + " Topic:\t" + topic + "Message:\t" + new String(message.getPayload())
//				+ " QoS:\t" + message.getQos());

		String msgPayload = new String(message.getPayload());
		String venID;
		String gw;
		String dev;
		String ess = "ESS";
		String pv = "PV";
		String resource = "RESOURCE";
		String recloser = "RECLOSER";

		int gwNum;
		int devNum, essNum, resourceNum, recloserNum, dcuNum;
		int pvNum;

		String topic_process;
		String process_value;

		String filter, idFilter;

		if (msgPayload.startsWith("{") && msgPayload.endsWith("}")) {
			try {
				JSONObject msg_json = new JSONObject(msgPayload);

				String[] topicParse = topic.split("/");

				String serviceType = null;
				if (topicParse.length > 2) {
					serviceType = topicParse[2];
				}

				filter = topicParse[0];
				topic_process = topicParse[1];
				idFilter = topicParse[1];

				HandleEnergyReport handleEnergyReport;

				/*
				 * EMAP 2018 8월 수정본 : EMAP1.0b
				 */
				if (topicParse[1].equals("EMAP")) {

					String profileVersion = "EMAP1.0b";

					if (msg_json.getString("DestEMA").equals(global.getSYSTEM_ID())) {

						String service = msg_json.getString("service");
						// String pushID;

						// Session Setup
						if (topicParse[4].equals("SessionSetup")) {

							new SessionSetup(client, service, msg_json, profileVersion).start();

						}

						// Report
						else if (topicParse[4].equals("Report")) {
							new Report(client, service, msg_json, profileVersion).start();
						}
						// Event
						else if (topicParse[4].matches("Event|Poll")) {
							new DemandResponseEvent(client, service, msg_json, profileVersion).start();
						}
						// Opt
						else if (topicParse[4].matches("Opt")) {
							new Opt(client, service, msg_json, profileVersion).start();
						}

					}

				}

				/*
				 * OpenADR 2018 10월 수정본 : OpenADR2.0b
				 */
				if (topicParse[1].equals("OpenADR")) {

					String profileVersion = "OpenADR2.0b";

					String service = msg_json.getString("service");
					service = service.replaceAll("oadr", "");

					if (service.matches("Poll")) {
						String venID_val = msg_json.getString("venID");

						if (global.venRegisterFlag.get(venID_val).intValue() == 0) {
							topicParse[4] = "SessionSetup";
						} else {
							topicParse[4] = "Poll";
						}
					}

					if (service.matches(
							"QueryRegistration|CreatePartyRegistration|CancelPartyRegistration|RegisterReport|RegisteredReport|RequestEvent")) {

						topicParse[4] = "SessionSetup";

					}

					if (service.matches("UpdateReport")) {
						topicParse[4] = "Report";

					}

					if (service.matches("CreatedEvent")) {
						topicParse[4] = "Event";

					}

					if (service.matches("CreateOpt|CancelOpt")) {
						topicParse[4] = "Opt";

					}
					// ===============================================================
					// Session Setup
					if (topicParse[4].equals("SessionSetup")) {

						if (service.matches("QueryRegistration|oadrQueryRegistration"))
							service = "CONNECTREGISTRATION";

						new SessionSetup(client, service, msg_json, profileVersion).start();
					}

					// Report
					else if (topicParse[4].equals("Report")) {
						new Report(client, service, msg_json, profileVersion).start();
					}
					// Event
					else if (topicParse[4].matches("Event|Poll")) {
						new DemandResponseEvent(client, service, msg_json, profileVersion).start();
					}
					// Opt
					else if (topicParse[4].matches("Opt")) {
						new Opt(client, service, msg_json, profileVersion).start();
					}

					// }

				}

				// OLD
				// -----------------------------
				// if (filter.equals("EMS")) {
				//
				// if (!msg_json.isNull("GW")) {
				// gw = (String) msg_json.get("GW");
				//
				// String[] gwID = gw.split("/");
				// gwNum = Integer.parseInt(gwID[1]);
				//
				// if (!msg_json.isNull("devnumber")) {
				// dev = (String) msg_json.get("devnumber");
				// String[] devID = dev.split("/");
				// devNum = Integer.parseInt(devID[1]);
				//
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gw, gwNum,
				// dev, devNum);
				// /*
				// * ESS device
				// */
				// } else if (!msg_json.isNull("ESSID")) {
				//
				// essNum = Integer.parseInt(msg_json.get("ESSID").toString());
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gw, gwNum,
				// ess, essNum);
				// /*
				// * PV device
				// */
				// } else if (!msg_json.isNull("PVID")) {
				//
				// pvNum = Integer.parseInt(msg_json.get("PVID").toString());
				//
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gw, gwNum,
				// pv, pvNum);
				//
				// } else if (!msg_json.isNull("RESOURCEID")) {
				//
				// resourceNum =
				// Integer.parseInt(msg_json.get("RESOURCEID").toString());
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gw, gwNum,
				// resource, resourceNum);
				//
				// } else if (!msg_json.isNull("RECLOSERID")) {
				//
				// recloserNum =
				// Integer.parseInt(msg_json.get("RECLOSERID").toString());
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gw, gwNum,
				// recloser, recloserNum);
				//
				// } else if (!msg_json.isNull("DCU_Number")) {
				//
				// dcuNum =
				// Integer.parseInt(msg_json.get("DCU_Number").toString());
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gwNum,
				// dcuNum);
				// } else if (!msg_json.isNull("Device")) {
				// /*
				// * 2018-01-18 실험 할때 추가된 사항
				// */
				// // System.out.println("gwNuM" + gwNum);
				// devNum = Integer.parseInt(msg_json.get("Device").toString());
				//
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gw, gwNum,
				// "dev", devNum);
				//
				// } else {
				//
				// venID = (String) msg_json.get("VENID");
				// if (topic_process.equals("UpdateReport")) {
				// process_value = "UpdateReport";
				// } else {
				// process_value = topicParse[2];
				// }
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, process_value,
				// msgPayload, venID, gw, gwNum);
				// }
				//
				// } else if (topic_process.equals("PowerMargin")) {
				// /*
				// * gw 값을 그냥 topic_process에서 받는다.
				// *
				// * power값과 margin값을 처리하는 함수를 만든다.
				// *
				// *
				// */
				//
				// gwNum = Integer.parseInt(topicParse[3]);
				//
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gwNum);
				// }
				// }
				//
				//

				if (topic.contains(global.SYSTEM_ID) && topic.contains("RDRrequest")) {

					String service = topic.split("/")[1];
					String emaID = msg_json.getString("SrcEMA");
					String devID = msg_json.getString("devnumber");

					DeviceControl hm = new DeviceControl(client, service, emaID, devID, msgPayload);
					hm.start();

				}

				else if (filter.equals(global.getSYSTEM_TYPE())) {

					/*----------------------------------------------------------------------
					 * Edited : 2018.01.24
					 * newHandleMqttMessage = new NewHandleMqttMessage(topicParse[3], msg_json);
					 * topicParse[3] => sort of Info, e.g. CreatePartyRegistration,RegisterReport,Poll and etc.
					 * msg_json => set payload message as json format
					 * ---------------------------------------------------------------------
					 */

					if (idFilter.equals(global.getSYSTEM_ID())) {
						if (serviceType.equals("SessionSetup")) {
							new SessionSetup(client, topicParse[2], topicParse[3], msg_json).start();
						} else if (serviceType.equals("Opt")) {
							new Opt(client, topicParse[2], topicParse[3], msg_json).start();

						} else if (serviceType.equals("Report")) {
							new Report(client, topicParse[2], topicParse[3], msg_json).start();

						} else if (serviceType.equals("Poll")) {
							new DemandResponseEvent(client, topicParse[2], topicParse[3], msg_json).start();

						}

						if (topicParse[2].equals("Request")) {

							/*----------------------------------------------------------------------
							 * Edited : 2018.01.15
							 * handleEnergyReport = new HandleEnergyReport(topicParse[3], msg_json);
							 * topicParse[3] => sort of Info, e.g. Abstract, Explicit, Connect
							 * msg_json => set payload message as json format
							 * ---------------------------------------------------------------------
							 */
							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();

						} else if (topicParse[2].equals("Periodical")) {

							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();

						} else if (topicParse[2].equals("Response")) {

							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();

						} else if (topicParse[2].equals("emergency")) {
							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {


			if (topic.contains("Event")) {
			} else {

				if (topic.contains(global.SYSTEM_ID) && topic.contains("info")) {

					String service = topic.split("/")[1];
					String emaID = msgPayload.split("/")[1];
					String devID = msgPayload.split("/")[3];

					DeviceControl hm = new DeviceControl(client, service, emaID, devID, msgPayload);
					hm.start();

				}

				if (topic.contains("DeviceConnect") || topic.contains("DeviceDisconnect")) {

					String service = topic.split("/")[1];
					String emaID = msgPayload.split("/")[1];
					String devID = msgPayload.split("/")[3];

					DeviceControl hm = new DeviceControl(client, service, emaID, devID, msgPayload);
					hm.start();

				}
				// else {
				//
				// String[] gwID = msgPayload.split("/");
				// gw = gwID[0];
				// gwNum = Integer.parseInt(gwID[1]);
				//
				// String[] topicParse = topic.split("/");
				//
				// topic_process = topicParse[1];
				//
				// if (gwID[2].equals("dev")) {
				//
				// dev = gwID[2];
				// devNum = Integer.parseInt(gwID[3]);
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gw, gwNum, dev,
				// devNum);
				// } else {
				// HandleMqttMessage hm = new HandleMqttMessage(client,
				// topic_process, msgPayload, gwNum);
				// }
				// }
			}
		}

	}

	public MqttClient getClient() {
		return client;
	}

	public void setClient(MqttClient client) {
		this.client = client;
	}

}
