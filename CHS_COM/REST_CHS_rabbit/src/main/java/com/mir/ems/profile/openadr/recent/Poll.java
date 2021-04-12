package com.mir.ems.profile.openadr.recent;

import org.json.JSONException;
import org.json.JSONObject;

public class Poll {

	private String srcEMA, destEMA, service, time;

	@Override
	public String toString() {

		JSONObject json = new JSONObject();

		try {
			json.put("SrcEMA", getSrcEMA());
			json.put("DestEMA", getDestEMA());
			json.put("service", getService());
			json.put("time", getTime());

			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}

	}

	public String getSrcEMA() {
		return srcEMA;
	}

	public void setSrcEMA(String srcEMA) {
		this.srcEMA = srcEMA;
	}

	public String getDestEMA() {
		return destEMA;
	}

	public void setDestEMA(String destEMA) {
		this.destEMA = destEMA;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
