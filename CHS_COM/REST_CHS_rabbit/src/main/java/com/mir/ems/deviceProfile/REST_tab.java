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
public class REST_tab extends JPanel {
	public static DefaultTableModel ema_table_model;

	public REST_tab() {
		String[] ven_colHeadings = { " ", "EMA Name", "Protocol", "Power", "threshold" };
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
		timer.scheduleAtFixedRate(chartUpdaterTask, 2000, 2000);

	}

	public void modify_EMA_table() {

		int ema_rows_num = EMA_tab_temp.ema_table_model.getRowCount();
		for (int i = ema_rows_num - 1; i >= 0; i--) {
			EMA_tab_temp.ema_table_model.removeRow(i);
		}



		Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();

		while (keys.hasNext()) {
			String key = keys.next();

			EMA_tab_temp.ema_table_model
					.addRow(new Object[] { false, key, global.emaProtocolCoAP.get(key).getProtocol(),
							global.emaProtocolCoAP.get(key).getqOs(), global.emaProtocolCoAP.get(key).getEmaCNT(),
							global.emaProtocolCoAP.get(key).getPower(), global.emaProtocolCoAP.get(key).getMaxValue(),
							global.emaProtocolCoAP.get(key).getMinValue(), global.emaProtocolCoAP.get(key).getMargin(),
							global.emaProtocolCoAP.get(key).getCustomerPriority() });

		}


	}

	public static void update_EMA_table() {

		ema_table_model.fireTableDataChanged();
	}
}
