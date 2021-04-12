package com.mir.ems.main;

import java.io.BufferedReader;
import java.io.FileReader;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.mir.ems.coap.CoAPServer;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.Mqtt;
import com.mir.ems.mqtt.emap.PeriodicalSummaryReport;
import com.mir.ems.udp.UdpClient;
import com.mir.rest.clent.REST_AMI_CLIENT_GET;
import com.mir.rabbitMQ.RabbitGlobal;
import com.mir.rabbitMQ.Subscribe;

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
		
		if (protocol_type.equals("REST")) {
			
			//AMI Topology (REST GET)
			REST_AMI_CLIENT_GET ami=new REST_AMI_CLIENT_GET("192.168.1.171",7778);
			ami.rest_topology();
			
			
//			REST_AMI_CLIENT_GET ami2=new REST_AMI_CLIENT_GET("192.168.1.172",7778);
//			ami2.rest_topology();
//			
//			REST_AMI_CLIENT_GET ami3=new REST_AMI_CLIENT_GET("192.168.1.173",7778);
//			ami3.rest_topology();

			
			connection_Status = true;

		} else if (protocol_type.equals("RABBIT")) {
			
			//Rabbit
			rabbitCFG();
			
			//ami Sub
			//subscribe
			Subscribe ami_sub=new Subscribe(RabbitGlobal.ami_sub_exhcangeName);
			ami_sub.run();
			
			
			//ema Sub		
			//subscribe
			Subscribe ema_sub=new Subscribe(RabbitGlobal.ema_sub_exchangeName);
			ema_sub.run();

			connection_Status = true;

		} else if (protocol_type.equals("BOTH")) {



			try {
				
				
	

			} catch (Exception e) {

			}
			connection_Status = true;

		}

//		setUdpIP(global.udpIP);
//		setUdpPort(global.udpPort);
//
//		UdpClient udpClient = new UdpClient(getUdpIP(), getUdpPort());
//		udpClient.start();



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
	
	public static void rabbitCFG() {

		try {
			FileReader fileReader = new FileReader("rabbit.cfg");

			BufferedReader br = new BufferedReader(fileReader);
			
			RabbitGlobal.host = br.readLine().split(":")[1];
			RabbitGlobal.port = Integer.parseInt(br.readLine().split(":")[1]);
			RabbitGlobal.userName = br.readLine().split(":")[1];
			RabbitGlobal.password = br.readLine().split(":")[1];
			RabbitGlobal.virtualHost = br.readLine().split(":")[1];
			RabbitGlobal.ema_sub_exchangeName=br.readLine().split(":")[1];
			RabbitGlobal.ami_sub_exhcangeName=br.readLine().split(":")[1];
			

			br.close();
			Thread.sleep(500);
		} catch (Exception e) {

		}

	}
	
}
