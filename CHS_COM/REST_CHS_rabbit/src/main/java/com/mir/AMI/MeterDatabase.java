package com.mir.AMI;

import java.util.ArrayList;

public class MeterDatabase {
	public ArrayList<MeterClass> meter_list;

	public MeterDatabase() {
		meter_list = new ArrayList<MeterClass>();
	}

	public void addValue(String dcuID, String meterID, String curEnergy, String activeEnergy) {

		meter_list.add(new MeterClass(dcuID, meterID, curEnergy, activeEnergy));

	}

}
