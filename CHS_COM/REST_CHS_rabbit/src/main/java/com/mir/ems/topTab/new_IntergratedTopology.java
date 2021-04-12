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
import com.mir.rest.clent.REST_DcuProfile;
import com.mir.rest.clent.REST_DeviceProfile;
import com.mir.rest.clent.REST_EMAProfile;
import com.mir.rest.clent.REST_EMSProfile;
import com.mir.rest.clent.REST_MdmsProfile;
import com.mir.rest.clent.REST_MeterProfile;
import com.mir.rest.clent.REST_SemaProfile;
import com.mir.rest.clent.Test;

@SuppressWarnings("serial")
public class new_IntergratedTopology extends JPanel {
	public static int flag = 0;

	Graph graph = new SingleGraph("Tutorial 1");
	public final String emsEdge = "REST_CHS";

	public new_IntergratedTopology() {
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
//		java.net.URL ledUrl = new_RestTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
		java.net.URL ledUrl = new_RestTopology.class.getResource("/IMAGE/ema_image1.png");
		java.net.URL semaURL = new_RestTopology.class.getResource("/IMAGE/ema_image2.png");
		
		java.net.URL dcuURL = new_RestTopology.class.getResource("/IMAGE/dcu.png");
		
		java.net.URL deviceURL = EmaTopology.class.getResource("/IMAGE/LED.png");
		java.net.URL meterURL = EmaTopology.class.getResource("/IMAGE/meter.png");
		
		

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
				EMS.addAll(global.rest_EMAProfile.keySet());

				for (int i = 0; i < EMS.size(); i++) {
					System.out.println("EMS?!" + EMS.get(i));
				}

				String[] emsList = new String[EMS.size()];

				for (int i = 0; i < EMS.size(); i++) {
					emsList[i] = EMS.get(i).toString();
				}

				Arrays.sort(emsList, new AlphanumericSorting());

				// sema
				ArrayList<String> SEMA = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				SEMA.addAll(global.rest_SemaProfile.keySet());

				System.out.println("SEMA_TO" + SEMA.toString());

				String[] semaList = new String[SEMA.size()];

				for (int i = 0; i < SEMA.size(); i++) {
					semaList[i] = SEMA.get(i).toString();
				}

				Arrays.sort(semaList, new AlphanumericSorting());
				
				
				
				
				

				// cema
				ArrayList<String> CEMA = new ArrayList<String>();

				// devString.addAll(global.devHashMap.keySet());
				CEMA.addAll(global.rest_CemaProfile.keySet());

				String[] cemaList = new String[CEMA.size()];

				for (int i = 0; i < CEMA.size(); i++) {
					cemaList[i] = CEMA.get(i).toString();

				}

				Arrays.sort(cemaList, new AlphanumericSorting());

				// device
				ArrayList<String> DEVICE = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				DEVICE.addAll(global.rest_deviceProfile.keySet());

				String[] deviceList = new String[DEVICE.size()];

				for (int i = 0; i < DEVICE.size(); i++) {
					deviceList[i] = DEVICE.get(i).toString();

				}

				Arrays.sort(deviceList, new AlphanumericSorting());

				// ems
				for (int i = 0; i < emsList.length; i++) {
					REST_EMAProfile mProfile = global.rest_EMAProfile.get(emsList[i]);

//					global.rest_semaCNT += 1;
					String key = mProfile.getEmaID();

					try {
						emsGroup = graph.addNode(key);
						emsGroup.setAttribute("x", (global.rest_semaCNT * 10));
						emsGroup.addAttribute("ui.label", emsGroup.getId());

						emsGroup.setAttribute("y", 0);
						emsGroup.addAttribute("ui.style",
								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
										+ gatewayUrl + "');");

//						emsGroup.addAttribute("ui.style",
//								"text-alignment: under; size: 45px, 45px; shape: rounded-box;  fill-mode: image-scaled; fill-image: url('"
//										+ gatewayUrl + "');");

						graph.addEdge(emsEdge + key, emsEdge, key);
//						graph.getEdge(emsEdge + key).addAttribute("ui.style", "stroke-mode:dots;","fill-color: red;");
						graph.getEdge(emsEdge + key).addAttribute("ui.style", "fill-color: red;");


					} catch (Exception e) {
					}

				}
				
				
				int restCNT=1;
				// sema
				for (int k = 0; k < semaList.length; k++) {
					REST_SemaProfile mProfile = global.rest_SemaProfile.get(semaList[k]);
					String dev_key = mProfile.getsubID();
//					global.rest_cemaCNT += 1;
					restCNT+=1;

					try {
						if (graph.getNode(dev_key) == null) {
						semaGroup = graph.addNode(dev_key);
//						semaGroup.setAttribute("x", (global.rest_cemaCNT * 10) - 5);
						semaGroup.setAttribute("x", (restCNT * 10) - 5);
						semaGroup.addAttribute("ui.label", semaGroup.getId());
						semaGroup.setAttribute("y", -20);
						semaGroup.addAttribute("ui.style",
								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
										+ semaURL + "');");

//							semaGroup.addAttribute("ui.style",
//									"text-alignment: under; size: 45px, 45px; shape: rounded-box; fill-mode: image-scaled; fill-image: url('"
//											+ ledUrl + "');");
						}
						// under ==> right, size : text size, siz2 : image size
						if (graph.getEdge("SEMA" + dev_key) == null) {
						graph.addEdge("SEMA" + dev_key, mProfile.getEmaID(), dev_key);
						
						
						}
					} catch (Exception e) {
					}
				}
				
				
				//rest
//				for (int k = 0; k < semaList.length; k++) {
//					REST_SemaProfile mProfile = global.rest_SemaProfile.get(semaList[k]);
//					String dev_key = mProfile.getsubID();
//					restCNT+=1;
//
//					try {
//						if (graph.getNode(dev_key) == null) {
//						semaGroup = graph.addNode(dev_key);
//						semaGroup.setAttribute("x", (restCNT * 10) - 5);
//						semaGroup.addAttribute("ui.label", semaGroup.getId());
//						semaGroup.setAttribute("y", -20);
//						semaGroup.addAttribute("ui.style",
//								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//										+ ledUrl + "');");
//
//						}
//						if (graph.getEdge("REST" + dev_key) == null) {
//						graph.addEdge("REST" + dev_key, emsEdge, dev_key);
//						graph.getEdge("REST" + dev_key).addAttribute("ui.style", "fill-color: red;");
//						
//						}
//						
//					} catch (Exception e) {
//					}
//				}
				
				
				

				// cema
				
				int cemaCNT=1;
				for (int k = 0; k < cemaList.length; k++) {
					REST_CemaProfile mProfile = global.rest_CemaProfile.get(cemaList[k]);
					String dev_key = mProfile.getDeviceEMAID();

//					global.rest_deviceCNT += 1;
					cemaCNT+=1;

					try {
						if ((graph.getNode(dev_key) == null)) {
						cemaGroup = graph.addNode(dev_key);
//						cemaGroup.setAttribute("x", (global.rest_deviceCNT * 25) - 5);
						cemaGroup.setAttribute("x", (cemaCNT * 25) - 5);
						cemaGroup.addAttribute("ui.label", cemaGroup.getId());
						cemaGroup.setAttribute("y", -30);
						cemaGroup.addAttribute("ui.style",
								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
										+ ledUrl + "');");
						}
						
						if (graph.getEdge("CEMA" + dev_key) == null) {
						graph.addEdge("CEMA" + dev_key, mProfile.getEmaID(), dev_key);
						
						}
					} catch (Exception e) {
					}
				}

				// device
				
				int deviceCNT=1;
				int deviceCNT2=1;
				for (int k = 0; k < deviceList.length; k++) {
					REST_DeviceProfile mProfile = global.rest_deviceProfile.get(deviceList[k]);
					String dev_key = mProfile.getDeviceEMAID();

//					global.rest_edgeCNT += 1;
					deviceCNT+=1;
					deviceCNT2+=1;

					try {

						if ((graph.getNode(dev_key) == null)  && (mProfile.getDeviceType().equals("METER"))){
							deviceGroup = graph.addNode(dev_key);
//							deviceGroup.setAttribute("x", (global.rest_edgeCNT * 6) - 5);
							deviceGroup.setAttribute("x", (deviceCNT*25) - 5);
							deviceGroup.addAttribute("ui.label", deviceGroup.getId());
							deviceGroup.setAttribute("y", -40);
							deviceGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ meterURL + "');");


							graph.addEdge("DEVICE" + dev_key,
									global.rest_deviceProfile.get(dev_key).getEmaID().toString(), dev_key);
//							
						}
//						else if (mProfile.getDeviceType().equals("DEVICE")){
						
						
						
							deviceGroup = graph.addNode(dev_key);
							deviceGroup.setAttribute("x", (deviceCNT2*25) - 5);
							deviceGroup.addAttribute("ui.label", deviceGroup.getId());
							deviceGroup.setAttribute("y", -60);
							deviceGroup.addAttribute("ui.style",
									"text-alignment: under; size: 25px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ deviceURL + "');");
							deviceGroup.addAttribute("ui.style",
									"size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ deviceURL + "');");
							graph.addEdge("DEVICE" + dev_key,
									global.rest_deviceProfile.get(dev_key).getEmaID().toString(), dev_key);
							
//						}
						
						if (graph.getEdge("DEVICE" + dev_key) == null) {
							graph.addEdge("DEVICE" + dev_key, mProfile.getEmaID(), dev_key);
							
							if(mProfile.getDeviceType().equals("METER")) {
								graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: orange;");
							}
							
						}
					} catch (Exception e) {
					}
				}

				// AMI

				// MDMS
				
				// AMI
				ArrayList<String>AMI=new ArrayList<String>();
				AMI.addAll(global.rest_MDMS_EMSProfile.keySet());
				
				String[] AMIList=new String[AMI.size()];
				
				for(int i=0;i<AMI.size();i++) {
					AMIList[i]=AMI.get(i).toString();
				}
				Arrays.sort(AMIList, new AlphanumericSorting());
				
				
				
				
				
				// MDMS
				
				Node AmiGroup = null;
				
				Node mdmsGroup = null;
				Node dcuGroup = null;
				Node meterGroup = null;
				Node deviceGroup = null;
				
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
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
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
							meterGroup.addAttribute("ui.label", meterGroup.getId());
							meterGroup.setAttribute("y", -40);
							meterGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
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
//		timer.scheduleAtFixedRate(chartUpdaterTask, 1000, 3000);
		timer.schedule(chartUpdaterTask, 15000);
	}

}
