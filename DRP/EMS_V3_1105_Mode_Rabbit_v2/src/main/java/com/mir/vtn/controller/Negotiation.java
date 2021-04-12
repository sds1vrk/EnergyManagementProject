package com.mir.vtn.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.globalVar.global;
import com.mir.vtn.DB.TimeFormat;
import com.mir.vtn.DB.cema_database;
import com.mir.vtn.global.Global;
import com.mir.vtn.profile.ReportDetail;
import com.mir.vtn.profile.openadr.OadrResponse;
import com.mir.vtn.profile.openadr.RegisteredReport;
import com.mir.vtn.profile.openadr.UpdatedReport;
import com.mir.vtn.profile.registered.RegisteredVen;
import com.mir.vtn.profile.*;

@Controller
@RequestMapping(value = "/OpenADR2/Simple/2.0b/Negotiation")

public class Negotiation {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> getFixedDepositList(@RequestBody byte[] payload) throws JSONException, SAXException, IOException, ParserConfigurationException, TransformerException {

//		RegisteredReport registeredReport = new RegisteredReport();
//		OadrResponse oadrResponse = new OadrResponse();
//		UpdatedReport updatedReport = new UpdatedReport();
		
		NegotiationProfile negotiationProfile=new NegotiationProfile();

		String responseBody = "";

		String requestBody = new String(payload);

		JSONObject json = new JSONObject(requestBody);

		String venID = json.getString("ID");
		double energy = json.getDouble("energy");
//		double current_threshold=json.getDouble("threshold");
		
	
		
		

//		System.out.println("ID:" + venID);
//		System.out.println("energy:" + energy);

		// Response : oadrResponse
//		responseBody = updatedReport.buildUp(venID, requestID);
		
		//Save Energy Negotiation value
		
		
		responseBody=negotiationProfile.buildUp(venID,energy);
		

//		responseBody = "NEgoTest";

		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
}
