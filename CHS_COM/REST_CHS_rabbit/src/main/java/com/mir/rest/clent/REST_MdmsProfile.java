package com.mir.rest.clent;

import java.util.Date;

public class REST_MdmsProfile {
	//emaID : EMS, subID : myID
	private String emaID, subID;
	
	private String deviceType;
	private Double threshold;
	
	
	
	

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public REST_MdmsProfile(String emaID, String subID) {

		this.setDeviceInfo(emaID, subID);
	}

	public String getEmaID() {
		return emaID;
	}

	public REST_MdmsProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}


	public String getsubID() {
		return subID;
	}

	public REST_MdmsProfile setSubID(String subID) {
		this.subID = subID;
		return this;
	}

	public void setDeviceInfo(String emaID, String subID) {

		this.setEmaID(emaID);
		this.setSubID(subID);
	}

	
	
	//add
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public REST_MdmsProfile(String emaID, String subID,String deviceType) {

		this.setDeviceInfo(emaID, subID,deviceType);
	}
	
	
	public void setDeviceInfo(String emaID, String subID,String deviceType) {

		this.setEmaID(emaID);
		this.setSubID(subID);
		this.setDeviceType(deviceType);
	}

	
	
	
	
	
	

}
