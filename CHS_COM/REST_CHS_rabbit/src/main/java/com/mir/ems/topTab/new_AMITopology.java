package com.mir.ems.topTab;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import com.mir.rest.clent.REST_DcuProfile;
import com.mir.rest.clent.REST_DeviceProfile;
import com.mir.rest.clent.REST_EMSProfile;
import com.mir.rest.clent.REST_MdmsProfile;
import com.mir.rest.clent.REST_MeterProfile;
import com.mir.rest.clent.Test;

@SuppressWarnings("serial")
public class new_AMITopology extends JPanel {
	public static int flag = 0;

	Graph graph = new SingleGraph("Tutorial 1");
	public final String emsEdge = "Operation Center";

	public new_AMITopology() {
		/*
		 * Graph Layout Setting
		 */
		java.net.URL emsUrl = new_RestTopology.class.getResource("/IMAGE/oc_8.png");

		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		// viewer.enableAutoLayout();
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
				"text-alignment: above; size: 45px, 45px; shape: box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
						+ emsUrl + "');");

		createTopology();

	}

	public void createTopology() {
		java.net.URL gatewayUrl = new_RestTopology.class.getResource("/IMAGE/dddd.png");
//		java.net.URL ledUrl = new_RestTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
		java.net.URL ledUrl = new_RestTopology.class.getResource("/IMAGE/ema_image1.png");
		java.net.URL semaURL = new_RestTopology.class.getResource("/IMAGE/ema_image2.png");
		
		java.net.URL dcuURL = new_RestTopology.class.getResource("/IMAGE/dcu.png");
		
		java.net.URL deviceURL = EmaTopology.class.getResource("/IMAGE/LED.png");
		java.net.URL meterURL = EmaTopology.class.getResource("/IMAGE/meter.png");
		TimerTask chartUpdaterTask = new TimerTask() {
			
			Node AmiGroup = null;
			
			Node mdmsGroup = null;
			Node dcuGroup = null;
			Node meterGroup = null;
			Node deviceGroup = null;

			@Override
			public void run() {
				
				// AMI
				ArrayList<String>AMI=new ArrayList<String>();
				AMI.addAll(global.rest_MDMS_EMSProfile.keySet());
				
				String[] AMIList=new String[AMI.size()];
				
				for(int i=0;i<AMI.size();i++) {
					AMIList[i]=AMI.get(i).toString();
				}
				Arrays.sort(AMIList, new AlphanumericSorting());
				
				
				
				
				
				// MDMS
				ArrayList<String> MDMS = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				MDMS.addAll(global.rest_MDMSProfile.keySet());

				String[] mdmsList1 = new String[MDMS.size()];

				for (int i = 0; i < MDMS.size(); i++) {

					System.out.println("아무것도 안뜸");
					mdmsList1[i] = MDMS.get(i).toString();
					System.err.println("MDMS@" + mdmsList1[i]);
				}

				Arrays.sort(mdmsList1, new AlphanumericSorting());

				// DCU
				ArrayList<String> DCU = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				DCU.addAll(global.rest_DCUProfile.keySet());

				String[] dcuList = new String[DCU.size()];

				for (int i = 0; i < DCU.size(); i++) {
					dcuList[i] = DCU.get(i).toString();
				}

				Arrays.sort(dcuList, new AlphanumericSorting());

				// meter
				ArrayList<String> METER = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				METER.addAll(global.rest_meterProfile.keySet());

				String[] meterList = new String[METER.size()];

				for (int i = 0; i < METER.size(); i++) {
					meterList[i] = METER.get(i).toString();
				}
				Arrays.sort(meterList, new AlphanumericSorting());

				// device
				ArrayList<String> DEVICE1 = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				DEVICE1.addAll(global.rest_deviceProfile.keySet());

				String[] deviceList1 = new String[DEVICE1.size()];

				for (int i = 0; i < DEVICE1.size(); i++) {
					deviceList1[i] = DEVICE1.get(i).toString();
				}
				Arrays.sort(deviceList1, new AlphanumericSorting());
				
				//REST_AMI 
				for(int i=0;i<AMIList.length;i++) {
					REST_EMSProfile rProfile=global.rest_MDMS_EMSProfile.get(AMIList[i]);
					String key=rProfile.getEmaID();
					
					
					
					System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@key?:"+key);
					
					try {
						
						if(graph.getNode(key)==null) {
							AmiGroup = graph.addNode(key);
							AmiGroup.setAttribute("x", (global.rest_mdmsCNT * 10));
							AmiGroup.addAttribute("ui.label", AmiGroup.getId());

							AmiGroup.setAttribute("y", 0);
							AmiGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ gatewayUrl + "');");
						}
						if (graph.getEdge(emsEdge + key) == null) {
							graph.addEdge(emsEdge + key, emsEdge, key);
//							graph.addEdge(emsEdge + key, emsEdge, emaID);
							
							graph.getEdge(emsEdge + key).addAttribute("ui.style", "fill-color: red;");
						}
//						global.rest_mdmsCNT += 1;
					}
				

				 catch (Exception e) {
				}
					
				}
				
				
				
				
				
				// MDMS
//				for (int i = 0; i < mdmsList1.length; i++) {
//					REST_MdmsProfile mProfile = global.rest_MDMSProfile.get(mdmsList1[i]);
//
//					String key = mProfile.getsubID();
//					String emaID = mProfile.getEmaID();
//					try {
//
//						if (graph.getNode(key) == null) {
//
//							mdmsGroup = graph.addNode(key);
//							mdmsGroup.setAttribute("x", (global.rest_mdmsCNT * 10));
//							mdmsGroup.addAttribute("ui.label", mdmsGroup.getId());
//
//							mdmsGroup.setAttribute("y", 0);
//							mdmsGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//											+ gatewayUrl + "');");
//						}
//
//						if (graph.getEdge(mdmsGroup + key) == null) {
//							graph.addEdge(mdmsGroup + key, emaID, key);
////							graph.addEdge(emsEdge + key, emsEdge, emaID);
//							
//							graph.getEdge(mdmsGroup + key).addAttribute("ui.style", "fill-color: red;");
//						}
//
//						global.rest_mdmsCNT += 1;
//
//					} catch (Exception e) {
//					}
//
//				}

				int dcu=1;
				// DCU
				for (int i = 0; i < dcuList.length; i++) {
					REST_DcuProfile dProfile = global.rest_DCUProfile.get(dcuList[i]);

					String dev_key = dProfile.getDeviceEMAID();
					String emaID = dProfile.getEmaID();
					dcu+=1;

					try {
						dcuGroup = graph.addNode(dev_key);
						dcuGroup.setAttribute("x", (dcu * 10) - 5);
						dcuGroup.addAttribute("ui.label", dcuGroup.getId());
						dcuGroup.setAttribute("y", -20);
						dcuGroup.addAttribute("ui.style",
								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
										+ dcuURL + "');");

						System.out.println("DEV_KEY" + dev_key);

						graph.addEdge("dcuGroup" + dev_key, dProfile.getEmaID(), dev_key);

						graph.getEdge("dcuGroup" + dev_key).addAttribute("ui.style", "fill-color: blue;");

					} catch (Exception e) {
					}

				}

				int meter=1;
				// meter
				for (int i = 0; i < meterList.length; i++) {
					REST_MeterProfile mProfile = global.rest_meterProfile.get(meterList[i]);

					String dev_key = mProfile.getDeviceEMAID();
					String emaID = mProfile.getEmaID();
					meter+=1;
					try {
						if (graph.getNode(dev_key) == null) {
							meterGroup = graph.addNode(dev_key);
							meterGroup.setAttribute("x", (meter * 10) - 5);
//							meterGroup.addAttribute("ui.label", meterGroup.getId());
							meterGroup.setAttribute("y", -40);
							meterGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
									
									"size: 15px, 15px; shape: rounded-box; size-mode: normal; fill-mode: image-scaled; fill-image: url('"
//									"size: 15px, 15px; size-mode: normal; fill-mode: image-scaled; fill-image: url('"
											+ meterURL + "');");
						}
						if (graph.getNode("METER" + dev_key) == null) {

							graph.addEdge("meterGroup" + dev_key, mProfile.getEmaID(), dev_key);
							graph.getEdge("meterGroup" + dev_key).addAttribute("ui.style", "fill-color: blue;");
						}
					} catch (Exception e) {
					}

				}




			}

		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 10000, 6000);
	}

}
