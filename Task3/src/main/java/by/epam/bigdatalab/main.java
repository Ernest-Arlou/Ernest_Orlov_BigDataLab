package by.epam.bigdatalab;


import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.DBParameter;
import by.epam.bigdatalab.dao.DBResourceManager;
import by.epam.bigdatalab.service.factory.ServiceFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class main {


    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        props.setProperty("dataSourceClassName",dbResourceManager.getValue(DBParameter.DB_DATA_SOURCE_CLASS_NAME));
        props.setProperty("dataSource.user", dbResourceManager.getValue(DBParameter.DB_USER));
        props.setProperty("dataSource.password", dbResourceManager.getValue(DBParameter.DB_PASSWORD));
        props.setProperty("dataSource.databaseName", dbResourceManager.getValue(DBParameter.DB_NAME));
//        props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig(props);
        HikariDataSource ds = new HikariDataSource(config);


//        HikariDataSource ds = new HikariDataSource();
//        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/crimes");
//        ds.setUsername("postgres");
//        ds.setPassword("1234");
//
        try {
            Connection connection = ds.getConnection();

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.test");

            while (resultSet.next()) {

                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("name"));

            }



            System.out.println(connection.getClientInfo());



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }























//        ServiceFactory.getInstance().getPoliceAPIService().test("E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/LondonStations.csv");











        //        String urlStr = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2019-01";

//        Date date = new GregorianCalendar(2019, 4,1).getTime();
//
//
//        double latitude = 52.629729;
//        double longitude = -1.131592;
//
//
//
//        addParameter("lat", latitude);
//        addParameter("lng", longitude);
//        addParameter("date", Util.formatDate(date));
//
//
//        List<Crime> crimes = Arrays.asList(doRequest("/crimes-street/all-crime", Crime[].class));
//
//
//        System.out.println(buildParameters());
    }







}
