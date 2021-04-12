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

import org.springframework.util.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CreatedPartyRegistration {

	String requestID, venID;

	public CreatedPartyRegistration() {

	}
	
	public CreatedPartyRegistration setRequestID(String requestID) {
		this.requestID = requestID;
		return this;
	}

	public CreatedPartyRegistration setVenID(String venID) {
		this.venID = venID;
		return this;
	}

	
	//Response for VTN Info
	public String buildUp(String requestID)
			throws SAXException, IOException, ParserConfigurationException, TransformerException {

		setRequestID(requestID);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("RegisterParty/CreatedPartyRegistration_VtnInfo.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("requestID")) {
				node.setTextContent(getRequestID());
				break;
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();
	}

	
	//Response for Ven Register
	public String buildUp(String requestID, String venID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		setRequestID(requestID);
		setVenID(DigestUtils.md5DigestAsHex(venID.getBytes()).toString());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("RegisterParty/CreatedPartyRegistration_VenRegister.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("requestID")) {
				node.setTextContent(getRequestID());
			}
			if (node.getNodeName().contains("venID")) {
				node.setTextContent(getVenID());
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();

	}

	
	/*
	 * 
	 * 
	 * GETTER && SETTER
	 * 
	 * 
	 */
	

	public String getVenID() {
		return venID;
	}
	
	public String getRequestID() {
		return requestID;
	}

}
