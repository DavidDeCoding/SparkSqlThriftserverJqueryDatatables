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
import java.util.stream.Collectors;

public class Main
{
	
	public static void main(String[] args) throws Exception
	{
		bootstrap();

		get("/", (req, res) -> new ModelAndView(getHome(), "home.hbs"), new HandlebarsTemplateEngine());
		get("/data", (req, res) -> { res.type("application/json"); return data(req.body()); });
		get("/allcountries", (req, res) -> { res.type("application/json"); return countries(); });
		get("/allcities", (req, res) -> { res.type("application/json"); return cities(req.queryParams("term")); });
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


	static String countries() throws Exception
	{
		Map<String, Object> countries = new HashMap<>();
		countries.put("results", LocalDB.getInstance().findAllCountries().stream().map(country -> filterMap(country)).collect(Collectors.toList()));
		return dataToJson(countries);
	}

	static String cities(String country) throws Exception
	{
		if (country == null || country.isEmpty()) return "";

		Map<String, Object> cities = new HashMap<>();
		cities.put("results", LocalDB.getInstance().findCities(country).stream().map(city -> filterMap(city)).collect(Collectors.toList()));
		return dataToJson(cities);
	}

	static String data(String body) throws Exception
	{
		Map<String, Object> temp = new HashMap<>();
		List<Object> data = new ArrayList<>();

		ResultSet rs = Connect.getInstance().getBy("India");
		while (rs.next()) data.add(Arrays.asList(new Object[] {rs.getFloat("average_temperature"), rs.getDate("date"), rs.getString("city"), rs.getString("country")}));

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

	static Map<String, String> filterMap(String data)
	{
		Map<String, String> m = new HashMap<>();
		m.put("id", data);
		m.put("text", data);
		return m;
	}
}