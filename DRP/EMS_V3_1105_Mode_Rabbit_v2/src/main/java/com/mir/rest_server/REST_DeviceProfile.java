package com.mir.rest_server;

import java.util.Date;

public class REST_DeviceProfile {
	
	//emaID : CEMAID, deviceEMAID : DeviceID
	private String emaID, deviceID, deviceType;

	public REST_DeviceProfile(String emaID, String deviceID) {

		this.setDeviceInfo(emaID, deviceID);
	}

	public String getEmaID() {
		return emaID;
	}

	public REST_DeviceProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}


	public String getDeviceEMAID() {
		return deviceID;
	}

	public REST_DeviceProfile setDeviceEMAID(String deviceID) {
		this.deviceID = deviceID;
		return this;
	}

	public void setDeviceInfo(String emaID, String deviceID) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceID);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	//add
	

	public REST_DeviceProfile(String emaID, String deviceID,String deviceType) {

		this.setDeviceInfo(emaID, deviceID,deviceType);
	}
	
	

	public void setDeviceInfo(String emaID, String deviceID,String deviceType) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceID);
		this.setDeviceType(deviceType);
	}


	
	
}
