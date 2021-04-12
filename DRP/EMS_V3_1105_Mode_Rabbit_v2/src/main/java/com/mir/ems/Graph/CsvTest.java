
package com.mir.ems.Graph;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.io.FileOutputStream;

import java.io.*;

public class CsvTest {

//	public static void main(String[] args) {

//		String enc=new java.io.OutputStreamWriter(System.out).getEncoding();

//		System.out.println("this en");
	
	String csvFileName = "data.csv";
//	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName), "UTF8"));
	
	BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName,true));



	public CsvTest() throws IOException {
		
		System.out.println("여기들어옴?!");
		
		Calendar now = Calendar.getInstance();

		int sYear = now.get(Calendar.YEAR);
		int sMonth = now.get(Calendar.MONTH) + 1;
		int sDate = now.get(Calendar.DATE);
		String strYMD = sYear + "" + sMonth + "" + sDate;
		
		
//		String firstRow = "Time:"+strYMD+"," +"Total Used \r\n";
		String firstRow = "Time:"+strYMD+"," +"Total Used"+","+"SEMA1 USED"+","+"SEMA2 USED"+","+"SEMA3 USED"+","+"SEMA1 Threshold"+","+"SEMA2 Threshold"+","+"SEMA3 Threshold   \r\n";
		
		writer.write(firstRow);

		
		//System.out.println("firstROW"+firstRow);
	}

	public void csvSave(String data) throws UnsupportedEncodingException, FileNotFoundException {
		
//		String csvFileName = "D:\\data/csv.csv";
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName), "UTF8"));

		try {

			
//			System.out.println("들어옴?@");
			
			writer.write(data);

			writer.flush();

		}

		catch (

		Exception e) {
			e.printStackTrace();
		}

//		finally {
//			try {
//				if (writer != null) {
//					writer.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

	}

//	}

}
