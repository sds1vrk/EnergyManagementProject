package com.mir.vtn.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

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

@Controller
@RequestMapping(value = "/OpenADR2/Simple/2.0b/EiReport")

public class EiReport {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> getFixedDepositList(@RequestBody byte[] payload)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		RegisteredReport registeredReport = new RegisteredReport();
		OadrResponse oadrResponse = new OadrResponse();
		UpdatedReport updatedReport = new UpdatedReport();

		String responseBody = "", requestID = "", venID = "";

		String requestBody = new String(payload);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(requestBody)));
		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		if (requestBody.contains("oadrRegisterReport")) {

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().contains("requestID"))
					requestID = node.getTextContent();
				if (node.getNodeName().contains("venID"))
					venID = node.getTextContent();

			}

			// Response : oadrRegisteredReport
			responseBody = registeredReport.buildUp(requestID, venID);

			String strVenID = Global.getRegisterVEN().get(venID).getStrVenID();
			String ipADDR = Global.getRegisterVEN().get(venID).getIpADDR();
			boolean pullModel = Global.getRegisterVEN().get(venID).isPullModel();
			Global.getRegisterVEN().replace(venID,
					new RegisteredVen(registeredReport.getRequestID(), 0, strVenID, ipADDR, pullModel));

		}

		else if (requestBody.contains("oadrRegisteredReport")) {

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().contains("requestID"))
					requestID = node.getTextContent();
				if (node.getNodeName().contains("venID"))
					venID = node.getTextContent();
			}

			// Response : oadrResponse
			responseBody = oadrResponse.buildUp(requestID, venID);

			/*
			 * 
			 * VEN ID Register Set SeqNum ==> 1
			 *
			 * 0: Register, 1: Periodic, 2: Distribute Event, 3:Create Report
			 * 
			 */

			String strVenID = Global.getRegisterVEN().get(venID).getStrVenID();
			String ipADDR = Global.getRegisterVEN().get(venID).getIpADDR();
			boolean pullModel = Global.getRegisterVEN().get(venID).isPullModel();

			Global.getRegisterVEN().replace(venID,
					new RegisteredVen(registeredReport.getRequestID(), 1, strVenID, ipADDR, pullModel));

		}

		else if (requestBody.contains("oadrUpdateReport")) {
			String dtStart = null;
			double power = 0, generate = 0, storage = 0;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeName().contains("date-time")) {
					dtStart = node.getTextContent();
				}
				if (node.getNodeName().contains("venID")) {
					venID = node.getTextContent();
				}
				if (node.getNodeName().contains("requestID")) {
					requestID = node.getTextContent();
				}
			}

			for (int i = 0; i < nodes.getLength(); i++) { 

				Node node = nodes.item(i);

				if (node.getNodeName().contains("oadrReportPayload")) {

					NodeList reportPayload = node.getChildNodes();

					String rID = null, oadrDataQuality = null;
					int confidence = 0, accuracy = 0;
					double payloadFloat = 0;

					for (int j = 0; j < reportPayload.getLength(); j++) {
						if (!reportPayload.item(j).getNodeName().contains("#text")) {
							if (reportPayload.item(j).getNodeName().contains("payloadFloat")) {
								payloadFloat = Double
										.parseDouble(reportPayload.item(j).getChildNodes().item(0).getTextContent());
							} else if (reportPayload.item(j).getNodeName().contains("rID")) {
								rID = reportPayload.item(j).getTextContent();
							} else if (reportPayload.item(j).getNodeName().contains("confidence")) {
								confidence = Integer.parseInt(reportPayload.item(j).getTextContent());
							} else if (reportPayload.item(j).getNodeName().contains("accuracy")) {
								accuracy = Integer.parseInt(reportPayload.item(j).getTextContent());
							} else if (reportPayload.item(j).getNodeName().contains("oadrDataQuality")) {
								oadrDataQuality = reportPayload.item(j).getTextContent();
							}
						}
					}
					
					String emaID = Global.registerVEN.get(venID).getStrVenID();
					
					if(rID.contains("SERVER")) {
						
//						System.out.println("SERVER_EMA");
						
						global.emaProtocolCoAP.get(emaID).setPower(payloadFloat);
						global.emaProtocolCoAP.get(emaID).setqOs("Controllable");
						Global.registerVEN.get(venID).setGenerate(generate);
						Global.registerVEN.get(venID).setStorage(storage);
						Global.registerVEN.get(venID).setPower(power);
						Global.registerVEN.get(venID).setMinValue(power);
						Global.registerVEN.get(venID).setMaxValue(power);

					}else if(rID.contains("CLIENT")) {

//						System.out.println("CLIENT_EMA");
						
						if (!global.emaProtocolCoAP_Device.containsKey(rID)) {
							
							Emap_Device_Profile deviceProfile = new Emap_Device_Profile(emaID, rID,
									"CEMA", "Controllable", "ON", payloadFloat, -9, 0, 0,
									storage, 0, 0, 0, new Date(System.currentTimeMillis()).toString(), new Date(System.currentTimeMillis()).toString(), -9);
							
							global.emaProtocolCoAP_Device.put(rID, deviceProfile);
							
							
//							System.out.println(global.emaProtocolCoAP_Device.keySet().toString());
							
							Global.devReport.put(rID,
									new ReportDetail(venID, rID, oadrDataQuality, payloadFloat, confidence, accuracy, dtStart));

							
						} else {
							
							global.emaProtocolCoAP_Device.get(rID).setPower(payloadFloat);
							Global.devReport.get(rID).setAccuracy(accuracy).setValue(payloadFloat);

						}

					}

//					System.out.println("venID: " + venID + ", \trID: " + rID + "==>" + payloadFloat);

				}

			}

//			for (int i = 0; i < nodes.getLength(); i++) {
//
//				Node node = nodes.item(i);
//
//				if (node.getNodeName().contains("oadrReportPayload")) {
//
//					NodeList reportPayload = node.getChildNodes();
//
//					String rID = null, oadrDataQuality = null;
//					int confidence = 0, accuracy = 0;
//					double payloadFloat = 0;
//
//					for (int j = 0; j < reportPayload.getLength(); j++) {
//						if (!reportPayload.item(j).getNodeName().contains("#text")) {
//							// System.out.println(reportPayload.item(j).getNodeName());
//							if (reportPayload.item(j).getNodeName().contains("payloadFloat")) {
//								payloadFloat = Double
//										.parseDouble(reportPayload.item(j).getChildNodes().item(1).getTextContent());
//							} else if (reportPayload.item(j).getNodeName().contains("rID")) {
//								rID = reportPayload.item(j).getTextContent();
//							} else if (reportPayload.item(j).getNodeName().contains("confidence")) {
//								confidence = Integer.parseInt(reportPayload.item(j).getTextContent());
//							} else if (reportPayload.item(j).getNodeName().contains("accuracy")) {
//								accuracy = Integer.parseInt(reportPayload.item(j).getTextContent());
//							} else if (reportPayload.item(j).getNodeName().contains("oadrDataQuality")) {
//								oadrDataQuality = reportPayload.item(j).getTextContent();
//							}
//
//						}
//					}
//
//					Global.devReport.put(venID + " " + rID,
//							new ReportDetail(venID, rID, oadrDataQuality, payloadFloat, confidence, accuracy, dtStart));
//
//					if (rID.contains("RECLOSER")) {
//
//					} else if (rID.contains("SOLAR")) {
//						generate += payloadFloat;
//					} else if (rID.contains("BATTERY")) {
//						storage += payloadFloat;
//					} else if (rID.contains("RESOURCE") || rID.contains("LED")) {
//						power += payloadFloat;
//					}
//
//					String time = new TimeFormat().getCurrentTime();
//
//					// System.out.println(rID+"ENERGY::::::::::::"+payloadFloat);
//
//					cema_database cd = new cema_database();
//					cd.buildup(rID, "qos", "state", payloadFloat, -1, -1, -1, -1, -1, -1, -1, time, time, -1);
//
//					// Global.saveReport.put(++Global.DATANUM,
//					// new ReportDetail(venID, rID, oadrDataQuality, payloadFloat, confidence,
//					// accuracy, dtStart));
//					// ++Global.DATACNT;
//				}
//
//			}


			// Response : oadrResponse
			responseBody = updatedReport.buildUp(venID, requestID);

			/*
			 * 
			 * VEN ID Register Set SeqNum ==> 1
			 *
			 * 0: Register, 1: Periodic, 2: Distribute Event, 3:Create Report
			 * 
			 */

		}

		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
}
