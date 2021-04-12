package com.mir.ems.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.LegendPosition;


public class testing implements ExampleChart<CategoryChart> {

	static Object[][] arr = new Object[20][2];

	
	public static void main(String[] args) {

//		for (int i=0; i<20; i++){
//			arr[i][0] = "CEMA"+i;
//		}
//		
//		for (int i=0; i<20; i++){
//			System.out.println("ARR[][0]"+ arr[i][0]);
//		}
//		
		ExampleChart<CategoryChart> exampleChart = new testing();
		CategoryChart chart = exampleChart.getChart();
		new SwingWrapper<CategoryChart>(chart).displayChart();
	}

	@Override
	public CategoryChart getChart() {

		

		
		
//		
		// Create Chart
		CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Score Histogram")
				.xAxisTitle("Date").yAxisTitle("Reserved Threshold").build();

		// Customize Chart
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		System.out.println(chart.getStyler().getLegendSeriesLineLength());
		
//		System.out.println(chart.getStyler().);
		
		
		chart.getStyler().setAvailableSpaceFill(.96);
//		chart.getStyler().setAnnotio
		chart.getStyler().setDatePattern("yyyy-MM-dd");
		chart.getStyler().setOverlapped(true);
		
//		chart.getStyler().setYAxisMax(200.0);
		
//		LegendPosition legendPosition;
//		legendPosition.InsideNW;
//		
		
		chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
//		chart.getStyler().setLegendVisible(false);
		chart.getStyler().setHasAnnotations(true);
//		chart.getStyler().ssetAnnotationType(AnnotationStyle.Percentage);

		Color color = new Color(255);
		
		
		// chart.getStyler().set(true);

		// Series
		Histogram histogram1 = new Histogram(getGaussianData(10000), 20, -20, 20);
		Histogram histogram2 = new Histogram(getGaussianData(5000), 20, -20, 20);
		Histogram histogram3 = new Histogram(getGaussianData(2500), 20, -20, 20);

		// X축
		Calendar calendar = Calendar.getInstance();
		Date aa;

		List<Date> xAxis = new ArrayList<Date>(7);

		for (int i = 0; i < 7; i++) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE) + i;
			xAxis.add(aa = new Date((year - 1900), month, day));
		}

		System.out.println(xAxis);

		// Y축
		List<Double> yAxis = new ArrayList<Double>(7);
		Random r = new Random();
		for (int i = 0; i < 7; i++) {
			double bbc = r.nextDouble();
			// double bbc = 0;

			yAxis.add(bbc * 10.0);
		}
		System.out.println(yAxis);

		System.out.println(yAxis.get(0));
		// X축
		// Calendar calendar = Calendar.getInstance();
		Date bb;

		List<Date> xAxis_DUP = new ArrayList<Date>(7);

		for (int i = 0; i < 7; i++) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE) + i;
			xAxis_DUP.add(aa = new Date((year - 1900), month, day));
		}

		System.out.println(xAxis_DUP);

		// Y축
		List<Double> yAxis_DUP = new ArrayList<Double>(7);
		Random r_DUP = new Random();
		for (int i = 0; i < 7; i++) {
			double bbc = r_DUP.nextDouble() * 15.0;
			// double bbc = 0 ;

			if (yAxis.get(i) > bbc) {
				bbc += yAxis.get(i);
//				yAxis_DUP.add(bbc);	
			}else{
//				yAxis_DUP.add(bbc);	
			}
			yAxis_DUP.add(bbc);	
			
		}
		System.out.println(yAxis_DUP);
		
		
		Date cc;

		List<Date> xAxis_DUP1 = new ArrayList<Date>(7);

		for (int i = 0; i < 7; i++) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE) + i;
			xAxis_DUP1.add(aa = new Date((year - 1900), month, day));
		}

		System.out.println(xAxis_DUP1);

		// Y축
		List<Double> yAxis_DUP1 = new ArrayList<Double>(7);
		Random r_DUP1 = new Random();
		for (int i = 0; i < 7; i++) {
			double bbc = r_DUP1.nextDouble() * 15.0;
			// double bbc = 0 ;

			if (yAxis_DUP.get(i) > bbc) {
				bbc += yAxis_DUP.get(i);
//				yAxis_DUP.add(bbc);	
			}else{
//				yAxis_DUP.add(bbc);	
			}
			yAxis_DUP1.add(bbc);	
			
		}
		System.out.println(yAxis_DUP1);
		chart.addSeries("HYUNJINPARK_EMS1", xAxis_DUP1, yAxis_DUP1);
		chart.addSeries("HEONILPARK_EMS2", xAxis_DUP, yAxis_DUP);
		chart.addSeries("HYUNJINPARK_EMS3", xAxis, yAxis);
		
		chart.addSeries("HYUNJINPARK_EMS4", xAxis_DUP1, yAxis_DUP1);
		chart.addSeries("HEONILPARK_EMS5", xAxis_DUP, yAxis_DUP);
		chart.addSeries("HYUNJINPARK_EMS6", xAxis, yAxis);
		chart.addSeries("HYUNJINPARK_EMS7", xAxis_DUP1, yAxis_DUP1);
		chart.addSeries("HEONILPARK_EMS8", xAxis_DUP, yAxis_DUP);
		chart.addSeries("HYUNJINPARK_EMS9", xAxis, yAxis);
		chart.addSeries("HYUNJINPARK_EMS10", xAxis_DUP1, yAxis_DUP1);
		chart.addSeries("HEONILPARK_EMS11", xAxis_DUP, yAxis_DUP);
		chart.addSeries("HYUNJINPARK_EMS12", xAxis, yAxis);
		
		
		chart.addSeries("HYUNJINPARK_EMS13", xAxis_DUP1, yAxis_DUP1);
		chart.addSeries("HEONILPARK_EMS14", xAxis_DUP, yAxis_DUP);
		chart.addSeries("HYUNJINPARK_EMS15", xAxis, yAxis);
		chart.addSeries("HYUNJINPARK_EMS16", xAxis_DUP1, yAxis_DUP1);
		chart.addSeries("HEONILPARK_EMS17", xAxis_DUP, yAxis_DUP);
		chart.addSeries("HYUNJINPARK_EMS18", xAxis, yAxis);
		
		chart.addSeries("HYUNJINPARK_EMS19", xAxis_DUP1, yAxis_DUP1);
		chart.addSeries("HEONILPARK_EMS20", xAxis_DUP, yAxis_DUP);
		chart.addSeries("HYUNJINPARK_EMS2S1", xAxis, yAxis);
		
//		chart.addSeries("HYUNJINPARK_EMS3", xAxis, yAxis);
		
		// chart.addSeries("DONGSUNGSEO_EMS", histogram3.getxAxisData(),
		// histogram3.getyAxisData());
		// chart.addSeries("HYUNJINPARK_EMS", histogram2.getxAxisData(),
		// histogram2.getyAxisData());
		// chart.addSeries("HEONILPARK_EMS", histogram1.getxAxisData(),
		// histogram1.getyAxisData());

		//
		// System.out.println(histogram1.getyAxisData());
		// System.out.println(histogram2.getyAxisData());
		// System.out.println(histogram3.getyAxisData());
		//
		//
		
		
		chart.getStyler().setLegendSeriesLineLength(30);


		return chart;
	}

	private List<Double> getGaussianData(int count) {

		List<Double> data = new ArrayList<Double>(count);
		Random r = new Random();
		for (int i = 0; i < count; i++) {
			data.add(r.nextGaussian() * 10);
		}
		return data;
	}

}