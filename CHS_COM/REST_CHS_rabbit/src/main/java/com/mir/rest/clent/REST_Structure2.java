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

public class REST_Structure2 {

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
	public REST_Structure2() throws JSONException {

	}

	public static String explicit_rest() throws JSONException {
		JSONObject json = new JSONObject();

		json.put("EMSID", "REST_EMS");

		ArrayList<String> SEMALIST = new ArrayList<String>();

//		SEMALIST.add(global.CHILD_ID);

		SEMALIST.addAll(global.rest_SemaProfile.keySet());
		// json.put("SEMAID", global.SYSTEM_ID);

		ArrayList<String> subEMALIST = new ArrayList<String>();
////
		subEMALIST.addAll(global.rest_CemaProfile.keySet());

		subEMALIST.addAll(global.rest_SemaProfile.keySet());

		try {
			JSONArray SEMA_array = new JSONArray();

//DEVICE KEY 
			for (int k = 0; k < SEMALIST.size(); k++) {
				JSONObject SEMA = new JSONObject();
				SEMA.put("SEMAID", SEMALIST.get(k));
				JSONArray SUBEMA_array = new JSONArray();

				Iterator<REST_CemaProfile> cIterator = global.rest_CemaProfile.values().iterator();

				int i = 0;
				while (cIterator.hasNext()) {
					i++;
					REST_CemaProfile cProfile = cIterator.next();
					if (cProfile.getEmaID().equals(SEMALIST.get(k))) {
						JSONObject SUBEMA = new JSONObject();
						SUBEMA.put("CEMAID", cProfile.getDeviceEMAID());

						JSONArray EDGE_array = new JSONArray();

						Iterator<REST_DeviceProfile> kIterator = global.rest_deviceProfile.values().iterator();
						while (kIterator.hasNext()) {
							REST_DeviceProfile kProfile = kIterator.next();
							System.out.println("iterator?!" + kProfile.getEmaID() + ", " + kProfile.getDeviceEMAID());
							if (kProfile.getEmaID().equals(subEMALIST.get(i))) {

								JSONObject EDGE = new JSONObject();
								EDGE.put("DEVICEID", kProfile.getDeviceEMAID());

								EDGE_array.put(EDGE);
							}
						}

						SUBEMA.put("DEVICEs", EDGE_array);
						SUBEMA_array.put(SUBEMA);
					}

				}

				SEMA.put("CEMAIDs", SUBEMA_array);
				SEMA_array.put(SEMA);

			}
			

			json.put("SEMAIDs", SEMA_array);
			System.out.println(json.toString());

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
				System.out.println("SEMAID?!"+SEMALIST.get(k));
				SEMA_array.put(SEMA);

			}
			

			json.put("SEMAIDs", SEMA_array);
			System.out.println(json.toString());

		}

		catch (Exception e) {
			// TODO: handle exception
		}

		return json.toString();
		
		
	}
	

}
