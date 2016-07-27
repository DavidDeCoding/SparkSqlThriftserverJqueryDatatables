 /**
 * Created by daviddecoding on 7/26/16.
 */
 package com.dde;

import java.sql.*;
import java.util.*;
import javax.sql.*;

import groovy.lang.Tuple;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LocalDB {

    private static String INSERT_STATEMENT          = "INSERT INTO country_city (country, city) VALUES (:country, :city)";
    private static String COUNTRY_SELECT_STATEMENT  = "SELECT DISTINCT country FROM country_city";
    private static String CITY_SELECT_STATEMENT     = "SELECT DISTINCT city FROM country_city WHERE country = ':country'";
    private NamedParameterJdbcTemplate db;
    private static LocalDB instance;

    public LocalDB()
    {
        org.springframework.context.ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:datasource_context.xml");
        db = new NamedParameterJdbcTemplate((DataSource) ctx.getBean("dataSource"));
    }

    public void insert(String country, String city) throws Exception
    {
        Map<String, String> params = new HashMap<>();
        params.put("country", country);
        params.put("city", city);
        db.update(INSERT_STATEMENT, params);
    }

    public List<String> findCities(String country) throws Exception
    {
        return db.query(CITY_SELECT_STATEMENT.replace(":country", country), new HashMap<String, Object>(), new ListMapper());
    }

    public List<String> findAllCountries() throws Exception
    {
        return db.query(COUNTRY_SELECT_STATEMENT, new HashMap<String, Object>(), new ListMapper());
    }

    static LocalDB getInstance() throws Exception
    {
        if (instance == null) instance = new LocalDB();
        return instance;
    }

    public void populateCountriesAndCities() throws Exception
    {
        try
        {
            ResultSet rs = Connect.getInstance().getAllCountriesAndCities();
            while (rs.next())
            {
                getInstance().insert(rs.getString("country"), rs.getString("city"));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    static class ListMapper implements RowMapper<String>
    {
        public String mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return rs.getString(1);
        }
    }
}
