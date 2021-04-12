package com.mir.ems.topTab;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import org.apache.tomcat.jni.Time;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import com.mir.ems.globalVar.global;
import com.mir.rest.clent.REST_CemaProfile;
import com.mir.rest.clent.REST_DeviceProfile;
import com.mir.rest.clent.REST_EMSProfile;
import com.mir.rest.clent.Test;

@SuppressWarnings("serial")
public class new_IntergratedTopology2 extends JPanel {
	public static int flag = 0;

	Graph graph = new SingleGraph("Tutorial 1");
	public final String emsEdge = "REST_EMS";

	public new_IntergratedTopology2() {
		/*
		 * Graph Layout Setting
		 */
		java.net.URL emsUrl = new_RestTopology.class.getResource("/IMAGE/dddd.png");

		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
//		 viewer.enableAutoLayout();
		viewer.disableAutoLayout();

		ViewPanel topologyPanel = (ViewPanel) viewer.addDefaultView(false);
		topologyPanel.setSize(1467, 700);
		add(topologyPanel);

		setBounds(14, 60, 1467, 700);
		setLayout(new BorderLayout(0, 0));
		setVisible(true);

		Node a = graph.addNode(emsEdge);

		a.addAttribute("ui.label", a.getId());

		int sum = 0;

		for (int i = 0; i < 20; i++) {

			sum += (i * 20);
		}

		a.setAttribute("x", (sum / 40));
		a.setAttribute("y", 10);

		a.addAttribute("ui.style",
				"text-alignment: above; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
						+ emsUrl + "');");

		createTopology();

	}

	public void createTopology() {
		java.net.URL gatewayUrl = new_RestTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
		java.net.URL ledUrl = new_RestTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
//		java.net.URL deviceURL = EmaTopology.class.getResource("/IMAGE/LED.png");
		java.net.URL deviceURL = EmaTopology.class.getResource("/IMAGE/meter.png");
		
		TimerTask chartUpdaterTask = new TimerTask() {

			Node emsGroup = null;
			Node semaGroup = null;
			Node cemaGroup = null;
			Node deviceGroup = null;
			
			
			Node mdmsGroup = null;
			Node dcuGroup = null;
			Node meterGroup = null;
//			Node deviceGroup = null;

			@Override
			public void run() {

				// ems
				ArrayList<String> EMS = new ArrayList<String>();
				EMS.addAll(global.rest_EMSProfile.keySet());

				for (int i = 0; i < EMS.size(); i++) {
					System.out.println("EMS?!" + EMS.get(i));
				}

				String[] emsList = new String[EMS.size()];

				for (int i = 0; i < EMS.size(); i++) {
					emsList[i] = EMS.get(i).toString();
				}

				Arrays.sort(emsList);

				// sema
				ArrayList<String> SEMA = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				SEMA.addAll(global.rest_SemaProfile.keySet());

				System.out.println("SEMA_TO" + SEMA.toString());

				String[] semaList = new String[SEMA.size()];

				for (int i = 0; i < SEMA.size(); i++) {
					semaList[i] = SEMA.get(i).toString();
					System.err.println("arraysLIST Sort" + semaList[i]);
				}

//				for(int i=0;i<SEMA.size();i++) {
//					System.out.println("SEMA?!"+SEMA.get(i));
//				}

				Arrays.sort(semaList);

				// cema
				ArrayList<String> CEMA = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				CEMA.addAll(global.rest_CemaProfile.keySet());

				String[] cemaList = new String[CEMA.size()];

				for (int i = 0; i < CEMA.size(); i++) {
					cemaList[i] = CEMA.get(i).toString();

				}

//				Arrays.sort(cemaList);

				ArrayList<Integer> templist = new ArrayList<Integer>();
				// sort
				for (int i = 0; i < CEMA.size(); i++) {

					int temp;
					temp = Integer.parseInt(CEMA.get(i).substring(10));
					
					
					templist.add(temp);

				}
				
			
				templist.sort(Comparator.naturalOrder());
				
				for (int i = 0; i < CEMA.size(); i++) {
					
					

					System.out.println("test_SORT"+templist.get(i));
					cemaList[i]="CLIENT_EMA"+templist.get(i);

				}
				
				//array.sort

				// device
				ArrayList<String> DEVICE = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				DEVICE.addAll(global.rest_deviceProfile.keySet());

				String[] deviceList = new String[DEVICE.size()];

				for (int i = 0; i < DEVICE.size(); i++) {
					deviceList[i] = DEVICE.get(i).toString();

				}

//				Arrays.sort(deviceList);
				
				
				ArrayList<Integer> templist2 = new ArrayList<Integer>();
				// sort
				for (int i = 0; i < DEVICE.size(); i++) {

					int temp;
					temp = Integer.parseInt(DEVICE.get(i).substring(6));
					
					
					templist2.add(temp);

				}
				
			
				templist2.sort(Comparator.naturalOrder());
				
				for (int i = 0; i < DEVICE.size(); i++) {
					
					

					System.out.println("test_SORT"+templist2.get(i));
					deviceList[i]="DEVICE"+templist2.get(i);

				}
				
				

				for (int i = 0; i < DEVICE.size(); i++) {

					System.err.println("DEVICELIST Sort" + deviceList[i]);

				}

				String nodeSet_ADD = graph.getNodeSet().toString();
				nodeSet_ADD = nodeSet_ADD.replace("[", "");
				nodeSet_ADD = nodeSet_ADD.replace("]", "");
				nodeSet_ADD = nodeSet_ADD.replace(" ", "");

				String[] nodeSet_ADD_ARR = nodeSet_ADD.split(",");
				Set<String> strSet = Arrays.stream(nodeSet_ADD_ARR).collect(Collectors.toSet());

//				
				for (int i = 0; i < emsList.length; i++) {
					String key = emsList[i];

					if (!strSet.contains(key.toString())) {

						try {
							emsGroup = graph.addNode(key);
							emsGroup.setAttribute("x", (global.rest_semaCNT * 10));
							emsGroup.addAttribute("ui.label", emsGroup.getId());

							emsGroup.setAttribute("y", 0);
							emsGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ gatewayUrl + "');");

//							emsGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box;  fill-mode: image-scaled; fill-image: url('"
//											+ gatewayUrl + "');");

							graph.addEdge(emsEdge + key, emsEdge, key);
//							graph.getEdge(emsEdge + key).addAttribute("ui.style", "stroke-mode:dots;","fill-color: red;");
							graph.getEdge(emsEdge + key).addAttribute("ui.style", "fill-color: red;");

							global.rest_semaCNT += 1;

						} catch (Exception e) {
						}
					}
				}

				// sema
				String nodeSemaSet_ADD = graph.getNodeSet().toString();
				nodeSemaSet_ADD = nodeSemaSet_ADD.replace("[", "");
				nodeSemaSet_ADD = nodeSemaSet_ADD.replace("]", "");
				nodeSemaSet_ADD = nodeSemaSet_ADD.replace(" ", "");

				String[] nodeDevSet_ADD_ARR = nodeSemaSet_ADD.split(",");
				Set<String> strDevSet = Arrays.stream(nodeDevSet_ADD_ARR).collect(Collectors.toSet());

				int semaCounting = semaList.length;

				// HTTP Connect
				for (int k = 0; k < semaCounting; k++) {

					System.out.println("여기");

					String dev_key = semaList[k] + "";
					if (!strDevSet.contains(dev_key.toString())) {

						System.out.println("여기들어옴?!");
						global.rest_cemaCNT += 1;

						try {
							semaGroup = graph.addNode(dev_key);
							semaGroup.setAttribute("x", (global.rest_cemaCNT * 10) - 5);
							semaGroup.addAttribute("ui.label", semaGroup.getId());
							semaGroup.setAttribute("y", -20);
							semaGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ ledUrl + "');");

//							semaGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; fill-mode: image-scaled; fill-image: url('"
//											+ ledUrl + "');");

							// under ==> right, size : text size, siz2 : image size

							System.out.println("DEV_KEY" + dev_key);
							graph.addEdge("SEMA" + dev_key, global.rest_SemaProfile.get(dev_key).getEmaID().toString(),
									dev_key);

//							graph.addEdge("SEMA" + dev_key,
//									emsEdge, dev_key);

						} catch (Exception e) {
						}
					}
				}

				// rest Connect
				for (int k = 0; k < semaCounting; k++) {

					System.out.println("여기");

					String dev_key = semaList[k] + "";

					System.out.println("여기들어옴?!");
					global.rest_cemaCNT2 += 1;

					try {

						graph.addEdge("SEMA2" + dev_key, emsEdge, dev_key);

						graph.getEdge("SEMA2" + dev_key).addAttribute("ui.style", "fill-color: red;");

//							graph.addEdge("SEMA" + dev_key,
//									emsEdge, dev_key);

					} catch (Exception e) {
					}

				}

				// cema
				String nodeCemaSet_ADD = graph.getNodeSet().toString();
				nodeCemaSet_ADD = nodeCemaSet_ADD.replace("[", "");
				nodeCemaSet_ADD = nodeCemaSet_ADD.replace("]", "");
				nodeCemaSet_ADD = nodeCemaSet_ADD.replace(" ", "");

				String[] nodeCema_ADD_ARR = nodeCemaSet_ADD.split(",");
				Set<String> strCemaSet = Arrays.stream(nodeCema_ADD_ARR).collect(Collectors.toSet());

				int cemaCounting = cemaList.length;

				for (int k = 0; k < cemaCounting; k++) {

					// System.out.println("여기");

					String dev_key = cemaList[k] + "";
					if (!strCemaSet.contains(dev_key.toString())) {

						global.rest_deviceCNT += 1;

						try {
							cemaGroup = graph.addNode(dev_key);
							cemaGroup.setAttribute("x", (global.rest_deviceCNT * 25) - 5);
							cemaGroup.addAttribute("ui.label", cemaGroup.getId());
							cemaGroup.setAttribute("y", -40);
							cemaGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ ledUrl + "');");

							graph.addEdge("CEMA" + dev_key, global.rest_CemaProfile.get(dev_key).getEmaID().toString(),
									dev_key);

						} catch (Exception e) {
						}
					}
				}

				// device
				String nodeDeviceSet_ADD = graph.getNodeSet().toString();
				nodeDeviceSet_ADD = nodeDeviceSet_ADD.replace("[", "");
				nodeDeviceSet_ADD = nodeDeviceSet_ADD.replace("]", "");
				nodeDeviceSet_ADD = nodeDeviceSet_ADD.replace(" ", "");

				String[] nodeDevice_ADD_ARR = nodeCemaSet_ADD.split(",");
				Set<String> strDeviceSet = Arrays.stream(nodeDevice_ADD_ARR).collect(Collectors.toSet());

				int deviceCounting = deviceList.length;

				for (int k = 0; k < deviceCounting; k++) {

					// System.out.println("여기");

					String dev_key = deviceList[k] + "";
					if (!strDeviceSet.contains(dev_key.toString())) {

						global.rest_edgeCNT += 1;

						try {
							deviceGroup = graph.addNode(dev_key);
							deviceGroup.setAttribute("x", (global.rest_edgeCNT * 6) - 5);
							deviceGroup.addAttribute("ui.label", deviceGroup.getId());
							deviceGroup.setAttribute("y", -100);
							deviceGroup.addAttribute("ui.style",
									"text-alignment: under; size: 25px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ deviceURL + "');");

//							deviceGroup.addAttribute("ui.style",
//									"size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//											+ deviceURL + "');");

//							graph.addEdge("DEVICE" + dev_key,
//									global.rest_deviceProfile.get(dev_key).getEmaID().toString(), dev_key);
//							
							graph.addEdge("DEVICE" + dev_key,
									global.rest_deviceProfile.get(dev_key).getEmaID().toString(), dev_key);

						} catch (Exception e) {
						}
					}
				}
				
				
				
				//AMI
				
//				// ems
//				ArrayList<String> MDMS_EMS = new ArrayList<String>();
//				MDMS_EMS.addAll(global.rest_MDMS_EMSProfile.keySet());
//
//				for (int i = 0; i < MDMS_EMS.size(); i++) {
//					System.out.println("EMS?!" + MDMS_EMS.get(i));
//				}
//
//				String[] REST_List = new String[MDMS_EMS.size()];
//
//				for (int i = 0; i < MDMS_EMS.size(); i++) {
//					REST_List[i] = MDMS_EMS.get(i).toString();
//				}
//
//				Arrays.sort(REST_List);
//
//				// sema (MDMS)
//				ArrayList<String> MDMS = new ArrayList<String>();
//				// devString.addAll(global.devHashMap.keySet());
//				MDMS.addAll(global.rest_MDMSProfile.keySet());
//
//				String[] mdmsList = new String[MDMS.size()];
//
//				for (int i = 0; i < MDMS.size(); i++) {
//
//					System.out.println("아무것도 안뜸");
//					mdmsList[i] = MDMS.get(i).toString();
//					System.err.println("MDMS@" + mdmsList[i]);
//				}
//
//				Arrays.sort(mdmsList);
//
//				// sort
////				ArrayList<Integer> mdmstemplist = new ArrayList<Integer>();
//
//				// cema
//				ArrayList<String> DCU = new ArrayList<String>();
//				// devString.addAll(global.devHashMap.keySet());
//				DCU.addAll(global.rest_DCUProfile.keySet());
//
//				String[] dcuList = new String[DCU.size()];
//
//				for (int i = 0; i < DCU.size(); i++) {
//					dcuList[i] = DCU.get(i).toString();
//				}
//
//				Arrays.sort(dcuList);
//
//				// device
//				ArrayList<String> METER = new ArrayList<String>();
//				// devString.addAll(global.devHashMap.keySet());
//				METER.addAll(global.rest_meterProfile.keySet());
//
//				String[] meterList = new String[METER.size()];
//
//				for (int i = 0; i < METER.size(); i++) {
//					meterList[i] = METER.get(i).toString();
//				}
//
//				Arrays.sort(deviceList);
//
////				// sort
////
////				ArrayList<Integer> meter_templist = new ArrayList<Integer>();
////				// sort
////				for (int i = 0; i < METER.size(); i++) {
////
////					int temp;
////					temp = Integer.parseInt(METER.get(i).substring(5));
////
////					meter_templist.add(temp);
////
////				}
////
////				meter_templist.sort(Comparator.naturalOrder());
////
////				for (int i = 0; i < METER.size(); i++) {
////
////					System.out.println("test_SORT" + meter_templist.get(i));
////					meterList[i] = "METER" + meter_templist.get(i);
////
////				}
////				// sort
//
//				String mdmsnodeSet_ADD = graph.getNodeSet().toString();
//				mdmsnodeSet_ADD = mdmsnodeSet_ADD.replace("[", "");
//				mdmsnodeSet_ADD = mdmsnodeSet_ADD.replace("]", "");
//				mdmsnodeSet_ADD = mdmsnodeSet_ADD.replace(" ", "");
//
//				String[] mdmsnodeSet_ARR = mdmsnodeSet_ADD.split(",");
//				Set<String> mdmsstrSet = Arrays.stream(mdmsnodeSet_ARR).collect(Collectors.toSet());
//
////				
//				for (int i = 0; i < REST_List.length; i++) {
//					String key = REST_List[i];
//
//					if (!mdmsstrSet.contains(key.toString())) {
//
//						try {
//							mdmsGroup = graph.addNode(key);
//							mdmsGroup.setAttribute("x", (global.rest_mdmsCNT * 10));
//							mdmsGroup.addAttribute("ui.label", mdmsGroup.getId());
//
//							mdmsGroup.setAttribute("y", 0);
//							mdmsGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//											+ gatewayUrl + "');");
//
//							graph.addEdge(emsEdge + key, emsEdge, key);
//							global.rest_mdmsCNT += 1;
//
//						} catch (Exception e) {
//						}
//					}
//				}
//
//				// sema
//				String dcuSemaSet_ADD = graph.getNodeSet().toString();
//				dcuSemaSet_ADD = dcuSemaSet_ADD.replace("[", "");
//				dcuSemaSet_ADD = dcuSemaSet_ADD.replace("]", "");
//				dcuSemaSet_ADD = dcuSemaSet_ADD.replace(" ", "");
//
//				String[] dcuSemaSet_ARR = dcuSemaSet_ADD.split(",");
//				Set<String> strDCUSet = Arrays.stream(dcuSemaSet_ARR).collect(Collectors.toSet());
//
//				int mdmsCounting = mdmsList.length;
//
//				for (int k = 0; k < mdmsCounting; k++) {
//
//					System.out.println("여기");
//
//					String dev_key = mdmsList[k] + "";
//					if (!strDCUSet.contains(dev_key.toString())) {
//
//						System.out.println("여기들어옴?!");
//						global.rest_dcuCNT += 1;
//
//						try {
//							dcuGroup = graph.addNode(dev_key);
//							dcuGroup.setAttribute("x", (global.rest_dcuCNT * 10) - 5);
//							dcuGroup.addAttribute("ui.label", dcuGroup.getId());
//							dcuGroup.setAttribute("y", -20);
//							dcuGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//											+ ledUrl + "');");
//
//							System.out.println("DEV_KEY" + dev_key);
//
//							graph.addEdge("dcuGroup" + dev_key, global.rest_MDMSProfile.get(dev_key).getEmaID().toString(),
//									dev_key);
//
//							graph.getEdge("dcuGroup" + dev_key).addAttribute("ui.style", "fill-color: red;");
//
////							graph.addEdge("SEMA" + dev_key,
////									emsEdge, dev_key);
//
//						} catch (Exception e) {
//						}
//					}
//				}
//
//				// cema
//				String nodeMeterSet_ADD = graph.getNodeSet().toString();
//				nodeMeterSet_ADD = nodeMeterSet_ADD.replace("[", "");
//				nodeMeterSet_ADD = nodeMeterSet_ADD.replace("]", "");
//				nodeMeterSet_ADD = nodeMeterSet_ADD.replace(" ", "");
//
//				String[] nodeMeterSet_ARR = nodeMeterSet_ADD.split(",");
//				Set<String> strMeterSet = Arrays.stream(nodeMeterSet_ARR).collect(Collectors.toSet());
//
//				int dcuCounting = dcuList.length;
//
//				for (int k = 0; k < dcuCounting; k++) {
//
//					// System.out.println("여기");
//
//					String dev_key = dcuList[k] + "";
//					if (!strMeterSet.contains(dev_key.toString())) {
//
//						global.rest_meterCNT += 1;
//
//						try {
//							meterGroup = graph.addNode(dev_key);
//							meterGroup.setAttribute("x", (global.rest_meterCNT * 10) - 5);
//							meterGroup.addAttribute("ui.label", meterGroup.getId());
//							meterGroup.setAttribute("y", -40);
//							meterGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//											+ ledUrl + "');");
//
//							graph.addEdge("meterGroup" + dev_key, global.rest_DCUProfile.get(dev_key).getEmaID().toString(),
//									dev_key);
//
//						} catch (Exception e) {
//						}
//					}
//				}
//
//				// device
//				String nodeMeterDeviceSet_ADD = graph.getNodeSet().toString();
//				nodeMeterDeviceSet_ADD = nodeMeterDeviceSet_ADD.replace("[", "");
//				nodeMeterDeviceSet_ADD = nodeMeterDeviceSet_ADD.replace("]", "");
//				nodeMeterDeviceSet_ADD = nodeMeterDeviceSet_ADD.replace(" ", "");
//
//				String[] nodeMeterDeviceSet_ARR = nodeMeterDeviceSet_ADD.split(",");
//				Set<String> strMeterDeviceSet = Arrays.stream(nodeMeterDeviceSet_ARR).collect(Collectors.toSet());
//
//				int MeterdeviceCounting = deviceList.length;
//
//				for (int k = 0; k < MeterdeviceCounting; k++) {
//
//					// System.out.println("여기");
//
//					String dev_key = deviceList[k] + "";
//					if (!strMeterDeviceSet.contains(dev_key.toString())) {
//
//						global.rest_AMIedgeCNT += 1;
//
//						try {
//							deviceGroup = graph.addNode(dev_key);
//							deviceGroup.setAttribute("x", (global.rest_AMIedgeCNT * 10) - 5);
//							deviceGroup.addAttribute("ui.label", deviceGroup.getId());
//							deviceGroup.setAttribute("y", -60);
//							deviceGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//											+ deviceURL + "');");
//
////							graph.addEdge("DEVICE" + dev_key,
////									global.rest_deviceProfile.get(dev_key).getEmaID().toString(), dev_key);
////							
//							graph.addEdge("DEVICE" + dev_key,
//									global.rest_meterProfile.get(dev_key).getEmaID().toString(), dev_key);
//
//						} catch (Exception e) {
//						}
//					}
//				}
				
				
				//AMI2
				// ems
				ArrayList<String> EMS2 = new ArrayList<String>();
				EMS2.addAll(global.rest_MDMS_EMSProfile.keySet());

				for (int i = 0; i < EMS2.size(); i++) {
					System.out.println("EMS?!" + EMS2.get(i));
				}

				String[] emsList1 = new String[EMS2.size()];

				for (int i = 0; i < EMS.size(); i++) {
					emsList1[i] = EMS.get(i).toString();
				}
				
			
				

				Arrays.sort(emsList1);

				// sema (MDMS)
				ArrayList<String> SEMA1 = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				SEMA1.addAll(global.rest_MDMSProfile.keySet());

				String[] semaList1 = new String[SEMA1.size()];

				for (int i = 0; i < SEMA1.size(); i++) {

					System.out.println("아무것도 안뜸");
					semaList1[i] = SEMA1.get(i).toString();
					System.err.println("MDMS@" + semaList1[i]);
				}

				Arrays.sort(semaList1);

				// sort
				ArrayList<Integer> templist1 = new ArrayList<Integer>();

				// cema
				ArrayList<String> CEMA1 = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				CEMA1.addAll(global.rest_DCUProfile.keySet());

				String[] cemaList1 = new String[CEMA1.size()];

				for (int i = 0; i < CEMA1.size(); i++) {
					cemaList1[i] = CEMA1.get(i).toString();
				}

				Arrays.sort(cemaList1);

				// device
				ArrayList<String> DEVICE1 = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				DEVICE1.addAll(global.rest_meterProfile.keySet());

				String[] deviceList1 = new String[DEVICE1.size()];

				for (int i = 0; i < DEVICE1.size(); i++) {
					deviceList1[i] = DEVICE1.get(i).toString();
				}
				
			
				

//				Arrays.sort(deviceList);

				// sort

				ArrayList<Integer> templist21 = new ArrayList<Integer>();
				// sort
				for (int i = 0; i < DEVICE1.size(); i++) {

					int temp;
					temp = Integer.parseInt(DEVICE1.get(i).substring(5));

					templist21.add(temp);

				}

				templist21.sort(Comparator.naturalOrder());

				for (int i = 0; i < DEVICE1.size(); i++) {

					System.out.println("test_SORT" + templist21.get(i));
					deviceList1[i] = "METER" + templist21.get(i);

				}
				// sort

				String nodeSet_ADD1 = graph.getNodeSet().toString();
				nodeSet_ADD1 = nodeSet_ADD1.replace("[", "");
				nodeSet_ADD1 = nodeSet_ADD1.replace("]", "");
				nodeSet_ADD1 = nodeSet_ADD1.replace(" ", "");

				String[] nodeSet_ADD_ARR1 = nodeSet_ADD1.split(",");
				Set<String> strSet1 = Arrays.stream(nodeSet_ADD_ARR1).collect(Collectors.toSet());

//				
				for (int i = 0; i < emsList1.length; i++) {
					String key = emsList1[i];

					if (!strSet1.contains(key.toString())) {

						try {
							mdmsGroup = graph.addNode(key);
							mdmsGroup.setAttribute("x", (global.rest_mdmsCNT * 10));
							mdmsGroup.addAttribute("ui.label", mdmsGroup.getId());

							mdmsGroup.setAttribute("y", 0);
							mdmsGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ gatewayUrl + "');");

							graph.addEdge(emsEdge + key, emsEdge, key);
							global.rest_mdmsCNT += 1;

						} catch (Exception e) {
						}
					}
				}

				// sema
				String nodeSemaSet_ADD1 = graph.getNodeSet().toString();
				nodeSemaSet_ADD1 = nodeSemaSet_ADD1.replace("[", "");
				nodeSemaSet_ADD1 = nodeSemaSet_ADD1.replace("]", "");
				nodeSemaSet_ADD1 = nodeSemaSet_ADD1.replace(" ", "");

				String[] nodeDevSet_ADD_ARR1 = nodeSemaSet_ADD1.split(",");
				Set<String> strDevSet1 = Arrays.stream(nodeDevSet_ADD_ARR1).collect(Collectors.toSet());

				int semaCounting1 = semaList1.length;

				for (int k = 0; k < semaCounting1; k++) {

					System.out.println("여기");

					String dev_key = semaList1[k] + "";
					if (!strDevSet1.contains(dev_key.toString())) {

						System.out.println("여기들어옴?!");
						global.rest_dcuCNT += 1;

						try {
							dcuGroup = graph.addNode(dev_key);
							dcuGroup.setAttribute("x", (global.rest_dcuCNT * 10) - 5);
							dcuGroup.addAttribute("ui.label", dcuGroup.getId());
							dcuGroup.setAttribute("y", -20);
							dcuGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ ledUrl + "');");

							System.out.println("DEV_KEY" + dev_key);

							graph.addEdge("dcuGroup" + dev_key, global.rest_MDMSProfile.get(dev_key).getEmaID().toString(),
									dev_key);

							graph.getEdge("dcuGroup" + dev_key).addAttribute("ui.style", "fill-color: red;");

//							graph.addEdge("SEMA" + dev_key,
//									emsEdge, dev_key);

						} catch (Exception e) {
						}
					}
				}

				// cema
				String nodeCemaSet_ADD1 = graph.getNodeSet().toString();
				nodeCemaSet_ADD1 = nodeCemaSet_ADD1.replace("[", "");
				nodeCemaSet_ADD1 = nodeCemaSet_ADD1.replace("]", "");
				nodeCemaSet_ADD1 = nodeCemaSet_ADD1.replace(" ", "");

				String[] nodeCema_ADD_ARR1 = nodeCemaSet_ADD1.split(",");
				Set<String> strCemaSet1 = Arrays.stream(nodeCema_ADD_ARR1).collect(Collectors.toSet());

				int cemaCounting1 = cemaList1.length;

				for (int k = 0; k < cemaCounting1; k++) {

					// System.out.println("여기");

					String dev_key = cemaList1[k] + "";
					if (!strCemaSet1.contains(dev_key.toString())) {

						global.rest_meterCNT += 1;

						try {
							meterGroup = graph.addNode(dev_key);
							meterGroup.setAttribute("x", (global.rest_meterCNT * 10) - 5);
							meterGroup.addAttribute("ui.label", meterGroup.getId());
							meterGroup.setAttribute("y", -40);
							meterGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ ledUrl + "');");

							graph.addEdge("meterGroup" + dev_key, global.rest_DCUProfile.get(dev_key).getEmaID().toString(),
									dev_key);

						} catch (Exception e) {
						}
					}
				}
				
				
				
//				Iterator<REST_DeviceProfile> dIterator = global.rest_deviceProfile.values().iterator();
//				while (dIterator.hasNext()) {
//					REST_DeviceProfile dProfile = dIterator.next();
//					
//					dProfile.getDeviceType();
//					
//				}
				
				// device
				String nodeDeviceSet_ADD1 = graph.getNodeSet().toString();
				nodeDeviceSet_ADD1 = nodeDeviceSet_ADD1.replace("[", "");
				nodeDeviceSet_ADD1 = nodeDeviceSet_ADD1.replace("]", "");
				nodeDeviceSet_ADD1 = nodeDeviceSet_ADD1.replace(" ", "");

				String[] nodeDevice_ADD_ARR1 = nodeCemaSet_ADD1.split(",");
				Set<String> strDeviceSet1 = Arrays.stream(nodeDevice_ADD_ARR1).collect(Collectors.toSet());

				int deviceCounting1 = deviceList1.length;

				for (int k = 0; k < deviceCounting1; k++) {

					// System.out.println("여기");

					String dev_key = deviceList1[k] + "";
					if (!strDeviceSet1.contains(dev_key.toString())) {

						global.rest_AMIedgeCNT += 1;

						try {
							deviceGroup = graph.addNode(dev_key);
							deviceGroup.setAttribute("x", (global.rest_AMIedgeCNT * 10) - 5);
							deviceGroup.addAttribute("ui.label", deviceGroup.getId());
							deviceGroup.setAttribute("y", -60);
							deviceGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ deviceURL + "');");

//							graph.addEdge("DEVICE" + dev_key,
//									global.rest_deviceProfile.get(dev_key).getEmaID().toString(), dev_key);
//							
							graph.addEdge("DEVICE" + dev_key,
									global.rest_meterProfile.get(dev_key).getEmaID().toString(), dev_key);

						} catch (Exception e) {
						}
					}
				}
				
				

			}

		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 1000, 3000);
//		timer.schedule(chartUpdaterTask, 5000);
	}

}
