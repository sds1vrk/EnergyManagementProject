package com.mir.rest.clent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.database.item.Rest_Ema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.EventResponder;
import com.mir.ems.profile.emap.v2.Profile;
import com.mir.ems.profile.emap.v2.Transports;

public class REST_Structure {

	public static String data;

	public ConcurrentHashMap<String, Emap_Device_Profile> cs;

//	JSONObject json=new JSONObject(getData());

//	JSONObject js=new JSONObject(data);

//	HashMap<String, String>test=new HashMap<>();

	public ArrayList<String> arr = new ArrayList<>();

	public ArrayList<String> getListData() {
		if (arr != null) {
			return arr;
		}

		else {
			return arr;
		}

	}

	public static String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	// public emaProfile<String, Rest_Ema_Profile>emaProfile;
	public REST_Structure() throws JSONException {

	}

	public static String explicit_rest() throws JSONException {
		JSONObject json = new JSONObject();

		json.put("REST_CHS", global.SYSTEM_ID);
		json.put("DEVICETYPE", "EMS");

		ArrayList<String> REST_EMA = new ArrayList<String>();

		// REST_EMA Network
		REST_EMA.addAll(global.rest_EMAProfile.keySet());

		try {
			JSONArray SEMA_array = new JSONArray();

			for (int k = 0; k < REST_EMA.size(); k++) {
				
				
				System.out.println("여기 들어옴?!111");
				
				JSONObject EMA = new JSONObject();
				EMA.put("EMAID", REST_EMA.get(k));
				EMA.put("DEVICETYPE", "REMA");

//				System.out.println("SEMAID?!" + SEMALIST.get(k));

				// DRP
				JSONArray DRP_array = new JSONArray();

				int i = 0;
				Iterator<REST_EMSProfile> dterator = global.rest_EMSProfile.values().iterator();
				while (dterator.hasNext()) {

					REST_EMSProfile dProfile = dterator.next();

					if (dProfile.getEmaID().equals(REST_EMA.get(k))) {
						JSONObject DRP = new JSONObject();

						DRP.put("DRPID", dProfile.getDeviceID());
						DRP.put("DEVICETYPE", "DRP");
						JSONArray SEMA_Array = new JSONArray();

						Iterator<REST_SemaProfile> sIterator = global.rest_SemaProfile.values().iterator();
//						int j = 0;
						while (sIterator.hasNext()) {
//							j++;
							REST_SemaProfile sProfile = sIterator.next();

//							System.out.println("dProfile"+dProfile.getEmaID()+"subEMALIST . get(i)"+subEMALIST.get(i));
							if (sProfile.getEmaID().equals(dProfile.getDeviceID())) {
								i++;

//								System.out.println("here?! to DEVICELIST?!");
								JSONObject SEMA = new JSONObject();
								SEMA.put("SEMAID", sProfile.getsubID());
								SEMA.put("DEVICETYPE", "SEMA");

								JSONArray CEMA_Array = new JSONArray();

								Iterator<REST_CemaProfile> cIterator = global.rest_CemaProfile.values().iterator();

								while (cIterator.hasNext()) {
									REST_CemaProfile cProfile = cIterator.next();

									if (cProfile.getEmaID().equals(sProfile.getSubID())) {

										JSONObject CEMA = new JSONObject();
										CEMA.put("CEMAID", cProfile.getDeviceEMAID());
										CEMA.put("DEVICETYPE", "CEMA");

										JSONArray DEVICE_Array = new JSONArray();
										Iterator<REST_DeviceProfile> deviceIterator = global.rest_deviceProfile.values()
												.iterator();

										while (deviceIterator.hasNext()) {
											REST_DeviceProfile deviceProfile = deviceIterator.next();

											if (deviceProfile.getEmaID().equals(cProfile.getDeviceEMAID())) {

												JSONObject DEVICE = new JSONObject();
												DEVICE.put("DEVICEID", deviceProfile.getDeviceEMAID());
												DEVICE.put("DEVICETYPE", "DEVICE");

												DEVICE_Array.put(DEVICE);
											}

										}
										CEMA.put("DEVICEIDs", DEVICE_Array);
										CEMA_Array.put(CEMA);

									}
								}
								SEMA.put("CEMAIDs", CEMA_Array);
								SEMA_Array.put(SEMA);
							}
						}
						DRP.put("SEMAIDs", SEMA_Array);
						DRP_array.put(DRP);
					}

				}
				EMA.put("DRPIDs", DRP_array);
				SEMA_array.put(EMA);

			}
			json.put("EMAIDs", SEMA_array);
			System.out.println(json.toString());

			System.out.println("POST TO SEMA or VISOR" + json.toString());

		}

		catch (Exception e) {
			// TODO: handle exception
		}

		return json.toString();

	}

	public static String implicit_rest() throws JSONException {
		JSONObject json = new JSONObject();

		json.put("EMSID", "REST_EMS");
		ArrayList<String> SEMALIST = new ArrayList<String>();

//		SEMALIST.add(global.CHILD_ID);

		SEMALIST.addAll(global.rest_SemaProfile.keySet());
//		json.put("SEMAIDs", SEMALIST);
//		ArrayList<String> subEMALIST = new ArrayList<String>();
////
//		subEMALIST.addAll(global.rest_CemaProfile.keySet());

//		subEMALIST.addAll(global.rest_SemaProfile.keySet());

		try {
			JSONArray SEMA_array = new JSONArray();

//DEVICE KEY 
			for (int k = 0; k < SEMALIST.size(); k++) {
				JSONObject SEMA = new JSONObject();
				SEMA.put("SEMAID", SEMALIST.get(k));
//				System.out.println("SEMAID?!"+SEMALIST.get(k));
				SEMA_array.put(SEMA);

			}

			json.put("SEMAIDs", SEMA_array);
//			System.out.println(json.toString());

		}

		catch (Exception e) {
			// TODO: handle exception
		}

		return json.toString();

	}

	public static String ami_explicit_rest() throws JSONException {
		JSONObject json = new JSONObject();

		json.put("EMSID", "REST_CHS");

		ArrayList<String> AMILIST = new ArrayList<String>();
		AMILIST.addAll(global.rest_MDMS_EMSProfile.keySet());

		ArrayList<String> SEMALIST = new ArrayList<String>();

		SEMALIST.add("MDMS");

		// json.put("SEMAID", global.SYSTEM_ID);

//		ArrayList<String> subEMALIST = new ArrayList<String>();
////		subEMALIST.addAll(global.rest_DCUProfile.keySet());

		try {
			JSONArray AMI_array = new JSONArray();

			for (int k = 0; k < AMILIST.size(); k++) {
				JSONObject SEMA = new JSONObject();
				SEMA.put("AMIID", AMILIST.get(k));
				SEMA.put("DEVICETYPE", "AMI");

//				SEMA.put("s_threshold", global.THRESHOLD);
				JSONArray SUBEMA_array = new JSONArray();

				int j = 0;
				Iterator<REST_MdmsProfile> cIterator = global.rest_MDMSProfile.values().iterator();
				while (cIterator.hasNext()) {
					j++;
					REST_MdmsProfile cprofile = cIterator.next();

					JSONObject CEMA = new JSONObject();
					CEMA.put("MDMSID", cprofile.getsubID());
					CEMA.put("DEVICETYPE", "MDMS");

//		 				CEMA.put("power",cprofile.getPower());
//		 				CEMA.put("threshold",cprofile.getMargin());

					// device
					JSONArray EDGE_array = new JSONArray();

					int i = 0;
					Iterator<REST_DcuProfile> kIterator = global.rest_DCUProfile.values().iterator();
					while (kIterator.hasNext()) {
						i++;
						REST_DcuProfile kProfile = kIterator.next();

						if (cprofile.getEmaID().equals(kProfile.getDeviceEMAID())) {

							JSONObject EDGE = new JSONObject();
							EDGE.put("DCUID", kProfile.getDeviceEMAID());
							EDGE.put("DEVICETYPE", "DCU");

							EDGE_array.put(EDGE);

							JSONArray METER_array = new JSONArray();
							Iterator<REST_MeterProfile> mIterlator = global.rest_meterProfile.values().iterator();

							while (mIterlator.hasNext()) {
								REST_MeterProfile mProfile = mIterlator.next();

								if (mProfile.getEmaID().equals(kProfile.getDeviceEMAID())) {
									JSONObject METER = new JSONObject();
									METER.put("METERID", mProfile.getDeviceEMAID());
									METER.put("DEVICETYPE", "METER");

									METER_array.put(METER);
								}

							}

						}
					}
					CEMA.put("METERIDs", EDGE_array);
					SUBEMA_array.put(CEMA);
//		 			}
				}
				SEMA.put("DCUIDs", SUBEMA_array);
				AMI_array.put(SEMA);
			}
			json.put("MDMSIDs", AMI_array);
			System.out.println(json.toString());
		}

		catch (

		Exception e) {
			// TODO: handle exception
		}

		System.out.println("새로 보낸 JSON MESSAGE" + json.toString());
		return json.toString();
	}

}
