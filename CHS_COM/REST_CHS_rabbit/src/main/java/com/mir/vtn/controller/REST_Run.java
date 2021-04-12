package com.mir.vtn.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mir.ems.GUI.Initial;
import com.mir.ems.GUI.MainFrame;
import com.mir.ems.coap.CoAPServer;
import com.mir.ems.globalVar.global;
import com.mir.ems.main.EmsMainClass;
import com.mir.pushServer.PushHttpServer;
import com.mir.rabbitMQ.RabbitGlobal;
import com.mir.rabbitMQ.Subscribe;
import com.mir.rest.clent.*;
//import com.mir.smartgrid.simulator.global.Global;


@SpringBootApplication
public class REST_Run {
	
//	static REST_Structure rs ;
	
	

	static void eventIDFileIO() {
		
		FileReader fileReader;
		
		try {
			
			fileReader = new FileReader("EVENTID.txt");
			BufferedReader br = new BufferedReader(fileReader);
			
			String type=null;
			
			while((type=br.readLine()) != null) {
				
				int seq = Integer.parseInt(type.split("=>")[0]);
				String eventID = type.split("/")[1];
				
				global.eventID.put(seq, eventID);
				
			}
			
		}catch (IOException e) {
			
			
			
			
		}
		
	}
	

	
	public static void main(String... args) throws Exception {

		try {
			eventIDFileIO();
			FileReader fileReader = new FileReader("ems.cfg");

			BufferedReader br = new BufferedReader(fileReader);

			global.summaryReport = Boolean.parseBoolean(br.readLine().split(":")[1]);
			global.summaryInterval = Integer.parseInt(br.readLine().split(":")[1]);
			
			global.autoDR = Boolean.parseBoolean(br.readLine().split(":")[1]);
		
			global.rest_reportType=br.readLine().split(":")[1];
			
			
			
			
			br.close();
		} catch (Exception e) {

		}

		new EmsMainClass();
		
		

		
		
		
		
		
//		new CoAPServer();

//		SpringApplication.run(REST_Run.class, args);
////
//		PushHttpServer server = new PushHttpServer();
//		server.start();
//		
		
//		EchoApplication_test ea = new EchoApplication_test();
//		ea.netty_start("192.168.1.171");
//		System.out.println("실행됨?");
////		ea.start();
//		ea.run();
		
//		EchoApplication_test ea2 = new EchoApplication_test();
//		ea2.netty_start("192.168.1.172");
		
//		EchoApplication ea=new EchoApplication("192.168.1.171");
//		ea.rest_topology();
//		
//		
//		EchoApplication ea2=new EchoApplication("192.168.1.172");
//		ea2.rest_topology();
		
		
		
		//SEMA
//		REST_CLIENT_GET ea4=new REST_CLIENT_GET("192.168.35.172",7778);
//		ea4.rest_topology();
//		
//		
//		REST_CLIENT_GET ea3=new REST_CLIENT_GET("192.168.35.171",7778);
//		ea3.rest_topology();
		
		
		
		//AMI Topology (REST GET)
//		REST_CLIENT_GET ems=new REST_CLIENT_GET("192.168.1.214",7777);
//		ems.rest_topology();
		
//		REST_CLIENT_GET ems2=new REST_CLIENT_GET("192.168.1.171",7778);
//		ems2.rest_topology();
		
		//AMI Topology (REST GET)
//		REST_AMI_CLIENT_GET ami=new REST_AMI_CLIENT_GET("192.168.1.171",7778);
//		ami.rest_topology();
//		
//		
//		REST_AMI_CLIENT_GET ami2=new REST_AMI_CLIENT_GET("192.168.1.172",7778);
//		ami2.rest_topology();
//		
//		REST_AMI_CLIENT_GET ami3=new REST_AMI_CLIENT_GET("192.168.1.173",7778);
//		ami3.rest_topology();
		
		
		
		//REST POST
//		REST_CLEINT_POST ea5=new REST_CLEINT_POST("192.168.1.214",7777);
//		ea5.rest_topology();
//		
//		
//		REST_CLIENT_AMI_EMA_POST ea6=new REST_CLIENT_AMI_EMA_POST("192.168.35.6",7778);
//		ea6.rest_topology();
		
		//AMI POST
//		REST_AMI_POST amipost=new REST_AMI_POST("192.168.35.171",7778);
//		amipost.rest_topology();
//		
//		REST_CLIENT_AMI_Post amipost2=new REST_CLIENT_AMI_Post("192.168.35.214",7778);
//		amipost2.rest_topology();
		
		
		
		
		//EMS
//		REST_CLIENT_GET ems=new REST_CLIENT_GET("192.168.1.214",7777);
//		ems.rest_topology();
		
	
		
		
		//임시 방편
//		System.err.println(global.rest_deviceProfile.size()+"DEVICEPROFILE SIZE");
//		if (global.rest_deviceProfile.size()==100) {
//			REST_CLEINT_POST ea5=new REST_CLEINT_POST("192.168.1.171",7777);
//			ea5.rest_topology();
//			
//			REST_CLEINT_POST ea6=new REST_CLEINT_POST("192.168.1.172",7777);
//			ea6.rest_topology();
//		}
		

		
		
//		REST_CLIENT_GET ea3=new REST_CLIENT_GET("192.168.1.171",7777);
//		ea3.rest_topology();
//		
//		REST_CLIENT_GET ea4=new REST_CLIENT_GET("192.168.1.172",7777);
//		ea4.rest_topology();
		
		
		
		//policy IP
//		policy_client policy=new policy_client("192.168.1.165",9998);
//		policy.rest_topology();
		
		
//		EchoApplication ea4=new EchoApplication("192.168.1.197");
//		ea4.rest_topology();
//		
//		
//		EchoApplication ea3=new EchoApplication("192.168.1.214");
//		ea3.rest_topology();
		
		
		//netty 3version
		
//		RESTClient restAgent = new RESTClient();
//		restAgent.setRestClient(restAgent);
		
		
//		REST_Structure rs = new REST_Structure();
//		ArrayList<String> test=rs.arr;
		
		
//		ArrayList<String>test=rs.getListData();
		
//		int k=test.size();
//		for(int i=0;i<k;i++) {
//			System.out.println(test.get(i));
//		}
		
		
//		Initial initial;
//		new EmsMainClass();
//		main.initial = new Initial();
//		main.initial.setMain(main);

		// Global.setBrokerIP("166.104.28.51");
		// Global.setParentnNodeID("SERVER_EMS1");

		// boolean pullModel = true;

		// Thread emapExcute = new Thread(new Connection("VTN1", "EMAP1.0b", "MQTT"));
		// emapExcute.start();
		// new Connection("CLIENTEMA1").start();

		// Global.databaseConnection.start();
		// new StoreData_cema();

	}

}
