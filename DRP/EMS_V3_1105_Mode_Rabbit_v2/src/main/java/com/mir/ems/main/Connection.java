package com.mir.ems.main;

import java.io.BufferedReader;
import java.io.FileReader;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.mir.ems.coap.CoAPServer;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.Mqtt;
import com.mir.ems.mqtt.emap.PeriodicalSummaryReport;
import com.mir.ems.udp.UdpClient;

public class Connection {

	String protocol_type;

	public String udpIP = "166.104.28.51";

	public int udpPort = 12346;

	public static Mqtt mqttclient = null;

	public static Mqtt mqttclient2 = null;

	public  Mqtt emapmqttclient = null;

	public static Mqtt openADRmqttclient = null;

	public static String mqttIP = "166.104.28.51";

	public static String mqttPort = "1883";

	public static String submqttIP = "166.104.28.51";

	public static String submqttPort = "1883";

	public static String semamqttIP = "166.104.28.51";

	public static String semamqttPort = "1883";

	public static int qos = 0;

	public boolean connection_Status = false;

	public Connection(String protocol_type) {

		this.protocol_type = protocol_type;

		parseCFG();
		
		if (protocol_type.equals("MQTT")) {

			try {

				mqttIP = global.tempIP;
				mqttPort = global.tempPort;
				qos = global.qos;

				/*
				 * 
				 * SUB1
				 * 
				 */

				// String mqtt = "tcp://" + mqttIP + ":" + mqttPort;
				//
				// mqttclient = new Mqtt(mqtt, "emsframe200", false, false,
				// null, null);
				//
				// mqttclient.start();
				//
				// System.out.println("Connected to Mqtt Broker" + mqtt);
				//
				// System.out.println("MQTT SERVER ON!");
				//
				// mqttclient.subscribe("SEMA/#", qos);

				/*
				 * 
				 * SUB2
				 * 
				 */

//				String mqtt2 = "tcp://" + mqttIP + ":" + mqttPort;
//
//				mqttclient2 = new Mqtt(mqtt2, "emsframe2asd0AKSKD50", false, false, null, null);
//
//				System.out.println("Connected to Mqtt Broker" + mqtt2);
//
//				System.out.println("MQTT SERVER ON!");
//				//
//				mqttclient2.subscribe(global.SYSTEM_ID+"/#", qos);

				/*
				 * 
				 * ADDED
				 * 
				 * CoAP VS MQTT Tester
				 * 
				 */

				// String mqtt3 = "tcp://" + submqttIP + ":" + submqttPort;

				// mqttclient2 = new Mqtt(mqtt3, "helloheonil", false, false,
				// null, null);

				//

				// mqttclient2.start();

				//

				// System.out.println("Connected to Mqtt Broker" + mqtt3);

				// System.out.println("MQTT SERVER ON!");

				//

				// mqttclient2.subscribe("EMS/#", qos);

//				String mqtt3 = "tcp://" + semamqttIP + ":" + submqttPort;
//
//				mqttclient2 = new Mqtt(mqtt3, "hellohasdeASDonil1", false, false, null, null);
//
//				mqttclient2.start();
//
//				System.out.println("Connected to Mqtt Broker" + mqtt3);
//
//				System.out.println("MQTT SERVER ON!");
//
//				mqttclient2.subscribe("SEMA/#", qos);

				String mqtt4 = "tcp://" + mqttIP + ":" + submqttPort;

				emapmqttclient = new Mqtt(mqtt4, "asa123sdjdkl", false, false, null, null);

				emapmqttclient.start();

				System.out.println("Connected to Mqtt Broker" + mqtt4);

				System.out.println("MQTT SERVER ON!");

				emapmqttclient.subscribe("/EMAP/" + global.SYSTEM_ID + "/#", qos);

				if(global.summaryReport) {
					System.out.println("SummaryReport");
					setEmapmqttclient(emapmqttclient);
					new PeriodicalSummaryReport(this).start();
				}
				
				String mqtt5 = "tcp://" + mqttIP + ":" + submqttPort;

				openADRmqttclient = new Mqtt(mqtt5, "ra123ndomNumber", false, false, null, null);

				openADRmqttclient.start();

				System.out.println("Connected to Mqtt Broker" + mqtt5);

				System.out.println("MQTT SERVER ON!");

				openADRmqttclient.subscribe("/OpenADR/" + global.SYSTEM_ID + "/#", qos);

				connection_Status = true;

				// udpServer();

			} catch (MqttException e) {

				e.printStackTrace();

				connection_Status = false;

			}

		} else if (protocol_type.equals("CoAP")) {

//			System.out.println("DURL");
//			CoAPServer coapServer = new CoAPServer();

			connection_Status = true;

		} else if (protocol_type.equals("BOTH")) {

			CoAPServer coapServer = new CoAPServer();

			connection_Status = true;

			try {
				String mqtt4 = "tcp://" + mqttIP + ":" + submqttPort;

				Mqtt emapmqttclient = new Mqtt(mqtt4, "asasasdjdkl", false, false, null, null);

				emapmqttclient.start();

				System.out.println("Connected to Mqtt Broker" + mqtt4);

				System.out.println("MQTT SERVER ON!");

				emapmqttclient.subscribe("/EMAP/" + global.SYSTEM_ID + "/#", qos);

				String mqtt5 = "tcp://" + mqttIP + ":" + submqttPort;

				openADRmqttclient = new Mqtt(mqtt5, "ranasddomNumber", false, false, null, null);

				openADRmqttclient.start();

				System.out.println("Connected to Mqtt Broker" + mqtt5);

				System.out.println("MQTT SERVER ON!");

				openADRmqttclient.subscribe("/OpenADR/" + global.SYSTEM_ID + "/#", qos);

			} catch (Exception e) {

			}
			connection_Status = true;

		}

		setUdpIP(global.udpIP);
		setUdpPort(global.udpPort);

		UdpClient udpClient = new UdpClient(getUdpIP(), getUdpPort());
		udpClient.start();

//		try {
//			new StoreData_cema();
//			new StoreData_device();
//			global.databaseConnection.start();
//		} catch (ClassNotFoundException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// udpServer udpServer = new udpServer();

		// udpServer.start();

	}

	public static String getSubmqttIP() {

		return submqttIP;

	}

	public static void setSubmqttIP(String submqttIP) {

		Connection.submqttIP = submqttIP;

	}

	public static String getSubmqttPort() {

		return submqttPort;

	}

	public static void setSubmqttPort(String submqttPort) {

		Connection.submqttPort = submqttPort;

	}

	public static String getSemamqttIP() {

		return semamqttIP;

	}

	public static void setSemamqttIP(String semamqttIP) {

		Connection.semamqttIP = semamqttIP;

	}

	public static String getSemamqttPort() {

		return semamqttPort;

	}

	public static void setSemamqttPort(String semamqttPort) {

		Connection.semamqttPort = semamqttPort;

	}

	public String getUdpIP() {
		return udpIP;
	}

	public Connection setUdpIP(String udpIP) {
		this.udpIP = udpIP;
		return this;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public Connection setUdpPort(int udpPort) {
		this.udpPort = udpPort;
		return this;
	}

	public Mqtt getEmapmqttclient() {
		return emapmqttclient;
	}

	public void setEmapmqttclient(Mqtt emapmqttclient) {
		this.emapmqttclient = emapmqttclient;
	}

	public void parseCFG() {
		
		
		try {
			FileReader fileReader = new FileReader("ems.cfg");
			
			BufferedReader br = new BufferedReader(fileReader);
			
			global.summaryReport = Boolean.parseBoolean(br.readLine().split(":")[1]);
//			if(br.readLine().split(":")[1].equals("COAP")) {
//				new CoAPServer();
//			}
			br.close();
		}catch(Exception e) {
			
		}
		
	}
	
}
