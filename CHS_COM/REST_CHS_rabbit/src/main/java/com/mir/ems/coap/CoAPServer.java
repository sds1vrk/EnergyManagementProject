package com.mir.ems.coap;

import java.net.InetAddress;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.GUI.MainFrame;
import com.mir.ems.coap.emap.DemandResponseEvent;
import com.mir.ems.coap.emap.Emap;
import com.mir.ems.coap.emap.OpenADR;
import com.mir.ems.coap.emap.Opt;
import com.mir.ems.coap.emap.Report;
import com.mir.ems.coap.emap.SessionSetup;
import com.mir.ems.coap.smartmeter.ActiveEnergy;
import com.mir.ems.coap.smartmeter.LoadProfile;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.DeviceClass;
import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.deviceProfile.GatewaySettingPanel;
import com.mir.ems.globalVar.global;
import com.mir.ems.topTab.DRScheduling;

public class CoAPServer {

	final String queryregistration = "QueryRegistration";
	final String createpartyregistration = "CreatePartyRegistration";
	final String registerreport = "RegisterReport";
	final String poll = "Poll";
	final String registeredreport = "RegisteredReport";
	final String requestevent = "RequestEvent";
	final String createdEvent = "createdEvent";
	final String updatereport = "UpdateReport";

	public String gw;
	public String venID;
	public String vtnID = "MIR_VTN";
	public int requestID;
	public int version;
	public String Path;
	public String Text;

	public static int ven;
	public static int poll_seq = 0;
	public static InetAddress client_ip;

	public CoAPServer() {
		// TODO Auto-generated constructor stub
		CoapServer server = new CoapServer();
		CoapResource info = new CoapResource("info") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
//				String text = exchange.getRequestText();
//				System.out.println(getName() + " called PUT method.");
				exchange.respond("info");

				String payload = exchange.getRequestText();

				String device_id = payload.split("/")[3];
				String ema_id = payload.split("/")[1];

				int state = Integer.parseInt(payload.split("/")[4]);
				int dimming = Integer.parseInt(payload.split("/")[5]);
				double value = Integer.parseInt(payload.split("/")[6]);
				int priority = Integer.parseInt(payload.split("/")[7]);

				String ipAddr = exchange.getSourceAddress().toString();

				global.devManger.replace(device_id, new DeviceClass(1, device_id, ema_id, 1, device_id, state, dimming,
						priority, ipAddr, null, value));

				// HandleCoAPMessage hcm = new HandleCoAPMessage(getName(),
				// exchange);
				/*
				 * CoAPDR dr = new CoAPDR(); dr.run(exchange, 0); text =
				 * dr.DRMessage();
				 */
			}
		};

		CoapResource resource0 = new CoapResource("powermargin") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@SuppressWarnings("unused")
			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub

				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");
				exchange.respond("info " + text);

				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
			}
		};

		CoapResource devConnect = new CoapResource("DeviceConnect") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {

				System.out.println("여기");
				String payload = exchange.getRequestText();
				System.out.println(payload);

				String device_id = payload.split("/")[3];
				String ema_id = payload.split("/")[1];

				int state = Integer.parseInt(payload.split("/")[4]);
				int dimming = Integer.parseInt(payload.split("/")[5]);
				double value = Integer.parseInt(payload.split("/")[6]);
				int priority = Integer.parseInt(payload.split("/")[7]);

				String ipAddr = exchange.getSourceAddress().toString();

				global.devManger.put(device_id, new DeviceClass(1, device_id, ema_id, 1, device_id, state, dimming,
						priority, ipAddr, null, value));
				
//				client_ip = exchange.getSourceAddress();
//				// TODO Auto-generated method stub
//				exchange.getSourceAddress();
//				String text = exchange.getRequestText();
//				System.out.println(getName() + " called PUT method.");
//
//				System.out.println("VEN Information Arrived.");
//
//				// String addr = client_ip.getHostAddress();
//				// String addr_token[] = addr.split(".");
//				// int ven_id = Integer.parseInt(addr_token[3]);
//				int ven_id = Integer.parseInt(text);
//
//				if (MainFrame.gateway_db.gateway_list.containsKey(ven_id)) {
//					if (!MainFrame.gateway_db.gateway_list.get(ven_id).ip_addr.equals(client_ip))
//						MainFrame.gateway_db.gateway_list.get(ven_id).ip_addr = client_ip;
//					if (MainFrame.gateway_db.gateway_list.get(ven_id).port_num != 5683)
//						MainFrame.gateway_db.gateway_list.get(ven_id).port_num = 5683;
//				} else {
//					MainFrame.gateway_db.addValue(ven_id + "", 2, client_ip, 5683, "CoAP VEN" + ven_id, 1, 101,
//							"class room", 0);
//					System.out.println("VEN ID: " + MainFrame.gateway_db.gateway_list.get(ven_id).gateway_id);
//					// port, building name, floor, room num, room name,
//					// threshold
//
//					// add new gateway to the list
//					GatewaySettingPanel.modify_gateway_table();
//				}
//				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
//
//				text = "Connect Success!";
//				exchange.respond("Connect " + text);
			}
		};

		CoapResource resource2 = new CoapResource(queryregistration) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub

				System.out.println("여기보자");
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();

				JSONObject jsonObj;
				try {
					jsonObj = new JSONObject(exchange.getRequestText());
					String emaID = jsonObj.getString("GW");
					String[] parseEMA = emaID.split("/");
					emaID = "EMA" + parseEMA[1];

					Emap_Cema_Profile emaProfile = new Emap_Cema_Profile(emaID, "NONE", "NONE", 0, 0, 0.0, 0.0, 0.0,
							0.0, 0.0, 0.0, 0.0, new JSONObject(), new JSONObject(), "CONNECT");

					global.putEmaProtocolCoAP(emaID, emaProfile);
					global.emaProtocolCoAP_EventFlag.put(emaID, new EMAP_CoAP_EMA_DR());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				exchange.respond("DR " + text);
			}
		};
		CoapResource resource3 = new CoapResource(createpartyregistration) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();

				System.out.println(text);
				exchange.respond("DR " + text);
			}
		};

		CoapResource resource4 = new CoapResource(registerreport) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				exchange.respond("DR " + text);
			}
		};
		CoapResource resource5 = new CoapResource(poll) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				DRScheduling drf = new DRScheduling();
				int d_event = drf.state();

				CoAPDR dr = new CoAPDR();
				System.out.println(exchange.getSourcePort());

				int ven_id = 0;
				dr.newVEN();
				if (dr.newVEN() == 1) {// poll_seq == 0){
					dr.run(exchange, 0);
					text = dr.DRMessage();
					exchange.respond("DR " + text);

					poll_seq = dr.newVEN();
				} else if (poll_seq == 2) {

					ven_id = dr.GetVENID();

					dr.run(exchange, 0);
					ven_id = dr.GetVENID();

					int event_index = drf.VEN_ID();
					switch (d_event) {
					case 1:
						if (event_index == ven_id) {

							text = drf.DR_MSG();
							poll_seq = 2;
							exchange.respond("DR_E " + text);
							d_event = 2;

							break;

						} else {
							dr.run(exchange, 1);
							text = dr.DRMessage();
							exchange.respond("DR_P " + text);
							d_event = 1;

							break;

						}

					default:
						dr.run(exchange, 1);
						text = dr.DRMessage();
						exchange.respond("DR_P " + text);
					}

				} else {
					dr.run(exchange, 1);
					text = dr.DRMessage();
					exchange.respond("DR_P " + text);
					poll_seq = drf.state();
				}

			}
		};

		CoapResource resource6 = new CoapResource(registeredreport) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				exchange.respond("DR " + text);
			}
		};

		CoapResource resource7 = new CoapResource(requestevent) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				exchange.respond("DR " + text);
			}
		};

		CoapResource resource8 = new CoapResource(createdEvent) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				exchange.respond("DR " + text);

				// HandleCoAPMessage hcm = new HandleCoAPMessage(getName(),
				// exchange);
			}
		};

		CoapResource resource9 = new CoapResource(updatereport) {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");

				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub

				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");
				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				exchange.respond("DR " + text);

				// HandleCoAPMessage hcm = new HandleCoAPMessage(getName(),
				// exchange);
			}
		};

		CoapResource resource10 = new CoapResource("ESS") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("ESS");
			}
		};

		CoapResource resource11 = new CoapResource("ESSinit") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();

				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("ESS");
			}
		};

		CoapResource resource12 = new CoapResource("PVinit") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();

				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("PV");
			}
		};

		CoapResource resource13 = new CoapResource("PV") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");
				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("PV");
			}
		};

		CoapResource resource14 = new CoapResource("negotiation") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");

				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("Negotiation");
			}
		};

		CoapResource rdr = new CoapResource("oadrRDR") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub

				// String text = exchange.getRequestText();
				// System.out.println(getName() + " called PUT method.");

				try {
					JSONObject json = new JSONObject(exchange.getRequestText());

					String emaID = json.getString("SrcEMA");
					String devID = json.getString("devnumber");
					double requestThreshold = json.getDouble("value");

					json = new JSONObject();

					json.put("SrcEMA", global.SYSTEM_ID);
					json.put("devnumber", devID);
					json.put("responseDescription", "OK");
					json.put("responseCode", 200);
					exchange.respond(ResponseCode.CONTENT, json.toString(), MediaTypeRegistry.APPLICATION_JSON);

				} catch (JSONException e) {

				}		

				// HandleCoAPMessage hcm = new HandleCoAPMessage(getName(),
				// exchange);
			}
		};

		CoapResource devDisconnect = new CoapResource("DeviceDisconnect") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub

				String payload = exchange.getRequestText();

				String device_id = payload.split("/")[3];

				global.devManger.remove(device_id);
				
//				String text = exchange.getRequestText();
//				System.out.println(getName() + " called PUT method.");
//
//				exchange.respond("Device Disconnected " + text);
//
//				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
			}
		};
		CoapResource resource17 = new CoapResource("RECLOSER") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");
				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("PV");
			}
		};
		CoapResource resource18 = new CoapResource("RECLOSERinit") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");
				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("PV");
			}
		};
		CoapResource resource19 = new CoapResource("RESOURCE") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");
				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("PV");
			}
		};
		CoapResource resource20 = new CoapResource("RESOURCEinit") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.getSourceAddress();
				String text = exchange.getRequestText();
				System.out.println(getName() + " called PUT method.");
				CoAPDR dr = new CoAPDR();
				dr.run(exchange, 0);
				text = dr.DRMessage();
				HandleCoAPMessage hcm = new HandleCoAPMessage(getName(), exchange);
				exchange.respond("PV");
			}
		};
		CoapResource summaryACK = new CoapResource("SummaryACK") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
			
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
			}
		};
		server.add(summaryACK);
		server.add(rdr);
//		server.start();

		server.add(info);
//		server.start();

		server.add(devConnect);
		server.add(devDisconnect);

		//
		// server.add(resource1);
		// server.start();
		//
		// server.add(resource2);
		// server.start();
		//
		// server.add(resource3);
		// server.start();
		//
		// server.add(resource4);
		// server.start();
		//
		// server.add(resource5);
		// server.start();
		//
		// server.add(resource6);
		// server.start();
		//
		// server.add(resource7);
		// server.start();
		//
		// server.add(resource8);
		// server.start();
		//
		// server.add(resource9);
		// server.start();
		//
		// server.add(resource10);
		// server.start();
		//
		// server.add(resource11);
		// server.start();
		//
		// server.add(resource12);
		// server.start();
		//
		// server.add(resource13);
		// server.start();
		//
		// server.add(resource14);
		// server.start();
		//
		// server.add(resource15);
		// server.start();
		//
		// server.add(resource16);
		// server.start();
		//
		// server.add(resource17);
		// server.start();
		// server.add(resource18);
		// server.start();
		// server.add(resource19);
		// server.start();
		// server.add(resource20);
		// server.start();
		server.add(new CoAPObserver("obs"));

		// MDMS

		// server.add(new LoadProfile("load_profile"));
		// server.add(new ActiveEnergy("active_energy"));
		// server.start();

		/*
		 * EMAP Old VERSION : START
		 */

		// server.add(new SessionSetup("ConnectRegistration"));
		// server.add(new SessionSetup("CreatePartyRegistration"));
		// server.add(new SessionSetup("CancelPartyRegistration"));
		//
		// server.add(new Report("RegisterReport"));
		// server.add(new Report("UpdateReport"));
		// server.add(new Report("RegisteredReport"));
		//
		// server.add(new Opt("CreateOpt"));
		// server.add(new Opt("CancelOpt"));
		//
		// server.add(new DemandResponseEvent("Poll"));
		// server.add(new DemandResponseEvent("CreatedEvent"));
		// server.add(new DemandResponseEvent("RequestEvent"));

		/*
		 * EMAP Old VERSION : END
		 */

		/*
		 * EMAP NEW VERSION : START
		 */

		server.add(new Emap("EMAP"));

		server.add(new OpenADR("OpenADR"));

		// Observe

		server.add(new CoAPObserver("OpenADR2.0b"));
		server.add(new CoAPObserver("EMAP1.0b"));

		// server.add(new SessionSetup("EMAP/SERVER_EMS1/1.0b/SessionSetup"));
//		if(global.summaryReport) {
//			System.out.println("DDDDDDD");
//			server.add(new CoAPSummaryObserver("Summary"));
//		}
		/*
		 * EMAP NEW VERSION : END
		 */

		setNetworkConfiguration();

		// server.addEndpoint(new CoapEndpoint(5683,
		// NetworkConfig.createStandardWithoutFile()
		// .setString(NetworkConfig.Keys.DEDUPLICATOR,
		// NetworkConfig.Keys.NO_DEDUPLICATOR)
		// .setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE,
		// 2000).setInt(NetworkConfig.Keys.EXCHANGE_LIFETIME, 1500)));

		server.addEndpoint(new CoapEndpoint(5683, setNetworkConfiguration()));

		server.start();

	}

	public int poll_sate() {
		int state = 0;
		return state;
	}

	private NetworkConfig setNetworkConfiguration() {

		return NetworkConfig.createStandardWithoutFile()
				.setString(NetworkConfig.Keys.DEDUPLICATOR, NetworkConfig.Keys.NO_DEDUPLICATOR)
				.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, 60000)
				.setInt(NetworkConfig.Keys.UDP_CONNECTOR_DATAGRAM_SIZE, 60000)
				.setInt(NetworkConfig.Keys.UDP_CONNECTOR_SEND_BUFFER, 60000)
				.setInt(NetworkConfig.Keys.UDP_CONNECTOR_RECEIVE_BUFFER, 60000)
				.setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, 60000).setInt(NetworkConfig.Keys.EXCHANGE_LIFETIME, 1500);
	}

	public static void main(String[] args) {

		new CoAPServer();

	}
}
