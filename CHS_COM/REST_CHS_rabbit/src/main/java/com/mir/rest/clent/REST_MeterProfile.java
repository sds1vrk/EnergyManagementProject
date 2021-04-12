package com.mir.rest.clent;

import java.util.Date;

public class REST_MeterProfile {
	
	//emaID : CEMAID, deviceEMAID : DeviceID
	private String emaID, deviceID;
	private String DEVICETYPE;
	

	public REST_MeterProfile(String emaID, String deviceID) {

		this.setDeviceInfo(emaID, deviceID);
	}

	public String getEmaID() {
		return emaID;
	}

	public REST_MeterProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}


	public String getDeviceEMAID() {
		return deviceID;
	}

	public REST_MeterProfile setDeviceEMAID(String deviceID) {
		this.deviceID = deviceID;
		return this;
	}

	public void setDeviceInfo(String emaID, String deviceID) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceID);
	}
	
	//add
	public String getDEVICETYPE() {
		return DEVICETYPE;
	}

	public void setDEVICETYPE(String dEVICETYPE) {
		DEVICETYPE = dEVICETYPE;
	}
	
	
	public REST_MeterProfile(String emaID, String deviceID, String deviceType) {

		this.setDeviceInfo(emaID, deviceID,deviceType);
	}
	
	public void setDeviceInfo(String emaID, String deviceID,String deviceType) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceID);
		this.setDEVICETYPE(deviceType);
	}



}
