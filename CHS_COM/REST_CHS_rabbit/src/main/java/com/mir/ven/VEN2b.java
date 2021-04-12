package com.mir.ven;
/*
 * Copyright (c) 2018, Hyunjin Park, Mobile Intelligence and Routing Lab
 * All rights reserved
 * 
 * 2018.07.05(Thu)
 * Edited by Hyunjin Park
 * Hanyang University
 * 
 */
public interface VEN2b {

	public void QueryRegistration(String requestID);

	// profileType 클래스 하나 만들어서 oadrProfileType Object로 변경
	// transportType 클래스 하나 만들어서 oadrTransportType Object로 변경
	public void CreatePartyRegistration(String profileType, String transportType, String transportAddress,
			boolean reportOnly, boolean xmlSignature, boolean httpPullModel, String registrationID, String requestID);

	// response 클래스 하나 만들어서 responses Object로 변경, 이벤트에 참여할지 optIn, optOut 여부를 결정하는 클래스로 생성한다.
	public void CreatedEvent(String responseCode, String responseDescription, String reponses, String requestID);
	
	// UpdateReport와 공유할수 있는 oadrReport 인터페이스를 하나만들고 그걸 구현한뒤에 여기다가 넣는 형태로 
	public void RegisterReport(String oadrReport,String requestID);

	public void RegisteredReport(String requestID, String responseCode, String responseDescription);

	// dtStart와 createdDateTime은 Time혹은 Date Date Type으로 변경해주어야 한다.
	public void UpdateReport(String oadrReport, String dtStart, String reportRequestID, String createdDateTime, String requestID);

	// optSchedule 클래스 하나 만들어서 optSchedule Object로 변경
	public void CreateOptSchedule(String optSchedule, String requestID);

	public void CancelOptSchedule(String optID, String requestID);

	public void Poll();

}
