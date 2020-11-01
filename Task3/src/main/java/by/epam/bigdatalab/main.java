package by.epam.bigdatalab;


import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.service.factory.ServiceFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class main {


    public static void main(String[] args) throws Exception {

        ServiceFactory.getInstance().getPoliceAPIService().test("E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/LondonStations.csv");











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
