package com.mir.rabbitMQ;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.globalVar.global;

public class Rabbit_Topology {

	public String topology4() throws JSONException {

		JSONObject json = new JSONObject();

		json.put("EMSID", global.SYSTEM_ID);
		json.put("DEVICETYPE", "EMS");

//		ArrayList<String> EMSLIST=new ArrayList<String>();

//		EMSLIST.add(global.SYSTEM_ID);

		ArrayList<String> SEMALIST = new ArrayList<String>();

//		SEMALIST.addAll(global.rest_SemaProfile.keySet());
		SEMALIST.addAll(global.emaProtocolCoAP.keySet());

		// json.put("SEMAID", global.SYSTEM_ID);

		ArrayList<String> subEMALIST = new ArrayList<String>();
//		subEMALIST.addAll(global.rest_CemaProfile.keySet());
		subEMALIST.addAll(global.emaProtocolCoAP_Device.keySet());

		try {

//			JSONArray EMS_array=new JSONArray();

//			for(int i=0;i<EMSLIST.size();i++) {
//				JSONObject EMS=new JSONObject();
//				EMS.put("EMSID", EMSLIST.get(i));
//				EMS.put("DEVICETYPE", "EMS");

			JSONArray SEMA_array = new JSONArray();

			for (int k = 0; k < SEMALIST.size(); k++) {
				JSONObject SEMA = new JSONObject();
				SEMA.put("SEMAID", SEMALIST.get(k));
				SEMA.put("DEVICETYPE", "EMA");

				JSONArray SUBEMA_array = new JSONArray();
				for (int j = 0; j < subEMALIST.size(); j++) {
					JSONObject CEMA = new JSONObject();
					CEMA.put("CEMAID", subEMALIST.get(j));
					CEMA.put("DEVICETYPE", "EMA");
					SUBEMA_array.put(CEMA);
				}

				SEMA.put("CEMAIDs", SUBEMA_array);
				SEMA_array.put(SEMA);

			}
			json.put("SEMAIDs", SEMA_array);

//			}

//			json.put("EMSIDs", EMS_array);

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