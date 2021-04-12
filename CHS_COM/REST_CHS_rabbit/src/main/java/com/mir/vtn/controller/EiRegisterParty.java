package com.mir.vtn.controller;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.rest.clent.REST_CLIENT_GET;
import com.mir.vtn.global.Global;
import com.mir.vtn.profile.openadr.CanceledPartyRegistration;
import com.mir.vtn.profile.openadr.CreatedPartyRegistration;
import com.mir.vtn.profile.registered.RegisteredVen;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;

@Controller
@RequestMapping(value = "/OpenADR2/Simple/2.0b/EiRegisterParty")

public class EiRegisterParty {
	private static Logger logger = Logger.getLogger(EiRegisterParty.class);

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> eiRegisterParty(@RequestBody byte[] payload, HttpServletRequest request)
			throws Exception {

		String ipADDR = request.getRemoteAddr();
		
		//지워야함 시작
		
//		String requestBody = new String(payload);
//
//		String venID = requestBody.split("/")[0];
//		String hashVENID = DigestUtils.md5DigestAsHex(venID.getBytes()).toString();
//		boolean pullModel = Boolean.parseBoolean(requestBody.split("/")[1]);		
//		
//		Global.registerVEN.put(hashVENID, new RegisteredVen("", 0, venID, ipADDR, pullModel));
//		
//		return new ResponseEntity<Object>("HI DONGSUNG", HttpStatus.OK);

		//지워야함 끝
		
		
		//기존 코드 시작
		CreatedPartyRegistration createdPartyRegistration = new CreatedPartyRegistration();
		String responseBody = "", requestID = "", venID = "", registrationID="";
		boolean pullModel=false;
		// need to modify
		logger.warn("eiRegisterParty");

		String requestBody = new String(payload);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(requestBody)));
		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

//		System.out.println(requestBody);
		
		if (requestBody.contains("oadrQueryRegistration")) {
//			System.out.println("DURL");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().contains("requestID")) {
					requestID = node.getTextContent();
					break;
				}
			}

			// Response: oadrCreatedPartyRegistration
			responseBody = createdPartyRegistration.buildUp(requestID);

		} else if (requestBody.contains("oadrCreatePartyRegistration")) {

			
//			System.out.println("D");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeName().contains("requestID")) {

					requestID = node.getTextContent();
				}
				if (node.getNodeName().contains("oadrVenName")) {
					venID = node.getTextContent();
				}
				if (node.getNodeName().contains("oadrHttpPullModel")) {
					pullModel = Boolean.parseBoolean(node.getTextContent());
				}
			}

			// Response: oadrCreatedPartyRegistration
			responseBody = createdPartyRegistration.buildUp(requestID, venID);

			/*
			 * 
			 * VEN ID Register Set SeqNum ==> 0
			 *
			 * 0: Register, 1: Periodic, 2: Distribute Event, 3:Create Report
			 * 
			 */
			Global.registerVEN.put(createdPartyRegistration.getVenID(), new RegisteredVen("", 0, venID, ipADDR, pullModel));

			
			System.out.println("등록");
			System.out.println(Global.registerVEN.get(createdPartyRegistration.getVenID()).getStrVenID());
			
			
			boolean realTimetableChanged = !pullModel && global.realTimeTableHasChanged ? true
					: false;
			
			Emap_Cema_Profile profile = new Emap_Cema_Profile("HTTP", venID, registrationID, null, null, 0,
					0, 0, 0, 0, 0, 0, 0, null, null, 0, pullModel, global.tableHasChanged,
					realTimetableChanged, "CONNECT");

			global.emaProtocolCoAP.put(venID, profile);
			
			
			
		}

		else if (requestBody.contains("oadrCanceledPartyRegistration")) {

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeName().contains("requestID")) {

					requestID = node.getTextContent();
				}
				if (node.getNodeName().contains("venID")) {
					venID = node.getTextContent();
				}
				if (node.getNodeName().contains("registrationID")) {
					registrationID = node.getTextContent();
				}
				

			}

			// Response: oadrCreatedPartyRegistration
			responseBody = new CanceledPartyRegistration().buildUp(requestID, venID, registrationID);

			// HASH REMOVE
			//			Global.registerVEN.put(createdPartyRegistration.getVenID(), new RegisteredVen("", 0, venID));

		}

		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);

		//기존 코드 끝
	}

}
