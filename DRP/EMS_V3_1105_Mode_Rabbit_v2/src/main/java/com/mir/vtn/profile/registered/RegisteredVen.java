package com.mir.vtn.profile.registered;

import java.util.Date;

public class RegisteredVen {
	
	private String reportRequestID, strVenID;
	private int seqNum;
	private double minValue=Integer.MAX_VALUE;
	private double maxValue=Integer.MIN_VALUE;
	private double avgValue;
	private double threshold;
	private double power;
	private double generate, storage;
	private String minTime, maxTime, ipADDR;
	private boolean pullModel;
	public RegisteredVen(String reportRequestID, int seqNum, String strVenID, String ipADDR, boolean pullModel) {
			
		setReportRequestID(reportRequestID);
		setSeqNum(seqNum);
		setStrVenID(strVenID);
		setIpADDR(ipADDR);
		setPullModel(pullModel);
	}

	
	
	public boolean isPullModel() {
		return pullModel;
	}



	public void setPullModel(boolean pullModel) {
		this.pullModel = pullModel;
	}



	public String getIpADDR() {
		return ipADDR;
	}



	public void setIpADDR(String ipADDR) {
		this.ipADDR = ipADDR;
	}



	public String getReportRequestID() {
		return reportRequestID;
	}

	public void setReportRequestID(String reportRequestID) {
		this.reportRequestID = reportRequestID;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public String getStrVenID() {
		return strVenID;
	}

	public void setStrVenID(String strVenID) {
		this.strVenID = strVenID;
	}

	@Override
	public String toString() {
		return "RegisteredVen [reportRequestID=" + reportRequestID + ", strVenID=" + strVenID + ", seqNum=" + seqNum
				+ "]";
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		
		if(this.minValue > minValue) {
			this.minValue = minValue;
			setMinTime(new Date(System.currentTimeMillis()).toString());
		}
		
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		
		if(this.maxValue < maxValue) {
			this.maxValue = maxValue;
			setMaxTime(new Date(System.currentTimeMillis()).toString());
		}
		
	}

	public double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(double avgValue) {
		this.avgValue = avgValue;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public double getGenerate() {
		return generate;
	}

	public void setGenerate(double generate) {
		this.generate = generate;
	}

	public double getStorage() {
		return storage;
	}

	public void setStorage(double storage) {
		this.storage = storage;
	}
	
	
	
	
	
}
