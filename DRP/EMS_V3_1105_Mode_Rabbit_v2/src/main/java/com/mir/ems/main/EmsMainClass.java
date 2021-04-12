package com.mir.ems.main;

import java.awt.EventQueue;

import com.mir.ems.GUI.Initial;
import com.mir.ems.GUI.MainFrame;
import com.mir.ems.globalVar.global;


public class EmsMainClass {
	public Initial initial;
	MainFrame mainFrame;

	public EmsMainClass() {
		
//		EmsMainClass main = new EmsMainClass();
//		main.initial = new Initial();
//		main.initial.setMain(main);
//		
		
//		System.out.println("aaa");
		initial = new Initial();
		initial.setMain(this);
		
		
	}

	public void showFrameTest() {
		initial.dispose();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connection connection = new Connection(global.	getProtocol_type_global());
					if (connection.connection_Status) {
						MainFrame frame = new MainFrame();
						frame.setVisible(true);
					} else {	
						EmsMainClass main = new EmsMainClass();	
						main.initial = new Initial();
						main.initial.setMain(main);
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
	