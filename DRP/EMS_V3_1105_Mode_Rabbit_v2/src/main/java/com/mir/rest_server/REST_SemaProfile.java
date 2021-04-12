package com.mir.rest_server;

import java.util.Date;

public class REST_SemaProfile {
	//emaID : EMS, emaID : myID
	private String emaID, subID;
	private String deviceType;

	public REST_SemaProfile(String emaID, String subID) {

		this.setDeviceInfo(emaID, subID);
	}

	public String getEmaID() {
		return emaID;
	}

	public REST_SemaProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}


	public String getsubID() {
		return subID;
	}

	public REST_SemaProfile setSubID(String subID) {
		this.subID = subID;
		return this;
	}

	public void setDeviceInfo(String emaID, String subID) {

		this.setEmaID(emaID);
		this.setSubID(subID);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	
	//add

	public REST_SemaProfile(String emaID, String subID,String deviceType) {

		this.setDeviceInfo(emaID, subID,deviceType);
	}
	
	
	public void setDeviceInfo(String emaID, String subID,String deviceType) {

		this.setEmaID(emaID);
		this.setSubID(subID);
		this.setDeviceType(deviceType);
	}
	
	
	

}
