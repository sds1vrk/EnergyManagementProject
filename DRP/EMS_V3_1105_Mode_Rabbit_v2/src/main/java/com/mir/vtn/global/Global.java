package com.mir.vtn.global;

import java.util.concurrent.ConcurrentHashMap;

import com.mir.pushEventListener.EventInitiater;
import com.mir.vtn.DB.DatabaseConnection;
import com.mir.vtn.DB.cema_database;
import com.mir.vtn.profile.EventDetail;
import com.mir.vtn.profile.ReportDetail;
import com.mir.vtn.profile.registered.RegisteredVen;
import com.mir.vtn.profile.registered.venIpInfo;

public class Global {

	// DATABASE
	public static int REPORTSEQ_CEMA = 0;
	public static int REPORTCNT_CEMA = 0;
	public static int REPORTSEQ = 0;
	public static int REPORTCNT = 0;
	public static ConcurrentHashMap<Integer, cema_database> saveCEMA = new ConcurrentHashMap<Integer, cema_database>();
	public static DatabaseConnection databaseConnection = new DatabaseConnection();
	
	//PUSH
	public static EventInitiater initiater = new EventInitiater();
	public static ConcurrentHashMap<String, venIpInfo> pushRegisterVEN = new ConcurrentHashMap<>();

	
	
	// VTN & HTTP/XML
	public static int DATANUM = 0;
	public static int DATACNT = 0;
	public static ConcurrentHashMap<String, EventDetail> eventInfo = new ConcurrentHashMap<String, EventDetail>();
	
	
	public static ConcurrentHashMap<String, RegisteredVen> registerVEN = new ConcurrentHashMap<String, RegisteredVen>();

	public static ConcurrentHashMap<String, RegisteredVen> getRegisterVEN() {
		return registerVEN;
	}
	
	public static ConcurrentHashMap<Integer, ReportDetail> saveReport = new ConcurrentHashMap<Integer, ReportDetail>();
	public static ConcurrentHashMap<String, ReportDetail> devReport = new ConcurrentHashMap<String, ReportDetail>();

	
	// EMAP & MQTT / JSON
	public static String qosType = "Non-controllable";
	public static final int interval = 2000;
	public static String parentnNodeID = "SERVER_EMS1";
	public static String brokerIP = "166.104.28.51";
	public static String profileType = "EMAP";
	public static String profile = "EMAP1.0b";
	public static String communicationModel= "Pull";
	public static String reportType = "Explicit";
	public static String version = "1.0b";
	public static String coapServerIP = "localhost";
	public static String coapServerPort = "5683";
	
//	public static ConcurrentHashMap<String, DeviceProfile> devProfile = new ConcurrentHashMap<String, DeviceProfile>();

	
	
	
	
	public static String getProfileType() {
		return profileType;
	}
	public static void setProfileType(String profileType) {
		Global.profileType = profileType;
	}
	public static String getParentnNodeID() {
		return parentnNodeID;
	}
	public static void setParentnNodeID(String parentnNodeID) {
		Global.parentnNodeID = parentnNodeID;
	}
	public static String getBrokerIP() {
		return brokerIP;
	}
	public static void setBrokerIP(String brokerIP) {
		Global.brokerIP = brokerIP;
	}
	
	
}
