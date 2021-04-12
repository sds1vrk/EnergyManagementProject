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
import com.mir.vtn.global.Global;

//import com.mir.ems.mqtt.Mqtt.publishThread;
import java.awt.Color;

import com.mir.vtn.controller.FrontController;


@SuppressWarnings("serial")
public class DRScheduling2 extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;

	Calendar calendar = Calendar.getInstance();

	public static String sYMD, eYMD, sTime, eTime;

	public static int indexOfEma;
	public String obsFlag;

	public static boolean obsEventFlag = false;

	int message_method;
	public static int value;
	public static int state = 0;

	public static String DR_MSG = null;
	public static int multiNum;

	public static int protocol;

	JSONObject drmsg = new JSONObject();

	public static LinkedList<Integer> gwNumQueue = new LinkedList<Integer>();

	public JComboBox<String> protocol_comboBox;
	
	public DRScheduling2() {
		setBackground(Color.WHITE);
		// setBackgroundcO();
		setBounds(14, 60, 1491, 700);
		setLayout(null);

		JLabel lblNewLabel = new JLabel("EMA Scheduling");
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblNewLabel.setBounds(12, 545, 166, 21);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("VEN ID");
		lblNewLabel_1.setBounds(477, 583, 55, 15);
		add(lblNewLabel_1);

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
		comboBox.addItem("Pull");
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

		textField = new JTextField();
		textField.setBounds(631, 577, 149, 21);
		add(textField);
		textField.setColumns(10);
		textField.setEditable(true);

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
		textField_8.setFont(new Font("Arial", Font.BOLD, 12));

		textField_8.setColumns(10);
		textField_8.setEditable(false);
		textField_8.setBounds(217, 577, 149, 21);
		add(textField_8);

		DRSchedulingGraph panel = new DRSchedulingGraph();
		panel.setBounds(12, 0, 1436, 529);
		add(panel);
		
		protocol_comboBox = new JComboBox<String>();
		protocol_comboBox.setBounds(217, 546, 149, 21);
		protocol_comboBox.addItem("MQTT");
		protocol_comboBox.addItem("COAP");
		
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
				} else if (comboBox.getSelectedItem().toString().equals("Pull")) {
					message_method = 1;
					textField.setEditable(true);
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
			}
		});

		sYMD = textField_1.getText();
		sTime = textField_2.getText() + textField_3.getText();

		eYMD = textField_4.getText();
		eTime = textField_5.getText() + textField_6.getText();

		btnNewButton.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String ema_name = textField.getText();
				value = Integer.parseInt(textField_7.getText());
			
				
//				if(protocol_comboBox.getSelectedItem().toString().equals("REST")){

//				else if (global.getProtocol_type_global().equals("CoAP")) {
					protocol = 2;
					FrontController ft=new FrontController();
					
					
					System.out.println("EMA_NAME"+ema_name);
					System.out.println("VALUE"+value);
					
					
					ft.changeRegisteredVenSeqNum(ema_name);
					Global.registerVEN.get(ema_name).setThreshold(value);
					
					
					
//				}

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
