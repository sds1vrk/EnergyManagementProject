package com.mir.rest.clent;

import java.util.Date;

public class REST_SemaProfile {
	//emaID : EMS, subID : myID
	private String emaID, subID;
	private Double threshold,maxValue, minValue;
	
	private int emaCNT;
	
	
	
	private String deviceType;
	
	
	
	
	
	

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

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
	
	public void setDeviceInfo(String emaID, String subID,String deviceType) {

		this.setEmaID(emaID);
		this.setSubID(subID);
		this.setDeviceType(deviceType);
	}
	
	
	//add
	public REST_SemaProfile(String emaID, String subID,String deviceType) {

		this.setDeviceInfo(emaID, subID,deviceType);
	}


	
	
	//add2
	
	public void setDeviceInfo(String emaID, String subID,String deviceType, double maxValue, double minValue, double threshold, int emaCNT) {

		this.setEmaID(emaID);
		this.setSubID(subID);
		this.setDeviceType(deviceType);
		this.setMaxValue(maxValue);
		this.setMinValue(minValue);
		this.setEmaCNT(emaCNT);
	}
	
	public REST_SemaProfile(String emaID, String subID,String deviceType,double maxValue, double minValue, double threshold, int emaCNT) {

		this.setDeviceInfo(emaID, subID,deviceType,maxValue,minValue,threshold,emaCNT);
		
		
	}
	
	
	
	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public int getEmaCNT() {
		return emaCNT;
	}

	public void setEmaCNT(int emaCNT) {
		this.emaCNT = emaCNT;
	}

	public String getSubID() {
		return subID;
	}
	
	
	

}
