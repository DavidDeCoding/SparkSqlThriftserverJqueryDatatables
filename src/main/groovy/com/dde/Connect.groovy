package com.dde

import java.sql.*

class Connect
{
	static { Class.forName('org.apache.hive.jdbc.HiveDriver') }
	
	Connection conn
	static Connect instance
	
	Connect() throws Exception
	{
		conn = DriverManager.getConnection("jdbc:hive2://192.168.1.11:10000", "", "")
	}
	
	Connection getConnection()
	{
		return conn
	}
	
	static def getInstance()
	{
		if (!instance) instance = new Connect()
		return instance
	}
	
	def execute( query ) throws Exception
	{
		return conn.createStatement().executeQuery( query )
	}
}