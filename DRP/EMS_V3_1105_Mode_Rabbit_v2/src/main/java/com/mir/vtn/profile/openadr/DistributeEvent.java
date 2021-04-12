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

public class DistributeEvent {

	private String requestID, venID;

	// Registration Distribute Event
	public String buildUp(String requestID, String venID)
			throws SAXException, IOException, ParserConfigurationException, TransformerException {

		setRequestID(requestID);
		setVenID(venID);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("Event/DistributeEvent.xml"));

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

	// Publish Distribute Event

	public String buildUp(String venID, String dtStart, int duration, String marketContextID, double payloadValue,
			int priority, String reponse_required_type_id, String signal_name_id, String signal_type_id,
			String vtn_comment, boolean test_event)
			throws SAXException, IOException, ParserConfigurationException, TransformerException {

		setVenID(venID);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("Event/DistributeEvent_Publish.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeName().contains("venID")) {
				node.setTextContent(getVenID());
			}
			if (node.getNodeName().contains("marketContext")) {
				node.setTextContent(marketContextID);
			}
			if (node.getNodeName().contains("date-time")) {
				node.setTextContent(dtStart);
			}
			if (node.getNodeName().contains("signalName")) {
				node.setTextContent(signal_name_id);
			}
			if (node.getNodeName().contains("signalType")) {
				node.setTextContent(signal_type_id);
			}
			if (node.getNodeName().contains("value")) {
				node.setTextContent(payloadValue+"");
			}
			if (node.getNodeName().contains("oadrResponseRequired")) {
				node.setTextContent(reponse_required_type_id);
			}
			if (node.getNodeName().contains("testEvent")) {
				node.setTextContent(String.valueOf(test_event));
			}
			if (node.getNodeName().contains("priority")) {
				node.setTextContent(priority+"");
			}
			if (node.getNodeName().contains("duration")) {	
				if(node.getTextContent().equals("PT1M")) {
					node.setTextContent("PT"+duration+"M");
				}
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		
//		System.out.println(sw.toString());
		
		return sw.toString();

	}


	public String buildUp(String venID, String dtStart, int duration, String marketContextID, double payloadValue,
			int priority, String reponse_required_type_id, String signal_name_id, String signal_type_id,
			String vtn_comment, boolean test_event, String eventID)
			throws SAXException, IOException, ParserConfigurationException, TransformerException {

		setVenID(venID);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("Event/DistributeEvent_Publish.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeName().contains("eventID")) {
				node.setTextContent(eventID);
			}
			
			if (node.getNodeName().contains("venID")) {
				node.setTextContent(getVenID());
			}
			if (node.getNodeName().contains("marketContext")) {
				node.setTextContent(marketContextID);
			}
			if (node.getNodeName().contains("date-time")) {
				node.setTextContent(dtStart);
			}
			if (node.getNodeName().contains("signalName")) {
				node.setTextContent(signal_name_id);
			}
			if (node.getNodeName().contains("signalType")) {
				node.setTextContent(signal_type_id);
			}
			if (node.getNodeName().contains("value")) {
				node.setTextContent(payloadValue+"");
			}
			if (node.getNodeName().contains("oadrResponseRequired")) {
				node.setTextContent(reponse_required_type_id);
			}
			if (node.getNodeName().contains("testEvent")) {
				node.setTextContent(String.valueOf(test_event));
			}
			if (node.getNodeName().contains("priority")) {
				node.setTextContent(priority+"");
			}
			if (node.getNodeName().contains("duration")) {	
				if(node.getTextContent().equals("PT1M")) {
					node.setTextContent("PT"+duration+"M");
				}
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		
//		System.out.println(sw.toString());
		
		return sw.toString();

	}

	
	public String getRequestID() {
		return requestID;
	}

	public DistributeEvent setRequestID(String requestID) {
		this.requestID = requestID;
		return this;
	}

	public String getVenID() {
		return venID;
	}

	public DistributeEvent setVenID(String venID) {
		this.venID = venID;
		return this;
	}

}
