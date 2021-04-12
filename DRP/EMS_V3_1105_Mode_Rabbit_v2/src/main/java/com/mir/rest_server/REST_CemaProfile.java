package com.mir.rest_server;

import java.util.Date;

public class REST_CemaProfile {

	private String emaID, deviceEMAID, deviceType;

	public REST_CemaProfile(String emaID, String deviceEMAID) {

		this.setDeviceInfo(emaID, deviceEMAID);
	}

	public String getEmaID() {
		return emaID;
	}

	public REST_CemaProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}


	public String getDeviceEMAID() {
		return deviceEMAID;
	}

	public REST_CemaProfile setDeviceEMAID(String deviceEMAID) {
		this.deviceEMAID = deviceEMAID;
		return this;
	}

	public void setDeviceInfo(String emaID, String deviceEMAID) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceEMAID);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	//add
	public REST_CemaProfile(String emaID, String deviceEMAID,String deviceType) {

		this.setDeviceInfo(emaID, deviceEMAID,deviceType);
	}
	
	
	
	public void setDeviceInfo(String emaID, String deviceEMAID,String deviceType) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceEMAID);
		this.setDeviceType(deviceType);
	}
	
	
	
	

}
