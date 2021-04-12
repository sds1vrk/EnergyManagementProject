package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mir.ems.database.EMADatabase;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.HandleEnergyReport;

@SuppressWarnings("serial")
public class EMA_tab_temp extends JPanel {
	public static DefaultTableModel ema_table_model;

	public EMA_tab_temp() {
		String[] ven_colHeadings = { " ", "EMA Name", "Protocol", "QoS", "emaCNT", "Power", "MaxPower", "MinPower",
				"Threshold", "Priority" };
		String[][] data = {};
		ema_table_model = new DefaultTableModel(data, ven_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		JTable ven_table = new JTable() {
			public java.lang.Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				case 3:
					return String.class;
				case 5:
					return String.class;
				default:
					return String.class;
				}
			};
		};
		ven_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ven_table.setBorder(null);
		ven_table.setFont(new Font("Arial", Font.PLAIN, 12));
		ven_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		ven_table.setModel(ema_table_model);
		ven_table.setBounds(0, 0, 615, 155);

		JScrollPane scrollPane = new JScrollPane(ven_table);
		scrollPane.setBounds(0, 0, 715, 140);
		add(scrollPane);

		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {

				modify_EMA_table();

			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 2000, 10000);
//		timer.scheduleAtFixedRate(chartUpdaterTask, 5000);
//		timer.schedule(chartUpdaterTask, 10000);

	}

	public void modify_EMA_table() {

		int ema_rows_num = EMA_tab_temp.ema_table_model.getRowCount();
		
		System.err.println("ema_rows_num:"+ema_rows_num);
		for (int i = ema_rows_num - 1; i >= 0; i--) {
			EMA_tab_temp.ema_table_model.removeRow(i);
		}

		// Iterator<String> keys = global.openADRHashMap.keySet().iterator();
		// String arr;
		// String[] parseArr;
		// while (keys.hasNext()) {
		// String key = keys.next();
		//
		// arr = global.openADRHashMap.get(key).toString();
		// parseArr = arr.split("/");
		//
		// EMA_tab_temp.ema_table_model.addRow(new Object[] { false, key,
		// parseArr[1], parseArr[4], parseArr[5],
		// parseArr[12], parseArr[13], parseArr[14], parseArr[9], parseArr[8]
		// });
		// }

//		Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();

		


		Iterator<String> keys = global.rest_SemaProfile.keySet().iterator();
		
		while (keys.hasNext()) {
			String key = keys.next();

			EMA_tab_temp.ema_table_model
					.addRow(new Object[] { false, key, "REST",
							"CONTOROLLER", global.rest_SemaProfile.get(key).getEmaCNT(),
							global.rest_SemaProfile.get(key).getMaxValue(), global.rest_SemaProfile.get(key).getMaxValue(),
							global.rest_SemaProfile.get(key).getMinValue(), global.rest_SemaProfile.get(key).getThreshold(),
							"1" });
			
			

		}

		// keys = global.emaProtocolCoAP_Device.keySet().iterator();
		// while(keys.hasNext()){
		// String key = keys.next();
		// System.out.println(key+":"+
		// global.emaProtocolCoAP_Device.get(key).getDeviceType());
		// }
	}
//
	public static void update_EMA_table() {

		ema_table_model.fireTableDataChanged();
	}
}
