package com.mir.rest.clent;

import java.util.Date;

public class REST_CemaProfile {

	private String emaID, deviceEMAID,deviceType;
	
	private double maxValue, minValue, threshold;
	
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

	public REST_CemaProfile(String emaID, String deviceEMAID,String deviceType) {
		
		this.setDeviceInfo(emaID, deviceEMAID,deviceType);
	}


	
	
	//add2
	
	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public void setDeviceInfo(String emaID, String deviceEMAID,String deviceType,double maxValue, double minValue, double threshold) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceEMAID);
		this.setDeviceType(deviceType);
		this.setMaxValue(maxValue);
		this.setMinValue(minValue);
		this.setThreshold(threshold);
	}

	public REST_CemaProfile(String emaID, String deviceEMAID,String deviceType, double maxValue, double minValue, double threshold) {
		
		this.setDeviceInfo(emaID, deviceEMAID,deviceType,maxValue,minValue,threshold);
	}
	
	

}
