package com.mir.vtn.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {


	public String url = "jdbc:mysql://166.104.143.225:3306/mir_smart?user=mir&password=1129&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
	public Connection conn = null;

	public Statement stmt = null;

	public ResultSet rs = null;
	
	
	
	public DatabaseConnection() {

	}
	
	public void start()throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url);
		System.out.println("Connection OK");
		stmt = conn.createStatement();
	}
	
	
}
