package com.mir.vtn.profile.registered;

@SuppressWarnings("restriction")
public class venIpInfo {

	private String ipADDR, venID, hashedVenID;
	private com.sun.net.httpserver.HttpExchange ex;

	public venIpInfo(String ipADDR, String venID, com.sun.net.httpserver.HttpExchange he, String hashedVenID) {
		setEx(he);
		setIpADDR(ipADDR);
		setVenID(venID);
		setHashedVenID(hashedVenID);
	}

	public String getHashedVenID() {
		return hashedVenID;
	}

	public void setHashedVenID(String hashedVenID) {
		this.hashedVenID = hashedVenID;
	}

	public String getIpADDR() {
		return ipADDR;
	}

	public void setIpADDR(String ipADDR) {
		this.ipADDR = ipADDR;
	}

	public String getVenID() {
		return venID;
	}

	public void setVenID(String venID) {
		this.venID = venID;
	}

	public com.sun.net.httpserver.HttpExchange getEx() {
		return ex;
	}

	public void setEx(com.sun.net.httpserver.HttpExchange ex) {
		this.ex = ex;
	}

}
