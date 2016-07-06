package com.dde;

import static spark.Spark.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

class Main
{
	
	public static void main(String[] args)
	{
		/*Connect cnt = Connect.getInstance()
		def rs = cnt.execute("SELECT city, MAX(avg_temp) FROM global_temp_by_city_df WHERE country = 'India' GROUP BY city")
		while (rs.next())
		{
			println("The city: " + rs.getString(1) + ", Avg Temp: " + rs.getString(2))
		}*/
/*		staticFileLocation("/public")*/
		get("/", {req, res -> new ModelAndView(getHome(), "home.hbs")}, new HandlebarsTemplateEngine());
		get("/data", {req, res ->
			println req
			return getTemp()
		})
	}
	
	static def getHome()
	{
		Map<String, Object> jobs = new HashMap<>();
        jobs.put("Title", "Jobs: ");
        return jobs;
	}
	
	static def getTemp()
	{
		Map temp = [:]
		temp["data"] = ["Hello", "World"]
		return dataToJson(temp)
	}
	
	static def dataToJson( object )
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