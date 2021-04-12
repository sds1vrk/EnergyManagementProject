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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;
import com.mir.vtn.global.Global;
import com.mir.vtn.profile.openadr.DistributeEvent;
import com.mir.vtn.profile.openadr.OadrResponse;
import com.mir.vtn.profile.openadr.RegisterReport;

@Controller
@RequestMapping(value = "/OpenADR2/Simple/2.0b/OadrPoll")

public class OadrPoll {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> getFixedDepositList(@RequestBody byte[] payload)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		RegisterReport registerReport = new RegisterReport();
		OadrResponse oadrResponse = new OadrResponse();
		DistributeEvent distributeEvent = new DistributeEvent();

		String responseBody = "", requestID = "", venID = "";

		String requestBody = new String(payload);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(requestBody)));
		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		/*
		 * 
		 * GET VEN OadrPoll Flag =>
		 * 
		 * 0: Register, 1: Periodic, 2: Distribute Event, 3:Create Report
		 * 
		 * Check Classes -> EiRegisterParty, EiReport, FrontController -
		 * changeRegisteredVenSeqNum
		 *
		 *
		 */

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("venID")) {
				venID = node.getTextContent();
			}
		}

		if (Global.getRegisterVEN().get(venID).getSeqNum() == 0) {

			// Response : oadrRegisterReport
			responseBody = registerReport.buildUp(venID);

		} else if (Global.registerVEN.get(venID).getSeqNum() == 1) {

//			System.out.println("이제 제대로가야지!");
			// Response : oadrResponse
			responseBody = oadrResponse.buildUp(null, venID);

		} else if (Global.registerVEN.get(venID).getSeqNum() == 2) {
			
			
//			System.err.println("들어왓니?");
			// Response : DistributeEvent

			String strVenID = Global.registerVEN.get(venID).getStrVenID();
			String dtStart = Global.eventInfo.get(strVenID).getDtStart();
			int duration = Global.eventInfo.get(strVenID).getDuration();
			String marketContextID = Global.eventInfo.get(strVenID).getMarket_context_id();
			double payloadValue = Global.eventInfo.get(strVenID).getPayload_value();
			int priority = Global.eventInfo.get(strVenID).getPriority();
			String response_required_type_id = Global.eventInfo.get(strVenID).getResponse_required_type_id();
			String signal_name_id = Global.eventInfo.get(strVenID).getSignal_name_id();
			String signal_type_id = Global.eventInfo.get(strVenID).getSignal_type_id();
			String vtn_comment = Global.eventInfo.get(strVenID).getVtn_comment();
			boolean test_event = Global.eventInfo.get(strVenID).isTest_event();
			String eventID = global.eventID.get(global.autoDRCNT);
			responseBody = distributeEvent.buildUp(venID, dtStart, duration, marketContextID, payloadValue, priority,
					response_required_type_id, signal_name_id, signal_type_id, vtn_comment, test_event, eventID);
			
//			responseBody = distributeEvent.buildUp(venID, dtStart, duration, marketContextID, payloadValue, priority,
//					response_required_type_id, signal_name_id, signal_type_id, vtn_comment, test_event);

			// After then Set registeredVEN SeqNum to 1 in order to response Periodical Poll
//			Global.registerVEN.get(venID).setSeqNum(1);
			Global.eventInfo.get(strVenID).setEvent_flag(true);
//			global.autoDRCNT += 1;
//			System.out.println(global.autoDRCNT+"===>");
//			Global.registerVEN.get(venID).setSeqNum(1);
			

		} else if (Global.registerVEN.get(venID).getSeqNum() == 3) {
			// Response : CreateReport
		}

		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
}
