package com.mir.ems.globalVar;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimpleFileWriterExample {
	public static void main(String[] args) {
		String name = "John Doe";
		int age = 44;
		double temp = 26.9;
		FileWriter fw;
		try {
			fw = new FileWriter(new File("RegisteredEMAList.cfg"));
			
			fw.write(String.format("My name is %s.",name));
			fw.write(System.lineSeparator()); //new line
			fw.write(String.format("I am %d years old.",age));
			fw.write(System.lineSeparator()); //new line
			fw.write(String.format("Today's temperature is %.2f.",temp));
			fw.write(System.lineSeparator()); //new line
			fw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println("Done");
		
	}

}