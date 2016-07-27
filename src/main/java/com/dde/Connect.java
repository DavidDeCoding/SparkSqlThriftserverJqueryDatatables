package com.dde;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class Connect
{
	static {
		try 					{ Class.forName("org.apache.hive.jdbc.HiveDriver"); }
		catch (Exception ex) 	{ ex.printStackTrace(); System.exit(1); }
	}
	
	Connection conn;
	static Connect instance;
	
	Connect() throws Exception
	{
		conn = DriverManager.getConnection("jdbc:hive2://192.168.1.17:10000", "", "");
	}
	
	Connection getConnection()
	{
		return conn;
	}
	
	public static Connect getInstance() throws Exception
	{
		if (instance == null) instance = new Connect();
		return instance;
	}
	
	public ResultSet execute(String query) throws Exception
	{
		return conn.createStatement().executeQuery( query );
	}

	public ResultSet getAllCountriesAndCities() throws Exception
	{
		return execute("SELECT DISTINCT country, city FROM g_weather");
	}

	public ResultSet getBy(String country) throws Exception
	{
		return execute("SELECT DISTINCT country, city, average_temperature, date FROM g_weather WHERE country = '" + country + "' LIMIT 200");
	}

	public ResultSet getBy(String country, List<String> listOfcities) throws Exception
	{
		String cities = listOfcities.stream().map( city -> "'" + city + "'" ).collect(Collectors.joining(","));
		return execute("SELECT DISTINCT country, city, average_temperature, date FROM g_weather WHERE country = '" + country + "' AND city IN (" + cities + ") LIMIT 200");
	}
}