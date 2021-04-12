package com.mir.ems.Graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.markers.SeriesMarkers;

import com.mir.ems.globalVar.global;
import com.mir.ems.main.Connection;
import com.mir.ems.mqtt.HandleEnergyReport;

import java.util.*;
import java.text.*;

import java.awt.Color;;

@SuppressWarnings("serial")
public class EnergyGraph3_Best extends JPanel implements ExampleChart<XYChart> {
	public XYChart xyChart;
	

//	private static final int BASEWATT = 30;
	private static final int BASEWATT = 15000;
//	private static final int BASEWATT = 7;
	private static final int YAXIS_TRANSFERENCE = 2;
//	private static final int PERIOD = 384;
	private static final int PERIOD = 500;
	private static final int TIMEGAP = 2000;

	static double phase = 0;
	public static int val = 0;

	public static double totalEMAEnergy = 0;

	public static boolean overEnergy = false;

	public static List<Date> xData = new ArrayList<Date>();
	public static List<Double> yData = new ArrayList<Double>();

	public static List<Date> totalEMAxData = new ArrayList<Date>();
	public static List<Double> totalEMAyData = new ArrayList<Double>();

	public static List<Double> totalEnergy = new ArrayList<Double>();

	public EnergyGraph3_Best() {

		final XYChart chart = getChart();
		
		
		setBounds(14, 60, 1467, 700);
		setLayout(null);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		XChartPanel chartPanel = new XChartPanel(chart);
		
		chartPanel.setBackground(Color.WHITE);
		chartPanel.setBounds(0, 0, 1467, 700);
		
		add(chartPanel);

		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {

				updateData();

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					public void run() {

						Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();

						while (it.hasNext()) {
							String key = it.next();
							global.emaProtocolCoAP.get(key).setMargin(global.THRESHOLD / global.EXPERIMENTNUM);
						}

//						if (totalEMAEnergy >= (global.THRESHOLD / 100) * global.EXPERIMENTPERCENT) {
							
						if (totalEMAEnergy >= BASEWATT) {	

							if (overEnergy) {
								chart.getStyler().setPlotBackgroundColor(Color.RED);

							} else {
								chart.getStyler().setPlotBackgroundColor(Color.WHITE);
							}

						} else {
							chart.getStyler().setPlotBackgroundColor(Color.WHITE);

						}

						repaint();
						
						
						if (totalEMAEnergy >= BASEWATT) {	

							if (overEnergy) {
								overEnergy = false;
							} else {
								overEnergy = true;
							}
							global.EXPERIMENTAUTODR=true;
							if (global.EXPERIMENTAUTODR) {
//								int k = global.autoDRCOUNT++;
								doEmergencyControl();
							}
						}

					}
				});
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 2000, TIMEGAP);
	}

	@SuppressWarnings("unchecked")
	public XYChart getChart() {

		// Create Chart
		xyChart = new XYChartBuilder().width(500).height(400).title("[Real-time] TOTAL Energy Usage Value Monitoring")
				.theme(ChartTheme.Matlab).build();
		
		xyChart.getStyler().setChartTitlePadding(15);

		xyChart.getStyler().setXAxisLabelRotation(60);
		
		xyChart.getStyler().setDatePattern("MM-dd HH:mm:ss");
		xyChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		xyChart.getStyler().setPlotMargin(0);
		
		xyChart.getStyler().setPlotContentSize(.95);
		xyChart.setXAxisTitle("Time");
		xyChart.setYAxisTitle("Energy Usage Value (Unit :KW)");
		

		XYSeries thresholdSeries = xyChart.addSeries("Threshold", getAXIS(), getYAXIS());

		thresholdSeries.setMarker(SeriesMarkers.NONE);
		thresholdSeries.isEnabled();
		
		// xyChart.addSeries("Threshold", getAXIS(), getYAXIS());
	
		
		xyChart.addSeries("EMA's Energy", getEMAAXIS(), getEMAYAXIS());
		
	

		return xyChart;
	}

	public void updateData() {
		
		
		//여기 추가
		xyChart.getStyler().setMarkerSize(1);
//		xyChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
	
		
		
		xyChart.updateXYSeries("Threshold", getAXIS(), getYAXIS(), null);
		xyChart.updateXYSeries("EMA's Energy", getEMAAXIS(), getEMAYAXIS(), null);

	}

	private List<Date> getAXIS() {

		long now = System.currentTimeMillis();
		Date date = new Date(now);
		xData.add(date);

		return xData;
	}

	private List<Double> getYAXIS() {

//		double radians = phase + (2 * Math.PI / 100 * val);
		
		
//		double radians = phase + (100*val);
		
		double radians = phase;
		val += 1;
//		phase += ((2 * Math.PI * 2) / 20.0) / PERIOD;

//		global.THRESHOLD = (YAXIS_TRANSFERENCE * Math.sin(radians) + BASEWATT) * 100;
		
		global.THRESHOLD = BASEWATT;

		global.AVAILABLE_THRESHOLD = (global.THRESHOLD - (global.THRESHOLD / global.RESERVE_THRESHOLD_PERCENTAGE));
		global.RESERVE_THRESHOLD = global.THRESHOLD - global.AVAILABLE_THRESHOLD;

		yData.add(YAXIS_TRANSFERENCE * Math.sin(radians) + BASEWATT);
		
//		yData.add(BASEWATT);
		

		return yData;
	}

	private List<Date> getEMAAXIS() {

		long now = System.currentTimeMillis();
		Date date = new Date(now);
		totalEMAxData.add(date);

		return totalEMAxData;
	}

	private List<Double> getEMAYAXIS() {

		Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();

		totalEMAEnergy = 0;
		while (keys.hasNext()) {
			String key = keys.next();

//			totalEMAEnergy = totalEMAEnergy + (global.emaProtocolCoAP.get(key).getPower() / 100);
			
			totalEMAEnergy = totalEMAEnergy + (global.emaProtocolCoAP.get(key).getPower());
			
			
			
			if (totalEMAEnergy >= BASEWATT) {


				EnergyGrpah_Exceed_log log = new EnergyGrpah_Exceed_log();
				
				long time = System.currentTimeMillis(); 

				SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

				String str = dayTime.format(new Date(time));

//				System.out.println(str);
				
				log.write_log("Current TIME: "+str +" Total EMA Energy USED:" +totalEMAEnergy);

			}
			
		}

		global globalVar = new global();
		globalVar.setTotalEnergyUsage(totalEMAEnergy);

		
		totalEMAyData.add(totalEMAEnergy);

		return totalEMAyData;
	}

	public void doEmergencyControl() {

		Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();

		while (keys.hasNext()) {
			String key = keys.next();

//			double power = global.emaProtocolCoAP.get(key).getPower();
//			double threshold = power * 0.8;
			double threshold=BASEWATT;

//			double threshold = global.THRESHOLD / global.EXPERIMENTNUM;

			System.err.println("HERE IS threshold " + threshold);

			Calendar now = Calendar.getInstance();

			int sYear = now.get(Calendar.YEAR);
			int sMonth = now.get(Calendar.MONTH) + 1;
			int sDate = now.get(Calendar.DATE);
			String strYMD = sYear + "" + sMonth + "" + sDate;
			int sHour = now.get(Calendar.HOUR_OF_DAY);
			int sMin = now.get(Calendar.MINUTE);

			String sTime = sHour + "" + sMin + "" + "11";
			String eTime = (sHour + 1) + "" + sMin + "" + "11";

			// PULL MODEL
			if (global.emaProtocolCoAP.get(key).isPullModel()) {
				global.emaProtocolCoAP_EventFlag.get(key).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
						.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
						.setEndTime(Integer.parseInt(eTime)).setThreshold(threshold);
			}

			// PUSH MODEL
			else if (!global.emaProtocolCoAP.get(key).isPullModel()) {

				if (global.emaProtocolCoAP.get(key).getProtocol().equals("MQTT")) {
					global.initiater.eventOccur(key, 1, Integer.parseInt(strYMD), Integer.parseInt(sTime),
							Integer.parseInt(strYMD), Integer.parseInt(eTime), threshold);
				} else {

					global.obs_emaProtocolCoAP_EventFlag.get(key).setEventFlag(true)
							.setStartYMD(Integer.parseInt(strYMD)).setStartTime(Integer.parseInt(sTime + "11"))
							.setEndYMD(Integer.parseInt(strYMD)).setEndTime(Integer.parseInt(eTime + "11"))
							.setThreshold(threshold);

				}
			}

			//
			//
			//
			//
			// global.emaProtocolCoAP_EventFlag.get(key).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
			// .setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
			// .setEndTime(Integer.parseInt(eTime)).setThreshold(threshold);

		}

	}
}
