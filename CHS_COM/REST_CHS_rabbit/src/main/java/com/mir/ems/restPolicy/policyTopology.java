//package com.mir.ems.restPolicy;
//
//import java.awt.BorderLayout;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.Random;
//import java.util.Scanner;
//import java.util.Set;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.stream.Collectors;
//
//import javax.swing.JPanel;
//
//import org.apache.tomcat.jni.Time;
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//import org.graphstream.graph.implementations.SingleGraph;
//import org.graphstream.ui.swingViewer.ViewPanel;
//import org.graphstream.ui.view.Viewer;
//import com.mir.ems.globalVar.global;
//import com.mir.rest.clent.Test;
//
//@SuppressWarnings("serial")
//public class policyTopology extends JPanel {
//	public static int flag = 0;
//
//	Graph graph = new SingleGraph("Tutorial 1");
//	public final String emsEdge = "REST_EMS";
//
//	public policyTopology() {
//		/*
//		 * Graph Layout Setting
//		 */
//		java.net.URL emsUrl = policyTopology.class.getResource("/IMAGE/dddd.png");
//
//		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
//		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
//		// viewer.enableAutoLayout();
//		viewer.disableAutoLayout();
//
//		ViewPanel topologyPanel = (ViewPanel) viewer.addDefaultView(false);
//		topologyPanel.setSize(1467, 700);
//		add(topologyPanel);
//
//		setBounds(14, 60, 1467, 700);
//		setLayout(new BorderLayout(0, 0));
//		setVisible(true);
//
//		Node a = graph.addNode(emsEdge);
//
//		a.addAttribute("ui.label", a.getId());
//
//		int sum = 0;
//
//		for (int i = 0; i < 20; i++) {
//
//			sum += (i * 20);
//		}
//
//		a.setAttribute("x", (sum / 40));
//		a.setAttribute("y", 10);
//
//		a.addAttribute("ui.style",
//				"text-alignment: above; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//						+ emsUrl + "');");
//
//		createTopology();
//
//	}
//
//	public void createTopology() {
//		java.net.URL gatewayUrl = policyTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
//		java.net.URL ledUrl = policyTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
//		java.net.URL deviceURL = policyTopology.class.getResource("/IMAGE/LED.png");
//		TimerTask chartUpdaterTask = new TimerTask() {
//
//			Node emsGroup = null;
//			Node semaGroup = null;
//			Node linkGroup = null;
//
//			@Override
//			public void run() {
//
//				// ems
//				ArrayList<String> EMS = new ArrayList<String>();
//				EMS.addAll(global.rest_EMSProfile.keySet());
//
//				for (int i = 0; i < EMS.size(); i++) {
//					System.out.println("EMS?!" + EMS.get(i));
//				}
//
//				String[] emsList = new String[EMS.size()];
//
//				for (int i = 0; i < EMS.size(); i++) {
//					emsList[i] = EMS.get(i).toString();
//				}
//
//				Arrays.sort(emsList);
//
//				// sema
//				ArrayList<String> SEMA = new ArrayList<String>();
//				// devString.addAll(global.devHashMap.keySet());
//				SEMA.addAll(global.node_profile.keySet());
//
//				System.out.println("POLICY_TO" + SEMA.toString());
//
//				String[] semaList = new String[SEMA.size()];
//
//				for (int i = 0; i < SEMA.size(); i++) {
//					semaList[i] = SEMA.get(i).toString();
//					System.out.println(semaList[i]);
//
//				}
//
//				Arrays.sort(semaList);
//
//				String nodeSet_ADD = graph.getNodeSet().toString();
//				nodeSet_ADD = nodeSet_ADD.replace("[", "");
//				nodeSet_ADD = nodeSet_ADD.replace("]", "");
//				nodeSet_ADD = nodeSet_ADD.replace(" ", "");
//
//				String[] nodeSet_ADD_ARR = nodeSet_ADD.split(",");
//				Set<String> strSet = Arrays.stream(nodeSet_ADD_ARR).collect(Collectors.toSet());
//
////				
//				for (int i = 0; i < emsList.length; i++) {
//					String key = emsList[i];
//
//					if (!strSet.contains(key.toString())) {
//
//						try {
//							emsGroup = graph.addNode(key);
//							emsGroup.setAttribute("x", (global.rest_semaCNT * 10));
//							emsGroup.addAttribute("ui.label", emsGroup.getId());
//
//							emsGroup.setAttribute("y", 0);
//							emsGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//											+ gatewayUrl + "');");
//
//							graph.addEdge(emsEdge + key, emsEdge, key);
//							global.rest_semaCNT += 1;
//
//						} catch (Exception e) {
//						}
//					}
//				}
//
//				// sema
//				String nodeSemaSet_ADD = graph.getNodeSet().toString();
//				nodeSemaSet_ADD = nodeSemaSet_ADD.replace("[", "");
//				nodeSemaSet_ADD = nodeSemaSet_ADD.replace("]", "");
//				nodeSemaSet_ADD = nodeSemaSet_ADD.replace(" ", "");
//
//				String[] nodeDevSet_ADD_ARR = nodeSemaSet_ADD.split(",");
//				Set<String> strDevSet = Arrays.stream(nodeDevSet_ADD_ARR).collect(Collectors.toSet());
//
//				// node
////				String node_ADD = graph.getNodeSet().toString();
////				node_ADD = node_ADD.replace("[", "");
////				node_ADD = node_ADD.replace("]", "");
////				node_ADD = node_ADD.replace(" ", "");
////
////				String[] node_ADD_ARR = node_ADD.split(",");
////				Set<String> strNodeset = Arrays.stream(node_ADD_ARR).collect(Collectors.toSet());
//
//				int semaCounting = semaList.length;
//
//				System.out.println("semaCounting" + semaCounting);
//
//				for (int k = 0; k < semaCounting; k++) {
//
//					System.out.println("strDevSet:" + strDevSet);
//
////					String dev_key = semaList[k] + "";
//					String dev_key = semaList[k];
//					System.out.println("key:" + dev_key);
////					if (!strDevSet.contains(dev_key.toString())) {
//					if (strDevSet.contains("0000000000000001")) {
//					global.rest_cemaCNT += 1;
//
//					try {
//						semaGroup = graph.addNode(dev_key);
//						semaGroup.setAttribute("x", (global.rest_cemaCNT * 10) - 5);
//						semaGroup.addAttribute("ui.label", semaGroup.getId());
//						semaGroup.setAttribute("y", -20);
//						semaGroup.addAttribute("ui.style",
//								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//										+ ledUrl + "');");
//
//						graph.addEdge("SEMA" + dev_key, global.node_profile.get(dev_key).getEmaID().toString(),
//								dev_key);
//
//						System.out.println("key_value" + dev_key);
//
//						policy_Profile pNode = global.node_profile.get(dev_key);
//
//						ArrayList<String> node = new ArrayList<String>();
//
//						node = pNode.getSidelist();
//
//						System.out.println("getSideList" + node.toString());
//						System.out.println("nodeSIZE" + node.size());
//
//						if (strDevSet.contains("of")) {
//							for (int i = 0; i < node.size(); i++) {
//
//								System.out.println("print for");
//								String nodeID = node.get(i);
////										linkGroup=graph.addNode(nodeID);
//								System.out.println("dev_key" + dev_key);
//								System.out.println("nodeID" + nodeID);
//
//								System.out.println("i SIZE" + i);
//
//								graph.addEdge("node" + nodeID, dev_key, nodeID);
//
//							}
//
//						}
//
//					} catch (Exception e) {
//					}
//
//					}
//
//				}
//
//			}
//
//		};
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(chartUpdaterTask, 10000, 3000);
//	}
//
//}
