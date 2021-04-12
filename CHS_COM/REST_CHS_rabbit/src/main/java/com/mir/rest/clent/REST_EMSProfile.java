package com.mir.rest.clent;

import java.util.Date;

public class REST_EMSProfile {
	//emaID : MyID
	
	
	private String emaID,deviceType, deviceID;
	
	
	
	

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public REST_EMSProfile(String emaID) {

		this.setDeviceInfo(emaID);
	}

	public String getEmaID() {
		return emaID;
	}

	public REST_EMSProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}


	public void setDeviceInfo(String emaID) {

		this.setEmaID(emaID);
	}
	
	
	public REST_EMSProfile(String emaID, String deviceType) {

		this.setDeviceInfo(emaID);
		this.setDeviceType(deviceType);
	}
	
	
	public REST_EMSProfile(String emaID, String deviceType,String deviceID) {

		this.setDeviceInfo(emaID);
		this.setDeviceType(deviceType);
		this.setDeviceID(deviceID);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	

}
