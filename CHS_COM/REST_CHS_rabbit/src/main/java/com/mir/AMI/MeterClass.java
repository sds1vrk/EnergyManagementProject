package com.mir.AMI;

import java.util.ArrayList;

public class MeterClass {

	public String meterID, activeEnergy, curEnergy;
	public String dcuID;
	public ArrayList<Double> value_list;

	public MeterClass(String dcuID, String meterID, String curEnergy, String activeEnergy) {
		this.dcuID = dcuID;
		this.meterID = meterID;
		this.activeEnergy = activeEnergy;
		this.curEnergy = curEnergy;
		value_list = new ArrayList<Double>();

		for (int i = 0; i <= 30; i++) {
			value_list.add(0.0);
		}
	}

	public void addValuesList(Double new_value) {
		value_list.add(new_value);
		value_list.remove(0);
	}

	public void deleteValuesList(Double new_value) {
		value_list.remove(new_value);
	}

	public ArrayList<Double> getValueList() {
		return value_list;
	}
}
