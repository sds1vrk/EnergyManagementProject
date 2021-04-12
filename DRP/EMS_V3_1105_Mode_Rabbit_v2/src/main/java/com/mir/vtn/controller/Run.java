package com.mir.vtn.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.mir.ems.GUI.Initial;
import com.mir.ems.GUI.MainFrame;
import com.mir.ems.coap.CoAPServer;
import com.mir.ems.globalVar.global;
import com.mir.ems.main.EmsMainClass;
import com.mir.pushServer.PushHttpServer;
import com.mir.rest_server.Config;
import com.mir.rest_server.NettyRestServer;
import com.mir.vtn.profile.NegoProfile;




import com.mir.rabbitMQ.*;

@SpringBootApplication
public class Run {

	static void eventIDFileIO() {

		FileReader fileReader;

		try {
			fileReader = new FileReader("EVENTID.txt");
			BufferedReader br = new BufferedReader(fileReader);

			String type = null;

			while ((type = br.readLine()) != null) {

				int seq = Integer.parseInt(type.split("=>")[0]);
				String eventID = type.split("/")[1];

				global.eventID.put(seq, eventID);

			}

		} catch (IOException e) {

		}

	}

	public static void main(String... args) throws Exception {

		try {
			eventIDFileIO();
			FileReader fileReader = new FileReader("ems.cfg");

			BufferedReader br = new BufferedReader(fileReader);

			global.summaryReport = Boolean.parseBoolean(br.readLine().split(":")[1]);
			global.summaryInterval = Integer.parseInt(br.readLine().split(":")[1]);

			thresholdCFG();

			if (br.readLine().split(":")[1].equals("COAP")) {
				new CoAPServer();
			}
			
			else {
				
				
				rabbitCFG();
				
				//subscribe
				Subscribe sub=new Subscribe();
				sub.run();
				
				
				//publish
				
				Publish pub=new Publish();
				pub.run();
				
				
				
//				new Publish.start();
				
				
				
			}
			
			

			global.autoDR = Boolean.parseBoolean(br.readLine().split(":")[1]);
			global.rest_port = Integer.parseInt(br.readLine().split(":")[1]);

			br.close();
		} catch (Exception e) {

		}

		new EmsMainClass();
//		new CoAPServer();

		SpringApplication.run(Run.class, args);

		PushHttpServer server = new PushHttpServer();
		server.start();
		
		

		// rest_Server
//		@SuppressWarnings("resource")
//		AbstractApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//		NettyRestServer server1 = context.getBean(NettyRestServer.class);
//		server1.start();

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

	public static void thresholdCFG() {

		try {
			FileReader fileReader = new FileReader("threshold.cfg");

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String sema1s = bufferedReader.readLine();
			String[] sema1 = sema1s.split(":");
			global.SEMA_thresholdProfile.put(sema1[0], Double.valueOf(sema1[1]));

			String sema2s = bufferedReader.readLine();
			String[] sema2 = sema2s.split(":");
			global.SEMA_thresholdProfile.put(sema2[0], Double.valueOf(sema2[1]));

			String sema3s = bufferedReader.readLine();
			String[] sema3 = sema3s.split(":");
			global.SEMA_thresholdProfile.put(sema3[0], Double.valueOf(sema3[1]));

			System.out.println("sema[1]" + sema1[0] + "value" + sema1[1]);

			System.out.println("sema[2]" + sema2[0] + "value" + sema2[1]);

			System.out.println("sema[3]" + sema3[0] + "value" + sema3[1]);
			
//			초기에 객체만 5000으로 해주면 됨
//			NegoProfile nego=new NegoProfile();
//			nego.setId(sema1[0]);
//			nego.setThreshold(sema1[0]);
//			global.Nego_Threshold.put(id, nego);
			

			bufferedReader.close();
			Thread.sleep(500);
		} catch (Exception e) {

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
			RabbitGlobal.sub_exchangeName=br.readLine().split(":")[1];
			RabbitGlobal.pub_exhcangeName=br.readLine().split(":")[1];
			

			br.close();
			Thread.sleep(500);
		} catch (Exception e) {

		}

	}
	

}
