package com.mir.ems.GUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;

import java.awt.Font;

import com.mir.ems.globalVar.RegisteredFileReader;
import com.mir.ems.globalVar.global;
import com.mir.ems.main.Connection;
import com.mir.ems.main.EmsMainClass;

import java.awt.Color;
import java.awt.Component;

import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Initial extends JFrame {
	private EmsMainClass main;
	private JButton btnLogin;
	private JButton btnInit;
	private JPasswordField passText;
	private JTextField userText;
	private boolean bLoginCheck;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField qos_textField;
	private JTextField textField_2;
	private JTextField textField_3;
	
	public Initial() {
		// setting
		setTitle("MIREnergy Management System");
		setSize(326, 614);
		setResizable(false);
		setLocation(800, 450);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		// panel
		JPanel panel = new JPanel();
		placeLoginPanel(panel);

		// add
		getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(12, 66, 298, 118);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel userLabel = new JLabel("Account");
		userLabel.setBounds(38, 35, 80, 25);
		panel_1.add(userLabel);
		userLabel.setFont(new Font("Arial", Font.BOLD, 13));

		JLabel passLabel = new JLabel("Passcode");
		passLabel.setBounds(38, 70, 80, 25);
		panel_1.add(passLabel);
		passLabel.setFont(new Font("Arial", Font.BOLD, 13));

		userText = new JTextField(20);
		userText.setText("test");
		userText.setBounds(141, 35, 123, 25);
		panel_1.add(userText);

		passText = new JPasswordField(20);
		passText.setToolTipText("");
		passText.setText("1234");
		passText.setBounds(141, 70, 123, 25);
		panel_1.add(passText);

		JLabel lblUser = new JLabel("User ");
		lblUser.setBounds(12, 10, 57, 15);
		panel_1.add(lblUser);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(12, 194, 298, 332);
		panel.add(panel_2);
		panel_2.setLayout(null);

		final JLabel lblNewLabel = new JLabel("IP Address");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel.setBounds(39, 75, 76, 25);
		panel_2.add(lblNewLabel);

		final JLabel lblNewLabel_1 = new JLabel("Port");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel_1.setBounds(39, 110, 76, 25);
		panel_2.add(lblNewLabel_1);

		textField = new JTextField();
		textField.setText("166.104.28.51");
		textField.setBounds(141, 75, 123, 25);
		panel_2.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setText("1883");
		textField_1.setBounds(141, 110, 123, 25);
		panel_2.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Protocol");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel_2.setBounds(38, 40, 76, 15);
		panel_2.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Connection Info");
		lblNewLabel_3.setBounds(12, 10, 103, 15);
		panel_2.add(lblNewLabel_3);

		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setForeground(Color.BLACK);
		comboBox.setBackground(Color.WHITE);
		comboBox.setBounds(141, 35, 123, 21);

		comboBox.addItem("REST");
		comboBox.addItem("RABBIT");
//		comboBox.addItem("UDP");
		comboBox.addItem("BOTH");

		comboBox.setSelectedIndex(0);
		global.setProtocol_type_global("REST");


		panel_2.add(comboBox);
		
		JLabel lblQos = new JLabel("QOS");
		lblQos.setFont(new Font("Arial", Font.BOLD, 13));
		lblQos.setBounds(39, 145, 76, 25);
		panel_2.add(lblQos);
		
		qos_textField = new JTextField();
		qos_textField.setText("0");
		qos_textField.setColumns(10);
		qos_textField.setBounds(141, 145, 123, 25);
		panel_2.add(qos_textField);
		
		JLabel lblUdpServer = new JLabel("UDP Server");
		lblUdpServer.setFont(new Font("Arial", Font.BOLD, 13));
		lblUdpServer.setBounds(39, 238, 76, 25);
		panel_2.add(lblUdpServer);
		
		textField_2 = new JTextField();
		textField_2.setText("192.168.1.199");
		textField_2.setColumns(10);
		textField_2.setBounds(141, 238, 123, 25);
		panel_2.add(textField_2);
		
		JLabel lblUdpPort = new JLabel("UDP Port");
		lblUdpPort.setFont(new Font("Arial", Font.BOLD, 13));
		lblUdpPort.setBounds(39, 273, 76, 25);
		panel_2.add(lblUdpPort);
		
		textField_3 = new JTextField();
		textField_3.setText("12346");
		textField_3.setColumns(10);
		textField_3.setBounds(141, 273, 123, 25);
		panel_2.add(textField_3);

		
		java.net.URL url = Initial.class.getResource("/IMAGE/123.png");

		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon(url));
		lblNewLabel_4.setBounds(146, 0, 182, 65);
		lblNewLabel_4.setPreferredSize(new Dimension(100, 100));
		panel.add(lblNewLabel_4);
		
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(comboBox.getSelectedItem().toString().equals("REST")){
					lblNewLabel.setVisible(true);
					lblNewLabel_1.setVisible(true);
					textField.setVisible(true);
					textField_1.setVisible(true);
					
					global.setProtocol_type_global("REST");
				}
				else if(comboBox.getSelectedItem().toString().equals("RABBIT")){
					
					lblNewLabel.setVisible(false);
					lblNewLabel_1.setVisible(false);
					textField.setVisible(false);
					textField_1.setVisible(false);
					
					global.setProtocol_type_global("RABBIT");
				}
			
				else if(comboBox.getSelectedItem().toString().equals("BOTH")){
					
					lblNewLabel.setVisible(true);
					lblNewLabel_1.setVisible(true);
					textField.setVisible(true);
					textField_1.setVisible(true);
					
					global.setProtocol_type_global("BOTH");
				}
			}
		});

		
		userText.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				isLoginCheck();
				
			}
		});
		
		passText.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				isLoginCheck();
			}
		});
		
		textField.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				isLoginCheck();
			}
		});
		
		textField_1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				isLoginCheck();
			}
		});

		// visiible
		setVisible(true);
	}

	public void placeLoginPanel(JPanel panel) {
		panel.setLayout(null);

		btnInit = new JButton("Reset");
		btnInit.setBounds(50, 536, 100, 25);
		panel.add(btnInit);
		btnInit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				userText.setText("");
				passText.setText("");
			}
		});

		btnLogin = new JButton("Confirm");
		btnLogin.setBounds(163, 536, 100, 25);
		panel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				isLoginCheck();
			}
		});
	}

	public void isLoginCheck() {
		
//		if(comboBox.)
		if (userText.getText().equals("test") && new String(passText.getPassword()).equals("1234")) {
			bLoginCheck = true;
			
			// �α��� �����̶�� �Ŵ���â �ٿ��
			if (isLogin()) {
				global.tempIP = textField.getText();
				global.tempPort = textField_1.getText();
				
				global.udpIP = textField_2.getText();
				global.udpPort = Integer.parseInt(textField_3.getText());
				

				global.qos = Integer.parseInt(qos_textField.getText());
				
				

				main.showFrameTest(); // ����â �޼ҵ带 �̿��� â�ٿ��x
			}
		} else {
			JOptionPane.showMessageDialog(null, "Faild");
		}
	}

	// mainClass�� ����
	public void setMain(EmsMainClass main) {
//		System.out.println("durl");
		this.main = main;
	}

	public boolean isLogin() {
		return bLoginCheck;
	}
}
