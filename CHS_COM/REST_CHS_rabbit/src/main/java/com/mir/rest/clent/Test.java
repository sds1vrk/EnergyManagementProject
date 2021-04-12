package com.mir.rest.clent;

import java.util.ArrayList;
import java.util.Arrays;

import com.mir.ems.globalVar.global;

public class Test {

	public void test() {

		ArrayList<String> EMS = new ArrayList<String>();
		EMS.addAll(global.rest_EMSProfile.keySet());
		// emaString.addAll(global.openADRHashMap.keySet());

		String[] ems = new String[EMS.size()];

		for (int i = 0; i < EMS.size(); i++) {
			ems[i] = EMS.get(i).toString();
			System.out.println("EMS:" + ems[i]);

		}

		ArrayList<String> emaString = new ArrayList<String>();

		emaString.addAll(global.rest_SemaProfile.keySet());
		// emaString.addAll(global.openADRHashMap.keySet());

		String[] emaList = new String[emaString.size()];

		for (int k = 0; k < emaString.size(); k++) {

			String dev_key = emaList[k] + "";

			System.out.println("SEMA:" + global.rest_SemaProfile.get(dev_key).getEmaID().toString() + dev_key);

		}

		ArrayList<String> cemaString = new ArrayList<String>();
		cemaString.addAll(global.rest_CemaProfile.keySet());
		// emaString.addAll(global.openADRHashMap.keySet());

		String[] emaList2 = new String[cemaString.size()];

		for (int k = 0; k < emaString.size(); k++) {

			String dev_key = emaList2[k] + "";

			System.out.println("CEMA:" + global.rest_CemaProfile.get(dev_key).getEmaID().toString() + dev_key);

		}

		ArrayList<String> Device = new ArrayList<String>();
		Device.addAll(global.rest_deviceProfile.keySet());
		// emaString.addAll(global.openADRHashMap.keySet());

		String[] emaList3 = new String[Device.size()];

		for (int k = 0; k < emaString.size(); k++) {

			String dev_key = emaList3[k] + "";

			System.out.println("CEMA:" + global.rest_deviceProfile.get(dev_key).getEmaID().toString() + dev_key);

		}

	}

}
