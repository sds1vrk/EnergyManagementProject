package com.mir.vtn.profile.openadr;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CanceledPartyRegistration {


	String requestID, venID, registrationID;
	
	public CanceledPartyRegistration() {

	}
	
	
	//Response for Ven CanceledRegister
	public String buildUp(String requestID, String venID, String registrationID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		setRequestID(requestID).setVenID(venID).setRegistrationID(registrationID);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("RegisterParty/CanceledPartyRegistration.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("requestID")) {
				node.setTextContent(getRequestID());
			}
			if (node.getNodeName().contains("venID")) {
				node.setTextContent(getVenID());
			}
			if (node.getNodeName().contains("registrationID")) {
				node.setTextContent(getRegistrationID());
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();

	}


	
	public String getRegistrationID() {
		return registrationID;
	}
	
	public CanceledPartyRegistration setRegistrationID(String reigistrationID) {
		this.registrationID = reigistrationID;
		return this;
	}

	public CanceledPartyRegistration setRequestID(String requestID) {
		this.requestID = requestID;
		return this;
	}

	public CanceledPartyRegistration setVenID(String venID) {
		this.venID = venID;
		return this;
	}
	
	public String getVenID() {
		return venID;
	}
	
	public String getRequestID() {
		return requestID;
	}
	
	
}
