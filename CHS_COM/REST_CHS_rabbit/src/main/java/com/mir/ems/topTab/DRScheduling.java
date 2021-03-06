package com.mir.ems.topTab;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jfree.data.time.Minute;
import org.json.simple.JSONObject;

import com.mir.ems.GUI.MainFrame;
import com.mir.ems.Graph.DRSchedulingGraph;
import com.mir.ems.coap.CoAPClient;
import com.mir.ems.coap.CoAPObserverSubPath;
import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.globalVar.global;
import com.mir.ems.main.Connection;
import com.mir.ems.mqtt.HandleMqttMessage;
import com.mir.ems.mqtt.ProcessStatus;
import com.mir.rest.clent.EMA_Event;
import com.mir.rest.clent.event_structure;
import com.mir.vtn.controller.FrontController;
import com.mir.vtn.global.Global;

//checkbox

//import com.mir.ems.mqtt.Mqtt.publishThread;
import java.awt.Color;

@SuppressWarnings("serial")
public class DRScheduling extends JPanel {

	public int report_time;
	public int poll_time;

	public String ip;
	public int port;
	// rest
	public event_structure ev;

	public JTextField textField_ip;
	public JTextField textField_port;

	public JTextField textField_report;
	public JTextField textField_poll;

	// checkbox
	public JCheckBox drCheckBox;
	public JCheckBox rdrCheckBox;

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;

	private JTextField textField_target;

	Calendar calendar = Calendar.getInstance();

	public static String sYMD, eYMD, sTime, eTime;

	public static int indexOfEma;
	public String obsFlag;

	public static boolean obsEventFlag = false;

	int message_method;
	public static int value;
	public static String target_name;

	public static int state = 0;
	public String rest_message;

	public static String DR_MSG = null;
	public static int multiNum;

	public static int protocol;

	JSONObject drmsg = new JSONObject();

	public static LinkedList<Integer> gwNumQueue = new LinkedList<Integer>();

	public JComboBox<String> protocol_comboBox;

	public DRScheduling() {
		setBackground(Color.WHITE);
		// setBackgroundcO();
		setBounds(14, 60, 1491, 700);
		setLayout(null);

		JLabel lblNewLabel = new JLabel("EMA Scheduling");
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblNewLabel.setBounds(12, 545, 166, 21);
		add(lblNewLabel);

		// IP
		JLabel ip_label = new JLabel("IP");
		ip_label.setBounds(89, 500, 55, 15);
		add(ip_label);

		// PORT
		JLabel port_label = new JLabel("PORT");
		port_label.setBounds(477, 500, 55, 15);
		add(port_label);

		// Report time
		JLabel report_time_label = new JLabel("Report_Time");
		report_time_label.setBounds(912, 500, 100, 15);
		add(report_time_label);

		// polling time
		JLabel poll_time_label = new JLabel("Polling_Time");
		poll_time_label.setBounds(1200, 500, 100, 15);
		add(poll_time_label);

		// target
		JLabel target_label = new JLabel("EMA ID");
		target_label.setBounds(477, 545, 55, 15);
		add(target_label);

		JLabel lblNewLabel_1 = new JLabel("Target ID");
		lblNewLabel_1.setBounds(477, 583, 55, 15);
		add(lblNewLabel_1);

		// DR CheckBox (x,y,width,heigth)
		JLabel DR_Label = new JLabel("Control");
		DR_Label.setBounds(912, 545, 55, 15);
		add(DR_Label);

		// TIME?!
//		JLabel polling_time=new JLabel("Time");
//		polling_time.setBounds(1210, 545, 55, 15);
//		add(polling_time);

		JLabel lblNewLabel_2 = new JLabel("Start Time");
		lblNewLabel_2.setBounds(912, 586, 82, 15);
		add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("End Time");
		lblNewLabel_3.setBounds(912, 625, 57, 15);
		add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Value");
		lblNewLabel_4.setBounds(477, 625, 57, 15);
		add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Protocol");
		lblNewLabel_5.setBounds(89, 580, 57, 15);
		add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("Type");
		lblNewLabel_6.setBounds(89, 619, 57, 15);
		add(lblNewLabel_6);

		JButton btnNewButton = new JButton("Set");
		btnNewButton.setBounds(1351, 621, 97, 23);
		add(btnNewButton);

		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(217, 616, 149, 21);

		comboBox.addItem("BroadCast");
		comboBox.addItem("obsDREvent");
		comboBox.addItem("obsDREvent : 3");
		comboBox.addItem("obsDREvent : 9");
		comboBox.addItem("obsDREvent : 20");

		// comboBox.addItem("Multicast");
		// comboBox.addItem("Unicast");

		comboBox.addItem("MultiCast : 3");
		comboBox.addItem("MultiCast : 9");
		comboBox.addItem("MultiCast : 20");
		comboBox.addItem("MultiCast : 3-pull");
		comboBox.addItem("MultiCast : 9-pull");
		comboBox.addItem("MultiCast : 20-pull");
		// comboBox.addItem("MultiCast : 1-pull");

		comboBox.addItem("Push");
		comboBox.addItem("REST_EVENT");
		comboBox.addItem("REST_EMA_CONTROL");
		comboBox.addItem("EMS_EVENT");
//		comboBox.addItem("REST_CEMA_CONTROL");
		add(comboBox);
		//
		// JComboBox<String> Addressing = new JComboBox<String>();
		// Addressing.setBounds(809, 94, 104, 21);
		// Addressing.addItem("BroadCast");
		// Addressing.addItem("MultiCast : 3");
		// Addressing.addItem("MultiCast : 9");
		// Addressing.addItem("MultiCast : 20");
		// Addressing.addItem("Push");
		// Addressing.addItem("Pull");

		String date;

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);

		if (month < 10 && day < 10) {
			date = Integer.toString(year) + "0" + Integer.toString(month) + "0" + Integer.toString(day);
		} else if (month < 10 && !(day < 10)) {
			date = Integer.toString(year) + "0" + Integer.toString(month) + Integer.toString(day);
		} else if (!(month < 10) && day < 10) {
			date = Integer.toString(year) + Integer.toString(month) + "0" + Integer.toString(day);
		} else {
			date = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
		}

		String start_hour, start_minute;
		String end_hour, end_minute;

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int e_minute = calendar.get(Calendar.MINUTE) + 1;

		start_hour = Integer.toString(hour);
		start_minute = Integer.toString(minute);

		end_hour = Integer.toString(hour);
		end_minute = Integer.toString(e_minute);

		// ip
		textField_ip = new JTextField();
		textField_ip.setBounds(217, 500, 149, 21);
		add(textField_ip);
		textField_ip.setColumns(10);
		textField_ip.setEditable(true);

		// port
		textField_port = new JTextField();
		textField_port.setBounds(631, 500, 149, 21);
		add(textField_port);
		textField_port.setColumns(10);
		textField_port.setEditable(true);
		
		//emaID
		textField = new JTextField();
		textField.setBounds(631, 546, 149, 21);
		add(textField);
		textField.setColumns(10);
		textField.setEditable(false);
		
		
		

		// target
		textField_target = new JTextField();
		textField_target.setBounds(631, 577, 149, 21);
		add(textField_target);
		textField_target.setColumns(10);
		textField_target.setEditable(false);

	

		rdrCheckBox = new JCheckBox("AutoRDR");
		rdrCheckBox.setBounds(1034, 546, 83, 23);
		add(rdrCheckBox);
		rdrCheckBox.setEnabled(false);

		drCheckBox = new JCheckBox("AutoDR");
		drCheckBox.setBounds(1128, 546, 83, 23);
		add(drCheckBox);
		drCheckBox.setEnabled(false);

		// Report time
		textField_report = new JTextField();
		textField_report.setBounds(1034, 500, 50, 15);
		add(textField_report);

		// polling time
		textField_poll = new JTextField();
		textField_poll.setBounds(1300, 500, 50, 15);
		add(textField_poll);

		textField_1 = new JTextField();
		textField_1.setBounds(1034, 583, 82, 21);
		add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText(date);

		textField_2 = new JTextField();
		textField_2.setBounds(1128, 583, 55, 21);
		add(textField_2);
		textField_2.setColumns(10);
		textField_2.setText(start_hour);

		textField_3 = new JTextField();
		textField_3.setBounds(1195, 583, 64, 21);
		add(textField_3);
		textField_3.setColumns(10);
		textField_3.setText(start_minute);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(1034, 619, 82, 21);
		add(textField_4);
		textField_4.setText(date);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(1128, 619, 55, 21);
		add(textField_5);
		textField_5.setText(end_hour);

		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(1195, 619, 64, 21);
		add(textField_6);
		textField_6.setText(end_minute);

		textField_7 = new JTextField();
		textField_7.setText("100");
		textField_7.setColumns(10);
		textField_7.setBounds(631, 619, 149, 21);
		add(textField_7);

		textField_8 = new JTextField();
		textField_8.setText("REST");
//		textField_8.setText((String) protocol_comboBox.getSelectedItem());
		textField_8.setFont(new Font("Arial", Font.BOLD, 12));

		textField_8.setColumns(10);
		textField_8.setEditable(false);
		textField_8.setBounds(217, 577, 149, 21);
		add(textField_8);

		DRSchedulingGraph panel = new DRSchedulingGraph();
		panel.setBounds(12, 0, 1436, 450);
		add(panel);

		protocol_comboBox = new JComboBox<String>();
		protocol_comboBox.setBounds(217, 546, 149, 21);
		protocol_comboBox.addItem("MQTT");
		protocol_comboBox.addItem("COAP");
		protocol_comboBox.addItem("REST");

		add(protocol_comboBox);
		//
		// JLabel lblNewLabel_7 = new JLabel("New label");
		// panel.add(lblNewLabel_7);

		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (comboBox.getSelectedItem().toString().equals("BroadCast")) {
					textField.setEditable(false);
					message_method = 0;
				} else if (comboBox.getSelectedItem().toString().equals("REST_EVENT")) {
					message_method = 1;
					textField.setEditable(true);
					textField_target.setEditable(true);

					obsFlag = "non-obs";
				} else if (comboBox.getSelectedItem().toString().equals("Push")) {
					message_method = 2;
					textField.setEditable(true);
					obsFlag = "non-obs";
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 3")) {
					textField.setEditable(false);
					message_method = 3;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 9")) {
					textField.setEditable(false);
					message_method = 9;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 20")) {
					textField.setEditable(false);
					message_method = 20;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 3-pull")) {
					textField.setEditable(false);
					message_method = 4;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 9-pull")) {
					textField.setEditable(false);
					message_method = 10;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 20-pull")) {
					textField.setEditable(false);
					message_method = 21;
				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent")) {
					textField.setEditable(true);
					message_method = 22;
					obsFlag = "obs";
				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent : 3")) {
					textField.setEditable(false);
					message_method = 23;
					obsFlag = "obs";

				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent : 9")) {
					textField.setEditable(false);
					message_method = 24;
					obsFlag = "obs";

				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent : 20")) {
					textField.setEditable(true);
					message_method = 25;
					obsFlag = "obs";

				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 1-pull")) {
					textField.setEditable(false);
					message_method = 1;
				}

				else if (comboBox.getSelectedItem().toString().equals("REST_EMA_CONTROL")) {
					textField.setEditable(true);
					drCheckBox.setEnabled(true);
					rdrCheckBox.setEnabled(true);
					textField_target.setEditable(false);
					message_method = 2;
				}
				
				else if (comboBox.getSelectedItem().toString().equals("EMS_EVENT")) {
					textField.setEditable(true);
					drCheckBox.setEnabled(true);
					rdrCheckBox.setEnabled(true);
					textField_target.setEditable(false);
					message_method = 99;
				}
			}
		});

		btnNewButton.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String ema_name = textField.getText();
				value = Integer.parseInt(textField_7.getText());
				target_name = textField_target.getText();

				ip = textField_ip.getText();
				port = Integer.parseInt(textField_port.getText());

				if(!(textField_report.getText().equals(""))) {
				report_time = Integer.parseInt(textField_report.getText());
				}

				if(!(textField_poll.getText().equals(""))) {
				poll_time = Integer.parseInt(textField_poll.getText());
				}

				sYMD = textField_1.getText();
				sTime = textField_2.getText() + textField_3.getText();

				eYMD = textField_4.getText();
				eTime = textField_5.getText() + textField_6.getText();

				// indexOfEma = Integer.parseInt(ema_name);

				String process_value;

//				if (global.getProtocol_type_global().equals("MQTT")) {
				if (protocol_comboBox.getSelectedItem().toString().equals("MQTT")) {
					protocol = 1;
					// ProcessStatus value = new
					// ProcessStatus().getProcess_value();
					process_value = ProcessStatus.getProcess_value();

					if (message_method == 0) {

						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						String p_topic = "gw/" + "pushall" + "/DistributeEvent";
						String m_msg = drmsg.toJSONString();

						// Runnable p = new publishThread(p_topic, 0,
						// m_msg.getBytes());
						// Thread p7 = new Thread(p);
						// p7.start();
					} else if (message_method == 1) {

						if (global.emaProtocolCoAP_EventFlag.containsKey(ema_name)) {

							if (global.emaProtocolCoAP.get(ema_name).isPullModel()) {
								global.emaProtocolCoAP_EventFlag.get(textField.getText()).setEventFlag(true)
										.setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime + "11")).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime + "11"))
										.setThreshold(Double.parseDouble(textField_7.getText()));
							} else {

								System.err.println(ema_name + " is not supported Pull Model");
								return;
							}

						}

						// state = 1;
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						// DR_MSG = m_msg;
					} else if (message_method == 4) {
						for (int i = 1; i < message_method; i++) {
							gwNumQueue.add(i);
							// System.out.println("????????????????????");
							// System.out.println(gwNumQueue.get(i));
						}
						state = 2; // multicast flag

					} else if (message_method == 10) {
						for (int i = 1; i < message_method; i++) {
							gwNumQueue.add(i);
						}
						state = 2; // multicast flag

					} else if (message_method == 21) {
						for (int i = 1; i < message_method; i++) {
							gwNumQueue.add(i);
						}
						state = 2; // multicast flag

					} else if (message_method == 2) {

						if (!global.emaProtocolCoAP.get(ema_name).isPullModel()) {

							global.initiater.eventOccur(ema_name, 1, Integer.parseInt(sYMD),
									Integer.parseInt(sTime + "11"), Integer.parseInt(eYMD),
									Integer.parseInt(eTime + "11"), Double.parseDouble(textField_7.getText()));
						} else {

							System.err.println(ema_name + " is not supported Push Model");
							return;
						}
						//
						// indexOfEma = Integer.parseInt(ema_name);
						//
						// drmsg.put("Service", "DistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", 200);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String p_topic = "gw/" + String.valueOf(indexOfEma) +
						// "/oadr" + "/DistributeEvent";
						// String m_msg = drmsg.toJSONString();

						// Runnable p = new publishThread(p_topic, 0,
						// m_msg.getBytes());
						// Thread p7 = new Thread(p);
						// p7.start();
					} else if (message_method == 3) {
						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");
						//
						// for (int i = 1; i <= message_method; i++) {
						// String p_topic = "gw/" + i + "/oadr" +
						// "/DistributeEvent";
						// String m_msg = drmsg.toJSONString();
						//
						// Runnable p = new publishThread(p_topic, 0,
						// m_msg.getBytes());
						// Thread p7 = new Thread(p);
						// p7.start();
						// }
					} else if (message_method == 9) {
						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						for (int i = 1; i <= message_method; i++) {
							String p_topic = "gw/" + i + "/oadr" + "/DistributeEvent";
							String m_msg = drmsg.toJSONString();

							// Runnable p = new publishThread(p_topic, 0,
							// m_msg.getBytes());
							// Thread p7 = new Thread(p);
							// p7.start();
						}
					} else if (message_method == 20) {
						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						for (int i = 1; i <= message_method; i++) {
							String p_topic = "gw/" + i + "/oadr" + "/DistributeEvent";
							String m_msg = drmsg.toJSONString();

							// Runnable p = new publishThread(p_topic, 0,
							// m_msg.getBytes());
							// Thread p7 = new Thread(p);
							// p7.start();
						}
					}

				}

				else if (protocol_comboBox.getSelectedItem().toString().equals("COAP")) {

//				else if (global.getProtocol_type_global().equals("CoAP")) {
					protocol = 2;
					System.out.println("????????????????");
					if (message_method == 0) {

						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						String m_msg = drmsg.toJSONString();
						DR_MSG = m_msg;

					} else if (message_method == 1) {
						// String.valueOf(indexOfEma)
						// System.out.println("????????????");
						// if
						// (global.emaProtocolCoAP_EventFlag.containsKey(textField.getText()))
						// {
						// // System.out.println("?????????????");
						// //
						// System.out.println("??????????????????"+global.emaProtocolCoAP_EventFlag.get(textField.getText()).isEventFlag());
						// global.emaProtocolCoAP_EventFlag.get(textField.getText()).setEventFlag(true)
						// .setStartYMD(Integer.parseInt(sYMD)).setStartTime(Integer.parseInt(sTime))
						// .setEndYMD(Integer.parseInt(eYMD)).setEndTime(Integer.parseInt(eTime));
						// }

						// global.emaProtocolCoAP_EventFlag.get(key)

//						System.out.println("============");
//						System.out.println(textField.getText());
//						System.out.println(sYMD);
//						System.out.println(sTime + "11");
//						System.out.println(eYMD);
//						System.out.println(eTime + "11");
//						System.out.println(textField_7.getText());
//						System.out.println("============");

						if (global.emaProtocolCoAP_EventFlag.containsKey(ema_name)) {

							if (global.emaProtocolCoAP.get(ema_name).isPullModel()) {
								global.emaProtocolCoAP_EventFlag.get(textField.getText()).setEventFlag(true)
										.setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime + "11")).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime + "11"))
										.setThreshold(Double.parseDouble(textField_7.getText()));
							} else {

								System.err.println(ema_name + " is not supported Pull Model");
								return;
							}

						}

						// System.out.println("????????????????????????");
						// indexOfEma = Integer.parseInt(ema_name);
						// state = 1;
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", 200);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						// DR_MSG = m_msg;
						// state = 1;

					} else if (message_method == 2) {
						System.out.println("????????????????");
						indexOfEma = Integer.parseInt(ema_name);
						state = 1;
						JSONObject drmsg = new JSONObject();

						drmsg.put("Service", "oadrDistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");
						String m_msg = drmsg.toJSONString();
						DR_MSG = m_msg;
						// for(int i=0; i<=20; i++){
						InetAddress gatewayIp = MainFrame.gateway_db.gateway_list.get(1).ip_addr;
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);

						String ipParse[] = gatewayIp.toString().split("/");
						String ipaddr = ipParse[1];
						CoAPClient coapClient = new CoAPClient(ipaddr, "oadrDistributeEvent", m_msg);
						// coapClient.
						// }
					} else if (message_method == 22) {

//						System.out.println("????????????");C
						// COAP OBS

						if (global.obs_emaProtocolCoAP_EventFlag.containsKey(textField.getText())) {
							// obsEventFlag = true;
							global.obs_emaProtocolCoAP_EventFlag.get(textField.getText()).setEventFlag(true)
									.setStartYMD(Integer.parseInt(sYMD)).setStartTime(Integer.parseInt(sTime + "11"))
									.setEndYMD(Integer.parseInt(eYMD)).setEndTime(Integer.parseInt(eTime + "11"))
									.setThreshold(Double.parseDouble(textField_7.getText()));
						}
						//
						// indexOfEma = Integer.parseInt(ema_name);
						//
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						//
						// DR_MSG = m_msg;
						// global.eventStatus = true;
					} else if (message_method == 23) {

//						System.out.println("?????????????");
						// System.out.println(
						// "??????????????????" +
						// global.emaProtocolCoAP_EventFlag.get(textField.getText()).isEventFlag());

						// obsEventFlag = true;
						for (multiNum = 1; multiNum < 4; multiNum++) {

							if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum))) {

								global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
										.setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime)).setThreshold(value);
							}
						}

						// System.out.println(
						// "??????????????????" +
						// global.emaProtocolCoAP_EventFlag.get(textField.getText()).isEventFlag());

						/*
						 * COAP
						 */
						// for (multiNum = 1; multiNum < 4; multiNum++) {
						// if
						// (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum)))
						// {
						//
						// global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum)).setEventFlag(true)
						// .setStartYMD(Integer.parseInt(sYMD)).setStartTime(Integer.parseInt(sTime))
						// .setEndYMD(Integer.parseInt(eYMD)).setEndTime(Integer.parseInt(eTime));
						//
						// System.out.println("????????????!"
						// +
						// global.emaProtocolCoAP_EventFlag.get(String.valueOf(multiNum)).isEventFlag());
						// }
						// }
						//
						// for (multiNum = 1; multiNum < 4; multiNum++) {
						// indexOfEma = multiNum;
						// /*
						// * maximal matching algorithm
						// */
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						//
						// DR_MSG = m_msg;
						// global.eventStatus = true;
						// }
					} else if (message_method == 24) {

						for (multiNum = 1; multiNum < 10; multiNum++) {

							if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum))) {

								global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
										.setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime)).setThreshold(value);
							}
						}

						// for (multiNum = 1; multiNum < 10; multiNum++) {
						// if
						// (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum)))
						// {
						//
						// global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
						// .setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
						// .setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
						// .setEndTime(Integer.parseInt(eTime));
						//
						// System.out.println("????????????! 99999999999"
						// +
						// global.emaProtocolCoAP_EventFlag.get(String.valueOf(multiNum)).isEventFlag());
						// }
						// }

					} else if (message_method == 25) {
						for (multiNum = 1; multiNum < 21; multiNum++) {

							if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum))) {

								global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
										.setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime)).setThreshold(value);
							}
						}

						// for (multiNum = 1; multiNum < 21; multiNum++) {
						// indexOfEma = multiNum;
						//
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						//
						// DR_MSG = m_msg;
						// global.eventStatus = true;
						// }
					}
				}

				else if (protocol_comboBox.getSelectedItem().toString().equals("REST")) {

//					else if (global.getProtocol_type_global().equals("CoAP")) {
					protocol = 3;
					System.out.println("REST");
					if (message_method == 1) {

						System.out.println("EMA_NAME" + ema_name);
						System.out.println("VALUE" + value);

						System.err.println("test@@@@@@@@@@@@@@@");

//						ev.setEMAID(ema_name);

						global.ema_name = ema_name;
						global.value = value;
						global.target_name = target_name;

//						System.out.println("ev.getEMAID"+ev.getEMAID());
//						ev.setThreshold(value);

						EMA_Event test = new EMA_Event(ip, port);

						test.rest_event();

//						FrontController ft = new FrontController();
//						ft.changeRegisteredVenSeqNum(ema_name);
//						Global.registerVEN.get(ema_name).setThreshold(value);

					}

//					rest_message = "DR_Control";
					if (message_method == 2) {

						System.out.println("EMA_NAME" + ema_name);

//						ev.setEMAID(ema_name);

						global.ema_name = ema_name;

						global.dr_control = drCheckBox.isSelected();
						global.rdr_control = rdrCheckBox.isSelected();

						global.rest_reportTime = report_time;
						global.resT_pollTime = poll_time;

						System.out.println("dr_Control" + global.dr_control);
						System.out.println("rdr_Control" + global.rdr_control);

						if (global.ema_name.contains("SERVER")) {

							System.err.println("rest_IP, port" + ip + " " + port);
							EMA_Event test = new EMA_Event(ip, port);
							test.dr_control();
						}

						else if (global.ema_name.contains("CLIENT")) {
							EMA_Event test = new EMA_Event(ip, port);
							test.dr_control();
						}

					}
					
					
					if (message_method == 99) {

						System.out.println("EMA_NAME" + ema_name);
						System.out.println("VALUE" + value);

						System.err.println("test@@@@@@@@@@@@@@@");

//						ev.setEMAID(ema_name);

						global.ema_name = ema_name;
						global.value = value;
						global.target_name = target_name;

//						System.out.println("ev.getEMAID"+ev.getEMAID());
//						ev.setThreshold(value);

						EMA_Event test = new EMA_Event(ip, port);

						test.ems_event();

//						FrontController ft = new FrontController();
//						ft.changeRegisteredVenSeqNum(ema_name);
//						Global.registerVEN.get(ema_name).setThreshold(value);

					}
					

				}

				//
				// Minute a ;
				// a = new Minute minute;
				Minute sm = new Minute(Integer.parseInt(textField_3.getText()), Integer.parseInt(textField_2.getText()),
						calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
				Minute em = new Minute(Integer.parseInt(textField_6.getText()), Integer.parseInt(textField_5.getText()),
						calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

				DRSchedulingGraph.updateScheduling(indexOfEma, sm, em, value);

			}
		});
	}

	public String DR_MSG() {
		state = 2;
		return DR_MSG;
	}

	public int VEN_ID() {
		return indexOfEma;
	}

	public int state() {
		System.out.println("DR State: " + state);
		return state;
	}
}
