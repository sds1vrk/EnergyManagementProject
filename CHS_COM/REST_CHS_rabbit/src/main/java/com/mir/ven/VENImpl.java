package com.mir.ven;

public class VENImpl implements VEN2b{

	
	
	@Override
	public void QueryRegistration(String requestID){

		
		
	}

	@Override
	public void CreatePartyRegistration(String profileType, String transportType, String transportAddress,
			boolean reportOnly, boolean xmlSignature, boolean httpPullModel, String registrationID, String requestID){
		
	}

	@Override
	public void CreatedEvent(String responseCode, String responseDescription, String reponses, String requestID){
		
	}
	
	@Override
	public void RegisterReport(String oadrReport,String requestID){
		
	}

	@Override
	public void RegisteredReport(String requestID, String responseCode, String responseDescription){
		
	}

	@Override
	public void UpdateReport(String oadrReport, String dtStart, String reportRequestID, String createdDateTime, String requestID){
		
	}

	@Override
	public void CreateOptSchedule(String optSchedule, String requestID){
		
	}
	
	@Override
	public void CancelOptSchedule(String optID, String requestID){
		
	}

	@Override
	public void Poll(){
		
	}

	
}
