package com.mir.rest_server;

import java.util.Date;

public class REST_EMSProfile {
	//emaID : MyID
	private String emaID;
	private String deviceType;
	private String rest_ems;
	
	

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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	
	//add

	public REST_EMSProfile(String emaID,String deviceType) {

		this.setDeviceInfo(emaID,deviceType);
	}



	public void setDeviceInfo(String emaID,String deviceType) {
		this.setDeviceType(deviceType);
		this.setEmaID(emaID);
	}

	
	
	public String getRest_ems() {
		return rest_ems;
	}

	public void setRest_ems(String rest_ems) {
		this.rest_ems = rest_ems;
	}

	//add2
	public REST_EMSProfile(String emaID,String deviceType,String rest_ems) {

		this.setDeviceInfo(emaID,deviceType,rest_ems);
	}



	public void setDeviceInfo(String emaID,String deviceType,String rest_ems) {
		this.setDeviceType(deviceType);
		this.setEmaID(emaID);
		this.setRest_ems(rest_ems);
	}
	
	

}
