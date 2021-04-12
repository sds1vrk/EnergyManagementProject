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

import com.mir.vtn.profile.openadr.CanceledOpt;
import com.mir.vtn.profile.openadr.CreatedOpt;

@Controller
@RequestMapping(value = "/OpenADR2/Simple/2.0b/EiOpt")
public class EiOpt {

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> optHandler(@RequestBody byte[] payload) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		String optID = "", requestID="", venID="", responseBody="";

		String requestBody = new String(payload);

//		System.out.println(requestBody);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(requestBody)));
		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("requestID"))
				requestID = node.getTextContent();
			if (node.getNodeName().contains("venID"))
				venID = node.getTextContent();
			if (node.getNodeName().contains("optID"))
				optID = node.getTextContent();
			
		}
		
		// Need to store OPT Information (Incomplete, Future Work) 
		
		if (requestBody.contains("oadrCreateOpt")) {



			// Response: oadrCreatedOpt
			responseBody = new CreatedOpt().buildUp(requestID, venID, optID);

		} 
		
		// Need to store OPT Information (Incomplete, Future Work) 

		else if (requestBody.contains("oadrCancelOpt")) {
			responseBody = new CanceledOpt().buildUp(requestID, venID, optID);

		}
		
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);

	}
}
