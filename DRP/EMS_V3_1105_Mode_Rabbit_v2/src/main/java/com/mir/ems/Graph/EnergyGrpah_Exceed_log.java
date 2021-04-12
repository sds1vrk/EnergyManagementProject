package com.mir.ems.Graph;




import java.io.File;
import java.io.FileWriter;

import com.mir.ems.globalVar.*;

import scala.util.Try;

public class EnergyGrpah_Exceed_log {
	
	String log_message;
	
//	public EnergyGrpah_Exceed_log(String log) {
//		
//		this.log_message=log;
//		
//	}
	
	
	
	File file=new File("exceed_log.txt");
	FileWriter writer=null;
	
	
	int i=0;
	
	public void write_log(String string) {
		
		log_message=string;
		
		
		try {
			
			
			writer=new FileWriter(file, true);
			writer.write(log_message+"\n");
			writer.flush();
			
//			System.out.println("LOG_COMPLETE_DONE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(writer!=null) {
					writer.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
