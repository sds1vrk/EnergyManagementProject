package com.mir.AMI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;

import com.mir.ems.globalVar.global;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class meter_tab extends JPanel implements ActionListener, MouseListener {

	public static JTable meter_table;
	public static MeterDatabase meter_db;
	public static DefaultTableModel meter_table_model;
	public JComboBox<String> comboBox;
	private JTextField textField;

	// public int intervalTime = 1000;

	public meter_tab() {

		String[] led_colHeadings = { " ", "Meter ID", "DCU ID", "Current Energy", "Total Energy" };
		String[][] data = {};
		meter_table_model = new DefaultTableModel(data, led_colHeadings);

		// JPanel panel = new JPanel();
		setBorder(null);
		setForeground(Color.WHITE);
		meter_table = new JTable() {
			public java.lang.Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 2:
				case 3:
					return String.class;
				default:
					return Integer.class;
				}
			};
		};
		meter_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		meter_table.setBorder(null);
		meter_table.setFont(new Font("Arial", Font.PLAIN, 12));
		meter_table.setForeground(Color.DARK_GRAY);
		setLayout(null);

		meter_table.setModel(meter_table_model);
		meter_table.setBounds(0, 0, 503, 240);

		// JScrollPane sc = new JScrollPane(table_2);
		JScrollPane scrollPane = new JScrollPane(meter_table);
		scrollPane.setBounds(0, 0, 618, 233);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(113, 243, 175, 21);
		comboBox.addItem("On-Demand");
		comboBox.addItem("Idle");
		comboBox.addActionListener(this); // combobox on/off mode

		add(comboBox);
		add(scrollPane);

		JLabel lblNewLabel = new JLabel("Control");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel.setBounds(59, 246, 57, 15);
		add(lblNewLabel);

		JLabel lblIntervalTime = new JLabel("Interval Time");
		lblIntervalTime.setFont(new Font("Arial", Font.BOLD, 12));
		lblIntervalTime.setBounds(328, 246, 85, 15);
		add(lblIntervalTime);

		textField = new JTextField();
		textField.setBounds(425, 243, 116, 21);
		add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("SET");
		btnNewButton.setBounds(556, 242, 62, 23);
		add(btnNewButton);



		meter_db = new MeterDatabase();
	}

	public static void modify_meter_table() {
		int meter_rows_num = meter_tab.meter_table_model.getRowCount();

		for (int i = meter_rows_num - 1; i >= 0; i--) {
			meter_table_model.removeRow(i);
		}
		int ledListSize = meter_db.meter_list.size();
		for (int i = 0; i < ledListSize; i++) {
			meter_table_model
					.addRow(new Object[] { false, meter_db.meter_list.get(i).meterID, meter_db.meter_list.get(i).dcuID,
							meter_db.meter_list.get(i).curEnergy, meter_db.meter_list.get(i).activeEnergy });
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}




}
