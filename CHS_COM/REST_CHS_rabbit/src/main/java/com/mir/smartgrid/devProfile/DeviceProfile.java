package com.mir.smartgrid.devProfile;

import java.util.Date;

public class DeviceProfile {

	private String emaID, deviceEMAID, deviceType, state;
	private int dimming, mode, priority;
	private double power, capacity, volt, hz, chargedEnergy, soc;
	private Date date;

	public DeviceProfile(String emaID, String deviceEMAID, String deviceType, String state, int dimming, int mode,
			int priority, double power, double capacity, double volt, double hz, double chargedEnergy, double soc,
			Date date) {

		this.setDeviceInfo(emaID, deviceEMAID, deviceType, state, dimming, mode, priority, power, capacity, volt, hz,
				chargedEnergy, soc, date);
	}

	public String getEmaID() {
		return emaID;
	}

	public DeviceProfile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}

	public double getSoc() {
		return soc;
	}

	public DeviceProfile setSoc(double soc) {
		this.soc = soc;
		return this;
	}

	public String getDeviceEMAID() {
		return deviceEMAID;
	}

	public DeviceProfile setDeviceEMAID(String deviceEMAID) {
		this.deviceEMAID = deviceEMAID;
		return this;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public DeviceProfile setDeviceType(String deviceType) {
		this.deviceType = deviceType;
		return this;
	}

	public String getState() {
		return state;
	}

	public DeviceProfile setState(String state) {
		this.state = state;
		return this;
	}

	public int getDimming() {
		return dimming;
	}

	public DeviceProfile setDimming(int dimming) {
		this.dimming = dimming;
		return this;
	}

	public int getMode() {
		return mode;
	}

	public DeviceProfile setMode(int mode) {
		this.mode = mode;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public DeviceProfile setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public double getPower() {
		return power;
	}

	public DeviceProfile setPower(double power) {
		this.power = power;
		return this;
	}

	public double getCapacity() {
		return capacity;
	}

	public DeviceProfile setCapacity(double capacity) {
		this.capacity = capacity;
		return this;
	}

	public double getVolt() {
		return volt;
	}

	public DeviceProfile setVolt(double volt) {
		this.volt = volt;
		return this;
	}

	public double getHz() {
		return hz;
	}

	public DeviceProfile setHz(double hz) {
		this.hz = hz;
		return this;
	}

	public double getChargedEnergy() {
		return chargedEnergy;
	}

	public DeviceProfile setChargedEnergy(double chargedEnergy) {
		this.chargedEnergy = chargedEnergy;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public DeviceProfile setDate(Date date) {
		this.date = date;
		return this;
	}

	public void setDeviceInfo(String emaID, String deviceEMAID, String deviceType, String state, int dimming, int mode,
			int priority, double power, double capacity, double volt, double hz, double chargedEnergy, double soc,
			Date date) {

		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceEMAID);
		this.setDeviceType(deviceType);
		this.setState(state);
		this.setDimming(dimming);
		this.setMode(mode);
		this.setPriority(priority);
		this.setPower(power);
		this.setCapacity(capacity);
		this.setVolt(volt);
		this.setHz(hz);
		this.setChargedEnergy(chargedEnergy);
		this.setSoc(soc);
		this.setDate(date);

	}

}
