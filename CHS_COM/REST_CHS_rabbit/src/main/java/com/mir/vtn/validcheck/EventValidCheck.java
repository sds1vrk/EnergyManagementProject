package com.mir.vtn.validcheck;

public class EventValidCheck extends Thread{

	String venID;
	public EventValidCheck(String venID) {
	
		this.venID = venID;
		
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		System.out.println(venID);
		
	}
	
}
