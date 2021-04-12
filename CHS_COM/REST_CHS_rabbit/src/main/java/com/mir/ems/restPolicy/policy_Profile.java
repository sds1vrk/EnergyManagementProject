package com.mir.ems.restPolicy;

import java.util.ArrayList;
import java.util.Date;

public class policy_Profile {
	//emaID : EMS, subID : myID
	private String emaID, subID;
	
	private ArrayList<String>sidelist=new ArrayList<>();
	
	

	public String getEmaID() {
		return emaID;
	}

	public void setEmaID(String emaID) {
		this.emaID = emaID;
	}

	public String getSubID() {
		return subID;
	}

	public void setSubID(String subID) {
		this.subID = subID;
	}

	public ArrayList<String> getSidelist() {
		return sidelist;
	}

	public void setSidelist(ArrayList<String> sidelist) {
		this.sidelist = sidelist;
	}
	
	public void addSidelist(String sidelist) {
		if(!this.sidelist.contains(sidelist)) {
			this.sidelist.add(sidelist);
		}
	}
	public void delSidelist(String sidelist) {
		
	}
	
	
	
	

}
