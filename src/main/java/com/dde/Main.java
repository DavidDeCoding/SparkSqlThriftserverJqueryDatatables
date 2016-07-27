package com.dde;

import static spark.Spark.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.collections.map.HashedMap;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.*;

public class Main
{
	
	public static void main(String[] args) throws Exception
	{
		bootstrap();

		get("/", (req, res) -> new ModelAndView(getHome(), "home.hbs"), new HandlebarsTemplateEngine());
		get("/data", (req, res) -> data(req.body()));
		get("/countries", (req, res) -> countries());
		get("/cities", (req, res) -> cities(req.body()));
	}

	static void bootstrap() throws Exception
	{
		LocalDB.getInstance().populateCountriesAndCities();
	}
	
	static Map<String, Object> getHome()
	{
		Map<String, Object> jobs = new HashMap<>();
        jobs.put("Title", "Jobs: ");
        return jobs;
	}


	static Map<String, Object> countries() throws Exception
	{
		Map<String, Object> countries = new HashMap<>();
		countries.put("data", LocalDB.getInstance().findAllCountries());
		return countries;
	}

	static Map<String, Object> cities(String body) throws Exception
	{
		Map<String, Object> cities = new HashMap<>();
		cities.put("data", LocalDB.getInstance().findCities(body));
		return cities;
	}

	static String data(String body) throws Exception
	{
		Map<String, Object> temp = new HashMap<>();
		List<Object> data = new ArrayList<>();

		ResultSet rs = Connect.getInstance().getBy("country");
		while (rs.next()) data.add(Arrays.asList(new Object[] {rs.getString("country"), rs.getString("city"), rs.getFloat("average_temperature")}));

		temp.put("data", data);
		return dataToJson(temp);
	}
	
	static String dataToJson(Map<String, Object> object)
	{
		try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, object);
            return sw.toString();
        } catch (IOException e){
            throw new RuntimeException("IOException from a StringWriter?");
        }
	}
}