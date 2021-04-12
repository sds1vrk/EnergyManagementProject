package com.mir.ven;

public class VenMain extends ISO8601 {

	int pollFrequency;

	VenMain() {
		pollFrequency = parseDutrationTotalSeconds("PT5S");
		System.out.println(pollFrequency);
	}

	public static void main(String[] args) {

		new VenMain();
		
		String a,b;
		
		a=  new Global().VEN_NAME;
	}
}
