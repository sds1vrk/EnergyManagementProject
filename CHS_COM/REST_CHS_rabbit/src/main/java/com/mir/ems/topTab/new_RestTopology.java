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
import com.mir.rest.clent.REST_CemaProfile;
import com.mir.rest.clent.REST_DeviceProfile;
import com.mir.rest.clent.REST_EMAProfile;
import com.mir.rest.clent.REST_EMSProfile;
import com.mir.rest.clent.REST_SemaProfile;
import com.mir.rest.clent.Test;

@SuppressWarnings("serial")
public class new_RestTopology extends JPanel {
	public static int flag = 0;

	Graph graph = new SingleGraph("Tutorial 1");
	public final String emsEdge = "REST_CHS";

	public new_RestTopology() {
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

		java.net.URL deviceURL = EmaTopology.class.getResource("/IMAGE/LED.png");
		java.net.URL meterURL = EmaTopology.class.getResource("/IMAGE/meter.png");
		TimerTask chartUpdaterTask = new TimerTask() {

			Node EMAGroup = null;
			Node emsGroup = null;
			Node semaGroup = null;
			Node cemaGroup = null;
			Node deviceGroup = null;

			@Override
			public void run() {

				// REST_EMA
				ArrayList<String> REST_EMA = new ArrayList<String>();
				REST_EMA.addAll(global.rest_EMAProfile.keySet());

				String[] emaList = new String[REST_EMA.size()];

				for (int i = 0; i < REST_EMA.size(); i++) {
					emaList[i] = REST_EMA.get(i).toString();
				}

				Arrays.sort(emaList, new AlphanumericSorting());
				
				System.out.println("count:"+emaList.length);

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

//				REST_EMSProfile eProfile=global.rest_EMSProfile.get(emsList[i]);

//				EMAGroup=graph.addNode(id)

				// REST _CHS <-> DRP
				for (int i = 0; i < emaList.length; i++) {
					REST_EMAProfile mProfile = global.rest_EMAProfile.get(emaList[i]);

//					global.rest_semaCNT += 1;
					String key = mProfile.getEmaID();

					System.out.println("key::::::::::::::::::::::::::::" + key);

					try {
						EMAGroup = graph.addNode(key);

						EMAGroup.setAttribute("x", (global.rest_semaCNT * 10));
						EMAGroup.addAttribute("ui.label", EMAGroup.getId());

						EMAGroup.setAttribute("y", 0);
						EMAGroup.addAttribute("ui.style",
								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
										+ gatewayUrl + "');");

//						emsGroup.addAttribute("ui.style",
//								"text-alignment: under; size: 45px, 45px; shape: rounded-box;  fill-mode: image-scaled; fill-image: url('"
//										+ gatewayUrl + "');");

						graph.addEdge(EMAGroup + key, emsEdge, key);
//						graph.getEdge(emsEdge + key).addAttribute("ui.style", "stroke-mode:dots;","fill-color: red;");
						graph.getEdge(EMAGroup + key).addAttribute("ui.style", "fill-color: red;");

					} catch (Exception e) {
					}

				}



//				for (int i = 0; i < emsList.length; i++) {
//					REST_EMSProfile mProfile = global.rest_EMSProfile.get(emsList[i]);
//
////					global.rest_semaCNT += 1;
//					String key = mProfile.getEmaID();
//					String deviceID1 = mProfile.getDeviceID();
//
//					try {
//						emsGroup = graph.addNode(key);
//
//						emsGroup.setAttribute("x", (global.rest_semaCNT * 10));
//						emsGroup.addAttribute("ui.label", emsGroup.getId());
//
//						emsGroup.setAttribute("y", 0);
//						emsGroup.addAttribute("ui.style",
//								"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//										+ gatewayUrl + "');");
//
////						emsGroup.addAttribute("ui.style",
////								"text-alignment: under; size: 45px, 45px; shape: rounded-box;  fill-mode: image-scaled; fill-image: url('"
////										+ gatewayUrl + "');");
//
//						graph.addEdge(emsEdge + key, key, deviceID1);
////						graph.getEdge(emsEdge + key).addAttribute("ui.style", "stroke-mode:dots;","fill-color: red;");
//						graph.getEdge(emsEdge + key).addAttribute("ui.style", "fill-color: red;");
//
//					} catch (Exception e) {
//					}
//
//				}

				int restCNT = 1;
				// sema
				// DRP<-> SEMA
				for (int k = 0; k < semaList.length; k++) {
					REST_SemaProfile mProfile = global.rest_SemaProfile.get(semaList[k]);
					String dev_key = mProfile.getsubID();
//					String ema_key=mProfile.getEmaID();

//					global.rest_cemaCNT += 1;
					restCNT += 1;

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

				// cema

				int cemaCNT = 1;
				for (int k = 0; k < cemaList.length; k++) {
					REST_CemaProfile mProfile = global.rest_CemaProfile.get(cemaList[k]);
					String dev_key = mProfile.getDeviceEMAID();

//					global.rest_deviceCNT += 1;
					cemaCNT += 1;

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

				int deviceCNT = 1;
				int deviceCNT2 = 1;
				for (int k = 0; k < deviceList.length; k++) {
					REST_DeviceProfile mProfile = global.rest_deviceProfile.get(deviceList[k]);
					String dev_key = mProfile.getDeviceEMAID();

//					global.rest_edgeCNT += 1;
					deviceCNT += 1;
					deviceCNT2 += 1;

					try {

						if ((graph.getNode(dev_key) == null) && (mProfile.getDeviceType().equals("METER"))) {
							deviceGroup = graph.addNode(dev_key);
							deviceGroup.setAttribute("x", (deviceCNT * 45) - 5);
							deviceGroup.addAttribute("ui.label", deviceGroup.getId());
							deviceGroup.setAttribute("y", -40);
							deviceGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ meterURL + "');");

						}

						else if ((graph.getNode(dev_key) == null) && (mProfile.getDeviceType().equals("DEVICE"))) {
							deviceGroup = graph.addNode(dev_key);
							deviceGroup.setAttribute("x", (global.rest_edgeCNT * 6) - 5);
							deviceGroup.setAttribute("x", (deviceCNT2 * 25) - 5);
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
//							
						}

						if (graph.getEdge("DEVICE" + dev_key) == null) {
							graph.addEdge("DEVICE" + dev_key, mProfile.getEmaID(), dev_key);

							if (mProfile.getDeviceType().equals("METER")) {
								graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: orange;");
							}

						}
					} catch (Exception e) {
					}
				}

			}

		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 1000, 3000);
//		timer.schedule(chartUpdaterTask, 5000);
	}

}
