package com.mir.vtn.profile;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;
import com.mir.vtn.global.Global;

import com.mir.ems.Graph.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import java.util.Iterator;

public class NegotiationProfile {

	private String venID, requestID;

//	private double SEMA_BASEWATE = 5000;

	public NegotiationProfile() {

	}

	public String buildUp(String venID, double energy)
			throws SAXException, IOException, ParserConfigurationException, TransformerException, JSONException {

		String requestID = venID;
		double requestEnergy = energy;
		double needEnergy = 0;
		double negotiationEnergy = 0;
		double value = 0;
		double drEnergy = 0;

		System.out.println("requestID:" + requestID);

//		Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();

//		Iterator<String> keys = Global.getRegisterVEN().keySet().iterator();
//		Iterator<String> keys= Global.askdfjalskdfjlkasdfjlk

		Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();

//		System.out.println("size:" + global.emaProtocolCoAP.size());
		System.out.println("size:" + Global.getRegisterVEN().size());

//		needEnergy = requestEnergy - global.Nego_Threshold.get(venID).getThreshold();

		needEnergy = requestEnergy - global.SEMA_thresholdProfile.get(venID);

		negotiationEnergy = needEnergy / (Global.getRegisterVEN().size() - 1);

		JSONObject json = new JSONObject();

		// log save
		EnergyGrpah_Exceed_log log = new EnergyGrpah_Exceed_log();

		// log save

		while (keys.hasNext()) {

			String key = keys.next();

//			System.out.println("ID:"+Global.getRegisterVEN().get(key).getStrVenID());

			String id = global.emaProtocolCoAP.get(key).getEmaID();

			System.out.println("ID" + global.emaProtocolCoAP.get(key).getEmaID());

//			System.out.println("ID2222:"+Global.getRegisterVEN().get(key));

//			if (!((Global.getRegisterVEN().get(key).getStrVenID()).equals(requestID))) {
			if (!(id).equals(requestID)) {
				System.out.println("key:" + global.emaProtocolCoAP.get(key).getEmaID());

				System.out.println("Power Check1:" + global.emaProtocolCoAP.get(key).getPower());

//				double threshold_sEMA=global.Nego_Threshold.get(id).getThreshold();

				double threshold_sEMA = global.SEMA_thresholdProfile.get(id);

				System.out.println("Threshold check" + threshold_sEMA);

//				if ((Global.getRegisterVEN().get(key).getPower()) < threshold_sEMA) {

				if (global.getEmaProtocolCoAP().get(key).getPower() < threshold_sEMA) {
//					double margin = threshold_sEMA - Global.getRegisterVEN().get(key).getPower();
					double margin = threshold_sEMA - global.getEmaProtocolCoAP().get(key).getPower();

//					double margin = SEMA_BASEWATE - global.emaProtocolCoAP.get(key).getPower();

					if (margin >= negotiationEnergy) {

						value = value + negotiationEnergy;

						// 여기에서 다른 SEMA에게 DR을 내려야 함.

					}

				}

			} else {
//				break;

			}

		}
		
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));

		if (value == needEnergy) {
			json.put("Check", "ACCEPTED");
			json.put("Margin", requestEnergy);

			// 여기에서 DR
//			FrontController ft = new FrontController();
//			ft.changeRegisteredVenSeqNum(ema_name);
//			Global.registerVEN.get(ema_name).setThreshold(value);

			// EventDetail

			Iterator<String> it = Global.getRegisterVEN().keySet().iterator();

			while (it.hasNext()) {

				String key = it.next();

				String id = Global.getRegisterVEN().get(key).getStrVenID();

				EventDetail eventDetail = new EventDetail();
				eventDetail.setVenID(id);
				eventDetail.setDtStart("2018-08-07");
				eventDetail.setEvent_flag(false);
				eventDetail.setDuration(1);
				eventDetail.setEvent_flag(false);
				eventDetail.setMarket_context_id("http://MarketContext1");
				eventDetail.setPriority(0);
				eventDetail.setResponse_required_type_id("always");
				eventDetail.setVtn_comment("1");
				eventDetail.setTest_event(false);
				eventDetail.setSignal_name_id("simple");
				eventDetail.setSignal_type_id("delta");
				

				if (!(id.equals(requestID))) {

//					asuasdflkjaslkdf 여기 밑에 줄 수정 Gloa~()

//					double threshold=global.Nego_Threshold.get(id).getThreshold();

					double threshold = global.SEMA_thresholdProfile.get(id);

					drEnergy = threshold - negotiationEnergy;

					
			
					System.out.println("Time:"+str+"/Other key:" + id + "/drEnergy:" + drEnergy);

					log.write_log("Time:"+str+"/Rented ID:" + id + "/STATE:" + "OK" + "/Threshold:" + drEnergy);

					eventDetail.setPayload_value(drEnergy);

					// 추가
//					NegoProfile nego=new NegoProfile();
//					nego.setId(id);
//					nego.setThreshold(drEnergy);
//					global.Nego_Threshold.put(venID, nego);

					global.SEMA_thresholdProfile.put(id, drEnergy);

					Global.getRegisterVEN().get(key).setSeqNum(2);
					Global.eventInfo.put(Global.getRegisterVEN().get(key).getStrVenID(), eventDetail);
					Global.registerVEN.get(key).setThreshold(eventDetail.getPayload_value());

				} else {

					System.out.println("Time:"+str+"/Nego key:" + id + "/requestEnergy:" + requestEnergy);

					log.write_log("Time:"+str+"ID:" + id + "/STATE:" + "ACCEPT" + "/Threshold:" + requestEnergy);

					// 추가
//					NegoProfile nego=new NegoProfile();
//					nego.setId(id);
//					nego.setThreshold(requestEnergy);
//					global.Nego_Threshold.put(venID, nego);

					global.SEMA_thresholdProfile.put(id, requestEnergy);

					eventDetail.setPayload_value(requestEnergy);
					Global.getRegisterVEN().get(key).setSeqNum(2);
					Global.eventInfo.put(Global.getRegisterVEN().get(key).getStrVenID(), eventDetail);
					Global.registerVEN.get(key).setThreshold(eventDetail.getPayload_value());

				}

			}

		} else {
			json.put("Check", "Reject");
			json.put("Margin", 0);

			log.write_log("Time:"+str+"requestID:" + requestID + "/STATE:" + "Reject" + "/Wanted Energy:" + energy);

			// 여기에서 DR? ==> Reject시에 자체적으로 DR

//			EventDetail eventDetail = new EventDetail();
//
//			eventDetail.setVenID(venID);
//			eventDetail.setDtStart("2018-08-07");
//			eventDetail.setEvent_flag(false);
//			eventDetail.setDuration(1);
//			eventDetail.setEvent_flag(false);
//			eventDetail.setMarket_context_id("http://MarketContext1");
//			eventDetail.setPriority(0);
//			eventDetail.setResponse_required_type_id("always");
//			eventDetail.setVtn_comment("1");
//			eventDetail.setTest_event(false);
//			eventDetail.setSignal_name_id("simple");
//			eventDetail.setSignal_type_id("delta");
//			eventDetail.setPayload_value(SEMA_BASEWATE);

		}

		return json.toString();
	}

	public String getVenID() {
		return venID;
	}

	public void setVenID(String venID) {
		this.venID = venID;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

}
