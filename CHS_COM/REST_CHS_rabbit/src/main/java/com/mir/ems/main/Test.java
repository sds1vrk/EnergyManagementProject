package com.mir.ems.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.EventResponder;
import com.mir.ems.mqtt.PushEventListener;

public class Test {
	enum Type {
		CONNECTREGISTRATION, CREATEPARTYREGISTRATION, REGISTERREPORT, POLL, REGISTEREDREPORT, REQUESTEVENT, CANCELPARTYREGISTRATION, PUSH
	}
	public static void main(String[] args) {
		

		
		
//		System.out.println(bb[0]);
		
//		for(int i=0; i<Type.values())
//		Type.values()	
		
//		int bb = Type.values(1);
		
//		Type type = Type.valueOf(service.toUpperCase());
		
		
//		MqttClient mq = null;
//		
//		EventResponder responder = new EventResponder(mq, "emap1.0b", "HYNJIN");
//
//		List<PushEventListener> listeners = new ArrayList<PushEventListener>();
//	
//		
//		listeners.add(responder);
//		
//		for(int i=0; i<listeners.size(); i++){
//			
//			EventResponder aa = (EventResponder) listeners.get(i);
//			System.out.println(aa.getEmaID());
//			
//		}
//		System.out.println(listeners);
//		
////		System.out.println(listeners.);
		
		
		
//		
//		Calendar now = Calendar.getInstance();
//		
//		int sYear = now.get(Calendar.YEAR);
//		int sMonth = now.get(Calendar.MONTH) + 1;
//		int sDate = now.get(Calendar.DATE);
//		
//		int sHour = now.get(Calendar.HOUR_OF_DAY);
//		int sMin = now.get(Calendar.MINUTE);
//		
//		
//		System.out.println(sHour+":" +sMin);
//		
//		
//		
//		
//		
//		System.out.println(global.AVAILABLE_THRESHOLD);
//
//		// TreeMap<A, B>
//		ConcurrentHashMap<String, Double> aa = new ConcurrentHashMap<>();
//		ConcurrentHashMap<String, Double> dup = new ConcurrentHashMap<>();
//		
//		ArrayList<Integer> memoization = new ArrayList<Integer>();
//
//		memoization.add(0);
////		aa.put("RESERVED", 1000.0);
//		aa.put("venID1", 5000.0);
//		aa.put("venID2", 5000.0);
//		aa.put("venID3", 5000.0);
//		aa.put("venID4", 5000.0);
//		aa.put("venID5", 5000.0);
//		aa.put("venID6", 5000.0);
////		aa.put("venID7", 6000.0);
//
//		
//		dup.put("venID1", 5000.0);
//		dup.put("venID2", 5000.0);
//		dup.put("venID3", 5000.0);
//		dup.put("venID4", 5000.0);
//		dup.put("venID5", 5000.0);
//		dup.put("venID6", 5000.0);
//
//		double minusVal = 0;
//
//		
//		while (true) {
//			double available = global.AVAILABLE_THRESHOLD;
//
//			Iterator<String> it = aa.keySet().iterator();
//			int cnt = 0;
//			while (it.hasNext()) {
//				
//				String key = it.next();
//				
//				double threshold;
////				threshold = aa.get(key).doubleValue()-minusVal<0 ? aa.get(key).doubleValue() : aa.get(key).doubleValue()-minusVal;				
//				threshold = dup.get(key).doubleValue();
//				available -= threshold;
//
////				dup.put(key, threshold);
//
//				if (available <= 0) {
//					break;
//				}
//				cnt += 1;
//
//			}
//			memoization.add(cnt);
//
//			minusVal+=1;
//			
//
//			if(Collections.max(memoization)== aa.size()){
//				System.out.println("보자꾸나");
//				System.out.println(available);
//				break;
//			}
//
//			Iterator<String> itit = aa.keySet().iterator();
//			
//			while (itit.hasNext()) {
//				
//				String key = itit.next();
//				double threshold;
//				threshold = dup.get(key).doubleValue()-minusVal<0 ? dup.get(key).doubleValue() : dup.get(key).doubleValue()-minusVal;
//				System.out.println("KEY"+ key + "Threshold"+ threshold);
//
//				dup.replace(key, threshold);
//
//			}
//			
//			
//		}
//
//		Iterator<String> tt = dup.keySet().iterator();
//		
//		while(tt.hasNext()){
//			
//			String key = tt.next();
//			
//			System.out.println("KEY: "+ key+ " Value: "+ dup.get(key).doubleValue());
//			
//		}
//		
//		
//		// }
//		// System.out.println(Collections.max(memoization));
//
//		// for(int i=0; i<)
//
//		// // The name of the file to open.
//		// String fileName = "ElectricityPrice.txt";
//		//
//		// // This will reference one line at a time
//		// String line = null;
//		//
//		// try {
//		// // FileReader reads text files in the default encoding.
//		// FileReader fileReader = new FileReader(fileName);
//		//
//		// // Always wrap FileReader in BufferedReader.
//		// BufferedReader bufferedReader = new BufferedReader(fileReader);
		//
		// while ((line = bufferedReader.readLine()) != null) {
		//
		// if(line.equals("Industrial1")){
		// System.out.println("Hello");
		// }else if(line.equals("Industrial2")){
		// System.out.println("Hello"+"Industrial2");
		// }
		// System.out.println(line);
		// }
		//
		// // Always close files.
		// bufferedReader.close();
		// } catch (FileNotFoundException ex) {
		// System.out.println("Unable to open file '" + fileName + "'");
		// } catch (IOException ex) {
		// System.out.println("Error reading file '" + fileName + "'");
		// // Or we could just do this:
		// // ex.printStackTrace();
		// }
	}
}
