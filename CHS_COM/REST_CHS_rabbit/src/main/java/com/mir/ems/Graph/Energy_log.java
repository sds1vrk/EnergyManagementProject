package com.mir.ems.Graph;

public class Energy_log {
	
	
	public String method;
	public long currentTime;
	public int totalEnergy;
	public String color;
	
	public Energy_log(String method, long currentTime, int totalEnergy, String color) {
		setColor(color);
		setCurrentTime(currentTime);
		setMethod(method);
		setTotalEnergy(totalEnergy);
		
	}
	
	@Override
	public String toString() {
		return "{\"method\":" + method + ","
				+ "\"currentTime\":" + currentTime + ","
				+ "\"totalEnergy\":" + totalEnergy + ","
				+ "\"color\":" + color + ""
				+ "}";
	}
	
	
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public long getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	public int getTotalEnergy() {
		return totalEnergy;
	}
	public void setTotalEnergy(int totalEnergy) {
		this.totalEnergy = totalEnergy;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	

}
