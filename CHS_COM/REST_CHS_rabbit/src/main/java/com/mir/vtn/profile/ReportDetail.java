package com.mir.vtn.profile;

public class ReportDetail {

	private String venID, rID, oadrDataQuality, time;
	private double value;
	private int confidence, accuracy;
	
	
	public ReportDetail() {
		
	}
	
	public ReportDetail(String venID, String rID, String oadrDataQuality, double value, int confidence, int accuracy, String time) {
	
		setVenID(venID);
		setrID(rID);
		setOadrDataQuality(oadrDataQuality);
		setValue(value);
		setConfidence(confidence);
		setAccuracy(accuracy);
		setTime(time);
	}
	
	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}



	public String getVenID() {
		return venID;
	}

	public void setVenID(String venID) {
		this.venID = venID;
	}

	public String getrID() {
		return rID;
	}
	public void setrID(String rID) {
		this.rID = rID;
	}
	public String getOadrDataQuality() {
		return oadrDataQuality;
	}
	public void setOadrDataQuality(String oadrDataQuality) {
		this.oadrDataQuality = oadrDataQuality;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getConfidence() {
		return confidence;
	}
	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public ReportDetail setAccuracy(int accuracy) {
		this.accuracy = accuracy;
		return this;
	}
	
	
	
}
