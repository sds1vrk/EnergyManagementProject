package com.mir.rest.clent;

import java.util.Date;

public class REST_DcuProfile {

	private String emaID, deviceEMAID,deviceType;

	public REST_DcuProfile(String emaID, String deviceEMAID) {

		this.setDeviceInfo(emaID, deviceEMAID);
	}

	public String getEmaID() {
		return emaID;
	}

	public REST_DcuProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}


	public String getDeviceEMAID() {
		return deviceEMAID;
	}

	public REST_DcuProfile setDeviceEMAID(String deviceEMAID) {
		this.deviceEMAID = deviceEMAID;
		return this;
	}

	public void setDeviceInfo(String emaID, String deviceEMAID) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceEMAID);
	}

	
	//add
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public void setDeviceInfo(String emaID, String deviceEMAID,String deviceType) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceEMAID);
		this.setDeviceType(deviceType);
	}

	
	public REST_DcuProfile(String emaID, String deviceEMAID,String deviceType) {

		this.setDeviceInfo(emaID, deviceEMAID,deviceType);
	}
	
	
	
	
	
	
	

}
