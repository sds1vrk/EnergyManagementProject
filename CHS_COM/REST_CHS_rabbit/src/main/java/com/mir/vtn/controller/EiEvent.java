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

import com.mir.vtn.global.Global;
import com.mir.vtn.profile.openadr.DistributeEvent;
import com.mir.vtn.profile.openadr.OadrResponse;

@Controller
@RequestMapping(value = "/OpenADR2/Simple/2.0b/EiEvent")

public class EiEvent {


	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> getFixedDepositList(@RequestBody byte[] payload)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		String requestID="", venID="", responseBody="";
		DistributeEvent distributeEvent = new DistributeEvent();
		OadrResponse oadrResponse = new OadrResponse();
		
		String requestBody = new String(payload);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(requestBody)));
		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		if (requestBody.contains("oadrRequestEvent")) {

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().contains("requestID"))
					requestID = node.getTextContent();
				if (node.getNodeName().contains("venID"))
					venID = node.getTextContent();
			}

			// Response: oadrCreatedPartyRegistration
			responseBody = distributeEvent.buildUp(requestID, venID);

			// Response : oadrDistributeEvent

		} else if (requestBody.contains("oadrCreatedEvent")) {

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().contains("requestID"))
					requestID = node.getTextContent();
				if (node.getNodeName().contains("venID"))
					venID = node.getTextContent();
			}
			
			responseBody = oadrResponse.buildUp(null, venID);

			Global.registerVEN.get(venID).setSeqNum(1);
			
			
			// Response : oadrResponse

		}

		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}

}
